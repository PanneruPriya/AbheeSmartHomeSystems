package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class TicketStatusActivity extends Fragment {
    ListView list;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    ArrayList<HashMap<String, String>> mCategoryList1;
    HashMap<String, String> hm;
    public static String custid, userid, mobile;
    SharedPreferences sharedPreferences;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;

    public static TicketStatusActivity newInstance() {
        TicketStatusActivity fragment = new TicketStatusActivity();
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
        v = inflater.inflate(R.layout.activity_ticketstatus, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        farname = (AppCompatTextView) view.findViewById(R.id.txtName);
        TicketStatusActivity.farname.setText("Service Ticket Status" + "");
        progressDialog = new ProgressDialog(getActivity());
        title = (AppCompatTextView) v.findViewById(R.id.title);

        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        custid = sharedPreferences.getString("custid", "");
        mobile = sharedPreferences.getString("mobilenumber", "");
        list = (ListView) v.findViewById(R.id.listTS);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        getDetailsToServer();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hm = mCategoryList1.get(position);
                custid = hm.get("customerId");
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new TicketStatusActivity1().newInstance(hm.get("servicetyp"),hm.get("uploadfile"),hm.get("requesttime"),hm.get("modelname"),hm.get("description"),hm.get("warranty"),hm.get("category"),
                        hm.get("companyname"),hm.get("taskno"),hm.get("communicationaddress"),hm.get("taskdeadline"),hm.get("assignedto"),hm.get("priority"),hm.get("descriptionA"),hm.get("uploadfileA"),hm.get("subject"),hm.get("kstatus"),hm.get("mobilenumber")));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }

    private void getDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("customerId", custid);
        Log.i("Logresponse", "" + custid);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "getServiceStatus", new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Logresponse", " " + response.toString());
                progressDialog.dismiss();
                mCategoryList1 = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray ServiceRequest = json.getJSONArray("ServiceRequest");
                    for (int i=0;i<ServiceRequest.length();i++){
                        JSONObject jsonObject= ServiceRequest.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject.getJSONArray(""+i);
                        for (int j=0;j<jsonArray1.length();j++){
                            HashMap<String,String> hm = new HashMap<>();
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            JSONArray CustomerBean = jsonObject1.getJSONArray("CustomerBean");

                            for (int k=0;k<CustomerBean.length();k++){
                                JSONObject jsonObject2 = CustomerBean.getJSONObject(k);
                                hm.put("servicetype",jsonObject2.getString("servicetype"));
                                hm.put("uploadfile",jsonObject2.getString("uploadfile"));
                                hm.put("requesttime",jsonObject2.getString("requesttime"));
                                hm.put("modelname",jsonObject2.getString("modelname"));
                                hm.put("description",jsonObject2.getString("description"));
                                hm.put("warranty",jsonObject2.getString("warranty"));
                                hm.put("id",jsonObject2.getString("id"));
                                hm.put("customer_id",jsonObject2.getString("customer_id"));
                                hm.put("category",jsonObject2.getString("category"));
                                hm.put("companyname",jsonObject2.getString("companyname"));
                                hm.put("taskno",jsonObject2.getString("taskno"));
                                hm.put("communicationaddress",jsonObject2.getString("communicationaddress"));
                            }
                            JSONArray AdminBean = jsonObject1.getJSONArray("AdminBean");
                            for (int l=0;l<AdminBean.length();l++){
                                JSONObject jsonObject3 = AdminBean.getJSONObject(l);
                                hm.put("status",jsonObject3.getString("status"));
                                if(!hm.get("status").equals("0")){
                                    hm.put("status",jsonObject3.getString("status"));
                                    if(!jsonObject3.getString("taskdeadline").equals(null)){
                                        hm.put("taskdeadline",jsonObject3.getString("taskdeadline"));
                                    }
                                    if(!jsonObject3.getString("mobilenumber").equals(null)){
                                        hm.put("mobilenumber",jsonObject3.getString("mobilenumber"));
                                    }
                                    if(!jsonObject3.getString("assignedto").equals(null)){
                                        hm.put("assignedto",jsonObject3.getString("assignedto"));
                                    }
                                    if(!jsonObject3.getString("priority").equals(null)){
                                        hm.put("priority",jsonObject3.getString("priority"));
                                    }
                                    if(!jsonObject3.getString("description").equals(null)){
                                        hm.put("descriptionA",jsonObject3.getString("description"));
                                    }
                                    if(!jsonObject3.getString("imgfile").equals(null)){
                                        hm.put("uploadfileA",jsonObject3.getString("imgfile"));
                                    }
                                    if(!jsonObject3.getString("subject").equals(null)){
                                        hm.put("subject",jsonObject3.getString("subject"));
                                    }
                                    if(!jsonObject3.getString("kstatus").equals(null)){
                                        hm.put("kstatus",jsonObject3.getString("kstatus"));
                                    }
                                }

                                mCategoryList1.add(hm);
                            }

                        }
                    }

                    Log.i("list1",mCategoryList1.toString());
                    CustomAdapter1 adapter = new CustomAdapter1(mCategoryList1);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chik();
                getDetailsToServer();
               // Toast.makeText(getActivity().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
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

            tv.setText("Service Request for my " + hm.get("companyname") + " " + hm.get("category"));
            tv1.setText("Ticket No: " + hm.get("taskno"));
            // tv2.setText(desc);
            tv2.setText("Status : Assigned to " + hm.get("assignedto"));
            return convertView;
        }

    }
    public void chik() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertlayout1, null);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.rbutton);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Reload");
       // alert.setIcon(R.drawable.ic_launcher);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailsToServer();
                dialog.hide();
            }
        });


    }
}