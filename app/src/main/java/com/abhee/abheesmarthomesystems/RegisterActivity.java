package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
   public static EditText frstName,lstName,mobi,email,pswd,repswd,address;
    Button register;
    TextView loginHere;
    SmsVerifyCatcher smsVerifyCatcher;
    String code;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Context mContext=this;
    AlertDialog alertDialog;
    AlertDialog dialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("In Progress...");
        frstName=(EditText)findViewById(R.id.firstname);
        lstName=(EditText)findViewById(R.id.lastname);
        mobi=(EditText)findViewById(R.id.mobile);
        email=(EditText)findViewById(R.id.email);
        pswd=(EditText)findViewById(R.id.passwordR1);
        repswd=(EditText)findViewById(R.id.passwordR2);
        address=(EditText)findViewById(R.id.address);
        register=(Button)findViewById(R.id.btn_signUp);
        loginHere=(TextView)findViewById(R.id.loginHere);
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=frstName.getText().toString();
                if (!isValidName(name)){
                    frstName.setError("Invalid name");
                }
                final String surname=lstName.getText().toString();
                if (!isValidSurname(surname)){
                    lstName.setError("Invalid surname");
                }
                final String addresss=address.getText().toString();
                if (!isValidSurname(addresss)){
                    address.setError("Invalid Address");
                }
                final String num=mobi.getText().toString();
                if (!isValidNumber(num)){
                    mobi.setError("Invalid Number");
                }

                final String email1=email.getText().toString();
                if (!isValidEmail(email1)){
                    email.setError("Invalid Email");
                }
                final String pswd1=pswd.getText().toString();
                if (!isValidPassword(pswd1)){
                    pswd.setError("Invalid Password");
                }
                final String repswd1=repswd.getText().toString();
                if (!isValidPassword1(repswd1)){
                    repswd.setError("Invalid Confirmation");
                }
                if ((!frstName.getText().toString().equals(null)&&(frstName.getText().length()!=0))&&
                        (!lstName.getText().toString().equals(null)&&(lstName.getText().length()!=0))&&
                        (!mobi.getText().toString().equals(null)&&(mobi.getText().length()!=0)&&(mobi.getText().length()==10))&&
                        (!email.getText().toString().equals(null)&&(email.getText().length()!=0))&&
                        (!pswd.getText().toString().equals(null)&&(email.getText().length()!=0))&&
                        (!repswd.getText().toString().equals(null)&&(repswd.getText().length()!=0))
                        &&(!address.getText().toString().equals(null)&&(address.getText().length()!=0))){
                    //sendDetailsDuplicateMob();
                    //sendDetailsDuplicateEmail();
                    if(repswd.getText().toString().equals(pswd.getText().toString())) {
                        progressDialog.setMessage("please wait...");
                        progressDialog.show();
                        sendDetailsToServer();
                    }else{
                        pswd.setError("Password not Matched");
                        repswd.setError("Password not Matched");
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Invalid  Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getApplicationContext(), LoginActivity.class));
    }

    private void sendDetailsToServerUser() {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        String firstname= removeSpace(frstName.getText().toString());
        String lastname = removeSpace(lstName.getText().toString());
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("firstname", firstname );
        postParam.put("lastname",lastname);
        postParam.put("email",email.getText().toString());
        postParam.put("mobilenumber",mobi.getText().toString());
        postParam.put("address",address.getText().toString());
        postParam.put("password",pswd.getText().toString());
        postParam.put("customerType","1");
        Log.i("Registerdata",""+postParam.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"saveRestCustomer",
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                Log.i("Response is:-",response.toString());

                    Toast.makeText(mContext, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    clearBox();
                    progressDialog.dismiss();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                sendDetailsToServerUser();
                Toast.makeText(RegisterActivity.this, "We are Unable to Provide Data"  , Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private boolean isValidEmail(String email) {
        if (email!=null && email.matches(emailPattern)){
            return true;
        }
        return false;
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
    }private boolean isValidAddress(String address) {
        if (address!=null&&address.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidNumber(String num) {
        if (num!=null&&num.length()==10){
            return true;
        }
        return false;
    }
    private boolean isValidPassword(String pswd) {
        if (pswd!=null && pswd.matches(repswd.getText().toString())){
            return true;
        }
        return false;
    }
    private boolean isValidPassword1(String repswd) {
        if (repswd!=null && repswd.matches(pswd.getText().toString())){
            return true;
        }
        return false;
    }

        private void sendDetailsToServer() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("mobilenumber", mobi.getText().toString());
            postParam.put("email",email.getText().toString());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"requestsms2",
                    new JSONObject(postParam), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {
                    Log.i("Response:-",response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        progressDialog.dismiss();
                        //String ss= String.valueOf(jsonObject.getJSONObject("statuscode"));
                        if(jsonObject.getString("statuscode").toString().contains("OK")){
                            final String otpnumber = jsonObject.getString("otpnumber").toString();
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
                            final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                            // userInput.setText(code);
                            final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);
                            final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancel);
                            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                            alert.setTitle("Enter OTP:");
                            alert.setIcon(R.drawable.ic_launcher);
                            alert.setView(alertLayout);
                            alert.setCancelable(false);
                            final AlertDialog dialog = alert.create();
                            dialog.show();
                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                        if (otpnumber.toString()!=null &&userInput.getText().toString().length()>0  &&
                                                otpnumber.toString().contains(userInput.getText().toString())){
                                            //Toast.makeText(getContext().getApplicationContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
                                            sendDetailsToServerUser();
                                            dialog.hide();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                                            userInput.setText("");
                                            dialog.show();
                                        }
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),""+jsonObject.getString("statuscode").toString(),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*if (response.toString().contains("already exists Mobile")||response.toString().contains("already exists Email")){
                        Log.i("alreadyexistedloop","exists");
                    }
                    else if (response.toString().contains("OK")){
                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
                        final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                        // userInput.setText(code);
                        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);
                        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancel);
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
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
                                    if (response.get("otpnumber").toString()!=null &&userInput.getText().toString().length()>0  &&
                                            response.get("otpnumber").toString().contains(userInput.getText().toString())){
                                        //Toast.makeText(getContext().getApplicationContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
                                        sendDetailsToServerUser();
                                        dialog.hide();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
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
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    sendDetailsToServer();
                   // Toast.makeText(getApplicationContext(), "We are Unable to Provide Data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            DefaultRetryPolicy  retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);
            queue.add(stringRequest);
        }


    private void sendDetailsDuplicateMob() {
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("mobilenumber", mobi.getText().toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"/custMobileDuplicate",new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //clearBox();
                        if (response.toString().contains("does not exists")){
                            sendDetailsToServer();
                        }
                        else {
                            Toast.makeText(mContext, "Mobile number already used", Toast.LENGTH_SHORT).show();
                        }
//                       Intent intent=new Intent(RegisterActivity.this,DashboardActivity.class);
//                        try {
//                            intent.putExtra("username123",response.get("status").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        //startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void sendDetailsDuplicateEmail() {
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("email", email.getText().toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"/custEmailDuplicate",new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //clearBox();
                        if (response.toString().contains("does not exists")){
                            //sendDetailsToServer();
                        }
                        else {
                            Toast.makeText(mContext, "Email already used", Toast.LENGTH_SHORT).show();
                        }
//                       Intent intent=new Intent(RegisterActivity.this,DashboardActivity.class);
//                        try {
//                            intent.putExtra("username123",response.get("status").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        //startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        frstName.setText("");
        lstName.setText("");
        mobi.setText("");
        email.setText("");
        pswd.setText("");
        repswd.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(), values[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public static String removeSpace(String s) {
        String withoutspaces = "";
        String Block="0";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' '&& Block.equals("0")) {
                //withoutspaces += s.charAt(i);
            }else{
                Block ="1";
                withoutspaces += s.charAt(i);
            }

        }
        return withoutspaces;

    }
}
