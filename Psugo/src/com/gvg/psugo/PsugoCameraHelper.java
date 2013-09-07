package com.gvg.psugo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//import org.apache.commons.codec.binary.Base64;

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
import org.kobjects.base64.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class PsugoCameraHelper extends Activity implements OnClickListener,
LocationListener  {

	private static final int TAKE_PHOTO_CODE = 1;
	private static final String TYPE_PHOTO_D = "D";
	private static final String TYPE_PHOTO_C = "C";
	private static final String TYPE_PHOTO_1 = "1";
	private static final String TYPE_PHOTO_2 = "2";
	private static final String TYPE_PHOTO_3 = "3";
	private static final String TYPE_PHOTO_4 = "4";
	private static final String TYPE_PHOTO_5 = "5";
	
	LocationManager locationManager;
	Location location;
	String provider;
	String fileStartName;
	int idPhoto;
	int instId;
	
	//EditText numPhoto;
	
	Button actionTakePics;
	Button actionAddPics;
	Button actionFinishPics;
	
	Spinner  emplacementList;
	String emplacementSelected;
	
	Photo myPhoto;
	
	// Database
	 PsugoDB psudb;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_helper);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idPhoto = extras.getInt("idPhoto");
			instId = extras.getInt("instId");
			idPhoto = idPhoto + 1; // incrementing cnt call to self if need be
			System.out.println("instId from PsugoCameraHelper= " + instId);
		}
	
		//numPhoto = (EditText) findViewById(R.id.numPhoto);

		// Pictures
		// imageView = (ImageView) findViewById(R.id.camera_image);
		
		//Spinners 
		
		emplacementList = (Spinner) findViewById(R.id.emplacementList);
		emplacementList.setOnItemSelectedListener(new MyOnItemSelectedListener());
		// Create an ArrayAdapter using the array defined in strings 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.emplacement_pics_array, android.R.layout.simple_dropdown_item_1line);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// Apply the adapter to the spinner
		emplacementList.setAdapter(adapter);
		emplacementList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				emplacementSelected = parent.getItemAtPosition(arg2).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// Buttons
		actionTakePics = (Button) findViewById(R.id.actionTakePics);
		actionTakePics.setOnClickListener(this);
		actionAddPics = (Button) findViewById(R.id.actionAddPics);
		actionAddPics.setOnClickListener(this);
		actionFinishPics = (Button) findViewById(R.id.actionFinishPics);
		actionFinishPics.setOnClickListener(this);

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

	  /*
     *  Start Camera to Take Picture 
     */
	
	
    private void takePhoto(){
    	
    	  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPicFile(this)) ); 
    	  startActivityForResult(intent, TAKE_PHOTO_CODE);
    	  System.out.println("done takePhoto()");
    	}



	private File getPicFile(Context context){
    	  //it will return /sdcard/image.tmp
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    	  if(!path.exists()){
    	    path.mkdir();
    	  }
    	  fileStartName = "Inst_" + String.valueOf(emplacementList.getSelectedItem());
    	  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		  String filename = this.fileStartName + timeStamp + ".jpg";
    	  return new File(path, filename);
    	}


	private void debugPhoto( Photo myPhoto) {
		
		System.out.println("myPhoto.datePhoto = " + myPhoto.datePhoto);
		System.out.println("myPhoto.latitude = " + myPhoto.latitude);
		System.out.println("myPhoto.longitude = " + myPhoto.longitude);
		System.out.println("myPhoto.typePhoto = " + myPhoto.typePhoto);
		System.out.println("myPhoto.photo = " + myPhoto.photo);
		System.out.println("Done Debug Photo");
	}
	
	private Photo createPhotoObject(byte[] byteArray){
		Photo aPhoto = new Photo();
		aPhoto.latitude = "";
		aPhoto.longitude = "";
		if (location != null) {
			aPhoto.longitude = String.valueOf(location.getLongitude());
			aPhoto.latitude = String.valueOf(location.getLatitude());
		}
		if (emplacementSelected.equalsIgnoreCase("Cours")){
			aPhoto.typePhoto = TYPE_PHOTO_C;
		}
		else if (emplacementSelected.equalsIgnoreCase("Devant")){
			aPhoto.typePhoto = TYPE_PHOTO_D;
		}
		else if (emplacementSelected.contains("1")){
			aPhoto.typePhoto = TYPE_PHOTO_1;
		}
		else if (emplacementSelected.contains("2")){
			aPhoto.typePhoto = TYPE_PHOTO_2;
		}
		else if (emplacementSelected.contains("3")){
			aPhoto.typePhoto = TYPE_PHOTO_3;
		}
		else if (emplacementSelected.contains("4")){
			aPhoto.typePhoto = TYPE_PHOTO_4;
		}
		else if (emplacementSelected.contains("5")){
			aPhoto.typePhoto = TYPE_PHOTO_5;
		}
		else aPhoto.typePhoto = "E"; // Error 
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
		Date date = new Date();
		aPhoto.datePhoto = df.format(date);
		aPhoto.photo = Base64.encode(byteArray);
		//aPhoto.photo = Base64.encodeBase64String(byteArray); //encode the data 
		return aPhoto;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Bitmap datifoto = null;
		System.out.println("onActivityResult - PsugoCameraHelper");
		if (resultCode == RESULT_OK) {
			System.out.println("inside if resultCode == RESULT_OK");
			
			/*
			if (requestCode == TAKE_PHOTO_CODE) {
			   
			      Uri picUri = data.getData();//<- get Uri here from data intent
			       if(picUri !=null){
			         try {
			             datifoto = android.provider.MediaStore.Images.Media.getBitmap(
			                                     this.getContentResolver(), 
			                                     picUri);
			         } catch (FileNotFoundException e) {
			            throw new RuntimeException(e);
			         } catch (IOException e) {
			            throw new RuntimeException(e);
			         }
			    }
			}
			if (datifoto != null ){
				System.out.println("i have a bitmap");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				datifoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				myPhoto = createPhotoObject(byteArray); // save image
				psudb = new PsugoDB(getBaseContext());
				psudb.open();
				psudb.insertInstitutionPhoto(instId, myPhoto.photo,
						myPhoto.longitude, myPhoto.latitude, myPhoto.datePhoto,
						myPhoto.typePhoto);
				psudb.close();
			}

			else */
			if (requestCode == TAKE_PHOTO_CODE) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data"); 
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				myPhoto = createPhotoObject(byteArray); // 
				psudb = new PsugoDB(getBaseContext());
				psudb.open();
				psudb.insertInstitutionPhoto(instId, myPhoto.photo,
						myPhoto.longitude, myPhoto.latitude, myPhoto.datePhoto,
						myPhoto.typePhoto);
				psudb.close();
				// mImageView.setImageBitmap(mImageBitmap);
			}
		}
		System.out.println("done onActivityResult - PsugoCameraHelper");

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

	//here
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// takePhoto();
		switch (v.getId()) {
		case R.id.actionTakePics:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//intent.putExtra(MediaStore.EXTRA_OUTPUT,
			//		Uri.fromFile(getPicFile(this)));
			startActivityForResult(intent, TAKE_PHOTO_CODE);
			//System.out.println("done takePhoto()");
			//setResult(RESULT_OK);
			// finish();
			break;
		case R.id.actionAddPics:

			// takePhoto();
			// here we need to clear fields (reset form)
			/*Intent i = new Intent(this, PsugoCameraHelper.class);
			Bundle b = new Bundle();
			b.putInt("idPhoto", idPhoto);
			i.putExtras(b);
			startActivity(i); 
			finish();*/
			Intent i = new Intent(this, Liste_Photo_Inst.class);
			Bundle b = new Bundle();
			b.putInt("instId", instId);
			i.putExtras(b);
			startActivity(i); 
			break;
		case R.id.actionFinishPics:

			setResult(RESULT_OK);
			finish();
			break;

		default:
			// text = "Dunno what was pushed!";
		}

	}


}
