package com.gvg.psugo;

import java.util.ArrayList;
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
import android.os.Handler;
import android.os.SystemClock;

public class PsugoSendClientDataHelper extends AsyncTask<Context, String, String> {

	final static String STR_COOKIE= "Set-Cookie";
	String strCookieValue ;  
	Context theBaseContext;
	
	
    public String LoginRequest() throws Exception {
    	final String USER_NAME = "agent01";
    	final String USER_PWD = "5304240";
    	final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	final String SOAP_ACTION_URN = "urn:wally.v3w.net#Login";
        final String METHOD_NAME = "Login";
 //   	final String NAME_SPACE = "http://wally.v3w.net";
    	final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
    	
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
        request.addProperty("user_name", USER_NAME);
        request.addProperty("password", USER_PWD);
        
            
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
		//for (int i = 0; i < nPhotos; i++) {
		//	if (myInstitution.photo[i] != null) {
		//		debugPhoto(myInstitution.photo[i]);
		//	}
		//	else System.out.println("myInstitution.photo[i]:"+i+" is null");
	//	}
		System.out.println("Done Debug Institution");
	}
    
    
    
    
    
	public int envoyerInstitution() throws Exception {

		// SoapObject login_resp = this.LoginRequest();
		final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
		final String SOAP_ACTION_URN = "urn:wally.v3w.net#EnvoyerInstitution";
		final String METHOD_NAME = "EnvoyerInstitution";
		final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
		int ret = 0;
		Photo dumPhoto = new Photo();
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		PsugoDB psudb = new PsugoDB(theBaseContext);
		psudb.open();
		Institution[] myDbInst = psudb.selectInstitution();
		int instSize = myDbInst.length;
		for (int i = 0; i < instSize; i++) {
			if ( myDbInst[i].photo != null  && myDbInst[i].photo.size() > 0) {
				System.out.println("Debug EnvoyerInstitution" + myDbInst[i].photo.size());
			}
			// ne pas transmettre si pas de photos... ou bien si l'utilisateur n'a pas mis a jour
			if ( myDbInst[i].photo != null  && myDbInst[i].photo.size() > 0 && (!myDbInst[i].photo.isEmpty() )){
				//*********PATCH********* TO FIX
				myDbInst[i].instTrouvee = "";
				//**********END PATCH
				debugInstitution("envoyerInstitution", myDbInst[i]);
				// trying to fix the Serialization issue
				//SoapObject photos = new SoapObject(NAME_SPACE_URN, "Photo");
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
				SoapObject result = (SoapObject) envelope.getResponse();
				ret = Integer.parseInt( envelope.getResponse().toString());
				if (ret != 1 )
					break;
				
			}
		}
		psudb.close();
		return ret;

	}

	@Override
	protected String doInBackground(Context... params) {
		// TODO Auto-generated method stub

		theBaseContext = params[0];
		int retCode = -1;
		try {
			retCode = Integer.parseInt(this.LoginRequest());
			if (retCode == 1) {
				retCode = this.envoyerInstitution();
				System.out.println("Just sent institution with Return Code:" + retCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// here we have issue we need to get data from DB

		}
		String resp = "" + retCode;
		return resp;
	}


}
