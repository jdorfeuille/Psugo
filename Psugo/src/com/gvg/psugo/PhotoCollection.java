package com.gvg.psugo;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class PhotoCollection extends Vector<Photo> implements KvmSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhotoCollection() {
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		 return this.get(arg0);
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		 return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
        info.type = PropertyInfo.OBJECT_CLASS; 
        info.name = "photo";
		
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		Photo thePhoto = (Photo)arg1;
		this.add(thePhoto);
		
	}

}
