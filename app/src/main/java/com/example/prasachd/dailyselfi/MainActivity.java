package com.example.prasachd.dailyselfi;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import android.os.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.FileFilter;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private static final File IMAGE_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private static CustomListAdapter mListAdapter;
    private List<MySelfieBean> mSelfieList = new ArrayList<MySelfieBean>();

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private Intent mNotificationIntent;

    private static final int REQUEST_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Get All Images*/
        getPreviousSelfies(mSelfieList);

        mListAdapter = new CustomListAdapter(this, R.layout.listitem, R.id.item_txt, mSelfieList);
        mListAdapter.setNotifyOnChange(true);
        setListAdapter(mListAdapter);

        /*Show Full*/
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent fullImageIntent = new Intent(Intent.ACTION_VIEW);
                fullImageIntent.setDataAndType(Uri.parse("file://" + mSelfieList.get(i).getMPath()), "image/*");
                startActivity(fullImageIntent);
            }
        });

        /* Create Pending Intent for Reminder notification */
        mNotificationIntent = new Intent(MainActivity.this, DailyNotificationReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mNotificationIntent, 0);

        createDailySelfieReminders();
    }


    private void createDailySelfieReminders() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Broadcast the notification intent at specified intervals
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 1000L, 60 * 1000L, mPendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            takePicture();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPreviousSelfies(List<MySelfieBean> mySelfieList) {
        if (IMAGE_DIR.exists() && mySelfieList != null) {
            mySelfieList.clear();
            /* Load selfies from the image directory */
            for (File file : IMAGE_DIR.listFiles(new SelfieFileFilter())) {
                mySelfieList.add(new MySelfieBean(file.getName(), file.getAbsolutePath(), getThumbnail(file.getAbsolutePath())));
            }

        }
    }

    private class SelfieFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.getName().contains("DAILY_SELFIE_");
        }
    }

    private void takePicture() {
        // Dispatch take picture intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check to see if there is an application available to handle capturing images.
        // This is required else if no camera handling application is found, the Selfie app will crash
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the file where the photo should be saved to
            File selfieFile = null;

            try {
                selfieFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (selfieFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(selfieFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    // Create a unique image file name with given prefix
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        String imageFileName = "DAILY_SELFIE_" + timeStamp + "_";

        IMAGE_DIR.mkdirs();

        File image = File.createTempFile(imageFileName, ".jpg", IMAGE_DIR);

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            updateImageList();
        }
    }
    private void updateImageList() {
        getPreviousSelfies(mSelfieList);
        mListAdapter.notifyDataSetChanged();
    }


    private Bitmap getThumbnail(String photoPath) {
        /* Steps Performed in this function
        *  1. Get the dimensions of the View
        *  2. Get the dimensions of the bitmap
        *  3. Determine how much to scale down the image
        *  4. Decode the image file into a Bitmap sized to fill the View
        */

        //Step 1
        int targetW = 100;
        int targetH = 100;

        //Step 2
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bitmapOptions);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        // Step 3
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Step 4
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(photoPath, bitmapOptions);
    }

}
