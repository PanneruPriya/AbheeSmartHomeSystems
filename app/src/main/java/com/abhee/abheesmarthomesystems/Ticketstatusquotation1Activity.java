package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketstatusquotation1Activity extends Fragment {
    static String number;
    static int pos;
    HashMap<String, String> hm;
    ArrayList<HashMap<String, String>> mCategoryList;
    public static String custid,userid,mobile,modelname;
    SharedPreferences sharedPreferences;
    TextView tv1,tv2,tv3,address,duedate,tsq1,tsq2,tsqi,tsq3,tsq4,tsq5,tsq6,tsq7,tsq8,tsq9;
    ImageButton back_button;
    AppCompatTextView title;
    FragmentTransaction transaction;
    public static AppCompatTextView farname;
    AlertDialog dialog;
    ImageView imageViewt,adminimage;
    LinearLayout customer,admin;
    String image,image2;
    int width,heigth;
    ArrayList<HashMap<String,String>> adminlist;
    LinearLayout adminlistview;
    AppCompatTextView notes,datatime;
    AppCompatImageView adminimageview;

    public static Ticketstatusquotation1Activity newInstance( String rnum) {
        Ticketstatusquotation1Activity fragment = new Ticketstatusquotation1Activity();
        number=rnum;
        return fragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.ticketstatusquotation1, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        title=(AppCompatTextView)v.findViewById(R.id.title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        Ticketstatusquotation1Activity.farname.setText("Quotation Ticket Status Data");
        customer =(LinearLayout)v.findViewById(R.id.customerrequset);
        admin=(LinearLayout)v.findViewById(R.id.adminresponse);
        address=(TextView)v.findViewById(R.id.tv3);

        tsq1=(TextView)v.findViewById(R.id.tsq1);
        tsq2=(TextView)v.findViewById(R.id.tsq2);
        tsq3=(TextView)v.findViewById(R.id.tsq3);
        tsq4=(TextView)v.findViewById(R.id.tsq4);
        tsq5=(TextView)v.findViewById(R.id.tsq5);
        //tsq6=(TextView)v.findViewById(R.id.tsq6);
       /* notes = (AppCompatTextView) v.findViewById(R.id.notes);
        datatime = (AppCompatTextView) v.findViewById(R.id.dataandtime);
        adminimageview = (AppCompatImageView) v.findViewById(R.id.adminimageview);*/
        adminlistview=(LinearLayout) v.findViewById(R.id.adminlist);
        imageViewt=(ImageView)v.findViewById(R.id.imageviewt);
        //adminimage=(ImageView)v.findViewById(R.id.adminimage);

        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid", "");
        custid=sharedPreferences.getString("custid", "");
        mobile=sharedPreferences.getString("mobilenumber","");
        adminlist = new ArrayList<>();
        getDetailsToServer();
        imageViewt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getActivity()).load(Constants.IMG_URL+image).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
        /*adminimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imageviewdialog, null);
                *//*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*//*
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getActivity()).load(Constants.IMG_URL+image2).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                *//*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*//*
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
        });*/
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(),LoginActivity.class));
        return v;
    }
    private void getDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("salesrequestnumber",number);
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
                    String AdminResponseStatus= String.valueOf(json.getString("AdminResponseStatus"));
                    JSONArray jsonarray = json.getJSONArray("quotationslist");

                    for(int i=0; i<jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            customer.setVisibility(View.VISIBLE);
                            hm.put("modelname",obj.getString("modelname"));
                           hm.put("reqdesc",obj.getString("reqdesc"));
                            hm.put("address",obj.getString("address"));
                            hm.put("salesrequestnumber",obj.getString("salesrequestnumber"));
                            hm.put("created_time",obj.getString("created_time"));
                             tsq1.setText(obj.getString("modelname"));
                             tsq2.setText(obj.getString("reqdesc"));
                              tsq4.setText(obj.getString("address"));
                             tsq5.setText(obj.getString("salesrequestnumber"));
                             tsq3.setText(obj.getString("created_time"));
                        image=obj.getString("imgfiles");
                        Log.i("chikimage",""+image);
                        Picasso.with(getActivity()).load(Constants.IMG_URL+image).into(imageViewt);
                    }
                    if(AdminResponseStatus.equals("1")){
                        JSONArray jsonarray2 = json.getJSONArray("AdminResponseList");
                        admin.setVisibility(View.VISIBLE);
                        for (int i=0;i<jsonarray2.length();i++) {
                            JSONObject obj1 = jsonarray2.getJSONObject(i);
                               /* hm=new HashMap<String,String>();
                               hm.put("notes",obj1.getString("notes"));
                               hm.put("quotation_documents",obj1.getString("quotation_documents"));
                              tsq6.setText(obj1.getString("notes"));
                              image2=obj1.getString("quotation_documents");
                                Picasso.with(getActivity()).load(Constants.IMG_URL+image2).into(adminimage);*/
                            hm = new HashMap<>();
                            hm.put("updated_time",obj1.getString("updated_time"));
                            hm.put("filename",obj1.getString("filename"));
                            hm.put("notes",obj1.getString("notes"));
                            //adminlist.add(hm);
                            addfield(hm.get("updated_time"),hm.get("filename"),hm.get("notes"));
                        }

                    }
                    //ListviewAdapter1 adapter1 = new ListviewAdapter1(getActivity(),adminlist);
                    //adminlistview.setAdapter(adapter1);
                }catch(JSONException e){

                }
                Log.i("chiklist",mCategoryList.toString());
                Log.i("hmsize",""+hm.size());
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

    private void addfield(String updated_time,String filename,String notess) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.listview_row1, null);
        notes = (AppCompatTextView) rowView.findViewById(R.id.notes);
        datatime = (AppCompatTextView) rowView.findViewById(R.id.dataandtime);
        adminimageview = (AppCompatImageView) rowView.findViewById(R.id.adminimageview);
        notes.setText(notess);
        datatime.setText(updated_time);
        Log.i("testttt","tttttttttt");
        Picasso.with(getActivity()).load(Constants.IMG_URL+filename).into(adminimageview);
        adminlistview.addView(rowView, adminlistview.getChildCount() - 1);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
   /* public class ListviewAdapter1 extends BaseAdapter {
        public ArrayList<HashMap<String,String>> productList;
        Activity activity;

        public ListviewAdapter1(Activity activity, ArrayList<HashMap<String,String>> productList) {
            super();
            this.activity = activity;
            this.productList = productList;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            AppCompatTextView notes,datatime;
            AppCompatImageView adminimageview;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_row1, null);
                holder = new ViewHolder();
                holder.notes = (AppCompatTextView) convertView.findViewById(R.id.notes);
                holder.datatime = (AppCompatTextView) convertView.findViewById(R.id.dataandtime);
                holder.adminimageview = (AppCompatImageView) convertView.findViewById(R.id.adminimageview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String,String> item = productList.get(position);
            holder.notes.setText(item.get("notes"));
            holder.datatime.setText(item.get("updated_time"));
            Picasso.with(getActivity()).load(Constants.IMG_URL+hm.get("filename")).into(holder.adminimageview);
            return convertView;
        }
    }*/

}
