package com.abhee.abheesmarthomesystems;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class TicketStatus extends Fragment {

    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    FragmentTransaction transaction;
    ImageView quo,ser;
    public static TicketStatus newInstance() {
        TicketStatus fragment = new TicketStatus();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ticket_status, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        farname = (AppCompatTextView) view.findViewById(R.id.txtName);
        TicketStatus.farname.setText("Ticket Status" + "");

        quo = (ImageView) v.findViewById(R.id.quo);
        ser = (ImageView) v.findViewById(R.id.ser);
        quo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new TicketstatusquotationActivity().newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new TicketStatusActivity().newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                //Toast.makeText(getActivity(),"Updated soon",Toast.LENGTH_SHORT).show();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginActivity.class));
        return v;
    }
}



