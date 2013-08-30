package com.gvg.psugo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Classe_Activity extends Activity implements OnClickListener, 
		LocationListener {
	
	private static final int TAKE_PHOTO_CODE = 1;

	// Composantes d'Interface graphique
	Button actionClassePics;
	Button actionProfPics;
	Button actionAddClasse;
	Button actionFinishClasse;
	
	//Spinners
	Spinner genreProfList;
	
	// Text Fields that needs to be save
	EditText nomClasse;
	EditText nomProfClasse;
	EditText nbrEleve;
	EditText emailProf;
	EditText phoneProf;

	//
	int idClasse;
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
		setContentView(R.layout.classe_activity_layout);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idClasse = extras.getInt("idClasse");
			idClasse = idClasse + 1; // incrementing cnt call to self if need be
		}
		// nomEcole.getText().toString(); to get the string value...
		// to get the int value
		// Integer.parseInt(myEditText.getText().toString())).
		nomClasse = (EditText) findViewById(R.id.nomClasse);
		nomProfClasse = (EditText) findViewById(R.id.nomProfClasse);
		nbrEleve = (EditText) findViewById(R.id.nbrEleve);
		emailProf = (EditText) findViewById(R.id.emailProf);
		phoneProf = (EditText) findViewById(R.id.phoneProf);

		// Pictures
		// imageView = (ImageView) findViewById(R.id.camera_image);;

		// Spinners
		genreProfList = (Spinner) findViewById(R.id.genreProfList);
		// Create an ArrayAdapter using the array defined in strings 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.genre_humain_array, android.R.layout.simple_dropdown_item_1line);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// Apply the adapter to the spinner
		genreProfList.setAdapter(adapter);
		genreProfList.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		// String.valueOf(spinner1.getSelectedItem()) to get the values 

		// Buttons
		actionClassePics = (Button) findViewById(R.id.photoClasse);
		actionClassePics.setOnClickListener(this);
		actionProfPics = (Button) findViewById(R.id.actionProfPics);
		actionProfPics.setOnClickListener(this);
		actionAddClasse = (Button) findViewById(R.id.actionAddClasse);
		actionAddClasse.setOnClickListener(this);
		actionFinishClasse = (Button) findViewById(R.id.actionFinishClasse);
		actionFinishClasse.setOnClickListener(this);

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
	
	 /*
     *  Start Camera to Take Picture 
     */
	
	
    private void takePhoto(String entite){
    	  final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPicFile(this, entite)) ); 
    	  startActivityForResult(intent, TAKE_PHOTO_CODE);
    	  finish();
    	}



	private File getPicFile(Context context, String entite){
    	  //it will return /sdcard/image.tmp
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    	  if(!path.exists()){
    	    path.mkdir();
    	  }
    	  String fileStartName = entite;
    	  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		  String filename = fileStartName + timeStamp + ".jpg";
    	  return new File(path, filename);
    	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values

		// CharSequence text;
		switch (v.getId()) {
		case R.id.actionFinishClasse:
			text = "'Done' clicked!";
			// save Daa
			showMessage(text);
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.actionAddClasse:
			text = "Activating the same intent to add another Classe";
			// takePhoto();
			showMessage(text);
			nomClasse.setText("");
			nomProfClasse.setText("");
			nbrEleve.setText("");
			emailProf.setText("");
			phoneProf.setText("");

			Intent i = new Intent(this, Classe_Activity.class);
			Bundle b = new Bundle();
			b.putInt("idClasse", idClasse);
			i.putExtras(b);
			startActivity(i);
			finish();
			break;
		case R.id.photoClasse:

			// onLocationChanged(location);
			String locationDisplay = "Latitude:" + schoolLatitude
					+ "   Longitude:" + schoolLongitude;
			Toast.makeText(getBaseContext(), locationDisplay,
					Toast.LENGTH_SHORT).show();
			takePhoto("Classe");
			break;

		case R.id.actionProfPics:

			// onLocationChanged(location);
			//String locationDisplay = "Latitude:" + schoolLatitude
			//		+ "   Longitude:" + schoolLongitude;
			//Toast.makeText(getBaseContext(), locationDisplay,
			//		Toast.LENGTH_SHORT).show();
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