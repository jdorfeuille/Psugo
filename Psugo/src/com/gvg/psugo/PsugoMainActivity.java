package com.gvg.psugo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.database.SQLException;

//Web Service tool
import org.ksoap2.SoapEnvelope;
import org.kobjects.base64.Base64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;  
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.app.Activity;
import android.content.ContentResolver;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.content.Intent;
import android.widget.Spinner;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast; // only Temporary for creating message
import android.graphics.Bitmap;
import android.graphics.Color;
public class PsugoMainActivity extends Activity implements OnClickListener, LocationListener {

	// Composantes d'Interface graphique
	Button actionSchoolPic;
	Button xferInfosBtn;
	Button doneBtn;
	Button actOk;
	Button actionAddDirects;
	Button actionAddClasses;
	
	//Spinner
	Spinner ecoleTrouveeList;
	AutoCompleteTextView nomEcole; 
	
	// Text Fields that needs to be save
	
	EditText adrEcole, adrDetEcole;
	EditText deptEcole;
	EditText sectCommunale, commune;
	EditText arrondissement;
	EditText phoneEcole;
	
	// Layout

	ScrollView scrollView1;
	RelativeLayout mainRelative;
	// Pictures
	private ImageView imageView;

	private static final int TAKE_PHOTO_CODE = 1;

	Uri imageUri;
	
	//Activities
	private static final int ADD_DIRECTEURS = 10;
	private static final int ADD_PICS = 100;
	private static final int ADD_CLASSES = 200;
	private static final int ADD_PROFS = 300;
	
	
	// Location Control "Make sure to get the location only once for this instance
	// we need to make sure we test this
	
	
	
	
	int ctlLocation=0;
	double schoolLatitude ;
	double schoolLongitude;
	LocationManager locationManager;
	Location location;
	String provider;
	//
	// Temporary data 
	TempData tempData; 
	String nomEcoleSelected ;
	String ecoleTrouveSelected;
	String[] listNomImst;
	int idxEcoleSelected;
	int instId; 
	//ArrayList<Photo> photoList = new ArrayList<Photo>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_psugo_main);
        
        // get Data that will populate the UI fields
       
        tempData = this.getRequiredUIData();
        listNomImst = getListNomInst();
        processListInst();
        
        // nomEcole.getText().toString(); to get the string value... 
        // to get the int value Integer.parseInt(myEditText.getText().toString())).
        //nomEcole = (EditText)findViewById(R.id.nomEcole);
        //Spinner   nomEcole = (Spinner  )findViewById(R.id.nomEcole);
        
