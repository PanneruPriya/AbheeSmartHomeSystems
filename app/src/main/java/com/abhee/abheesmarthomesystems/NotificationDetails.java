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
import java.util.Map;

public class NotificationDetails extends Fragment {
    static String tasknos,customerids;
    ArrayList<HashMap<String,String>> details;
    ArrayList<HashMap<String,String>> history;
    AppCompatTextView serviceType,subject,requesttime,descriptions,priority,communicationaddresss,customerId,taskno,warrantys,categorys,taskdeadline;
    AppCompatImageView imgfile,uploadfile;
    LinearLayout show;

    public static NotificationDetails newInstance(String taskno, String customerid) {
        NotificationDetails fragment = new NotificationDetails();
        tasknos=taskno;
        customerids=customerid;
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
        getDetailsforserver();
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
                                    HashMap<String,String> hm = new HashMap<>();
                                    hm.put("serviceType",obj.getString("serviceType"));
                                    hm.put("subject",obj.getString("subject"));
                                    hm.put("requesttime",obj.getString("requesttime"));
                                    hm.put("description",obj.getString("description"));
                                    hm.put("priority",obj.getString("priority"));
                                    hm.put("communicationaddress",obj.getString("communicationaddress"));
                                    //hm.put("taskstatus",obj.getString("taskstatus"));
                                    hm.put("imgfile",obj.getString("imgfile"));
                                    hm.put("uploadfile",obj.getString("uploadfile"));
                                    hm.put("customerId",obj.getString("customerId"));
                                    hm.put("taskno",obj.getString("taskno"));
                                    hm.put("warranty",obj.getString("warranty"));
                                   // hm.put("id",obj.getString("id"));
                                    hm.put("category",obj.getString("category"));
                                    hm.put("taskdeadline",obj.getString("taskdeadline"));
                                    details.add(hm);

                                }for (int i =0;i<NotificationList.length();i++){
                                    JSONObject obj= NotificationList.getJSONObject(i);
                                    HashMap<String,String> hm = new HashMap<>();
                                    hm.put("notificationstatus",obj.getString("notificationstatus"));
                                    hm.put("taskno",obj.getString("taskno"));
                                    hm.put("description",obj.getString("description"));
                                    hm.put("addComment",obj.getString("addComment"));
                                    history.add(hm);
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

            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}
