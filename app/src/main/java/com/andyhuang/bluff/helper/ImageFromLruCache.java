package com.andyhuang.bluff.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Constant.Constants;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.concurrent.Executors;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class ImageFromLruCache {
    float roundPx = 0f;
    public void set(ImageView imageView, String imageUrl,float roundPxInput) {
        roundPx = roundPxInput;
        Bitmap bitmap = (Bitmap) Bluff.getLruCache().get(imageUrl);

        if (bitmap == null) {
            Log.d(Constants.TAG, "LruCache doesn't exist, start download.: " + imageUrl);
            //download the image from internet
            new DownloadImageTask(imageView, imageUrl).executeOnExecutor(Executors.newCachedThreadPool());
        } else {
            Log.d(Constants.TAG, "LruCache exist, set bitmap directly.: " + imageUrl);
            imageView.setImageBitmap(getRoundedCornerBitmap(bitmap,roundPx));
        }

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeBitmap(String url, int maxWidth){

        Bitmap bitmap = null;
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxWidth);

            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (MalformedInputException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private class DownloadImageTask extends AsyncTask {
        private ImageView mImageView;
        private String mImageUrl;
        private Bitmap mBitmap;
        public DownloadImageTask(ImageView imageView, String imageUrl) {
            mImageView = imageView;
            mImageUrl = imageUrl;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            mBitmap = decodeBitmap(mImageUrl, 200);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (mBitmap != null) {
                Bluff.getLruCache().put(mImageUrl, mBitmap);
                if (mImageView.getTag() == mImageUrl) {
                    //set the image with round corner
                    mImageView.setImageBitmap(getRoundedCornerBitmap(mBitmap,roundPx));
                }
            }
        }
    }
}