        nomEcole = (AutoCompleteTextView )findViewById(R.id.nomEcole);
        //nomEcole.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listNomImst);
        
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //android.R.layout.simple_dropdown_item_1line
        nomEcole.setAdapter(adapter);
        //nomEcole.showDropDown();
        //nomEcole.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        
        nomEcole.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
            	nomEcole.showDropDown();               
            }
        });
       
        
        nomEcole.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			public void updateUIFields(String item) {
				int idx = PsugoMainActivity.this
						.getIdxString(item, listNomImst);
				if (idx > -1) {
					instId = tempData.instArray[idx].id;
					adrEcole.setText(tempData.instArray[idx].adresse);
					adrDetEcole.setText(tempData.instArray[idx].adresseDetail);
					sectCommunale
							.setText(tempData.instArray[idx].sectionRurale);
					deptEcole.setText(tempData.instArray[idx].departement);
					arrondissement
							.setText(tempData.instArray[idx].arrondissement);
					commune.setText(tempData.instArray[idx].commune);
				}

			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// main = arg0
				// position = arg2
				// Id = arg3
				String item = arg0.getItemAtPosition(arg2).toString() ;
				nomEcoleSelected = item;
				updateUIFields(item);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        
        
        
        nomEcole.setOnItemClickListener( new OnItemClickListener() {
        	
			public void updateUIFields() {
				int idx = PsugoMainActivity.this.getIdxString(nomEcoleSelected,
						listNomImst);
				if (idx > -1) {
					instId = tempData.instArray[idx].id;
					adrEcole.setText(tempData.instArray[idx].adresse);
					adrDetEcole.setText(tempData.instArray[idx].adresseDetail);
					sectCommunale
							.setText(tempData.instArray[idx].sectionRurale);
					deptEcole.setText(tempData.instArray[idx].departement);
					arrondissement
							.setText(tempData.instArray[idx].arrondissement);
					commune.setText(tempData.instArray[idx].commune);
				}

			}
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {	
                // TODO Auto-generated method stub
        		nomEcoleSelected = (String)arg0.getItemAtPosition(arg2).toString();
        		System.out.println("valueSelected = " + nomEcoleSelected);
        		updateUIFields();
        		Toast.makeText(getApplicationContext(),(CharSequence)arg0.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
        	}
        });
        
        
        //
        adrEcole = (EditText)findViewById(R.id.adrEcole);   
        adrDetEcole = (EditText)findViewById(R.id.adrDetaillee); 
        sectCommunale = (EditText)findViewById(R.id.sectCommunale);
        deptEcole = (EditText)findViewById(R.id.dept);
        arrondissement = (EditText)findViewById(R.id.arrondissement);
        commune = (EditText)findViewById(R.id.instCommune);
        phoneEcole = (EditText)findViewById(R.id.phoneEcole);
        adrDetEcole=(EditText)findViewById(R.id.adrDetaillee);
       
        // Pictures
        imageView = (ImageView) findViewById(R.id.imgPrev);
        //Scroller 
        scrollView1 = (ScrollView)findViewById(R.id.scroller);
        scrollView1.setEnabled(true);
        
        //Layout
        mainRelative = (RelativeLayout)findViewById(R.id.mainRelative);
        
        int maxScrollPosition = mainRelative.getHeight() - scrollView1.getHeight();    
        scrollView1.scrollTo(0,maxScrollPosition);
        
        //Spinner
        ecoleTrouveeList = (Spinner) findViewById(R.id.ecoleTrouveeList);
        ecoleTrouveeList.setOnItemSelectedListener(new OnItemSelectedListener() {

    			@Override
    			public void onItemSelected(AdapterView<?> arg0, View arg1,
    					int arg2, long arg3) {

    				String item = arg0.getItemAtPosition(arg2).toString() ;
    				ecoleTrouveSelected = item;

    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				
    			}
            	

        });
        /*
        ecoleTrouveeList.setOnItemClickListener( new OnItemClickListener() {
        	
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {	
                // TODO Auto-generated method stub
        		ecoleTrouveSelected = (String)arg0.getItemAtPosition(arg2).toString();
        		System.out.println("valueSelected = " + nomEcoleSelected);
        	}
        });
        */
        
        // Buttons 
        actionSchoolPic = (Button)findViewById(R.id.actionSchoolPic);
        actionSchoolPic.setOnClickListener(this);
        xferInfosBtn = (Button)findViewById(R.id.actionUploadData);
        xferInfosBtn.setOnClickListener(this);
        doneBtn = (Button)findViewById(R.id.actionDone);
        doneBtn.setOnClickListener(this);
        actionAddDirects = (Button)findViewById(R.id.actionAddDirects);
        actionAddDirects.setOnClickListener(this);
        //jwd
        actionAddClasses =  (Button)findViewById(R.id.actionAddClasses);
        actionAddClasses.setOnClickListener(this);
        
        //Test Button to remove
        actOk = (Button)findViewById(R.id.Ok);
        actOk.setOnClickListener(this);
        
        //GPS Coordinates
        // Getting LocationManager object
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if ( !enabled ) {
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
        }
        else
            Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        

   }


    private String[] getListNomInst() {
    	int instArrayLen = tempData.instArray.length;
    	String[] instArray = new String[instArrayLen];
    	for(int i=0;i<instArrayLen;i++) {
    		instArray[i]=tempData.instArray[i].nomInstitution;
    	}
    	return instArray;
    	
    }
    
	private int getIdxString(String sLookup, String[] arrayToLook) {
		int idx = -1;

		for (int i = 0; i < arrayToLook.length; i++) {
			if (sLookup.equalsIgnoreCase(arrayToLook[i])) {
				idx = i;
				break;
			}
		}
		return idx;
	}
    
    private TempData getRequiredUIData() {
    	
    	TempData resp = null;
    	try {
			PsugoServiceClientHelper psch = new PsugoServiceClientHelper();

			AsyncTask<String, String, TempData> servCall_Login = psch.execute(new String[] { "Login" });
			resp = servCall_Login.get();
			
		} catch (Exception e) {
			System.out.println("Erreur Fatale pas de donnees pour l'application ... ");
			e.printStackTrace();

		}
    	return resp;
    }
    
    /*
     * 
     */
	private void processListInst() {
		PsugoDB psudb = new PsugoDB(getBaseContext());
		psudb.open();
		Institution[] myDbInst = psudb.selectInstitution();
		Institution tempInst;
		int idx = -1;
		// clean up institution
		for (int i = 0; i < myDbInst.length; i++) {
			String schoolName = myDbInst[i].nomInstitution;
			idx = getIdxString(schoolName, listNomImst);
			if (idx == -1) {
				psudb.deleteInstitution(myDbInst[i].id);
			}
		}
		// update les institutions recu..
		// rafraichir la liste de la bd
		myDbInst = psudb.selectInstitution();
		int instArrayLen = myDbInst.length;
		String[] instArrayDB = new String[instArrayLen];
		for (int i = 0; i < instArrayLen; i++) {
			instArrayDB[i] = myDbInst[i].nomInstitution;
		}
		for (int i = 0; i < tempData.instArray.length; i++) {
			tempInst = tempData.instArray[i];
			debugInstitution("processListInst - printing tempDataList", tempInst);
			String schoolName = tempData.instArray[i].nomInstitution;
			idx = getIdxString(schoolName, instArrayDB);
			if (idx > -1) {
				if (tempInst.telephone == null )
					tempInst.telephone = "";
				if (tempInst.instTrouvee == null )
					tempInst.instTrouvee = "";
				psudb.updateInstitution(tempInst.id, tempInst.nomInstitution,
						tempInst.commune, tempInst.sectionRurale,
						tempInst.adresse, tempInst.adresseDetail,
						tempInst.telephone, tempInst.instTrouvee);
			} else {
				// Insert Institution
				psudb.insertInstitution(tempInst.id, tempInst.nomInstitution,tempInst.departement,
						tempInst.arrondissement,tempInst.commune, tempInst.sectionRurale);

			}
		}
		psudb.close();
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
     
    private void takePhoto(){
    	  final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPicFile(this)) ); 
    	  startActivityForResult(intent, TAKE_PHOTO_CODE);
    	}

