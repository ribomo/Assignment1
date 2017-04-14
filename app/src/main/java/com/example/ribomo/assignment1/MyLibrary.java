package com.example.ribomo.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class MyLibrary extends AppCompatActivity {

    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);

        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        final ArrayList<File> files = new ArrayList<File>(Arrays.asList(storageDir.listFiles()));
        FileAdapter itemsAdapter = new FileAdapter(this, files);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final File file = files.get(position);
                String type = URLConnection.guessContentTypeFromName(file.getName());

                Uri data = FileProvider.getUriForFile(view.getContext(), "com.example.android.fileprovider", file);

                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                newIntent.setDataAndType(data, type);
                view.getContext().startActivity(newIntent);
            }
        });
        listView.setAdapter(itemsAdapter);
    }
}
