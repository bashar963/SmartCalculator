package com.bashar963.smartcalculator.Fragments;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bashar963.smartcalculator.ArityParser;
import com.bashar963.smartcalculator.R;


import java.util.ArrayList;
import java.util.List;


public class standard extends Fragment implements View.OnClickListener{

    private static final int[] BUTTON_IDS = {
            R.id.zeroButton,R.id.oneButton,R.id.twoButton,R.id.threeButton,R.id.fourButton,R.id.fiveButton,
            R.id.sixButton,R.id.sevenButton,R.id.eightButton,R.id.nineButton,R.id.dotButton, R.id.equalButton,R.id.plusButton,
            R.id.minusButton,R.id.multButton,R.id.divisButton,R.id.plsminsButton,R.id.CButton,R.id.CEButton,R.id.sqRButton,R.id.percentButton,R.id.oneOverXButon
    };



    private TextView screen1;
    private TextView screen2;
    private StringBuilder display1=new StringBuilder();
    private boolean justEvaluated;
    private String lastAnswer;
    private SharedPreferences mPrefs;
    private boolean firstTime;
    private boolean digitEntered;
    ArityParser p = new ArityParser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);


        View mView= inflater.inflate(R.layout.fragment_standard, container, false);
        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();


        screen1= mView.findViewById(R.id.display1);
        screen2=mView.findViewById(R.id.display2);
        screen1.setFocusable(false);
        screen2.setMovementMethod(new ScrollingMovementMethod());
        getContext();
        mPrefs = getContext().getSharedPreferences("standard", Context.MODE_PRIVATE);
        String s1=mPrefs.getString("screen1","0");
        String s2=mPrefs.getString("screen2","");
        lastAnswer=mPrefs.getString("lastAnswer","");
        justEvaluated=mPrefs.getBoolean("justEvaluated",false);
        firstTime=mPrefs.getBoolean("firstTime",true);
        digitEntered=mPrefs.getBoolean("digitEntered",false);
        display1.append(mPrefs.getString("display1",""));
        screen1.setText(s1);
        screen2.setText(s2);


        List<Button> buttons;
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = mView.findViewById(id);
            button.setOnClickListener(this);
            buttons.add(button);
        }
        ImageButton delButton=mView.findViewById(R.id.delButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!screen1.getText().toString().equals("0")&&!screen1.getText().toString().equals("")){
                    screen1.setText(screen1.getText().toString().substring(0, screen1.length() - 1));
                } if (screen1.getText().toString().equals("")){
                    screen1.setText("0");
                }

            }
        });
        ImageButton sqButton=mView.findViewById(R.id.sqButton);
        sqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'){
                        if (screen1.getText().toString().equals("")||screen1.getText().toString().equals("0")){
                        }else {
                            try {
                                String expression = screen2.getText().toString()+screen1.getText().toString()+"^2";
                                screen2.setText(expression);
                                lastAnswer= p.parse(expression);
                            }catch (Exception e){
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                            if (lastAnswer.equals("ERROR: null")){
                                screen1.setText("0");
                            }else {
                                screen1.setText(lastAnswer);
                                justEvaluated=true;
                                digitEntered=false;
                            }
                    }

                    }
                }catch (Exception ignored){

                }


            }
        });
        return  mView;
    }

    private void OnNumberPressed(View view){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'){
                Button b= (Button)view;
                if (justEvaluated) {
                    screen1.setText("0");
                    justEvaluated=false;
                }
                if (screen1.getText().toString().equals("0")&&!b.getText().toString().equals(".")&&b.getId()!=R.id.plsminsButton){
                    screen1.setText("");
                }
                if (b.getId()==R.id.dotButton){
                    if (!screen1.getText().toString().contains(".")){
                        screen1.append(b.getText());
                    }
                }else if (b.getId()==R.id.plsminsButton){
                    if (!screen1.getText().toString().equals("")||!screen1.getText().toString().equals("0")){
                        if (screen1.getText().toString().contains(".")){
                            double i=Double.valueOf(screen1.getText().toString());
                            i*=-1;
                            screen1.setText(String.valueOf(i));
                        }else {
                            long i=Long.valueOf(screen1.getText().toString());
                            i*=-1;
                            screen1.setText(String.valueOf(i));
                        }
                    }
                }
                else {
                    screen1.append(b.getText());
                }

                digitEntered=true;
                firstTime=false;
            }else {
                screen2.setText("");
                OnNumberPressed(view);
            }
        }catch (Exception ignored){}

    }
    private void ONCLearPressed(View view){
        if (view.getId()==R.id.CButton){
            screen1.setText("0");
            screen2.setText("");lastAnswer = "";
            justEvaluated =false;firstTime=true;digitEntered=false;
        }else {
            screen1.setText("0");
        }
    }
    private void ONEqualPressed(){
        if (screen2.getText().toString().equals("")||screen1.getText().toString().equals("")){
            return;
        }
        if (!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+')&&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-')
                &&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×')&&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷')){
            try {
                String expression = screen2.getText().toString();
                lastAnswer= p.parse(expression);
            }catch (Exception e){
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            if (lastAnswer.equals("ERROR: null")){
                screen1.setText("0");
            }else {
                screen1.setText(lastAnswer);
                screen2.setText("");
            }
        }else {
            try {
                String expression = screen2.getText().toString()+screen1.getText().toString();
                lastAnswer= p.parse(expression);
            }catch (Exception e){
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            if (lastAnswer.equals("ERROR: null")){
                screen1.setText("0");
            }else {
                screen1.setText(lastAnswer);
                screen2.setText("");
            }
        }

    }
    private void ONOperPressed(View view){
        Button b=(Button)view;
        if (screen1.getText().toString().equals("")&&!firstTime)return;
        if (!screen2.getText().toString().equals("")&&!digitEntered){
            switch (screen2.getText().toString().charAt(screen2.getText().toString().length()-1)){
                case '+':
                    String res=screen2.getText().toString().substring(0,screen2.getText().toString().length()-1);
                    display1.append(res);
                    digitEntered=false;
                    screen2.setText(display1+b.getText().toString());
                    break;
                case '-':
                    res=screen2.getText().toString().substring(0,screen2.getText().toString().length()-1);
                    display1.append(res);
                    digitEntered=false;
                    screen2.setText(display1+b.getText().toString());
                    break;
                case '÷':
                    res=screen2.getText().toString().substring(0,screen2.getText().toString().length()-1);
                    display1.append(res);
                    digitEntered=false;
                    screen2.setText(display1+b.getText().toString());
                    break;
                case '×':
                    res=screen2.getText().toString().substring(0,screen2.getText().toString().length()-1);
                    display1.append(res);
                    screen2.setText(display1+b.getText().toString());
                    digitEntered=false;
                    break;
                default:
                    res=screen2.getText().toString();
                    display1.append(res);
                    screen2.setText(display1+b.getText().toString());
                    digitEntered=false;
                    break;
            }
        }else if (!firstTime){
            display1.append(screen1.getText().toString());
            screen2.setText(screen2.getText().toString()+display1+b.getText().toString());
            digitEntered=false;
            firstTime=false;
        }


        try {
            if (screen2.getText().toString().equals(""))return;
            String expression = screen2.getText().toString().substring(0,screen2.getText().toString().length()-1);
            lastAnswer= p.parse(expression);
        }catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        if (lastAnswer.equals("ERROR: null")){
            screen1.setText("0");
        }else {
            screen1.setText(lastAnswer);
            justEvaluated=true;
        }

        display1.delete(0,display1.length());
    }
    @Override
    public void onClick(View v) {
        if (screen1.getText().toString().equals("Infinity")){
            screen1.setText("");
        }
        switch (v.getId()) {
            case R.id.zeroButton:
            case R.id.oneButton:
            case R.id.twoButton:
            case R.id.threeButton:
            case R.id.fiveButton:
            case R.id.sixButton:
            case R.id.sevenButton:
            case R.id.fourButton:
            case R.id.eightButton:
            case R.id.nineButton:
            case R.id.dotButton:
            case R.id.plsminsButton:
                OnNumberPressed(v);
                break;
            case R.id.equalButton:
                ONEqualPressed();
                break;
            case R.id.CButton:
            case R.id.CEButton:
                ONCLearPressed(v);
                break;
            case R.id.percentButton:
                try {
                    if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'){
                        if (screen1.getText().toString().equals("0")||screen1.getText().toString().equals("")){
                            screen1.setText("0");
                            return;
                        }
                        try {
                            if (screen2.getText().toString().equals("")){
                                String expression = screen1.getText().toString()+"%";
                                String per=p.parse(screen1.getText().toString()+"%");
                                screen2.setText(screen2.getText().toString()+per);
                                lastAnswer= p.parse(expression);
                            }
                            else {
                                String expression=screen2.getText().toString().substring(0,screen2.getText().length()-1);
                                String s=screen1.getText().toString();
                                expression=p.parse(expression);
                                String answer="("+expression+"*"+s+")/100";
                                lastAnswer=p.parse(answer);
                                screen2.setText(screen2.getText().toString()+lastAnswer);
                            }

                        }catch (Exception e){
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        if (lastAnswer.equals("ERROR: null")){
                            screen1.setText("0");
                        }else {
                            screen1.setText(lastAnswer);
                            justEvaluated=true;digitEntered=false;
                        }
                    }
                }catch (Exception ignored){

                }

                break;
            case R.id.sqRButton:
                try{
                    if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'){
                        if (!screen1.getText().toString().equals("")){
                            try {
                                String expression = "√("+screen1.getText().toString()+")";
                                screen2.setText(screen2.getText().toString()+expression);
                                lastAnswer= p.parse(expression);
                            }catch (Exception e){
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                            if (lastAnswer.equals("ERROR: null")){
                                screen1.setText("0");
                            }else {
                                screen1.setText(lastAnswer);
                                justEvaluated=true;

                            }
                        }
                        digitEntered=false;
                    }
                }catch (Exception ignored){

                }

                break;
            case R.id.oneOverXButon:
                if (!screen1.getText().toString().equals("")){
                try {
                    String expression = "1/"+screen1.getText().toString();
                    screen2.setText(screen2.getText().toString()+expression);
                    lastAnswer= p.parse(expression);
                }catch (Exception e){
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                if (lastAnswer.equals("ERROR: null")){
                    screen1.setText("0");
                }else {
                    screen1.setText(lastAnswer);
                    justEvaluated=true;
                }
            }
            digitEntered=false;

                break;
            case R.id.plusButton:
            case R.id.minusButton:
            case R.id.multButton:
            case R.id.divisButton:ONOperPressed(v);
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("screen1", screen1.getText().toString());
        ed.putString("screen2", screen2.getText().toString());
        ed.putString("lastAnswer",lastAnswer);
        ed.putString("display1",String.valueOf(display1));
        ed.putBoolean("justEvaluated",justEvaluated);
        ed.putBoolean("firstTime",firstTime);
        ed.putBoolean("digitEntered",digitEntered);
        ed.apply();
    }

    }





