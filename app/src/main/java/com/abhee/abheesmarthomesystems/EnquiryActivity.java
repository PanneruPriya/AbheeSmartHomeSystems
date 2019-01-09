package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Dash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 09-07-2018.
 */

public class EnquiryActivity extends Fragment {
    SharedPreferences sharedPreferences;
    String custid,userid,mobile;
    Spinner categ,compa,proMod;
    int hold,cate,model;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ArrayList<HashMap<String, String>> commName;
    ArrayList<HashMap<String, String>> mCategoryLists;
    private ArrayList<Model1> productList;
    List<String> com,iD;
    Button sendEnquiry;
    ImageView iv_clinic;
    String selectedItemText;
    RadioGroup rbg;
    RadioButton rb;
    private static int PICK_IMAGE_REQUEST=3;
    String rbCheck="";
    byte img_store[]=null;
    Button btnBrowse;
    EditText name,email,phone,description;
    HashMap<String,String>  hm;
    Model1 item1;
    String resu;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    Bitmap bitmap;
    String encodedImage=null;
    android.app.AlertDialog.Builder builder1;
    public static EnquiryActivity newInstance() {
        EnquiryActivity fragment = new EnquiryActivity();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.activity_enquiry, container, false);
        progressDialog=new ProgressDialog(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        EnquiryActivity.farname.setText("Enquiry"+"");
        progressDialog.setTitle("In Progress...");
        builder1 = new android.app.AlertDialog.Builder(getActivity());
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title=(AppCompatTextView)v.findViewById(R.id.title);
       // title.setText("Enquiry");
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
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid", "");
        custid=sharedPreferences.getString("custid", "");
        mobile=sharedPreferences.getString("mobilenumber","");
        categ=(Spinner) v.findViewById(R.id.categS);
        compa=(Spinner) v.findViewById(R.id.compS);
        proMod=(Spinner)v.findViewById(R.id.modeS);
        name=(EditText)v.findViewById(R.id.name);
        email=(EditText)v.findViewById(R.id.email);
        phone=(EditText)v.findViewById(R.id.phone);
        description=(EditText)v.findViewById(R.id.descr);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate = categ.getSelectedItemPosition()+1 ;
                // String pos= String.valueOf(hold);
                //Toast.makeText(getActivity(), "Pos:-"+pos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        proMod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = proMod.getSelectedItemPosition()+1 ;
                // String pos= String.valueOf(hold);
                //Toast.makeText(getActivity(), "Pos:-"+pos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sendEnquiry=(Button)v.findViewById(R.id.btn_Enquiry);
        sendEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1=email.getText().toString();
                final String names=name.getText().toString();
                final String phones = phone.getText().toString();
                if (!isValidEmail(email1)){
                    email.setError("Invalid Email");
                }
                if(!isValidName(names)){
                    name.setError("plaese Enter Name");
                }
                if(!isValidPhone(phones)){
                    phone.setError("plaese Enter Phone Number");
                }
                if(encodedImage==null){
                    builder1.setMessage("Please select image")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    android.app.AlertDialog alert = builder1.create();
                    alert.show();
                }
                if((email.getText().toString()!=null && email.getText().toString().matches(emailPattern))
                        &&(name.getText().toString()!=null && name.getText().toString().length()>0)
                        &&(phone.getText().toString()!=null && phone.getText().toString().length()>0)&&
                        encodedImage!=null) {
                    //encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    //Log.e("img", "img" + encodedImage);
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();
                    sendDetailsToServer();
                }
            }

        });
        iv_clinic=(ImageView)v.findViewById(R.id.iv_clinic);
        btnBrowse=(Button) v.findViewById(R.id.btnBrowse);
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

        getDetailsFromServer();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            try {
                Uri uri = data.getData();
                if(bitmap!= null){
                    bitmap.recycle();
                    bitmap=null;
                }
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
    private boolean isValidEmail(String email) {
        if (email!=null && email.matches(emailPattern)){
            return true;
        }
        return false;
    }
    private boolean isValidName(String name) {
        if (name!=null && name.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidPhone(String phone) {
        if (phone!=null && phone.length()>0){
            return true;
        }
        return false;
    }

    private void sendDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("modelnumber", ""+proMod.getSelectedItem().toString());
        postParam.put("customername",name.getText().toString());
        postParam.put("email",email.getText().toString());
        postParam.put("mobileno",phone.getText().toString());
        postParam.put("reqdesc", description.getText().toString());
        postParam.put("requestType","Enquiry");
        postParam.put("imgfiles",encodedImage);

        Log.i("PastData1:--",""+postParam.toString());


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"saveEnquiryDetails",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    if (json.getString("status").contains("Enquiry details sent successfully")) {
                        Toast.makeText(getContext(), "We will get back you soon", Toast.LENGTH_SHORT).show();
                        clearBox();
                        Intent i=new Intent(getContext(), DashboardActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Server not Responding" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void clearBox() {
        name.setText("");
        email.setText("");
        phone.setText("");
        description.setText("");
    }

    private void getDetailsFromServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL +"getproductdetails", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Spinner:"," response:---"+response);
                    progressDialog.dismiss();
                    JSONObject json= null;
                    try {
                        json = new JSONObject(response.toString());
                        JSONArray array=json.getJSONArray("productslist");
                        List<String> marketName = new ArrayList<String>();
                        commName=new ArrayList<HashMap<String, String>>();
                        productList = new ArrayList<Model1>();
                        for(int i=0; i<array.length(); i++) {

                            final JSONObject obj = array.getJSONObject(i);
                            marketName.add(obj.getString("category"));

                            hm = new HashMap<String, String>();
                            hm.put("products", obj.getString("category"));
                            hm.put("company",obj.getString("companyname"));
                            hm.put("model",obj.getString("productmodelname"));
                            hm.put("id",obj.getString("id"));

                            commName.add(hm);

                            Set<String> uniqueList;
                            uniqueList = new HashSet<String>(marketName);
                            marketName.clear();
                            marketName.addAll(uniqueList);

                            item1 = new Model1(obj.getString("category"), obj.getString("companyname"),obj.getString("productmodelname"),obj.getString("id"));
                            productList.add(item1);

                            CustomSpinnerAdapter1 customSpinnerAdapter = new CustomSpinnerAdapter1
                                    (EnquiryActivity.this, marketName);
                            categ.setAdapter(customSpinnerAdapter);

                            categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    compa.setSelection(position);
                                   // int j=0;
                                    com=new ArrayList<String>();

                                    for (int i=0;i<commName.size();i++){

                                        hm=commName.get(i);
                                        if(hm.get("products").equals(categ.getSelectedItem().toString()))
                                        {
                                           // com.add(j, hm.get("company"));
                                          //  j++;
                                            com.add(hm.get("company"));
                                            HashSet<String> hashSet = new HashSet<String>();
                                            hashSet.addAll(com);
                                            com.clear();
                                            com.addAll(hashSet);

                                        }
                                    }
                                    CustomSpinnerAdapter2 customSpinnerAdapter1 = new CustomSpinnerAdapter2
                                            (EnquiryActivity.this, com);
                                    compa.setAdapter(customSpinnerAdapter1);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            compa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    proMod.setSelection(position);
                                    int j=0;
                                    com=new ArrayList<String>();

                                    for (int i=0;i<commName.size();i++){

                                        hm=commName.get(i);
                                        if(hm.get("company").equals(compa.getSelectedItem().toString()))
                                        {
                                            com.add(j, hm.get("model"));
                                            j++;
                                        }

                                    }
                                    CustomSpinnerAdapter3 customSpinnerAdapter3 = new CustomSpinnerAdapter3
                                            (EnquiryActivity.this, com);
                                    proMod.setAdapter(customSpinnerAdapter3);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            proMod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    int j=0;
                                    com=new ArrayList<String>();
                                    iD=new ArrayList<String>();

                                    for (int i=0;i<commName.size();i++){

                                        hm=commName.get(i);
                                        if(hm.get("model").equals(proMod.getSelectedItem().toString()))
                                        {
                                            iD.add(j,hm.get("id"));
                                            resu =hm.get("id");

                                            j++;
                                        }

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    chik();
                   // Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }

    private class CustomSpinnerAdapter3 extends BaseAdapter implements SpinnerAdapter {

        private final EnquiryActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter3(EnquiryActivity context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }

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
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }
    private class CustomSpinnerAdapter2 extends BaseAdapter implements SpinnerAdapter {

        private final EnquiryActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter2(EnquiryActivity context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }

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
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }
    private class CustomSpinnerAdapter1 extends BaseAdapter implements SpinnerAdapter {

        private final EnquiryActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter1(EnquiryActivity context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }
        public int getPosition(String str){return asr.indexOf(str);}

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }
    public void chik() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertlayout1, null);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.rbutton);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Reload");
       // alert.setIcon(R.drawable.ic_launcher);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailsFromServer();
                dialog.hide();
            }
        });
    }
    }

