package com.gvg.psugo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Psugo_Login_Activity extends Activity implements OnClickListener {

	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnCancel;
	String theUserName;
	String thePassword;
	private static final int PSUGO_LOGIN = 999;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.psugo_login_activity);

		txtUserName = (EditText) this.findViewById(R.id.txtUname);
		txtPassword = (EditText) this.findViewById(R.id.txtPwd);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnCancel = (Button) this.findViewById(R.id.btnCancel);

		btnLogin.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	/*
	 * btnLogin.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub
	 * 
	 * theUserName = txtUserName.getText().toString(); thePassword =
	 * txtPassword.getText().toString(); Intent returnIntent = new Intent();
	 * returnIntent.putExtra("theUserName",theUserName);
	 * returnIntent.putExtra("thePassword",thePassword);
	 * 
	 * 
	 * 
	 * PsugoServiceClientHelper psch = new
	 * PsugoServiceClientHelper(getBaseContext());
	 * 
	 * try { AsyncTask<String, String, TempData> servCall_Login =
	 * psch.execute(new String[] { "Login", theUserName, thePassword });
	 * servCall_Login.get(); } catch (Exception e) {
	 * System.out.println("Erreur Login en echec... "); }
	 * 
	 * PsugoDB psudb = new PsugoDB(getBaseContext()); psudb.open(); if
	 * (psudb.validateLogin(theUserName, thePassword) == true){
	 * 
	 * finish(); } else Toast.makeText(Psugo_Login_Activity.this,
	 * "Invalid Login", Toast.LENGTH_LONG).show(); psudb.close(); } }); Bundle b
	 * = null; b = new Bundle(); // getApplicationContext() ... we can pass in
	 * the context here Intent request =new Intent(this,
	 * PsugoMainActivity.class); startActivityForResult(request, PSUGO_LOGIN);
	 * //setResult(RESULT_OK,returnIntent); //finish(); }
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TempData resp = new TempData();
		PsugoDB psudb = new PsugoDB(getBaseContext()); 
		psudb.open(); 
		switch (v.getId()) {
		case R.id.btnLogin:
			theUserName = txtUserName.getText().toString();
			thePassword = txtPassword.getText().toString();
			Bundle b = null;
			b = new Bundle();
			b.putString("theUserName", theUserName);
			b.putString("thePassword", thePassword);
			PsugoUtils pscn = new PsugoUtils(this.getBaseContext());
			if (pscn.isNetworkAvailable() ) {
				try { 
					PsugoServiceClientHelper psch = new PsugoServiceClientHelper(getBaseContext());
					AsyncTask<String, String, TempData> servCall_Login =
					psch.execute(new String[] { "Login", theUserName, thePassword });
					//servCall_Login.get();
					resp = servCall_Login.get();
					if ( resp.userIsValid) {
						psudb.insertUser(theUserName, thePassword); // make sure to keep the user and pwd..
						Intent request = new Intent(this, PsugoMainActivity.class);
					   // b.putBoolean("isNetworkAvailable", true);
						request.putExtras(b);
						startActivityForResult(request, PSUGO_LOGIN);
					}
					else Toast.makeText(Psugo_Login_Activity.this,
							  "Invalid Login", Toast.LENGTH_LONG).show(); 
				 }
				catch (Exception e) {
						 System.out.println("Erreur Login en echec... ");
						 //Erreur grave... 
						 }
			}
			
			else {		
					// Local User Validation 
					
					if	 (psudb.validateLogin(theUserName, thePassword) )
					{		 
						Intent request = new Intent(this, PsugoMainActivity.class);
						//b.putBoolean("isNetworkAvailable", false);
						request.putExtras(b);
						startActivityForResult(request, PSUGO_LOGIN);
					} 
					else Toast.makeText(Psugo_Login_Activity.this,
							  "Invalid Login", Toast.LENGTH_LONG).show(); 
					
			}
			psudb.close();
			break;
		// setResult(RESULT_OK,returnIntent);
		case R.id.btnCancel:
			finish();

		}
	}
	
	
	
}
