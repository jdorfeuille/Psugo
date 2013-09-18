package com.gvg.psugo;
import android.content.Context;
import java.io.File;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;

// A helper class to manage database creation and version management. 
public class AndroidOpenDbHelper extends SQLiteOpenHelper {
	// Database attributes
	public static final String DB_NAME = "psugo_lite_db2";
	public static final String DB_FILE_PATH = "/storage/sdcard1";
	public static final int DB_VERSION = 26;

	// Table DDL
	
	
	//INSTITUTIONS
	public static final String TABLE_NAME_INSTITUTION = "institution_table";
	public static final String COLUMN_NAME_INST_ID = "inst_id_column";
	public static final String COLUMN_NAME_INST_NAME = "inst_name_column";
	public static final String COLUMN_NAME_INST_DEPT = "inst_dept_column";
	public static final String COLUMN_NAME_INST_ARROND = "inst_arrond_column";
	public static final String COLUMN_NAME_INST_COMMUNE = "inst_commune_column";
	public static final String COLUMN_NAME_INST_SECTION_RURALE = "inst_section_rurale_column";
	public static final String COLUMN_NAME_INST_ADRESSE = "inst_adresse_column";
	public static final String COLUMN_NAME_INST_ADRESSE_DETAIL = "inst_adresse_detail_column";
	public static final String COLUMN_NAME_INST_PHONE = "inst_telephone_column";
	public static final String COLUMN_NAME_INST_TROUVEE= "inst_trouvee_column"; //????
	public static final String COLUMN_NAME_INFO_BANCAIRE= "info_bancaire"; 
	
	
	//INST_PHOTO
	public static final String TABLE_NAME_INSTITUTION_PH = "institution_photo_table";
	public static final String COLUMN_NAME_INST_ID_INST = "inst_id_inst_column";
	public static final String COLUMN_NAME_INST_ID_PH = "inst_id_ph_column";
	public static final String COLUMN_NAME_INST_PHOTO = "inst_photo_column";
	public static final String COLUMN_NAME_INST_LONG = "inst_longitude_column";
	public static final String COLUMN_NAME_INST_LATITUDE = "inst_latitude_column";
	public static final String COLUMN_NAME_INST_PHOTO_DATE = "inst_photo_date_column";
	public static final String COLUMN_NAME_INST_PHOTO_TYPE = "inst_photo_type_column";
	
	//DIRECTEUR	
	public static final String TABLE_NAME_DIRECTEUR = "directeur_table";
	public static final String COLUMN_NAME_DIR_ID = "dir_id_column";
	public static final String COLUMN_NAME_DIR_INSTID = "dir_instid_column";
	public static final String COLUMN_NAME_DIR_TYPE = "dir_type_column";
	public static final String COLUMN_NAME_DIR_NAME = "dir_name_column";
	public static final String COLUMN_NAME_DIR_GENRE = "dir_genre_column";
	public static final String COLUMN_NAME_DIR_EMAIL = "dir_email_column";
	public static final String COLUMN_NAME_DIR_PHONE = "dir_phone_column";
	public static final String COLUMN_NAME_DIR_CIN = "dir_cin_column";
	
	//DIR_PHOTO
	public static final String TABLE_NAME_DIRECTEUR_PH = "directeur_photo_table";
	public static final String COLUMN_NAME_DIR_ID_DIR = "dir_id_dir_column";
	public static final String COLUMN_NAME_DIR_PHOTO = "dir_photo_column";
	public static final String COLUMN_NAME_DIR_LONG = "dir_longitude_column";
	public static final String COLUMN_NAME_DIR_LATITUDE = "dir_latitude_column";
	public static final String COLUMN_NAME_DIR_PHOTO_DATE = "dir_photo_date_column";
	
	//CLASSE	
	public static final String TABLE_NAME_CLASSE = "classe_table";
	public static final String COLUMN_NAME_CL_ID = "cl_id_column";           //Classe ID
	public static final String COLUMN_NAME_CL_INST_ID = "cl_inst_id_column";
	public static final String COLUMN_NAME_CL_NAME = "cl_name_column";
	public static final String COLUMN_NAME_CL_NBR_ELEVES = "cl_nbr_eleves_column";
	
