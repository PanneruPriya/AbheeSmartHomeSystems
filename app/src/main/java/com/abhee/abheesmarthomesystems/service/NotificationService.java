package com.abhee.abheesmarthomesystems.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.abhee.abheesmarthomesystems.Constants;
import com.abhee.abheesmarthomesystems.NotificationReceiverActivity;
import com.abhee.abheesmarthomesystems.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class NotificationService extends Service {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String custid=null;
    public NotificationService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = this.getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        custid= sharedPreferences.getString("custid", "");
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Map<String,String> params = new HashMap<>();
        params.put("assignby",custid);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"getNotificationByCustomerId", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("noti",response.toString());
                        try {
                            int count;
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("History");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                String kstatus= jsonObject1.getString("kstatus");
                                String addComment = jsonObject1.getString("addComment");
                                Intent intent1 = new Intent(getApplicationContext(), NotificationReceiverActivity.class);
                                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent1, 0);
                                Notification noti = new Notification.Builder(getApplicationContext())
                                        .setContentTitle(kstatus)
                                        .setContentText(addComment).setSmallIcon(R.drawable.icon)
                                        .setContentIntent(pIntent).build();
                                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                // hide the notification after its selected
                                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                                notificationManager.notify(i, noti);
                                count =i;
                            }
                            editor.putString("noticount",""+jsonArray.length());
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        /*Intent intent1 = new Intent(getApplicationContext(), NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent1, 0);
        Notification noti = new Notification.Builder(getApplicationContext())
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        //notificationManager.notify(0, noti);*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(getApplicationContext(),NotificationService.class));
                    //Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
                }
            }, 45000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
