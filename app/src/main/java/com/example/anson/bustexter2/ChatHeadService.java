package com.example.anson.bustexter2;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


// this code is currently not being used but serves as an example to set up a chathead
public class ChatHeadService extends Service {

    /*WindowManager is a service on the phone that handles things that are on the screen such as the different icons and the taskbar n top
    In this case it is used to update where the chathead is when the user drags it around
    */
    private WindowManager windowManager;

    /*
    This imageview is used to set what touching the icon does and sets the actual pic for the icon
     */
    private ImageView chatHead;

    /*
    The parameters used by the windowmanager to figure out how big the chathead should be and where it is located
     */
    WindowManager.LayoutParams params;


    //This is a function within service that we are not using
    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        //set windowmanager to the one that controls the display outside of app
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //initialize chathead witht eh ic_launcher logo
        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.ic_launcher);

        // set the parameters used by chat head
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

//this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
        });

        //add the chatlhead to the display of windowmanager
        windowManager.addView(chatHead, params);
    }


    // when the service is closed, remove the chathead as well
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}

