package com.abhee.abheesmarthomesystems;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.abhee.abheesmarthomesystems.Products_Companies.position;

public class ContentDashboard extends Fragment implements  BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL;
    HashMap<String, Integer> HashMapForLocalRes;
    TextView user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edt;
    FragmentManager manager;
    LinearLayout lin1,  lin3, lin4, lin5;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    HLVAdapter mAdapter;
    ArrayList<String> alName;
    ArrayList<String> alImage;
    ArrayList<String> cid;
    FragmentTransaction transaction;
    HashMap<String, String> hm;
    ArrayList<HashMap<String, String>> mCategoryLists;
    public static String modelid,cateid;
    Display display;
    int width;
    int height;
    ProgressDialog progressDialog;
    AlertDialog dialog;

    public static AppCompatTextView farname;
    RelativeLayout top_layout;
    private static AdapterView.OnItemClickListener mOnItemClickListener;

    private AdapterView.OnItemClickListener onItemClickListener;

    public static ContentDashboard newInstance() {
        ContentDashboard fragment = new ContentDashboard();
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
        View v;
        v = inflater.inflate(R.layout.content_dashboard, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        ContentDashboard.farname.setText("Customer Dashboard"+"");
        progressDialog = new ProgressDialog(getActivity());
        display = getActivity().getWindowManager().getDefaultDisplay();
        LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.l1);
        ViewGroup.LayoutParams params1 = linearLayout.getLayoutParams();
        int width = display.getWidth();
        int height = display.getHeight();
        params1.width= width/3;
        params1.height = height/4;
        linearLayout.setLayoutParams(params1);

        sliderLayout = (SliderLayout)v.findViewById(R.id.slider);
        ViewGroup.LayoutParams params=sliderLayout.getLayoutParams();
        params.width=width;
        params.height=height/3;
        sliderLayout.setLayoutParams(params);

        alName = new ArrayList<>();
        alImage = new ArrayList<>();
        cid = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        String fname = sharedPreferences.getString("fname", "");
        String lname = sharedPreferences.getString("lname", "");
        edt = sharedPreferences.edit();
       /* Intent intent = new Intent(getActivity(), NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);
        Notification noti = new Notification.Builder(getActivity())
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
              *//*  .addAction(R.drawable.icon, "Call", pIntent)
                .addAction(R.drawable.icon, "More", pIntent)
                .addAction(R.drawable.icon, "And more", pIntent).build();
     *//*   NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
*/

        if (fname.length() > 0) {
           /* user = (TextView) v.findViewById(R.id.txt_username);
            user.setText("Welcome \n" + fname + " " + lname);*/

            lin1 = (LinearLayout) v.findViewById(R.id.linear1);
            ViewGroup.LayoutParams layoutParams=lin1.getLayoutParams();
            layoutParams.width=width/3;
            layoutParams.height=height/4-40;
            lin1.setLayoutParams(layoutParams);
            lin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new Products_Names().newInstance());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack("tag");
                    transaction.commit();
                }
            });
            lin3 = (LinearLayout) v.findViewById(R.id.linear2);
            ViewGroup.LayoutParams layoutParams1=lin3.getLayoutParams();
            layoutParams1.width=width/3;
            layoutParams1.height=height/4-40;
            lin3.setLayoutParams(layoutParams1);
            lin3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new TicketStatus().newInstance());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack("tag");
                    transaction.commit();
                }
            });
            lin4 = (LinearLayout) v.findViewById(R.id.linear3);
            ViewGroup.LayoutParams layoutParams2=lin4.getLayoutParams();
            layoutParams2.width=width/3;
            layoutParams2.height=height/4-40;
            lin4.setLayoutParams(layoutParams2);
            lin4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new ServiceRequestActivity().newInstance());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack("tag");
                    transaction.commit();
                }
            });
            lin5 = (LinearLayout) v.findViewById(R.id.linear4);
            ViewGroup.LayoutParams layoutParams3=lin5.getLayoutParams();
            layoutParams3.width=width/3;
            layoutParams3.height=height/4-40;
            lin5.setLayoutParams(layoutParams3);
            lin5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new EnquiryActivity().newInstance());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack("tag");
                    transaction.commit();
                }
            });
            }
            AddImageUrlFormLocalRes();
            for (String name : HashMapForLocalRes.keySet()) {
                TextSliderView textSliderView = new TextSliderView(getActivity().getApplicationContext());
                textSliderView.description(name)
                        .image(HashMapForLocalRes.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(2000);
            sliderLayout.addOnPageChangeListener(this);

            mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            ViewGroup.LayoutParams params2=mRecyclerView.getLayoutParams();
            params2.width=width;
            params2.height=height/4-40;
            mRecyclerView.setLayoutParams(params2);
            mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(),Products_Companies.class));
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sendDetailsToServer();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }

    private void sendDetailsToServer() {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        alImage.clear();
        alName.clear();
        cid.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL +"getcategories", new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Log1", " response" + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("categorieslist");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String category = jsonObject1.getString("category");
                        String img = jsonObject1.getString("categoryimg");
                        String id = String.valueOf(jsonObject1.getInt("id"));
                        //Log.i("category", category);
                        //Log.i("img", img);
                        alName.add(category);
                        alImage.add(img);
                        cid.add(id);
                    }

                    Log.i("id",cid.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                mAdapter = new HLVAdapter(getActivity().getApplicationContext(), alName, alImage,width,height);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chik();
                sendDetailsToServer();
                Log.i("alert","chik");
               // Toast.makeText(getActivity().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    public void AddImageUrlFormLocalRes(){
        HashMapForLocalRes = new HashMap<String, Integer>();
        HashMapForLocalRes.put("Home Theaters", R.drawable.slide_04);
        HashMapForLocalRes.put("Professional Audios", R.drawable.paa);
        HashMapForLocalRes.put("Wooden Flooring", R.drawable.flooring);
        HashMapForLocalRes.put("Security Camers", R.drawable.security);
        HashMapForLocalRes.put("Remote gates", R.drawable.remote);
        HashMapForLocalRes.put("Projectors", R.drawable.proj);
        HashMapForLocalRes.put("Solar Fencing", R.drawable.solar);
        HashMapForLocalRes.put("Car Accessories", R.drawable.car);
        HashMapForLocalRes.put("Artifical Grass", R.drawable.grass);
    }
    public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder>  {
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        ArrayList<String> alName;
        ArrayList<String> alImage;
        Context context;
        int width;
        int height;
        public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<String> alImage,int width,int height) {
            super();
            this.context = context;
            this.alName = alName;
            this.alImage = alImage;
            this.width=width;
            this.height=height;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.tvSpecies.setText(alName.get(i));
            //viewHolder.imgThumbnail.setImageResource(alImage.get(i));
            Picasso.with(context).load(Constants.IMG_URL+alImage.get(i)).into(viewHolder.imgThumbnail);
            display = getActivity().getWindowManager().getDefaultDisplay();
            ViewGroup.LayoutParams layoutParams = viewHolder.relativeLayout.getLayoutParams();
            int width = display.getWidth();
            int height = display.getHeight();
            layoutParams.width= width/3;        // params1.width=width
            layoutParams.height = height/4-40;     // params1.height=height/3+1234
            viewHolder.relativeLayout.setLayoutParams(layoutParams);
            viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity()," position"+alName.get(i).toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(),"position"+alImage.get(i),Toast.LENGTH_LONG).show();
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new Products_Companies().newInstance(position,alName.get(i).toString(),cid.get(i)));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
           /* viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                       hm=mCategoryLists.get(position);
                        modelid=hm.get("id");
                        cateid=hm.get("category");
                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, new Products_Companies().newInstance(position,hm.get("title").toString()));
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Toast.makeText(context, "P" + position + " - " + alName.get(position) , Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, DashboardActivity.class));
                       }
                }
            });*/
        }
        @Override
        public int getItemCount() {
            return alName.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder  {
            public ImageView imgThumbnail;
            public TextView tvSpecies;
            private ItemClickListener clickListener;
            public RelativeLayout relativeLayout;
            public ViewHolder(View itemView) {
                super(itemView);
             /*   Picasso.with(getActivity()).load(Constants.IMG_URL+hm.get("url")).into(img);*/
                relativeLayout=(RelativeLayout) itemView.findViewById(R.id.top_layout);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
               /* mRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hm=mCategoryLists.get(position);
                        modelid=hm.get("id");
                        cateid=hm.get("category");
                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, new Products_Companies().newInstance(position,hm.get("title").toString()));
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Toast.makeText(context, "P" + position + " - " + alName.get(position) , Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, DashboardActivity.class));
                    }
                });*/
                }
            public void setClickListener(ItemClickListener itemClickListener) {
               this.clickListener = itemClickListener;
            }
        }
        private class OnRecyclerViewItemClickListener {
        }
        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedPreferences.getString("ddd","").equals("0")){
            edt.putString("ddd","1");
            edt.commit();
            startActivity(new Intent(getActivity(),DashboardActivity.class));
        }
    }
    public void chik(){
        /*AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert.setView(R.layout.alertlayout1);
        }
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();*/
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertlayout1, null);
        final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
        // userInput.setText(code);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.rbutton);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Please Reload");
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