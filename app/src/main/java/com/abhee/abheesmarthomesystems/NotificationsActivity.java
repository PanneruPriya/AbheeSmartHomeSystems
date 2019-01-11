package com.abhee.abheesmarthomesystems;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 07-07-2018.
 */

public class NotificationsActivity extends Fragment {
    public static AppCompatTextView farname;
    ArrayList<HashMap<String,String>> list;
    SharedPreferences sharedPreferences;
    ListView listView;
    FragmentTransaction transaction;
    String customerid ="";

    public static NotificationsActivity newInstance() {
        NotificationsActivity fragment = new NotificationsActivity();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.activity_notifications, container, false);
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        customerid=sharedPreferences.getString("custid", "");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        NotificationsActivity.farname.setText("Notifications"+"");
        list = new ArrayList<HashMap<String, String>>();
        createNotification();
        listView = (ListView)v.findViewById(R.id.notificationList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> hm = list.get(position);
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new NotificationDetails().newInstance(hm.get("taskno"),customerid, hm.get("serviceType")));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> params = new HashMap<>();
        params.put("assignby",customerid);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getNotificationsListByCustomerId", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("notificationList",response.toString());
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String s = jsonObject.getString("status");
                            String[] data = s.split(",");
                            if(s.equals("Not_Found")){

                            }else {
                                for (int j = 0; j <data.length; j++) {
                                    String ss = data[j];
                                    Log.i("listtttt", ss);
                                    if (ss.equals("Quotation_List")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("Quotation");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            HashMap<String, String> hm = new HashMap<>();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            hm.put("serviceType",jsonObject1.getString("request_type"));
                                            hm.put("taskno",jsonObject1.getString("salesrequestnumber"));
                                            hm.put("description",jsonObject1.getString("reqdesc"));
                                            list.add(hm);
                                        }
                                    }
                                    if (ss.equals("Service_List")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("Service");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            HashMap<String, String> hm = new HashMap<>();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            JSONArray jsonArray1 = jsonObject1.getJSONArray("" + i);
                                            for (int k = 0; k < jsonArray1.length(); k++) {
                                                Log.i("ara", "" + jsonArray1.length());
                                                JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                                                hm.put("serviceType", jsonObject2.getString("request_type"));
                                                hm.put("taskno", jsonObject2.getString("taskno"));
                                                hm.put("description", jsonObject2.getString("description"));
                                                list.add(hm);
                                            }
                                        }
                                    }
                                }
                            }
                            CustomAdapter1 adapter = new CustomAdapter1(list);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createNotification();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }




    public class CustomAdapter1 extends BaseAdapter {
        private LayoutInflater inflater = null;
        ArrayList<HashMap<String, String>> alData1;

        public CustomAdapter1(ArrayList<HashMap<String, String>> al) {
            alData1 = al;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return alData1.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.ticket_status, null);
            }
            HashMap<String, String> hm = alData1.get(position);
            TextView tv = (TextView) convertView.findViewById(R.id.tv1);
            TextView tv1 = (TextView) convertView.findViewById(R.id.tv2);
            TextView tv2 = (TextView) convertView.findViewById(R.id.tv3);

            tv.setText("Request Type: " + hm.get("serviceType"));
            tv1.setText("Ticket No: " + hm.get("taskno"));
            // tv2.setText(desc);
            tv2.setText("Description: " + hm.get("description"));
            return convertView;
        }

    }

}
