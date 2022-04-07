package com.example.radio_m;

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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.radio_m.databinding.MainWindowBinding;

import java.net.SocketOption;

public class FirstFragment extends Fragment {

    private MainWindowBinding binding;
    private TextView textView;
    SharedPreferences preferences;
    final static String SEAK_BAR = "SEAK_BAR";
    final static String URL_INPUT = "URL_INPUT";
    private SeekBar seekBar;
    private EditText editText;
    private Button connect;

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
        load_prreff();

        binding.setings.setOnClickListener(new View.OnClickListener() {
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
                String url = editText.getText().toString();
                if (url.startsWith("http://") || url.startsWith("https://")){
                    System.out.println("хорошо");
                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    save_url_text(url);
                }
                else
                    System.out.println("плохо");

            }
        });
    }

    private void load_prreff() {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int seek_progress = Integer.parseInt(preferences.getString(SEAK_BAR, "30"));
        String url_input = preferences.getString(URL_INPUT, "");

        textView.setText(String.valueOf(seek_progress));
        seekBar.setProgress(seek_progress);
        editText.setText(url_input);

    }

    private void save_url_text(String url) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(URL_INPUT, url);
        ed.commit();

    }

    private void save_seek_text(String progress) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(SEAK_BAR, progress);
        ed.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        textView = null;
        editText = null;
        seekBar = null;
    }

}