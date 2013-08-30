package com.gvg.psugo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;


public class PugoSubTaskHelper extends AsyncTask<String, String, SoapObject> {
	
	//here we want to call the next service after login
	
	public SoapObject ListerInstitutionRequest() throws Exception {

    	//SoapObject login_resp = this.LoginRequest();
    	final String URL = "http://wally.v3w.net/PsugoSoapServer/server.php";
    	final String SOAP_ACTION_URN = "urn:wally.v3w.net#ListerInstitution";
        //final String SOAP_ACTION = "http://wally.v3w.net/PsugoSoapServer/server.wsdl";
        final String METHOD_NAME = "ListerInstitution";
    	//final String NAME_SPACE = "http://wally.v3w.net";
    	final String NAME_SPACE_URN = "urn:wally.v3w.net:PsugoSoapServer:server.wsdl";
    	
        SoapObject request = new SoapObject(NAME_SPACE_URN, METHOD_NAME);

        
            
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // It seems that it is a .NET Web service because it doesn't work without next line
        envelope.dotNet = false;

        HttpTransportSE transport = new HttpTransportSE(URL);
        transport.call(SOAP_ACTION_URN, envelope);
        System.out.println("response = " + envelope.bodyIn.toString());
        System.out.println("response = " + envelope.getResponse().toString());
        //envelope.getResponse();
        return (SoapObject) envelope.bodyIn;
        
    }
	@Override
	protected SoapObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		System.out.println("Executing subtask");
		String requestLogin = "Login";
		String requestListerInstitutionRequest = "ListerInstitutionRequest";
		String inParm = params[0];
		System.out.println("InParm=" + inParm);
		SoapObject response = null;
		try {
			
				response = this.ListerInstitutionRequest();
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		return response;
	}

}
