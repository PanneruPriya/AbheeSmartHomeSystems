package com.abhee.abheesmarthomesystems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context activity;
    private final Class<?> myActivityClass;
    public MyExceptionHandler(Context context,Class<?> c){
        activity = context;
        myActivityClass =c;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);

        Intent intent = new Intent(activity,myActivityClass);
        intent.putExtra("UnaughtException","Exception: "+stackTrace.toString());
        intent.putExtra("stackTrace","s");
        activity.startActivity(intent);

        Process.killProcess(Process.myPid());
        System.exit(0);

    }
}
