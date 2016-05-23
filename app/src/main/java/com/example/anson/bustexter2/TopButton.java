package com.example.anson.bustexter2;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.view.GestureDetector;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

//How the chathead works is explained in the comments of chatheadservice
// only comments here are about how the file data is read and processed

public class TopButton extends Service {
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;

    private ArrayList<JSONObject> filedata;
    String FILENAME = "DATA_STORE";

    @Override
    public void onCreate() {
        super.onCreate();

// When the chathead service is started, retrieve the data in the file, you can read about this function in listofCommands
        try {
            filedata = listofCommands.getJSONarray(FILENAME, getApplicationContext());
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.ic_launcher);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

     chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    PopupMenu popup = new PopupMenu(getBaseContext(), v,
                            Gravity.START | Gravity.CENTER_HORIZONTAL);

                    Menu menu = popup.getMenu();

                    // for every element in the list, find the label and add them to the menu
                    for(int i = 0; i < filedata.size(); i++){
                        try {
                            menu.add(1, i, i, filedata.get(i).get("label").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    popup.getMenuInflater().inflate(R.menu.menu_main, menu);

                    // When an element of the table is tapped, get the number and message from filedata and send it using smsManager

                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                                                         //@Override
                                                         public boolean onMenuItemClick(MenuItem item) {
                                                             android.telephony.SmsManager smsManager = SmsManager.getDefault();

                                                             //Toast.makeText(getApplicationContext(), String.valueOf(item.getItemId()),Toast.LENGTH_LONG).show();
                                                             JSONObject content = filedata.get(item.getItemId());

                                                             try {
                                                                 smsManager.sendTextMessage((String)content.get("number"), null, (String)content.get("message"), null, null);
                                                             } catch (JSONException e) {
                                                                 e.printStackTrace();
                                                             }


                                                             return true;
                                                         }
                                                     }

                    );

                    popup.show();
                    return true;
                } else {
                    // your code for move and drag
                    switch (event.getAction())

                    {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, params);
                            return true;
                    }

                    return false;
                }
            }
        });

        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }

    //magic that removes an error, don't touch
    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}