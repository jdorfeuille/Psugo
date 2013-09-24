package com.gvg.psugo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.transport.Transport;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kobjects.base64.Base64;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class PsugoSendClientDataHelper extends AsyncTask<PsugoSendDataParm, String, String> 
										implements PsugoOnTaskCompleted {

	final static String STR_COOKIE= "Set-Cookie";
	final static String TYPE_DIR_ADMIN = "Administratif";
	final static String TYPE_DIR_PEDAG = "Pedagogique";
	private static String PSUGO_SERVEUR = "psugo.primature.ht"; 
	///private static String PSUGO_SERVEUR = "wally.v3w.net";  
	private static final String DEBUG_TAG= "PsugoSendClientDataHelper";
//	private final Logger log = Logger.getLogger(PsugoSendClientDataHelper.class);
	private PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
	

	
	String strCookieValue ;  
	String logMessage;
	String theUserId;
	String theUserPwd;
	Context theBaseContext;
	//List<Integer> instSentList ;
	String finalResponse = "";
	private PsugoOnTaskCompleted listener;
	
	static {

	}
    public PsugoSendClientDataHelper(PsugoOnTaskCompleted listener){
        this.listener=listener;
//        File path ;
//        boolean res;
//        String fnPathAndName = "";
//        String tname = "Psugo.log";
//        try {
//	        path = new File( Environment.getExternalStorageDirectory(), "PsugoTmp" );
//	    	if(!path.exists()){
//	    	    path.mkdir();
//	    	 }
//	    	File fn = new File(path, "Psudo.log");
//	    	fnPathAndName = path + File.separator + tname;
//	    	if (fn.exists()) {
//	    		log.addAppender(new RollingFileAppender(layout, "Psugo.log"));
//	    	}
//	    	else {
//	    			res= fn.createNewFile();
//	    			log.addAppender(new RollingFileAppender(layout, "Psugo.log"));
//	    	}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
	
    public String LoginRequest() throws Exception {
    	//final String USER_NAME = "agent01";
    	//final String USER_PWD = "5304240";
    	
    	final String URL = "http://" + PSUGO_SERVEUR + "/PsugoSoapServer/server.php";
		final String SOAP_ACTION_URN = "urn:" + PSUGO_SERVEUR + "#Login";
		final String NAME_SPACE_URN ="urn:" + PSUGO_SERVEUR+":PsugoSoapServer:server.wsdl";
		
		//--
    	//final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	//final String SOAP_ACTION_URN = "urn:wally.v3w.net#Login";
        final String METHOD_NAME = "Login";
    	//final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
    	
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
        request.addProperty("user_name", theUserId);
        request.addProperty("password", theUserPwd);
        
            
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // It seems that it is a .NET Web service because it doesn't work without next line
        envelope.dotNet = false;

        HttpTransportSE transport = new HttpTransportSE(URL);
        List<?> headerList = transport.call(SOAP_ACTION_URN, envelope, null );
        strCookieValue="";
		for (Object el : headerList) {
			HeaderProperty headerProperty = (HeaderProperty) el;
			String headerKey = headerProperty.getKey();
			String headerValue = headerProperty.getValue();
			//System.out.println(headerKey + " : " + headerValue);
			if (headerKey == null) {
			} else if (headerKey.equalsIgnoreCase(STR_COOKIE)) {
				
				strCookieValue = headerValue.substring(0,headerValue.indexOf(";"));
			}

		}
        //return (SoapObject) envelope.bodyIn;
        return  envelope.getResponse().toString();
        
    }

	private void debugPhoto( Photo myPhoto) {
		
		System.out.println("myPhoto.photo length= " + myPhoto.photo.length());
		myPhoto.photo = "TEST PHOTO";
		System.out.println("myPhoto.latitude = " + myPhoto.latitude);
		System.out.println("myPhoto.longitude = " + myPhoto.longitude);
		System.out.println("myPhoto.typePhoto = " + myPhoto.typePhoto);
		System.out.println("myPhoto.datePhoto = " + myPhoto.datePhoto);
		System.out.println("Done Debug Photo");
	}
	
	private void debugInstitution(String fromMethod, Institution myInstitution) {
		System.out.println("Printing from method: " + fromMethod);
		System.out.println("MyInstitution.id = " + myInstitution.id);
		System.out.println("MyInstitution.nomInstitution = "
				+ myInstitution.nomInstitution);
		System.out.println("MyInstitution.adresse = " + myInstitution.adresse);
		System.out.println("MyInstitution.adresseDetail = "
				+ myInstitution.adresseDetail);
		System.out.println("MyInstitution.arrondissement = "
				+ myInstitution.arrondissement);
		System.out.println("MyInstitution.departement = "
				+ myInstitution.departement);
		System.out.println("MyInstitution.sectionRurale = "
				+ myInstitution.sectionRurale);
		System.out.println("MyInstitution.commune = " + myInstitution.commune);
		System.out.println("MyInstitution.telephone = "
				+ myInstitution.telephone);
		System.out.println("MyInstitution.instTrouvee = "
				+ myInstitution.instTrouvee);
		int nPhotos = myInstitution.photo.size();
		System.out.println("MyInstitution nombre de photos " + nPhotos);
		Enumeration<Photo> el = myInstitution.photo.elements();
		while (el.hasMoreElements()) {
			Photo thePhoto = (Photo) el.nextElement();
			debugPhoto(thePhoto);
		}
		//System.out.println("Done Debug Institution");
	}
    
    private void outLogMessage(String msg) {
    	//log.info(msg);
    	
    }
    
    
    
	public int envoyerInstitution() throws Exception {

		// SoapObject login_resp = this.LoginRequest();
    	final String URL = "http://" + PSUGO_SERVEUR + "/PsugoSoapServer/server.php";
		final String SOAP_ACTION_URN = "urn:" + PSUGO_SERVEUR + "#EnvoyerInstitution";
		final String NAME_SPACE_URN ="urn:" + PSUGO_SERVEUR+":PsugoSoapServer:server.wsdl";
		//
		//final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
		//final String SOAP_ACTION_URN = "urn:wally.v3w.net#EnvoyerInstitution";
		final String METHOD_NAME = "EnvoyerInstitution";
		//final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
		int ret = 0;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		PsugoDB psudb = new PsugoDB(theBaseContext);
		psudb.open();
		Institution[] myDbInst = psudb.selectInstitution();
		psudb.close();
		int instSize = myDbInst.length;
		//instSentList = new ArrayList<Integer>();
		logMessage = "envoyerInstitution Nombre Institutions au depart:" + instSize;
		this.outLogMessage(logMessage);
		for (int i = 0; i < instSize; i++) {
			//if ( myDbInst[i].photo != null  && myDbInst[i].photo.size() > 0) {
				//System.out.println("Debug EnvoyerInstitution" + myDbInst[i].photo.size());
			//}
			// ne pas transmettre si pas de photos... ou bien si l'utilisateur n'a pas mis a jour
			logMessage = "Verification de Photos pour Institution :" +myDbInst[i].id;
			this.outLogMessage(logMessage);
			if ( myDbInst[i].photo != null  && myDbInst[i].photo.size() > 0 && (!myDbInst[i].photo.isEmpty() )){
				//debugInstitution("envoyerInstitution", myDbInst[i]);
				//instSentList.add(myDbInst[i].id);
				logMessage = "Photo Existantes pour institution :" +myDbInst[i].id;
				this.outLogMessage(logMessage);

				SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
				envelope.setOutputSoapObject(request);
				envelope.encodingStyle = SoapEnvelope.ENC;
				new MarshalBase64().register(envelope); // serialization
				HttpTransportSE transport = new HttpTransportSE(URL);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				String aCookiePair = this.strCookieValue + ";";
				HeaderProperty headerPropertyObj = new HeaderProperty("Cookie",
						aCookiePair);
				headerList.add(headerPropertyObj);
				PropertyInfo pi = new PropertyInfo();
				pi.setName("myInst");
				pi.setValue(myDbInst[i]);
				pi.setType(myDbInst[i].getClass());
				request.addProperty(pi);
				envelope.addMapping(NAME_SPACE_URN, "Institution",
						myDbInst[i].getClass());
				//envelope.addMapping(NAME_SPACE_URN, "Photo",
				//		myDbInst[i].photo.getClass());
				transport.call(SOAP_ACTION_URN, envelope, headerList);
				//SoapObject result = (SoapObject) envelope.getResponse();
				ret = Integer.parseInt( envelope.getResponse().toString());
				if (ret == 1 ) {
					logMessage = "Procahaine Etape Envoie de Directeurs :" +myDbInst[i].id;
					this.outLogMessage(logMessage);
						int retCodeDir = this.envoyerDirecteurs(myDbInst[i].id);
						if (retCodeDir == 1 ) {
							logMessage = "Procahaine Etape Envoie de Classes :" +myDbInst[i].id;
							this.outLogMessage(logMessage);
							int retCodeClasse = this.envoyerClasses(myDbInst[i].id);
							if (retCodeClasse != 1 ) break;
						}
						else break;
				}
				else break; // if Institution is in error
				
			}
		}

		return ret;

	}
	
	public int sendDirecteur(Directeur theDir) throws Exception {
		// Probablement un try/catch afin de mieux retourner un message significatif
    	final String URL = "http://" + PSUGO_SERVEUR + "/PsugoSoapServer/server.php";
		final String SOAP_ACTION_URN = "urn:" + PSUGO_SERVEUR + "#EnvoyerDirecteur";
		final String NAME_SPACE_URN ="urn:" + PSUGO_SERVEUR+":PsugoSoapServer:server.wsdl";
		
		//
	
		//final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
		//final String SOAP_ACTION_URN = "urn:wally.v3w.net#EnvoyerDirecteur";
		final String METHOD_NAME = "EnvoyerDirecteur";
		//final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		int ret = 0;
	    String typeDirTemp = theDir.typeDirection.substring(0, 1);
	    theDir.typeDirection = typeDirTemp;
		SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapEnvelope.ENC;
		new MarshalBase64().register(envelope); // serialization
		HttpTransportSE transport = new HttpTransportSE(URL);
		List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
		String aCookiePair = this.strCookieValue + ";";
		HeaderProperty headerPropertyObj = new HeaderProperty("Cookie",
				aCookiePair);
		headerList.add(headerPropertyObj);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("theDir");
		pi.setValue(theDir);
		pi.setType(theDir.getClass());
		request.addProperty(pi);
		envelope.addMapping(NAME_SPACE_URN, "Directeur",
				theDir.getClass());

		transport.call(SOAP_ACTION_URN, envelope, headerList);
		ret = Integer.parseInt( envelope.getResponse().toString());
		return ret;
		
	}

	public int envoyerDirecteurs(int theInstId) throws Exception {

		// SoapObject login_resp = this.LoginRequest();
		
	
		int retCode=1;
		//Photo dumPhoto = new Photo();
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		PsugoDB psudb = new PsugoDB(theBaseContext);
		psudb.open();
		Directeur dirAdmin = psudb.selectDirecteur(theInstId, TYPE_DIR_ADMIN);
		if (dirAdmin != null ) {
				logMessage = "Envoie Directeur Admin :" ;
				this.outLogMessage(logMessage);
				retCode = sendDirecteur(dirAdmin);
				if (retCode != 1 ){
					logMessage = "Envoie Directeur Admin en Echec !!!";
					this.outLogMessage(logMessage);
				}
		}
		Directeur dirPedag = psudb.selectDirecteur(theInstId, TYPE_DIR_PEDAG);
		if (dirPedag != null ) {
			logMessage = "Envoie Directeur Pedagogique :" ;
			this.outLogMessage(logMessage);
			retCode = sendDirecteur(dirPedag);
			if (retCode != 1) {
				logMessage = "Envoie Directeur Pedagogique en Echec !!!";
				this.outLogMessage(logMessage);
			}
		}

		psudb.close();
		return retCode;

	}
	
	public int envoyerClasses(int theInstId) throws Exception {

		//System.out.println("---envoyerClasses ---envoyerClasses");
		// SoapObject login_resp = this.LoginRequest();
    	final String URL = "http://" + PSUGO_SERVEUR + "/PsugoSoapServer/server.php";
		final String SOAP_ACTION_URN = "urn:" + PSUGO_SERVEUR + "#EnvoyerClasse";
		final String NAME_SPACE_URN ="urn:" + PSUGO_SERVEUR+":PsugoSoapServer:server.wsdl";		
		
		//
		//final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
		//final String SOAP_ACTION_URN = "urn:wally.v3w.net#EnvoyerClasse";
		final String METHOD_NAME = "EnvoyerClasse";
		//final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
		int ret = 1;
		// Photo dumPhoto = new Photo();
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		PsugoDB psudb = new PsugoDB(theBaseContext);
		psudb.open();
		Classe[] myClasses = psudb.selectClasse(theInstId);
		psudb.close();
		int classeSize = myClasses.length;
		logMessage = "Prochaine Etapes tentative denvoyer Nbre de Classes :"+ classeSize ;
		this.outLogMessage(logMessage);
		for (int i = 0; i < classeSize; i++) {
			logMessage = "Verification pour Envoie :"+ myClasses[i].nomClasse ;
			this.outLogMessage(logMessage); 
			if (!myClasses[i].nomClasse.isEmpty()
					&& myClasses[i].photoClasse != null) {
				logMessage = "Cette Classe va etre envoyer :"+ myClasses[i].nomClasse ;
				this.outLogMessage(logMessage); 
				SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
				envelope.setOutputSoapObject(request);
				envelope.encodingStyle = SoapEnvelope.ENC;
				new MarshalBase64().register(envelope); // serialization
				HttpTransportSE transport = new HttpTransportSE(URL);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				String aCookiePair = this.strCookieValue + ";";
				HeaderProperty headerPropertyObj = new HeaderProperty("Cookie",
						aCookiePair);
				headerList.add(headerPropertyObj);
				PropertyInfo pi = new PropertyInfo();
				pi.setName("myClasse");
				pi.setValue(myClasses[i]);
				pi.setType(myClasses[i].getClass());
				request.addProperty(pi);
				envelope.addMapping(NAME_SPACE_URN, "Classe",
						myClasses[i].getClass());

				transport.call(SOAP_ACTION_URN, envelope, headerList);

				ret = Integer.parseInt(envelope.getResponse().toString());
				if (ret != 1) {
					logMessage = "Envoie de Classe en Echec :"+ myClasses[i].nomClasse ;
					this.outLogMessage(logMessage);
					break;
				}
			}
		}

		return ret;

	}
	@Override
	protected String doInBackground(PsugoSendDataParm... params)  {
		// TODO Auto-generated method stub

		theBaseContext = params[0].theContext;
		theUserId = params[0].uName;
		theUserPwd = params[0].uPwd;
		String msg = "";
		PsugoUtils pscn = new PsugoUtils(theBaseContext);
		int retCode = -1;
		try {
			retCode = Integer.parseInt(this.LoginRequest());
			if (retCode == 1) {
				retCode = this.envoyerInstitution();
				//System.out.println("Just sent institution with Return Code:" + retCode);
				if (retCode == 0 ) {
					//msg = 
					//pscn.displayMessage("Transfert Complete avec succes !!!");
				}
				//else pscn.displayMessage("le Transfert n'a pu etre Completer veuillez re-essayer plus tard!!!");
				
			}
			finalResponse = "" + retCode;

		} catch (Exception e) {
			finalResponse = "0";
			retCode =1; //Herve's ... 
			if (e != null ) 
				logMessage = "Exception PsugoSendClientDataHelper- doInBackground !!!!!" + e.getMessage();
			else 
				logMessage = "Exception PsugoSendClientDataHelper- doInBackground !!!!! (e) is Null";
			this.outLogMessage(logMessage);
			//PsugoException pse = new PsugoException(logMessage);

			//	throw pse;

			//Log.i(DEBUG_TAG, logMessage); 
			//e.printStackTrace();
			

		}
		String resp = "" + retCode;
	
		//System.out.println("finalReponse = " + finalResponse);
		return resp;
	}
	


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//System.out.println("result=" + result);
		//System.out.println("finalResponse from PostExecute" + finalResponse);
		listener.onTaskCompleted(finalResponse);
	}

	@Override
	public void onTaskCompleted(String reponse) {
		// TODO Auto-generated method stub
		
	}



}
