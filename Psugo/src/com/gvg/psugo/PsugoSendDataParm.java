package com.gvg.psugo;

import android.content.Context;

public class PsugoSendDataParm {

	public Context theContext;
	String uName;
	String uPwd;
	
	public PsugoSendDataParm() {
		// TODO Auto-generated constructor stub
		super();
	}

	public PsugoSendDataParm(Context theContext, String uName, String uPwd) {
		super();
		this.theContext = theContext;
		this.uName = uName;
		this.uPwd = uPwd;
	}


	
	

}
