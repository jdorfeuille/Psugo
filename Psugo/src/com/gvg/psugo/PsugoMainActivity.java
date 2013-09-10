package com.gvg.psugo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
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
import android.widget.SimpleAdapter;
import android.widget.Toast; // only Temporary for creating message
import android.graphics.Bitmap;
import android.graphics.Color;
public class PsugoMainActivity extends Activity implements OnClickListener {

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
	// Adapters
	ArrayAdapter<String> adapterSectComm;
	//SimpleAdapter adapterSectComm;
	
	// Text Fields that needs to be save
	
	EditText adrEcole, adrDetEcole;
	EditText deptEcole;
	Spinner sectCommunale, commune;
	EditText arrondissement;
	EditText phoneEcole, infoBancaire;
	
	// Layout

	ScrollView scrollView1;
	RelativeLayout mainRelative;
	// Pictures
//	private ImageView imageView;

	private static final int TAKE_PHOTO_CODE = 1;

	Uri imageUri;
	
	//Activities
	private static final int ADD_DIRECTEURS = 10;
	private static final int ADD_PICS = 100;
	private static final int ADD_CLASSES = 200;
	private static final int PSUGO_LOGIN = 999;


	int ctlLocation=0;
	double schoolLatitude ;
	double schoolLongitude;
	LocationManager locationManager;
	Location location = null;
	Location  lastLocation = null;
	String provider;
	String theUserName ;
	String thePassword;
	//boolean isNetworkAvailable;
	//
	// Temporary data 
	TempData tempData; 
	String nomEcoleSelected, sectCommSelected, communeSelected ;
	String ecoleTrouveSelected;
	String[] listNomImst;
	String[] listSectRurale = {""}; // 
	String[] listCommune    = {""};
	int idxEcoleSelected;
	int instId; 
	//ArrayList<Photo> photoList = new ArrayList<Photo>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.activity_psugo_main);
        
