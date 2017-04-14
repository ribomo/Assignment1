package com.example.ribomo.assignment1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ribomo on 3/8/17.
 */

public class FileAdapter extends ArrayAdapter<File>{
    public FileAdapter(Context context, ArrayList<File> files) {
        super(context, 0, files);
    }

    TextToSpeech tts;

    @Nullable
    @Override
    public File getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final File file = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file_adapter, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        Button btnTTS = (Button) convertView.findViewById(R.id.btnTTS);

        String type = URLConnection.guessContentTypeFromName(file.getName());
        if(type == "image/jpeg"){
            imageView.setImageResource(R.drawable.ic_landscape_black_24dp);
        }else if(type == "video/mp4"){
            imageView.setImageResource(R.drawable.ic_ondemand_video_black_24dp);
        }else {
            imageView.setImageResource(R.drawable.ic_music_note_black_24dp);
        }

        textView.setText(file.getName());
        btnTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                 tts = new TextToSpeech(v.getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.US);
                            final String string = file.getName();
                            final String[] strings = string.split("_");
                            final String date = strings[1];
                            final String time = strings[2];
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            Date datetime = null;
                            try{
                                datetime = sdf.parse(date+"_"+time);
                            }catch (Exception ex){
                                Log.e("main", ex.toString());
                            }
                            sdf = new SimpleDateFormat("yyyy MMMM d h 'hours' m 'minutes and' s 'seconds'");
                            final String script =
                                    "Produced in " + sdf.format(datetime);
                            Log.d("Test", script);
                            tts.speak(script, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
