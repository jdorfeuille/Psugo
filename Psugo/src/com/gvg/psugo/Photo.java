package com.gvg.psugo;


import java.util.Hashtable;
//import java.util.Vector;
//import org.kobjects.base64.Base64;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Photo implements KvmSerializable {
	//public byte[] photo;
	public String photo;
	public String longitude;
	public String latitude;
	public String datePhoto;
	public String typePhoto;
	
	
	public Photo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Photo( String bs, String longitude, String latitude, String date, String type){
		this.photo = bs;
		this.longitude = longitude;
		this.latitude = latitude;
		this.datePhoto = date;
		this.typePhoto = type;
	}


	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
        case 0:
            return this.photo;
        case 1:
            return this.longitude;
        case 2:
            return this.latitude;
        case 3:
            return this.datePhoto;
        case 4:
            return this.typePhoto;
        }
		return null;
		
	}


	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 5;
	}


	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch(index)
        {

        case 0:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "photo";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "longitude";
            break;
        case 2:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "latitude";
            break;
        case 3:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "datePhoto";
            break;
        case 4:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "typePhoto";
            break;
        default:break;
        }
	}


	@Override
	public void setProperty(int index, Object value) {
		// TODO Auto-generated method stub
		switch(index)
        {
        case 0:
            this.photo =  String.valueOf(value);	           
            break;
        case 1:
        	this.longitude =  String.valueOf(value);
            break;
        case 2:
        	this.latitude =  String.valueOf(value);
            break;
        case 3:
        	this.datePhoto =  String.valueOf(value);
            break;
        case 4:
        	this.typePhoto =  String.valueOf(value);
            break;
    
        default:break;
        }
		
	}
}
