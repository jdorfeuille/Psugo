package com.gvg.psugo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PsugoUtils {

	
	Context theContext;
	
	public boolean isNetworkAvailable() {
		
		
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public PsugoUtils(Context c) {
		// TODO Auto-generated constructor stub
		theContext = c;
	}

}
