package com.example.anson.bustexter2;


import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by David on 2016-05-06.
 */
public class listofCommands extends ListActivity {

    private TextView text;
    private Button addcontent;
    private List<String> listValues;
    private ArrayList<JSONObject> data;
    String FILENAME = "DATA_STORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        text = (TextView) findViewById(R.id.debugText);
        addcontent = (Button) findViewById(R.id.addcontent);

        listValues = new ArrayList<String>();

        try {
            data = getJSONarray(FILENAME, getApplicationContext());
            populateList(listValues, data);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        addcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_addcontent, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                Button btnSubmit = (Button) popupView.findViewById(R.id.submit);
                final EditText labeltext = (EditText) popupView.findViewById(R.id.labelfield);
                final EditText numbertext = (EditText) popupView.findViewById(R.id.numberfield);
                final EditText messagetext = (EditText) popupView.findViewById(R.id.messagefield);
                popupWindow.setFocusable(true);
                popupWindow.update();
                btnDismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                btnSubmit.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (labeltext.getText().toString().equals("") | numbertext.getText().toString().equals("") | messagetext.getText().toString().equals(""))
                            Toast.makeText(listofCommands.this, "Error: One of the above fields isn't filled", Toast.LENGTH_LONG).show();

                        else {
                            JSONObject newData = new JSONObject();
                            try {
                                newData.put("label", labeltext.getText().toString());
                                newData.put("number", numbertext.getText().toString());
                                newData.put("message", messagetext.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                                fos.write(newData.toString().getBytes());
                                fos.write("\n".getBytes());
                                fos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                data = getJSONarray(FILENAME, getApplicationContext());
                                populateList(listValues, data);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(listofCommands.this, "Success, sequence added", Toast.LENGTH_LONG).show();
                            popupWindow.dismiss();
                        }
                    }
                });

                popupWindow.showAsDropDown(findViewById(R.id.listcommands), 10, -1000, Gravity.CENTER_HORIZONTAL);

            }
        });

    }

    @Override
    protected void onListItemClick(ListView list, View view, final int position, long id) {
        super.onListItemClick(list, view, position, id);

        final String selectedItem = (String) getListView().getItemAtPosition(position);

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_editcontent, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        Button btnDelete = (Button) popupView.findViewById(R.id.delete);
        Button btncancel = (Button) popupView.findViewById(R.id.cancel);
        TextView label = (TextView) popupView.findViewById(R.id.labelfield);
        TextView message = (TextView) popupView.findViewById(R.id.messagefield);
        TextView number = (TextView) popupView.findViewById(R.id.numberfield);

        JSONObject listdata;
        try {
            listdata = getdata(position);
            label.setText("Label: " + listdata.get("label").toString());
            message.setText("message: " + listdata.get("message").toString());
            number.setText("number: " + listdata.get("number").toString());
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removefromfile(position);
                    data = getJSONarray(FILENAME, getApplicationContext());
                    populateList(listValues, data);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

            }
        });

        popupWindow.showAsDropDown(findViewById(R.id.listcommands), 10, -1000, Gravity.CENTER_HORIZONTAL);
    }

    public static ArrayList<JSONObject> getJSONarray(String filename, Context context) throws IOException, JSONException {
        ArrayList<JSONObject> dataarray = new ArrayList<JSONObject>();
        InputStream ReadFile;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        ReadFile = context.openFileInput(filename);
        inputStreamReader = new InputStreamReader(ReadFile);
        bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        int i = 0;

        while ((line = bufferedReader.readLine()) != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(line);

            String word = sb.toString();

            dataarray.add(new JSONObject(word));
            i++;
        }
        return dataarray;
    }

    private void populateList(List<String> populatee, ArrayList<JSONObject> populater) throws JSONException {
        populatee.clear();
        for (int i = 0; i < populater.size(); i++) {

            populatee.add((String) populater.get(i).get("label"));
        }

        ArrayAdapter<String> viewAdapter = new ArrayAdapter<String>(this, R.layout.list_content, R.id.listText, listValues);

        setListAdapter(viewAdapter);
    }

    private void removefromfile(int position) throws IOException, JSONException {

        ArrayList<JSONObject> data = getJSONarray(FILENAME, getApplicationContext());
        data.remove(position);

        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        for (int i = 0; i < data.size(); i++) {
            JSONObject newData = data.get(i);
            fos.write(newData.toString().getBytes());
            fos.write("\n".getBytes());


        }
        fos.close();
    }

    private JSONObject getdata(int position) throws IOException, JSONException {
        return data.get(position);
    }
}