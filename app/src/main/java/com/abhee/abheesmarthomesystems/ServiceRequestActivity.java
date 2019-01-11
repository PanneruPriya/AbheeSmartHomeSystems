package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.model.Dash;

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
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 09-07-2018.
 */

public class ServiceRequestActivity extends Fragment {
    Spinner categ, compa, proMod,times;
    List<String> list;
    EditText phone, address, description;
    RadioGroup rbg;
    RadioButton rb;
    String rbCheck = "";
    String selectedItemText;
    Button submit, cancel;
    String encodedImage = " ";
    HashMap<String, String> hm;
    Spinner spinner;
    Model1 item1;
    byte img_store[] = null;
    Button btnBrowse;
    ImageView iv_clinic;
    Bitmap bitmap;
    int hold, cate, model;
    private static int PICK_IMAGE_REQUEST = 3;
    ArrayList<HashMap<String, String>> commName;
    ArrayList<HashMap<String, String>> mCategoryLists;
    private ArrayList<Model1> productList;
    List<String> com;
    SharedPreferences sharedPreferences;
    String custid, userid, mobile;
    int position;
    String companyid=null;
    String modelIDs, catIDs;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;

    android.app.AlertDialog.Builder builder1;

    public static ServiceRequestActivity newInstance() {
        ServiceRequestActivity fragment = new ServiceRequestActivity();
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

        View v;
        v = inflater.inflate(R.layout.activity_servicerequest, container, false);
        progressDialog = new ProgressDialog(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        farname = (AppCompatTextView) view.findViewById(R.id.txtName);
        ServiceRequestActivity.farname.setText("Service Request" + "");
        progressDialog.setTitle("In Progress...");
        getDataforServer();
        list = new ArrayList<>();

        // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title = (AppCompatTextView) v.findViewById(R.id.title);
        // title.setText("Service Request");
        /*back_button.setOnClickListener(new View.OnClickListener() {
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
        userid = sharedPreferences.getString("userid", "");
        custid = sharedPreferences.getString("custid", "");
        mobile = sharedPreferences.getString("mobilenumber", "");
        categ = (Spinner) v.findViewById(R.id.categS);
        compa = (Spinner) v.findViewById(R.id.compS);
        proMod = (Spinner) v.findViewById(R.id.modeS);
        times=(Spinner)v.findViewById(R.id.times);
        iv_clinic = (ImageView) v.findViewById(R.id.iv_clinic);
        // spinner  = (Spinner)v.findViewById(R.id.spiSR);
        spinner = (Spinner) v.findViewById(R.id.spiSR);
        String[] plants = new String[]{
                "Select Type of Request ",
                "REPAIR",
                "INSTALLATION"
        };
        Set<String> uniqueList;
        uniqueList= new HashSet<String>(list);
        list.clear();
        list.add("Select Time");
        list.addAll(uniqueList);
        CustomSpinnerAdapter customSpinnerAdapterm = new CustomSpinnerAdapter(getActivity(), list);
        times.setAdapter(customSpinnerAdapterm);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
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
                /*TextView txt = new TextView(getActivity());*/
                tv.setPadding(16, 16, 16, 16);
                tv.setTextSize(14);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTextColor(Color.parseColor("#000000"));
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
                    // Toast.makeText(getActivity().getApplicationContext(),""+ hold, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate = categ.getSelectedItemPosition() + 1;
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
                model = proMod.getSelectedItemPosition() + 1;
                // String pos= String.valueOf(hold);
                //Toast.makeText(getActivity(), "Pos:-"+pos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        List<String> categories = new ArrayList<String>();
//        categories.add("Repair");
//        categories.add("Installation");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);

        productList = new ArrayList<Model1>();
        rbg = (RadioGroup) v.findViewById(R.id.radiogroup);

        rbg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                position = group.indexOfChild(radioButton);
                // Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
                if (null != rb && checkedId > -1) {
                    rbCheck = rb.getText().toString();
                    if (rb.getText().toString().equals("Yes")) {

                    }
                    if (rb.getText().toString().equals("No")) {

                    }
                }
            }
        });
        builder1 = new android.app.AlertDialog.Builder(getActivity());
        phone = (EditText) v.findViewById(R.id.phoneS);
        address = (EditText) v.findViewById(R.id.addrS);
        description = (EditText) v.findViewById(R.id.descrS);
        submit = (Button) v.findViewById(R.id.submitS);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name=phone.getText().toString();
                if (!isValidPhone(name)){
                    phone.setError("Plaese Enter the Mobile Number");
                }
                final String surname=address.getText().toString();
                if (!isValidAddress(surname)){
                    address.setError("Plaese Enter the Address");
                }
                if(description.getText().toString().equals(null)&&description.getText().length()>0){
                    description.setText("  ");
                }
                if(times.getSelectedItem().equals("Select Time")){
                    Toast.makeText(getActivity(),"Plaese Select the Service Time",Toast.LENGTH_SHORT).show();
                }
                if(spinner.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(),"Plaese Select the Requset Type",Toast.LENGTH_SHORT).show();
                }


                if(encodedImage == null){
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
                    android.app.AlertDialog alert = builder1.create();
                    //alert.setTitle("AlertDialogExample");
                    alert.show();
                }
                if (encodedImage != null) {
                    //encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    Log.e("img", "img" + encodedImage);
                    for (int i=0;i<commName.size();i++){
                        if(commName.get(i).get("company").equals(compa.getSelectedItem())){
                            companyid =commName.get(i).get("companyid");
                            //progressDialog.setTitle("In Progress...");

                            if((!phone.getText().toString().equals(null)&&phone.getText().length()!=0)
                                    &&(!address.getText().toString().equals(null)&&address.getText().length()!=0)
                                    //&&(!description.getText().toString().equals(null)&&description.getText().length()!=0)
                                    &&(!times.getSelectedItem().equals("Select Time"))
                                    &&(spinner.getSelectedItemPosition()!=0)){
                                //Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
                                sendDetailsToServer();
                            }
                            //sendDetailsToServer();
                            //Toast.makeText(getActivity(),commName.get(i).get("companyid"),Toast.LENGTH_SHORT).show();
                        }
                    }
                    //
                }
            }
        });

        cancel = (Button) v.findViewById(R.id.cancelS);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DashboardActivity.class);
                startActivity(i);
            }
        });
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
        getDetailsFromServer();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }
    private boolean isValidPhone(String surname1) {
        if (surname1!=null&&surname1.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidAddress(String surname1) {
        if (surname1!=null&&surname1.length()>0){
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

    private void getDetailsFromServer() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL + "getproductdetails", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Spinner:", " response:---" + response);
                    progressDialog.dismiss();
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response.toString());
                        JSONArray array = json.getJSONArray("productslist");
                        List<String> marketName = new ArrayList<String>();
                        // marketName.add(0,"Select Category");

                        commName = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < array.length(); i++) {

                            final JSONObject obj = array.getJSONObject(i);
                            marketName.add(obj.getString("category"));
                            hm = new HashMap<String, String>();
                            hm.put("products", obj.getString("category"));
                            hm.put("company", obj.getString("companyname"));
                            hm.put("model", obj.getString("productmodelname"));
                            hm.put("id", obj.getString("id"));
                            hm.put("companyid",obj.getString("companyid"));
                            hm.put("categoryid", obj.getString("categoryid"));

                            commName.add(hm);

                            Set<String> uniqueList;
                            uniqueList = new HashSet<String>(marketName);
                            marketName.clear();
                            marketName.addAll(uniqueList);


                            item1 = new Model1(obj.getString("category"), obj.getString("companyname"), obj.getString("productmodelname"), obj.getString("id"));
                            productList.add(item1);

                            final CustomSpinnerAdapter1 customSpinnerAdapter = new CustomSpinnerAdapter1
                                    (ServiceRequestActivity.this, marketName);
                            categ.setAdapter(customSpinnerAdapter);

                           /* final int sp_position = customSpinnerAdapter.getPosition("Select Category");
                            categ.setSelection(sp_position);
*/
                            categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    compa.setSelection(position);
                                    // int j=1;
                                    com = new ArrayList<String>();
                                    // com.add(0,"Select Company");
                                    for (int i = 0; i < commName.size(); i++) {


                                        hm = commName.get(i);
                                        if (hm.get("products").equals(categ.getSelectedItem().toString())) {
                                            // com.add(j, hm.get("company"));
                                            com.add(hm.get("company"));
                                            HashSet<String> hashSet = new HashSet<String>();
                                            hashSet.addAll(com);
                                            com.clear();
                                            com.addAll(hashSet);
                                            catIDs = hm.get("categoryid");
                                            // j++;
                                        }
                                    }
                                    CustomSpinnerAdapter2 customSpinnerAdapter1 = new CustomSpinnerAdapter2
                                            (ServiceRequestActivity.this, com);
                                    compa.setAdapter(customSpinnerAdapter1);
                                    /*final int sp_position = customSpinnerAdapter.getPosition("Select Company");
                                    compa.setSelection(sp_position);
*/
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            compa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    proMod.setSelection(position);
                                    int j = 0;
                                    com = new ArrayList<String>();
                                    // com.add(0,"Select Model");
                                    for (int i = 0; i < commName.size(); i++) {

                                        hm = commName.get(i);
                                        if (hm.get("company").equals(compa.getSelectedItem().toString())) {

                                            com.add(j, hm.get("model"));
                                            modelIDs = hm.get("id");
                                            j++;
                                        }
                                    }
                                    CustomSpinnerAdapter3 customSpinnerAdapter3 = new CustomSpinnerAdapter3
                                            (ServiceRequestActivity.this, com);
                                    proMod.setAdapter(customSpinnerAdapter3);
                                   /* final int sp_position = customSpinnerAdapter.getPosition("Select Model");
                                    proMod.setSelection(sp_position);
*/

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
                    //chik();
                    getDetailsFromServer();
                   // Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }

    private void sendDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("message", description.getText().toString());
        postParam.put("custaddress", address.getText().toString());
        postParam.put("servicetypeid", String.valueOf(hold));
        postParam.put("id", userid);
        postParam.put("customerId", custid);
        postParam.put("catid", catIDs);
        Log.i("catIDs", "" + catIDs);
        postParam.put("modelid", modelIDs);
        //  Log.i("modelId:-",""+modelIDs);
        postParam.put("warranty","  ");
        postParam.put("companyid", companyid);
        postParam.put("requesttime",String.valueOf(times.getSelectedItemPosition()));
        postParam.put("imgname", encodedImage);

        Log.i("PastData1:--", "" + postParam.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "restSaveServiceRequest", new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    if (json.getString("status").contains("Service Request Already Exists")) {
                        Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
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
                //chik();
                sendDetailsToServer();
               // Toast.makeText(getContext(), "Error:---" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        phone.setText("");
        address.setText("");
        description.setText("");
        iv_clinic.setImageDrawable(null);
    }

    private class CustomSpinnerAdapter1 extends BaseAdapter implements SpinnerAdapter {

        private final ServiceRequestActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter1(ServiceRequestActivity context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }

        public int getPosition(String str) {
            return asr.indexOf(str);
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

        private final ServiceRequestActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter2(ServiceRequestActivity context, List<String> asr) {
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

    private class CustomSpinnerAdapter3 extends BaseAdapter implements SpinnerAdapter {

        private final ServiceRequestActivity activity;
        private List<String> asr;

        public CustomSpinnerAdapter3(ServiceRequestActivity context, List<String> asr) {
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
                sendDetailsToServer();
                dialog.hide();
            }
        });

    }

    private void getDataforServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"getRequestTime", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("lllll",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("getRequestTimelist");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                list.add(obj.getString("requesttime"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
