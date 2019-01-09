package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Products_Names extends Fragment {
    GridView grid;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    HashMap<String, String> hm;
    ArrayList<HashMap<String, String>> mCategoryLists;
    public static String modelid,cateid;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    public static Products_Names newInstance() {
        Products_Names fragment = new Products_Names();
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
        v = inflater.inflate(R.layout.products_names, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname= view.findViewById(R.id.txtName);
        Products_Names.farname.setText("Products"+"");
        progressDialog=new ProgressDialog(getActivity());
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title= v.findViewById(R.id.title);
      //  title.setText("Products");
       /* back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ContentDashboard().newInstance());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
*/        grid = v.findViewById(R.id.grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hm=mCategoryLists.get(position);
                modelid=hm.get("id");
                cateid=hm.get("category");

                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Products_Companies().newInstance(position, hm.get("title"),hm.get("cid")));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        sendDetailsToServer();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }
    private void sendDetailsToServer() {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getcategories",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Log1"," response"+response.toString());
              mCategoryLists  =new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    JSONArray jsonarray = json.getJSONArray("categorieslist");

                    for(int i=0; i<jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        hm=new HashMap<String, String>();
                        hm.put("title", obj.getString("category"));
                        hm.put("url", obj.getString("categoryimg"));
                        hm.put("cid",obj.getString("id"));
                        mCategoryLists.add(hm);
                    }
                    Log.i("id",mCategoryLists.toString());
                    grid.setAdapter(new CustomAdapter( mCategoryLists));
                }catch(JSONException e){
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chik();
               // Toast.makeText(getActivity().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                sendDetailsToServer();
            }
        });
        queue.add(stringRequest);
    }

    public class CustomAdapter extends BaseAdapter {
        private LayoutInflater inflater=null;
        ArrayList<HashMap<String, String>> alData1;
        public CustomAdapter( ArrayList<HashMap<String, String>> al) {
            alData1=al;
            inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            if(convertView==null){
                convertView = inflater.inflate(R.layout.activity_gridview, null);
            }
            HashMap<String, String> hm=alData1.get(position);
            TextView tv= convertView.findViewById(R.id.grid_text);
            ImageView img= convertView.findViewById(R.id.grid_image);
            Picasso.with(getActivity()).load(Constants.IMG_URL+hm.get("url")).into(img);
            tv.setText(hm.get("title"));
           // Log.e("Loge"," hm"+hm.get("title"));
            // Toast.makeText(getActivity().getApplicationContext(), "title:" + hm.get("title"), Toast.LENGTH_LONG).show();
            return convertView;
        }
    }
    public void chik() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertlayout1, null);
        final Button btnSave = alertLayout.findViewById(R.id.rbutton);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Reload");
      //  alert.setIcon(R.drawable.ic_launcher);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailsToServer();
                dialog.hide();
            }
        });
    }
}
