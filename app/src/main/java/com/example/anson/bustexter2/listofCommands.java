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

public class listofCommands extends ListActivity {

    // this textView is currently not being used, was used earlier for debugging
    private TextView text;

    // This is the button on the bottom of the screen, to be initialized in oncreate()
    private Button addcontent;

    // This list stores the titles that are in the local file, it is used to add the titles to the display
    private List<String> listValues;

    // This stores the data that is being uploaded or downloaded from the local file
    //NOTE: This list is updated everytime the file is modified so that the file data can be retrived quickly
    private ArrayList<JSONObject> data;

    // the name of the local file
    String FILENAME = "DATA_STORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);


        // again the now defunct debug text that is not being used
        text = (TextView) findViewById(R.id.debugText);

        // associates the button with the button in the xml
        addcontent = (Button) findViewById(R.id.addcontent);

        // intializing the list of string values
        listValues = new ArrayList<String>();


        // attempt to get data form the file and upload it to the list
        try {
            data = getJSONarray(FILENAME, getApplicationContext());
            populateList(listValues, data);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        /* This onClick creates the popup window that allows you to edit the contents of the file, after the edit is finished it will update the list
        and view of the window
        */
        addcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This is a service that you need to have to create a pop up
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);

                // This initializes a view that uses the format of the layout file popup_addcontent
                View popupView = layoutInflater.inflate(R.layout.popup_addcontent, null);

                // Add the view to a popupwindow, this is necessary because the popupwindow class has functions ot update and close the view after
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);

                // identify all the buttons and texts that are in the xml file and match them to the
                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                Button btnSubmit = (Button) popupView.findViewById(R.id.submit);
                final EditText labeltext = (EditText) popupView.findViewById(R.id.labelfield);
                final EditText numbertext = (EditText) popupView.findViewById(R.id.numberfield);
                final EditText messagetext = (EditText) popupView.findViewById(R.id.messagefield);

                // this makes it so the keyboard can be brought out while on this window
                popupWindow.setFocusable(true);

                // add the popupwindow to the screen
                popupWindow.update();

                // Dismiss the popup when this button is hit
                btnDismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                //Adds the data to the file when this button is pressed
                btnSubmit.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // this if statement checks if all the edittext fields have been filled
                        // need to improve this so it detects if the number is valid in the number field
                        if (labeltext.getText().toString().equals("") | numbertext.getText().toString().equals("") | messagetext.getText().toString().equals(""))
                            Toast.makeText(listofCommands.this, "Error: One of the above fields isn't filled", Toast.LENGTH_LONG).show();

                        // if entry is valid, then
                        else {

                            // create the JSONObject and store the data in the edittexts
                            JSONObject newData = new JSONObject();
                            try {
                                newData.put("label", labeltext.getText().toString());
                                newData.put("number", numbertext.getText().toString());
                                newData.put("message", messagetext.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // open the file and append the new JSONobject to it after converting the JSONObject to a string
                            try {
                                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                                fos.write(newData.toString().getBytes());
                                fos.write("\n".getBytes());
                                fos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // uses populatelist to update the list being displayed in app
                            try {
                                data = getJSONarray(FILENAME, getApplicationContext());
                                populateList(listValues, data);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                            // notification in toast and close the popup
                            Toast.makeText(listofCommands.this, "Success, sequence added", Toast.LENGTH_LONG).show();
                            popupWindow.dismiss();
                        }
                    }
                });

                // this decided
                popupWindow.showAsDropDown(findViewById(R.id.listcommands), 10, -1000, Gravity.CENTER_HORIZONTAL);

            }
        });

    }

    // this function decides what happens when a member of the list being displayed is clicked
    @Override
    protected void onListItemClick(ListView list, View view, final int position, long id) {
        super.onListItemClick(list, view, position, id);

// this was used to debug before but is now useless, but the function is how to get the item that's being pressed
        final String selectedItem = (String) getListView().getItemAtPosition(position);

        // same as before, just standard stuff with popupwindows
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


        // this uses the getdata function to find the data associated with the list item
        JSONObject listdata;
        try {
            listdata = getdata(position);
            label.setText("Label: " + listdata.get("label").toString());
            message.setText("message: " + listdata.get("message").toString());
            number.setText("number: " + listdata.get("number").toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        // Deletes the label and associated data using removefromfile func. , refreshes list after completion
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

        // dismiss popupwindow when cancle button is pressed
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        // show the popup
        popupWindow.showAsDropDown(findViewById(R.id.listcommands), 10, -1000, Gravity.CENTER_HORIZONTAL);
    }



    /* Returns a list of JSONObjects: JSON is a method of storing data, http://developers.squarespace.com/what-is-json/

       How it works: The file contains txt that corresponds to a group of JSONObjects, this function translates said text to String,
       Then puts them into JSONObject Objects. These objects are then put into a list to be returned
     */
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


    /* This function adds the labels of the JSONObject into another list, with the purpose of using this list as the viewAdapter(Meaning the phone will display elements in the list)

    How it works: A loop obtains the label tag of the JSONObjects and puts them into a list of strings, this list is then set as the listadapter
     */

    private void populateList(List<String> populatee, ArrayList<JSONObject> populater) throws JSONException {
        populatee.clear();
        for (int i = 0; i < populater.size(); i++) {

            populatee.add((String) populater.get(i).get("label"));
        }

        ArrayAdapter<String> viewAdapter = new ArrayAdapter<String>(this, R.layout.list_content, R.id.listText, listValues);

        setListAdapter(viewAdapter);
    }


    /* Removes the xth element from the file,

    How it works: Get the file data and put them into a list, remove the xth element from that list and put the list back into the file
     */
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

    // gets the data from the given position, since data is always refreshed everytime the table changes, this should be up to date
    private JSONObject getdata(int position) throws IOException, JSONException {
        return data.get(position);
    }
}