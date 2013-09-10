package com.gvg.psugo;

import java.util.Enumeration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
 
public class Liste_Classes_Prof extends Activity {
 

	
	// we need to getExtra institutionID
	 Classe[] myClasseList;
	 int theInstID;
 
     TableLayout tl;
     TableRow tr;
     TextView nomClasseTV,nomProfTV;
     ImageView photoClasseIV, photoProfIV;
     final int THUMBNAIL_SIZE = 200;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//
    	// getextras
        super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			theInstID = extras.getInt("instId");
		}
		PsugoDB psudb = new PsugoDB(getBaseContext()); 
		psudb.open(); 
		myClasseList = psudb.selectClasse(theInstID);
		psudb.close();
        setContentView(R.layout.liste_photos_classe);
        tl = (TableLayout) findViewById(R.id.classe_table);
        addHeaders();
        addData();
    }
 
   

	/** This function add the headers to the table**/
    public void addHeaders(){
 
         /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
 
        /** Creating a TextView to add to the row **/
        TextView nomClasseTV = new TextView(this);
        nomClasseTV.setText("Nom Classe");
        nomClasseTV.setTextColor(Color.GRAY);
        nomClasseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        nomClasseTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        nomClasseTV.setPadding(11, 5, 5, 0);
        tr.addView(nomClasseTV);  // Adding textView to tablerow.
 
        /** Creating another textview **/
        TextView nomProfTV = new TextView(this);
        nomProfTV.setText("Nom Prof");
        nomProfTV.setTextColor(Color.GRAY);
        nomProfTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        nomProfTV.setPadding(11, 5, 5, 0);
        nomProfTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(nomProfTV); // Adding textView to tablerow.
        
        /** Creating another textview **/
        TextView photoClasseTV = new TextView(this);
        photoClasseTV.setText("photo Classe");
        photoClasseTV.setTextColor(Color.GRAY);
        photoClasseTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        photoClasseTV.setPadding(11, 5, 5, 0);
        photoClasseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(photoClasseTV); // Adding textView to tablerow.
        
        /** Creating another textview **/
        TextView photoProfTV = new TextView(this);
        photoProfTV.setText("photo Prof");
        photoProfTV.setTextColor(Color.GRAY);
        photoProfTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        photoProfTV.setPadding(11, 5, 5, 0);
        photoProfTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(photoProfTV); // Adding textView to tablerow.
 
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
 
        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
 
        /** Creating another textview **/
        TextView divider = new TextView(this);
        divider.setText("------------");
        divider.setTextColor(Color.GREEN);
        divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider.setPadding(11, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.
 
        TextView divider2 = new TextView(this);
        divider2.setText("----------");
        divider2.setTextColor(Color.GREEN);
        divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider2.setPadding(11, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.
        
        TextView divider3 = new TextView(this);
        divider3.setText("--------------");
        divider3.setTextColor(Color.GREEN);
        divider3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider3.setPadding(11, 0, 0, 0);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.
        
        TextView divider4 = new TextView(this);
        divider4.setText("------------");
        divider4.setTextColor(Color.GREEN);
        divider4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider4.setPadding(11, 0, 0, 0);
        divider4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider4); // Adding textView to tablerow.
 
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }
 
    /** This function add the data to the table **/
    @SuppressWarnings("deprecation")
	public void addData(){
    	PsugoUtils psu = new PsugoUtils(getBaseContext());
        for (int i = 0; i < myClasseList.length; i++)
        {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
 
            /** Creating a TextView to add to the row **/
            nomClasseTV = new TextView(this);
            nomClasseTV.setText(myClasseList[i].nomClasse);
            nomClasseTV.setTextColor(Color.RED);
            nomClasseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            nomClasseTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            nomClasseTV.setPadding(11, 5, 0, 5);
            tr.addView(nomClasseTV);  // Adding textView to tablerow.
            
            /** Creating a TextView to add to the row **/
            // Here we need to check if we have a Prof
            nomProfTV = new TextView(this);
            nomProfTV.setText(myClasseList[i].nomProfesseur);
            nomProfTV.setTextColor(Color.RED);
            nomProfTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            nomProfTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            nomProfTV.setPadding(11, 5, 0, 5);
            tr.addView(nomProfTV);  // Adding textView to tablerow.
 
            /** Creating an  imageview **/

            photoClasseIV = new ImageView(this);
            Bitmap bmp = psu.StringToBitMap(myClasseList[i].photoClasse.photo);
            Bitmap thmbn = ThumbnailUtils.extractThumbnail(bmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            photoClasseIV.setImageBitmap(thmbn);
            photoClasseIV.setPadding(5, 5,0, 5);
            tr.addView(photoClasseIV); // Adding textView to tablerow.
            
            /** Creating another imageview **/
            // Here we need to check if we have a Prof
            //valueTV = new TextView(this);
            if (myClasseList[i].photoProfesseur != null ){
            	if (myClasseList[i].photoProfesseur.photo.isEmpty() == false ) {
	                photoProfIV = new ImageView(this);
	                Bitmap bmp2 = psu.StringToBitMap(myClasseList[i].photoProfesseur.photo);
	                Bitmap thmbn2 = ThumbnailUtils.extractThumbnail(bmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
	                photoProfIV.setImageBitmap(thmbn2);
	                photoProfIV.setPadding(5, 5,0, 5);
	                tr.addView(photoProfIV); // Adding textView to tablerow.
            	}
            	
            }

            
 
            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }
}
