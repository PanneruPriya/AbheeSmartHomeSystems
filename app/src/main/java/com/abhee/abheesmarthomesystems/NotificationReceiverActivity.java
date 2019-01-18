package com.abhee.abheesmarthomesystems;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationReceiverActivity extends AppCompatActivity {
    AppCompatTextView titles;
    AppCompatImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationresult);
        Intent intent = getIntent();
        String requsettype= intent.getStringExtra("Quotation");
        String taskno = intent.getStringExtra("taskno");
        String custid = intent.getStringExtra("customerid");
         back=(AppCompatImageButton)findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });
        titles=(AppCompatTextView)findViewById(R.id.noticTitle);
        titles.setText(requsettype +" Notification");

    }

}
