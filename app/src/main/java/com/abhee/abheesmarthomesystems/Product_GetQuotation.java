package com.abhee.abheesmarthomesystems;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Product_GetQuotation extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener,LocationListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private static int RESULT_LOAD_IMAGE = 1;
    private GoogleMap mMap;
    byte img_store[] = null;
 //   String url ="http://13.232.65.248:8088/abheesmarthomes/savequotationrequest";
    String location1;
    LocationManager locationManager;
    AlertDialog.Builder builder,builder1;
    String log;
    TextView locat;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    EditText pincode,address,description;
    Button searchPin,chooseFile,submit,reset;
    ImageView image;
    SharedPreferences sharedPreferences;
    String custid,userid,mobile;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;



    public static Product_GetQuotation newInstance() {
        Product_GetQuotation fragment = new Product_GetQuotation();
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.product_getquotation, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        Product_GetQuotation.farname.setText("Get Quotation"+"");
        progressDialog=new ProgressDialog(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid", "");
        custid=sharedPreferences.getString("custid", "");
        mobile=sharedPreferences.getString("mobilenumber","");
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
       // title=(AppCompatTextView)v.findViewById(R.id.title);
       // title.setText(""+cats);
       /* back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ContentDashboard().newInstance());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
*/        locat=(TextView)v.findViewById(R.id.location);
        pincode=(EditText)v.findViewById(R.id.pin);
        searchPin=(Button)v.findViewById(R.id.btn_Search);
        searchPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();

                getpindata();
                }
        });
        address=(EditText)v.findViewById(R.id.addressM);
        description=(EditText)v.findViewById(R.id.descM);
        chooseFile=(Button)v.findViewById(R.id.btn_Choose);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        image=(ImageView)v.findViewById(R.id.image);
        submit=(Button)v.findViewById(R.id.btn_Submit);
        reset=(Button)v.findViewById(R.id.btn_Reset);
        builder1 = new AlertDialog.Builder(getContext());
        Location();
        getLocation();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                final String name=address.getText().toString();
                if (!isValidName(name)){
                    address.setError("Add address");
                }
                final String surname=description.getText().toString();
                if (!isValidSurname(surname)){
                    description.setError("Add description");
                }
                if (isValidName(name)&& isValidSurname(surname) &&image.getDrawable()!=null){
                    send_data(locat.getText().toString());
                }
                if (image.getDrawable()==null){
                    builder1.setMessage("Please select image")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Toast.makeText(getContext().getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });*/
                    AlertDialog alert = builder1.create();
                    //alert.setTitle("AlertDialogExample");
                    alert.show();
                }
             /*   builder.setTitle("chik");
                builder.setMessage(log).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!locat.getText().equals(null) && locat.getText().length() !=0){

                                }else{
                                    Toast.makeText(getContext().getApplicationContext(),"no oper",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getContext().getApplicationContext(), "you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("AlertDialogExample");
                alert.show();*/
            }

        });
        progressDialog.dismiss();
         reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincode.setText("");
                address.setText("");
                description.setText("");
                image.setImageBitmap(null);
                locat.setText("");
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(),LoginActivity.class));
        return v;
    }
    private boolean isValidName(String name1) {
        if (name1!=null&& name1.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidSurname(String surname1) {
        if (surname1!=null&&surname1.length()>0){
            return true;
        }
        return false;
    }
    private void getpindata() {
        String code=pincode.getText().toString();
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"http://api.openweathermap.org/data/2.5/forecast?zip="+code+",in&appid=990274466483cd969f2a96f84311fb8d&units=metric" ,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("response",response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(String.valueOf(response));

                    JSONArray jsonArray= jsonObject.getJSONArray("list");
                    JSONObject jsonObject5 = jsonObject.getJSONObject("city");
                    Log.i("id",jsonObject5.getString("id"));
                    Log.i("Location",jsonObject5.getString("name"));
                    JSONObject jsonObject6 = jsonObject5.getJSONObject("coord");
                    /*Log.i("location",jsonObject6.getString("lat")+" "+
                            jsonObject6.getString("lon"));*/
                    String lat=jsonObject6.getString("lat");
                    String lon = jsonObject6.getString("lon");
                    locat.setText("Location: "+lat+","+lon);
                    try{
                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(Double.valueOf(lat),Double.valueOf(lon),1);
                        address.setText(addresses.get(0).getAddressLine(0));
                        Toast.makeText(getActivity().getApplicationContext(),""+addresses.get(0).getAddressLine(0).toString(),Toast.LENGTH_SHORT).show();
                    }catch(Exception e){}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void send_data(String s) {
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("address", address.getText().toString());
        postParam.put("customerid", custid);
        postParam.put("modelnumber", Products_SubCategories.modelid);
        postParam.put("location", s);
        postParam.put("imgfiles", String.valueOf(chooseFile));
        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"savequotationrequest", new JSONObject(postParam), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.i("Response",response.toString());
                progressDialog.dismiss();
                JSONObject json = null;
                try {
                    json = new JSONObject(response.toString());
                    if (json.getString("status").contains("requestSubmittedSuccessfully")) {
                        Toast.makeText(getContext().getApplicationContext(), "we will get back to you soon", Toast.LENGTH_SHORT).show();
                        clearBox();
                        Intent i=new Intent(getContext(),DashboardActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "error in response", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(StringRequest);
    }
    private void clearBox() {
        pincode.setText("");
        address.setText("");
        description.setText("");
        image.setImageBitmap(null);
        Location();
        getLocation();
    }
    private void Location() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("location ssss","Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        locat.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        locat.setVisibility(View.VISIBLE);
        try{
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address.setText(addresses.get(0).getAddressLine(0));
           // Toast.makeText(getActivity().getApplicationContext(),""+addresses.get(0).getAddressLine(0).toString(),Toast.LENGTH_SHORT).show();
        }catch(Exception e){}
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext().getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 65, stream);
                img_store = stream.toByteArray();
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            try {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, stream);
                    img_store = stream.toByteArray();
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onMapSearch(View view) {
        EditText pincode = view.findViewById(R.id.pin);
        if(!pincode.getText().equals(null) && pincode.getText().length() !=0){
            String location = pincode.getText().toString();
            List<Address> addressList = null;
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                double latitude = 0,longitude=0;
                location1 =""+String.valueOf(latitude)+"&&"+String.valueOf(longitude);
               // Toast.makeText(getContext().getApplicationContext(),latLng +" ",Toast.LENGTH_LONG).show();
                locat =view.findViewById(R.id.location);
                locat.setText(latLng.toString());
                log= locat.getText().toString();
                locat.setVisibility(View.VISIBLE);
            }
        }else{
            Toast.makeText(getContext().getApplicationContext(),"enter pin code",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        mMap.setOnMarkerDragListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
    }
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }
    private void showDefaultLocation() {
        Toast.makeText(getContext(), "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(16.4349, 80.5688);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }
        }
    }
    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(8);
                    return false;
                }
            };
    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener = new GoogleMap.OnMyLocationClickListener() {
        @Override
        public void onMyLocationClick(@NonNull Location location) {

            mMap.setMinZoomPreference(8);
            pincode= getView().findViewById(R.id.pin);

            locat = (TextView) getView().findViewById(R.id.location);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker").draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //tv1.setText("Latitude:" + latitude + ", Longitude:" + longitude);
            locat.setText("Latitude:" + latitude + ", Longitude:" + longitude);
            locat.setVisibility(View.GONE);
            log =locat.getText().toString();
            Toast.makeText(getContext().getApplicationContext(),latitude +"Latitude",Toast.LENGTH_LONG).show();
            Toast.makeText(getContext().getApplicationContext(),longitude +"Longitude",Toast.LENGTH_LONG).show();

            location1 =""+String.valueOf(latitude)+"&&"+String.valueOf(longitude);
            Geocoder geocoder = new Geocoder(getContext().getApplicationContext(), Locale.getDefault());

            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();

               /* Toast.makeText(getContext().getApplicationContext(), "Address: " +
                        address + " " + city, Toast.LENGTH_LONG).show();
*/            } catch (IOException e) {
                e.printStackTrace();
            }
        }



       /* private void onMarkerDragEnd() {
            // TODO Auto-generated method stub
            Toast.makeText(
                    getApplicationContext(), "Lat " + mMap.getMyLocation().getLatitude() + " " + "Long " + mMap.getMyLocation().getLongitude(),
                    Toast.LENGTH_LONG).show();
            System.out.println("yalla b2a " + mMap.getMyLocation().getLatitude());


        }
*/
    };

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position = marker.getPosition();
        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f", position.latitude, position.longitude));
        Toast.makeText(getContext().getApplicationContext(), String.format("Drag from %f:%f", position.latitude, position.longitude), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position = marker.getPosition();
        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f", position.latitude, position.longitude));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position = marker.getPosition();
        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f", position.latitude, position.longitude));
        Geocoder geocoder=new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses=geocoder.getFromLocation(position.latitude,position.longitude,1);
            String address=addresses.get(0).getAddressLine(0);
            String city=addresses.get(0).getLocality();

           /*Toast.makeText(getActivity().getApplicationContext(), "Lat " + mMap.getMyLocation().getLatitude() + " " + "Long " + mMap.getMyLocation().getLongitude(),
                    Toast.LENGTH_LONG).show();*/
            System.out.println("yalla b2a " + mMap.getMyLocation().getLatitude());
            // tv2.setText("Latitude:" + latitude + ", Longitude:" + longitude);
            locat.setText("Lat " + mMap.getMyLocation().getLatitude() + " " + "Long " + mMap.getMyLocation().getLongitude());
            log =locat.getText().toString();

           // Toast.makeText(getActivity().getApplicationContext(),"Address :"+ address+ ""+city,Toast.LENGTH_LONG).show();
            //addmarker();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
