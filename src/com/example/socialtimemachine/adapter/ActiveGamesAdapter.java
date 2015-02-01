package com.example.socialtimemachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialtimemachine.AcceptGameActivity;
import com.example.socialtimemachine.GetActiveGamesActivity;
import com.example.socialtimemachine.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActiveGamesAdapter extends ParseQueryAdapter {

    static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
    static DateFormat timeFormatter = new SimpleDateFormat("hh:mm");
    static String partId;

    public ActiveGamesAdapter(Context context, final String userId) {
         super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
             public ParseQuery create() {
                 partId = userId;
                 ParseQuery query = new ParseQuery("Game")
                         .whereEqualTo("users", userId);
                 return query;
             }
         });
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.active_game_item, null);
        }

        super.getItemView(object, v, parent);
        TextView titleTextView = (TextView) v.findViewById(R.id.active_game_title);
        titleTextView.setText(object.getString("gameTitle"));

        TextView startDate = (TextView) v.findViewById(R.id.active_game_date_start);
        startDate.setText(dateFormatter.format(object.getDate("startDate")));

        TextView endDate = (TextView) v.findViewById(R.id.active_game_date_end);
        endDate.setText(dateFormatter.format(object.getDate("endDate")));

        TextView startTime = (TextView) v.findViewById(R.id.active_game_time_start);
        startTime.setText(timeFormatter.format(object.getDate("startTime")));

        TextView endTime = (TextView) v.findViewById(R.id.active_game_time_end);
        endTime.setText(timeFormatter.format(object.getDate("endTime")));

        final Button accept = (Button) v.findViewById(R.id.accept_button);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acceptGame = new Intent( getContext(), AcceptGameActivity.class);
                acceptGame.putExtra("GameId", object.getObjectId());
                acceptGame.putExtra("UserId", partId);
                getContext().startActivity(acceptGame);
            }
        });
        return  v;
    }
}
