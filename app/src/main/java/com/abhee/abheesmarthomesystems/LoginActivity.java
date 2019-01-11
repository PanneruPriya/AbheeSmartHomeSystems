package com.abhee.abheesmarthomesystems;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhee.abheesmarthomesystems.dialogbox.dialogbox;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText user,pswd;
    TextView forgtPswd;
    Button login;
    TextView regisHere;
    AlertDialog dialog;
    Config config = new Config();
    SharedPreferences sharedPreferences;
    AppCompatEditText mobileDiaP;
    SharedPreferences.Editor prefsEditor;
    ProgressDialog progressDialog;
    dialogbox dialogbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=(EditText)findViewById(R.id.username);
        pswd=(EditText)findViewById(R.id.passwordL);
        forgtPswd=(TextView)findViewById(R.id.forgotPswd);
        login=(Button)findViewById(R.id.btn_signIn);
        dialogbox = new dialogbox();
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("In Progress...");

        sharedPreferences = this.getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        String mob=sharedPreferences.getString("mobilenumber",null);
        String pass=sharedPreferences.getString("password",null);
        if(mob !=null && pass !=null){
           // startActivity(new Intent(LoginActivity.this,PinEnterActivity.class));
        }else{
          //  Toast.makeText(getApplicationContext(),"User not Register ",Toast.LENGTH_SHORT).show();
        }
        regisHere=(TextView)findViewById(R.id.registerHere);
        regisHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = user.getText().toString();
                if (!isUName(uname)) {
                    user.setError("Name should not be empty.");
                    return;
                }
                String pwd = pswd.getText().toString();
                if (!isPWD(pwd)) {
                    pswd.setError("Password should not be empty.");
                    return;
                }
                if (uname != null && pwd != null) {
                    sendDetailsToServer();
                   // dialogbox.SuccessMessage(LoginActivity.this,"Loging Success");
                }
            }
        });

        forgtPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater1 = getLayoutInflater();
                View alertLayout = inflater1.inflate(R.layout.custom_forgot, null);
                final ImageView image=(ImageView)alertLayout.findViewById(R.id.close_dialog1);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                mobileDiaP=(AppCompatEditText) alertLayout.findViewById(R.id.editT);
                final AppCompatButton btnSubmit = (AppCompatButton) alertLayout.findViewById(R.id.btn_submit);
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setView(alertLayout);
                alert.setCancelable(false);
                dialog = alert.create();
                dialog.getWindow().setLayout(200, 300);
                dialog.show();
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = mobileDiaP.getText().toString();
                        if (!isUName(phone)) {
                            mobileDiaP.setError("Invalid Number");
                            return;
                        }
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.show();

                        sendDetailsToServerPin();
                    }
                });
            }
        });
    }

    private boolean isUName(String uname) {
        if (uname != null && uname.length() > 0) {
            return true;
        }
        return false;
    }
    private boolean isPWD(String pwd) {
        if (pwd != null && pwd.length() > 0) {
            return true;
        }
        return false;
    }

    private void sendDetailsToServer() {
        dialogbox.prograssBoxShow(LoginActivity.this);
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", user.getText().toString());
            jsonBody.put("password", pswd.getText().toString());
            final String mRequestBody = jsonBody.toString();
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"logincredentials",new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    Log.e("Log1"," response"+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if(jsonObject.getString("status").equals("success")){
                            JSONArray  jsonArray = jsonObject.getJSONArray("userbean");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject json=jsonArray.getJSONObject(i);
                                prefsEditor = sharedPreferences.edit();
                                prefsEditor.putString("mobilenumber",json.getString("mobilenumber"));
                                prefsEditor.putString("fname",json.getString("firstname"));
                                prefsEditor.putString("lname",json.getString("lastname"));
                                prefsEditor.putString("userid",json.getString("id"));
                                prefsEditor.putString("custid",json.getString("customerId"));
                                prefsEditor.putString("password",json.getString("password"));
                                prefsEditor.commit();
                                dialogbox.prograssBoxClose();
                                Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),jsonObject.getString("status").toString(),Toast.LENGTH_SHORT).show();
                            dialogbox.prograssBoxClose();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*try{
                        JSONObject json=new JSONObject(response.toString());
                        prefsEditor = sharedPreferences.edit();
                        prefsEditor.putString("mobilenumber",json.getString("mobilenumber"));
                        prefsEditor.putString("fname",json.getString("firstname"));
                        prefsEditor.putString("lname",json.getString("lastname"));
                        prefsEditor.putString("userid",json.getString("id"));
                        prefsEditor.putString("custid",json.getString("customerId"));
                        prefsEditor.putString("password",json.getString("password"));
                        prefsEditor.commit();
                        Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }catch(JSONException e){
                        Toast.makeText(LoginActivity.this, "Mobile Number/Email doesn’t not exists", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogbox.prograssBoxClose();
                    dialogbox.ErrorMessage(LoginActivity.this,"Server Is Slow");
                   // Toast.makeText(LoginActivity.this, "Data is not getting", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDetailsToServerPin() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("mobilenumber", mobileDiaP.getText().toString().trim());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"restForgotPassword",
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.i("ResponsePin:-",response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "check the password", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "We are Unable to Provide Data", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        queue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
