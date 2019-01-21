package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class TicketStatusActivity1 extends Fragment {

    public static  HashMap<String, String> mCategoryList;
    public static String custid,userid,mobile;
    SharedPreferences sharedPreferences;
    AppCompatTextView title;
    FragmentTransaction transaction;
    int width,heigth;

    static String servicetypes, uploadfiles, requesttimes, modelnames, descriptions,
     warrantys, categorys, companynames, tasknos, communicationaddresss,
     taskdeadlines, assignedtos, prioritys, descriptionAs, uploadfileAs, subjects,kstatuss,mobilenumbers,invimgs;

    AppCompatTextView Company,Model,Task_No,mnumber,Address,Due_Date,description,warranty,category,assignedtoA,descriptionA,subjectA,taskdeadlineA,priorityA,status,AdminUploadtitle;
    ImageView cusUploaded,adminUploading,invimage;
    LinearLayout invView;
    public static AppCompatTextView farname;
    AppCompatButton reassign;
    AlertDialog dialog;
    public static TicketStatusActivity1 newInstance(
            String servicetype,String uploadfile,String requesttime,String modelname,String description,
            String warranty,String category,String companyname,String taskno,String communicationaddress,
            String taskdeadline,String assignedto,String priority,String descriptionA,String uploadfileA,String subject,String kstatus,String mobilenumber,String invimg
    ) {
        TicketStatusActivity1 fragment = new TicketStatusActivity1();
        servicetypes =servicetype;
        uploadfiles =uploadfile;
        requesttimes = requesttime;
        modelnames =modelname;
        descriptions =description;
        warrantys =warranty;
        categorys=category;
        companynames=companyname;
        tasknos=taskno;
        communicationaddresss=communicationaddress;
        taskdeadlines = taskdeadline;
        assignedtos = assignedto;
        prioritys =priority;
        descriptionAs =descriptionA;
        uploadfileAs =uploadfileA;
        subjects =subject;
        kstatuss=kstatus;
        mobilenumbers =mobilenumber;
        invimgs=invimg;
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
        v = inflater.inflate(R.layout.activity_ticketstatus1, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title=(AppCompatTextView)v.findViewById(R.id.title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        TicketStatusActivity1.farname.setText("Service Ticket Status Data");
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid", "");
        custid=sharedPreferences.getString("custid", "");
        mobile=sharedPreferences.getString("mobilenumber","");
        Company= (AppCompatTextView) v.findViewById(R.id.Company);
        Model= (AppCompatTextView) v.findViewById(R.id.Model);
        Task_No= (AppCompatTextView) v.findViewById(R.id.Task_No);
        Address= (AppCompatTextView) v.findViewById(R.id.communicationaddress);
        Due_Date= (AppCompatTextView) v.findViewById(R.id.Due_Date);
        description= (AppCompatTextView) v.findViewById(R.id.description);
        warranty= (AppCompatTextView) v.findViewById(R.id.warranty);
        category= (AppCompatTextView) v.findViewById(R.id.category);
        assignedtoA= (AppCompatTextView) v.findViewById(R.id.assignedtoA);
        descriptionA= (AppCompatTextView) v.findViewById(R.id.descriptionA);
        subjectA= (AppCompatTextView) v.findViewById(R.id.subjectA);
        taskdeadlineA= (AppCompatTextView) v.findViewById(R.id.taskdeadlineA);
        priorityA= (AppCompatTextView) v.findViewById(R.id.priorityA);
        cusUploaded=(ImageView)v.findViewById(R.id.cusUploaded);
        adminUploading=(ImageView)v.findViewById(R.id.adminUploading);
        status=(AppCompatTextView)v.findViewById(R.id.kstatus);
        AdminUploadtitle=(AppCompatTextView)v.findViewById(R.id.upload);
        mnumber =(AppCompatTextView)v.findViewById(R.id.mobilenumber);
        invView = (LinearLayout)v.findViewById(R.id.invView);
        invimage=(ImageView)v.findViewById(R.id.invImage);
        reassign=(AppCompatButton)v.findViewById(R.id.reassign);

        reassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                Map<String,String> params = new HashMap<>();
                params.put("taskno",tasknos);
                params.put("kstatus",kstatuss);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"changeKstatusByTaskno", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("kstatus",response.toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(response.toString());
                                    Toast.makeText(getActivity(),""+jsonObject.getString("Kstatus"),Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("Kstatus").equals("Updated")) {
                                        reassign.setVisibility(View.GONE);
                                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.frame, new TicketStatusActivity().newInstance());
                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        transaction.addToBackStack("tag");
                                        transaction.commit();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            }
        });

        cusUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                ImageView photoView = mView.findViewById(R.id.imageView);
                Picasso.with(getActivity()).load(Constants.IMG_URL+uploadfiles).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        setData();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }

    private void setData() {
        Company.setText(companynames);
        Model.setText(modelnames);
        Task_No.setText(tasknos);
        Address.setText(communicationaddresss);
        Due_Date.setText(requesttimes);
        description.setText(descriptions);
        if(warrantys.equals("0")){
            warranty.setText("YES");
        }else{
            warranty.setText("NO");
        }
        category.setText(categorys);
        assignedtoA.setText(assignedtos);
        descriptionA.setText(descriptionAs);
        subjectA.setText(subjects);
        taskdeadlineA.setText(taskdeadlines);
        priorityA.setText(prioritys);
        status.setText(kstatuss);
        mnumber.setText(mobilenumbers);
        if(kstatuss.equals("CUSTOMER NOT AVAILABLE")){
            reassign.setVisibility(View.VISIBLE);
        }
        Picasso.with(getActivity()).load(Constants.IMG_URL+uploadfiles).into(cusUploaded);
        if(invimgs.length()!=1){
            invView.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(Constants.IMG_URL+invimgs).into(invimage);
        }
        cusUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getActivity()).load(Constants.IMG_URL+uploadfiles).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
        if (!uploadfileAs.equals(" ")) {
            adminUploading.setVisibility(View.VISIBLE);
            AdminUploadtitle.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(Constants.IMG_URL + uploadfileAs).into(adminUploading);
        }
    }
}