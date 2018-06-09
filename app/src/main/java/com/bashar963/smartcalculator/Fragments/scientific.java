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

public class scientific extends Fragment implements View.OnClickListener{


    private static final int[] BUTTON_IDS = {
            R.id.zeroButton,R.id.oneButton,R.id.twoButton,R.id.threeButton,R.id.fourButton,R.id.fiveButton,
            R.id.sixButton,R.id.sevenButton,R.id.eightButton,R.id.nineButton,R.id.dotButton, R.id.equalButton,R.id.plusButton,
            R.id.minusButton,R.id.multButton,R.id.divisButton,R.id.plsminsButton,R.id.CButton,R.id.CEButton,R.id.PIButton,R.id.factButton
            ,R.id.liftBrackets,R.id.rightBrackets,R.id.DegOrRad
    };

    private ImageButton sin;
    private ImageButton cos;
    private ImageButton tan;
    private ImageButton sinh;
    private ImageButton cosh;
    private ImageButton tanh;
    private ImageButton log;
    private ImageButton sqrt;
    private ImageButton xtoy;
    private ImageButton square;
    private ImageButton oneOverx;

    private TextView screen1;
    private TextView screen2;
    private StringBuilder display1=new StringBuilder();
    private boolean justEvaluated;
    private String lastAnswer;
    private SharedPreferences mPrefs;

