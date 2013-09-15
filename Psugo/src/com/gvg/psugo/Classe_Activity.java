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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
//import android.provider.Settings;
//import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Classe_Activity extends Activity implements OnClickListener {

	private static final int TAKE_PHOTO_CLASSE_CODE = 50;
	private static final int TAKE_PHOTO_PROF_CODE = 60;

	// Composantes d'Interface graphique
	Button actionClassePics;
	Button actionProfPics;
	Button actionAddClasse;
	Button actionFinishClasse, actionPreviewPic;

	// Spinners
	Spinner genreProfList;
	AutoCompleteTextView classList;
	Classe[] classesFromDB;
	String[] listNomClasse;
	String classSelected;
	String genreProf;

	// Text Fields that needs to be save
	EditText nomClasse;
	EditText nomProfClasse;
	EditText nbrEleve;
	EditText emailProf;
	EditText phoneProf;
	EditText cinProf;

	//
	int idClasse = 1;
	int instId;
	int ctlLocation = 0;
	double photoLatitude;
	double photoLongitude;
	LocationManager locationManager;
	Location location = null;
	Location lastLocation = null;
	String provider;
	Photo photoProf, photoClasse;

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
			instId = extras.getInt("instId");
			photoLongitude = extras.getDouble("longitude");
			photoLatitude = extras.getDouble("latitude");
			idClasse = idClasse + 1; // incrementing cnt call to self if need be
		}

		PsugoDB psudb = new PsugoDB(getBaseContext());
		psudb.open();
		classesFromDB = psudb.selectClasse(instId);
		psudb.close();
		listNomClasse = this.getListNomClasse();
		nomClasse = (EditText) findViewById(R.id.nomClasse);
		nomProfClasse = (EditText) findViewById(R.id.nomProfClasse);
		nbrEleve = (EditText) findViewById(R.id.nbrEleve);
		emailProf = (EditText) findViewById(R.id.emailProf);
		phoneProf = (EditText) findViewById(R.id.phoneProf);
		cinProf = (EditText) findViewById(R.id.cinProf);

		// Pictures
		// imageView = (ImageView) findViewById(R.id.camera_image);;

		// Spinners
		genreProfList = (Spinner) findViewById(R.id.genreProfList);
		// Create an ArrayAdapter using the array defined in strings
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.genre_humain_array,
				android.R.layout.simple_dropdown_item_1line);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// Apply the adapter to the spinner
		genreProfList.setAdapter(adapter);
		genreProfList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String item = arg0.getItemAtPosition(arg2).toString();
				genreProf = item;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		classList = (AutoCompleteTextView) findViewById(R.id.classList);
		ArrayAdapter<String> adapterClasse = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, listNomClasse);
		adapterClasse
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		// android.R.layout.simple_dropdown_item_1line
		classList.setAdapter(adapterClasse);
		classList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				classList.showDropDown();
			}
		});

		classList.setOnItemClickListener(new OnItemClickListener() {

			private int getClasseInfos(String sLookup, String[] arrayToLook) {
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

				int idx = getClasseInfos(classSelected, listNomClasse);
				if (idx > -1) {
					nomClasse.setText(classesFromDB[idx].nomClasse);
					nomClasse.setEnabled(false); // disable so we don't update
													// this field
					nbrEleve.setText(String
							.valueOf(classesFromDB[idx].nombreEleve));
					photoClasse = classesFromDB[idx].photoClasse;
					nomProfClasse.setText(classesFromDB[idx].nomProfesseur);
					emailProf.setText(classesFromDB[idx].emailProf);
					phoneProf.setText(classesFromDB[idx].phoneProf);
					photoProf = classesFromDB[idx].photoProfesseur;
					cinProf.setText(classesFromDB[idx].cinProf);
					genreProf = classesFromDB[idx].genreProf;
					String temp = (String) genreProfList.getItemAtPosition(0);
					if (temp.equalsIgnoreCase(genreProf))
						genreProfList.setSelection(0);
					else
						genreProfList.setSelection(1);

				}

			}

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				// got called System.out.println("classList=====>onItemClick");
				classSelected = (String) arg0.getItemAtPosition(arg2)
						.toString();
				System.out.println("class selected = " + classSelected);
				updateUIFields();
			}
		});

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
		actionPreviewPic = (Button) findViewById(R.id.previewClasses);
		actionPreviewPic.setOnClickListener(this);

		

	}

	/* display a Toast with message text. */
	private void showMessage(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	//
	private String[] getListNomClasse() {
		int instArrayLen = classesFromDB.length;
		String[] instArray = new String[instArrayLen];
		for (int i = 0; i < instArrayLen; i++) {
			instArray[i] = classesFromDB[i].nomClasse;
		}
		return instArray;

	}

	//
	public void displayMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do things

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		// alert.dismiss();
	}

	//
	private File getPicFile(Context context, String entite) {
		// it will return /sdcard/image.tmp
		final File path = new File(Environment.getExternalStorageDirectory(),
				context.getPackageName());
		if (!path.exists()) {
			path.mkdir();
		}
		String fileStartName = entite;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		String filename = fileStartName + timeStamp + ".jpg";
		return new File(path, filename);
	}

	private Photo createPhotoObject(byte[] byteArray) {
		Photo aPhoto = new Photo();
		aPhoto.latitude = String.valueOf(photoLongitude);
		;
		aPhoto.longitude = String.valueOf(photoLongitude);
		;
		aPhoto.typePhoto = "";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
		Date date = new Date();
		aPhoto.datePhoto = df.format(date);
		aPhoto.photo = Base64.encode(byteArray);
		// aPhoto.photo = Base64.encodeBase64String(byteArray); //encode the
		// data
		return aPhoto;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Bitmap datifoto = null;
		// System.out.println("onActivityResult - PsugoCameraHelper");
		if (resultCode == RESULT_OK) {
			// System.out.println("inside if resultCode == RESULT_OK");
			if (requestCode == TAKE_PHOTO_CLASSE_CODE) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				photoClasse = createPhotoObject(byteArray);

			}
			if (requestCode == TAKE_PHOTO_PROF_CODE) {
				Bitmap bmp2 = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray2 = stream.toByteArray();
				photoProf = createPhotoObject(byteArray2);

			}
		}
		// System.out.println("done onActivityResult - PsugoCameraHelper");

	}

	public boolean validClasseInput() {
		boolean isValid = true;
		if (nomClasse.getText().toString().isEmpty()) {
			isValid = false;
		}
		if (nbrEleve.getText().toString().isEmpty()) {
			isValid = false;
		}
		if (photoClasse == null) {
			isValid = false;
		}

		return isValid;

	}

	public void saveScreen() {

		// System.out.println("instID from SaveScreen classe ==> " + instId);
		String tNomProf = "";
		String tEmailProf = "";
		String tPhoneProf = "";
		String tCinProf = "";

		if (this.validClasseInput()) {

			if (!nomProfClasse.getText().toString().isEmpty()) {
				tNomProf = nomProfClasse.getText().toString();
			}
			if (!emailProf.getText().toString().isEmpty()) {
				tEmailProf = emailProf.getText().toString();
			}
			if (!phoneProf.getText().toString().isEmpty()) {
				tPhoneProf = phoneProf.getText().toString();
			}
			if (!cinProf.getText().toString().isEmpty()) {
				tCinProf = cinProf.getText().toString();
			}
			if (photoProf == null) {
				photoProf = new Photo("", "", "", "", "");
			}

			PsugoDB psudb = new PsugoDB(getBaseContext());
			psudb.open();
			psudb.insertClasse(instId, nomClasse.getText().toString(),
					Integer.parseInt(nbrEleve.getText().toString()),
					photoClasse, tNomProf, tEmailProf, tPhoneProf, tCinProf,
					genreProf, photoProf);
			psudb.close();
		}

		// Toast.makeText(Psugo_Login_Activity.this, "Invalid Login",
		// Toast.LENGTH_LONG).show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values
		Intent intent;
		// CharSequence text;
		switch (v.getId()) {
		case R.id.actionFinishClasse:
			this.saveScreen();
			text = "'Done' clicked!";
			// save Daa
			showMessage(text);
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.actionAddClasse:
			text = "Activating the same intent to add another Classe";
			// takePhoto();
			// save the current class
			this.saveScreen();
			showMessage(text);
			nomClasse.setText("");
			nomProfClasse.setText("");
			nbrEleve.setText("");
			emailProf.setText("");
			phoneProf.setText("");
			photoProf = null;
			this.photoClasse = null;
			Intent i = new Intent(this, Classe_Activity.class);
			Bundle b = new Bundle();
			b.putInt("idClasse", idClasse);
			b.putInt("instId", instId);
			i.putExtras(b);
			startActivity(i);
			finish();
			break;
		case R.id.photoClasse:

			// onLocationChanged(location);
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PHOTO_CLASSE_CODE);
			break;

		case R.id.actionProfPics:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PHOTO_PROF_CODE);
			break;

		case R.id.previewClasses:
			this.saveScreen();
			Intent i2 = new Intent(this, Liste_Classes_Prof.class);
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
