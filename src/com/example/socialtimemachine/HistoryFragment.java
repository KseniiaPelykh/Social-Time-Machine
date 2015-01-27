package com.example.socialtimemachine;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){

       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_item, container, false);
       ListView games = (ListView) rootView.findViewById(R.id.game_list);
       CustomAdapter mainAdapter = new CustomAdapter(getActivity());
       games.setAdapter(mainAdapter);
       games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // argument position gives the index of item which is clicked
           public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
           }
       });

       return rootView;
   }

}
