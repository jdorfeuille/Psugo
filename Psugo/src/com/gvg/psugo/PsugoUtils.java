package com.gvg.psugo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.kobjects.base64.Base64;

public class PsugoUtils {

	
	Context theContext;
	
	public boolean isNetworkAvailable() {
		
		
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		 return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * @param encodedString
	 * @return bitmap (from given string)
	 */
	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	


	public PsugoUtils(Context c) {
		// TODO Auto-generated constructor stub
		theContext = c;
	}

}
