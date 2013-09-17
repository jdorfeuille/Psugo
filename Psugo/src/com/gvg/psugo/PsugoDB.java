package com.gvg.psugo;

import java.util.Vector;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.content.Context;

public class PsugoDB {
	private SQLiteDatabase db;
	private AndroidOpenDbHelper dbHelper;

	public PsugoDB(Context context) {
		dbHelper = new AndroidOpenDbHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	// Insert a record on table Institution with ID, NAME, DEPT, ARROND, COMMUNE
	// and SECTION_RURALE set.
	public void insertInstitution(int id, String name, String dept,
			String arrond, String commune, String sectionr, String infoBancaire) {

		String query = "INSERT INTO "
				+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION + " VALUES ("
				+ Integer.toString(id) + ", "
				+ DatabaseUtils.sqlEscapeString(name) + ", "
				+ DatabaseUtils.sqlEscapeString(dept) + ", "
				+ DatabaseUtils.sqlEscapeString(arrond) + ", "
				+ DatabaseUtils.sqlEscapeString(commune) + ", "
				+ DatabaseUtils.sqlEscapeString(sectionr) + ", '', '', '', '', " 
				+ DatabaseUtils.sqlEscapeString(infoBancaire) + ")";
		try {
			db.execSQL(query);
		} catch (Exception e) {
			System.out.println("insertIntitution failed ...");
			e.printStackTrace();
		}
	}

	// update institution NAME, COMMUNE, SECTION_RURALE, ADRESSE,
	// ADRESSE_DETAIL, PHONE, TROUVEE, INFOBANCAIRE
	public void updateInstitution(int id, String name, String commune,
			String sectionr, String adresse, String adresseDetail,
			String phone, String trouvee, String infoBancaire) {

		String query = "UPDATE " + AndroidOpenDbHelper.TABLE_NAME_INSTITUTION
				+ " SET " + AndroidOpenDbHelper.COLUMN_NAME_INST_NAME + " = "
				+ DatabaseUtils.sqlEscapeString(name) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_COMMUNE + " = "
				+ DatabaseUtils.sqlEscapeString(commune) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_SECTION_RURALE + " = "
				+ DatabaseUtils.sqlEscapeString(sectionr) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_ADRESSE + " = "
				+ DatabaseUtils.sqlEscapeString(adresse) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_ADRESSE_DETAIL + " = "
				+ DatabaseUtils.sqlEscapeString(adresseDetail) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_PHONE + " = "
				+ DatabaseUtils.sqlEscapeString(phone) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INFO_BANCAIRE + " = "
				+ DatabaseUtils.sqlEscapeString(infoBancaire) + ", "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_TROUVEE + " = "
				+ DatabaseUtils.sqlEscapeString(trouvee) + " " + "WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_ID + " = "
				+ Integer.toString(id);
		query = query + ";"; // juste pour test...
		db.execSQL(query);
	}

	public void deleteInstitution(int id) {

		deleteInstitutionPhoto(id);
		deleteDirecteur(id);

		db.execSQL("DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_INSTITUTION
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_INST_ID + " = "
				+ id);
		
		
	}

	// delete (if exist) and insert institution_photo
	public void insertInstitutionPhoto(int instId, String photo,
			String longitude, String latitude, String date, String type) {

		deleteInstitutionPhoto(instId, type);

		String query = "INSERT INTO "
				+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION_PH
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		SQLiteStatement p = db.compileStatement(query);

		p.bindNull(1);
		p.bindLong(2, instId);
		p.bindString(3, photo);
		p.bindString(4, longitude);
		p.bindString(5, latitude);
		p.bindString(6, date);
		p.bindString(7, type);
		p.executeInsert();
	}

	public void deleteInstitutionPhoto(int instId, String type) {
		db.execSQL("DELETE FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION_PH + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_PHOTO_TYPE + " = '"
				+ type + "' and "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_ID_INST + " = " + instId);
	}

	public void deleteInstitutionPhoto(int id) {
		db.execSQL("DELETE FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION_PH + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_INST_ID_PH + " = " + id);
	}

	public Institution[] selectInstitution() {
		Cursor c = db.rawQuery("select * from "
				+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION, null);
		int i = 0;
		int i2 = 0;
		int count1 = 0;
		int count2 = 0;
		if (c.getCount() > 0)
			count1 = c.getCount();

		Institution[] inst = new Institution[count1];

		if (count1 > 0) {
			c.moveToFirst();
			do {
				int id = c
						.getInt(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_ID));
				inst[i] = new Institution(
						id,
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_NAME)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_DEPT)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_ARROND)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_COMMUNE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_SECTION_RURALE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_ADRESSE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_ADRESSE_DETAIL)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_PHONE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_TROUVEE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INFO_BANCAIRE)));
				count2 = 0;
				Cursor c2 = db.rawQuery("select * from "
						+ AndroidOpenDbHelper.TABLE_NAME_INSTITUTION_PH
						+ " where "
						+ AndroidOpenDbHelper.COLUMN_NAME_INST_ID_INST + " = "
						+ id, null);

				if (c2.getCount() > 0)
					count2 = c2.getCount();

				Photo[] tPhoto = new Photo[count2];
				i2 = 0;
				// inst[i].photo = photo;
				if (count2 > 0) {
					c2.moveToFirst();
					do {
						int jj = c2
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_PHOTO);
						System.out.println("jj dans PSugoDB =" + jj);

						tPhoto[i2] = new Photo(
								c2.getString(c2
										.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_PHOTO)),
								c2.getString(c2
										.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_LONG)),
								c2.getString(c2
										.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_LATITUDE)),
								c2.getString(c2
										.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_PHOTO_DATE)),
								c2.getString(c2
										.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_INST_PHOTO_TYPE)));
						i2++;
					} while (c2.moveToNext());

				}
				c2.close();
				// transferts du tableau de photo dans PhotoCollection
				PhotoCollection v = new PhotoCollection();
				for (int idx = 0; idx < tPhoto.length; idx++)
					v.add(tPhoto[idx]);
				inst[i].photo = v;

				//
				i++;
			} while (c.moveToNext());
			c.close();
		}
		return inst;
	}

	// delete (if exist) and insert directeur of type X.
	public void insertDirecteur(int instId, String nom, String genre,
			String type, String email, String telephone, String cin,
			String photo, String longitude, String latitude, String datePhoto) {

		deleteDirecteur(instId, type);

		String query = "INSERT INTO "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		SQLiteStatement p = db.compileStatement(query);

		p.bindNull(1);
		p.bindLong(2, instId);
		p.bindString(3, type);
		p.bindString(4, nom);
		p.bindString(5, genre);
		p.bindString(6, email);
		p.bindString(7, telephone);
		p.bindString(8, cin);
		long dirId = p.executeInsert();

		query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR_PH
				+ " VALUES (?, ?, ?, ?, ?)";
		p = db.compileStatement(query);

		p.bindLong(1, dirId);
		p.bindString(2, photo);
		p.bindString(3, longitude);
		p.bindString(4, latitude);
		p.bindString(5, datePhoto);
		p.executeInsert();
	}

	public void deleteDirecteur(int instId, String type) {
		// delete la photo de ce directeur
		String query = "DELETE FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR_PH + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_ID_DIR + " IN (SELECT "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_ID + " FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID + " = " + instId
				+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_DIR_TYPE + " = "
				+ DatabaseUtils.sqlEscapeString(type) + ")";
		db.execSQL(query);

		// delete ce directeur
		query = "DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID
				+ " = " + instId + " AND "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_TYPE + " = "
				+ DatabaseUtils.sqlEscapeString(type);
		db.execSQL(query);
	}

	public void deleteDirecteur(int instId) {
		// delete la photo de ce directeur
		String query = "DELETE FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR_PH + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_ID_DIR + " IN (SELECT "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_ID + " FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID + " = " + instId
				+ ")";
		db.execSQL(query);

		// delete ce directeur
		query = "DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID
				+ " = " + instId;
		db.execSQL(query);
	}

	public Directeur selectDirecteur(int instId, String type) {

		String query = "select * from "
				+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID + " = " + instId
				+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_DIR_TYPE + " = "
				+ DatabaseUtils.sqlEscapeString(type);
		Cursor c = db.rawQuery(query, null);

		Directeur directeur = null;

		if (c.getCount() > 0) {
			c.moveToFirst();
			int dirId = c.getInt(c
					.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_ID));
			directeur = new Directeur(
					c.getInt(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_INSTID)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_TYPE)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_NAME)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_GENRE)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_EMAIL)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_PHONE)),
					c.getString(c
							.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_CIN)));

			Cursor c2 = db.rawQuery("select * from "
					+ AndroidOpenDbHelper.TABLE_NAME_DIRECTEUR_PH + " where "
					+ AndroidOpenDbHelper.COLUMN_NAME_DIR_ID_DIR + " = "
					+ dirId, null);

			if (c2.getCount() > 0) {
				c2.moveToFirst();
				directeur.photo = new Photo(
						c2.getString(c2
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_PHOTO)),
						c2.getString(c2
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_LONG)),
						c2.getString(c2
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_LATITUDE)),
						c2.getString(c2
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_DIR_PHOTO_DATE)),
						null);
			}
			c2.close();
		}
		c.close();
		return directeur;
	}

	// delete (if exist) and insert classe of name X.
	public void insertClasse(int instId, String nomClasse, int nombreEleve,
			Photo photoClasse, String professeur, String emailProf, String phoneProf, 
			String cinProf, String genreProf, Photo photoProf) {

		deleteClasse(instId, nomClasse);

		String query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_CLASSE
				+ " VALUES (?, ?, ?, ?)";
		SQLiteStatement p = db.compileStatement(query);

		p.bindNull(1);
		p.bindLong(2, instId);
		p.bindString(3, nomClasse);
		p.bindLong(4, nombreEleve);
		long classeId = p.executeInsert();

		query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_CLASSE_PH
				+ " VALUES (?, ?, ?, ?, ?, ?)";
		p = db.compileStatement(query);

		p.bindNull(1);
		p.bindLong(2, classeId);
		p.bindString(3, photoClasse.photo);
		p.bindString(4, photoClasse.longitude);
		p.bindString(5, photoClasse.latitude);
		p.bindString(6, photoClasse.datePhoto);
		p.executeInsert();

		query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_PROF
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		p = db.compileStatement(query);

		p.bindNull(1);
		p.bindLong(2, instId);
		p.bindLong(3, classeId);
		p.bindString(4, professeur);
		p.bindString(5, emailProf);
		p.bindString(6, phoneProf);
		p.bindString(7, cinProf);
		p.bindString(8, genreProf);
		
		
		
		long profId = p.executeInsert();

		query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_PROF_PH
				+ " VALUES (?, ?, ?, ?, ?, ?)";
		p = db.compileStatement(query);
		p.bindNull(1);
		p.bindLong(2, profId);
		p.bindString(3, photoProf.photo);
		p.bindString(4, photoProf.longitude);
		p.bindString(5, photoProf.latitude);
		p.bindString(6, photoProf.datePhoto);
		p.executeInsert();
	}

	public void deleteClasse(int instId, String nomClasse) {
		// delete les photos de cette classe
		String query = "DELETE FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_CLASSE_PH + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_ID_CL + " IN (SELECT "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_ID + " FROM "
				+ AndroidOpenDbHelper.TABLE_NAME_CLASSE + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_INST_ID + " = " + instId
				+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_CL_NAME + " = "
				+ DatabaseUtils.sqlEscapeString(nomClasse) + ")";
		db.execSQL(query);

		// delete la photo du prof de cette classe
		query = "DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_PROF_PH
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_PR_ID_PR
				+ " IN (SELECT " + AndroidOpenDbHelper.COLUMN_NAME_PR_ID
				+ " FROM " + AndroidOpenDbHelper.TABLE_NAME_PROF + ", "
				+ AndroidOpenDbHelper.TABLE_NAME_CLASSE + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_PR_INST_ID + " = " + instId
				+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_CL_NAME + " = "
				+ DatabaseUtils.sqlEscapeString(nomClasse) + " AND "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_ID + " = "
				+ AndroidOpenDbHelper.COLUMN_NAME_PR_CL_ID + ")";
		db.execSQL(query);

		// delete le prof de cette classe
		query = "DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_PROF
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_PR_CL_ID
				+ " IN (SELECT " + AndroidOpenDbHelper.COLUMN_NAME_CL_ID
				+ " FROM " + AndroidOpenDbHelper.TABLE_NAME_CLASSE + " WHERE "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_INST_ID + " = " + instId
				+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_CL_NAME + " = "
				+ DatabaseUtils.sqlEscapeString(nomClasse) + ")";
		db.execSQL(query);

		// delete cette classe
		query = "DELETE FROM " + AndroidOpenDbHelper.TABLE_NAME_CLASSE
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_CL_INST_ID
				+ " = " + instId + " AND "
				+ AndroidOpenDbHelper.COLUMN_NAME_CL_NAME + " = "
				+ DatabaseUtils.sqlEscapeString(nomClasse);
		db.execSQL(query);

	}

	public Classe[] selectClasse(int instId) {

		String query = "select * from " + AndroidOpenDbHelper.TABLE_NAME_CLASSE
				+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_CL_INST_ID
				+ " = " + instId;
		Cursor c = db.rawQuery(query, null);

		int nbClasse =0;
		int cursorRows = c.getCount();
		int classeId;

		
		if (cursorRows > 0){
			nbClasse = cursorRows;
		}
		
		Classe[] classes = new Classe[nbClasse];
		
		
		if (nbClasse > 0) {
			c.moveToFirst();
			
			int i = 0;
			Photo photoClasse = null;
			Photo photoProfesseur = null;
			String professeur = null;
			String emailProf = null;
			String phoneProf = null;
			String cinProf = null;
			String genreProf = null;
			
			int professeurId = 0;
			do {
				classeId = c.getInt(c
						.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_ID));
				Cursor c2 = db.rawQuery("select * from "
						+ AndroidOpenDbHelper.TABLE_NAME_CLASSE_PH + " where "
						+ AndroidOpenDbHelper.COLUMN_NAME_CL_ID_CL + " = "
						+ classeId, null);

				if (c2.getCount() > 0) {
					c2.moveToFirst();
					photoClasse = new Photo(
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_PHOTO)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_LONG)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_LATITUDE)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_PHOTO_DATE)),
							null);

				}
				c2.close();

				c2 = db.rawQuery("select * from "
						+ AndroidOpenDbHelper.TABLE_NAME_PROF + " where "
						+ AndroidOpenDbHelper.COLUMN_NAME_PR_CL_ID + " = "
						+ classeId, null);

				if (c2.getCount() > 0) {
					c2.moveToFirst();
					professeur = c2
							.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_NAME));
					professeurId = c2
							.getInt(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_ID));
					emailProf = c2
							.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_EMAIL));
					phoneProf = c2
							.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_PHONE));
					cinProf = c2
							.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_CIN));
					genreProf = c2
							.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_GENRE));

				}
				c2.close();

				c2 = db.rawQuery("select * from "
						+ AndroidOpenDbHelper.TABLE_NAME_PROF_PH + " where "
						+ AndroidOpenDbHelper.COLUMN_NAME_PR_ID_PR + " = "
						+ professeurId, null);

				if (c2.getCount() > 0) {
					c2.moveToFirst();
					photoProfesseur = new Photo(
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_PHOTO)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_LONG)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_LATITUDE)),
							c2.getString(c2
									.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_PR_PHOTO_DATE)),
							null);
				}
				c2.close();

				classes[i] = new Classe(
						c.getInt(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_INST_ID)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_NAME)),
						c.getInt(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CL_NBR_ELEVES)),
						photoClasse, professeur, emailProf, phoneProf, cinProf, genreProf, photoProfesseur);
				i=i+1;
			} while (c.moveToNext());
		}
		c.close();
		return classes;
	}

	public CommuneSectRurale[] selectComsectR() {
		Cursor c = db.rawQuery("select * from "
				+ AndroidOpenDbHelper.TABLE_NAME_COMSECTR, null);
		int i = 0;
		int count1 = 0;
		if (c.getCount() > 0)
			count1 = c.getCount();

		CommuneSectRurale[] csrs = new CommuneSectRurale[count1];

		if (count1 > 0) {
			c.moveToFirst();
			do {
				csrs[i] = new CommuneSectRurale(
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CS_COMMUNE)),
						c.getString(c
								.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_CS_SECTION_RURALE)));
				i++;
			} while (c.moveToNext());
			c.close();
		}
		return csrs;
	}

	public void insertComsectR(String commune, String sectionR) {

		String query = "INSERT INTO " + AndroidOpenDbHelper.TABLE_NAME_COMSECTR
				+ " VALUES (?, ?, ?)";
		SQLiteStatement p = db.compileStatement(query);

		p.bindNull(1);
		p.bindString(2, commune);
		p.bindString(3, sectionR);

		p.executeInsert();
	}
	
	public void delete_ALL_ComsectR() {

		// remove all rows from table
		db.execSQL("delete from "+ AndroidOpenDbHelper.TABLE_NAME_COMSECTR);

	}

	// Insert a username-password in login table
		public void insertUser(String user, String password) {
			deleteUser(user);
			String query = "INSERT INTO "
					+ AndroidOpenDbHelper.TABLE_NAME_LOGIN + " VALUES ( null, "
					+ DatabaseUtils.sqlEscapeString(user) + ", "
					+ DatabaseUtils.sqlEscapeString(password) + ")";
			try {
				db.execSQL(query);
			} catch (Exception e) {
				System.out.println("insertLogin failed ...");
				e.printStackTrace();
			}
		}

		public void deleteUser(String user){
			db.execSQL("delete from "+ AndroidOpenDbHelper.TABLE_NAME_LOGIN + 
					" where " + AndroidOpenDbHelper.COLUMN_NAME_LOGIN_USER 
					+ " = " + DatabaseUtils.sqlEscapeString(user));		
		}
		
		public boolean validateLogin(String user, String password){
			String query = "select * from " + AndroidOpenDbHelper.TABLE_NAME_LOGIN
					+ " WHERE " + AndroidOpenDbHelper.COLUMN_NAME_LOGIN_USER
					+ " = " + DatabaseUtils.sqlEscapeString(user)
					+ " AND " + AndroidOpenDbHelper.COLUMN_NAME_LOGIN_PASSWORD
					+ " = " + DatabaseUtils.sqlEscapeString(password);
			
			Cursor c = db.rawQuery(query, null);

			if (c.getCount() ==  1) 
				return true;

			return false;
		}

}
