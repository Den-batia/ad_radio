package com.example.radio_m;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.radio_m.databinding.MainWindowBinding;

import java.net.SocketOption;

public class FirstFragment extends Fragment {

    private MainWindowBinding binding;
    SharedPreferences preferences;
    private TextView textView;
    private SeekBar seekBar;
    private EditText editText;
    private TextView errors;
    private Button connect;
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
//        checkBox = view.findViewById(R.id.checkBox);
//        checkBox1 = view.findViewById(R.id.checkBox1);
//        checkBox2 = view.findViewById(R.id.checkBox2);
        errors = view.findViewById(R.id.errors);
//        errors.setVisibility(View.GONE);
        load_prreff();

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
                save_seek_text(progress);
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
                if(url.isEmpty())
                    string_errors += "* Введите 'url' аудио потока\n";
                else if(!url.startsWith("http://"))
                        string_errors += "* Адрес должен ачинаться с 'http://' или 'https://'\n";


                if(url.isEmpty()){
                    url = "";
                }

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

}