    private boolean firstTime;
    private boolean digitEntered;
    ArityParser p = new ArityParser();
    private boolean isDeg=true;
    private boolean isChangeButtonPressed=false;
    private int numOfLiftBrackets=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);

        View mView = inflater.inflate(R.layout.fragment_scientific, container, false);

        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();



        screen1= mView.findViewById(R.id.display1);
        screen2= mView.findViewById(R.id.display2);
        screen1.setFocusable(false);
        screen2.setMovementMethod(new ScrollingMovementMethod());
        getContext();
        mPrefs = getContext().getSharedPreferences("scientific", Context.MODE_PRIVATE);
        String s1=mPrefs.getString("screen1","0");
        String s2=mPrefs.getString("screen2","");
        lastAnswer=mPrefs.getString("lastAnswer","");
        justEvaluated=mPrefs.getBoolean("justEvaluated",false);
        firstTime=mPrefs.getBoolean("firstTime",true);
        digitEntered=mPrefs.getBoolean("digitEntered",false);
        display1.append(mPrefs.getString("display1",""));
        screen1.setText(s1);
        screen2.setText(s2);


        sin= mView.findViewById(R.id.sinButton);
        cos= mView.findViewById(R.id.cosButton);
        tan= mView.findViewById(R.id.tanButton);
        sinh= mView.findViewById(R.id.sinhButton);
        cosh= mView.findViewById(R.id.coshButton);
        tanh= mView.findViewById(R.id.tanhButton);
        log= mView.findViewById(R.id.logButton);
        sqrt= mView.findViewById(R.id.sqRButton);
        xtoy= mView.findViewById(R.id.xtoyButton);
        square= mView.findViewById(R.id.sqButton);
        oneOverx= mView.findViewById(R.id.oneOverXButon);
        sin.setTag("sin");
        cos.setTag("cos");
        tan.setTag("tan");
        sinh.setTag("sinh");
        cosh.setTag("cosh");
        tanh.setTag("tanh");
        log.setTag("log");
        sqrt.setTag("sqrt");
        xtoy.setTag("xtoy");
        square.setTag("square");
        oneOverx.setTag("oneOverx");
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          sin();
            }
        });
        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cos();
            }
        });
        tan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tan();
            }
        });
        sinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hyp(v);
            }
        });
        cosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hyp(v);
            }
        });
        tanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hyp(v);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOrln(v);
            }
        });
        sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqrt();
            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Square();
            }
        });
        oneOverx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneOverx();
            }
        });
        xtoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqrtoy();
            }
        });
        List<Button> buttons;
        buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            Button button = mView.findViewById(id);
            button.setOnClickListener(this);
            buttons.add(button);
        }


        final ImageButton change = mView.findViewById(R.id.changeStateButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        ImageButton delButton= mView.findViewById(R.id.delButton);
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
        return mView;
    }
    private void hyp(View view){
        ImageButton i=(ImageButton)view;

            if (sin.getTag().equals("sinh")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = i.getTag().toString()+"("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = i.getTag().toString()+"("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
    }
    private  void logOrln(View view){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
                ImageButton i=(ImageButton)view;

                if (log.getTag().equals("log")){
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = i.getTag().toString()+"("+screen1.getText().toString()+")";
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
                }else {
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = i.getTag().toString()+"("+screen1.getText().toString()+")";
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
            }
        }catch (Exception ignored){}

    }
    private void sqrt(){
        try{
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
                if (log.getTag().equals("log")){
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
                }else {
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = "y "+screen1.getText().toString()+" root( ";
                            screen2.setText(screen2.getText().toString()+expression);
                        }catch (Exception e){
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        if (lastAnswer.equals("ERROR: null")){
                            screen1.setText("0");
                        }else {
                            justEvaluated=true;
                        }
                    }
                    digitEntered=false;
                }
            }
        }catch (Exception ignored){}


    }
    private void sqrtoy(){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
                if (xtoy.getTag().equals("xtoy")){
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = screen1.getText().toString()+"^";
                            screen2.setText(screen2.getText().toString()+expression);

                        }catch (Exception e){
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        if (lastAnswer.equals("ERROR: null")){
                            screen1.setText("0");
                        }else {

                            justEvaluated=true;
                        }
                    }
                    digitEntered=false;
                }else {
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = "10^"+screen1.getText().toString();
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
            }
        }catch (Exception ignored){}


    }
    private void Square(){
        try{
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
                if (square.getTag().equals("square")){
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = screen1.getText().toString()+"^2";
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
                }else {
                    if (!screen1.getText().toString().equals("")){
                        try {
                            String expression = screen1.getText().toString()+"^3";
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
            }

        }catch (Exception ignored){}

    }
    private void OneOverx(){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
            if (oneOverx.getTag().equals("oneOverx")){
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

    }else {
        if (!screen1.getText().toString().equals("")){
            try {
                String expression = "e^"+screen1.getText().toString();
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
}

}catch (Exception ignored){

}

    }
    private void sin(){
        if (!isDeg){
            if (sin.getTag().equals("sin")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "sin("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "asin("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
        }else {
            if (sin.getTag().equals("sin")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression ="sin("+screen1.getText().toString()+"*π/180)";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "asin("+screen1.getText().toString()+")*180/π";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }

        }
    }
    private void cos(){
        if (!isDeg){
            if (cos.getTag().equals("cos")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "cos("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "acos("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
        }else {
            if (cos.getTag().equals("cos")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression ="cos("+screen1.getText().toString()+"*π/180)";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "acos("+screen1.getText().toString()+")*180/π)";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }

        }
    }
    private void tan(){
        if (!isDeg){
            if (tan.getTag().equals("tan")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "tan("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "atan("+screen1.getText().toString()+")";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
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
        }else {
            if (tan.getTag().equals("tan")){
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression ="tan("+screen1.getText().toString()+"*π/180)";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }else {
                if (!screen1.getText().toString().equals("")){
                    try {
                        String expression = "atan("+screen1.getText().toString()+")*180/π)";
                        lastAnswer= p.parse(expression);
                        screen2.append(lastAnswer);
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (lastAnswer.equals("ERROR: null")){
                        screen1.setText("0");
                    }else {
                        screen1.setText(String.valueOf(lastAnswer));
                        justEvaluated=true;
                    }
                }
                digitEntered=false;
            }

        }
    }
    private void OnNumberPressed(View view){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==' '||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='^'){
                Button b= (Button)view;
                if (justEvaluated) {
                    screen1.setText("0");
                    justEvaluated=false;
                }
                if (screen1.getText().toString().equals("0")&&!b.getText().toString().equals(".")&&b.getId()!=R.id.plsminsButton||screen1.getText().toString().equals("3.141592653589793")){
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
                } else if (b.getId()==R.id.PIButton){
                    screen1.setText("");
                    if (!screen1.getText().toString().equals("3.141592653589793")){
                        screen1.append("3.141592653589793");
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
            numOfLiftBrackets=0;
            justEvaluated =false;firstTime=true;digitEntered=false;
        }else {
            screen1.setText("0");
        }
    }
    private void ONEqualPressed(){
        if (screen2.getText().toString().equals("")||screen1.getText().toString().equals("")){
            return;
        }
        if (screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==' '){
            try {
                String expression = screen2.getText().toString()+screen1.getText().toString()+" )";
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
            return;
        }
        if (!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+')&&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-')
                &&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×')&&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷')&&!(screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='^')){
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
        if (!screen2.getText().toString().equals("")){
            if (screen2.getText().toString().charAt(screen2.getText().length()-1)==' '){
                screen2.setText(screen2.getText().toString()+screen1.getText().toString()+" )");
                screen1.setText("");
            }
        }

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
                case ')':
                    res=screen2.getText().toString();
                    display1.append(res);
                    screen2.setText(res+b.getText().toString());
                    digitEntered=false;
                    break;
                case '!':
                    res=screen2.getText().toString();
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
            if (!screen2.getText().toString().equals("")){
                if (screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'){
                    display1.append(screen1.getText().toString());
                    screen2.setText(screen2.getText().toString()+b.getText().toString());
                    digitEntered=false;
                    firstTime=false;
            }
                else {
                    display1.append(screen1.getText().toString());
                    screen2.setText(screen2.getText().toString()+display1+b.getText().toString());
                    digitEntered=false;
                    firstTime=false;
                }
            }else {
                display1.append(screen1.getText().toString());
                screen2.setText(screen2.getText().toString()+display1+b.getText().toString());
                digitEntered=false;
                firstTime=false;
            }
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
    private void BracketsInput(View view){
        try {
            if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==' '||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='('||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)==')'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='0'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='1'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='2'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='3'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='4'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='5'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='6'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='7'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='8'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='9'){
                Button b=(Button)view;
                if (b.getId()==R.id.liftBrackets){
                    if (!screen2.getText().toString().equals("")){
                        if (screen2.getText().toString().charAt(screen2.getText().length()-1)==')'){
                            screen2.setText("");
                        }
                    }
                    digitEntered=true;
                    screen2.setText(screen2.getText().toString()+"(");
                    numOfLiftBrackets++;
                }else {
                    if (!(screen2.getText().toString().charAt(screen2.getText().length()-1)==')') && !screen2.getText().toString().equals(""))
                    {screen2.setText(screen2.getText().toString()+screen1.getText().toString());}
                    if (numOfLiftBrackets!=0){
                        screen2.setText(screen2.getText().toString()+")");
                        numOfLiftBrackets--;
                        digitEntered=true;
                    }
                    if (numOfLiftBrackets==0){
                        String ex=screen2.getText().toString();
                        ex=p.parse(ex);
                        screen1.setText(ex);
                        digitEntered=true;
                        justEvaluated=true;
                    }
                }
            }else {
                screen2.setText("");
                BracketsInput(view);
            }
        }catch (Exception ignored){}

    }
    private void change(){
        if (!isChangeButtonPressed){
            sin.setTag("asin");
            sin.setImageResource(R.drawable.sinmone);
            cos.setTag("acos");
            cos.setImageResource(R.drawable.cosmone);
            tan.setTag("atan");
            tan.setImageResource(R.drawable.tanmone);
            sinh.setTag("asinh");
            sinh.setImageResource(R.drawable.sinhmone);
            cosh.setTag("acosh");
            cosh.setImageResource(R.drawable.coshmone);
            tanh.setTag("atanh");
            tanh.setImageResource(R.drawable.tanhmone);
            log.setTag("ln");
            log.setImageResource(R.drawable.ln);
            sqrt.setTag("sqrtyx");
            sqrt.setImageResource(R.drawable.sqryx);
            xtoy.setTag("tenX");
            xtoy.setImageResource(R.drawable.tenx);
            square.setTag("quebec");
            square.setImageResource(R.drawable.xtothree);
            oneOverx.setTag("etox");
            oneOverx.setImageResource(R.drawable.etox);
            isChangeButtonPressed=true;
        }else {
            sin.setTag("sin");
            sin.setImageResource(R.drawable.sin);
            cos.setTag("cos");
            cos.setImageResource(R.drawable.cos);
            tan.setTag("tan");
            tan.setImageResource(R.drawable.tan);
            sinh.setTag("sinh");
            sinh.setImageResource(R.drawable.sinh);
            cosh.setTag("cosh");
            cosh.setImageResource(R.drawable.cosh);
            tanh.setTag("tanh");
            tanh.setImageResource(R.drawable.tanh);
            log.setTag("log");
            log.setImageResource(R.drawable.log);
            sqrt.setTag("sqrt");
            sqrt.setImageResource(R.drawable.sqrt);
            xtoy.setTag("xtoy");
            xtoy.setImageResource(R.drawable.xtoy);
            square.setTag("square");
            square.setImageResource(R.drawable.square);
            oneOverx.setTag("oneOverx");
            oneOverx.setImageResource(R.drawable.oneoverx);
            isChangeButtonPressed=false;
        }
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
            case R.id.PIButton:
                OnNumberPressed(v);
                break;
            case R.id.equalButton:
                ONEqualPressed();
                break;
            case R.id.CButton:
            case R.id.CEButton:
                ONCLearPressed(v);
                break;
            case R.id.plusButton:
            case R.id.minusButton:
            case R.id.multButton:
            case R.id.divisButton:
                ONOperPressed(v);
                break;
            case R.id.liftBrackets:
            case R.id.rightBrackets:
                BracketsInput(v);
                break;
            case R.id.factButton:
                try{
                    if (screen2.getText().toString().equals("")||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='÷'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='+'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='-'||screen2.getText().toString().charAt(screen2.getText().toString().length()-1)=='×'){
                        if (!screen1.getText().toString().equals("")){
                            try {
                                String expression = screen1.getText().toString()+"!";
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
            case R.id.DegOrRad:
                Button b=(Button)v;
                if (b.getText().equals("RAD")){
                    b.setText(R.string.deg);
                    isDeg=true;
                }else {
                    b.setText(R.string.rad);
                    isDeg=false;
                }
                break;
            default:
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
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
