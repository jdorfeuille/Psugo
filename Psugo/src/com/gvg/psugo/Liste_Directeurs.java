package com.gvg.psugo;

import java.util.Enumeration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
 
public class Liste_Directeurs extends Activity {
 

	

    private static final String TYPE_ADMINISTRATIF ="Administratif";
    private static  final String TYPE_PEDAGOGIQUE = "Pedagogique";
    Directeur dirAdm, dirPed;
    Directeur[] dirList;
	int theInstID;
 
     TableLayout tl;
     TableRow tr;
     TextView nomDirecteurTV, genreDirectTV, typeDirectTV, photoDirectTV;
     ImageView photoDirecteurIV;
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
		dirAdm = psudb.selectDirecteur(theInstID, TYPE_ADMINISTRATIF);
		dirPed = psudb.selectDirecteur(theInstID, TYPE_PEDAGOGIQUE);
		psudb.close();
		int nDir = 0;
		if (dirAdm != null ) nDir++;
		if (dirPed != null ) nDir++;
		dirList = new Directeur[nDir];
		int idx=0;
		if (dirAdm != null ) {
			dirList[idx]=dirAdm;
			idx++;
		}
		if (dirPed != null ) {
			dirList[idx]=dirPed;
		}
        setContentView(R.layout.liste_directeurs);
        tl = (TableLayout) findViewById(R.id.directeurs_table);
        addHeaders();
        addData();
    }
 
	@Override
	public void onBackPressed() {
		// disable back key
	}
   

	/** This function add the headers to the table**/
    public void addHeaders(){
 
         /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
 
        /** Creating a TextView to add to the row **/
        nomDirecteurTV = new TextView(this);
        nomDirecteurTV.setText("Nom  ");
        nomDirecteurTV.setTextColor(Color.GRAY);
        nomDirecteurTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        nomDirecteurTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        nomDirecteurTV.setPadding(11, 5, 5, 0);
        tr.addView(nomDirecteurTV);  // Adding textView to tablerow.
 
        /** Creating another textview **/
        typeDirectTV = new TextView(this);
        typeDirectTV.setText("Type  ");
        typeDirectTV.setTextColor(Color.GRAY);
        typeDirectTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        typeDirectTV.setPadding(11, 5, 5, 0);
        typeDirectTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(typeDirectTV); // Adding textView to tablerow.
        
        /** Creating another textview **/
        genreDirectTV = new TextView(this);
        genreDirectTV.setText("genre  ");
        genreDirectTV.setTextColor(Color.GRAY);
        genreDirectTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        genreDirectTV.setPadding(11, 5, 5, 0);
        genreDirectTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(genreDirectTV); // Adding textView to tablerow.
        
        /** Creating another textview **/
        photoDirectTV = new TextView(this);
        photoDirectTV.setText("photo ");
        photoDirectTV.setTextColor(Color.GRAY);
        photoDirectTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        photoDirectTV.setPadding(11, 5, 5, 0);
        photoDirectTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(photoDirectTV); // Adding textView to tablerow.
 
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
        for (int i = 0; i < dirList.length; i++)
        {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
 
            /** Creating a TextView to add to the row **/
            nomDirecteurTV = new TextView(this);
            nomDirecteurTV.setText(dirList[i].nom);
            nomDirecteurTV.setTextColor(Color.RED);
            nomDirecteurTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            nomDirecteurTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            nomDirecteurTV.setPadding(11, 5, 0, 5);
            tr.addView(nomDirecteurTV);  // Adding textView to tablerow.
            
            /** Creating a TextView to add to the row **/
            // Here we need to check if we have a Prof
            typeDirectTV = new TextView(this);
            typeDirectTV.setText(dirList[i].typeDirection);
            typeDirectTV.setTextColor(Color.RED);
            typeDirectTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            typeDirectTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            typeDirectTV.setPadding(11, 5, 0, 5);
            tr.addView(typeDirectTV);  // Adding textView to tablerow.
 
            /** Creating an  imageview **/

            genreDirectTV = new TextView(this);
            genreDirectTV.setText(dirList[i].genre);
            genreDirectTV.setTextColor(Color.RED);
            genreDirectTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            genreDirectTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            genreDirectTV.setPadding(5, 5,0, 5);
            tr.addView(genreDirectTV); // Adding textView to tablerow.
            
            /** Creating another imageview **/
            // Here we need to check if we have a Prof
            //valueTV = new TextView(this);

            photoDirecteurIV = new ImageView(this);
            Bitmap bmp2 = psu.StringToBitMap(dirList[i].photo.photo);
            Bitmap thmbn2 = ThumbnailUtils.extractThumbnail(bmp2, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            photoDirecteurIV.setImageBitmap(thmbn2);
            photoDirecteurIV.setPadding(5, 5,0, 5);
            tr.addView(photoDirecteurIV); // Adding textView to tablerow.


            
 
            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT));
        TextView blankLine = new TextView(this);
        blankLine.setText(" ");   
        tr.addView(blankLine); // Adding textView to tablerow.
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        // Test Image Button
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT));
        ImageButton btn = new ImageButton(this);
        btn.setImageResource(R.drawable.arrow_right);
        btn.setId(16);
        tr.addView(btn);
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
        		//System.out.println("Yeah Image Button ");
				finish();
			}
        });
        
    }
}
