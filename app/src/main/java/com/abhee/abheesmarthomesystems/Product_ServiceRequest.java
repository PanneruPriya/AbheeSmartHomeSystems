package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Product_ServiceRequest extends Fragment implements LocationListener,AdapterView.OnItemSelectedListener {
    static String image, company, model, companyid;
    ImageView imgPrd;
    TextView companyT, modelT;
    Spinner spinner, times;
    String custid, userid, mobile;
    EditText addE, descE;
    Button submit;
    int position;
    int hold;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    ImageView iv_clinic;
    String selectedItemText;
    RadioGroup rbg;
    RadioButton rb;
    String rbCheck = "";
    byte img_store[] = null;
    Button btnBrowse;
    AlertDialog.Builder builder1;
    Bitmap bitmap;
    String id;
    public String loc;
    LocationManager locationManager;
    private static int PICK_IMAGE_REQUEST = 3;
    SharedPreferences sharedPreferences;
    String encodedImage = " ";
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    List<String> list;
    List<HashMap<String, String>> list1;

    public static Product_ServiceRequest newInstance(String imga, String companya, String modela, String companyids) {
        Product_ServiceRequest fragment = new Product_ServiceRequest();
        image = imga;
        company = companya;
        model = modela;
        companyid = companyids;
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
        v = inflater.inflate(R.layout.product_servicerequest, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        farname = (AppCompatTextView) view.findViewById(R.id.txtName);
        Product_ServiceRequest.farname.setText("Service Request" + "");
        getDataforServer();

        progressDialog = new ProgressDialog(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        custid = sharedPreferences.getString("custid", "");
        mobile = sharedPreferences.getString("mobilenumber", "");
        progressDialog.setTitle("GPS Location");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        Location();
        getLocation();
        list = new ArrayList<>();
        list1 = new ArrayList<HashMap<String, String>>();
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
        });*/
        builder1 = new AlertDialog.Builder(getActivity());
        rbg = (RadioGroup) v.findViewById(R.id.radiogroup);
        rbg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                position = group.indexOfChild(radioButton);
                //Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
                if (null != rb && checkedId > -1) {
                    rbCheck = rb.getText().toString();
                    if (rb.getText().toString().equals("Yes")) {

                    }
                    if (rb.getText().toString().equals("No")) {

                    }
                }
            }
        });
        iv_clinic = (ImageView) v.findViewById(R.id.iv_clinic);
        companyT = (TextView) v.findViewById(R.id.cmpnySR);
        modelT = (TextView) v.findViewById(R.id.modelSR);
        imgPrd = (ImageView) v.findViewById(R.id.imgSR);
        companyT.setText(company);
        modelT.setText(model);
        Picasso.with(getActivity()).load(Constants.IMG_URL + image).into(imgPrd);
        btnBrowse = (Button) v.findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_store = null;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        // spinner  = (Spinner)v.findViewById(R.id.spiSR);
       /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               hold = spinner.getSelectedItemPosition()+1 ;
              // String pos= String.valueOf(hold);
                //Toast.makeText(getActivity(), "Pos:-"+pos, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        final Spinner spinner = (Spinner) v.findViewById(R.id.typeC);
        times = (Spinner) v.findViewById(R.id.timetype);
        String[] plants = new String[]{
                "Select Type of Request...",
                "Repair",
                "Installation"
        };
        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
//                    Toast.makeText
//                            (getActivity().getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                    hold = spinner.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Set<String> uniqueList;
        uniqueList = new HashSet<String>(list);
        list.clear();
        list.add("Select Time");
        list.addAll(uniqueList);
        CustomSpinnerAdapter customSpinnerAdapterm = new CustomSpinnerAdapter(getActivity(), list);
        times.setAdapter(customSpinnerAdapterm);
        addE = (EditText) v.findViewById(R.id.addSR);
        descE = (EditText) v.findViewById(R.id.descSR);
        submit = (Button) v.findViewById(R.id.submitSR);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*progressDialog.setMessage("please wait...");
                progressDialog.show();*/
                final String name = addE.getText().toString();
                if (!isValidName(name)) {
                    addE.setError("Add address");
                }
                final String surname = descE.getText().toString();
                if (!isValidSurname(surname)) {
                    descE.setError("Add description");
                }
                /*if (isValidName(name)&& isValidSurname(surname)&&encodedImage!=null &&rb.isChecked() ){
                    //encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    Log.e("img", "img" + encodedImage);

                    sendDetailsToServer();
                }*/
                if (name != null && surname != null && encodedImage != null ) {
                    progressDialog.setTitle("In Progress");
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();
                    sendDetailsToServer(loc);
                }
                /*if (rbg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Select warranty or not", Toast.LENGTH_SHORT).show();
                }*/
                if (encodedImage == null) {
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
            }
        });
        //progressDialog.dismiss();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        Log.i("company", company);
        return v;
    }

    private void getDataforServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL + "getRequestTime", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("lllll", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("getRequestTimelist");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("requesttime", obj.getString("requesttime"));
                                hm.put("id", String.valueOf(obj.getInt("id")));
                                hm.put("requesttimeid", obj.getString("requesttimeid"));
                                hm.put("status", obj.getString("status"));
                                list1.add(hm);
                                list.add(hm.get("requesttime"));
                                Log.i("list22", "" + list1.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("list1", list1.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getDataforServer();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private boolean isValidName(String name1) {
        if (name1 != null && name1.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isValidSurname(String surname1) {
        if (surname1 != null && surname1.length() > 0) {
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            try {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, stream);
                    img_store = stream.toByteArray();
                    iv_clinic.setImageBitmap(bitmap);
                    encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void sendDetailsToServer(final String loc) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("message", descE.getText().toString());
        postParam.put("custaddress", addE.getText().toString());
        postParam.put("servicetypeid", String.valueOf(hold));
        postParam.put("id", userid);
        postParam.put("companyid", companyid);
        postParam.put("customerId", custid);
        postParam.put("requesttime", String.valueOf(times.getSelectedItemPosition()));
        postParam.put("catid", Products_SubCategories.cateid);
        postParam.put("modelid", Products_SubCategories.modelid);
        Log.i("modelId:-", "" + Products_SubCategories.modelid);
        postParam.put("warranty", " " );
        //postParam.put("mobilenumber",mobile);
        postParam.put("imgname", encodedImage);
        Log.i("PostData:---", "" + postParam.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "restSaveServiceRequest", new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    // Toast.makeText(getContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (json.getString("status").contains("Service Request Already Exists")) {
                        Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                        clearBox();
                    } else {
                        Toast.makeText(getContext(), "Request has been received", Toast.LENGTH_SHORT).show();
                        clearBox();
                        Intent intent = new Intent(getContext(), DashboardActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendDetailsToServer(loc);
                Toast.makeText(getContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void clearBox() {
        addE.setText("");
        descE.setText("");
        iv_clinic.setImageDrawable(null);

    }

    private void Location() {

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {


        Log.i("location ssss","Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        loc="Current Location: " + location.getLatitude() + ", " + location.getLongitude();
        //   locat.setVisibility(View.VISIBLE);
        try{
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                addE.setText(addresses.get(0).getAddressLine(0));
                // Toast.makeText(getActivity().getApplicationContext(),""+addresses.get(0).getAddressLine(0).toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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



    private class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private List<String> asr;

        public CustomSpinnerAdapter(Context context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getPosition(String str){return asr.indexOf(str);}
        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            AppCompatTextView txt = new AppCompatTextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            AppCompatTextView txt = new AppCompatTextView(getActivity());
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

    }
}
