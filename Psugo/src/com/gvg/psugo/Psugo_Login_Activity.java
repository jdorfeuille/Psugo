package com.gvg.psugo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Psugo_Login_Activity extends Activity {

	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnCancel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.psugo_login_activity);

		txtUserName = (EditText) this.findViewById(R.id.txtUname);
		txtPassword = (EditText) this.findViewById(R.id.txtPwd);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if ((txtUserName.getText().toString()).equals(txtPassword
						.getText().toString())) {
					Toast.makeText(Psugo_Login_Activity.this, "Login Successful",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(Psugo_Login_Activity.this, "Invalid Login",
							Toast.LENGTH_LONG).show();
				}
				finish();

			}
		});
	}
}
