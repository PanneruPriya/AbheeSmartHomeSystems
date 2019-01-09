package com.abhee.abheesmarthomesystems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends Fragment {
    EditText currentp,newp,confirmp;
    Button edit,save;
    SharedPreferences sharedPreferences;
    String mob,pass,id;
    FragmentTransaction transaction;
    LinearLayout l1,l2;
    int count=0;
    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();
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
        v = inflater.inflate(R.layout.changepassword, container, false);
        l1=(LinearLayout)v.findViewById(R.id.newpc);
        l2=(LinearLayout)v.findViewById(R.id.confirmPc);
        currentp=(EditText)v.findViewById(R.id.currentP);
        newp=(EditText)v.findViewById(R.id.newP);
        confirmp=(EditText)v.findViewById(R.id.confirmP);
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        mob =sharedPreferences.getString("mobilenumber","");
        pass=sharedPreferences.getString("password","");
        id=sharedPreferences.getString("userid","");
        //getDetailsFromServer();
        edit=(Button) v.findViewById(R.id.edit_pas);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(pass.equals(currentp.getText().toString())) {
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        currentp.setEnabled(false);
                        edit.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                        newp.setEnabled(true);
                        confirmp.setEnabled(true);
                    }
                    else {
                        Toast.makeText(getActivity(),"Incorrect Password",Toast.LENGTH_SHORT).show();
                    }
            }
        });
        save=(Button)v.findViewById(R.id.save_pas);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newp.getText().toString().equals(confirmp.getText().toString())) {
                    sendDetailstoServer();
                }else {
                    Toast.makeText(getActivity(),"New password not matched with the Confirm password ",Toast.LENGTH_SHORT).show();
                }
                //getDetailsFromServer();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }

    private void getDetailsFromServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("mobilenumber", mob);
            jsonBody.put("password", pass);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"logincredentials", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Oldpassword", " response" + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        currentp.setText(obj.getString("password"));
                        currentp.setEnabled(false);
                        newp.setEnabled(false);
                        confirmp.setEnabled(false);
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

    private void sendDetailstoServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("password", newp.getText().toString());
            final String mRequestBody = jsonBody.toString();
            Log.i("Responsechange",""+jsonBody.toString());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"restChangePassword", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        if (obj.getString("password").contains("Not Updated")) {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        } else if (obj.getString("password").contains("Updated")){
                            edit.setVisibility(View.VISIBLE);
                            save.setVisibility(View.GONE);
                            currentp.setEnabled(false);
                            newp.setEnabled(false);
                            confirmp.setEnabled(false);
                            Toast.makeText(getContext(), "Password has been changed", Toast.LENGTH_SHORT).show();
                            /*transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame, new ContentDashboard().newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();*/
                            startActivity(new Intent(getActivity(),LoginActivity.class));
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
                    Toast.makeText(getActivity(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }
}