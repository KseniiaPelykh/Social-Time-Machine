package com.example.socialtimemachine;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class NewGameActivity extends Activity {
	
	public static final int RESULT_LOAD_IMAGE = 1;
	public static final int REAUTH_ACTIVITY_CODE = 2;
	private static final String EMPTY_STRING = "";
	private String userId = "";
	private UiLifecycleHelper uiHelper;
		
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");
		setContentView(R.layout.activity_newgame);
		
		Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		buttonLoadImage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0){
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});		
	}
	
	public void saveGame(View view){
		EditText titleOfGame = (EditText)findViewById(R.id.title_of_game);
		EditText textOfUser = (EditText)findViewById(R.id.text_of_user);
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
