package com.pavka.android.rssproject;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    Map<String, String> checkedRecords;
    Gson gson;
    SharedPreferences preferences;

    final static String VOA = "https://av.voanews.com/clips/VLE/2019/04/29/2d0b0197-14f4-4010-90a7-9bcdc80bccc9_hq.mp3";
    InputStream in;
    BufferedReader bReader;
    String[] records;

    MediaPlayer mp;
    AudioManager manager;
    int length;

    TextView text = null;
    HttpURLConnection connection;
    URL url;
    InputStream is;

    Reader reader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            url = new URL("https://learningenglish.voanews.com/podcast/?count=20&zoneId=1579");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        text = findViewById(R.id.text);
        try {
            connection = (HttpsURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mp = new MediaPlayer();



    }

    @Override
    protected void onStart() {
        super.onStart();
        reader = new Reader();
        reader.execute(connection);
    }

    public void playPodcast(View view) throws IOException {


        mp.setDataSource(reader.result);
        Log.d("PLAYER! : ", "Data set!");

        mp.setOnPreparedListener(this);
        mp.prepareAsync();
        Log.d("PLAYER! : ", "PrepareAsync!");
        Log.e("PLAYER! : ", reader.result);


    }
    void readArticle() {

    }

    //TODO incorrect signature in onClick, permission in Manifest

    void cancel() throws IOException {

        if (reader != null) bReader.close();
        if (in != null) in.close();
        if (mp != null) {
            try {
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("PLAYER! : ", "onPrepared");
        mp.start();
    }
}
