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

import javax.measure.quantity.Mass;
import javax.measure.unit.Unit;

import static javax.measure.unit.NonSI.*;
import static javax.measure.unit.SI.*;
import com.bashar963.smartcalculator.R;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WeightAndMassFragment extends Fragment implements View.OnClickListener {

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

        View mView = inflater.inflate(R.layout.fragment_weight_and_mass, container, false);

        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();

        display1 = (TextView) mView.findViewById(R.id.display1);
        display2 = (TextView) mView.findViewById(R.id.display2);
        spinner1 = (Spinner) mView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) mView.findViewById(R.id.spinner2);
        getContext();
        mPrefs = getContext().getSharedPreferences("weight", Context.MODE_PRIVATE);
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

        ImageButton delButton = (ImageButton) mView.findViewById(R.id.backSpaceButton);
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
            Button button = (Button) mView.findViewById(id);
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
        if (whichDisplay.equals("display1")) {
            fromUnit = spinner1.getSelectedItem().toString();
            toUnit = spinner2.getSelectedItem().toString();
            fromNumber = Double.valueOf(display1.getText().toString());
            screen = display2;
        } else {
            fromUnit = spinner2.getSelectedItem().toString();
            toUnit = spinner1.getSelectedItem().toString();
            fromNumber = Double.valueOf(display2.getText().toString());
            screen = display1;
        }
        double n;
        Unit<Mass> v;
        UnitConverter u;
        switch (fromUnit){
            case "Carats":
                switch (toUnit){
                    case "Carats":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= GRAM.divide(5);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Milligrams":
                switch (toUnit){
                    case "Carats":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= MILLI(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Centigrams":
                switch (toUnit){
                    case "Carats":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= CENTI(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Decigrams":
                switch (toUnit){
                    case "Carats":
                        v= DECI(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                        if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                        else screen.setText(new DecimalFormat("#.#################").format(n));
                    }else {
                        if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                        else screen.setText(""+(long)n);
                    }
                        break;
                    case "Milligrams":
                        v= DECI(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= DECI(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= DECI(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= DECI(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= DECI(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= DECI(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= DECI(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= DECI(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= DECI(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= DECI(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= DECI(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= DECI(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= DECI(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Grams":
                switch (toUnit){
                    case "Carats":
                        v= GRAM;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= GRAM;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= GRAM;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= GRAM;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= GRAM;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= GRAM;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                        if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                        else screen.setText(new DecimalFormat("#.#################").format(n));
                    }else {
                        if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                        else screen.setText(""+(long)n);
                    }
                        break;
                    case "Hectograms":
                        v= GRAM;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= GRAM;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= GRAM;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= GRAM;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= GRAM;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= GRAM;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= GRAM;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= GRAM;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Dekagrams":
                switch (toUnit){
                    case "Carats":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= DEKA(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Hectograms":
                switch (toUnit){
                    case "Carats":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= HECTO(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Kilograms":
                switch (toUnit){
                    case "Carats":
                        v= KILO(GRAM);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= KILO(GRAM);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= KILO(GRAM);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= KILO(GRAM);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= KILO(GRAM);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= KILO(GRAM);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= KILO(GRAM);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= KILO(GRAM);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= KILO(GRAM);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= KILO(GRAM);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= KILO(GRAM);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= KILO(GRAM);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= KILO(GRAM);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= KILO(GRAM);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Metric tonnes":
                switch (toUnit){
                    case "Carats":
                        v= METRIC_TON;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= METRIC_TON;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= METRIC_TON;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= METRIC_TON;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= METRIC_TON;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= METRIC_TON;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= METRIC_TON;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= METRIC_TON;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= METRIC_TON;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= METRIC_TON;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= METRIC_TON;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= METRIC_TON;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= METRIC_TON;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= METRIC_TON;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Ounces":
                switch (toUnit){
                    case "Carats":
                        v= OUNCE;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= OUNCE;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= OUNCE;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= OUNCE;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= OUNCE;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= OUNCE;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= OUNCE;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= OUNCE;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= OUNCE;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= OUNCE;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= OUNCE;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= OUNCE;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= OUNCE;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= OUNCE;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Pounds":
                switch (toUnit){
                    case "Carats":
                        v= POUND;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= POUND;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= POUND;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= POUND;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= POUND;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= POUND;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= POUND;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= POUND;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= POUND;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= POUND;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= POUND;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= POUND;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= POUND;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= POUND;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Stone":
                switch (toUnit){
                    case "Carats":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= GRAM.divide(0.000157);
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Short tons(US)":
                switch (toUnit){
                    case "Carats":
                        v= TON_US;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= TON_US;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= TON_US;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= TON_US;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= TON_US;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= TON_US;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= TON_US;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= TON_US;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= TON_US;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v)); if (!(n%1==-0)){
                        if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                        else screen.setText(new DecimalFormat("#.#################").format(n));
                    }else {
                        if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                        else screen.setText(""+(long)n);
                    }
                        break;
                    case "Ounces":
                        v= TON_US;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= TON_US;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= TON_US;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= TON_US;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= TON_US;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
            case "Long tons(UK)":
                switch (toUnit){
                    case "Carats":
                        v= TON_UK;
                        u=v.getConverterTo(GRAM.divide(5));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Milligrams":
                        v= TON_UK;
                        u=v.getConverterTo(MILLI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Centigrams":
                        v= TON_UK;
                        u=v.getConverterTo(CENTI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Decigrams":
                        v= TON_UK;
                        u=v.getConverterTo(DECI(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Grams":
                        v= TON_UK;
                        u=v.getConverterTo(GRAM);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Dekagrams":
                        v= TON_UK;
                        u=v.getConverterTo(DEKA(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Hectograms":
                        v= TON_UK;
                        u=v.getConverterTo(HECTO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Kilograms":
                        v= TON_UK;
                        u=v.getConverterTo(KILO(GRAM));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Metric tonnes":
                        v= TON_UK;
                        u=v.getConverterTo(METRIC_TON);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Ounces":
                        v= TON_UK;
                        u=v.getConverterTo(OUNCE);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Pounds":
                        v= TON_UK;
                        u=v.getConverterTo(POUND);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Stone":
                        v= TON_UK;
                        u=v.getConverterTo(GRAM.divide(0.000157));
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Short tons(US)":
                        v= TON_UK;
                        u=v.getConverterTo(TON_US);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                    case "Long tons(UK)":
                        v= TON_UK;
                        u=v.getConverterTo(TON_UK);
                        n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                        if (!(n%1==-0)){
                            if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                            else screen.setText(new DecimalFormat("#.#################").format(n));
                        }else {
                            if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                            else screen.setText(""+(long)n);
                        }
                        break;
                }
                break;
        }
    }
}