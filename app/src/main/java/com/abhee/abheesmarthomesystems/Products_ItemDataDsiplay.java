package com.abhee.abheesmarthomesystems;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class Products_ItemDataDsiplay extends Fragment {
    TextView tvPrd,tvTitle,tvTitle1,tvPrice,tvPrddis;
    TableLayout tlPrd;
    Button btnServ,btnQuot;
    String str;
    ImageView imgPrd;
    static String desca,imga,companya,modela,speca,price,companyid,categoryids;
    FragmentTransaction transaction;
    ImageButton back_button;
    AppCompatTextView title;
    public static AppCompatTextView farname;

    public static Products_ItemDataDsiplay newInstance(String desc, String company, String model, String img,
                                                       String spec, String product_price,String companyids,String categoryid) {
        Products_ItemDataDsiplay fragment = new Products_ItemDataDsiplay();
        imga=img;
        desca=desc;
        companya=company;
        modela=model;
        speca=spec;
        price=product_price;
        companyid=companyids;
        categoryids= categoryid;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.products_itemdatadsiplay, container, false);
        //  back_button=(ImageButton)v.findViewById(R.id.back_button);
        //title=(AppCompatTextView)v.findViewById(R.id.title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view =((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        Products_ItemDataDsiplay.farname.setText(""+companya+" ");
       // title.setText(""+companya+"  ");
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
        tvPrd=(TextView)v.findViewById(R.id.tvPrd);
        tvTitle=(TextView)v.findViewById(R.id.tvTitle);
        tvTitle1=(TextView)v.findViewById(R.id.tvTitle1);
        //tvPrice=(TextView)v.findViewById(R.id.tvPrice);
        imgPrd=(ImageView) v.findViewById(R.id.imgPrd);
        btnQuot=(Button)v.findViewById(R.id.btnQuot);
        btnQuot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Get a Quotation here",Toast.LENGTH_SHORT).show();
                transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Product_GetQuotation1().newInstance(imga,companya,modela,categoryids));
                transaction.addToBackStack(null);
                transaction.commit();
               /*Intent in=new Intent(getActivity(),Map1Activity.class);
                startActivity(in);*/
            }
        });
        btnServ=(Button)v.findViewById(R.id.btnServ);
        btnServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Product_ServiceRequest().newInstance(imga,companya,modela,companyid));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tvTitle.setText(companya);
        tvTitle1.setText(modela);
       /*String text4 ="<font COLOR=\'#E51009\'>" + "Subject: "+ "\n" + "</font>"
                + "<font COLOR=\'BLACK\'>" + desca + "</font>"
                + " ";
        tvPrd.setText(Html.fromHtml(text4));*/
        tvPrd.setText("Specifications:-"+"\n\n"+desca+"\n\n"+"Description:-"+"\n\n"+speca);
        Picasso.with(getActivity()).load(Constants.IMG_URL+imga).into(imgPrd);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(),LoginActivity.class));

        return v;
    }
}
