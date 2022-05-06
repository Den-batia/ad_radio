package com.example.radio_m;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.radio_m.databinding.SetingsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SecondFragment extends Fragment {

    String ip_s = "";
    String error = "";
    SharedPreferences preferences;
    private SetingsBinding binding;
    private Button connect;
    private Button disconnect;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SetingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connect = view.findViewById(R.id.connect);
        disconnect = view.findViewById(R.id.disconnect);
        load_prreff();

        binding.mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.bestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_TherdFragment);
            }
        });

        binding.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_ip("");
            }
        });

        binding.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new Task_s().execute("");
            }
        });
    }
    private void save_ip(String ip) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(String.valueOf(Day.IP), ip);
        ed.commit();
        ip_s = ip;
        if (!ip_s.isEmpty()){
            connect.setText("Вы привязаны");
            connect.setEnabled(false);
        }else {
            connect.setEnabled(true);
            connect.setText("Привязать");
        }
    }

    private void load_prreff(){
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        ip_s = preferences.getString(String.valueOf(Day.IP), "");
        if (!ip_s.isEmpty()){
            connect.setText("Вы привязаны");
            connect.setEnabled(false);
        }else {
            connect.setEnabled(true);
            connect.setText("Привязать");
        }
    }

    private class Task_s extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);
            if(! data.isEmpty()){
                save_ip(data);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream stream = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://192.168.100.101/api/wifi-info");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream)) ;
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                JSONObject obj = new JSONObject(buffer.toString());

                if(obj.has("ip")){
                    return (String) obj.get("ip");
                }


            }catch (SocketTimeoutException e){
                error = "Проверьте правильность подключения к точке доступа 'Radio' либо включите радиоприемник.";
                return "";
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection !=null)
                    connection.disconnect();
                try{
                    if(reader != null)
                        reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    if(stream != null)
                        stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        connect = null;
        preferences = null;
        disconnect = null;
    }



}