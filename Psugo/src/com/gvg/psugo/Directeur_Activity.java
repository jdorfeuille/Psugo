package com.gvg.psugo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import android.provider.Settings;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Directeur_Activity extends Activity implements OnClickListener, 
		LocationListener {

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
	int ctlLocation = 0;
	double schoolLatitude;
	double schoolLongitude;
	LocationManager locationManager;
	Location location;
	String provider;

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
		typeDirecteurList.setOnItemSelectedListener(new MyOnItemSelectedListener());
		genreDirecteurList = (Spinner) findViewById(R.id.genreDirList);
		genreDirecteurList.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
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
			i.putExtras(b);
			startActivity(i);
			finish();
			break;
		case R.id.actionDirectPics:

			// onLocationChanged(location);
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