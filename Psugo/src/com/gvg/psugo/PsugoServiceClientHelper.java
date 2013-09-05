package com.gvg.psugo;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.content.Context;
import android.os.AsyncTask;




public class PsugoServiceClientHelper extends AsyncTask<String, String, TempData> {

	final static String STR_COOKIE= "Set-Cookie";
	String strCookieValue ;
    public Context myCtx;

	
	public PsugoServiceClientHelper(Context baseContext) {
		this.myCtx = baseContext;
	}

	public String LoginRequest(String user, String password) throws Exception {
    	final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	final String SOAP_ACTION_URN = "urn:wally.v3w.net#Login";
        final String METHOD_NAME = "Login";
 //   	final String NAME_SPACE = "http://wally.v3w.net";
    	final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
    	
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
        request.addProperty("user_name", user);
        request.addProperty("password", password);
        
            
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

    public Institution[] ListerInstitutionRequest() throws Exception {

    	//SoapObject login_resp = this.LoginRequest();
    	final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	final String SOAP_ACTION_URN = "urn:wally.v3w.net#ListerInstitution";
        final String METHOD_NAME = "ListerInstitution";
    	final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // It seems that it is a .NET Web service because it doesn't work without next line
        envelope.dotNet = false;

        HttpTransportSE transport = new HttpTransportSE(URL);
        List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
        String aCookiePair = this.strCookieValue + ";";
        //System.out.println("aCookiePair=" + aCookiePair);
        HeaderProperty headerPropertyObj = new HeaderProperty("Cookie", aCookiePair);
        headerList.add(headerPropertyObj);
        transport.call(SOAP_ACTION_URN, envelope, headerList);
        //System.out.println("response = " + envelope.bodyIn.toString());
        //System.out.println("response = " + envelope.getResponse().toString());
       // SoapObject result = (SoapObject) envelope.bodyIn;    
        SoapObject result = (SoapObject) envelope.getResponse();
        
        Institution[] res = new Institution[result.getPropertyCount()];
        for (int i=0;i<res.length;i++){
        	SoapObject pii = (SoapObject)result.getProperty(i);
        	Institution inst = new Institution();
        	inst.id = Integer.parseInt(pii.getPropertyAsString("id"));
        	//System.out.println("id = " + inst.id);
        	inst.nomInstitution = pii.getPropertyAsString("nom_institution");
        	//System.out.println("nomInstitution = " + inst.nomInstitution);
        	inst.departement = pii.getPropertyAsString("departement");
        	//System.out.println("departement = " + inst.departement);
        	inst.sectionRurale = pii.getPropertyAsString("section_rurale");
        	//System.out.println("sectionRurale = " + inst.sectionRurale);
        	inst.arrondissement = pii.getPropertyAsString("arrondissement");
        	//System.out.println("arrondissement = " + inst.arrondissement);
        	inst.commune = pii.getPropertyAsString("commune");
        	//System.out.println("commune = " + inst.commune);
        	inst.adresse = pii.getPropertyAsString("adresse");
        	//System.out.println("adresse = " + inst.adresse);
        	inst.adresseDetail= pii.getPropertyAsString("adresse_detail");
        	//System.out.println("adresseDetail = " + inst.adresseDetail);
        	inst.telephone = ""; // not returned by server
        	inst.instTrouvee = ""; // Not Returned by Server
        	inst.photo = null;
        	res[i] = inst;
        }

        // ici on peut ecrire dans la BD Locale ou changer le type pour retourner un array of string
        return res;
        
    }
    
    public CommuneSectRurale[] ListerSectionRuraleRequest() throws Exception {

    	//SoapObject login_resp = this.LoginRequest();
    	final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	final String SOAP_ACTION_URN = "urn:wally.v3w.net#ListerSectionRurale";
        final String METHOD_NAME = "ListerSectionRurale";
    	final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);
 
            
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // It seems that it is a .NET Web service because it doesn't work without next line
        envelope.dotNet = false;

        HttpTransportSE transport = new HttpTransportSE(URL);
        List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
        String aCookiePair = this.strCookieValue + ";";
        HeaderProperty headerPropertyObj = new HeaderProperty("Cookie", aCookiePair);
        headerList.add(headerPropertyObj);
        transport.call(SOAP_ACTION_URN, envelope, headerList);   
        SoapObject result = (SoapObject) envelope.getResponse();
        
        CommuneSectRurale[] res = new CommuneSectRurale[result.getPropertyCount()];
        for (int i=0;i<res.length;i++){
        	SoapObject pii = (SoapObject)result.getProperty(i);
        	CommuneSectRurale csr = new CommuneSectRurale();
        	csr.commune = pii.getPropertyAsString("commune");
        	//System.out.println("commune = " + csr.commune);
        	csr.sectionRurale = pii.getPropertyAsString("section_rurale");
        	//System.out.println("section Rurale = " + csr.sectionRurale);
        	res[i] = csr;
        }
        
        // ici on peut ecrire dans la BD Locale ou changer le type pour retourner un array of string
        return res;
        
    }

	@Override
	protected TempData doInBackground(String... params) {
		// TODO Auto-generated method stub

		String inParm = params[0];
		System.out.println("InParm=" + inParm);
		
		TempData tData = new TempData();
		tData.userIsValid = false;
		try {
			
			int retCode = Integer.parseInt(this.LoginRequest(params[1], params[2]));
		    System.out.println("after Login return code =" + retCode);
		    if(retCode == 1){
		    	// save the username/pasword
		    	tData.userIsValid = true;
				PsugoDB psudb = new PsugoDB(this.myCtx);
				psudb.open();
				psudb.insertUser(params[1], params[2]);
				psudb.close();
		    }
		    if (retCode == 1 && inParm != "Login") { 	
			// here if no response from server then get data from DB
			//SystemClock.sleep(1000);
		    	tData.userIsValid = true;
		    	tData.instArray = this.ListerInstitutionRequest();
		    	tData.csrArray = this.ListerSectionRuraleRequest();
		    	// save section rurale...
		    }

		    // here we have issue we need to get data from DB
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// here we have issue we need to get data from DB
			
		}
		
		return tData;
	}

	/*
	@Override
	protected void onPostExecute(SoapObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		System.out.println("on Post Execute will create a message");
		try {
		
				PugoSubTaskHelper psch2 = new PugoSubTaskHelper();
				AsyncTask<String, String, SoapObject> servCall = psch2
						.execute(new String[] { "ListerInstitutionRequest" });
				SoapObject resp_servCall = servCall.get();
		}
		catch (Exception e) {
			System.out.println("onPostExecute got an exception");
			e.printStackTrace();
		}
		
		System.out.println("finishing onPostExecute  " );
	}
	
 */
}
