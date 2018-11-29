package com.example.searchApplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    public JSONArray jsonArray;
    ArrayList<String> nameArray;
    ArrayList<String> venueArray;
    ArrayList<String> dateArray;
    ArrayList<String> eventidArray;
    ArrayList<String> imageArray;
    public boolean favToggle=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ListView listView = findViewById(R.id.resultsListView);
        Bundle b = getIntent().getExtras();
        String Array=b.getString("Array");
        try {
            jsonArray = new JSONArray(Array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameArray = new ArrayList<String>();
        venueArray = new ArrayList<String>();
        dateArray = new ArrayList<String>();
        eventidArray = new ArrayList<String>();
        imageArray = new ArrayList<String>();

        for(int i = 0, count = jsonArray.length(); i< count; i++)
        {
            try {
                nameArray.add(jsonArray.getJSONObject(i).get("name").toString());
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("dates");
                JSONObject dateObject = (JSONObject) jsonObject.get("start");
                if ((dateObject.has("localDate")) && (dateObject.has("localTime")))
                    dateArray.add(dateObject.get("localDate").toString() + " " + dateObject.get("localTime"));
                else if (dateObject.has("localDate")){
                    dateArray.add(dateObject.get("localDate").toString());
                }
                else
                    dateArray.add("");
                jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("_embedded");
                JSONArray venueObject = (JSONArray) jsonObject.get("venues");
                venueArray.add(venueObject.getJSONObject(0).get("name").toString());
                eventidArray.add(jsonArray.getJSONObject(i).get("id").toString());
                JSONArray classifications = (JSONArray) jsonArray.getJSONObject(i).get("classifications");
                JSONObject segment = (JSONObject) classifications.getJSONObject(0).get("segment");
//                imageArray.add(segment.get("id").toString());
                if( segment.get("id").toString().equals("KZFzniwnSyZfZ7v7nJ"))
                {
                    imageArray.add("http://csci571.com/hw/hw9/images/android/music_icon.png");
                }
                if( segment.get("id").toString().equals("KZFzniwnSyZfZ7v7nE"))
                {
                    imageArray.add("http://csci571.com/hw/hw9/images/android/sport_icon.png");
                }if( segment.get("id").toString().equals("KZFzniwnSyZfZ7v7na"))
                {
                    imageArray.add("http://csci571.com/hw/hw9/images/android/art_icon.png");
                }
                if( segment.get("id").toString().equals("KZFzniwnSyZfZ7v7na"))
                {
                    imageArray.add("http://csci571.com/hw/hw9/images/android/film_icon.png");
                }
                if( segment.get("id").toString().equals("KZFzniwnSyZfZ7v7n1"))
                {
                    imageArray.add("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
                }




            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Search Results");



        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = (String) view.findViewById(R.id.textView_name);
//                Toast.makeText(this,"You selected : " + item,Toast.LENGTH_SHORT).show();
                makeDetailsCall(ApplicationConstants.eventdetailsURL
                                + "?eventid=" + eventidArray.get(position), nameArray.get(position));
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.resultlayout, null);

            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
            TextView textView_name = (TextView)convertView.findViewById(R.id.textView_name);
            TextView textView_venue = (TextView)convertView.findViewById(R.id.textView_venue);
            TextView textView_date = (TextView)convertView.findViewById(R.id.textView_date);

            textView_name.setText(nameArray.get(position));
            textView_date.setText(dateArray.get(position));
            textView_venue.setText(venueArray.get(position));

            Picasso
                    .get()
                    .load(imageArray.get(position))
//                    .resize(80, 80)
                    //.fit().centerCrop()
                    .into(imageView);

            //imageView.setImageURI(Uri.parse("http://csci571.com/hw/hw9/images/android/music_icon.png"));
            final ImageView imageViewFav = (ImageView)convertView.findViewById(R.id.imageView);
            imageViewFav.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (favToggle) {
                        imageViewFav.setImageResource(R.drawable.heart_outline_black);
                        Toast.makeText(getApplicationContext(), "Successfully removed from favourites",
                                Toast.LENGTH_LONG).show();
                        favToggle=false;
                    }
                    else {
                        imageViewFav.setImageResource(R.drawable.heart_fill_red);
                        Toast.makeText(getApplicationContext(), "Successfully added to Favourites",
                                Toast.LENGTH_LONG).show();
                        favToggle=true;
                    }
                }
            });
            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeDetailsCall(String url, final String title) {
        ApiCall.make(this.getApplicationContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement

                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
//                    JSONObject responseObject = responseObjectOuter.getJSONObject("_embedded");
//                    JSONArray array = responseObject.getJSONArray("events");
                    List<String> stringList = new ArrayList<>();
                    Intent intent = new Intent(getApplicationContext(), TabbedActivity.class);
                    Bundle b = new Bundle();
                    b.putString("Response",responseObjectOuter.toString());
                    intent.putExtra("title", title);
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
}
