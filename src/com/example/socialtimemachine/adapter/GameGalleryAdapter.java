package com.example.socialtimemachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.socialtimemachine.FullImageActivity;
import com.example.socialtimemachine.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.HashMap;
import java.util.List;

public class GameGalleryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // name of games
    private HashMap<String, List<String>> listDataChild;

    public  GameGalleryAdapter(
            Context context,
            List<String> listDataHeader,
            HashMap<String, List<String>> listDataChild)
    {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition){
        return listDataChild
                .get(listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public View getChildView(
            int groupPosition,
            final int childPosition,
            boolean isLastChild,
            View convertView,
            ViewGroup parent){

        final String moveId = (String) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.game_gallery_list_item, null);
        }

        ParseObject move = new ParseObject("Part");

        try {
            move = new ParseQuery("Part")
                    .get(moveId);

            Log.i("Insiede adapter partID: ", moveId);
        }
        catch (Exception e){
            Log.i("Move Error", e.toString());
        }

        ParseImageView image = (ParseImageView) convertView
                .findViewById(R.id.game_gallery_list_image);
        ParseFile imageFile = move.getParseFile("image");
        if (imageFile != null) {
            image.setParseFile(imageFile);
            image.loadInBackground();
        }

        return  convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return  true;
    }

    @Override
    public int getGroupCount(){
        return  this.listDataHeader.size();
    }

    @Override
    public  boolean hasStableIds(){
        return false;
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public Object getGroup(int groupPosition){
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return  this.listDataChild
                    .get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public View getGroupView(
            int groupPosition,
            boolean isExpanded,
            View convertView,
            ViewGroup parent){
        String gameId = (String) getGroup(groupPosition);

        ParseObject game = new ParseObject("Game");
        try {
            game = new ParseQuery("Game").get(gameId);
        }
        catch (Exception e){
            Log.i("Game error", e.toString());
        }

        if (convertView == null){
            LayoutInflater infalInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.gallery_list_group, null);
        }

        TextView lblGameHeader = (TextView) convertView
                .findViewById(R.id.gallery_game_title);

        lblGameHeader.setText(game.getString("gameTitle"));

        return  convertView;
    }

/*
    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullImage = new Intent( getContext(),FullImageActivity.class);
                fullImage.putExtra("PartId", object.getObjectId());
                getContext().startActivity(fullImage);
            }
        });

        return  v;
    }*/
}
