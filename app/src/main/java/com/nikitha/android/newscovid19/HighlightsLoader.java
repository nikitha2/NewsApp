package com.nikitha.android.newscovid19;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class HighlightsLoader extends AsyncTaskLoader<ArrayList<ListItems>> {
    ArrayList<ListItems> listdata=new ArrayList<ListItems>();
    String args;

    public HighlightsLoader(@NonNull Context context, String args) {
        super(context);
        this.args=args;
    }

    @Nullable
    @Override
    public ArrayList<ListItems> loadInBackground() {
        String Stringurl=args;
        URL url = null;

        try{
            if(Stringurl!=null) {
                url = createURL(Stringurl);
            }
          String jsonResponse= makeHttpRequest(url);
          listdata= extractFeatureFromJson(jsonResponse);
        }catch(Exception e){
            e.printStackTrace();
        }
        return listdata;
    }

    private ArrayList<ListItems> extractFeatureFromJson(String jsonResponse) throws JSONException, MalformedURLException, ParseException {
        String sourceName=null,title=null,url=null,webPublicationDate=null;String author=null;Date date1=null;
        Bitmap image=null;
        ArrayList<ListItems> extractedData= new ArrayList<ListItems>();

        JSONObject jsonObject=new JSONObject(jsonResponse);
        JSONObject response=jsonObject.getJSONObject("response");

        JSONArray results= response.getJSONArray(/*"results");*/"results");

        for(int i=0;i<results.length();i++) {
            JSONObject articlesAtIndex = results.getJSONObject(i);

            if (articlesAtIndex.has("sectionName"/*"source"*/)) {
//                JSONObject source = articlesAtIndex.getJSONObject("source");
//                if(source.has("name")){
//                    sourceName= source.getString("name");
           //     }
                sourceName= articlesAtIndex.getString("sectionName");
            }

            if(articlesAtIndex.has(/*"title"*/"webTitle")){
                title=articlesAtIndex.getString(/*"title"*/"webTitle");
            }
            if(articlesAtIndex.has(/*"url"*/"webUrl")){
                url=articlesAtIndex.getString(/*"url"*/"webUrl");
            }

            if(articlesAtIndex.has("webPublicationDate")){
                webPublicationDate="published on: "+articlesAtIndex.getString("webPublicationDate");
               // date1=new SimpleDateFormat("dd/MM/yyyy").parse(webPublicationDate);
               // webPublicationDate=date1.toString();
            }

            /*if(articlesAtIndex.has("urlToImage")){
                String imageString=articlesAtIndex.getString("urlToImage");
                try{
                    URL urlImage = new URL(imageString);
                    image = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
                }
                catch( MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            if(articlesAtIndex.has("tags")){
                JSONArray tags = articlesAtIndex.getJSONArray("tags");
                if(tags!=null && tags.length()>0) {
                    JSONObject tagAtIndex = tags.getJSONObject(0);
                    if(tagAtIndex.has("firstName") && tagAtIndex.has("lastName")) {
                        author = "author: "+tagAtIndex.getString("firstName") + " " + tagAtIndex.getString("lastName");
                    }
                }


            }

            extractedData.add(new ListItems(null,title,sourceName, url,webPublicationDate,author));
        }
        return extractedData;
    }

    private String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        if(url ==null){
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        int responseCode=urlConnection.getResponseCode();
        if(responseCode==CONSTANTS.HTTP_OK_CONNECTION){
            inputStream=urlConnection.getInputStream();
            jsonResponse= readInputStream(inputStream);
        }
        return jsonResponse;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder jsonResponse=new StringBuilder();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while(line!=null){
            jsonResponse.append(line);
            line=bufferedReader.readLine();
        }
        return jsonResponse.toString();
    }

    private URL createURL(String stringurl) throws MalformedURLException {
        return new URL(stringurl);
    }


}
