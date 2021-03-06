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
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
public class PsugoMainActivity extends Activity implements OnClickListener, PsugoOnTaskCompleted {

	// Composantes d'Interface graphique
	Button actionSchoolPic;
	Button xferInfosBtn;
	Button doneBtn;
	Button actOk;
	Button actionAddDirects, actionSave;
	Button actionAddClasses;
	Button actionCin_Commentaires;
	
	//Spinner
	Spinner ecoleTrouveeList;
	Spinner nomEcole; 
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
	private static final int ADD_COMMENTS = 500;
	private static final int ADD_CLASSES = 200;
	private static final int PSUGO_LOGIN = 999;
	private static final int PSUGO_INST_NULL = -9999;


	int ctlLocation=0;
	static double schoolLatitude ;
	static double schoolLongitude;
	LocationManager locationManager;
	Location location = null;
	Location  lastLocation = null;
	String provider;
	String theUserName ;
	String thePassword;
	String cin_commentaires;
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
	int instId = PSUGO_INST_NULL; 
	boolean isXferRunning = false;
	//Boolean doneDisplay = false;
	PsugoOnTaskCompleted potc;
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
        nomEcoleSelected="";
        listNomImst = getListNomInst();
        processListInst();
        //
        // ------- NOM ECOLE
        nomEcole = (Spinner )findViewById(R.id.nomEcole);
        //nomEcole.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
       		android.R.layout.simple_dropdown_item_1line, listNomImst);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
               //android.R.layout.simple_dropdown_item_1line
        nomEcole.setAdapter(adapter);
       		/*
               nomEcole.setOnClickListener(new OnClickListener() {
                   
                   public void onClick(View v) {
                   	nomEcole.showDropDown();               
                   }
       			
               });
       		*/
       	nomEcole.setOnItemSelectedListener( new OnItemSelectedListener() {
               	
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
       		        // init communeSelected
       		        //	String[] tmp = getResources().getStringArray(R.array.genre_humain_array);
       		        //	communeSelected = tmp[0];
       				//sectRuraleAdapter to update
       				adapterSectComm = new ArrayAdapter<String>(PsugoMainActivity.this, android.R.layout.simple_dropdown_item_1line,  listSectRurale);
       				sectCommunale.setAdapter(adapterSectComm);


       			}
               	
       			public void updateUIFields() {
       				//here no change necessary
       				int idx = PsugoMainActivity.this.getIdxString(nomEcoleSelected,
       						listNomImst);
       				if (idx > -1) {
       					idxEcoleSelected = idx;
       					instId = tempData.instArray[idx].id;
       					adrEcole.setText(tempData.instArray[idx].adresse);
       					adrDetEcole.setText(tempData.instArray[idx].adresseDetail);
       					phoneEcole.setText(tempData.instArray[idx].telephone);
       					updateSectCommuneFields(tempData.instArray[idx].commune);
       					deptEcole.setText(tempData.instArray[idx].departement);
       					infoBancaire.setText(tempData.instArray[idx].infoBancaire);
       					cin_commentaires=tempData.instArray[idx].cin;
       					
       					arrondissement
       							.setText(tempData.instArray[idx].arrondissement);
       					String ecoleTrouvee = tempData.instArray[idx].instTrouvee;
						String temp = (String) ecoleTrouveeList.getItemAtPosition(0);
						if (temp.equalsIgnoreCase(ecoleTrouvee)) {
							ecoleTrouveeList.setSelection(0);
							ecoleTrouveSelected = temp;
						}
						else {
								ecoleTrouveeList.setSelection(1);
								ecoleTrouveSelected = (String) ecoleTrouveeList.getItemAtPosition(1);
						}

       				}
       			}
       				
               	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {	
                       // TODO Auto-generated method stub
               		
               		// got called System.out.println("nomEcole=====>onItemClick");
               		nomEcoleSelected = (String)arg0.getItemAtPosition(arg2).toString();
               		updateUIFields();
               		//Toast.makeText(getApplicationContext(),(CharSequence)arg0.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
               	}
       			@Override
       			public void onNothingSelected(AdapterView<?> arg0) {
       				// TODO Auto-generated method stub
       			//	String[] tmp = getResources().getStringArray(R.array.type_directeur_array);
       			//	communeSelected = tmp[0];
       				 
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
			//	String[] tmp = getResources().getStringArray(R.array.type_directeur_array);
			//	communeSelected = tmp[0];
				 
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

        
        // Buttons 
        actionSchoolPic = (Button)findViewById(R.id.actionSchoolPic);
        actionSchoolPic.setOnClickListener(this);
        actionCin_Commentaires = (Button)findViewById(R.id.cin);
        actionCin_Commentaires.setOnClickListener(this);
        xferInfosBtn = (Button)findViewById(R.id.actionUploadData);
        xferInfosBtn.setOnClickListener(this);
        doneBtn = (Button)findViewById(R.id.actionDone);
        doneBtn.setOnClickListener(this);
        actionAddDirects = (Button)findViewById(R.id.actionAddDirects);
        actionAddDirects.setOnClickListener(this);
        //jwd
        actionAddClasses =  (Button)findViewById(R.id.actionAddClasses);
        actionAddClasses.setOnClickListener(this);
        
     //   actionSave =  (Button)findViewById(R.id.actionSave);
      //  actionSave.setOnClickListener(this);
        
        //Test Button to remove
       // actOk = (Button)findViewById(R.id.Ok);
        //actOk.setOnClickListener(this);
        
        //GPS Coordinates
        // Getting LocationManager object
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
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5*60*1000, 10, myLocationListener);
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // si on a les 2 localisations
        if(location != null && lastLocation != null){
        	// on prend la plus precise (accuracy la plus petite)
        	if(location.getAccuracy() > lastLocation.getAccuracy()){
        		schoolLatitude = lastLocation.getLatitude();
        		schoolLongitude = lastLocation.getLongitude();
        	}
        	else{
        		schoolLatitude = location.getLatitude();
        		schoolLongitude = location.getLongitude();
        	}
        }
        else if (location != null){
        	schoolLatitude = location.getLatitude();
        	schoolLongitude = location.getLongitude();
        }
        else if (lastLocation != null){
        	schoolLatitude = lastLocation.getLatitude();
        	schoolLongitude = lastLocation.getLongitude();
        }
        else {
        	schoolLatitude = 0;
        	schoolLongitude = 0;
        }
   }

        

   


    private String[] getListNomInst() {
    	// here we have to concatenate name = name + id
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
				e.printStackTrace();
				//System.out.println("Erreur Fatale pas de donnees pour l'application ... ");
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
			String schoolName = myDbInst[i].nomInstitution; //here we have to concatenate instID + name
			idx = getIdxString(schoolName, listNomImst);
			if (idx == -1) {
				psudb.deleteInstitution(myDbInst[i].id); //removing existing institution no longer sent
				Classe[] lClasses = psudb.selectClasse(myDbInst[i].id);
				for (int k=0;k<lClasses.length;k++){
					psudb.deleteClasse(myDbInst[i].id, lClasses[k].nomClasse);
				}
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
				// Data found Locally so we take update for display
				tempData.instArray[i].adresse = myDbInst[idx].adresse;
				tempData.instArray[i].adresseDetail = myDbInst[idx].adresseDetail;
				tempData.instArray[i].telephone =  myDbInst[idx].telephone;
				tempData.instArray[i].infoBancaire =   myDbInst[idx].infoBancaire;
				tempData.instArray[i].cin = myDbInst[idx].cin;
				cin_commentaires =  myDbInst[idx].cin;
				tempData.instArray[i].instTrouvee = myDbInst[idx].instTrouvee;
				ecoleTrouveSelected = myDbInst[idx].instTrouvee;
			} else {
				psudb.insertInstitution(tempInst.id, tempInst.nomInstitution,tempInst.departement,
						tempInst.arrondissement,tempInst.commune, tempInst.sectionRurale, 
						tempInst.adresse, tempInst.adresseDetail, tempInst.telephone,
						tempInst.infoBancaire, tempInst.cin, tempInst.instTrouvee);

			}
		}

		psudb.close();
	}
    
	public void displayMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		        	   //doneDisplay=true;
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		//alert.dismiss();
	}
    
    /* display a Toast with message text. */
    private void showMessage(CharSequence text) {
    	Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
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
        		//CharSequence text = "Returned from Adding Directeurs";
      			//showMessage(text);
      		}
        	  break;
          case ADD_COMMENTS:
        	  if(resultCode == RESULT_OK){
        		  cin_commentaires = data.getStringExtra("newComment");
        		  this.saveCurrentData();
      		}
        	  break;
          case PSUGO_LOGIN:
        	  if(resultCode == RESULT_OK){
        		//CharSequence text = "Returned from LOGIN";
          	  theUserName=data.getStringExtra("theUserName");  
          	  thePassword=data.getStringExtra("thePassword");  
      			//showMessage(text);
      		}
        	  break;
          case ADD_PICS:
        		//CharSequence text = "Returned from Taking Pictures";
        		//System.out.println("ok i did take some pic");
      			//showMessage(text);
      		
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
				 myUpdatedInst.infoBancaire, myUpdatedInst.cin);
		 
		 psudb.close();
		 

		 
    }
    
    public String getDimensions() {
    	
    	DisplayMetrics displaymetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    	int height = displaymetrics.heightPixels;
    	int width = displaymetrics.widthPixels;
    	return String.valueOf(height) + " par " + String.valueOf(width);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dummy method for now to test the buttons and display all values
		PsugoUtils pscn = new PsugoUtils(this.getBaseContext());
		String msg = "";
		Bundle b;
		//CharSequence text;
		switch (v.getId()) {
		case R.id.actionDone:
			//text = "'Done' clicked!";
			this.saveCurrentData();
			if (isXferRunning) {
				this.displayMessage("Vous ne Pouvez quittez pour le moment: Transfert en cours"); // to put in strings
			}
			else {
			
				finish();
			}
			break;
//		case R.id.actionSave:
			//text = "'Done' clicked!";
//			this.saveCurrentData();
//			break;
		case R.id.actionSchoolPic:
			//CharSequence text = "Activating the camera for a picture";
			//takePhoto();
			//showMessage(text);
			if ( instId != PSUGO_INST_NULL) {
				Intent camIntent = new Intent(this, PsugoCameraHelper.class);
				b = null;
				b = new Bundle();
				b.putInt("idPhoto", ADD_PICS);
				b.putInt("instId", instId);
				camIntent.putExtras(b);
				startActivityForResult(camIntent, ADD_PICS);
			}
			else {
				msg =getResources().getString(R.string.MsgNoInstSelected);
				this.displayMessage(msg);
			}
			break;
		case R.id.actionUploadData:
			this.saveCurrentData();
			isXferRunning = true;
			
			if (pscn.isNetworkAvailable()) {
				PsugoSendDataParm psdp = new PsugoSendDataParm(this.getBaseContext(), theUserName, thePassword);
				//PsugoSendDataParm pdp = new 
				try {
					msg = getResources().getString(R.string.MsgXferRunning);
					this.displayMessage(msg);
					String resp= "";
					potc = this;
					PsugoSendClientDataHelper psch = new PsugoSendClientDataHelper(potc);
					AsyncTask<PsugoSendDataParm, String, String> servCall_send = psch.execute(psdp);
					//* Test
					//resp = servCall_send.get();
					//*
					//System.out.println("response from xfer=" +resp);
				} catch (Exception e) {
					//System.out.println("Exception ... JW...failed UPLOAD service call");
					msg = getResources().getString(R.string.MsgXferFail);
					this.displayMessage(msg);
					//e.printStackTrace();
	
				}
			}
			else {
					msg =getResources().getString(R.string.MsgNoNetworkForFn);
					this.displayMessage(msg);
			}

			// Test refresh List
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		       		android.R.layout.simple_dropdown_item_1line, listNomImst);

			adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			
			// Test refresh List
			break;
		case R.id.actionAddDirects:
			// here we can save Institution data so we can do an update
			//this.displayMessage(this.getDimensions()); // to remove 
			
			if ( instId != PSUGO_INST_NULL) {
				this.saveCurrentData();
				Intent request =new Intent(this, Directeur_Activity.class);
				b = null;
				b = new Bundle();
				b.putInt("idDir", ADD_DIRECTEURS);
				b.putInt("instId", instId);
				request.putExtras(b);
				startActivityForResult(request, ADD_DIRECTEURS);
			}
			else {
				msg =getResources().getString(R.string.MsgNoInstSelected);
				this.displayMessage(msg);
			}
			break;
			
		case R.id.actionAddClasses:
			// here we can save Institution data so we can do an update
			if ( instId != PSUGO_INST_NULL) {
				this.saveCurrentData();
				Intent requestClasse =new Intent(this, Classe_Activity.class);
				b = null;
				b = new Bundle();
				b.putInt("idClasse", ADD_CLASSES);
				b.putInt("instId", instId);
				requestClasse.putExtras(b);
				startActivityForResult(requestClasse, ADD_CLASSES);
			}
			else {
				msg =getResources().getString(R.string.MsgNoInstSelected);
				this.displayMessage(msg);
			}
			break;
		case R.id.cin:   //ADD COMMENTS
			if ( instId != PSUGO_INST_NULL) {
				Intent intent = new Intent(this, Institution_Comment.class);
				b = null;
				b = new Bundle();
				b.putInt("instId", instId);
				b.putString("commentaires", cin_commentaires);
				intent.putExtras(b);
				startActivityForResult(intent, ADD_COMMENTS);
			}
			else {
				msg =getResources().getString(R.string.MsgNoInstSelected);
				this.displayMessage(msg);
			}
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
		System.out.println("MyInstitution.cin_commentaires = " + myInstitution.cin);
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

		
		myInstitution.infoBancaire = infoBancaire.getText().toString();
		myInstitution.cin = cin_commentaires; //From Institution_Commenta ctivity
		// debug printing the object
		
				
		// Refresh TempData
		int idx = idxEcoleSelected ;
		tempData.instArray[idx].adresse = myInstitution.adresse;
		tempData.instArray[idx].adresseDetail = myInstitution.adresseDetail;
		tempData.instArray[idx].telephone = myInstitution.telephone;
		tempData.instArray[idx].commune = myInstitution.commune;
		tempData.instArray[idx].departement =myInstitution.departement;
		tempData.instArray[idx].infoBancaire =myInstitution.infoBancaire;
		tempData.instArray[idx].cin =myInstitution.cin;
		cin_commentaires = myInstitution.cin;
		tempData.instArray[idx].arrondissement = myInstitution.arrondissement;
		tempData.instArray[idx].instTrouvee = myInstitution.instTrouvee;
	

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
	@Override
	public void onBackPressed() {
		// disable back key
	}






	@Override
	public void onTaskCompleted(String reponse) {
		// TODO Auto-generated method stub
		//	System.out.println("Main after Transfert reponse=" + reponse);
			if ( reponse.equalsIgnoreCase("1")) {
				String	msg = getResources().getString(R.string.MsgXferSuccess);
				this.displayMessage(msg);
			}
			else {
				String	msg = getResources().getString(R.string.MsgXferFail);
				this.displayMessage(msg);
			}
			this.isXferRunning = false;

			
		
	}



	
}
