package com.example.driverwellness;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.example.driverwellness.app.*;



import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;


public class MainActivity extends Activity implements OnClickListener {

    private static final int ICON_KEY = 0;
    // the tuple key corresponding to the temperature displayed on the watch
    private static final int STATUS_KEY = 1;
    // This UUID identifies the weather app

    String [] anArray =   {"Happy", "Sad", "Annoyed", "Rage"} ;

    private static final UUID APP_UUID = UUID.fromString("28AF3DC7-E40D-490F-BEF2-29548C8B0600");

    private static int wid;

	private String testJson = "http://79.170.40.237/koollector.com/php/abc.json";
	private String userJson = "https://api-jp-t-itc.com/GetUserInfo?developerkey=c64ada9521d9&responseformat=json&userid=ITCUS_USERID_052";
	//private String vehicleJsonMM = "https://api-jp-t-itc.com/GetVehicleInfoMM?developerkey=c64ada9521d9&responseformat=json&vid=ITCUS_VID_052&infoids=[VehBehvr]";
	private String vehicleJson = "https://api-jp-t-itc.com/GetVehicleInfo?developerkey=c64ada9521d9&responseformat=json&vid=ITCUS_VID_052&infoids=[VehBehvr]&searchtime=";
	private String statJson = "https://api-jp-t-itc.com/GetStatisticsInfo?developerkey=c64ada9521d9&responseformat=xml&category=suddenbraking&vid=ITCJP_VID_001&deceleration=2&frequency=0";
	private static String TAG = MainActivity.class.getSimpleName();
	private Button btnMakeObjectRequest, btnMakeArrayRequest;
	String timeStamp;
	String year, month, day, hour, minute, sec;
	private int index;
	
	// Progress dialog
	private ProgressDialog pDialog;
	private String vehicleJsonTime;
	
	private int driverStatus = 0;
	private int timeinc = 0; // for next 10 min
	private String driverDescription = "Normal";
	
	private TextView txtResponse,textView;
	
	// temporary string to show the parsed response
	private String jsonResponse;
	private boolean bluetoothPairing = false; // app will trigger when bluetooth of the phone is in the proximity of the car
	private String []  speed = new String[10];
	private String []  brake = new String[10];
	private String [] steer = new String[10];
	

	NotificationManager NM;
	private TextView stt_accessibility;
	private Button btn_accessibility, btn_test;
	EditText one, two, three;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Common.init(this);


		stt_accessibility =(TextView)findViewById(R.id.stt_accessibility);
		
		btn_accessibility = (Button)findViewById(R.id.btn_accessibility);
		btn_accessibility.setOnClickListener(this);
		

		btn_test.setOnClickListener(this);
		
	    one = (EditText)findViewById(R.id.etext_title);
	    two = (EditText)findViewById(R.id.etext_heading);
	    three = (EditText)findViewById(R.id.etext_body);
				
	    
	    btnMakeObjectRequest = (Button) findViewById(R.id.btnObjRequest);

		txtResponse = (TextView) findViewById(R.id.txtResponse);


		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		
		bluetoothPairing = true;
		timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		year = timeStamp.substring(0, 4);
		month = timeStamp.substring(4, 6);
		day = timeStamp.substring(6, 8);
		hour = timeStamp.substring(8, 10);
		minute = timeStamp.substring(10, 12);
		sec = timeStamp.substring(12, 14);


        /* Tomtom integration
        SDKContext.setDeveloperKey("z2ry3qbwsra8dmgnrn3fqjgj");

        MyReverseGeocodeListener listener = new MyReverseGeocodeListener();

        // Adding the Parameters
        params = new ReverseGeocodeOptionalParameters();

        Coordinates coords = new Coordinates(-36.968139f,174.85934f);
        ReverseGeocodeOptionalParameters params = new ReverseGeocodeOptionalParameters();
        params.type = ReverseGeocodeOptionalParameters.REVERSE_TYPE_NATIONAL;
        ReverseGeocoder.reverseGeocode(coords, null, listener, null);

        */
		
		btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

                sendDataToWatch(0);


