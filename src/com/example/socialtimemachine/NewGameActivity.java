package com.example.socialtimemachine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

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
import android.view.View;
import android.widget.Button;
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
import com.parse.ParseObject;

import org.w3c.dom.Text;

public class NewGameActivity extends FragmentActivity {

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
        timeView.setText(
                new StringBuilder()
                        .append(mHour)
                        .append(":")
                        .append(mMinute));
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
        dateView.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay)
                .append("/")
                .append(mMonth + 1)
                .append("/")
                .append(mYear)
                .append(" "));
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
		
		/*Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		buttonLoadImage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0){
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});		*/

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
        EditText titleOfGame = (EditText)findViewById(R.id.game_title);
        EditText textOfUser = (EditText)findViewById(R.id.game_description);
        ImageView gameImage = (ImageView)findViewById(R.id.imgView);
        setUserId();

        if (!titleOfGame.getText().toString().matches("") &&
                !textOfUser.getText().toString().matches("") &&
                gameImage.getDrawable() != null && !userId.matches("")) {
            ParseObject newgame = new ParseObject("Game");
            newgame.put("gameTitle", titleOfGame.getText().toString());
            newgame.put("gameText", textOfUser.getText().toString());
            newgame.put("gameUser", userId);
            Bitmap bitmap = ((BitmapDrawable) gameImage.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ParseFile file = new ParseFile("picturePath.png", stream.toByteArray());
            newgame.put("gameImage", file);
            newgame.saveInBackground();

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
		      uiHelper.onActivityResult(requestCode, resultCode, data);
		    } else if (resultCode == Activity.RESULT_OK) {
		        // Do nothing for now
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
