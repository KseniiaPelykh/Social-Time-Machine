package com.example.socialtimemachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.model.GraphUser;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AcceptGameActivity extends Activity {
    ParseObject game;
    ProgressDialog mProgressDialog;
    private static String gameId;
    static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
    static DateFormat timeFormatter = new SimpleDateFormat("hh:mm");
    static String userId;
    public static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            gameId = extras.getString("GameId");
            userId = extras.getString("UserId");
        }

        setContentView(R.layout.activity_accept_game);

        Button start = (Button) findViewById(R.id.accept_game_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGame(v);
            }
        });

        ImageView iconLoadImage = (ImageView) findViewById(R.id.accept_game_add_photo);
        iconLoadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        new RemoteDataTask().execute();
    }

    public void saveGame(View view){
        EditText gameDescription = (EditText)findViewById(R.id.accept_game_description);
        ImageView gameImage = (ImageView)findViewById(R.id.accept_game_image);

        if (!gameDescription.getText().toString().matches("") &&
             gameImage.getDrawable() != null &&
             !userId.matches("")) {

            ParseObject newpart = new ParseObject("Part");
            Bitmap bitmap = ((BitmapDrawable) gameImage.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ParseFile file = new ParseFile("picturePath.png", stream.toByteArray());
            newpart.put("image", file);
            newpart.put("gameParent",game);
            newpart.put("description", gameDescription.getText().toString());
            newpart.put("user", userId);
            newpart.saveInBackground();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String picturePath = cursor.getString(columnIndex);

            File image = new File(picturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            ImageView gameImage = (ImageView) findViewById(R.id.accept_game_image);
            gameImage.setImageBitmap(bitmap);
        }
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AcceptGameActivity.this);

            // Set progressdialog title
            mProgressDialog.setTitle("History");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);

            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                game = new ParseQuery("Game")
                        .get(gameId);
            }
            catch (Exception e){
                Log.i("Get game by id:", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            TextView titleTextView = (TextView) findViewById(R.id.accept_game_title);
            titleTextView.setText(game.getString("gameTitle"));

            TextView startDate = (TextView) findViewById(R.id.accept_game_date_start);
            startDate.setText(dateFormatter.format(game.getDate("startDate")));

            TextView endDate = (TextView) findViewById(R.id.accept_game_date_end);
            endDate.setText(dateFormatter.format(game.getDate("endDate")));

            TextView startTime = (TextView) findViewById(R.id.accept_game_time_start);
            startTime.setText(timeFormatter.format(game.getDate("startTime")));

            TextView endTime = (TextView) findViewById(R.id.accept_game_time_end);
            endTime.setText(timeFormatter.format(game.getDate("endTime")));

            // Close the progressdialog
            mProgressDialog.dismiss();

        }
    }

}
