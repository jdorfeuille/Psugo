package com.gvg.psugo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.kobjects.base64.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Directeur_Activity extends Activity implements OnClickListener, 
		LocationListener {

	private static final int TAKE_PHOTO_DIRECT_CODE = 50;
	// Composantes d'Interface graphique
	Button actionDirectPics;
	Button actionAddDirect;
	Button actionFinishDirect;
	
	//Spinners
	
	Spinner typeDirecteurList, genreDirecteurList;
	
	// Text Fields that needs to be save
	EditText nomDirecteur;
	EditText phoneDirecteur;
	EditText cinDirecteur;
	EditText emailDirecteur;

	//
	int idDir;
	int instId;
	int ctlLocation = 0;
	double schoolLatitude;
	double schoolLongitude;
	LocationManager locationManager;
	Location location;
	String provider;
	String typeDirSelected, genreDirSelected;

	//
	CharSequence text;

	// Pictures
	private ImageView imageView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directeur_activity_layout);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idDir = extras.getInt("idDir");
			instId = extras.getInt("instId");
			idDir = idDir + 1; // incrementing cnt call to self if need be
		}
		// nomEcole.getText().toString(); to get the string value...
		// to get the int value
		// Integer.parseInt(myEditText.getText().toString())).
		nomDirecteur = (EditText) findViewById(R.id.nomDir);
		phoneDirecteur = (EditText) findViewById(R.id.phoneDir);
		cinDirecteur = (EditText) findViewById(R.id.cinDir);
		emailDirecteur = (EditText) findViewById(R.id.emailDir);

		// Pictures
		// imageView = (ImageView) findViewById(R.id.camera_image);;

		// Spinners
		typeDirecteurList = (Spinner) findViewById(R.id.typeDirecteurList);
		typeDirecteurList.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String item = arg0.getItemAtPosition(arg2).toString();
						typeDirSelected = item;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
		
		
		genreDirecteurList = (Spinner) findViewById(R.id.genreDirList);
		genreDirecteurList
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String item = arg0.getItemAtPosition(arg2).toString();
						genreDirSelected = item;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
		
		// String.valueOf(spinner1.getSelectedItem()) to get the values 

		// Buttons
		actionDirectPics = (Button) findViewById(R.id.actionDirectPics);
		actionDirectPics.setOnClickListener(this);
		actionAddDirect = (Button) findViewById(R.id.actionAddDir);
		actionAddDirect.setOnClickListener(this);
		actionFinishDirect = (Button) findViewById(R.id.actionFinishDirect);
		actionFinishDirect.setOnClickListener(this);

		// GPS Coordinates
		// Getting LocationManager object
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			System.out.println("GPS is not enabled");
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		// Creating an empty criteria object
		Criteria criteria = new Criteria();

		// Getting the name of the provider that meets the criteria
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		} else
			Toast.makeText(getBaseContext(), "Location can't be retrieved",
					Toast.LENGTH_SHORT).show();

	}

	/* display a Toast with message text. */
	private void showMessage(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	private Photo createPhotoObject(byte[] byteArray){
		Photo aPhoto = new Photo();
		aPhoto.latitude = "";
		aPhoto.longitude = "";
		if (location != null) {
			aPhoto.longitude = String.valueOf(location.getLongitude());
			aPhoto.latitude = String.valueOf(location.getLatitude());
		}
		aPhoto.typePhoto = "";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
		Date date = new Date();
		aPhoto.datePhoto = df.format(date);
		aPhoto.photo = Base64.encode(byteArray);
		//aPhoto.photo = Base64.encodeBase64String(byteArray); //encode the data 
		return aPhoto;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Bitmap datifoto = null;
		System.out.println("onActivityResult - PsugoCameraHelper");
		if (resultCode == RESULT_OK) {
			System.out.println("inside if resultCode == RESULT_OK");
			if (requestCode == TAKE_PHOTO_DIRECT_CODE) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				Photo myPhoto = createPhotoObject(byteArray); 
				PsugoDB psudb = new PsugoDB(getBaseContext());
				psudb.open();
				/*
				public void  insertDirecteur(int instId, String nom, String genre, String type, String email, 
						String telephone, String cin, byte[] photo, String longitude, String latitude, String datePhoto){
					*/
				psudb.insertDirecteur(instId, nomDirecteur.getText().toString(),
						genreDirSelected, typeDirSelected,emailDirecteur.getText().toString(),
						phoneDirecteur.getText().toString(),cinDirecteur.getText().toString(),
						myPhoto.photo, myPhoto.longitude, myPhoto.latitude, myPhoto.datePhoto);

				psudb.close();
				// mImageView.setImageBitmap(mImageBitmap);
			}
		}
		System.out.println("done onActivityResult - PsugoCameraHelper");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values

		// CharSequence text;
		switch (v.getId()) {
		case R.id.actionFinishDirect:
			text = "'Done' clicked!";
			showMessage(text);
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.actionAddDir:
			text = "Activating the same intent to add another director";
			// takePhoto();
			showMessage(text);
			nomDirecteur.setText("");
			Intent i = new Intent(this, Directeur_Activity.class);
			Bundle b = new Bundle();
			b.putInt("idDir", idDir);
			b.putInt("instId", instId);
			i.putExtras(b);
			startActivity(i);
			finish();
			break;
		case R.id.actionDirectPics:

			// onLocationChanged(location);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PHOTO_DIRECT_CODE);
			String locationDisplay = "Latitude:" + schoolLatitude
					+ "   Longitude:" + schoolLongitude;
			Toast.makeText(getBaseContext(), locationDisplay,
					Toast.LENGTH_SHORT).show();
			break;

		default:
			// text = "Dunno what was pushed!";
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}