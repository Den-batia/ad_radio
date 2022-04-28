package com.example.radio_m;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.radio_m.databinding.MainWindowBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {

    private MainWindowBinding binding;

    private String ip;
    SharedPreferences preferences;
    private TextView textView;
    private TextView textView_info;
    private SeekBar seekBar;
    private EditText editText;
    private TextView errors;
    private Button connect;
    private Button get_info;
//    private CheckBox checkBox;
//    private CheckBox checkBox1;
//    private CheckBox checkBox2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = MainWindowBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.progress);
        seekBar = view.findViewById(R.id.seekBar1);
        editText = view.findViewById((R.id.url_input));
        connect = view.findViewById(R.id.imageButton);
        get_info = view.findViewById(R.id.get_info);
        textView_info = view.findViewById(R.id.textView4);
//        checkBox = view.findViewById(R.id.checkBox);
//        checkBox1 = view.findViewById(R.id.checkBox1);
//        checkBox2 = view.findViewById(R.id.checkBox2);
        errors = view.findViewById(R.id.errors);
//        errors.setVisibility(View.GONE);
        load_prreff();

        binding.getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String url = editText.getText().toString().trim();
//                System.out.println(url);
                new Task().execute("http://icecast.omroep.nl/radio6-bb-mp3");
            }
        });
        http://icecast.omroep.nl/radio6-bb-mp3
        binding.bestMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_TherdFragment);
            }
        });

        binding.setings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String progress = String.valueOf(seekBar.getProgress());
                String string_errors = "";
                if(ip == ""){
                    string_errors += "* ваш телефон (планшет) не синхронизирован с радиоприемником. Перейдите в паздел 'Настройки" +
                            "' и синхронизируйтесь.\n";
                    errors.setText(string_errors);
                    errors.setVisibility(View.VISIBLE);
                }else{
                    Api.change_vol(ip, progress, getActivity());
                    save_seek_text(progress);
                }
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = editText.getText().toString().trim();
//                if(checkBox.isChecked())
//                    check_bar = String.valueOf(checkBox.getText());
//                if(checkBox1.isChecked())
//                    check_bar = String.valueOf(checkBox1.getText());
//                if(checkBox2.isChecked())
//                    check_bar = String.valueOf(checkBox2.getText());

                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String string_errors = "";
                if(ip == ""){
                    string_errors += "* ваш телефон (планшет) не синхронизирован с радиоприемником. Перейдите в паздел 'Настройки" +
                            "' и синхронизируйтесь.\n";
                }else if(url.isEmpty()){
                    url = "";
                    string_errors += "* Введите 'url' аудио потока\n";
                }else if(!url.startsWith("http://"))
                        string_errors += "* Адрес должен ачинаться с 'http://' или 'https://'\n";

                if(string_errors.isEmpty()){
                    errors.setVisibility(View.GONE);
                    save_url_text(url);
                    // играем
                } else{
                    errors.setText(string_errors);
                    errors.setVisibility(View.VISIBLE);
                }


            }
        });

//        binding.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkBox1.setChecked(false);
//                checkBox2.setChecked(false);
//            }
//        });

//        binding.checkBox1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkBox.setChecked(false);
//                checkBox2.setChecked(false);
//            }
//        });

//        binding.checkBox2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkBox.setChecked(false);
//                checkBox1.setChecked(false);
//            }
//        });

    }


    private void load_prreff() {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int seek_progress = Integer.parseInt(preferences.getString(String.valueOf(Day.SEAK_BAR), "30"));
        String url_input = preferences.getString(String.valueOf(Day.URL_INPUT), "");
        ip = preferences.getString(String.valueOf(Day.IP), "");

//        String check_bar = preferences.getString(String.valueOf(Day.STREEM_TYPE), "");
//        switch (check_bar) {
//            case  ("Youtube"):
//                checkBox.setChecked(true);
//                break;
//            case ("Radio"):
//                checkBox2.setChecked(true);
//                break;
//            case ("Twitch"):
//                checkBox1.setChecked(true);
//                break;
//            default:
//                checkBox.setChecked(false);
//                checkBox2.setChecked(false);
//                checkBox1.setChecked(false);
//                break;
//        }
        textView.setText(String.valueOf(seek_progress));
        seekBar.setProgress(seek_progress);
        editText.setText(url_input);
        errors.setVisibility(View.GONE);

    }

    private void save_url_text(String url) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(String.valueOf(Day.URL_INPUT), url);
//        ed .putString(String.valueOf(Day.STREEM_TYPE), check_bar);
        ed.commit();

    }

    private void save_seek_text(String progress) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(String.valueOf(Day.SEAK_BAR), progress);
        ed.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        textView = null;
        editText = null;
        seekBar = null;
//        checkBox = null;
//        checkBox1 = null;
//        checkBox2 = null;
    }

    private class Task extends AsyncTask<String, String, String> {

        public String getArtist(Map<String, String> data) throws IOException {


            if (!data.containsKey("StreamTitle"))
                return "";

            String streamTitle = data.get("StreamTitle");
            int pos = streamTitle.indexOf(" - ");
            String title = (pos == -1) ? streamTitle : streamTitle.substring(0, pos);

            return title.trim();
        }

        public String getTitle(Map<String, String> data) throws IOException {
            if (!data.containsKey("StreamTitle"))
                return "";

            String streamTitle = data.get("StreamTitle");
            System.out.println(streamTitle);
            int pos = streamTitle.indexOf(" - ");
            String artist = (pos == -1) ? streamTitle : streamTitle.substring(pos+2);

            return artist.trim();
        }

        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);
            textView_info.setText(data);
        }


        @Override
        protected String doInBackground(String... strings) {
            System.out.println("ssssssssssssssssssssssssssssssssssssssssssss");
            SharedPreferences preferences;
            HttpURLConnection connection = null;
            InputStream stream = null;
            Map<String, String> metadata = null;
            boolean isError = false;

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Icy-MetaData", "1");
                int metaDataOffset = 0;
                Map<String, List<String>> headers = connection.getHeaderFields();
                stream = connection.getInputStream();
                if (headers.containsKey("icy-metaint")) {
                    // Headers are sent via HTTP
                    metaDataOffset = Integer.parseInt(headers.get("icy-metaint").get(0));
                }

                if (metaDataOffset == 0) {
                    isError = true;
                    return null;
                }
                int b;
                int count = 0;
                int metaDataLength = 4080; // 4080 is the max length
                boolean inData = false;
                StringBuilder metaData = new StringBuilder();
                while ((b = stream.read()) != -1) {
                    count++;

                    // Length of the metadata
                    if (count == metaDataOffset + 1) {
                        metaDataLength = b * 16;
                    }

                    if (count > metaDataOffset + 1 && count < (metaDataOffset + metaDataLength)) {
                        inData = true;
                    } else {
                        inData = false;
                    }
                    if (inData) {
                        if (b != 0) {
                            metaData.append((char) b);
                        }
                    }
                    if (count > (metaDataOffset + metaDataLength)) {
                        break;
                    }

                }
                stream.close();
                metadata = IcyStreamMeta.parseMetadata(metaData.toString());
                System.out.println(metadata);
                if (!metadata.containsKey("StreamTitle"))
                    return "";
                String streamTitle = metadata.get("StreamTitle");
                int pos = streamTitle.indexOf(" - ");
                String author = (pos == -1) ? streamTitle : streamTitle.substring(0, pos);
                String artist = (pos == -1) ? streamTitle : streamTitle.substring(pos+2);
                System.out.println(streamTitle);

                return streamTitle;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection !=null)
                    connection.disconnect();

            }

            return null;
        }
    }
}