package com.gvg.psugo;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Classe  implements KvmSerializable {
	public int instituionId;
	public String nomClasse;
	public int nombreEleve;
	public Photo photoClasse;
	public String nomProfesseur;
	public String emailProf;
	public String phoneProf;
	public String cinProf;
	public String genreProf ; // Masculin ou Feminin
	public Photo photoProfesseur;
	
	
	public Classe() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Classe(int instituionId, String nomClasse, int nombreEleve, Photo photoClasse, String professeur, Photo photoProfesseur)
	{
		this.instituionId = instituionId;
		this.nomClasse = nomClasse;
		this.nombreEleve = nombreEleve;
		this.photoClasse = photoClasse;
		this.nomProfesseur = professeur;
		this.photoProfesseur = photoProfesseur;
	}
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
        case 0:
            return this.instituionId;
        case 1:
            return this.nomClasse;
        case 2:
            return this.nombreEleve;
        case 3:
            return this.photoClasse;
        case 4:
            return this.nomProfesseur;
        case 5:
            return this.emailProf;
        case 6:
            return this.phoneProf;
        case 7:
            return this.cinProf;
        case 8:
            return this.genreProf;
        case 9:
            return this.photoProfesseur;
        }
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 10;
	}
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		 switch(index)
	        {
	        case 0:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "instituionId";
	            break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "nomClasse";
	            break;
	        case 2:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "nombreEleve";
	            break;
	        case 3:
	            info.type = PropertyInfo.OBJECT_CLASS;
	            info.name = "photoClasse";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "nomProfesseur";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "emailProf";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "phoneProf";
	            break;
	        case 7:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cinProf";   
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "genreProf";
	        case 9:
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
	            this.instituionId = Integer.parseInt(value.toString());
	            break;
	        case 1:
	            this.nomClasse =  value.toString();	           
	            break;
	        case 2:
	        	this.nombreEleve =  Integer.parseInt(value.toString());
	            break;
	        case 3:
	        	this.photoClasse =  (Photo) value;
	            break;
	        case 4:
	        	this.nomProfesseur =  value.toString();
	            break;
	        case 5:
	        	this.emailProf =  value.toString();
	            break;
	        case 6:
	        	this.phoneProf =  value.toString();
	            break;
	        case 7:
	        	this.cinProf =  value.toString();
	            break;
	        case 8:
	        	this.genreProf =  value.toString();
	            break;
	        case 9:
	        	this.photoProfesseur = (Photo) value;
	            break;
	        default:break;
	        }
	}

	
}
