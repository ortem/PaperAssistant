package com.team.pusto.paperassistant;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import org.opencv.core.Size;

import org.opencv.android.OpenCVLoader;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {
    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageView.setImageResource(R.drawable.not32);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public void doScience() {
        File photosDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
        File[] files1 = photosDir.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < files1.length && i < 20; i++)
            files.add(files1[i]);

        Bitmap bmp = decodeSampledBitmapFromFile(files.get(0).getAbsolutePath(), 200, 200);
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bmp, imageMat);

        /*
        InputStream stream = null;
        Uri path = Uri.parse("android.resource://com.team.pusto.paperassistant/" + R.drawable.not32);
        try {
            stream = getContentResolver().openInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmp = BitmapFactory.decodeStream(stream, null, bmpFactoryOptions);


        Mat imageMat = new Mat();
        Utils.bitmapToMat(bmp, imageMat);

        // calculate histogram
        Mat hist = new Mat();
        ArrayList<Mat> imageList = new ArrayList<Mat>();
        imageList.add(imageMat);
        Imgproc.calcHist(imageList, new MatOfInt(0, 1, 2), null, hist, new MatOfInt(8, 8, 8),
                new MatOfFloat(0, 256, 0, 256, 0, 256));
        */

        // do smth with mat
        Mat newMat = new Mat();
        Imgproc.cvtColor(imageMat, newMat, Imgproc.COLOR_RGB2GRAY);

        // convert to bitmap:
        Bitmap bm = Bitmap.createBitmap(newMat.cols(), newMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(newMat, bm);

        // find the imageview and draw it!
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.setImageBitmap(bm);
    }

    public void buttonOnClick(View view) {
        doScience();
    }
}