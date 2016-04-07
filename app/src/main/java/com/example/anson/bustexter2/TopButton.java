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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.view.GestureDetector;


/**
 * Created by Anson on 2016-03-27.
 */
public class TopButton extends Service {
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;

    String textTransLink = "33333";

    String UBC49 = "59275";
    String oak49 = "52037";
    String oak41 = "50805";
    String hospital = "51479";


    @Override
    public void onCreate() {
        super.onCreate();

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

        /*
        chatHead.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getBaseContext(), view,
                        Gravity.START | Gravity.CENTER_HORIZONTAL);

                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());


                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                                                     //@Override
                                                     public boolean onMenuItemClick(MenuItem item) {
                                                         android.telephony.SmsManager smsManager = SmsManager.getDefault();

                                                         switch (item.getItemId()) {
                                                             case R.id.UBC49:
                                                                 smsManager.sendTextMessage(textTransLink, null, UBC49, null, null);
                                                                 return true;

                                                             case R.id.hospital:
                                                                 smsManager.sendTextMessage(textTransLink, null, hospital, null, null);
                                                                 return true;

                                                             case R.id.oak41:
                                                                 smsManager.sendTextMessage(textTransLink, null, oak41, null, null);
                                                                 return true;

                                                             case R.id.oak49:
                                                                 smsManager.sendTextMessage(textTransLink, null, oak49, null, null);
                                                                 return true;

                                                             default:
                                                                 return true;
                                                         }
                                                     }
                                                 }

                );

                popup.show();
            }
        });
*/
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

                    popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());


                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                                                         //@Override
                                                         public boolean onMenuItemClick(MenuItem item) {
                                                             android.telephony.SmsManager smsManager = SmsManager.getDefault();

                                                             switch (item.getItemId()) {
                                                                 case R.id.UBC49:
                                                                     smsManager.sendTextMessage(textTransLink, null, UBC49, null, null);
                                                                     return true;

                                                                 case R.id.hospital:
                                                                     smsManager.sendTextMessage(textTransLink, null, hospital, null, null);
                                                                     return true;

                                                                 case R.id.oak41:
                                                                     smsManager.sendTextMessage(textTransLink, null, oak41, null, null);
                                                                     return true;

                                                                 case R.id.oak49:
                                                                     smsManager.sendTextMessage(textTransLink, null, oak49, null, null);
                                                                     return true;

                                                                 default:
                                                                     return true;
                                                             }
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