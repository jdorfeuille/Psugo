package com.gvg.psugo;

public class Classe {
	public int instituionId;
	public String nomClasse;
	public int nombreEleve;
	public Photo photoClasse;
	public String nomProfesseur;
	public String emailProf;
	public String phoneProf;
	public int cinProf;
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
	
	
}
