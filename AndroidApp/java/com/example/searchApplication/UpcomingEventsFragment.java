package com.example.searchApplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpcomingEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapterUE customAdapterUE;
    public int upcomingEventsSeq=0;
    public JSONArray event;
    ArrayList<String> nameArray;
    ArrayList<String> bandArray;
    ArrayList<String> dateArray;
    ArrayList<String> typeArray;
    public Spinner defaultSpinner;
    public Spinner sortSpinner;

    private OnFragmentInteractionListener mListener;

    public UpcomingEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingEventsFragment newInstance(String param1, String param2) {
        UpcomingEventsFragment fragment = new UpcomingEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    class CustomAdapterUE extends RecyclerView.Adapter<CustomAdapterUE.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName;
            TextView textViewBand;
            TextView textViewDate;
            TextView textViewType;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textViewName = (TextView) itemView.findViewById(R.id.textView_name);
                this.textViewBand = (TextView) itemView.findViewById(R.id.textView_band);
                this.textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
                this.textViewType = (TextView) itemView.findViewById(R.id.textView_type);

            }
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_cards, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            TextView textViewName = holder.textViewName;
            TextView textViewBand = holder.textViewBand;
            TextView textViewDate = holder.textViewDate;
            TextView textViewType = holder.textViewType;

            textViewName.setText(nameArray.get(position));
            textViewBand.setText(bandArray.get(position));
            textViewDate.setText(dateArray.get(position));
            textViewType.setText(typeArray.get(position));


        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return nameArray.size();
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        Bundle b = getActivity().getIntent().getExtras();
        String Array = b.getString("Response");

        nameArray = new ArrayList<String>();
        bandArray = new ArrayList<String>();
        dateArray = new ArrayList<String>();
        typeArray = new ArrayList<String>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new RecyclerView.Adapter();
//        mRecyclerView.setAdapter(mAdapter);
        customAdapterUE = new CustomAdapterUE();
        mRecyclerView.setAdapter(customAdapterUE);
        defaultSpinner = view.findViewById(R.id.spinner_upone);
        sortSpinner = view.findViewById(R.id.spinner_uptwo);

        JSONObject embedded = null;
        try {
            embedded = (JSONObject) new JSONObject(Array).get("_embedded");
            JSONArray venue = (JSONArray) embedded.get("venues");
            String vname = venue.getJSONObject(0).get("name").toString();
            makeUpcomingEvents(ApplicationConstants.searchUpcomingEvents
                    + "?VenueNameUE=" + vname);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        String[] default_cat = new String[]{"Default", "Event Name", "Time", "Artist", "Type"};
//        categories_value = new String[]{"", "KZFzniwnSyZfZ7v7nJ", "KZFzniwnSyZfZ7v7nE", "KZFzniwnSyZfZ7v7na", "KZFzniwnSyZfZ7v7nn", "KZFzniwnSyZfZ7v7n1"};
        ArrayAdapter<String> adapter_categories = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, default_cat);
        defaultSpinner.setAdapter(adapter_categories);

        String[] sort_cat = new String[]{"Ascending", "Descending"};
//        categories_value = new String[]{"", "KZFzniwnSyZfZ7v7nJ", "KZFzniwnSyZfZ7v7nE", "KZFzniwnSyZfZ7v7na", "KZFzniwnSyZfZ7v7nn", "KZFzniwnSyZfZ7v7n1"};
        ArrayAdapter<String> adapter_categories_sort = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, sort_cat);
        sortSpinner.setAdapter(adapter_categories_sort);

        defaultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    sortSpinner.setEnabled(false);
                }
                else {
                    sortSpinner.setEnabled(true);
                }
//                Collections.sort(nameArray, new Comparator<String>() {
//                    @Override
//                    public int compare(String lhs, String rhs) {
//                        return lhs.compareTo(rhs);
//                    }
//                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
        return view;
    }

    public void makeUpcomingEvents(String url)
    {
        ApiCall.make(getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement

                try {
                    if (upcomingEventsSeq==0){
                        JSONObject responseObjectOuter = new JSONObject(response);
                        JSONObject resultspage = (JSONObject) responseObjectOuter.get("resultsPage");
                        JSONObject results = (JSONObject) resultspage.get("results");
                        JSONArray venue = (JSONArray) results.get("venue");
                        String venueId = venue.getJSONObject(0).get("id").toString();
                        upcomingEventsSeq=1;
                        makeUpcomingEvents(ApplicationConstants.searchUpcomingEvents2
                                + "?idxyz=" + venueId);
                    }
                    else if (upcomingEventsSeq==1){
                        JSONObject responseObjectOuter = new JSONObject(response);
                        JSONObject resultspage = (JSONObject) responseObjectOuter.get("resultsPage");
                        JSONObject result = (JSONObject) resultspage.get("results");
                        event = (JSONArray) result.get("event");
                        for(int i = 0, count = event.length(); i< count; i++)
                        {
                            if(i>4)
                            {
                                break;
                            }
                            nameArray.add(event.getJSONObject(i).get("displayName").toString());
                            JSONArray performance = (JSONArray) event.getJSONObject(0).get("performance");
//                            String displayName = performance.getJSONObject(0).getString("displayName");
                            bandArray.add(performance.getJSONObject(0).get("displayName").toString());
                            typeArray.add(event.getJSONObject(i).get("type").toString());
                            JSONObject start = event.getJSONObject(i).getJSONObject("start");
                            dateArray.add(start.get("datetime").toString());

                        }


                    }


                    customAdapterUE.notifyDataSetChanged();


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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionUE(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionUE(Uri uri);
    }
}
