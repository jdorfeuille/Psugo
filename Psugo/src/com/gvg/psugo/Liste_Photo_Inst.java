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
 
public class Liste_Photo_Inst extends Activity {
 

	
	// we need to getExtra institutionID
	 Institution[] myInstList;
	 int theInstID;
 
     TableLayout tl;
     TableRow tr;
     TextView typePhotoTV,valueTV;
     ImageView valueIV;
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
		myInstList = psudb.selectInstitution();
		Institution myInst = getInstitution(theInstID);
		// create Photo Array from PhotoCollection 
		int nPhotos = myInst.photo.size();
		System.out.println("MyInstitution nombre de photos " + nPhotos);
		Photo[] myPhotoList = new Photo[nPhotos];
		int idx = 0;
		Enumeration<Photo> el = myInst.photo.elements();
		while (el.hasMoreElements()) {
			Photo thePhoto = (Photo) el.nextElement();
			myPhotoList[idx] = thePhoto;
			idx++;
		}
		//
        setContentView(R.layout.liste_photos_institutions);
        tl = (TableLayout) findViewById(R.id.maintable);
        addHeaders();
        addData(myPhotoList);
    }
 
    private Institution getInstitution(int instIdTofind) {
		// TODO Auto-generated method stub
    	Institution theFoundInst= null;
    	for (int idx=0; idx < myInstList.length; idx++) {
    		if ( myInstList[idx].id == instIdTofind) {
    			theFoundInst = myInstList[idx];
    			break;
    		}
    	}
		return theFoundInst;
	}

	/** This function add the headers to the table**/
    public void addHeaders(){
 
         /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
 
        /** Creating a TextView to add to the row **/
        TextView typePhotoTV = new TextView(this);
        typePhotoTV.setText("Type Photo");
        typePhotoTV.setTextColor(Color.GRAY);
        typePhotoTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        typePhotoTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        typePhotoTV.setPadding(110, 5, 5, 0);
        tr.addView(typePhotoTV);  // Adding textView to tablerow.
 
        /** Creating another textview **/
        TextView valueTV = new TextView(this);
        valueTV.setText("Photo");
        valueTV.setTextColor(Color.GRAY);
        valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        valueTV.setPadding(110, 5, 5, 0);
        valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(valueTV); // Adding textView to tablerow.
 
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
        divider.setText("-----------------");
        divider.setTextColor(Color.GREEN);
        divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider.setPadding(110, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.
 
        TextView divider2 = new TextView(this);
        divider2.setText("-------------------------");
        divider2.setTextColor(Color.GREEN);
        divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider2.setPadding(110, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.
 
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }
 
    /** This function add the data to the table **/
    @SuppressWarnings("deprecation")
	public void addData(Photo[] myPhotoList){
    	PsugoUtils psu = new PsugoUtils(getBaseContext());
        for (int i = 0; i < myPhotoList.length; i++)
        {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
 
            /** Creating a TextView to add to the row **/
            typePhotoTV = new TextView(this);
            typePhotoTV.setText(myPhotoList[i].typePhoto);
            typePhotoTV.setTextColor(Color.RED);
            typePhotoTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            typePhotoTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            typePhotoTV.setPadding(110, 5, 0, 5);
            tr.addView(typePhotoTV);  // Adding textView to tablerow.
 
            /** Creating another textview **/
            //valueTV = new TextView(this);
            valueIV = new ImageView(this);
            Bitmap bmp = psu.StringToBitMap(myPhotoList[i].photo);
            Bitmap thmbn = ThumbnailUtils.extractThumbnail(bmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            valueIV.setImageBitmap(thmbn);
           // valueTV.setText(myPhotoList[i].photo);
            //valueTV.setTextColor(Color.GREEN);
            //valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            //valueTV.setPadding(5, 5, 5, 5);
            //valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            valueIV.setPadding(5, 5,0, 5);
            tr.addView(valueIV); // Adding textView to tablerow.
            
 
            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }
}
