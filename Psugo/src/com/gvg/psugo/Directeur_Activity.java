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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Directeur_Activity extends Activity implements OnClickListener {

	private static final int TAKE_PHOTO_DIRECT_CODE = 50;
	private static final String TYPE_ADMINISTRATIF = "Administratif";
	private static final String TYPE_PEDAGOGIQUE = "Pedagogique";
	// Composantes d'Interface graphique
	Button actionDirectPics;
	Button actionAddDirect;
	Button actionPreviewDir;
	Button actionFinishDirect;

	// Spinners

	Spinner typeDirecteurList, genreDirecteurList;

	// Text Fields that needs to be save
	EditText nomDirecteur;
	EditText phoneDirecteur;
	EditText cinDirecteur;
	EditText emailDirecteur;
	Spinner dirList;

	//
	int idDir;
	int instId;
	int ctlLocation = 0;
//	double photoLatitude;
//	double photoLongitude;
	String provider;
	String typeDirSelected, genreDirSelected, dirSelected;
	Photo photoDir;
	Directeur[] directeursFromDB;
	String[] listNomDirect;
	String photoPath;

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
//			photoLongitude = extras.getDouble("longitude");
//			photoLatitude = extras.getDouble("latitude");
			idDir = idDir + 1; // incrementing cnt call to self if need be
		}
		directeursFromDB = this.getDirecteursFromDB(instId);
		listNomDirect = this.getListNomDir();

		nomDirecteur = (EditText) findViewById(R.id.nomDir);
		//
		dirList = (Spinner) findViewById(R.id.dirList);

		// directList = (AutoCompleteTextView)findViewById(R.id.directList);
		ArrayAdapter<String> adapterDirect = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, listNomDirect);
		//adapterDirect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// android.R.layout.simple_dropdown_item_1line
		dirList.setAdapter(adapterDirect);
		/*
		dirList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//dirList.showDropDown();
			}
		});
		*/
		dirList.setOnItemSelectedListener(new OnItemSelectedListener() {

			private int getDirInfos(String sLookup, String[] arrayToLook) {
				int idx = -1;

				for (int i = 0; i < arrayToLook.length; i++) {
					if (sLookup.equalsIgnoreCase(arrayToLook[i])) {
						idx = i;
						break;
					}
				}
				return idx;
			}

			public void updateUIFields() {

				if (dirSelected.isEmpty()) {
					nomDirecteur.setText("");
					nomDirecteur.setEnabled(true);
					emailDirecteur.setText("");
					photoDir = null;
					cinDirecteur.setText("");
					phoneDirecteur.setText("");
					genreDirSelected = "";
					typeDirSelected = "";	
				}
				else {
					int idx = getDirInfos(dirSelected, listNomDirect);
					
					if (idx > -1) {
						nomDirecteur.setText(directeursFromDB[idx].nom);
						nomDirecteur.setEnabled(false); // disable so we don't
														// update this field
						emailDirecteur.setText(String
								.valueOf(directeursFromDB[idx].email));
						photoDir = directeursFromDB[idx].photo;
						cinDirecteur.setText(directeursFromDB[idx].cin);
						phoneDirecteur.setText(directeursFromDB[idx].telephone);
	
						genreDirSelected = directeursFromDB[idx].genre;
						String temp = (String) genreDirecteurList
								.getItemAtPosition(0);
						
						if (temp.equalsIgnoreCase(genreDirSelected))
							genreDirecteurList.setSelection(0);
						else
							genreDirecteurList.setSelection(1);
	
						typeDirSelected = directeursFromDB[idx].typeDirection;
						temp = (String) typeDirecteurList.getItemAtPosition(0);
						if (temp.equalsIgnoreCase(typeDirSelected))
							typeDirecteurList.setSelection(0);
						else
							typeDirecteurList.setSelection(1);
	
					}
				}
			}

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String item = arg0.getItemAtPosition(arg2).toString();
				dirSelected = item;
				updateUIFields();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		//
		phoneDirecteur = (EditText) findViewById(R.id.phoneDir);
		cinDirecteur = (EditText) findViewById(R.id.cinDir);
		emailDirecteur = (EditText) findViewById(R.id.emailDir);

		// Pictures
		// imageView = (ImageView) findViewById(R.id.camera_image);;

		// Spinners
		typeDirecteurList = (Spinner) findViewById(R.id.typeDirecteurList);
		typeDirecteurList
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String item = arg0.getItemAtPosition(arg2).toString();
						typeDirSelected = item;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						String[] tmp = getResources().getStringArray(R.array.type_directeur_array);
						typeDirSelected = tmp[0];

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
						String[] tmp = getResources().getStringArray(R.array.genre_humain_array);
						genreDirSelected = tmp[0];
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
		actionPreviewDir = (Button) findViewById(R.id.actionPreviewDir);
		actionPreviewDir.setOnClickListener(this);
		

	}

	private String[] getListNomDir() {
		// TODO Auto-generated method stub
		int nDir = directeursFromDB.length;
		String[] listNom = new String[nDir];
		for (int i = 0; i < nDir; i++) {
			listNom[i] = directeursFromDB[i].nom;
		}
		return listNom;
	}

	private Directeur[] getDirecteursFromDB(int instId2) {
		// TODO Auto-generated method stub
		PsugoDB psudb = new PsugoDB(getBaseContext());
		psudb.open();
		Directeur dirAdm = psudb.selectDirecteur(instId2, TYPE_ADMINISTRATIF);
		Directeur dirPed = psudb.selectDirecteur(instId2, TYPE_PEDAGOGIQUE);
		psudb.close();
		int nDir = 0;
		if (dirAdm != null)
			nDir++;
		if (dirPed != null)
			nDir++;
		Directeur[] dirList = new Directeur[nDir+1];
		dirList[0] = new Directeur(instId, "","","","","","", null); //dummy to allow no selection if needed
		int idx = 1;
		if (dirAdm != null) {
			dirList[idx] = dirAdm;
			idx++;
		}
		if (dirPed != null) {
			dirList[idx] = dirPed;
		}
		return dirList;
	}

	/* display a Toast with message text. */
	private void showMessage(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private Photo createPhotoObject(byte[] byteArray) {
		Photo aPhoto = new Photo();
		aPhoto.latitude = String.valueOf(PsugoMainActivity.schoolLatitude);
		aPhoto.longitude = String.valueOf(PsugoMainActivity.schoolLongitude);
		aPhoto.typePhoto = "";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
		Date date = new Date();
		aPhoto.datePhoto = df.format(date);
		aPhoto.photo = Base64.encode(byteArray);
		// aPhoto.photo = Base64.encodeBase64String(byteArray); //encode the
		// data
		return aPhoto;
	}

	public void saveScreen() {

		// System.out.println("instID from SaveScreen classe ==> " + instId);
		String tNomDir = "";
		String tEmailDir = "";
		String tPhoneDir = "";
		String tCinDir = "";
		if (!nomDirecteur.getText().toString().isEmpty()) {
			tNomDir = nomDirecteur.getText().toString();
		}
		if (!emailDirecteur.getText().toString().isEmpty()) {
			tEmailDir = emailDirecteur.getText().toString();
		}
		if (!phoneDirecteur.getText().toString().isEmpty()) {
			tPhoneDir = phoneDirecteur.getText().toString();
		}
		if (!cinDirecteur.getText().toString().isEmpty()) {
			tCinDir = cinDirecteur.getText().toString();
		}
		if (photoDir == null) {
			photoDir = new Photo("", "", "", "", "");
		}

		PsugoDB psudb = new PsugoDB(getBaseContext());
		psudb.open();
		psudb.insertDirecteur(instId, tNomDir, genreDirSelected,
				typeDirSelected, tEmailDir, tPhoneDir, tCinDir, photoDir.photo,
				photoDir.longitude, photoDir.latitude, photoDir.datePhoto);

		psudb.close();

		// Toast.makeText(Psugo_Login_Activity.this, "Invalid Login",
		// Toast.LENGTH_LONG).show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Bitmap datifoto = null;
		//System.out.println("onActivityResult - PsugoCameraHelper");
		if (resultCode == RESULT_OK) {
			//System.out.println("inside if resultCode == RESULT_OK");
			if (requestCode == TAKE_PHOTO_DIRECT_CODE) {
				/*
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				Photo myPhoto = createPhotoObject(byteArray);
				photoDir = myPhoto;
				
				*/
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] byteArray = stream.toByteArray();
				photoDir = createPhotoObject(byteArray); // 
				if (genreDirSelected.isEmpty()){
					String[] tmp = getResources().getStringArray(R.array.genre_humain_array);
					genreDirSelected = tmp[0];
				}
				if (typeDirSelected.isEmpty()) {
					String[] tmp = getResources().getStringArray(R.array.type_directeur_array);
					typeDirSelected = tmp[0];
				}

				PsugoDB psudb = new PsugoDB(getBaseContext());
				psudb.open();
				psudb.insertDirecteur(instId,
						nomDirecteur.getText().toString(), genreDirSelected,
						typeDirSelected, emailDirecteur.getText().toString(),
						phoneDirecteur.getText().toString(), cinDirecteur.getText().toString(), 
						photoDir.photo, photoDir.longitude, photoDir.latitude, photoDir.datePhoto);

				psudb.close();
				// mImageView.setImageBitmap(mImageBitmap);
			}
		}
		// System.out.println("done onActivityResult - PsugoCameraHelper");

	}

	@Override
	public void onBackPressed() {
		// disable back key
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values

		// CharSequence text;
		switch (v.getId()) {
		case R.id.actionFinishDirect:
			//text = "'Done' clicked!";
			//showMessage(text);
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.actionAddDir:
			//text = "Activating the same intent to add another director";
			// takePhoto();
			//showMessage(text);
			nomDirecteur.setText("");
			Intent i = new Intent(this, Directeur_Activity.class);
			Bundle b = new Bundle();
			b.putInt("idDir", idDir);
			b.putInt("instId", instId);
			i.putExtras(b);
			startActivity(i);
			setResult(RESULT_OK); 
			finish();
			break;
		case R.id.actionDirectPics:

			// onLocationChanged(location);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				 final File path = new File( Environment.getExternalStorageDirectory(), this.getPackageName() );
		    	  if(!path.exists()){
		    	    path.mkdir();
		    	  }
				File image = File.createTempFile("phototmp", "jpg", path);	   
				photoPath = image.getAbsolutePath();
		    	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image)); 
		    	startActivityForResult(intent, TAKE_PHOTO_DIRECT_CODE);
		    	image.delete();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			break;

		case R.id.actionPreviewDir:
			this.saveScreen();
			Intent i2 = new Intent(this, Liste_Directeurs.class);
			Bundle b2 = new Bundle();
			b2.putInt("instId", instId);
			i2.putExtras(b2);
			startActivity(i2);
			break;
		default:
			// text = "Dunno what was pushed!";
		}
	}

}