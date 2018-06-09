package com.bashar963.smartcalculator.Fragments;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.DataAmount;
import javax.measure.unit.Unit;

import static javax.measure.unit.NonSI.*;
import static javax.measure.unit.SI.*;
import com.bashar963.smartcalculator.R;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment implements View.OnClickListener {

    private TextView display1, display2;
    private Spinner spinner1, spinner2;
    private String whichDisplay;
    private boolean isChanged;
    private SharedPreferences mPrefs;
    private static final int[] BUTTON_IDS = {
            R.id.zeroButton, R.id.oneButton, R.id.twoButton, R.id.threeButton, R.id.fourButton, R.id.fivebutton,
            R.id.sixButton, R.id.sevenButton, R.id.eightButton, R.id.nineButton, R.id.dotButton, R.id.CEButton};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);

        View mView = inflater.inflate(R.layout.fragment_data, container, false);

        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();

        display1 = mView.findViewById(R.id.display1);
        display2 = mView.findViewById(R.id.display2);
        spinner1 = mView.findViewById(R.id.spinner1);
        spinner2 = mView.findViewById(R.id.spinner2);
        getContext();
        mPrefs = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        whichDisplay = mPrefs.getString("whichDisplay", "display1");
        isChanged = mPrefs.getBoolean("isChanged", false);
        spinner1.setSelection(mPrefs.getInt("sp1", 0));
        spinner2.setSelection(mPrefs.getInt("sp2", 0));
        display1.setText(mPrefs.getString("display1", "0"));
        display2.setText(mPrefs.getString("display2", "0"));

        if (whichDisplay.equals("display1")) {
            display1.setTypeface(null, Typeface.BOLD);
            display2.setTypeface(null, Typeface.NORMAL);
        } else {
            display2.setTypeface(null, Typeface.BOLD);
            display1.setTypeface(null, Typeface.NORMAL);
        }
        display1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display1.setTypeface(null, Typeface.BOLD);
                display2.setTypeface(null, Typeface.NORMAL);
                whichDisplay = "display1";
                isChanged = true;
                convert();
            }
        });
        display2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display2.setTypeface(null, Typeface.BOLD);
                display1.setTypeface(null, Typeface.NORMAL);
                whichDisplay = "display2";
                isChanged = true;
                convert();
            }
        });

        ImageButton delButton = mView.findViewById(R.id.backSpaceButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView screen1;
                if (whichDisplay.equals("display1")) screen1 = display1;
                else screen1 = display2;
                if (!screen1.getText().toString().equals("0") && !screen1.getText().toString().equals("")) {
                    screen1.setText(screen1.getText().toString().substring(0, screen1.length() - 1));
                }
                if (screen1.getText().toString().equals("")) {
                    screen1.setText("0");
                }
                convert();

            }
        });
        List<Button> buttons;
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for (int id : BUTTON_IDS) {
            Button button = mView.findViewById(id);
            button.setOnClickListener(this);
            buttons.add(button);
        }
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                convert();
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                convert();
            }
        });
        return mView;
    }

    private void OnNumberPressed(View view) {
        Button b = (Button) view;
        TextView screen1;
        if (whichDisplay.equals("display1")) {
            screen1 = display1;
        } else {
            screen1 = display2;
        }
        if (isChanged) screen1.setText("");
        isChanged = false;
        if (screen1.getText().toString().length() >= 15) return;
        if (b.getId() == R.id.dotButton) {
            if (!screen1.getText().toString().contains(".")) {
                screen1.append(b.getText());
            }
        } else if (screen1.getText().toString().equals("0") && !screen1.getText().toString().contains(".")) {
            screen1.setText("");
            screen1.append(b.getText());
            convert();
        } else {
            screen1.append(b.getText());
            convert();
        }


    }

    private void ONCLearPressed() {
        display1.setText("0");
        display2.setText("0");
        isChanged = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("whichDisplay", whichDisplay);
        ed.putBoolean("isChanged", isChanged);
        ed.putInt("sp1", spinner1.getSelectedItemPosition());
        ed.putInt("sp2", spinner2.getSelectedItemPosition());
        ed.putString("display1", display1.getText().toString());
        ed.putString("display2", display2.getText().toString());
        ed.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zeroButton:
            case R.id.oneButton:
            case R.id.twoButton:
            case R.id.threeButton:
            case R.id.fivebutton:
            case R.id.sixButton:
            case R.id.sevenButton:
            case R.id.fourButton:
            case R.id.eightButton:
            case R.id.nineButton:
            case R.id.dotButton:
                OnNumberPressed(v);
                break;
            case R.id.CEButton:
                ONCLearPressed();
                break;
        }

    }

    private void convert() {



        String fromUnit;
        String toUnit;
        TextView screen;
        double fromNumber;
        if (whichDisplay.equals("display1")){
            fromUnit =spinner1.getSelectedItem().toString();
            toUnit =spinner2.getSelectedItem().toString();
            fromNumber =Double.valueOf(display1.getText().toString());
            screen =display2;
        }else {
            fromUnit =spinner2.getSelectedItem().toString();
            toUnit =spinner1.getSelectedItem().toString();
            fromNumber =Double.valueOf(display2.getText().toString());
            screen =display1;
        }
        double n;
        Unit<DataAmount> v=BIT;
        UnitConverter u=v.getConverterTo(BIT);
        switch (fromUnit){
            case  "Bits":
                switch (toUnit){
                    case  "Bits":
                        v=BIT;
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=BIT;
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=BIT;
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=BIT;
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=BIT;
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=BIT;
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=BIT;
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=BIT;
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=BIT;
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=BIT;
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=BIT;
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=BIT;
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=BIT;
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=BIT;
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=BIT;
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=BIT;
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=BIT;
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=BIT;
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Bytes":
                switch (toUnit){
                    case  "Bits":
                        v=BYTE;
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=BYTE;
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=BYTE;
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=BYTE;
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=BYTE;
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=BYTE;
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=BYTE;
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=BYTE;
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=BYTE;
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=BYTE;
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=BYTE;
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=BYTE;
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=BYTE;
                        u=v.getConverterTo(EXA(BIT));

                        break;
                    case "Exabytes":
                        v=BYTE;
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=BYTE;
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=BYTE;
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=BYTE;
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=BYTE;
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Kilobits":
                switch (toUnit){
                    case  "Bits":
                        v=KILO(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=KILO(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=KILO(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=KILO(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Kilobytes":
                switch (toUnit){
                    case  "Bits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=KILO(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=KILO(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Megabits":
                switch (toUnit){
                    case  "Bits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(EXA(BYTE));

                        break;
                    case "Zetabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=MEGA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=MEGA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Megabytes":
                switch (toUnit){
                    case  "Bits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));

                        break;
                    case "Exabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=MEGA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Gigabits":
                switch (toUnit){
                    case  "Bits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(KILO(BYTE));

                        break;
                    case "Megabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=GIGA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=GIGA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Gigabytes":
                switch (toUnit){
                    case  "Bits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=GIGA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Terabits":
                switch (toUnit){
                    case  "Bits":
                        v=TERA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=TERA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=TERA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=TERA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Terabytes":
                switch (toUnit){
                    case  "Bits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=TERA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=TERA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Petabits":
                switch (toUnit){
                    case  "Bits":
                        v=PETA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=PETA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=PETA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=PETA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Petabytes":
                switch (toUnit){
                    case  "Bits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=PETA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=PETA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Exabits":
                switch (toUnit){
                    case  "Bits":
                        v=EXA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=EXA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=EXA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=EXA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Exabytes":
                switch (toUnit){
                    case  "Bits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));

                        break;
                    case "Yottabits":
                        v=EXA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));

                        break;
                    case "Yottabytes":
                        v=EXA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Zetabits":
                switch (toUnit){
                    case  "Bits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=ZETTA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Zetabytes":
                switch (toUnit){
                    case  "Bits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));

                        break;
                    case "Gigabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));

                        break;
                    case "Terabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=ZETTA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Yottabits":
                switch (toUnit){
                    case  "Bits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=YOTTA(BIT);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
            case "Yottabytes":
                switch (toUnit){
                    case  "Bits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(BIT);
                        break;
                    case "Bytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(BYTE);
                        break;
                    case "Kilobits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(KILO(BIT));
                        break;
                    case "Kilobytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(KILO(BYTE));
                        break;
                    case "Megabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(MEGA(BIT));
                        break;
                    case "Megabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(MEGA(BYTE));
                        break;
                    case "Gigabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(GIGA(BIT));
                        break;
                    case "Gigabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(GIGA(BYTE));
                        break;
                    case "Terabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(TERA(BIT));
                        break;
                    case "Terabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(TERA(BYTE));
                        break;
                    case "Petabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(PETA(BIT));
                        break;
                    case "Petabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(PETA(BYTE));
                        break;
                    case "Exabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(EXA(BIT));
                        break;
                    case "Exabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(EXA(BYTE));
                        break;
                    case "Zetabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(ZETTA(BIT));
                        break;
                    case "Zetabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(ZETTA(BYTE));
                        break;
                    case "Yottabits":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(YOTTA(BIT));
                        break;
                    case "Yottabytes":
                        v=YOTTA(BYTE);
                        u=v.getConverterTo(YOTTA(BYTE));
                        break;
                }
                break;
        }
        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
        if (!(n%1==-0)){
            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
            else screen.setText(new DecimalFormat("#.#################").format(n));
        }else {
            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
            else screen.setText(""+(long)n);
        }
    }
}


