package com.example.searchApplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public Button home_submit;
    public Button home_clear;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    public String lat = "";
    public String lon = "";
    public String cat = "";
    public String unit = "";
    public String distance = "";
    public String keyword = "";
    public Spinner home_spinner_cat;
    public Spinner home_spinner_unit;
    public EditText obj_keyword;
    public EditText obj_distance;
    public EditText obj_location;
    public RadioGroup home_radio_location;
    public RadioButton home_radio_curr;
    public RadioButton home_radio_oth;
    public String[] categories_value;
    public String[] units_value;
    public TextView errorone;
    public TextView errortwo;

    public Boolean validationFlag = true;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        autoCompleteConfig(view);
        home_spinner_cat = view.findViewById(R.id.home_spinner_cat);
        home_spinner_unit = view.findViewById(R.id.home_spinner_unit);
        obj_keyword = view.findViewById(R.id.home_edit_keyword);
        obj_distance = view.findViewById(R.id.home_edit_distance);
        home_radio_location = view.findViewById(R.id.home_radio_location);
        obj_location = view.findViewById(R.id.home_edit_location);
        home_radio_curr = view.findViewById(R.id.home_radio_current);
        home_radio_oth = view.findViewById(R.id.home_radio_others);
        home_submit = (Button) view.findViewById(R.id.home_submit);
        errorone = view.findViewById(R.id.error_one);
        errortwo = view.findViewById(R.id.error_two);
        errorone.setVisibility(View.GONE);
        errortwo.setVisibility(View.GONE);
        obj_location.setEnabled(false);



        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    0);
        }
        String[] categories = new String[]{"All", "Music", "Sports", "Arts/Theatre", "Film", "Misc"};
        categories_value = new String[]{"", "KZFzniwnSyZfZ7v7nJ", "KZFzniwnSyZfZ7v7nE", "KZFzniwnSyZfZ7v7na", "KZFzniwnSyZfZ7v7nn", "KZFzniwnSyZfZ7v7n1"};
        ArrayAdapter<String> adapter_categories = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        home_spinner_cat.setAdapter(adapter_categories);

        String[] units = new String[]{"Miles", "Kilometres"};
        units_value = new String[]{"miles", "km"};
        ArrayAdapter<String> adapter_units = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, units);
        home_spinner_unit.setAdapter(adapter_units);



        home_radio_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(home_radio_oth.isChecked())
                {
                    obj_location.setEnabled(true);
                }
                else
                    obj_location.setEnabled(false);
            }
        });
        home_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cat = home_spinner_cat.getSelectedItem().toString();
                unit = home_spinner_unit.getSelectedItem().toString();
                validateInputs();
                if (validationFlag) {
                    if (obj_distance.getText().toString().equals("")) {
                        distance = "10";
                    } else {
                        distance = obj_distance.getText().toString();
                    }

                    if (home_radio_oth.isChecked()) {
                        makeLocCall(ApplicationConstants.otherLoc + "?otherLocation=" + obj_location.getText());
                    }
                    if (home_radio_curr.isChecked()) {
                        Location current = getLastBestLocation();
                        lat = Double.toString(current.getLatitude());
                        lon = Double.toString(current.getLongitude());
                        if (categories_value[home_spinner_cat.getSelectedItemPosition()] == "") {
                            makeSearchCall(ApplicationConstants.searchURL
                                            + "?keyword=" + obj_keyword.getText() +
//                                "&segmentId=" + categories_value[home_spinner_cat.getSelectedItemPosition()] +
                                            "&distance=" + distance +
                                            "&distanceUnit=" + units_value[home_spinner_unit.getSelectedItemPosition()] +
                                            "&latitude=" + lat +
                                            "&longitude=" + lon

                            );

                        } else {

                            makeSearchCall(ApplicationConstants.searchURL
                                    + "?keyword=" + obj_keyword.getText() +
                                    "&segmentId=" + categories_value[home_spinner_cat.getSelectedItemPosition()] +
                                    "&distance=" + distance +
                                    "&distanceUnit=" + units_value[home_spinner_unit.getSelectedItemPosition()] +
                                    "&latitude=" + lat +
                                    "&longitude=" + lon
                            );
                        }
                    }


                }
            }
        });

        home_clear = (Button) view.findViewById(R.id.home_clear);
        home_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearInputs();
            }
        });

        return view;
    }

    public void autoCompleteConfig(View view) {
        final AppCompatAutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.home_edit_keyword);

        autoSuggestAdapter = new AutoSuggestAdapter(this.getContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//                        selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(ApplicationConstants.autokeyURL + autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    public void validateInputs() {
        validationFlag = true;
        if (TextUtils.isEmpty(obj_keyword.getText())) {
//            obj_keyword.setError(getContext().getString(R.string.mandatory));
            errorone.setVisibility(View.VISIBLE);
            validationFlag = false;
            Toast.makeText(getActivity(), "Please fix all fields with error",
                    Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(obj_location.getText()) && home_radio_oth.isChecked()) {
            errortwo.setVisibility(View.VISIBLE);
            validationFlag = false;
            Toast.makeText(getActivity(), "Please fix all fields with error",
                    Toast.LENGTH_LONG).show();
        }
        if (!TextUtils.isEmpty(obj_location.getText()) && home_radio_oth.isChecked()) {
            errortwo.setVisibility(View.GONE);
        }


    }

    public void clearInputs() {

        obj_keyword.setText("");
//        obj_keyword.setError(null);
        obj_distance.setText("");
        home_radio_curr.setChecked(true);
        obj_location.setText("");
//        obj_location.setError(null);
        obj_location.setEnabled(false);
        errorone.setVisibility(View.GONE);
        errortwo.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
    }

    private void makeLocCall(String url) {
        ApiCall.make(this.getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement

                try {
                    JSONObject responseForLocation = new JSONObject(response);
                    JSONArray results = (JSONArray) responseForLocation.get("results");
                    JSONObject geometry = (JSONObject) results.getJSONObject(0).get("geometry");
                    JSONObject location = (JSONObject) geometry.get("location");
                    lat = location.get("lat").toString();
                    lon = location.get("lng").toString();
                    if (categories_value[home_spinner_cat.getSelectedItemPosition()] == "") {
                        makeSearchCall(ApplicationConstants.searchURL
                                        + "?keyword=" + obj_keyword.getText() +
//                                "&segmentId=" + categories_value[home_spinner_cat.getSelectedItemPosition()] +
                                        "&distance=" + distance +
                                        "&distanceUnit=" + units_value[home_spinner_unit.getSelectedItemPosition()] +
                                        "&latitude=" + lat +
                                        "&longitude=" + lon

                        );

                    } else {

                        makeSearchCall(ApplicationConstants.searchURL
                                + "?keyword=" + obj_keyword.getText() +
                                "&segmentId=" + categories_value[home_spinner_cat.getSelectedItemPosition()] +
                                "&distance=" + distance +
                                "&distanceUnit=" + units_value[home_spinner_unit.getSelectedItemPosition()] +
                                "&latitude=" + lat +
                                "&longitude=" + lon
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


    }

    private void makeApiCall(String url) {
        ApiCall.make(this.getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
                    JSONObject responseObject = responseObjectOuter.getJSONObject("_embedded");
                    JSONArray array = responseObject.getJSONArray("attractions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void makeLocationCall(String text) {
        ApiCall.makeLocation(this.getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    lat = responseObject.getString("lat");
                    lon = responseObject.getString("lon");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void makeSearchCall(String url) {
        ApiCall.make(this.getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement

                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
                    JSONObject responseObject = responseObjectOuter.getJSONObject("_embedded");
                    JSONArray array = responseObject.getJSONArray("events");
                    List<String> stringList = new ArrayList<>();
                    Intent intent = new Intent(getActivity(), ResultsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("Array", array.toString());
                    intent.putExtras(b);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private Location getLastBestLocation() {
        LocationManager mLocationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }
        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }
}