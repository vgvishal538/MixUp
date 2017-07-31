package com.mixup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mixup.R;
import com.mixup.adapter.ArtistAdapter;
import com.mixup.model.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private RecyclerView detail_listing_recyclerview;
    private ArtistAdapter artistAdapter;
    private List<Artist> artists_data;
    private ProgressDialog progressDialog;
    private EditText artist_name_edittext;
    String data ;
    private ProgressBar progressBar;
    String finalTimerString = "";
    String secondsString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detail_listing_recyclerview = (RecyclerView)findViewById(R.id.detail_listing_recyclerview);
        artist_name_edittext = (EditText)findViewById(R.id.artist_name_edittext);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        detail_listing_recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        detail_listing_recyclerview.setLayoutManager(layoutManager);

        artist_name_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    data = artist_name_edittext.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    performSearch();
                    return true;
                }
                return false;
            }
        });


    }

    private void performSearch() {

        artist_name_edittext.clearFocus();
        final InputMethodManager in = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(artist_name_edittext.getWindowToken(), 0);

        AsyncHttpClient client = new AsyncHttpClient();

        String Url = " https://itunes.apple.com/search?term="+data;

            client.get(Url,  new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e("_response",new String(responseBody, StandardCharsets.UTF_8));
                    artists_data = new ArrayList<Artist>();
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject json = new JSONObject(new String(responseBody, StandardCharsets.UTF_8));
                        JSONArray json1 = json.getJSONArray("results");
                        for (int i = 0; i< json1.length();i++ ){
                            Artist artist = new Artist();
                            JSONObject json2 = json1.getJSONObject(i);
                            String trackName = json2.getString("trackName");
                            String artistName = json2.getString("artistName");
                            String primaryGenreName = json2.getString("primaryGenreName");
                            long trackTimeMillis = json2.getLong("trackTimeMillis");
                            milliSecondsToTimer(trackTimeMillis);
                            double trackPrice = json2.getDouble("trackPrice");
                            String artworkUrl30 = json2.getString("artworkUrl30");
                            String artworkUrl100 = json2.getString("artworkUrl100");
                            String collectionName = json2.getString("collectionName");
                            String previewUrl = json2.getString("previewUrl");
                            artist.setArtistName(artistName);
                            artist.setArtworkUrl30(artworkUrl30);
                            artist.setTrackName(trackName);
                            artist.setPrimaryGenreName(primaryGenreName);
                            artist.setCollectionName(collectionName);
                            artist.setTrackTimeMillis(trackTimeMillis);
                            artist.setTrackPrice(trackPrice);
                            artist.setPreviewUrl(previewUrl);
                            artist.setArtworkUrl100(artworkUrl100);
                            artists_data.add(artist);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    artistAdapter = new ArtistAdapter(getApplicationContext(),artists_data);
                    detail_listing_recyclerview.setAdapter(artistAdapter);



                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.dismiss();
                    try {
                        Log.e("_response_error",new String(responseBody, StandardCharsets.UTF_8));

                    }catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }


                }
            });



    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){


        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

}
