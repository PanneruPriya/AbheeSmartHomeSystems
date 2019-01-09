package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class ProfileActivity1 extends Fragment {
    EditText first, last,address;
    Button editP, saveP;
    TextView chngpswd;
    SharedPreferences sharedPreferences;
    String mob,pass,id;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FragmentTransaction transaction;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    public static ProfileActivity1 newInstance() {
        ProfileActivity1 fragment = new ProfileActivity1();
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
        v = inflater.inflate(R.layout.activity_profile1, container, false);
        progressDialog=new ProgressDialog(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        ProfileActivity1.farname.setText("profile"+"");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        first = (EditText) v.findViewById(R.id.text1);
        last = (EditText) v.findViewById(R.id.text2);
        address=(EditText)v.findViewById(R.id.text5);

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
                address.setEnabled(true);
            }
        });
        saveP = (Button) v.findViewById(R.id.save_per);
        saveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendDetailstoServer();
                editP.setVisibility(View.VISIBLE);
                saveP.setVisibility(View.GONE);
                first.setEnabled(false);
                last.setEnabled(false);
                address.setEnabled(false);
                final String name=first.getText().toString();
                if (!isValidName(name)){
                    first.setError("Invalid name");
                }
                final String surname=last.getText().toString();
                if (!isValidSurname(surname)){
                    last.setError("Invalid surname");
                }
                final String addres=address.getText().toString();
                if (!isValidAddress(addres)){
                    address.setError("Invalid Email");
                }
                if ((!first.getText().equals(null)&&first.getText().length()>0)
                        &&(!last.getText().equals(null)&&last.getText().length()>0)
                        &&(!address.getText().equals(null)&&address.getText().length()>0)){
                    sendDetailstoServer();
                }else {
                    Toast.makeText(getActivity(),"please check your profile",Toast.LENGTH_SHORT).show();
                }
                getDetailsFromServer();
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

                                address.setText(obj.getString("address"));
                                address.setEnabled(false);

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
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("firstname", first.getText().toString());
            jsonBody.put("lastname", last.getText().toString());
            jsonBody.put("address", address.getText().toString());

            final String mRequestBody = jsonBody.toString();
            Log.i("Changed:-- ",""+jsonBody.toString());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"restEditProfileInfo", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        if (obj.getString("profileinfo").contains("Not Updated")) {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        } else if (obj.getString("profileinfo").contains("Updated")){
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            getDetailsFromServer();
                            Intent i=new Intent(getContext(),DashboardActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                    }
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
}