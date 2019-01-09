package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
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

public class Products_Companies extends Fragment {
    GridView grid;
    static String ll;
    FragmentTransaction transaction;
    ArrayList<HashMap<String, String>> mCategoryList;
    HashMap<String, String> hm;
    ImageButton back_button;
    AppCompatTextView title;
    public static String modelid,cateid,cids;
    static int position;
    static String cats;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    public static Products_Companies newInstance(int pos, String category,String cid) {
        Products_Companies fragment = new Products_Companies();
        position=pos+1;
        cats = category.toString();
        cids=cid;
        Log.i("Category123",""+ll);
         ll = String.valueOf(position);

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

// Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.products_companies, container, false);
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title=(AppCompatTextView)v.findViewById(R.id.title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        Products_Companies.farname.setText(""+cats);
        progressDialog=new ProgressDialog(getActivity());
       // title.setText(""+cats);
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
  */      grid = (GridView) v.findViewById(R.id.grid1);
       // grid.setAdapter(new CustomAdapter(getActivity()));
      grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hm=mCategoryList.get(position);
                modelid=hm.get("companyid");
                cateid=hm.get("productid");
                //Toast.makeText(getActivity(), ""+getString(position), Toast.LENGTH_SHORT).show();
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Products_SubCategories().newInstance(position,hm.get("companyname"),hm.get("categoryname"),hm.get("companyid"),hm.get("categoryid")));
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
        postParam.put("categoryid",cids);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getproductcompanies",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Logresponse123"," response"+response.toString());
                mCategoryList=new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    JSONArray jsonarray = json.getJSONArray("productcompanyDetails");

                    for(int i=0; i<=jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);

                        //Log.e("Log2"," valueeee:-"+position+obj.getString("productid"));
                        //Toast.makeText(getActivity(),""+cats.toString(),Toast.LENGTH_SHORT).show();
                       //if(cats.toString().equals((obj.getString("categoryname")))) {
                            hm = new HashMap<String, String>();
                            hm.put("companyid", obj.getString("companyid"));
                            hm.put("productid", String.valueOf(obj.getInt("productid")));
                            hm.put("companyname", obj.getString("companyname"));
                            hm.put("categoryname", obj.getString("categoryname"));
                            hm.put("categoryimg", obj.getString("categoryimg"));
                            hm.put("categoryid", obj.getString("categoryid"));
                            hm.put("companyimg", obj.getString("companyimg"));
                            mCategoryList.add(hm);

                            Log.i("Data123:---",""+mCategoryList);
                           CustomAdapter adapter = new CustomAdapter(mCategoryList);
                           grid.setAdapter(adapter);
                        //}
                    }

                }catch(JSONException e){
                    e.printStackTrace();

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
                convertView = inflater.inflate(R.layout.activity_prd1, null);
            }
            //HashMap<String, String> hm=alData1.get("companyimg").toString();
            //alData1.get(position).get("companyimg");
            HashMap<String, String> hm=alData1.get(position);
            Log.i("Data",""+hm);

            ImageView img=(ImageView) convertView.findViewById(R.id.display_image1);
            Picasso.with(getActivity()).load(Constants.IMG_URL+hm.get("companyimg")).into(img);
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
                sendDetailsToServer();
                dialog.hide();
            }
        });

    }
}
