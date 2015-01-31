package com.example.socialtimemachine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;


public class NewGameActivity extends FragmentActivity {
    static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
    static DateFormat timeFormatter = new SimpleDateFormat("hh:mm");
    private static List<GraphUser> selectedUsers;

    public List<GraphUser> getSelectedUsers(){
        return selectedUsers;
    }

    public static void setSelectedUsers(List<GraphUser> users){
        selectedUsers = users;
    }

    public static class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a nea instance of TimePickerDialog and return it
            return  new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void  onTimeSet(TimePicker view, int hourOfDay, int minute){
            TextView time = (TextView) getActivity().findViewById(R.id.start_time);
            updateTime(time, hourOfDay, minute);
        }
    }

    public static class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a nea instance of TimePickerDialog and return it
            return  new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void  onTimeSet(TimePicker view, int hourOfDay, int minute){
            TextView time = (TextView) getActivity().findViewById(R.id.end_time);
            updateTime(time, hourOfDay, minute);
        }
    }

    static void updateTime(TextView timeView, int mHour, int mMinute){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, mHour);
        cal.set(Calendar.MINUTE, mMinute);
        Date date = cal.getTime();
        timeView.setText(timeFormatter.format(date));
    }

    public static class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            TextView dateView = (TextView) getActivity().findViewById(R.id.start_date);
            updateDate(dateView, year, month, day);
        }
    }

    public static class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            TextView dateView = (TextView) getActivity().findViewById(R.id.end_date);
            updateDate(dateView, year, month, day);
        }
    }

    static void updateDate(TextView dateView, int mYear, int mMonth, int mDay){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        Date date = cal.getTime();
        dateView.setText(dateFormatter.format(date));
    }
	
	public static final int RESULT_LOAD_IMAGE = 1;
	public static final int REAUTH_ACTIVITY_CODE = 2;
	private static final String EMPTY_STRING = "";
	private String userId = "";
	private UiLifecycleHelper uiHelper;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
		
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");
		setContentView(R.layout.activity_newgame);
		
		ImageView iconLoadImage = (ImageView) findViewById(R.id.add_photo);
		iconLoadImage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0){
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        View.OnClickListener timeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        };

        TextView startTimeView = (TextView) findViewById(R.id.start_time);
        updateTime(startTimeView, mHour, mMinute);
        startTimeView.setOnClickListener(timeListener);

        TextView endTimeView = (TextView) findViewById(R.id.end_time);
        updateTime(endTimeView, mHour, mMinute);
        endTimeView.setOnClickListener(timeListener);

        View.OnClickListener dateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDatePickerDialog(v);
            }
        };

        TextView startDateView = (TextView) findViewById(R.id.start_date);
        updateDate(startDateView, mYear, mMonth, mDay);
        startDateView.setOnClickListener(dateListener);

        TextView endDateView = (TextView) findViewById(R.id.end_date);
        updateDate(endDateView, mYear, mMonth, mDay);
        endDateView.setOnClickListener(dateListener);

    }

    private void showTimePickerDialog(View v){
        DialogFragment newFragment;

        if (v.getId() == R.id.start_time) {
            newFragment = new StartTimePickerFragment();
        }
        else {
            newFragment = new EndTimePickerFragment();
        }

        newFragment.show(this.getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment;
        if (v.getId() == R.id.start_date) {
            newFragment = new StartDatePickerFragment();
        }
        else {
            newFragment = new EndDatePickerFragment();
        }

        newFragment.show(this.getFragmentManager(), "datePicker");
    }

    public void saveGame(View view){
        EditText gameTitle = (EditText)findViewById(R.id.game_title);
        EditText gameDescription = (EditText)findViewById(R.id.game_description);
        TextView startDateView = (TextView)findViewById(R.id.start_date);
        TextView endDateView = (TextView)findViewById(R.id.end_date);
        TextView startTimeView = (TextView)findViewById(R.id.start_time);
        TextView endTimeView = (TextView)findViewById(R.id.end_time);
        ImageView gameImage = (ImageView)findViewById(R.id.imgView);
        setUserId();

        Date startDate = new Date();
        Date endDate = new Date();
        Date startTime = new Date();
        Date endTime = new Date();

        try{
            String startText = startDateView.getText().toString();
            startDate = dateFormatter.parse(startText);
            String endText = endDateView.getText().toString();
            endDate = dateFormatter.parse(endText);
            String startTimeText = startTimeView.getText().toString();
            startTime = timeFormatter.parse(startTimeText);
            String endTimeText = endTimeView.getText().toString();
            endTime = timeFormatter.parse(endTimeText);
            Calendar cal = Calendar.getInstance();
        }
        catch (ParseException e){
            e.printStackTrace();
            Log.i("Date : ", e.toString());
        };

        List<String> usersIds = new ArrayList<String>();
        if (getSelectedUsers() != null){
            for (GraphUser graphUser : getSelectedUsers()){
                usersIds.add(graphUser.getId());
            };
        }

         if (!gameTitle.getText().toString().matches("") &&
                !gameDescription.getText().toString().matches("") &&
                startDate != null &&
                endDate != null &&
                startTime != null &&
                endTime != null &&
                gameImage.getDrawable() != null &&
                !userId.matches("") &&
                usersIds != null) {
                    ParseObject newgame = new ParseObject("Game");
                    newgame.put("gameTitle", gameTitle.getText().toString());
                    newgame.put("startDate", startDate);
                    newgame.put("endDate", endDate);
                    newgame.put("startTime", startTime);
                    newgame.put("endTime", endTime);
                    newgame.addAll("users", usersIds);
                    newgame.saveInBackground();

                    // Create Installation query, aggiunge gli utenti veri.
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("userId", userId);

                    // Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
                    push.setMessage("Hai ricevuto una nuova sfida");
                    push.sendInBackground();

                    ParseObject newpart = new ParseObject("Part");
                    Bitmap bitmap = ((BitmapDrawable) gameImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    ParseFile file = new ParseFile("picturePath.png", stream.toByteArray());
                    newpart.put("image", file);
                    newpart.put("gameParent",newgame);
                    newpart.put("description", gameDescription.getText().toString());
                    newpart.put("user", userId);
                    newpart.saveInBackground();

                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
    }

    public void showFriends(View view){
        startPickerActivity(PickerActivity.FRIEND_PICKER, REAUTH_ACTIVITY_CODE);
    }

	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
		
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String picturePath = cursor.getString(columnIndex);
			
			File image = new File(picturePath);
			Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());			
			ImageView gameImage = (ImageView) findViewById(R.id.imgView);
			gameImage.setImageBitmap(bitmap);			
		}
		
		if (requestCode == REAUTH_ACTIVITY_CODE) {
		    } else if (resultCode == Activity.RESULT_OK) {
		    }
	}
	
	private void setUserId(){
		Session session = Session.getActiveSession();		
		
		if (session != null){
			Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {				
				
				@Override 
				public void onCompleted(GraphUser user, Response response){
					if (user != null){
						userId = user.getId();						
					}
				}
			});
			Request.executeBatchAsync(request);
		}		
	}
	
	private void startPickerActivity(Uri data, int requestCode){
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(this, PickerActivity.class);
		startActivityForResult(intent, requestCode);
	}
}
