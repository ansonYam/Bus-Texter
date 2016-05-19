package com.example.anson.bustexter2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Anson on 2016-03-27.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startButton = (Button) findViewById(R.id.startService);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startServiceIntent = new Intent(MainActivity.this, TopButton.class);
                startService(startServiceIntent);
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopService);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent stopServiceIntent = new Intent(MainActivity.this, TopButton.class);
                stopService(stopServiceIntent);
            }
        });

        Button modifyCommands = (Button) findViewById(R.id.modify);
        modifyCommands.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent listactivity = new Intent(MainActivity.this, listofCommands.class);
                startActivity(listactivity);
            }
        });
    }





    }
