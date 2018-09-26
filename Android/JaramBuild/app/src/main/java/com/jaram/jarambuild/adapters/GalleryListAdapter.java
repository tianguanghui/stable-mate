package com.jaram.jarambuild.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaram.jarambuild.R;
import com.jaram.jarambuild.models.GalleryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder>  implements Filterable
{

    private String TAG = "GalleryAdapter";
    private LayoutInflater inflater;
    public static List<GalleryModel> imageListForGallery;
    public List<GalleryModel> imageListFiltered;
    CustomGalleryFilter filter;


    public GalleryListAdapter(Context ctx, List<GalleryModel> imageListForGallery)
    {

        inflater = LayoutInflater.from(ctx);
        GalleryListAdapter.imageListForGallery = imageListForGallery;
        imageListFiltered = imageListForGallery;
    }

    @Override
    public GalleryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.activity_gallery_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final GalleryListAdapter.MyViewHolder holder, final int position)
    {

        holder.titleTV.setText("Title: " + imageListForGallery.get(position).getTitle());
        holder.descTV.setText("Description: " + imageListForGallery.get(position).getDescription());

        //get date data
        String epochDateString = imageListForGallery.get(position).getDate();
        Log.d(TAG, "Epoch Date: " + epochDateString);

        String convertedDate = "Unavailable";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        convertedDate = sdf.format(new Date(Long.parseLong(epochDateString)));

        holder.dateTV.setText("Date: " + convertedDate);
        //get img data
        Bitmap bitmap = decodeSampledBitmapFromFilePath(imageListForGallery.get(position).getPhotoPath_edited(), 100, 100);
        holder.imgPreview.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount()
    {
        return imageListForGallery.size();
    }

    @Override
    public Filter getFilter()
    {
        if(filter==null)
        {
            //filter=new CustomGalleryFilter(filterList,this);
        }

        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        protected TextView titleTV;
        protected TextView descTV;
        protected TextView dateTV;
        protected TextView locationTV;
        protected ImageView imgPreview;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTv);
            descTV = itemView.findViewById(R.id.descTv);
            dateTV = itemView.findViewById(R.id.dateTv);
            imgPreview = itemView.findViewById(R.id.imgPreview);

            imgPreview.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    // Code here executes on main thread after user presses button
                    Log.d(TAG, "img clicked");

                }
            });
        }
    }


    //to avoid loading a large bitmap, images are down sampled, this calculates required sample size
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    //decodeFile(String pathName, BitmapFactory.Options opts)

    public static Bitmap decodeSampledBitmapFromFilePath(String pathname,
                                                         int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathname, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathname, options);
    }
}
