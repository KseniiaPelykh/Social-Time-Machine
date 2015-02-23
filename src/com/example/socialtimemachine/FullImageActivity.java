package com.example.socialtimemachine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.socialtimemachine.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FullImageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        Intent i = getIntent();
        String partId = i.getExtras().getString("PartId");
        ParseObject move = new ParseObject("Part");

        try{
            move = new ParseQuery("Part").get(partId);
        }
        catch (Exception e){
            Log.i("Get part error:", e.toString());
        }

        ParseImageView imageView = (ParseImageView) findViewById(R.id.full_image_view);
        ParseFile imageFile = move.getParseFile("image");
        if (imageFile != null) {
            imageView.setParseFile(imageFile);
            imageView.loadInBackground();
        }
    }
}