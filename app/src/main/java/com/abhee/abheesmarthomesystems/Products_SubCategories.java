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

import static com.abhee.abheesmarthomesystems.Products_Companies.position;

public class Products_SubCategories extends Fragment {
    GridView list;
    FragmentTransaction transaction;
    ArrayList<HashMap<String, String>> mCategoryList;
    HashMap<String, String> hm;
    public static String modelid,cateid;
    static int pos;
    static String cats,cats1,companyids,categoryids;
    String walletBal,desc;
    ImageButton back_button;
    AppCompatTextView title;
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    AlertDialog dialog;

    public Products_SubCategories newInstance(int position, String companyname,
                                              String categoryname,String companyid,String categoryid) {
        Products_SubCategories fragment = new Products_SubCategories();
        pos=position+1;
        cats = companyname.toString();
        cats1=categoryname.toString();
        companyids = companyid;
        categoryids = categoryid;
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
        v = inflater.inflate(R.layout.products_subcategories, container, false);
       // back_button=(ImageButton)v.findViewById(R.id.back_button);
        title=(AppCompatTextView)v.findViewById(R.id.title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        Products_SubCategories.farname.setText(""+cats+" _ "+cats1);
        progressDialog=new ProgressDialog(getActivity());

       // title.setText(""+cats+"  "+cats1);
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
       list = (GridView) v.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hm=mCategoryList.get(position);
                modelid=hm.get("companyid");
                cateid=hm.get("categoryid");
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Products_ItemDataDsiplay().newInstance(hm.get("description"),
                        hm.get("companyname"),hm.get("productmodelname"),
                        hm.get("productmodelpics"),hm.get("product_model_specifications"),
                        hm.get("product_price"),
                        hm.get("companyid"),hm.get("categoryid")));
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
        postParam.put("categoryid",categoryids);
        postParam.put("companyid",companyids);


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getproducts",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Log response"," response"+response.toString());
                mCategoryList=new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    JSONArray jsonarray = json.getJSONArray("productDetails");

                    for(int i=0; i<jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(cats.toString().equals (obj.getString("companyname"))) {
                            Log.e("Log1"," valueeee"+pos+obj.getString("companyname"));
                            hm = new HashMap<String, String>();

                            hm.put("productmodelpics",obj.getString("productmodelpics"));
                            hm.put("productmodelname",obj.getString("productmodelname"));
                            hm.put("product_price",obj.getString("product_price"));
                            hm.put("companyname",obj.getString("companyname"));
                            hm.put("companyid",obj.getString("companyid"));
                            hm.put("categoryid",obj.getString("categoryid"));
                            hm.put("category",obj.getString("category"));
                            hm.put("product_model_specifications",obj.getString("product_model_specifications"));
                            hm.put("description",obj.getString("description"));
                           // walletBal = obj.getString("description");
                            //desc= walletBal.substring(walletBal.indexOf('-'));

                            mCategoryList.add(hm);
                            CustomAdapter adapter = new CustomAdapter(mCategoryList);
                            list.setAdapter(adapter);
                        }
                    }
                }catch(JSONException e){
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chik();
                sendDetailsToServer();
               // Toast.makeText(getActivity().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                sendDetailsToServer();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                convertView = inflater.inflate(R.layout.activity_prd2, null);
            }
            HashMap<String, String> hm=alData1.get(position);
            TextView tv=(TextView) convertView.findViewById(R.id.title);
            TextView tv1=(TextView) convertView.findViewById(R.id.artist);
           // TextView tv2=(TextView) convertView.findViewById(R.id.artist1);
           // TextView tv3=(TextView) convertView.findViewById(R.id.artist2);
            ImageView img=(ImageView) convertView.findViewById(R.id.list_image);

            tv.setText(hm.get("companyname"));
            tv1.setText(hm.get("productmodelname"));
           // tv2.setText(desc);
           // tv2.setText(hm.get("description"));
           // tv3.setText("Price: Rs."+hm.get("product_price"));
            Picasso.with(getActivity()).load(Constants.IMG_URL+hm.get("productmodelpics")).into(img);
            Log.e("Log1234e"," hm::::"+hm.get("productmodelpics"));

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