	//CLASSE_PH
	public static final String TABLE_NAME_CLASSE_PH = "classe_photo_table";
	public static final String COLUMN_NAME_CL_ID_PH = "cl_id_ph_column"; 
	public static final String COLUMN_NAME_CL_ID_CL = "cl_id_cl_column"; 
	public static final String COLUMN_NAME_CL_PHOTO = "cl_photo_column";
	public static final String COLUMN_NAME_CL_LONG = "cl_longitude_column";
	public static final String COLUMN_NAME_CL_LATITUDE = "cl_latitude_column";
	public static final String COLUMN_NAME_CL_PHOTO_DATE = "cl_photo_date_column";
	
	
	//PROFESSEUR	
	public static final String TABLE_NAME_PROF = "professeur_table";
	public static final String COLUMN_NAME_PR_ID = "pr_id_column";           //Classe ID
	public static final String COLUMN_NAME_PR_INST_ID = "pr_inst_id_column";
	public static final String COLUMN_NAME_PR_CL_ID = "pr_cl_id_column";
	public static final String COLUMN_NAME_PR_NAME = "pr_name_column";
	public static final String COLUMN_NAME_PR_EMAIL = "pr_email_column";
	public static final String COLUMN_NAME_PR_PHONE = "pr_phone_column";
	public static final String COLUMN_NAME_PR_CIN = "pr_cin_column";
	public static final String COLUMN_NAME_PR_GENRE = "pr_genre_column";

	
	//PROFESSEUR_PH
	public static final String TABLE_NAME_PROF_PH = "pr_photo_table";
	public static final String  COLUMN_NAME_PR_ID_PH = "pr_id_ph_column"; 
	public static final String  COLUMN_NAME_PR_ID_PR = "pr_id_pr_column"; 
	public static final String COLUMN_NAME_PR_PHOTO = "pr_photo_column";
	public static final String COLUMN_NAME_PR_LONG = "pr_longitude_column";
	public static final String COLUMN_NAME_PR_LATITUDE = "pr_latitude_column";
	public static final String COLUMN_NAME_PR_PHOTO_DATE = "pr_photo_date_column";
	
	//COMMUNE SECTION RURALE
	public static final String TABLE_NAME_COMSECTR = "comsectr_table";
	public static final String COLUMN_NAME_CS_ID = "cs_id_column";           
	public static final String COLUMN_NAME_CS_COMMUNE = "cs_commune_column";
	public static final String COLUMN_NAME_CS_SECTION_RURALE = "cs_section_rurale_column";
	
	
	//LOGIN
	public static final String TABLE_NAME_LOGIN = "login_table";
	public static final String COLUMN_NAME_LOGIN_ID = "login_id_column";           
	public static final String COLUMN_NAME_LOGIN_USER = "login_user_column";
	public static final String COLUMN_NAME_LOGIN_PASSWORD = "login_password_column";
	

	public AndroidOpenDbHelper(Context context) {
		
		//super(context, DB_NAME, null, DB_VERSION);
	    //super(context, Environment.getExternalStorageDirectory() + File.separator +  DB_NAME, null, DB_VERSION);

		super(context, DB_FILE_PATH + File.separator +  DB_NAME, null, DB_VERSION);
	    
		
	}

