package com.fitflo.fitflo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    public ArrayAdapter<String> mSearchResultsAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //adpater is how a view keeps in sync with datastructure
        mSearchResultsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,new ArrayList<String>());
        ListView listView = (ListView) getView().findViewById(R.id.searchResultsList);
        listView.setAdapter(mSearchResultsAdapter);

        //listener for list items. bring up event details activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //this is how we start new activities

                //first create an intent, first arg is context, second is class
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);


                //start the activity, using the intent
                startActivity(intent);
            }
        });

        TextView textView = (TextView) getView().findViewById(R.id.greeting);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        textView.setText("Hi " + FileUtils.getString(getContext(),getString(R.string.username)));
        super.onViewCreated(view, savedInstanceState);
    }




    public void sendGetAllEventsRequest(View view) {
        Log.d("hey", "got inside");



        //notice the https:// prefix. necessary
        String ip = "https://" + MainActivity.cjsServerIp + ":3000/events/getAllEvents";

        JsonArrayRequest request = new JsonArrayRequest(ip,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mSearchResultsAdapter.clear();
                for(int i = 0; i < response.length();i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        String title = jObj.getString("title");
                        String instructor = jObj.getString("instructor");
                        double price = jObj.getDouble("price");
                        String id = jObj.getString("_id");

                        mSearchResultsAdapter.add(id + "," + title + ", " + instructor + ", " + price + " dollars");
                    } catch(JSONException exc) {
                        mSearchResultsAdapter.add("error");
                    }



                }
                mSearchResultsAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSearchResultsAdapter.clear();
                mSearchResultsAdapter.add(error.toString());
                mSearchResultsAdapter.notifyDataSetChanged();
                error.printStackTrace();

            }
        });

        MainActivity.requestQueue.add(request);
    }
}
