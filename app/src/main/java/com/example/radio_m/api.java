package com.example.radio_m;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Task extends AsyncTask<String, String, String>{



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("ОТправляем...");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream)) ;
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection !=null)
                connection.disconnect();
            try{
                if(reader != null)
                    reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }


        }
        return null;
    }
}

class Api {

    public static void change_vol(String url, String volume){
        new Task().execute(volume);
    }

    public static void change_SSID(String ssid, String pasword){

    }

    public static void change_station(String station){

    }

}