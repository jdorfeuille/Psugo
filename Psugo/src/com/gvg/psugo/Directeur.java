package com.gvg.psugo;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Directeur implements KvmSerializable{
	
	public int institutionId;
	public String nom;
	public String genre;
	public String typeDirection;
	public String email;
	public String telephone;
	public String cin;
	public Photo photo;
	
	public Directeur(int institutionId, String typeDirection, String nom, String genre, String email, String telephone, String cin, Photo photo){
		this.institutionId = institutionId;
		this.nom = nom;
		this.genre = genre;
		this.typeDirection = typeDirection;
		this.email = email;
		this.telephone = telephone;
		this.cin = cin;
		this.photo = photo;
	}
	public Directeur(int institutionId, String typeDirection, String nom, String genre, String email, String telephone, String cin){
		this.institutionId = institutionId;
		this.nom = nom;
		this.genre = genre;
		this.typeDirection = typeDirection;
		this.email = email;
		this.telephone = telephone;
		this.cin = cin;
	}
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
        case 0:
            return this.institutionId;
        case 1:
            return this.nom;
        case 2:
            return this.genre;
        case 3:
            return this.typeDirection;
        case 4:
            return this.email;
        case 5:
            return this.telephone;
        case 6:
            return this.cin;
        case 7:
            return this.photo;
        }
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		 switch(index)
	        {
	        case 0:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "institutionId";
	            break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "nom";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "genre";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "typeDirection";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "email";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "telephone";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cin";
	            break;
	        case 7:
	            info.type = PropertyInfo.OBJECT_CLASS; 
	            info.name = "photo";
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
	            this.institutionId = Integer.parseInt(value.toString());
	            break;
	        case 1:
	            this.nom =  value.toString();	           
	            break;
	        case 2:
	        	this.genre =  value.toString();
	            break;
	        case 3:
	        	this.typeDirection =  value.toString();
	            break;
	        case 4:
	        	this.email =  value.toString();
	            break;
	        case 5:
	        	this.telephone =  value.toString();
	            break;
	        case 6:
	        	this.cin =  value.toString();
	            break;
	        case 7:
	        	this.photo = (Photo) value;
	            break;
	        default:break;
	        }
	}

}
