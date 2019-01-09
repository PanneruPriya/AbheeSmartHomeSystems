package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends Fragment {
    EditText first, last, mobile, email,address,cid;
    Button editP, saveP;
    TextView chngpswd;
    SharedPreferences sharedPreferences;
    String mob,pass,id;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FragmentTransaction transaction;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    public static ProfileActivity newInstance() {
        ProfileActivity fragment = new ProfileActivity();
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
        v = inflater.inflate(R.layout.activity_profile, container, false);
        progressDialog=new ProgressDialog(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        ProfileActivity.farname.setText("Profile"+"");


        first = (EditText) v.findViewById(R.id.text1);
        last = (EditText) v.findViewById(R.id.text2);
        mobile = (EditText) v.findViewById(R.id.text3);
        email = (EditText) v.findViewById(R.id.text4);
        address=(EditText)v.findViewById(R.id.text5);
        cid=(EditText)v.findViewById(R.id.textid);
        first.setEnabled(false);
        last.setEnabled(false);
        mobile.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        cid.setEnabled(false);
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        mob =sharedPreferences.getString("mobilenumber","");
        pass=sharedPreferences.getString("password","");
        id=sharedPreferences.getString("userid","");

        getDetailsFromServer();

        editP = (Button) v.findViewById(R.id.edit_per);
        editP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editP.setVisibility(View.GONE);
                saveP.setVisibility(View.VISIBLE);
                first.setEnabled(true);
                last.setEnabled(true);
                mobile.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
            }
        });
        saveP = (Button) v.findViewById(R.id.save_per);
        saveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendDetailstoServer();



                final String name=first.getText().toString();
                if (!isValidName(name)){
                    first.setError("Invalid name");
                }
                final String surname=last.getText().toString();
                if (!isValidSurname(surname)){
                    last.setError("Invalid surname");
                }
                final String num=mobile.getText().toString();
                if (!isValidNumber(num)){
                    mobile.setError("Invalid Number");
                }
                final String email1=email.getText().toString();
                if (!isValidEmail(email1)){
                    email.setError("Invalid Email");
                }
                final String addres=address.getText().toString();
                if (!isValidAddress(addres)){
                    address.setError("Invalid Email");
                }
                if ((!first.getText().equals(null)&&first.getText().length()>0)
                        &&(!last.getText().equals(null)&&last.getText().length()>0)
                        &&(!address.getText().equals(null)&&address.getText().length()>0)
                        &&(!mobile.getText().equals(null)&&mobile.getText().length()>0&&mobile.getText().length()==10)
                        &&(!email.getText().equals(null)&&email.getText().length()>0)){
                    first.setEnabled(false);
                    last.setEnabled(false);
                    mobile.setEnabled(false);
                    email.setEnabled(false);
                    address.setEnabled(false);
                    sendDetailstoServer();
                }else {
                    Toast.makeText(getActivity(),"please check your profile",Toast.LENGTH_SHORT).show();
                }
                //getDetailsFromServer();
            }
        });

        chngpswd = (TextView) v.findViewById(R.id.chngpswd);
        chngpswd.setPaintFlags(chngpswd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chngpswd.setText("Change Password!");
        chngpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ChangePassword().newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }
    private boolean isValidAddress(String addres) {
        if (!addres.equals(null)&& addres.length()>0){
            return true;
        }else
        return false;
    }
    private boolean isValidEmail(String email) {
        if (!email.equals(null) && email.matches(emailPattern)){
            return true;
        }else
        return false;
    }
    private boolean isValidName(String name1) {
        if (name1!=null&& name1.length()>0){
            return true;
        }else
        return false;
    }
    private boolean isValidSurname(String surname1) {
        if (surname1!=null&&surname1.length()>0){
            return true;
        }else
        return false;
    }
    private boolean isValidNumber(String num) {
        if (num!=null&&num.length()==10){
            return true;
        }else
        return false;
    }
    private void getDetailsFromServer() {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", mob);
            jsonBody.put("password", pass);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"logincredentials", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray  jsonArray = jsonObject.getJSONArray("userbean");
                        progressDialog.dismiss();
                        if(jsonObject.getString("status").equals("success")){
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject obj=jsonArray.getJSONObject(i);
                                first.setText(obj.getString("firstname"));
                                first.setEnabled(false);

                                last.setText(obj.getString("lastname"));
                                last.setEnabled(false);

                                mobile.setText(obj.getString("mobilenumber"));
                                mobile.setEnabled(false);

                                email.setText(obj.getString("email"));
                                email.setEnabled(false);

                                address.setText(obj.getString("address"));
                                address.setEnabled(false);
                                cid.setText(obj.getString("customerId"));
                                cid.setEnabled(false);

                            }
                        }else{
                            Toast.makeText(getActivity(),"Not found",Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*try {
                        JSONObject obj = new JSONObject(response.toString());

                        first.setText(obj.getString("firstname"));
                        first.setEnabled(false);

                        last.setText(obj.getString("lastname"));
                        last.setEnabled(false);

                        mobile.setText(obj.getString("mobilenumber"));
                        mobile.setEnabled(false);

                        email.setText(obj.getString("email"));
                        email.setEnabled(false);

                        address.setText(obj.getString("address"));
                        address.setEnabled(false);

                    } catch (JSONException e) {
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }

    private void sendDetailstoServer() {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("firstname", first.getText().toString());
            jsonBody.put("lastname", last.getText().toString());
            jsonBody.put("mobilenumber", mobile.getText().toString());
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("address", address.getText().toString());

            final String mRequestBody = jsonBody.toString();
            Log.i("Changed:-- ",""+jsonBody.toString());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"restEditProfileInfo", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    editP.setVisibility(View.VISIBLE);
                    saveP.setVisibility(View.GONE);
                    Log.e("Log1", " response" + response.toString());
                    try {
                        final JSONObject obj = new JSONObject(response.toString());
                        if(obj.getString("status").contains("OK")){
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
                            final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                            // userInput.setText(code);
                            final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);
                            final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancel);
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Enter OTP:");
                            alert.setIcon(R.drawable.ic_launcher);
                            alert.setView(alertLayout);
                            alert.setCancelable(false);
                            final AlertDialog dialog = alert.create();
                            dialog.show();
                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        if (obj.getString("otp").toString()!=null &&userInput.getText().toString().length()>0  &&
                                                obj.getString("otp").toString().contains(userInput.getText().toString())){
                                            Toast.makeText(getContext().getApplicationContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
                                            Verified();
                                            dialog.hide();
                                        }
                                        else {
                                            Toast.makeText(getActivity().getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                                            userInput.setText("");
                                            dialog.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            });
                        } if(obj.getString("status").contains("Not Updated")){

                        } if(obj.getString("status").contains("Failed")){

                        }
                        /*if (obj.getString("profileinfo").contains("Not Updated")) {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        } else if (obj.getString("profileinfo").contains("Updated")){
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            getDetailsFromServer();
                            Intent i=new Intent(getContext(),DashboardActivity.class);
                            startActivity(i);
                        }*/
                        /*else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }*/
                    } catch (JSONException e) {
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }
    public void otp(final String otp){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
        final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
        // userInput.setText(code);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancel);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Enter OTP:");
        alert.setIcon(R.drawable.ic_launcher);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp!=null &&userInput.getText().toString().length()>0  &&
                        otp.toString().contains(userInput.getText().toString())){
                    Toast.makeText(getContext().getApplicationContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
                    Verified();
                    dialog.hide();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                    userInput.setText("");
                    dialog.show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    private void Verified() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("firstname", first.getText().toString());
            jsonBody.put("lastname", last.getText().toString());
            jsonBody.put("mobilenumber", mobile.getText().toString());
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("address", address.getText().toString());

            jsonBody.put("otpstatus", "OTP_Verified");
            final String mRequestBody = jsonBody.toString();
            Log.i("Changed:-- ",""+jsonBody.toString());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"otpVerification", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    //getDetailsFromServer();
                    try{
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if(jsonObject.getString("status").contains("Updated"))
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Profile data updated successfully", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getContext(),LoginActivity.class);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server11", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }
}