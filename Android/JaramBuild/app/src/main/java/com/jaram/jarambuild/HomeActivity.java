package com.jaram.jarambuild;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;

import com.jaram.jarambuild.utils.TinyDB;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{
    //variables (I leave as global until ready for release, and then convert to local variable)
    private Button cameraBtn;
    private Button calibrateBtn;
    private Button galleryBtn;
    private Button settingsBtn;

    //camera
    public static final int REQUEST_PERMISSION = 200;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_CALIBRATE = 300;
    private String imageFilePath = "";

    //log
    private static final String TAG = "HomeActivity";

    //logged in user
    TinyDB tinydb;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //shared prefs
        tinydb = new TinyDB(this);

        //buttons
        cameraBtn = findViewById(R.id.cameraBtn);
        calibrateBtn = findViewById(R.id.calibrateBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        //register listeners
        cameraBtn.setOnClickListener(this);
        calibrateBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);

        //check permissions (to avoid crashy funtime)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            Log.d(TAG, "Requesting Permissions ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

    //custom menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()== R.id.settingsMenuBtn)
        {
            Log.d(TAG, "Settings Btn Clicked");
            //Go to settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        else if(item.getItemId()== R.id.helpMenuBtn)
        {
            //TODO Make help activity
            Toast.makeText(this, "Help Menu TBC", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Help Btn Clicked");

        }

        else if(item.getItemId()== R.id.logoutMenuBtn)
        {
            Log.d(TAG, "Logout Btn Clicked");
            //set logged in user to null
            tinydb.putString("loggedInAccount", "");
            //return to Login Page
            Intent settingsIntent = new Intent(this, MainActivity.class);
            startActivity(settingsIntent);
            finish();
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cameraBtn:
                //Open Camera
                openCameraIntent("editActivity");
                break;
            case R.id.calibrateBtn:
                //Open Camera
                openCameraIntent("calibrateActivity");
                break;
            case R.id.galleryBtn:
                //Go to gallery activity
                Intent galleryIntent = new Intent(this, GalleryActivity.class);
                startActivity(galleryIntent);
                break;
            case R.id.settingsBtn:
                //Go to settings activity
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }

    void openCameraIntent(String destination)
    {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (pictureIntent.resolveActivity(getPackageManager()) != null)
        {

            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            } catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                if (destination == "editActivity")
                {
                    startActivityForResult(pictureIntent, REQUEST_IMAGE);
                } else
                {
                    startActivityForResult(pictureIntent, REQUEST_CALIBRATE);
                }
                Log.d(TAG, "startAct Uri: " + photoUri);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_IMAGE:
                    if (resultCode == RESULT_OK)
                    {
                        Intent intent = new Intent(HomeActivity.this, CheckCalibrationActivity.class);
                        //add raw file path URI string to intent
                        intent.putExtra("rawPhotoPath", imageFilePath);
                        Log.d(TAG, "rawPhotoPath: " + imageFilePath);
                        //open edit Check calibration Activity
                        startActivity(intent);
                    } else if (resultCode == RESULT_CANCELED)
                    {
                        Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "result cancelled");
                    }
                    break;
                case REQUEST_CALIBRATE:
                    if (resultCode == RESULT_OK)
                    {
                        Intent intent = new Intent(HomeActivity.this, CalibrateActivity.class);
                        //add raw file path URI string to intent
                        intent.putExtra("rawPhotoPath", imageFilePath);
                        Log.d(TAG, "rawPhotoPath: " + imageFilePath);
                        //open edit Image Activity
                        startActivity(intent);
                    } else if (resultCode == RESULT_CANCELED)
                    {
                        Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "result cancelled");
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException
    {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "RAW_" + timeStamp + "_";
        //File storageDir = FileUtils.createFolders();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }
}