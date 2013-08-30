package com.gvg.psugo;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MyOnItemSelectedListener implements OnItemSelectedListener {

	public String itemSelect;
	@Override
	public void onItemSelected(AdapterView parent, View view, int pos, long id) {

		itemSelect = parent.getItemAtPosition(pos).toString();
		Toast.makeText(parent.getContext(), "item Selected : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView parent) {

	}
}