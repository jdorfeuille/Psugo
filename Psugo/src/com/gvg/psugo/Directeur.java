package com.gvg.psugo;

public class Directeur{
	
	public int instituionId;
	public String nom;
	public String genre;
	public String typeDirection;
	public String email;
	public String telephone;
	public String cin;
	public Photo photo;
	
	public Directeur(int instituionId, String typeDirection, String nom, String genre, String email, String telephone, String cin, Photo photo){
		this.instituionId = instituionId;
		this.nom = nom;
		this.genre = genre;
		this.typeDirection = typeDirection;
		this.email = email;
		this.telephone = telephone;
		this.cin = cin;
		this.photo = photo;
	}
	public Directeur(int instituionId, String typeDirection, String nom, String genre, String email, String telephone, String cin){
		this.instituionId = instituionId;
		this.nom = nom;
		this.genre = genre;
		this.typeDirection = typeDirection;
		this.email = email;
		this.telephone = telephone;
		this.cin = cin;
	}
}