*/
    private File getPicFile(Context context){
    	  //it will return /sdcard/image.tmp
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    	  if(!path.exists()){
    	    path.mkdir();
    	  }
    	  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		  String filename = "Ecole_" + timeStamp + ".jpg";
    	  return new File(path, filename);
    	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK) {
        switch(requestCode){
          case TAKE_PHOTO_CODE:
        	 System.out.println("what am i doing here !!!!!!!!!!!!");
        	 //here 
            final File file = getPicFile(this);
            try {
              Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
              // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
              CharSequence text = file.getName();
              showMessage(text);
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          break;
          case ADD_DIRECTEURS:
        	  if(resultCode == RESULT_OK){
        		CharSequence text = "Returned from Adding Directeurs";
      			showMessage(text);
      		}
        	  break;
          case ADD_PICS:
        		CharSequence text = "Returned from Taking Pictures";
        		System.out.println("ok i did take some pic");
      			showMessage(text);
      		
        	  break;
        }
      }
    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values
		
		Bundle b;
		//CharSequence text;
		switch (v.getId()) {
		case R.id.actionDone:
			//text = "'Done' clicked!";
			finish();
			break;
		case R.id.actionSchoolPic:
			//CharSequence text = "Activating the camera for a picture";
			//takePhoto();
			//showMessage(text);
			
			Intent camIntent = new Intent(this, PsugoCameraHelper.class);
			b = null;
			b = new Bundle();
			b.putInt("idPhoto", ADD_PICS);
			b.putInt("instId", instId);
			camIntent.putExtras(b);
			startActivityForResult(camIntent, ADD_PICS);
			break;
		case R.id.Ok:
			 Institution myUpdatedInst = createInstitutionFromUI();
			 myUpdatedInst.id = instId;
			 debugInstitution("onClick avant update", myUpdatedInst);
			 PsugoDB psudb = new PsugoDB(getBaseContext());
			 psudb.open();
			 psudb.updateInstitution(instId, myUpdatedInst.nomInstitution,
					 myUpdatedInst.commune, myUpdatedInst.sectionRurale,
					 myUpdatedInst.adresse, myUpdatedInst.adresseDetail,
					 myUpdatedInst.telephone, myUpdatedInst.instTrouvee);
			 
			 psudb.close();
			 System.out.println("Bouton Sauvegarde a fait l'Insert sans trop de prob");
			 break;
		case R.id.actionUploadData:
			// here all we will do is to loop thru the DB and pass data 
			// text = "'upload data ' clicked! need method to upload data";
			// just for today we will test the WS here
			Context c = this.getBaseContext();
			//PsugoSendDataParm pdp = new 
			try {
				PsugoSendClientDataHelper psch = new PsugoSendClientDataHelper();
				AsyncTask<Context, String, String> servCall_send = psch.execute(c);
				String resp = servCall_send.get();
				
			} catch (Exception e) {
				System.out.println("Exception ... JW...failed UPLOAD service call");
				e.printStackTrace();

			}

			break;
		case R.id.actionAddDirects:
			// here we can save Institution data so we can do an update
			Intent request =new Intent(this, Directeur_Activity.class);
			b = null;
			b = new Bundle();
			b.putInt("idDir", ADD_DIRECTEURS);
			b.putInt("instId", instId);
			request.putExtras(b);
			startActivityForResult(request, ADD_DIRECTEURS);
			break;
			
		case R.id.actionAddClasses:
			// here we can save Institution data so we can do an update
			Intent requestClasse =new Intent(this, Classe_Activity.class);
			b = null;
			b = new Bundle();
			b.putInt("idClasse", ADD_CLASSES);
			requestClasse.putExtras(b);
			startActivityForResult(requestClasse, ADD_CLASSES);
			break;
		default:
			//text = "Dunno what was pushed!"; actionAddClasses
	}


	}

	private void debugInstitution(String fromMethod, Institution myInstitution) {
		System.out.println("Printing from method: "+ fromMethod);
		System.out.println("MyInstitution.id = " + myInstitution.id);
		System.out.println("MyInstitution.nomInstitution = " + myInstitution.nomInstitution);
		System.out.println("MyInstitution.adresse = " + myInstitution.adresse);
		System.out.println("MyInstitution.adresseDetail = " + myInstitution.adresseDetail);
		System.out.println("MyInstitution.arrondissement = " + myInstitution.arrondissement);
		System.out.println("MyInstitution.departement = " + myInstitution.departement);
		System.out.println("MyInstitution.sectionRurale = " + myInstitution.sectionRurale);
		System.out.println("MyInstitution.commune = " + myInstitution.commune);
		System.out.println("MyInstitution.telephone = " + myInstitution.telephone);
		System.out.println("MyInstitution.instTrouvee = " + myInstitution.instTrouvee);
		System.out.println("Done Debug");
	}

	private Institution createInstitutionFromUI() {
		// TODO Auto-generated method stub
		Institution myInstitution = new Institution();
		myInstitution.id = this.instId;
		myInstitution.nomInstitution = nomEcoleSelected;
		myInstitution.adresse = adrEcole.getText().toString();
		myInstitution.adresseDetail = adrDetEcole.getText().toString();
		myInstitution.arrondissement = arrondissement.getText().toString();
		myInstitution.departement = deptEcole.getText().toString();
		myInstitution.sectionRurale = sectCommunale.getText().toString();
		myInstitution.commune = commune.getText().toString();
		myInstitution.telephone = phoneEcole.getText().toString();
		if (myInstitution.telephone == null )
			myInstitution.telephone = "";
		myInstitution.instTrouvee = ecoleTrouveSelected;
		if (myInstitution.instTrouvee == null )
			myInstitution.instTrouvee = "";
		
		// debug printing the object
		
		debugInstitution("createInstitutionFromUI", myInstitution);
		//
		return myInstitution;
	}


	/**
	 * Method to save bitmap into internal memory
	 * @param image and context
	 */
	public File saveImageToInternalStorage(Bitmap image,Context context)
	{
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
			String filename = "Ecole_" + timeStamp + ".jpg";
			File file = new File(filename);
			
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			//FileOutputStream fos = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			// 100 means no compression, the lower you go, the stronger the compression
			fos.flush();
			fos.close();
			return file;
		}
		catch (Exception e) {
			Log.e("saveToInternalStorage()", e.getMessage());
		}
		return null;
	}



	// Location Methods
	@Override
	public void onLocationChanged(Location location) {

		// here we need to get the location
		schoolLatitude = location.getLatitude();
		schoolLongitude = location.getLongitude();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		ctlLocation = 0;  //reset the variable so we can get the location
		
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
		
	}
	// End Location Methods




	
}
