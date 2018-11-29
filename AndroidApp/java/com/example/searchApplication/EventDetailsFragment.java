package com.example.searchApplication;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDetailsFragment extends Fragment {
    public JSONObject jsonArrayDetails;
    public TextView artistName;
    public TextView venueName;
    public TextView eventTime;
    public TextView category;
    public TextView priceRange;
    public TextView ticketStatus;
    public TextView buy;
    public TextView seatMap;
    public TextView priceRangeLabel;
    public TextView ticketStatusLabel;
    public TextView buyLabel;
    public TextView seatMapLabel;



    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        artistName = view.findViewById(R.id.textView14);
        venueName = view.findViewById(R.id.textView15);
        eventTime = view.findViewById(R.id.textView16);
        category = view.findViewById(R.id.textView17);
        priceRange = view.findViewById(R.id.textView18);
        ticketStatus = view.findViewById(R.id.textView19);
        buy = view.findViewById(R.id.textView20);
        seatMap = view.findViewById(R.id.textView22);
        priceRangeLabel = view.findViewById(R.id.textView10);
        ticketStatusLabel = view.findViewById(R.id.textView11);
        buyLabel = view.findViewById(R.id.textView12);
        seatMapLabel = view.findViewById(R.id.textView13);
        Bundle b = getActivity().getIntent().getExtras();
        String Array = b.getString("Response");
//        try {
//            jsonArrayDetails = (JSONObject) new JSONObject(Array).get("_embedded");
////            jsonArrayDetails = new JSONArray(Array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        try {
            JSONObject newArray = (JSONObject) new JSONObject(Array);

            JSONObject embedded = (JSONObject) new JSONObject(Array).get("_embedded");
            JSONArray attractions = (JSONArray) embedded.get("attractions");
            String name = attractions.getJSONObject(0).get("name").toString();
            String name2 = attractions.getJSONObject(1).get("name").toString();
            String fullName;
            if(name2 == null)
            {
                fullName = name ;
            }
            else {
                 fullName = name + " | " + name2;
            }
            artistName.setText(fullName);
//            artistName = jsonArrayDetails.getJSONObject(0).get("name").toString();
            JSONArray venue = (JSONArray) embedded.get("venues");
            String vname = venue.getJSONObject(0).get("name").toString();
            venueName.setText(vname);
            SimpleDateFormat ipformatter = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat opformatter = new SimpleDateFormat("MMM dd,yyyy");
            JSONObject dates = (JSONObject) new JSONObject(Array).get("dates");
            JSONObject start = (JSONObject) dates.get("start");
            String localDate = start.get("localDate").toString();
            Date swe = ipformatter.parse(localDate);

            String localTime = start.get("localTime").toString();
            String fullDate = opformatter.format(swe).toString() + " " + localTime;
            eventTime.setText(fullDate);
            JSONArray classifications = (JSONArray) new JSONObject(Array).get("classifications");
            JSONObject segment = (JSONObject) classifications.getJSONObject(0).get("segment");
            String segmentName = segment.get("name").toString();
//            String segmentname = segment.getJSONObject().get("name").toString();
//            JSONObject segmentname = (JSONObject) segment.getJSONObject().get("name").toString();
            JSONObject genre = (JSONObject) classifications.getJSONObject(0).get("genre");
            String genreName = genre.get("name").toString();
            String segGen = segmentName + " | " + genreName;
            category.setText(segGen);
            if(newArray.has("priceRanges")) {
                JSONArray priceR = (JSONArray) new JSONObject(Array).get("priceRanges");
                String minRange = priceR.getJSONObject(0).get("min").toString();
                String maxRange = priceR.getJSONObject(0).get("max").toString();
                String price = "$" + minRange + " ~ " + "$" + maxRange;
                priceRange.setText(price);
            }
            else
            {
                priceRange.setVisibility(View.GONE);
                priceRangeLabel.setVisibility(View.GONE);
            }
            if(dates.has("status"))
            {
                JSONObject status = (JSONObject) dates.get("status");
                String code = status.get("code").toString();
                ticketStatus.setText(code);
            }
            else
            {
                ticketStatus.setVisibility(View.GONE);
                ticketStatusLabel.setVisibility(View.GONE);
            }

            if(newArray.has("url"))
            {
                String url = (String) new JSONObject(Array).get("url");
                buy.setText(url);
                String linkTextBuy = "<a href=" + url + ">Ticketmaster</a>";
                buy.setText(Html.fromHtml(linkTextBuy));
                buy.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else
            {
                buy.setVisibility(View.GONE);
                buyLabel.setVisibility(View.GONE);
            }
            if(newArray.has("seatmap"))
            {
                JSONObject seatMapPicture = (JSONObject) new JSONObject(Array).get("seatmap");
                String staticUrl = (String) seatMapPicture.get("staticUrl");
                seatMap.setText(staticUrl);
                String linkText = "<a href=" + staticUrl + ">View Here</a>";
                seatMap.setText(Html.fromHtml(linkText));
                seatMap.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else
            {
                seatMap.setVisibility(View.GONE);
                seatMapLabel.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
//        CustomAdapter customAdapter = new CustomAdapter();
        return view;



    }


}
//
//    class CustomAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return jsonArrayDetails.length();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = getLayoutInflater().inflate(R.layout.fragment_event, null);
//
//
//            TextView textView_name = (TextView)convertView.findViewById(R.id.textView14);
////            TextView textView_venue = (TextView)convertView.findViewById(R.id.textView_venue);
////            TextView textView_date = (TextView)convertView.findViewById(R.id.textView_date);
//
//            textView_name.setText(artistName);
////            textView_date.setText(dateArray.get(position));
////            textView_venue.setText(venueArray.get(position));
//            return convertView;
//        }
//    }
