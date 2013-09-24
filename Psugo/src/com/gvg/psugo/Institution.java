package com.gvg.psugo;

//Web Service tool
import java.util.Hashtable;
import java.util.Vector;


import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class Institution implements KvmSerializable {
	public int id;
	public String nomInstitution;
	public String departement;
	public String arrondissement;
	public String commune;
	public String sectionRurale;
	public String adresse;
	public String adresseDetail;
	public String telephone;
	public String cin; // use as comment field
	public String instTrouvee;
	public PhotoCollection photo;
	public String infoBancaire;

	public Institution() {
		super();
	}
	public Institution(int id, String nomInstitution, String departement,
			String arrondissement, String commune, String sectionRurale,
			String adresse, String adresseDetail, String telephone, String cin,
			Photo[] photo, String infoBancaire) {
		super();
		this.id = id;
		this.nomInstitution = nomInstitution;
		this.departement = departement;
		this.arrondissement = arrondissement;
		this.commune = commune;
		this.sectionRurale = sectionRurale;
		this.adresse = adresse;
		this.adresseDetail = adresseDetail;
		this.telephone = telephone;
		this.cin = "";
		this.infoBancaire = infoBancaire;
		PhotoCollection v = new PhotoCollection();
		for (int i = 0; i< photo.length;i++)
			v.add(photo[i]);
		this.photo = v;
		
	}

	public Institution(int id, String nomInstitution, String departement, String arrondissement, String commune,
						String sectionRurale, String adresse, String adresseDetail, String telephone, String trouvee, 
						String infoBancaire,String commentaires) {
		this.id = id;
		this.nomInstitution = nomInstitution;
		this.departement = departement;
		this.arrondissement = arrondissement;
		this.commune = commune;
		this.sectionRurale = sectionRurale;
		this.adresse = adresse;
		this.adresseDetail = adresseDetail;
		this.telephone = telephone;
		this.instTrouvee = trouvee;
		this.infoBancaire = infoBancaire;
		this.cin = commentaires;
	}
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
        case 0:
            return this.id;
        case 1:
            return this.nomInstitution;
        case 2:
            return this.departement;
        case 3:
            return this.arrondissement;
        case 4:
            return this.commune;
        case 5:
            return this.sectionRurale;
        case 6:
            return this.adresse;
        case 7:
            return this.adresseDetail;
        case 8:
            return this.telephone;
        case 9:
            return this.cin;
        case 10:
            return this.instTrouvee;
        case 11:
            return this.photo;
        case 12:
            return this.infoBancaire;
        }
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 13;
	}
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		 switch(index)
	        {
	        case 0:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "id";
	            break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "nomInstitution";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "departement";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "arrondissement";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "commune";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "sectionRurale";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "adresse";
	            break;
	        case 7:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "adresseDetail";
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "telephone";
	            break;
	        case 9:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cin";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "instTrouvee";
	            break;
	        case 11:
	            info.type = PropertyInfo.OBJECT_CLASS; 
	            info.name = "photo";
	            break;
	        case 12:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "infoBancaire";
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
	            this.id = Integer.parseInt(value.toString());
	            break;
	        case 1:
	            this.nomInstitution =  value.toString();	           
	            break;
	        case 2:
	        	this.departement =  value.toString();
	            break;
	        case 3:
	        	this.arrondissement =  value.toString();
	            break;
	        case 4:
	        	this.commune =  value.toString();
	            break;
	        case 5:
	        	this.sectionRurale =  value.toString();
	            break;
	        case 6:
	        	this.adresse =  value.toString();
	            break;
	        case 7:
	        	this.adresseDetail =  value.toString();
	            break;
	        case 8:
	        	this.telephone =  value.toString();
	            break;
	        case 9:
	        	this.cin =  value.toString();
	            break;
	        case 10:
	        	this.instTrouvee =  value.toString();
	            break;
	        case 11:
	        	this.photo = (PhotoCollection) value;
	            break;
	        case 12:
	        	this.infoBancaire = value.toString();
	            break;
	        default:break;
	        }
	}

}
