package com.example.anson.bustexter2;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;


/**
 * Created by Anson on 2016-03-27.
 */
public class TopButton extends Service{
    private WindowManager windowManager;
    private ImageView chatHead;

    String textTransLink = "33333";

    String UBC49 = "59275";
    String oak49 = "52037";
    String oak41 = "50805";
    String hospital = "51479";

    //magic that removes an error, don't touch
    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.ic_launcher);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        params.x = 0;
        params.y = 0;

        //texts TransLink, right now only the 49 from UBC. update to other common bus numbers
        chatHead.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(getBaseContext(), view,
                        Gravity.START | Gravity.CENTER_HORIZONTAL);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                /** Defining menu item click listener for the popup menu */
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

                /** Showing the popup menu */
                popup.show();
            }
        });

        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}