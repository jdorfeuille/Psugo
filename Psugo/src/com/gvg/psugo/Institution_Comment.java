package com.gvg.psugo;




import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
//import android.provider.Settings;
//import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Institution_Comment extends Activity implements OnClickListener {


	// Composantes d'Interface graphique

		Button actionFinishComment;
		// Text Fields that needs to be save
		EditText cinComment;
		String commentaires;
		int instId;



		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.inst_comment_activity_layout);

			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				instId = extras.getInt("instId");
				commentaires = extras.getString("commentaires");

			}

			cinComment = (EditText) findViewById(R.id.cin);
			if (commentaires.isEmpty()) {
				cinComment.setText("");
			}
			else cinComment.setText(commentaires);

			// Buttons
			actionFinishComment = (Button) findViewById(R.id.actionFinishComment);
			actionFinishComment.setOnClickListener(this);


			

		}

	
	

		public void saveScreen() {


			if (!cinComment.getText().toString().isEmpty()) {
				commentaires = cinComment.getText().toString();
				
			}


		}
		@Override
		public void onBackPressed() {
			// disable back key
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// dummy method for now to test the buttons and display all values
			Intent intent;
			// CharSequence text;
			switch (v.getId()) {
			case R.id.actionFinishComment:
				this.saveScreen();
				intent=new Intent();
				intent.putExtra("newComment", commentaires);
				setResult(RESULT_OK, intent);
				finish();
				break;
		
			default:
				// text = "Dunno what was pushed!";
			}
		}



}
