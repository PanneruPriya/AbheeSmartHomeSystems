package com.abhee.abheesmarthomesystems.dialogbox;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abhee.abheesmarthomesystems.R;
import com.squareup.picasso.Picasso;

public class dialogbox {
    public static AlertDialog dialog;
     int width,heigth;
    AppCompatTextView message;
    AlertDialog.Builder alert;
    public static Activity activity;
    AppCompatButton submit,closes;
    LinearLayout msglayout,imglayout;

    public dialogbox(){
        super();
    }
    public void MessageBoxShow(Activity activity, String message){
        this.activity = activity;
        dialogbox(message);
        dialog.show();
        dialog.getWindow().setLayout(width/2+200,width/2);
    }
    public void ErrorMessage(Activity activity,String message){
        this.activity = activity;
        dialogbox2(message);
        dialog.show();
        dialog.getWindow().setLayout(width/2+200,width/2);
    }

    private void dialogbox2(String msg) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.imagedialog, null);
        final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
         ImageView imageView = (ImageView) alertLayout.findViewById(R.id.messageimage);
        Picasso.with(activity).load(R.drawable.error).into(imageView);
        message =(AppCompatTextView)alertLayout.findViewById(R.id.immssg);
        imglayout=(LinearLayout)alertLayout.findViewById(R.id.immsg);
        imglayout.setVisibility(View.VISIBLE);
        message.setText(msg);
        alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void SuccessMessage(Activity activity,String message){
        this.activity = activity;
        dialogbox3(message);
        dialog.show();
        dialog.getWindow().setLayout(width/2+200,width/2);
    }

    private void dialogbox3(String msg) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.imagedialog, null);
        final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
        ImageView imageView = (ImageView) alertLayout.findViewById(R.id.messageimage);
        Picasso.with(activity).load(R.drawable.checked).into(imageView);
        imglayout=(LinearLayout)alertLayout.findViewById(R.id.immsg);
        imglayout.setVisibility(View.VISIBLE);
        message =(AppCompatTextView)alertLayout.findViewById(R.id.immssg);
        message.setText(msg);
        alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void MessageBoxdialogclose(){
        dialog.dismiss();
    }
    public void prograssBoxShow(Activity activity)
    {
        this.activity = activity;
        prograssbox();
        dialog.show();
        dialog.getWindow().setLayout(500,200);
    }
    public void prograssBoxClose()
    {
        dialog.dismiss();
    }


    public void prograssbox()
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialogprograss, null);
        alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.getWindow().setLayout(width/2+200,width/2);
    }


    public void dialogbox(String msg)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.imagedialog, null);
        final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
        msglayout = (LinearLayout)alertLayout.findViewById(R.id.msg);
        msglayout.setVisibility(View.VISIBLE);
        message =(AppCompatTextView)alertLayout.findViewById(R.id.imageView2);
        message.setText(msg);
        alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}
