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
import javax.measure.quantity.Power;
import javax.measure.unit.Unit;

import static javax.measure.unit.NonSI.*;
import static javax.measure.unit.SI.*;
import com.bashar963.smartcalculator.R;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PowerFragment extends Fragment implements View.OnClickListener{

    private TextView display1,display2;
    private Spinner spinner1,spinner2;
    private String whichDisplay;
    private boolean isChanged;
    private SharedPreferences mPrefs;
    private static final int[] BUTTON_IDS = {
            R.id.zeroButton,R.id.oneButton,R.id.twoButton,R.id.threeButton,R.id.fourButton,R.id.fivebutton,
            R.id.sixButton,R.id.sevenButton,R.id.eightButton,R.id.nineButton,R.id.dotButton,R.id.CEButton};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);

        View mView= inflater.inflate(R.layout.fragment_power, container, false);

        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();


        display1= mView.findViewById(R.id.display1);
        display2= mView.findViewById(R.id.display2);
        spinner1= mView.findViewById(R.id.spinner1);
        spinner2= mView.findViewById(R.id.spinner2);
        getContext();
        mPrefs = getContext().getSharedPreferences("power", Context.MODE_PRIVATE);
        whichDisplay=mPrefs.getString("whichDisplay","display1");
        isChanged=mPrefs.getBoolean("isChanged",false);
        spinner1.setSelection(mPrefs.getInt("sp1",0));
        spinner2.setSelection(mPrefs.getInt("sp2",0));
        display1.setText(mPrefs.getString("display1","0"));
        display2.setText(mPrefs.getString("display2","0"));

        if (whichDisplay.equals("display1")){
            display1.setTypeface(null, Typeface.BOLD);
            display2.setTypeface(null,Typeface.NORMAL);
        }else {
            display2.setTypeface(null, Typeface.BOLD);
            display1.setTypeface(null,Typeface.NORMAL);
        }
        display1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display1.setTypeface(null, Typeface.BOLD);
                display2.setTypeface(null,Typeface.NORMAL);
                whichDisplay="display1";
                isChanged=true;
                convert();
            }
        });
        display2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display2.setTypeface(null, Typeface.BOLD);
                display1.setTypeface(null,Typeface.NORMAL);
                whichDisplay="display2";
                isChanged=true;
                convert();
            }
        });

        ImageButton delButton= mView.findViewById(R.id.backSpaceButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView screen1;
                if (whichDisplay.equals("display1")) screen1 = display1;
                else screen1 = display2;
                if (!screen1.getText().toString().equals("0")&&!screen1.getText().toString().equals("")){
                    screen1.setText(screen1.getText().toString().substring(0, screen1.length() - 1));
                }
                if (screen1.getText().toString().equals("")){
                    screen1.setText("0");
                }
                convert();

            }
        });
        List<Button> buttons;
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
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
        return  mView;
    }

    private void OnNumberPressed(View view) {
        Button b = (Button) view;
        TextView screen1;
        if (whichDisplay.equals("display1")){
            screen1 = display1;
        }
        else{
            screen1 = display2;
        }
        if (isChanged)screen1.setText("");
        isChanged=false;
        if (screen1.getText().toString().length()>=15)return;
        if (b.getId() == R.id.dotButton) {
            if (!screen1.getText().toString().contains(".")) {
                screen1.append(b.getText());
            }
        }
        else if (screen1.getText().toString().equals("0")&&!screen1.getText().toString().contains(".")){
            screen1.setText("");
            screen1.append(b.getText());
            convert();
        }
        else{
            screen1.append(b.getText());
            convert();
        }



    }
    private void ONCLearPressed(){
        display1.setText("0");
        display2.setText("0");
        isChanged=false;
    }
    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("whichDisplay",whichDisplay);
        ed.putBoolean("isChanged",isChanged);
        ed.putInt("sp1",spinner1.getSelectedItemPosition());
        ed.putInt("sp2",spinner2.getSelectedItemPosition());
        ed.putString("display1",display1.getText().toString());
        ed.putString("display2",display2.getText().toString());
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

    private void convert(){
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
        Unit<Power> v;
        UnitConverter u;
       switch (fromUnit){
           case "Watts":
               switch (toUnit){
                   case "Watts":
                       v=WATT;
                       u=v.getConverterTo(WATT);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Kilowatts":
                       v=WATT;
                       u=v.getConverterTo(KILO(WATT));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Horsepower":
                       v=WATT;
                       u=v.getConverterTo(HORSEPOWER);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Foot-pounds/minute":
                       v=WATT;
                       u=v.getConverterTo(WATT.divide(44.25373));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "BTUs/watts":
                       v=WATT;
                       u=v.getConverterTo(WATT.divide(0.056869));
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
           case "Kilowatts":
               switch (toUnit){
                   case "Watts":
                       v=KILO(WATT);
                       u=v.getConverterTo(WATT);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                       if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                       else screen.setText(new DecimalFormat("#.#################").format(n));
                   }else {
                       if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                       else screen.setText(""+(long)n);
                   }
                       break;
                   case "Kilowatts":
                       v=KILO(WATT);
                       u=v.getConverterTo(KILO(WATT));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Horsepower":
                       v=KILO(WATT);
                       u=v.getConverterTo(HORSEPOWER);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Foot-pounds/minute":
                       v=KILO(WATT);
                       u=v.getConverterTo(WATT.divide(44.25373));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "BTUs/watts":
                       v=KILO(WATT);
                       u=v.getConverterTo(WATT.divide(0.056869));
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
           case "Horsepower":
               switch (toUnit){
                   case "Watts":
                       v=HORSEPOWER;
                       u=v.getConverterTo(WATT);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Kilowatts":
                       v=HORSEPOWER;
                       u=v.getConverterTo(KILO(WATT));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Horsepower":
                       v=HORSEPOWER;
                       u=v.getConverterTo(HORSEPOWER);
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Foot-pounds/minute":
                       v=HORSEPOWER;
                       u=v.getConverterTo(WATT.divide(44.25373));
                       n=u.convert(Measure.valueOf(fromNumber,v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "BTUs/watts":
                       v=HORSEPOWER;
                       u=v.getConverterTo(WATT.divide(0.056869));
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
           case "Foot-pounds/minute":
               switch (toUnit) {
                   case "Watts":
                       v = WATT.divide(44.25373);
                       u = v.getConverterTo(WATT);
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Kilowatts":
                       v = WATT.divide(44.25373);
                       u = v.getConverterTo(KILO(WATT));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Horsepower":
                       v = WATT.divide(44.25373);
                       u = v.getConverterTo(HORSEPOWER);
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Foot-pounds/minute":
                       v = WATT.divide(44.25373);
                       u = v.getConverterTo(WATT.divide(44.25373));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "BTUs/watts":
                       v = WATT.divide(44.25373);
                       u = v.getConverterTo(WATT.divide(0.056869));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
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
           case "BTUs/watts":
               switch (toUnit) {
                   case "Watts":
                       v = WATT.divide(0.056869);
                       u = v.getConverterTo(WATT);
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Kilowatts":
                       v = WATT.divide(0.056869);
                       u = v.getConverterTo(KILO(WATT));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Horsepower":
                       v = WATT.divide(0.056869);
                       u = v.getConverterTo(HORSEPOWER);
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "Foot-pounds/minute":
                       v = WATT.divide(0.056869);
                       u = v.getConverterTo(WATT.divide(44.25373));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
                       if (!(n%1==-0)){
                           if (n<0.0000000001) screen.setText(new DecimalFormat("#.######E00").format(n));
                           else screen.setText(new DecimalFormat("#.#################").format(n));
                       }else {
                           if (n>Long.MAX_VALUE) screen.setText(new DecimalFormat("######E00").format(n));
                           else screen.setText(""+(long)n);
                       }
                       break;
                   case "BTUs/watts":
                       v = WATT.divide(0.056869);
                       u = v.getConverterTo(WATT.divide(0.056869));
                       n = u.convert(Measure.valueOf(fromNumber, v).doubleValue(v));
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
