package com.example.goplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    //public static Bitmap reduceImageSize(ImageView image){

    //}

    public static Bitmap convertStringToBitmap(String image){
        byte[] byteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static String convertBitmapToString(Bitmap bitmap){
        if(bitmap == null)
            return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromImageView(ImageView ivImage) {
        Drawable drawable = ivImage.getDrawable();
        Bitmap bitmap = null;
        if ( drawable != null && drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        return bitmap;
    }
    public static void uriToImageView(Context context, Uri uri, ImageView ivPhoto) {
        try {
            Glide.with(context)
                    .load(uri)  // Assuming post.getPostImage() returns the image URL
            // Optional: error image if loading fails
            .override(100, 100) // Resize image to 600x600 pixels (adjust this size as necessary)
            .into(ivPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stringToImageView(String image, ImageView targetIV) {
        if (image == null || image.isEmpty()) {
            targetIV.setImageResource(R.drawable.error_image); // Set a default image
            return;
        }

        try {
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            targetIV.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            targetIV.setImageResource(R.drawable.error_image); // Set error image if decoding fails
        }
    }

}
