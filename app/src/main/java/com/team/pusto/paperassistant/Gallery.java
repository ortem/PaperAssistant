package com.team.pusto.paperassistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.team.pusto.paperassistant.classifierengine.Classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.team.pusto.paperassistant.MainActivity.decodeSampledBitmapFromFile;

public class Gallery extends AppCompatActivity {

    public GridView gridView;
    public GridViewAdapter gridAdapter;
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 324;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);
    }

    private ArrayList<ImageItem> getData() {
        File photosDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(photosDir.listFiles()));

        Classifier classifier = new Classifier();
        classifier.addPhotos(files);
        ArrayList<File> paperFiles = classifier.getPapers();

        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < paperFiles.size(); i++) {
            Bitmap bmp = decodeSampledBitmapFromFile(files.get(i).getAbsolutePath(), 200, 200);
            imageItems.add(new ImageItem(bmp, "Image#" + i));
        }
        return imageItems;
    }
}
