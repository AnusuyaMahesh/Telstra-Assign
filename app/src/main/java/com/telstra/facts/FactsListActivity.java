package com.telstra.facts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.telestra.facts.R;
import com.telstra.facts.adapter.FactListAdapter;
import com.telstra.facts.model.Facts;
import com.telstra.facts.network.RequestManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Anusuya on 3/12/2015.
 * Main activity for listing the facts
 */
public class FactsListActivity extends ActionBarActivity {

    private static final String TAG = "FactListActivity";
    private static final String FACT_URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    ProgressDialog mProgressDialog;
    private ActionBar actionBar;
    private ListView mListView;
    private Context mContext;
    private FactListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts_list);
        mContext = this;
        initView();
        fetchFactList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_facts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            fetchData(FACT_URL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Init view components
    private void initView() {
        actionBar = getSupportActionBar();
        updateActionBarTitle(getString(R.string.app_name));
        mListView = (ListView) findViewById(R.id.list_view);
    }

     private void fetchFactList() {

        Cache cache = RequestManager.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(FACT_URL);
        if (entry != null) {
            try {
                String dataCached = new String(entry.data, "UTF-8");
                parseNetworkResponse(dataCached);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
             fetchData(FACT_URL);
        }

    }

    //Update List View with the json response
    private void updateListView(Facts factsList) {

        updateActionBarTitle(factsList.getTitle());
        mListAdapter = new FactListAdapter(mContext, factsList.getRows());
        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

    }

    //Update actionbar title
    private void updateActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    // Usage of volley JSON Object request for network calls
    private void fetchData(String url) {

        // Volley retries the request for a default retry limit. Hence
        //the tag is used to cancel the request
        String tag_json_obj = "json_obj_req";

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        mProgressDialog.hide();

                        String jsonString = response.toString();
                        parseNetworkResponse(jsonString);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                mProgressDialog.hide();
                Toast.makeText(mContext, getString(R.string.network_error_retry), Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        RequestManager.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    //Serialize and Deserialize object using GSON library
    private void parseNetworkResponse(String jsonString) {
        Gson gson = new Gson();
        Facts factsList = gson.fromJson(jsonString, Facts.class);
        updateListView(factsList);
    }
}