                makeUserJsonRequest();
                /* For Development Purpose Only, it won't work with the real dataset provided

				index = 0 ;
				// time increase will check next 10 min
				
				for (int i = timeinc; i<timeinc+10;i++)
					makeVehicleJsonRequest(i);

				analyzeDriver(steer,speed, brake);
				*/
				
				
			}
		});



	}
	

	@Override
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_accessibility:
			Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);

			startActivity(intent);


	
			break;
			
		}


	}	
	
	  
	   public void notify(View vobj){
	      String title = one.getText().toString();
	      String subject = two.getText().toString();
	      String body = three.getText().toString();	     
	  
	      PendingIntent pending=PendingIntent.getActivity(
	      getApplicationContext(),0, new Intent(),0);
		      
	      Notification notify = new Notification.Builder(getApplicationContext())
	      .setContentTitle(title) // Title of noti
	      .setContentText(subject) // Content of noti
	      .setTicker(body) // Speak this!
          .setWhen(System.currentTimeMillis())
          .setContentIntent(pending)
          .setAutoCancel(true)
          .setSmallIcon(R.drawable.ic_launcher)
	      .build();
	      NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	      NM.notify(0, notify);
	    

	   }
	  

	private void makeUserJsonRequest() {

		showpDialog();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				userJson, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
					//	textView.setText(response.toString());
						
						
						try {
							// Parsing json object response
							// response will be a json object
					
							
							JSONObject vi = response.getJSONArray("vehicleinfo").getJSONObject(0);
							String age = vi.getString("age");
							String sex = vi.getString("sex");
							//textView.setText(age);

							jsonResponse = "";
							

							txtResponse.setText(jsonResponse);

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	private void analyzeDriver(String steer[],String speed[], String br[]) {
		
		// using difference in speed to determine user condition
		int speedDiff = Math.abs(Integer.parseInt(speed[2]) - Integer.parseInt(speed[1]));
		int steeringDiff = Math.abs(Integer.parseInt(steer[2]) - Integer.parseInt(steer[1]));
		
		// Basically an algorithm to detect erratic driving, obviously this below is
		// not at all scientific. 
		if (speedDiff > 3 || steeringDiff > 300) {
			if (driverStatus<3)
				driverStatus++;
		} else if (speedDiff < 3) {
			if (driverStatus > 0)
			driverStatus--;
		}

		/*
		 *  Will determine over 10 min
		 * 
		 */
	}
	

	private void makeVehicleJsonRequest(int i) {

		showpDialog();
		// get json string over 10 min, we choose a timestamp that has data to debug
		vehicleJsonTime = vehicleJson + year + "-" + month + "-05+08:0" + i + ":00";
		
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				vehicleJsonTime, null, new Response.Listener<JSONObject>() {			
			

					@Override
					public void onResponse(JSONObject response) {
						//Log.d(TAG, response.toString());
					//	textView.setText(response.toString());
						
						
						try {
							// Parsing json object response
							// response will be a json object
					
							
							JSONObject vi = response.getJSONArray("vehicleinfo").getJSONObject(0);
							JSONObject data = vi.getJSONArray("data").getJSONObject(0);
							speed[index] = data.getString("Spd");
							brake[index] = data.getString("BrkIndcr");
							steer[index] = data.getString("SteerAg");

							
							jsonResponse = "";
							jsonResponse += steer[index];
							
						//	textView.setText(vehicleJsonTime);
							txtResponse.setText(jsonResponse);
							index += 1;

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

    public void sendDataToWatch(int wid) {


        // Build up a Pebble dictionary containing the weather icon and the current temperature in degrees celsius
        PebbleDictionary data = new PebbleDictionary();

        if(wid ==4 ) wid = 0;
        data.addUint8(ICON_KEY, (byte) wid);
        data.addString(STATUS_KEY, anArray[wid]);
        wid++;
        // Send the assembled dictionary to the weather watch-app; this is a no-op if the app isn't running or is not
        // installed
        PebbleKit.sendDataToPebble(getApplicationContext(), APP_UUID, data);
    }



    /* TomTom integration
    private class MyReverseGeocodeListener implements ReverseGeocodeListener{

        public void handleReverseGeocode(Vector<ReverseGeocodeData> data, Object payload){

            System.out.println(data);
            if (data != null && data.size()>0 ) {
                ReverseGeocodeData result = data.elementAt(0);

                showSpeedLimit.setText("The max speed is: " + result.maxSpeedKph);
            }else{
                System.out.println("wrong !!!!");
            }
        }

    }*/










}
