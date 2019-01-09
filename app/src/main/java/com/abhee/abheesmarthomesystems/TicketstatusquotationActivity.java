package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

public class TicketstatusquotationActivity extends Fragment {
    ListView list;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    ArrayList<HashMap<String, String>> mCategoryList1;
    public static String custid, userid, mobile,modelname;
    SharedPreferences sharedPreferences;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;

    public static TicketstatusquotationActivity newInstance() {
        TicketstatusquotationActivity fragment = new  TicketstatusquotationActivity();
        return fragment;
    }

    @Override
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
        v = inflater.inflate(R.layout.ticketstatusquotation, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        farname = (AppCompatTextView) view.findViewById(R.id.txtName);
        TicketstatusquotationActivity.farname.setText("Quotation Ticket Status" + "");
        progressDialog = new ProgressDialog(getActivity());
        mCategoryList1 = new ArrayList<HashMap<String, String>>();

        // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title = (AppCompatTextView) v.findViewById(R.id.title);
        // title.setText("Ticket Status");
       /* back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ContentDashboard().newInstance());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });*/
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        custid = sharedPreferences.getString("custid", "");
        mobile = sharedPreferences.getString("mobilenumber", "");
        modelname=sharedPreferences.getString("modelname","");
        list = (ListView) v.findViewById(R.id.listTS);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        getDetailsToServer();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hm;
                hm = mCategoryList1.get(position);
                //custid = hm.get("customerId");
               // modelname=hm.get("modelname");
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Ticketstatusquotation1Activity().newInstance(hm.get("salesrequestnumber")));
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
        postParam.put("customerid",custid);
        Log.i("Logresponse", "" + modelname);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "getquotationlist", new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Logresponse", " " + response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray jsonarray = json.getJSONArray("quotationslist");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<String, String>();
                       /* hm.put("modelname",obj.getString("modelname"));
                        hm.put("imgfiles",obj.getString("imgfiles"));
                        hm.put("address",obj.getString("address"));
                        hm.put("location",obj.getString("location"));
                        hm.put("id",obj.getString("id"));
                        hm.put("salesrequestnumber",obj.getString("salesrequestnumber"));
                       */
                        hm.put("request_type",obj.getString("request_type"));
                        hm.put("modelname",obj.getString("modelname"));
                        hm.put("salesrequestnumber",obj.getString("salesrequestnumber"));
                        mCategoryList1.add(hm);
                        Log.i("hm size",""+hm.size());
                    }
                    Log.i("list",mCategoryList1.toString());
                    TicketstatusquotationActivity.CustomAdapter1 adapter = new TicketstatusquotationActivity.CustomAdapter1(mCategoryList1);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chik();
                // Toast.makeText(getActivity().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
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
                convertView = inflater.inflate(R.layout.ticket_statues1, null);
            }
            HashMap<String, String> hm = alData1.get(position);
            TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
            TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
            TextView tv3 = (TextView) convertView.findViewById(R.id.tv3);

            tv1.setText("Request for " + hm.get("modelname"));
            tv2.setText("Ticket No: " + hm.get("salesrequestnumber"));
            // tv2.setText(desc);
            tv3.setText("Request Type  :  " + hm.get("request_type"));
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
