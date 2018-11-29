package com.example.searchApplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    String a = new String();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView nameTxtView;
    public TextView followerTxtView;
    public TextView popularityTxtView;
    public TextView checkAtTxtView;
    public TextView labelView;
    public String name;
    private OnFragmentInteractionListener mListener;
    ArrayList<String> artistImageArray;
    public ListView listViewArtist;

    public ArtistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistImageArray = new ArrayList<String>();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_artist, container, false);
        listViewArtist = view.findViewById(R.id.artists_images);
        Bundle b = getActivity().getIntent().getExtras();
        String Array = b.getString("Response");
        nameTxtView = view.findViewById(R.id.textView26);
        labelView = view.findViewById(R.id.textView7);
        followerTxtView = view.findViewById(R.id.textView27);
        popularityTxtView = view.findViewById(R.id.textView28);
        checkAtTxtView = view.findViewById(R.id.textView29);

        JSONObject embedded = null;
        try {
            embedded = (JSONObject) new JSONObject(Array).get("_embedded");
            JSONArray attractions = (JSONArray) embedded.get("attractions");
            JSONArray classifications = (JSONArray) new JSONObject(Array).get("classifications");
            JSONObject segment = (JSONObject) classifications.getJSONObject(0).get("segment");
            String segmentId = segment.get("id").toString();
            if (!(segmentId).equals("KZFzniwnSyZfZ7v7nJ"))
            {
                labelView.setVisibility(View.GONE);
                nameTxtView.setVisibility(View.GONE);
                followerTxtView.setVisibility(View.GONE);
                popularityTxtView.setVisibility(View.GONE);
                checkAtTxtView.setVisibility(View.GONE);
                view.findViewById(R.id.textView21).setVisibility(View.GONE);
                view.findViewById(R.id.textView23).setVisibility(View.GONE);
                view.findViewById(R.id.textView24).setVisibility(View.GONE);
                view.findViewById(R.id.textView25).setVisibility(View.GONE);

                for(int i = 0, count = attractions.length(); i< count; i++)
                makeArtistImagesCall(ApplicationConstants.searchArtistImages
                        + "?ArtistName=" + attractions.getJSONObject(i).get("name"));
            }
            else {
                name = attractions.getJSONObject(0).get("name").toString();
                makeArtistsCall(ApplicationConstants.searchArtistsURL
                        + "?ArtistName=" + name);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }



        return view;
    }
//
    private void makeArtistsCall(String url) {
        ApiCall.make(getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
//

//String yourFormattedString = formatter.format(100000);Å
                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
                    JSONObject artistObj = (JSONObject) responseObjectOuter.get("artists");
                    JSONArray items = (JSONArray) artistObj.get("items");
                    String nameArtist = items.getJSONObject(0).get("name").toString();
                    nameTxtView.setText(nameArtist);
                    labelView.setText(nameArtist);
                    JSONObject followers = (JSONObject) items.getJSONObject(0).get("followers");
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    int fnumber = (int) followers.get("total");
                    followerTxtView.setText(formatter.format(fnumber));
                    String popularity = items.getJSONObject(0).get("popularity").toString();
                    popularityTxtView.setText(popularity);
                    JSONObject externalUrls = (JSONObject) items.getJSONObject(0).get("external_urls");
                    String spotify = externalUrls.get("spotify").toString();
                    checkAtTxtView.setText(spotify);
                    String linkTextBuy = "<a href=" + spotify + ">Spotify</a>";
                    checkAtTxtView.setText(Html.fromHtml(linkTextBuy));
                    checkAtTxtView.setMovementMethod(LinkMovementMethod.getInstance());
                    makeArtistImagesCall(ApplicationConstants.searchArtistImages
                            + "?ArtistName=" + name);


//                    JSONObject responseObject = responseObjectOuter.getJSONObject("_embedded");
//                    JSONArray array = responseObject.getJSONArray("events");
//                    List<String> stringList = new ArrayList<>();
//                    Intent intent = new Intent(getContext(), TabbedActivity.class);
//
//                    Bundle b = new Bundle();
//                    a = responseObjectOuter.toString();
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

    private void makeArtistImagesCall(String url) {
        ApiCall.make(getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
//

//String yourFormattedString = formatter.format(100000);Å
                try {
                    JSONObject responseObjectOuter = new JSONObject(response);
                    JSONArray items =  (JSONArray) responseObjectOuter.get("items");
                    for(int i=0; i<items.length(); i++){
                        if (i==8){
                            break;
                        }
                        artistImageArray.add(items.getJSONObject(i).get("link").toString());
                    }
                    listViewArtist.setAdapter(
                            new ImageListAdapter(
                                    getContext(), artistImageArray)
                    );
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
            mListener.onFragmentInteractionArtist(uri);
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
        void onFragmentInteractionArtist(Uri uri);
    }




    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList arrayList;
        public ImageListAdapter(Context context, ArrayList arrayList) {
            super(context, R.layout.artists_images);

            this.context = context;
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if (null == convertView) {
                convertView = inflater.inflate(R.layout.artists_images, parent, false);
                ImageView imageView = convertView.findViewById(R.id.image_artists);//:D
            //}


            Picasso
                    .get()
                    .load(arrayList.get(position).toString())
                    .into(imageView);

            return convertView;
        }
    }
}
