package com.pavka.android.rssproject;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class Reader extends AsyncTask<HttpURLConnection, String, String> implements MediaPlayer.OnPreparedListener {

    public String result;
    MediaPlayer mp;
    @Override
    protected String doInBackground(HttpURLConnection... httpURLConnections) {
        InputStream in = null;
        try {
            in = httpURLConnections[0].getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String xml = "";
        String line;
        JSONArray items;
        try {
            while ((line = reader.readLine()) != null) {
                xml += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(xml);


        try {
            String jsonString = Jsoup.connect("https://learningenglish.voanews.com/podcast/?count=20&zoneId=1579").execute().body();
            System.out.println(jsonString);
            int j = jsonString.indexOf('>');
            System.out.println("ATTENTION!!! I = "+j);
            String sjson = jsonString.substring(j+1);
            System.out.println(sjson);



            //JSONObject jsonObject = new JSONObject(xml);
            XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
            JSONObject jsonObject = xmlToJson.toJson();


            JSONObject rss = jsonObject.getJSONObject("rss");
            JSONObject channel = rss.getJSONObject("channel");
            items = channel.getJSONArray("item");



            String[] records = new String[items.length()];

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject enclosure = item.getJSONObject("enclosure");
                String mp3 = enclosure.getString("url");
                System.out.println(mp3);
                records[i] = mp3;
            }


        int tapeNo = (int) (Math.random() * items.length());
        result = records[tapeNo];
        System.out.println("HERE THE RESULT: " + result);
    }

        catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        mp = new MediaPlayer();

        try {
            mp.setDataSource(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("PLAYER! : ", "Data set!");

        mp.setOnPreparedListener(this);
        mp.prepareAsync();

        Log.d("PLAYER! : ", "PrepareAsync!");
        System.out.println("AGAIN : " + result);
        Log.e("PLAYER! : ", result);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