        // get Data that will populate the UI fields
       
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			theUserName = extras.getString("theUserName");
			thePassword = extras.getString("thePassword");
			//isNetworkAvailable = extras.getBoolean("isNetworkAvailable");
		}
        tempData = this.getRequiredUIData();
        //listCommune = getDistinctListCommune(); JW
        //listSectRurale = getListSectionRurale();
        listNomImst = getListNomInst();
        processListInst();
        //
      
        nomEcole = (AutoCompleteTextView )findViewById(R.id.nomEcole);
        //nomEcole.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listNomImst);
        
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //android.R.layout.simple_dropdown_item_1line
        nomEcole.setAdapter(adapter);
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
					//sectCommunale.setTop(tempData.instArray[idx].sectionRurale);
					deptEcole.setText(tempData.instArray[idx].departement);
					arrondissement.setText(tempData.instArray[idx].arrondissement);
					//commune.setTop(tempData.instArray[idx].commune);
				}

			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// main = arg0
				// position = arg2
				// Id = arg3
				System.out.println("nomEcole=====>onItemSelected");
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
        	
        	public void updateSectCommuneFields(String item) {
				
				ArrayList<String> sectRList = new ArrayList<String>();
				int nElem=0;
				for(int i=0;i<tempData.csrArray.length;i++){
					if (tempData.csrArray[i].commune.equals(item) ) {
						sectRList.add(tempData.csrArray[i].sectionRurale);
						nElem++;
					}
				}
				listSectRurale = new String[nElem];
				int idx = 0;
				Iterator<String> el = sectRList.iterator();
				while (el.hasNext()) {
					listSectRurale[idx] = el.next();
					idx++;
				}
				// update Commune
				String [] tempCommuneList = getDistinctListCommune();
				listCommune = new String[tempCommuneList.length];
				listCommune[0] = item; // set the first Item of the list to the received Commune
				idx=0;
				int j=1;
				while ( idx < tempCommuneList.length) {
					if ( item.equalsIgnoreCase(tempCommuneList[idx])){
						idx++;
						
					}
					else {
						listCommune[j] = tempCommuneList[idx];
						j++;
						idx++;
					}
				}
		        ArrayAdapter<String> adapterCommune = new ArrayAdapter<String>(PsugoMainActivity.this, android.R.layout.simple_dropdown_item_1line, listCommune);
		        commune.setAdapter(adapterCommune);
				//sectRuraleAdapter to update
				adapterSectComm = new ArrayAdapter<String>(PsugoMainActivity.this, android.R.layout.simple_dropdown_item_1line,  listSectRurale);
				sectCommunale.setAdapter(adapterSectComm);


			}
        	
			public void updateUIFields() {
				int idx = PsugoMainActivity.this.getIdxString(nomEcoleSelected,
						listNomImst);
				if (idx > -1) {
					instId = tempData.instArray[idx].id;
					adrEcole.setText(tempData.instArray[idx].adresse);
					adrDetEcole.setText(tempData.instArray[idx].adresseDetail);
					updateSectCommuneFields(tempData.instArray[idx].commune);
					deptEcole.setText(tempData.instArray[idx].departement);
					infoBancaire.setText(tempData.instArray[idx].infoBancaire);
					arrondissement
							.setText(tempData.instArray[idx].arrondissement);
					// test trouve(o/n)
					arrondissement
					.setText(tempData.instArray[idx].arrondissement);
				
				}

			}
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {	
                // TODO Auto-generated method stub
        		
        		// got called System.out.println("nomEcole=====>onItemClick");
        		nomEcoleSelected = (String)arg0.getItemAtPosition(arg2).toString();
        		updateUIFields();
        		//Toast.makeText(getApplicationContext(),(CharSequence)arg0.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
        	}
        });
        
      
        // Commune 
        commune = (Spinner )findViewById(R.id.instCommune);
        ArrayAdapter<String> adapterCommune = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listCommune);
        
        //adapterCommune.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //android.R.layout.simple_dropdown_item_1line or simple_spinner_item
        commune.setAdapter(adapterCommune);
        commune.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			public void updateSectCommuneFields(String item) {
				
				ArrayList<String> sectRList = new ArrayList<String>();
				int nElem = 0;
				for(int i=0;i<tempData.csrArray.length;i++){
					if (tempData.csrArray[i].commune.equals(item) ) {
						sectRList.add(tempData.csrArray[i].sectionRurale);
						nElem++;
					}
				}

				listSectRurale = new String[nElem];
				int idx = 0;
				Iterator<String> el = sectRList.iterator();
				while (el.hasNext()) {
					listSectRurale[idx] = el.next();
					idx++;
				}
				
				adapterSectComm = new ArrayAdapter<String>(PsugoMainActivity.this, android.R.layout.simple_dropdown_item_1line,  listSectRurale);
				sectCommunale.setAdapter(adapterSectComm);
			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// main = arg0
				// position = arg2
				// Id = arg3
				//got called System.out.println("commune=====>onItemSelected");
				String item = arg0.getItemAtPosition(arg2).toString() ;
				communeSelected = item;
				updateSectCommuneFields(item);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        // Sect Communale
        sectCommunale = (Spinner)findViewById(R.id.sectCommunale); 
        adapterSectComm = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, listSectRurale);
        adapterSectComm.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //android.R.layout.simple_dropdown_item_1line or simple_spinner_item
        adapterSectComm.setNotifyOnChange(true); 
        sectCommunale.setAdapter(adapterSectComm);
        sectCommunale.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// main = arg0
				// position = arg2
				// Id = arg3
				// this method got called 
				String item = arg0.getItemAtPosition(arg2).toString() ;
				sectCommSelected = item;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        //

        adrEcole = (EditText)findViewById(R.id.adrEcole);   
        adrDetEcole = (EditText)findViewById(R.id.adrDetaillee); 
      
        deptEcole = (EditText)findViewById(R.id.dept);
        deptEcole.setEnabled(false);
        arrondissement = (EditText)findViewById(R.id.arrondissement); 
        arrondissement.setEnabled(false);
        
        phoneEcole = (EditText)findViewById(R.id.phoneEcole);
        adrDetEcole=(EditText)findViewById(R.id.adrDetaillee);
        infoBancaire = (EditText)findViewById(R.id.infoBancaire);
       
        // Pictures
       // imageView = (ImageView) findViewById(R.id.imgPrev);
        //Scroller 
        scrollView1 = (ScrollView)findViewById(R.id.scroller);
        scrollView1.setEnabled(true);
        
        //Layout
        mainRelative = (RelativeLayout)findViewById(R.id.mainRelative);
        
        int maxScrollPosition = mainRelative.getHeight() - scrollView1.getHeight();    
        scrollView1.scrollTo(0,maxScrollPosition);
        
        //Spinner
        ecoleTrouveeList = (Spinner) findViewById(R.id.ecoleTrouveeList); // check to see if we can set to what was selected before 09/07
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
       // actOk = (Button)findViewById(R.id.Ok);
        //actOk.setOnClickListener(this);
        
        //GPS Coordinates
        // Getting LocationManager object
        LocationListener myLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            if(lastLocation == null){
            lastLocation = location;
    		schoolLatitude = location.getLatitude();
    		schoolLongitude = location.getLongitude();

            }
            if (location.getAccuracy() <  lastLocation.getAccuracy() || lastLocation.getTime() + 5 * 60 * 1000 > location.getTime()){
            lastLocation = location;
    		schoolLatitude = location.getLatitude();
    		schoolLongitude = location.getLongitude();

            }
            }            
            public void onProviderDisabled(String provider) {}
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        }; 
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 60*1000, 10, myLocationListener);

        

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
	

	public void saveNewComsectr(CommuneSectRurale[] theCsrs) {
		PsugoDB psudb = new PsugoDB(getBaseContext());
		psudb.open();
		psudb.delete_ALL_ComsectR();
		for (int i=0;i<theCsrs.length;i++) {
			CommuneSectRurale csr = theCsrs[i];
			psudb.insertComsectR(csr.commune, csr.sectionRurale);
		}
		psudb.close();
		
	}
    
	
	private String[] getListSectionRurale() {
		String[] res = new String[tempData.csrArray.length];
		for (int i=0; i<tempData.csrArray.length;i++){
			res[i] = tempData.csrArray[i].sectionRurale;
		}
		return res;
	}
	
	private String[] getDistinctListCommune() {
		String tempCommune = "";
		ArrayList<String> dListCommune = new ArrayList<String>();
		for(int i=0;i<tempData.csrArray.length;i++){
			if (tempData.csrArray[i].commune.equals(tempCommune) == false ) {
				tempCommune = tempData.csrArray[i].commune;
				dListCommune.add(tempCommune);
			}
			i++;
		}
		String[] res = new String[dListCommune.size()];
		int idx = 0;
		Iterator<String> el = dListCommune.iterator();
		while (el.hasNext()) {
			res[idx] = el.next();
			idx++;
		}
		return res;
	}
	
	
	private TempData getRequiredUIData() {

		TempData resp = null;
		PsugoUtils pscn = new PsugoUtils(this.getBaseContext());
		if (pscn.isNetworkAvailable()) {
			try {
				PsugoServiceClientHelper psch = new PsugoServiceClientHelper(
						getBaseContext());

				AsyncTask<String, String, TempData> servCall_Login = psch
						.execute(new String[] { "Section", theUserName,
								thePassword });
				resp = servCall_Login.get();
				saveNewComsectr(resp.csrArray);

			} catch (Exception e) {
				System.out.println("Erreur Fatale pas de donnees pour l'application ... ");
				//e.printStackTrace(); //Irrecoverable exception needs to handle properly
			}
		} else {
			
			PsugoDB psudb = new PsugoDB(getBaseContext());
			psudb.open();
			resp = new TempData();
			resp.instArray = psudb.selectInstitution();
			resp.csrArray = psudb.selectComsectR();
			psudb.close();

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
				psudb.deleteInstitution(myDbInst[i].id); //removing existing institution no longer sent
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
			//debugInstitution("processListInst - printing tempDataList", tempInst);
			String schoolName = tempData.instArray[i].nomInstitution;
			idx = getIdxString(schoolName, instArrayDB);
			if (idx > -1) { 
				// we found the inst. in the local DB
//				if (tempInst.telephone == null )
//					tempInst.telephone = "";
//				if (tempInst.instTrouvee == null )
//					tempInst.instTrouvee = "";
//				psudb.updateInstitution(tempInst.id, tempInst.nomInstitution,
//						tempInst.commune, tempInst.sectionRurale,
//						tempInst.adresse, tempInst.adresseDetail,
//						tempInst.telephone, tempInst.instTrouvee);
			} else {
				// Insert Institution
				
				psudb.insertInstitution(tempInst.id, tempInst.nomInstitution,tempInst.departement,
						tempInst.arrondissement,tempInst.commune, tempInst.sectionRurale, tempInst.infoBancaire);

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
          case PSUGO_LOGIN:
        	  if(resultCode == RESULT_OK){
        		CharSequence text = "Returned from LOGIN";
          	  theUserName=data.getStringExtra("theUserName");  
          	  thePassword=data.getStringExtra("thePassword");  
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
    private void saveCurrentData() {
	    Institution myUpdatedInst = createInstitutionFromUI();
		 myUpdatedInst.id = instId;
		 PsugoDB psudb = new PsugoDB(getBaseContext());
		 psudb.open();
		 psudb.updateInstitution(instId, myUpdatedInst.nomInstitution,
				 myUpdatedInst.commune, myUpdatedInst.sectionRurale,
				 myUpdatedInst.adresse, myUpdatedInst.adresseDetail,
				 myUpdatedInst.telephone, myUpdatedInst.instTrouvee,
				 myUpdatedInst.infoBancaire);
		 
		 psudb.close();
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
			this.saveCurrentData();
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
		/*case R.id.Ok:
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
			 break; */
		case R.id.actionUploadData:
			// here all we will do is to loop thru the DB and pass data 
			// text = "'upload data ' clicked! need method to upload data";
			// just for today we will test the WS here
			//Context c = this.getBaseContext();
			PsugoUtils pscn = new PsugoUtils(this.getBaseContext());
			if (pscn.isNetworkAvailable()) {
				PsugoSendDataParm psdp = new PsugoSendDataParm(this.getBaseContext(), theUserName, thePassword);
				//PsugoSendDataParm pdp = new 
				try {
					PsugoSendClientDataHelper psch = new PsugoSendClientDataHelper();
					AsyncTask<PsugoSendDataParm, String, String> servCall_send = psch.execute(psdp);
					String resp = servCall_send.get();
					
				} catch (Exception e) {
					//System.out.println("Exception ... JW...failed UPLOAD service call");
					CharSequence text1 = "Erreur dans le transfert de donnees...Veuillez Re-essayer plus tard...";
					showMessage(text1);
					e.printStackTrace();
	
				}
			}
			else {
					CharSequence text = "Fonction Non Disponible actuellement Pas de Services Reseaux";
					showMessage(text);
				
			}

			break;
		case R.id.actionAddDirects:
			// here we can save Institution data so we can do an update
			this.saveCurrentData();
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
			this.saveCurrentData();
			Intent requestClasse =new Intent(this, Classe_Activity.class);
			b = null;
			b = new Bundle();
			b.putInt("idClasse", ADD_CLASSES);
			b.putInt("instId", instId);
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
		System.out.println("MyInstitution.infoBancaire = " + myInstitution.infoBancaire);
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
		myInstitution.sectionRurale = sectCommSelected;
		myInstitution.commune = communeSelected;
		myInstitution.telephone = phoneEcole.getText().toString();
		if (myInstitution.telephone == null )
			myInstitution.telephone = "";
		myInstitution.instTrouvee = ecoleTrouveSelected;
		if (myInstitution.instTrouvee == null )
			myInstitution.instTrouvee = "";
		
		myInstitution.infoBancaire = infoBancaire.getText().toString();
		// debug printing the object
		
		//debugInstitution("createInstitutionFromUI", myInstitution);
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


	
}