	// Called when the database is created for the first time. 
	//This is where the creation of tables and the initial population of the tables should happen.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// We need to check whether table that we are going to create is already exists.
		//Because this method get executed every time we created an object of this class. 
		
		
		//"create table if not exists TABLE_NAME ( BaseColumns._ID integer primary key autoincrement, FIRST_COLUMN_NAME text not null, SECOND_COLUMN_NAME integer not null);"
		//INSTITUTION
		String sqlQueryToCreateInstitutionTable = "create table if not exists " + TABLE_NAME_INSTITUTION + " ( " 
																+ COLUMN_NAME_INST_ID + " integer primary key not null, "
																+ COLUMN_NAME_INST_NAME + " text not null, "
																+ COLUMN_NAME_INST_DEPT + " text not null, "
																+ COLUMN_NAME_INST_ARROND + " text not null, "
																+ COLUMN_NAME_INST_COMMUNE + " text not null, "
																+ COLUMN_NAME_INST_SECTION_RURALE + " text not null, "
																+ COLUMN_NAME_INST_ADRESSE + " text not null, "
																+ COLUMN_NAME_INST_ADRESSE_DETAIL + " text not null, "
																+ COLUMN_NAME_INST_PHONE + " text not null, "
																+ COLUMN_NAME_INST_TROUVEE + " text not null, "
																+ COLUMN_NAME_INFO_BANCAIRE + " text not null);";
		
		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateInstitutionTable);
		
		//INSTITUTION_PHOTO
		String sqlQueryToCreateInstitutionPHTable = "create table if not exists " + TABLE_NAME_INSTITUTION_PH + " ( " 
																+ COLUMN_NAME_INST_ID_PH + " integer primary key autoincrement, "
																+ COLUMN_NAME_INST_ID_INST + " integer not null, "
																+ COLUMN_NAME_INST_PHOTO + " text not null, "
																+ COLUMN_NAME_INST_LONG + " text not null, "
																+ COLUMN_NAME_INST_LATITUDE + " text not null, "
																+ COLUMN_NAME_INST_PHOTO_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
																+ COLUMN_NAME_INST_PHOTO_TYPE + " text not null);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateInstitutionPHTable);
		
		//DIRECTEUR TABLE
		String sqlQueryToCreateDirecteurTable = "create table if not exists " + TABLE_NAME_DIRECTEUR  + " ( " 
				+ COLUMN_NAME_DIR_ID + " integer primary key autoincrement,  "
				+ COLUMN_NAME_DIR_INSTID + " integer not null, "
				+ COLUMN_NAME_DIR_TYPE + " text not null, "
				+ COLUMN_NAME_DIR_NAME + " text not null, "
				+ COLUMN_NAME_DIR_GENRE + " text not null, "
				+ COLUMN_NAME_DIR_EMAIL + " text not null, "
				+ COLUMN_NAME_DIR_PHONE + " text not null, "
				+ COLUMN_NAME_DIR_CIN + " text not null);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateDirecteurTable);
		
		//DIR_PHOTO
		String sqlQueryToCreateDirecteurPHTable = "create table if not exists " + TABLE_NAME_DIRECTEUR_PH + " ( " 
				+ COLUMN_NAME_DIR_ID_DIR + " text not null, "
				+ COLUMN_NAME_DIR_PHOTO + " BLOB not null, "
				+ COLUMN_NAME_DIR_LONG + " text not null, "
				+ COLUMN_NAME_DIR_LATITUDE + " text not null, "
				+ COLUMN_NAME_DIR_PHOTO_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateDirecteurPHTable);

		//CLASSE	
		String sqlQueryToCreateClasseTable = "create table if not exists " + TABLE_NAME_CLASSE + " ( " 
				+ COLUMN_NAME_CL_ID + " integer primary key autoincrement,  "
				+ COLUMN_NAME_CL_INST_ID + " text not null, "
				+ COLUMN_NAME_CL_NAME + " text not null, "
				+ COLUMN_NAME_CL_NBR_ELEVES + " text not null);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateClasseTable);
		
		//CLASSE_PHOTO
		String sqlQueryToCreateClassePHTable = "create table if not exists " + TABLE_NAME_CLASSE_PH  + " ( " 
				+ COLUMN_NAME_CL_ID_PH + " integer primary key autoincrement,  "
				+ COLUMN_NAME_CL_ID_CL + " integer not null, "
				+ COLUMN_NAME_CL_PHOTO + " BLOB not null, "
				+ COLUMN_NAME_CL_LONG + " text not null, "
				+ COLUMN_NAME_CL_LATITUDE + " text not null, "
				+ COLUMN_NAME_CL_PHOTO_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateClassePHTable);
		
		//PROFESSEUR	
		String sqlQueryToCreateProfTable = "create table if not exists " + TABLE_NAME_PROF + " ( " 
				+ COLUMN_NAME_PR_ID + " integer primary key autoincrement, "
				+ COLUMN_NAME_PR_INST_ID + " integer not null, "
				+ COLUMN_NAME_PR_CL_ID + " integert not null, "
				+ COLUMN_NAME_PR_NAME + " text not null, "
				+ COLUMN_NAME_PR_EMAIL + " text not null, "
				+ COLUMN_NAME_PR_PHONE + " text not null, "
				+ COLUMN_NAME_PR_CIN + " text not null, "
				+ COLUMN_NAME_PR_GENRE + " text not null);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateProfTable);
			
		//PROFESSEUR_PH
		String sqlQueryToCreateProfPHTable = "create table if not exists " + TABLE_NAME_PROF_PH + " ( " 
				+ COLUMN_NAME_PR_ID_PH + " integer primary key autoincrement, "
				+ COLUMN_NAME_PR_ID_PR + " integer not null, "
				+ COLUMN_NAME_PR_PHOTO + " BLOB not null, "
				+ COLUMN_NAME_PR_LONG + " text not null, "
				+ COLUMN_NAME_PR_LATITUDE + " text not null, "
				+ COLUMN_NAME_PR_PHOTO_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateProfPHTable);
		
		//
		//COMMUNE SECTION RURALE
		String sqlQueryToCreateComsectRTable = "create table if not exists " + TABLE_NAME_COMSECTR + " ( " 
				+ COLUMN_NAME_CS_ID + " integer primary key autoincrement, "
				+ COLUMN_NAME_CS_COMMUNE + " text not null, "
				+ COLUMN_NAME_CS_SECTION_RURALE + " text not null );";


		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateComsectRTable);
		
		//
		// Login
		String sqlQueryToCreateLoginTable = "create table if not exists " + TABLE_NAME_LOGIN + " ( " 
				+ COLUMN_NAME_LOGIN_ID + " integer primary key autoincrement, "
				+ COLUMN_NAME_LOGIN_USER + " text not null, "
				+ COLUMN_NAME_LOGIN_PASSWORD + " text not null );";


		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateLoginTable);
		
		
	}

	// onUpgrade method is use when we need to upgrade the database in to a new version
	//As an example, the first release of the app contains DB_VERSION = 1
	//Then with the second release of the same app contains DB_VERSION = 2
	//where you may have add some new tables or alter the existing ones
	//Then we need check and do relevant action to keep our pass data and move with the next structure
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion != newVersion){
			// Upgrade the database
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INSTITUTION);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INSTITUTION_PH);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIRECTEUR);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIRECTEUR_PH);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CLASSE);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CLASSE_PH);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROF);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROF_PH);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMSECTR);
		    onCreate(db);
		}		
	}
}


