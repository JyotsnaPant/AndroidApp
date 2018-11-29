package com.example.searchApplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VenueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView venueNameTxtView;
    public TextView venueAddrTxtView;
    public TextView venueCityTxtView;
    public  TextView phoneNoTxtView;
    public TextView openHTxtView;
    public TextView gRuleTxtView;
    public TextView gRuleLabel;
    public TextView cRuleTxtView;
    public String latx;
    public String longx;
    private OnFragmentInteractionListener mListener;
    GoogleMap mGoogleMap;
    MapView mMapView;


    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_venue, container, false);
            Bundle b = getActivity().getIntent().getExtras();
            String Array = b.getString("Response");
        venueNameTxtView = view.findViewById(R.id.textView37);
        venueAddrTxtView = view.findViewById(R.id.textView38);
        venueCityTxtView = view.findViewById(R.id.textView39);
        phoneNoTxtView = view.findViewById(R.id.textView40);
        openHTxtView = view.findViewById(R.id.textView41);
        gRuleTxtView = view.findViewById(R.id.textView42);
        gRuleTxtView.setMovementMethod(ScrollingMovementMethod.getInstance());
        cRuleTxtView = view.findViewById(R.id.textView43);
        cRuleTxtView.setMovementMethod(ScrollingMovementMethod.getInstance());
        gRuleLabel = view.findViewById(R.id.textView35);
        mMapView = (MapView) view.findViewById(R.id.map);
        if(mMapView!=null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);


        }


//            nameTxtView = view.findViewById(R.id.textView26);
//            followerTxtView = view.findViewById(R.id.textView27);
//            popularityTxtView = view.findViewById(R.id.textView28);
//            checkAtTxtView = view.findViewById(R.id.textView29);


            JSONObject embedded = null;
            try {
                embedded = (JSONObject) new JSONObject(Array).get("_embedded");
                JSONArray venue = (JSONArray) embedded.get("venues");
                String vname = venue.getJSONObject(0).get("name").toString();
                makeVenueCall(ApplicationConstants.searchVenueURL
                        + "?venueName=" + vname);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return view;
        }



    private void makeVenueCall(String url) {
        ApiCall.make(getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement

                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
                    JSONObject venueObj = (JSONObject) responseObjectOuter.get("_embedded");
                    JSONArray venuesArr = (JSONArray) venueObj.get("venues");
                    String venueN = venuesArr.getJSONObject(0).get("name").toString();
                    venueNameTxtView.setText(venueN);
                    JSONObject address = (JSONObject) venuesArr.getJSONObject(0).get("address");
                    String line = address.get("line1").toString();
                    venueAddrTxtView.setText(line);
                    JSONObject city = (JSONObject) venuesArr.getJSONObject(0).get("city");
                    String cityname = city.get("name").toString();
                    JSONObject state = (JSONObject) venuesArr.getJSONObject(0).get("state");
                    String statename = state.get("name").toString();
                    String cityFinal = cityname + "," + statename;
                    venueCityTxtView.setText(cityFinal);
                    JSONObject boxOffice = (JSONObject) venuesArr.getJSONObject(0).get("boxOfficeInfo");
                    String phone = boxOffice.get("phoneNumberDetail").toString();
                    phoneNoTxtView.setText(phone);
                    String openHours = boxOffice.get("openHoursDetail").toString();
                    openHTxtView.setText(openHours);
                    JSONObject gInfo = (JSONObject) venuesArr.getJSONObject(0).get("generalInfo");
                    String cRule = gInfo.get("childRule").toString();
                    cRuleTxtView.setText(cRule);
                    if(venuesArr.getJSONObject(0).has("generalRule"))
                    {
                        String gRule = gInfo.get("generalRule").toString();
                        gRuleTxtView.setText(gRule);
                    }
                    else
                    {

                        gRuleTxtView.setVisibility(View.GONE);
                        gRuleLabel.setVisibility(View.GONE);
                    }
                    JSONObject location = (JSONObject) venuesArr.getJSONObject(0).get("location");
                    latx = location.get("latitude").toString();
                    longx = location.get("longitude").toString();
                    onMapReady(mGoogleMap);



//                    JSONObject followers = (JSONObject) items.getJSONObject(0).get("followers");
//                    int fnumber = (int) followers.get("total");
//                    followerTxtView.setText(fnumber);
//                    String popularity = items.getJSONObject(0).get("popularity").toString();
//                    popularityTxtView.setText(popularity);
//                    JSONObject externalUrls = (JSONObject) items.getJSONObject(0).get("external_urls");
//                    String spotify = externalUrls.get("spotify").toString();
//                    checkAtTxtView.setText(spotify);



//                    JSONObject responseObject = responseObjectOuter.getJSONObject("_embedded");
//                    JSONArray array = responseObject.getJSONArray("events");
//                    List<String> stringList = new ArrayList<>();
//                    Intent intent = new Intent(getContext(), TabbedActivity.class);
//
//                    Bundle b = new Bundle();

//                    intent.putExtras(b);
//                    startActivity(intent);
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
            mListener.onFragmentInteractionVenue(uri);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {


        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if ( !(latx == null)  && !(longx ==null)) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latx), Double.parseDouble(longx))));
            CameraPosition mapFill = CameraPosition.builder().target(new LatLng(Double.parseDouble(latx), Double.parseDouble(longx))).zoom(16).bearing(0).tilt(0).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mapFill));
        }

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
        void onFragmentInteractionVenue(Uri uri);
    }
}
