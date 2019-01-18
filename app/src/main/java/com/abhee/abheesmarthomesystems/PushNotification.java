package com.abhee.abheesmarthomesystems;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PushNotification extends AppCompatActivity {
    AppCompatTextView titles;
    AppCompatImageButton back;
    HashMap<String,String> hm;
    String requsettype,taskno,custid;
    String image,image2;
    int width,heigth;
    ArrayList<HashMap<String, String>> mCategoryList;

    TextView tv1,tv2,tv3,address,duedate,tsq1,tsq2,tsqi,tsq3,tsq4,tsq5,tsq6,tsq7,tsq8,tsq9;
    ImageView imageViewt,adminimage,uploadfile,imgfile;
    LinearLayout quotation_layout,Service_Layout;
    AppCompatTextView mobilenumber,subject,requesttime,modelname,description,priority,kstatus,assignedto,communicationaddress,servicetype,companyname,tasknos,warranty,customer_id,category,taskdeadline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        mCategoryList = new ArrayList<>();
        getSupportActionBar().hide();
        Intent intent = getIntent();
        requsettype= intent.getStringExtra("Quotation");
        taskno = intent.getStringExtra("taskno");
        custid = intent.getStringExtra("customerid");

        quotation_layout =(LinearLayout)findViewById(R.id.Quotation);
        Service_Layout=(LinearLayout)findViewById(R.id.Service);

        mobilenumber=(AppCompatTextView)findViewById(R.id.mobilenumber);
        subject=(AppCompatTextView)findViewById(R.id.subject);
        requesttime=(AppCompatTextView)findViewById(R.id.requesttime);
        modelname=(AppCompatTextView)findViewById(R.id.modelname);
        description=(AppCompatTextView)findViewById(R.id.description);
        priority=(AppCompatTextView)findViewById(R.id.priority);
        kstatus=(AppCompatTextView)findViewById(R.id.kstatus);
        assignedto=(AppCompatTextView)findViewById(R.id.assignedto);
        communicationaddress=(AppCompatTextView)findViewById(R.id.communicationaddress);
        servicetype=(AppCompatTextView)findViewById(R.id.servicetype);
        companyname=(AppCompatTextView)findViewById(R.id.companyname);
        tasknos=(AppCompatTextView)findViewById(R.id.taskno);
        warranty=(AppCompatTextView)findViewById(R.id.warranty);
        customer_id=(AppCompatTextView)findViewById(R.id.customer_id);
        category=(AppCompatTextView)findViewById(R.id.category);
        taskdeadline=(AppCompatTextView)findViewById(R.id.taskdeadline);

        uploadfile=(ImageView)findViewById(R.id.uploadfile);
        imgfile=(ImageView)findViewById(R.id.imgfile);



        tsq1=(TextView)findViewById(R.id.tsq1);
        tsq2=(TextView)findViewById(R.id.tsq2);
        tsq3=(TextView)findViewById(R.id.tsq3);
        tsq4=(TextView)findViewById(R.id.tsq4);
        tsq5=(TextView)findViewById(R.id.tsq5);
        tsq6=(TextView)findViewById(R.id.tsq6);
        imageViewt=(ImageView)findViewById(R.id.imageviewt);
        adminimage=(ImageView)findViewById(R.id.adminimage);
        back=(AppCompatImageButton)findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            }
        });
        titles=(AppCompatTextView)findViewById(R.id.noticTitle);
        titles.setText(requsettype +" Notification");
        if(requsettype.equals("Quotation")){
            QuotationDetails();
            quotation_layout.setVisibility(View.VISIBLE);
            Service_Layout.setVisibility(View.GONE);
        }else if(requsettype.equals("Service")){
            ServiceDeails();
            quotation_layout.setVisibility(View.GONE);
            Service_Layout.setVisibility(View.VISIBLE);
        }
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getApplicationContext(),LoginActivity.class));
    }

    private void ServiceDeails() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Map<String,String> postparam = new HashMap<>();
        postparam.put("taskno",taskno);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL + "getNotificationByTaskno", new JSONObject(postparam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Service respose",response.toString());
                        try {
                            hm = new HashMap<>();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("NotificationList");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                hm.put("mobilenumber",jsonObject1.getString("mobilenumber"));
                                hm.put("subject",jsonObject1.getString("subject"));
                                hm.put("requesttime",jsonObject1.getString("requesttime"));
                                hm.put("modelname",jsonObject1.getString("modelname"));
                                hm.put("description",jsonObject1.getString("description"));
                                hm.put("priority",jsonObject1.getString("priority"));
                                hm.put("kstatusid",jsonObject1.getString("kstatusid"));
                                hm.put("assignedto",jsonObject1.getString("assignedto"));
                                hm.put("communicationaddress",jsonObject1.getString("communicationaddress"));
                                hm.put("servicetype",jsonObject1.getString("servicetype"));
                                hm.put("kstatus",jsonObject1.getString("kstatus"));
                                hm.put("uploadfile",jsonObject1.getString("uploadfile"));
                                hm.put("companyname",jsonObject1.getString("companyname"));
                                hm.put("imgfile",jsonObject1.getString("imgfile"));
                                hm.put("taskno",jsonObject1.getString("taskno"));
                                hm.put("warranty",jsonObject1.getString("warranty"));
                                hm.put("id",jsonObject1.getString("id"));
                                hm.put("customer_id",jsonObject1.getString("customer_id"));
                                hm.put("category",jsonObject1.getString("category"));
                                hm.put("taskdeadline",jsonObject1.getString("taskdeadline"));
                                hm.put("status",jsonObject1.getString("status"));

                            }
                            setdataofservice(hm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setdataofservice(HashMap<String,String> hm) {
        mobilenumber.setText(hm.get("mobilenumber"));
        subject.setText(hm.get("subject"));
        requesttime.setText(hm.get("requesttime"));
        modelname.setText(hm.get("modelname"));
        description.setText(hm.get("description"));
        priority.setText(hm.get("priority"));
        kstatus.setText(hm.get("kstatus"));
        assignedto.setText(hm.get("assignedto"));
        communicationaddress.setText(hm.get("communicationaddress"));
        servicetype.setText(hm.get("servicetype"));
        companyname.setText(hm.get("companyname"));
        tasknos.setText(hm.get("taskno"));
        if(hm.get("warranty").equals("0")) {
            warranty.setText("Yes");
        }else{
            warranty.setText("NO");
        }
        customer_id.setText(hm.get("customer_id"));
        category.setText(hm.get("category"));
        taskdeadline.setText(hm.get("taskdeadline"));
        Picasso.with(PushNotification.this).load(Constants.IMG_URL+hm.get("uploadfile")).into(uploadfile);
        Picasso.with(PushNotification.this).load(Constants.IMG_URL+hm.get("imgfile")).into(imgfile);
        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getApplicationContext()).load(Constants.IMG_URL+image).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        imgfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getApplicationContext()).load(Constants.IMG_URL+image2).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void QuotationDetails() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("salesrequestnumber",taskno);
        Log.i("custId",""+custid);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getquotationlistByRequestNo",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hm=new HashMap<String, String>();
                Log.e("Logresponse  --->"," "+response.toString());
                mCategoryList=new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    JSONArray jsonarray = json.getJSONArray("quotationslist");
                    JSONArray jsonarray2 = json.getJSONArray("AdminResponseList");
                    for(int i=0; i<jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        hm.put("modelname",obj.getString("modelname"));
                        hm.put("reqdesc",obj.getString("reqdesc"));
                        hm.put("address",obj.getString("address"));
                        hm.put("salesrequestnumber",obj.getString("salesrequestnumber"));
                        hm.put("created_time",obj.getString("created_time"));
                        hm.put("imgfiles",obj.getString("imgfiles"));
                        //image=obj.getString("imgfiles");
                        Log.i("chikimage",""+image);
                        //Picasso.with(getApplicationContext()).load(Constants.IMG_URL+image).into(imageViewt);
                        //Picasso.with(getActivity()).load(Constants.IMG_URL+image).into(imageViewt);
                    }
                    for (int i=0;i<jsonarray2.length();i++){
                        JSONObject obj1=jsonarray2.getJSONObject(i);
                        int status=obj1.getInt("status");
                        if(status!=0){
                            hm=new HashMap<String,String>();
                            hm.put("notes",obj1.getString("notes"));
                            hm.put("quotation_documents",obj1.getString("quotation_documents"));
                            //Picasso.with(getActivity()).load(Constants.IMG_URL+image2).into(adminimage);
                        }
                        setdataofquotation(hm);
                    }
                }catch(JSONException e){

                }
                Log.i("chiklist",mCategoryList.toString());
                Log.i("hmsize",""+hm.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void setdataofquotation(HashMap<String,String> hm) {
        Log.i("set hm ",""+hm.size());
        tsq1.setText(hm.get("modelname"));
        tsq2.setText(hm.get("reqdesc"));
        tsq4.setText(hm.get("address"));
        tsq5.setText(hm.get("salesrequestnumber"));
        tsq3.setText(hm.get("created_time"));
        image=hm.get("imgfiles");
        tsq6.setText(hm.get("notes"));
        image2=hm.get("quotation_documents");
        Picasso.with(PushNotification.this).load(Constants.IMG_URL+image).into(imageViewt);
        Picasso.with(PushNotification.this).load(Constants.IMG_URL+image2).into(adminimage);
        imageViewt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getApplicationContext()).load(Constants.IMG_URL+image).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        adminimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getApplicationContext()).load(Constants.IMG_URL+image2).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
