package com.nikitha.android.newscovid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ListItems>> {
    ArrayList<ListItems> populatedData=new ArrayList<ListItems>();
    ArrayListHighlightsAdaptor  arrayListHighlightsAdaptor;
    Bundle input=new Bundle();
    NetworkInfo networkInfo;
    ConnectivityManager ConnectionManager;

    String url1="http://content.guardianapis.com/search?";
            //"http://content.guardianapis.com/search?q=coronovirus&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populatedData= populatData(populatedData);

        GridView view=findViewById(R.id.gridView1);
        arrayListHighlightsAdaptor=new ArrayListHighlightsAdaptor(this,populatedData);
        view.setAdapter(arrayListHighlightsAdaptor);

        LayoutInflater inflater = getLayoutInflater();
        View headerView = getLayoutInflater().inflate(R.layout.header, null);
 //       view.addHeaderView(headerView);

        input.putString("url",url1);
        LoaderManager.getInstance(this).initLoader(1, input, this).forceLoad();
    }

    private ArrayList<ListItems> populatData(List<ListItems> populatedData) {
        Bitmap image=null;

        for(int i=0;i<10;i++) {
            populatedData.add(new ListItems(image, "title", "source", "url","webPublicationDate","author"));
        }
        return (ArrayList<ListItems>) populatedData;
    }

    @NonNull
    @Override
    public Loader<ArrayList<ListItems>> onCreateLoader(int id, @Nullable Bundle args) {
        TextView emptyview=findViewById(R.id.emptyView);
        ProgressBar progressBar=findViewById(R.id.progressBarspinner);
        progressBar.setVisibility(View.VISIBLE);
        //return new HighlightsLoader(this,args);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(url1);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //"q=coronovirus&api-key=test";

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", "coronovirus");
        uriBuilder.appendQueryParameter("api-key", "54edbbdc-691d-464c-acf2-94839f637510");
        uriBuilder.appendQueryParameter("show-tags", "contributor");


        // uriBuilder.appendQueryParameter("orderby", "time");
        ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            return new HighlightsLoader(this, uriBuilder.toString());
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ListItems>> loader, ArrayList<ListItems> data) {
        TextView emptyview=findViewById(R.id.emptyView);
        ProgressBar progressBar=findViewById(R.id.progressBarspinner);
        progressBar.setVisibility(View.INVISIBLE);
        try{
            if(data!=null && data.size()>0) {
                emptyview.setVisibility(View.INVISIBLE);
                arrayListHighlightsAdaptor.setData(data);
            }
            else{
                GridView view=findViewById(R.id.gridView1);
                emptyview.setVisibility(View.VISIBLE);
                view.setEmptyView(emptyview);
                ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo==null){
                    emptyview.setText(getResources().getString(R.string.noDatabczNoInternet));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ListItems>> loader) {
        arrayListHighlightsAdaptor.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
