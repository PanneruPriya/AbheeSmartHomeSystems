package com.abhee.abheesmarthomesystems;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class NotificationDetails extends Fragment {
    static String tasknos,customerids,request_types;
    ArrayList<HashMap<String,String>> details;
    ArrayList<HashMap<String,String>> history;
    AppCompatTextView serviceType,subject,requesttime,descriptions,priority,communicationaddresss,customerId,taskno,warrantys,categorys,taskdeadline;
    AppCompatImageView imgfile,uploadfile;
    LinearLayout show;
    HashMap<String, String> hm;
    ArrayList<HashMap<String, String>> mCategoryList;
    String image,image2;
    public static NotificationDetails newInstance(String taskno, String customerid,String request_type) {
        NotificationDetails fragment = new NotificationDetails();
        tasknos=taskno;
        customerids=customerid;
        request_types=request_type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notification_details, container, false);
        if(request_types.equals("Service Request")){
            getDetailsforserver();
        }else{
            getDetailsToQuotation();
        }

        component(v);

        return  v;
    }

    private void setdate() {
        HashMap<String,String> hm = details.get(0);

        serviceType.setText(hm.get("serviceType"));
        subject.setText(hm.get("subject"));
        requesttime.setText(hm.get("requesttime"));
        descriptions.setText(hm.get("description"));
        priority.setText(hm.get("priority"));
        communicationaddresss.setText(hm.get("communicationaddress"));
        customerId.setText(hm.get("customerId"));
        taskno.setText(hm.get("taskno"));
        warrantys.setText(hm.get("warranty"));
        categorys.setText(hm.get("category"));
        taskdeadline.setText(hm.get("taskdeadline"));
    }

    private void component(View v) {
        details = new ArrayList<>();
        history = new ArrayList<>();
        serviceType=v.findViewById(R.id.serviceType);
        subject=v.findViewById(R.id.subject);
        requesttime=v.findViewById(R.id.requesttime);
        descriptions=v.findViewById(R.id.description);
        priority=v.findViewById(R.id.priority);
        communicationaddresss=v.findViewById(R.id.communicationaddress);
        customerId=v.findViewById(R.id.customerId);
        taskno=v.findViewById(R.id.taskno);
        warrantys=v.findViewById(R.id.warranty);
        categorys=v.findViewById(R.id.category);
        taskdeadline=v.findViewById(R.id.taskdeadline);
        imgfile=v.findViewById(R.id.imgfile);
        uploadfile=v.findViewById(R.id.uploadfile);
        show=v.findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getDetailsforserver() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> params= new HashMap<>();
        params.put("taskno",tasknos);
        params.put("customerId",customerids);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"getServiceDetailsByTaskno", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("notification details",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if(jsonObject.getString("NotificationStatus").equals("Found")){
                                JSONArray ServiceDtailsList = jsonObject.getJSONArray("ServiceDtailsList");
                                JSONArray NotificationList = jsonObject.getJSONArray("NotificationList");
                                for (int i =0;i<ServiceDtailsList.length();i++){
                                    JSONObject obj= ServiceDtailsList.getJSONObject(i);
                                    HashMap<String,String> hm1 = new HashMap<>();
                                    hm1.put("serviceType",obj.getString("serviceType"));
                                    hm1.put("subject",obj.getString("subject"));
                                    hm1.put("requesttime",obj.getString("requesttime"));
                                    hm1.put("description",obj.getString("description"));
                                    hm1.put("priority",obj.getString("priority"));
                                    hm1.put("communicationaddress",obj.getString("communicationaddress"));
                                    //hm.put("taskstatus",obj.getString("taskstatus"));
                                    hm1.put("imgfile",obj.getString("imgfile"));
                                    hm1.put("uploadfile",obj.getString("uploadfile"));
                                    hm1.put("customerId",obj.getString("customerId"));
                                    hm1.put("taskno",obj.getString("taskno"));
                                    hm1.put("warranty",obj.getString("warranty"));
                                   // hm.put("id",obj.getString("id"));
                                    hm1.put("category",obj.getString("category"));
                                    hm1.put("taskdeadline",obj.getString("taskdeadline"));
                                    details.add(hm1);

                                }for (int i =0;i<NotificationList.length();i++){
                                    JSONObject obj= NotificationList.getJSONObject(i);
                                    HashMap<String,String> hm1 = new HashMap<>();
                                    hm1.put("notificationstatus",obj.getString("notificationstatus"));
                                    hm1.put("taskno",obj.getString("taskno"));
                                    hm1.put("description",obj.getString("description"));
                                    hm1.put("addComment",obj.getString("addComment"));
                                    history.add(hm1);
                                }

                            }else{
                                Toast.makeText(getActivity(),"history not found",Toast.LENGTH_SHORT).show();
                            }
                            setdate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getDetailsforserver();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    private void getDetailsToQuotation() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("salesrequestnumber",tasknos);
        Log.i("custId",""+customerids);

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
                        // hm.put("mobileno",obj.getString("mobileno"));
                        hm.put("reqdesc",obj.getString("reqdesc"));
                        hm.put("address",obj.getString("address"));
                        hm.put("salesrequestnumber",obj.getString("salesrequestnumber"));
                        hm.put("created_time",obj.getString("created_time"));

                        // tsqi.setText(obj.getString("imgfiles"));
                        image=obj.getString("imgfiles");
                        Log.i("chikimage",""+image);
                        //Picasso.with(getActivity()).load(Constants.IMG_URL+image).into(imageViewt);
                    }
                    for (int i=0;i<jsonarray2.length();i++){
                        JSONObject obj1=jsonarray2.getJSONObject(i);
                        int status=obj1.getInt("status");
                        if(status!=0){

                            hm=new HashMap<String,String>();
                            hm.put("notes",obj1.getString("notes"));
                            hm.put("quotation_documents",obj1.getString("quotation_documents"));

                            image2=obj1.getString("quotation_documents");
                            //Picasso.with(getActivity()).load(Constants.IMG_URL+image2).into(adminimage);


                        }

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


}
