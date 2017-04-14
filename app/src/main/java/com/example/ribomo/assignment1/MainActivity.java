package com.example.ribomo.assignment1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1000;
    boolean isRecording;
    MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRecording = false;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("111111", "Permission Accepted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("111111", "Permission Denied");

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Codes.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void dispatchRecordVideoIntent(View v) {
        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (recordVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {

            }
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        videoFile);
                recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(recordVideoIntent, Codes.REQUEST_VIDEO_RECORD);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PICTURE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = video.getAbsolutePath();
        return video;
    }

    private File createVoiceFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VOICE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File voice = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp3",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = voice.getAbsolutePath();
        return voice;
    }


    public void go2MyLibrary(View v) {
        Intent intent = new Intent(MainActivity.this, MyLibrary.class);
        startActivity(intent);
    }

    public void voiceRecorder(View v) {
        final ImageButton recordButton = (ImageButton) findViewById(R.id.imageButtonRecord);
        if (!isRecording) {
            File voiceFile = null;
            try {
                voiceFile = createVoiceFile();
            } catch (IOException ex) {

            }
            if (voiceFile != null) {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                Log.d("file path", voiceFile.getPath());
                mediaRecorder.setOutputFile(voiceFile.getPath());
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    recordButton.setImageResource(R.drawable.ic_stop_black_24dp);
                    isRecording = true;
                } catch (IOException ex) {
                    Log.e("Main", ex.toString());
                }
            }
        } else {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            recordButton.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
            isRecording = false;
        }

    }
}
