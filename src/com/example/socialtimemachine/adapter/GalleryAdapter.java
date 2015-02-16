package com.example.socialtimemachine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialtimemachine.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class GalleryAdapter extends ParseQueryAdapter {
    static String partId;

    public GalleryAdapter(Context context, final String userId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {

            public ParseQuery create() {
                partId = userId;

                ParseQuery query = new ParseQuery("Game")
                        .whereEqualTo("users", userId);

                query.include("firstPart");

                return query;
            }
        });
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.gallery_row_grid, null);
        }

        super.getItemView(object, v, parent);
        TextView titleTextView = (TextView) v.findViewById(R.id.gallery_row_grid_title);
        titleTextView.setText(object.getString("gameTitle"));

        // Download and Add the image
        ParseImageView image = (ParseImageView) v.findViewById(R.id.gallery_row_grid_image);
        ParseFile imageFile = object.getParseObject("firstPart").getParseFile("image");
        if (imageFile != null) {
            image.setParseFile(imageFile);
            image.loadInBackground();
        }

        return  v;
    }
}


