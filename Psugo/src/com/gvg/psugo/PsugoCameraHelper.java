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
import android.graphics.BitmapFactory;
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
//import android.widget.Toast;

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
	
//	double photoLongitude, photoLatitude;
	String provider;
	String fileStartName;
	String photoPath;
	int idPhoto;
	int instId;
	File image=null;
	
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

	}

	  /*
     *  Start Camera to Take Picture 
     */
	
	/*
    private void takePhoto(){
    	
    	  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPicFile(this)) ); 
    	  startActivityForResult(intent, TAKE_PHOTO_CODE);
    	  //System.out.println("done takePhoto()");
    	}


 */
	private File getPicFile(Context context) throws IOException{
    	  //it will return /sdcard/image.tmp
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    	  if(!path.exists()){
    	    path.mkdir();
    	  }
    	  fileStartName = "Inst_" + String.valueOf(emplacementList.getSelectedItem());
    	  System.out.println("fileStartName=" + fileStartName);
    	  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		  String filename = this.fileStartName + timeStamp ;
		  return File.createTempFile(filename, "jpg", path);
    	  //return new File(path, filename);
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
		aPhoto.latitude = String.valueOf(PsugoMainActivity.schoolLatitude);
		aPhoto.longitude = String.valueOf(PsugoMainActivity.schoolLongitude);
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
			//System.out.println("inside if resultCode == RESULT_OK");
			/*
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
			*/
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
			byte[] byteArray = stream.toByteArray();
			myPhoto = createPhotoObject(byteArray); // 
			psudb = new PsugoDB(getBaseContext());
			psudb.open();
			psudb.insertInstitutionPhoto(instId, myPhoto.photo,
					myPhoto.longitude, myPhoto.latitude, myPhoto.datePhoto,
					myPhoto.typePhoto);
			psudb.close();
			image.delete();
			
		}
		//System.out.println("done onActivityResult - PsugoCameraHelper");

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
	@Override
	public void onBackPressed() {
		// disable back key
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// takePhoto();

		switch (v.getId()) {
		case R.id.actionTakePics:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				 final File path = new File( Environment.getExternalStorageDirectory(), this.getPackageName() );
		    	  if(!path.exists()){
		    	    path.mkdir();
		    	  }
				
				image = File.createTempFile("phototmp", "jpg", path);	
				//image = this.getPicFile(this);
				photoPath = image.getAbsolutePath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(intent, TAKE_PHOTO_CODE);
				
			}
			catch(IOException e){
				e.printStackTrace();
			}

			break;
		case R.id.actionAddPics:

			//Preview pics wrong button name 
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
