package com.bashar963.smartcalculator.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.bashar963.smartcalculator.ProgParser;

import com.bashar963.smartcalculator.R;


import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitHelper;


public class Programmer extends Fragment implements View.OnClickListener{

    private Button hexButton,decButton,octButton,binButton,byteButton,shiftRight,shiftLeft;
    private TextView hexDisplay,decDisplay,octDisplay,binDisplay,screen1;
    private ProgParser pp=new ProgParser();
    private String lastAnswer;
    private String CURRENT_ACTION;
    private SharedPreferences mPrefs;

    private double valueOne=Double.NaN;
    private double valueTwo;
    private boolean isChangeButtonPressed=false,isNight;
    private final int BUTTONS_IDS[]={
            R.id.zeroButton,R.id.oneButton,R.id.twoButton,R.id.threeButton,R.id.fourButton,R.id.fiveButton,
            R.id.sixButton,R.id.sevenButton,R.id.eightButton,R.id.nineButton, R.id.equalButton,R.id.plusButton,
            R.id.subButton,R.id.mulButton,R.id.divideButton,R.id.plsminusBUtton,R.id.CButtonp,R.id.CEButton,R.id.AButton
            ,R.id.BButton,R.id.CButton,R.id.DButton,R.id.EButton,R.id.FButton,
            R.id.AndButton,R.id.OrButton,R.id.XorButton,R.id.NotButton,R.id.modButton,R.id.RshRorbuuton,R.id.LshRolButton

    };
    private String decodeState,byteState;
    private boolean justEvaluated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);

        final View mView= inflater.inflate(R.layout.fragment_programmer, container, false);

        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();


        hexButton=mView.findViewById(R.id.hexButton);
        decButton=mView.findViewById(R.id.decButton);
        octButton=mView.findViewById(R.id.octButton);
        binButton=mView.findViewById(R.id.binButton);
        byteButton=mView.findViewById(R.id.byteButton);
        hexDisplay= mView.findViewById(R.id.hexDisplay);
        decDisplay= mView.findViewById(R.id.decDisplay);
        octDisplay= mView.findViewById(R.id.octDisplay);
        binDisplay= mView.findViewById(R.id.binDisplay);
        screen1=mView.findViewById(R.id.display2);
        shiftLeft=mView.findViewById(R.id.LshRolButton);
        shiftRight=mView.findViewById(R.id.RshRorbuuton);
        shiftLeft.setTag("LSH");
        shiftRight.setTag("RSH");
        AutofitHelper.create(screen1);
        getContext();
        mPrefs = getContext().getSharedPreferences("programming", Context.MODE_PRIVATE);
        SharedPreferences mPrefs2 = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        isNight  = mPrefs2.getBoolean("isNight",false);
        String s1=mPrefs.getString("screen1","0");
        lastAnswer=mPrefs.getString("lastAnswer","");
        justEvaluated=mPrefs.getBoolean("justEvaluated",false);
        isChangeButtonPressed=mPrefs.getBoolean("isChangeButtonPressed",false);
        decodeState=mPrefs.getString("decodeState","hex");
        byteState=mPrefs.getString("byteState","QWORD");

        screen1.setText(s1);
        change();
        byteButton.setText(byteState);
        convert();
        ImageButton backSpace = mView.findViewById(R.id.backSpace);
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!screen1.getText().toString().equals("0")&&!screen1.getText().toString().equals("")){
                    screen1.setText(screen1.getText().toString().substring(0, screen1.length() - 1));
                } if (screen1.getText().toString().equals("")){
                    screen1.setText("0");
                }
                convert();
            }
        });
        ImageButton change= mView.findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        List<Button> buttons;
        buttons = new ArrayList<>(BUTTONS_IDS.length);
        for(int id : BUTTONS_IDS) {
            Button button = mView.findViewById(id);
            button.setOnClickListener(this);
            buttons.add(button);
        }

        if (isNight){
            changeColorButtonNight(mView);
        }else {
            changeColorButton(mView);
        }



        hexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeState="hex";
                if (isNight){
                    changeColorButtonNight(mView);
                }else {
                    changeColorButton(mView);
                }
                screen1.setText(hexDisplay.getText().toString());
            }
        });
        octButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeState="oct";
                if (isNight){
                    changeColorButtonNight(mView);
                }else {
                    changeColorButton(mView);
                }
                screen1.setText(octDisplay.getText().toString());
            }
        });
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeState="dec";
                if (isNight){
                    changeColorButtonNight(mView);
                }else {
                    changeColorButton(mView);
                }
                screen1.setText(decDisplay.getText().toString());
            }
        });
        binButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeState="bin";
                if (isNight){
                    changeColorButtonNight(mView);
                }else {
                    changeColorButton(mView);
                }
                screen1.setText(binDisplay.getText().toString());
            }
        });

        byteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonState();
            }
        });
        return mView;
    }
    private void changeColorButton( View view)
    {
        Button Ab=view.findViewById(R.id.AButton);
        Button Bb=view.findViewById(R.id.BButton);
        Button Cb=view.findViewById(R.id.CButton);
        Button Db=view.findViewById(R.id.DButton);
        Button Eb= view.findViewById(R.id.EButton);
        Button Fb= view.findViewById(R.id.FButton);
        Button nineB= view.findViewById(R.id.nineButton);
        Button eightB= view.findViewById(R.id.eightButton);
        Button sevenB= view.findViewById(R.id.sevenButton);
        Button sixB= view.findViewById(R.id.sixButton);
        Button fiveB= view.findViewById(R.id.fiveButton);
        Button fourB= view.findViewById(R.id.fourButton);
        Button threeB= view.findViewById(R.id.threeButton);
        Button twoB= view.findViewById(R.id.twoButton);
        switch (decodeState){
            case "hex":
                hexButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                hexDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                octButton.setTextColor(Color.BLACK);
                octDisplay.setTextColor(Color.BLACK);
                decButton.setTextColor(Color.BLACK);
                decDisplay.setTextColor(Color.BLACK);
                binButton.setTextColor(Color.BLACK);
                binDisplay.setTextColor(Color.BLACK);

                decodeState="hex";
                Ab.setClickable(true);
                Ab.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                Bb.setClickable(true);
                Bb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                Cb.setClickable(true);
                Cb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                Db.setClickable(true);
                Db.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                Eb.setClickable(true);
                Eb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                Fb.setClickable(true);
                Fb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                nineB.setClickable(true);
                nineB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                eightB.setClickable(true);
                eightB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                break;
            case "oct":
                hexButton.setTextColor(Color.BLACK);
                hexDisplay.setTextColor(Color.BLACK);
                octButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                octDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                decButton.setTextColor(Color.BLACK);
                decDisplay.setTextColor(Color.BLACK);
                binButton.setTextColor(Color.BLACK);
                binDisplay.setTextColor(Color.BLACK);
                decodeState="oct";

                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(false);
                nineB.setTextColor(Color.rgb(177, 195, 205));
                eightB.setClickable(false);
                eightB.setTextColor(Color.rgb(177, 195, 205));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                break;
            case "dec":
                hexButton.setTextColor(Color.BLACK);
                hexDisplay.setTextColor(Color.BLACK);
                octButton.setTextColor(Color.BLACK);
                octDisplay.setTextColor(Color.BLACK);
                decButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                decDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                binButton.setTextColor(Color.BLACK);
                binDisplay.setTextColor(Color.BLACK);
                decodeState="dec";
                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(true);
                nineB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                eightB.setClickable(true);
                eightB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                break;
            case "bin":
                hexButton.setTextColor(Color.BLACK);
                hexDisplay.setTextColor(Color.BLACK);
                octButton.setTextColor(Color.BLACK);
                octDisplay.setTextColor(Color.BLACK);
                decButton.setTextColor(Color.BLACK);
                decDisplay.setTextColor(Color.BLACK);
                binButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                binDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                decodeState="bin";
                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(false);
                nineB.setTextColor(Color.rgb(177, 195, 205));
                eightB.setClickable(false);
                eightB.setTextColor(Color.rgb(177, 195, 205));
                sevenB.setClickable(false);
                sevenB.setTextColor(Color.rgb(177, 195, 205));
                sixB.setClickable(false);
                sixB.setTextColor(Color.rgb(177, 195, 205));
                fiveB.setClickable(false);
                fiveB.setTextColor(Color.rgb(177, 195, 205));
                fourB.setClickable(false);
                fourB.setTextColor(Color.rgb(177, 195, 205));
                threeB.setClickable(false);
                threeB.setTextColor(Color.rgb(177, 195, 205));
                twoB.setClickable(false);
                twoB.setTextColor(Color.rgb(177, 195, 205));

                break;
            default:
                break;
        }
    }
    private void changeButtonState(){
        int i;
        int c=1;
        switch (byteState){
            case "QWORD":
                byteButton.setText(getResources().getString(R.string.dword));
                byteState=getResources().getString(R.string.dword);
                if (decodeState.equals( "hex"))c=8;
                if (decodeState.equals( "oct"))c=10;
                if (decodeState.equals( "dec")){
                    String s=decDisplay.getText().toString();
                    s=Long.toBinaryString(Long.valueOf(s));
                    int len=s.length()-32;
                    if (len<0)return;
                    s=s.substring(len);
                    long i2=Long.parseLong(s,2);
                    s= String.valueOf((int)i2);
                    screen1.setText(s);
                    decDisplay.setText(s);convert();
                }
                if (decodeState.equals( "bin"))c=32;
                if (screen1.getText().length()>c&&!decodeState.equals("dec")) {
                    i=screen1.getText().length()-c;
                    screen1.setText(screen1.getText().toString().substring(i));
                    convert();
                }


                break;
            case "DWORD":
                byteButton.setText(getResources().getString(R.string.word));
                byteState=getResources().getString(R.string.word);
                if (decodeState.equals( "hex"))c=4;
                if (decodeState.equals( "oct"))c=5;
                if (decodeState.equals( "dec")){
                    String s=decDisplay.getText().toString();
                    s=Long.toBinaryString(Long.valueOf(s));
                    int len=s.length()-16;
                    if (len<0)return;
                    s=s.substring(len);
                    int i2=Integer.parseInt(s,2);
                    s= String.valueOf((short)i2);
                    screen1.setText(s);
                    decDisplay.setText(s);convert();
                }
                if (decodeState.equals( "bin"))c=16;
                if (screen1.getText().length()>c&&!decodeState.equals("dec")) {
                    i=screen1.getText().length()-c;
                    screen1.setText(screen1.getText().toString().substring(i));
                    convert();
                }
                break;
            case "WORD":
                byteButton.setText(getResources().getString(R.string.Byte));
                byteState=getResources().getString(R.string.Byte);
                if (decodeState.equals( "hex"))c=2;
                if (decodeState.equals( "oct"))c=2;
                if (decodeState.equals( "dec")){
                    String s=decDisplay.getText().toString();
                    s=Long.toBinaryString(Long.valueOf(s));
                    int len=s.length()-8;
                    if (len<0)return;
                    s=s.substring(len);
                    short i2=Short.parseShort(s,2);
                    s= String.valueOf((byte)i2);
                    screen1.setText(s);
                    decDisplay.setText(s);
                    convert();
                }
                if (decodeState.equals( "bin"))c=8;
                if (screen1.getText().length()>c&&!decodeState.equals("dec")) {
                    i=screen1.getText().length()-c;
                    screen1.setText(screen1.getText().toString().substring(i));
                    convert();
                }
                break;
            case "BYTE":
                byteButton.setText(getResources().getString(R.string.qword));
                byteState=getResources().getString(R.string.qword);
                convert();
                break;
            default:
                break;
        }
    }
    private void change(){
        if (!isChangeButtonPressed){
            shiftLeft.setTag("ROL");
            shiftLeft.setText("ROL");
            shiftRight.setText("ROR");
            shiftRight.setTag("ROR");
            isChangeButtonPressed=true;
        }else {
            shiftLeft.setTag("LSH");
            shiftLeft.setText("LSH");
            shiftRight.setText("RSH");
            shiftRight.setTag("RSH");
            isChangeButtonPressed=false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zeroButton:
            case R.id.oneButton:
            case R.id.twoButton:
            case R.id.threeButton:
            case R.id.fourButton:
            case R.id.fiveButton:
            case R.id.sixButton:
            case R.id.sevenButton:
            case R.id.eightButton:
            case R.id.nineButton:
            case R.id.plsminusBUtton:
            case R.id.AButton:
            case R.id.BButton:
            case R.id.CButton:
            case R.id.DButton:
            case R.id.EButton:
            case R.id.FButton:
                OnNumberPressed(v);
                break;
            case R.id.CButtonp:
            case R.id.CEButton:
                ONCLearPressed(v);
                break;
            case R.id.plusButton:
            case R.id.subButton:
            case R.id.divideButton:
            case R.id.mulButton:
            case R.id.AndButton:
            case R.id.OrButton:
            case R.id.XorButton:
            case R.id.NotButton:
            case R.id.LshRolButton:
            case R.id.RshRorbuuton:
            case R.id.modButton:
                ONOperPressed(v);
                break;
            case R.id.equalButton:
                ONEqualPressed();
                break;

        }
    }
    private void OnNumberPressed(View view){

            try {
                    Button b= (Button)view;
                boolean b1=false;
                    if (justEvaluated&&b.getId()!=R.id.plsminusBUtton) {
                        screen1.setText("0");
                        justEvaluated=false;
                    }
                    if (screen1.getText().toString().equals("0")){
                        screen1.setText("");
                    }
                     if (b.getId()==R.id.plsminusBUtton){
                         b1=true;
                        if (!screen1.getText().toString().equals("")||screen1.getText().toString().equals("0")){
                            String str=screen1.getText().toString();
                            int i;
                            switch (decodeState) {
                                case "hex": {
                                    str = pp.hexToDec(str, byteState);
                                     i = Integer.valueOf(str);
                                    i = i * -1;
                                    str = pp.decToHex(String.valueOf(i), byteState);
                                    screen1.setText(str);
                                    break;
                                }
                                case "dec": {
                                     i = Integer.valueOf(str);
                                    i = i * -1;
                                    screen1.setText(String.valueOf(i));
                                    break;
                                }
                                case "oct": {
                                    str = pp.octToDec(str, byteState);
                                     i = Integer.valueOf(str);
                                    i = i * -1;
                                    str = pp.decToOct(String.valueOf(i), byteState);
                                    screen1.setText(str);
                                    break;
                                }
                                case "bin": {
                                    str = pp.binToDec(str, byteState);
                                     i = Integer.valueOf(str);
                                    i = i * -1;
                                    str = pp.decToBin(String.valueOf(i), byteState);
                                    screen1.setText(str);
                                    break;
                                }
                            }
                            convert();
                        }else screen1.setText("0");
                    }
                    if (decodeState.equals("hex")&&byteState.equals("QWORD")&&screen1.getText().toString().length()>=16)return;
                    if (decodeState.equals("hex")&&byteState.equals("DWORD")&&screen1.getText().toString().length()>=8)return;
                    if (decodeState.equals("hex")&&byteState.equals("WORD")&&screen1.getText().toString().length()>=4)return;
                    if (decodeState.equals("hex")&&byteState.equals("BYTE")&&screen1.getText().toString().length()>=2)return;
                    if (decodeState.equals("dec")&&byteState.equals("QWORD")&&screen1.getText().toString().length()>=19)return;
                    if (decodeState.equals("dec")&&byteState.equals("DWORD")&&screen1.getText().toString().length()>=9)return;
                    if (decodeState.equals("dec")&&byteState.equals("WORD")&&screen1.getText().toString().length()>=4)return;
                    if (decodeState.equals("dec")&&byteState.equals("BYTE")&&screen1.getText().toString().length()>=2)return;
                    if (decodeState.equals("oct")&&byteState.equals("QWORD")&&screen1.getText().toString().length()>=21)return;
                    if (decodeState.equals("oct")&&byteState.equals("DWORD")&&screen1.getText().toString().length()>=10)return;
                    if (decodeState.equals("oct")&&byteState.equals("WORD")&&screen1.getText().toString().length()>=5)return;
                    if (decodeState.equals("oct")&&byteState.equals("BYTE")&&screen1.getText().toString().length()>=2)return;
                    if (decodeState.equals("bin")&&byteState.equals("QWORD")&&screen1.getText().toString().length()>=64)return;
                    if (decodeState.equals("bin")&&byteState.equals("DWORD")&&screen1.getText().toString().length()>=32)return;
                    if (decodeState.equals("bin")&&byteState.equals("WORD")&&screen1.getText().toString().length()>=16)return;
                    if (decodeState.equals("bin")&&byteState.equals("BYTE")&&screen1.getText().toString().length()>=8)return;
                    else if (!b1){
                    screen1.setText(screen1.getText().toString()+b.getText());
                }


            }catch (Exception ignored){}
            convert();

        }
    private void convert(){
        String converted;
        switch (decodeState){

            case "hex":

                converted=pp.hexToDec(screen1.getText().toString(),byteState);
                hexDisplay.setText(pp.decToHex(converted,byteState));
                screen1.setText(hexDisplay.getText());
                decDisplay.setText(converted);
                octDisplay.setText(pp.decToOct(converted,byteState));
                binDisplay.setText(pp.decToBin(converted,byteState));

                break;
            case "oct":
                converted=pp.octToDec(screen1.getText().toString(),byteState);
                hexDisplay.setText(pp.decToHex(converted,byteState));
                decDisplay.setText(converted);
                octDisplay.setText(pp.decToOct(converted,byteState));
                binDisplay.setText(pp.decToBin(converted,byteState));
                screen1.setText(octDisplay.getText());
                break;
            case "dec":
                converted=screen1.getText().toString();
                hexDisplay.setText(pp.decToHex(converted,byteState));
                decDisplay.setText(converted);
                octDisplay.setText(pp.decToOct(converted,byteState));
                binDisplay.setText(pp.decToBin(converted,byteState));
                screen1.setText(decDisplay.getText());
                break;
            case "bin":
                converted=pp.binToDec(screen1.getText().toString(),byteState);
                hexDisplay.setText(pp.decToHex(converted,byteState));
                decDisplay.setText(converted);
                octDisplay.setText(pp.decToOct(converted,byteState));
                binDisplay.setText(pp.decToBin(converted,byteState));
                screen1.setText(binDisplay.getText());
                break;
        }

    }
    private void ONCLearPressed(View view){
        if (view.getId()==R.id.CButtonp){
            screen1.setText("0");
            hexDisplay.setText("0");
            decDisplay.setText("0");
            octDisplay.setText("0");
            binDisplay.setText("0");
            lastAnswer="";valueOne=Double.NaN;
            justEvaluated =false;
        }else {
            screen1.setText("0");
            hexDisplay.setText("0");
            decDisplay.setText("0");
            octDisplay.setText("0");
            binDisplay.setText("0");
        }
    }

    private void ONOperPressed(View view){
        Button b=(Button)view;
        if (b.getText().toString().equalsIgnoreCase("and")){
            computeCalculation();
            CURRENT_ACTION = "&";
        }else if (b.getText().toString().equalsIgnoreCase("or")){
            computeCalculation();
            CURRENT_ACTION = "|";
        }else if (b.getText().toString().equalsIgnoreCase("xor")){
            computeCalculation();
            CURRENT_ACTION = "^";
        }else if(b.getText().toString().equalsIgnoreCase("mod")){
            computeCalculation();
            CURRENT_ACTION = "%";
        }
        else if (b.getText().toString().equalsIgnoreCase("not")){
            long x=0;
            if(decodeState.equalsIgnoreCase("hex")){
                switch (byteState){
                    case "QWORD":
                        x = new BigInteger(screen1.getText().toString(),16).longValue();
                        break;
                    case "DWORD":
                        x = (int)Long.parseLong(screen1.getText().toString(),16);
                        break;
                    case "WORD":
                        x = (short)Integer.parseInt(screen1.getText().toString(),16);
                        break;
                    case "BYTE":
                        x = (byte)Short.parseShort(screen1.getText().toString(),16);
                        break;

                }
            }
            if(decodeState.equalsIgnoreCase("dec")){
                switch (byteState){
                    case "QWORD":
                        x = Long.parseLong(screen1.getText().toString());
                        break;
                    case "DWORD":
                        x = Integer.parseInt(screen1.getText().toString());
                        break;
                    case "WORD":
                        x = Short.parseShort(screen1.getText().toString());
                        break;
                    case "BYTE":
                        x = Byte.parseByte(screen1.getText().toString());
                        break;

                }
            }
            if(decodeState.equalsIgnoreCase("oct")){
                switch (byteState){
                    case "QWORD":
                        x = new BigInteger(screen1.getText().toString(),8).longValue();
                        break;
                    case "DWORD":
                        x = (int)Long.parseLong(screen1.getText().toString(),8);
                        break;
                    case "WORD":
                        x = (short)Integer.parseInt(screen1.getText().toString(),8);
                        break;
                    case "BYTE":
                        x = (byte)Short.parseShort(screen1.getText().toString(),8);
                        break;

                }
            }
            if(decodeState.equalsIgnoreCase("bin")){
                switch (byteState){
                    case "QWORD":
                        x = new BigInteger(screen1.getText().toString(),2).longValue();
                        break;
                    case "DWORD":
                        x = (int)Long.parseLong(screen1.getText().toString(),2);
                        break;
                    case "WORD":
                        x = (short)Integer.parseInt(screen1.getText().toString(),2);
                        break;
                    case "BYTE":
                        x = (byte)Short.parseShort(screen1.getText().toString(),2);
                        break;

                }}
            x=~x;

            if (decodeState.equalsIgnoreCase("hex")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toHexString(x);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toHexString((int) x);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toHexString((int) x & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toHexString((int) x & 0xFF);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("dec")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = String.valueOf(x);
                        break;
                    case "DWORD":
                        lastAnswer = String.valueOf((int)x);
                        break;
                    case "WORD":
                        lastAnswer = String.valueOf((short)x);
                        break;
                    case "BYTE":
                        lastAnswer = String.valueOf((byte)x);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("oct")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toOctalString( x);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toOctalString((int) x);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toOctalString((int) x & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toOctalString((int) x & 0xFF);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("bin")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toBinaryString( x);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toBinaryString((int) x);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toBinaryString((int) x & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toBinaryString((int) x & 0xFF);
                        break;

                }}
                screen1.setText(lastAnswer);
                convert();
                justEvaluated=true;
        }
        else if (b.getText().toString().equalsIgnoreCase("lsh")){
            computeCalculation();
            if (b.getTag().equals("LSH"))
            CURRENT_ACTION = "<<";
            else CURRENT_ACTION="ROL";
        }
        else if (b.getText().toString().equalsIgnoreCase("rsh")){
            computeCalculation();
            if (b.getTag().equals("RSH"))
            CURRENT_ACTION = ">>";
            else CURRENT_ACTION="ROR";
        }
        else {
            computeCalculation();
            CURRENT_ACTION = b.getText().toString();


        }



    }
    private void computeCalculation() {
        if(!Double.isNaN(valueOne)) {
            if (decodeState.equalsIgnoreCase("hex")) {
                switch (byteState) {
                    case "QWORD":
                        valueTwo = new BigInteger(screen1.getText().toString(), 16).longValue();
                        break;
                    case "DWORD":
                        valueTwo = (int) Long.parseLong(screen1.getText().toString(), 16);
                        break;
                    case "WORD":
                        valueTwo = (short) Integer.parseInt(screen1.getText().toString(), 16);
                        break;
                    case "BYTE":
                        valueTwo = (byte) Short.parseShort(screen1.getText().toString(), 16);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("dec")) {
                switch (byteState) {
                    case "QWORD":
                        valueTwo = Long.parseLong(screen1.getText().toString());
                        break;
                    case "DWORD":
                        valueTwo = Integer.parseInt(screen1.getText().toString());
                        break;
                    case "WORD":
                        valueTwo = Short.parseShort(screen1.getText().toString());
                        break;
                    case "BYTE":
                        valueTwo = Byte.parseByte(screen1.getText().toString());
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("oct")) {
                switch (byteState) {
                    case "QWORD":
                        valueTwo = new BigInteger(screen1.getText().toString(), 8).longValue();
                        break;
                    case "DWORD":
                        valueTwo = (int) Long.parseLong(screen1.getText().toString(), 8);
                        break;
                    case "WORD":
                        valueTwo = (short) Integer.parseInt(screen1.getText().toString(), 8);
                        break;
                    case "BYTE":
                        valueTwo = (byte) Short.parseShort(screen1.getText().toString(), 8);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("bin")) {
                switch (byteState) {
                    case "QWORD":
                        valueTwo = new BigInteger(screen1.getText().toString(), 2).longValue();
                        break;
                    case "DWORD":
                        valueTwo = (int) Long.parseLong(screen1.getText().toString(), 2);
                        break;
                    case "WORD":
                        valueTwo = (short) Integer.parseInt(screen1.getText().toString(), 2);
                        break;
                    case "BYTE":
                        valueTwo = (byte) Short.parseShort(screen1.getText().toString(), 2);
                        break;

                }
            }


            switch (CURRENT_ACTION) {
                case "+":
                    valueOne = this.valueOne + valueTwo;
                    break;
                case "-":
                    valueOne = this.valueOne - valueTwo;
                    break;
                case "×":
                    valueOne = this.valueOne * valueTwo;
                    break;
                case "÷":
                    valueOne = this.valueOne / valueTwo;
                    break;
                case "&":
                    valueOne = (long) valueOne & (long) valueTwo;
                    break;
                case "|":
                    valueOne = (long) valueOne | (long) valueTwo;
                    break;
                case "^":
                    valueOne = (long) valueOne ^ (long) valueTwo;
                    break;
                case "%":
                    if (valueTwo==0)return;
                    valueOne = (long) valueOne % (long) valueTwo;
                    break;
                case "<<":
                    valueOne = (long) valueOne << (long) valueTwo;
                    break;
                case ">>":
                    valueOne = (long) valueOne >> (long) valueTwo;
                    break;
                case "ROR":
                    valueOne = Long.rotateRight((long) valueOne, (int) valueTwo);
                    break;
                case "ROL":
                    valueOne = Long.rotateLeft((long) valueOne, (int) valueTwo);
                    break;

            }
            justEvaluated = true;
            if (decodeState.equalsIgnoreCase("hex")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toHexString((long) valueOne);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toHexString((int) valueOne);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toHexString((int) valueOne & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toHexString((int) valueOne & 0xFF);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("dec")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = String.valueOf((long)valueOne);
                        break;
                    case "DWORD":
                        lastAnswer = String.valueOf((int)valueOne);
                        break;
                    case "WORD":
                        lastAnswer = String.valueOf((short)valueOne);
                        break;
                    case "BYTE":
                        lastAnswer = String.valueOf((byte)valueOne);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("oct")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toOctalString((long) valueOne);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toOctalString((int) valueOne);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toOctalString((int) valueOne & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toOctalString((int) valueOne & 0xFF);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("bin")) {
                switch (byteState) {
                    case "QWORD":
                        lastAnswer = Long.toBinaryString((long) valueOne);
                        break;
                    case "DWORD":
                        lastAnswer = Integer.toBinaryString((int) valueOne);
                        break;
                    case "WORD":
                        lastAnswer = Integer.toBinaryString((int) valueOne & 0xFFFF);
                        break;
                    case "BYTE":
                        lastAnswer = Integer.toBinaryString((int) valueOne & 0xFF);
                        break;

                }}
                screen1.setText(lastAnswer);
            if (decodeState.equalsIgnoreCase("hex")) {
                switch (byteState) {
                    case "QWORD":
                        valueOne = new BigInteger(screen1.getText().toString(), 16).longValue();
                        break;
                    case "DWORD":
                        valueOne = (int) Long.parseLong(screen1.getText().toString(), 16);
                        break;
                    case "WORD":
                        valueOne = (short) Integer.parseInt(screen1.getText().toString(), 16);
                        break;
                    case "BYTE":
                        valueOne = (byte) Short.parseShort(screen1.getText().toString(), 16);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("dec")) {
                switch (byteState) {
                    case "QWORD":
                        valueOne = Long.parseLong(screen1.getText().toString());
                        break;
                    case "DWORD":
                        valueOne = Integer.parseInt(screen1.getText().toString());
                        break;
                    case "WORD":
                        valueOne = Short.parseShort(screen1.getText().toString());
                        break;
                    case "BYTE":
                        valueOne = Byte.parseByte(screen1.getText().toString());
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("oct")) {
                switch (byteState) {
                    case "QWORD":
                        valueOne = new BigInteger(screen1.getText().toString(), 8).longValue();
                        break;
                    case "DWORD":
                        valueOne = (int) Long.parseLong(screen1.getText().toString(), 8);
                        break;
                    case "WORD":
                        valueOne = (short) Integer.parseInt(screen1.getText().toString(), 8);
                        break;
                    case "BYTE":
                        valueOne = (byte) Short.parseShort(screen1.getText().toString(), 8);
                        break;

                }
            }
            if (decodeState.equalsIgnoreCase("bin")) {
                switch (byteState) {
                    case "QWORD":
                        valueOne = new BigInteger(screen1.getText().toString(), 2).longValue();
                        break;
                    case "DWORD":
                        valueOne = (int) Long.parseLong(screen1.getText().toString(), 2);
                        break;
                    case "WORD":
                        valueOne = (short) Integer.parseInt(screen1.getText().toString(), 2);
                        break;
                    case "BYTE":
                        valueOne = (byte) Short.parseShort(screen1.getText().toString(), 2);
                        break;

                }
            }
                convert();

            } else {

                try {
                    if (decodeState.equalsIgnoreCase("hex")) {
                        switch (byteState) {
                            case "QWORD":
                                valueOne = new BigInteger(screen1.getText().toString(), 16).longValue();
                                break;
                            case "DWORD":
                                valueOne = (int) Long.parseLong(screen1.getText().toString(), 16);
                                break;
                            case "WORD":
                                valueOne = (short) Integer.parseInt(screen1.getText().toString(), 16);
                                break;
                            case "BYTE":
                                valueOne = (byte) Short.parseShort(screen1.getText().toString(), 16);
                                break;

                        }
                    }
                    if (decodeState.equalsIgnoreCase("dec")) {
                        switch (byteState) {
                            case "QWORD":
                                valueOne = Long.parseLong(screen1.getText().toString());
                                break;
                            case "DWORD":
                                valueOne = Integer.parseInt(screen1.getText().toString());
                                break;
                            case "WORD":
                                valueOne = Short.parseShort(screen1.getText().toString());
                                break;
                            case "BYTE":
                                valueOne = Byte.parseByte(screen1.getText().toString());
                                break;

                        }
                    }
                    if (decodeState.equalsIgnoreCase("oct")) {
                        switch (byteState) {
                            case "QWORD":
                                valueOne = new BigInteger(screen1.getText().toString(), 8).longValue();
                                break;
                            case "DWORD":
                                valueOne = (int) Long.parseLong(screen1.getText().toString(), 8);
                                break;
                            case "WORD":
                                valueOne = (short) Integer.parseInt(screen1.getText().toString(), 8);
                                break;
                            case "BYTE":
                                valueOne = (byte) Short.parseShort(screen1.getText().toString(), 8);
                                break;

                        }
                    }
                    if (decodeState.equalsIgnoreCase("bin")) {
                        switch (byteState) {
                            case "QWORD":
                                valueOne = new BigInteger(screen1.getText().toString(), 2).longValue();
                                break;
                            case "DWORD":
                                valueOne = (int) Long.parseLong(screen1.getText().toString(), 2);
                                break;
                            case "WORD":
                                valueOne = (short) Integer.parseInt(screen1.getText().toString(), 2);
                                break;
                            case "BYTE":
                                valueOne = (byte) Short.parseShort(screen1.getText().toString(), 2);
                                break;

                        }
                    }
                    justEvaluated = true;
                } catch (Exception ignored) {

                }
            justEvaluated = true;
            }
        }
    private void ONEqualPressed(){
        computeCalculation();
        valueOne = Double.NaN;
        CURRENT_ACTION = "0";
    }

    private void changeColorButtonNight( View view)
    {
        Button Ab=view.findViewById(R.id.AButton);
        Button Bb=view.findViewById(R.id.BButton);
        Button Cb=view.findViewById(R.id.CButton);
        Button Db=view.findViewById(R.id.DButton);
        Button Eb= view.findViewById(R.id.EButton);
        Button Fb= view.findViewById(R.id.FButton);
        Button nineB= view.findViewById(R.id.nineButton);
        Button eightB= view.findViewById(R.id.eightButton);
        Button sevenB= view.findViewById(R.id.sevenButton);
        Button sixB= view.findViewById(R.id.sixButton);
        Button fiveB= view.findViewById(R.id.fiveButton);
        Button fourB= view.findViewById(R.id.fourButton);
        Button threeB= view.findViewById(R.id.threeButton);
        Button twoB= view.findViewById(R.id.twoButton);
        switch (decodeState){
            case "hex":
                hexButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                hexDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));

                octButton.setTextColor(Color.WHITE);
                octDisplay.setTextColor(Color.WHITE);
                decButton.setTextColor(Color.WHITE);
                decDisplay.setTextColor(Color.WHITE);
                binButton.setTextColor(Color.WHITE);
                binDisplay.setTextColor(Color.WHITE);

                decodeState="hex";
                Ab.setClickable(true);
                Ab.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                Bb.setClickable(true);
                Bb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                Cb.setClickable(true);
                Cb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                Db.setClickable(true);
                Db.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                Eb.setClickable(true);
                Eb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                Fb.setClickable(true);
                Fb.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                nineB.setClickable(true);
                nineB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                eightB.setClickable(true);
                eightB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                break;
            case "oct":
                hexButton.setTextColor(Color.WHITE);
                hexDisplay.setTextColor(Color.WHITE);
                octButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                octDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                decButton.setTextColor(Color.WHITE);
                decDisplay.setTextColor(Color.WHITE);
                binButton.setTextColor(Color.WHITE);
                binDisplay.setTextColor(Color.WHITE);
                decodeState="oct";

                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(false);
                nineB.setTextColor(Color.rgb(177, 195, 205));
                eightB.setClickable(false);
                eightB.setTextColor(Color.rgb(177, 195, 205));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                break;
            case "dec":
                hexButton.setTextColor(Color.WHITE);
                hexDisplay.setTextColor(Color.WHITE);
                octButton.setTextColor(Color.WHITE);
                octDisplay.setTextColor(Color.WHITE);
                decButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                decDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                binButton.setTextColor(Color.WHITE);
                binDisplay.setTextColor(Color.WHITE);
                decodeState="dec";
                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(true);
                nineB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                eightB.setClickable(true);
                eightB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                sevenB.setClickable(true);
                sevenB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                sixB.setClickable(true);
                sixB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fiveB.setClickable(true);
                fiveB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fourB.setClickable(true);
                fourB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                threeB.setClickable(true);
                threeB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                twoB.setClickable(true);
                twoB.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                break;
            case "bin":
                hexButton.setTextColor(Color.WHITE);
                hexDisplay.setTextColor(Color.WHITE);
                octButton.setTextColor(Color.WHITE);
                octDisplay.setTextColor(Color.WHITE);
                decButton.setTextColor(Color.WHITE);
                decDisplay.setTextColor(Color.WHITE);
                binButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                binDisplay.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryNight)));
                decodeState="bin";
                Ab.setClickable(false);
                Ab.setTextColor(Color.rgb(177, 195, 205));

                Bb.setClickable(false);
                Bb.setTextColor(Color.rgb(177, 195, 205));

                Cb.setClickable(false);
                Cb.setTextColor(Color.rgb(177, 195, 205));

                Db.setClickable(false);
                Db.setTextColor(Color.rgb(177, 195, 205));

                Eb.setClickable(false);
                Eb.setTextColor(Color.rgb(177, 195, 205));

                Fb.setClickable(false);
                Fb.setTextColor(Color.rgb(177, 195, 205));

                nineB.setClickable(false);
                nineB.setTextColor(Color.rgb(177, 195, 205));
                eightB.setClickable(false);
                eightB.setTextColor(Color.rgb(177, 195, 205));
                sevenB.setClickable(false);
                sevenB.setTextColor(Color.rgb(177, 195, 205));
                sixB.setClickable(false);
                sixB.setTextColor(Color.rgb(177, 195, 205));
                fiveB.setClickable(false);
                fiveB.setTextColor(Color.rgb(177, 195, 205));
                fourB.setClickable(false);
                fourB.setTextColor(Color.rgb(177, 195, 205));
                threeB.setClickable(false);
                threeB.setTextColor(Color.rgb(177, 195, 205));
                twoB.setClickable(false);
                twoB.setTextColor(Color.rgb(177, 195, 205));

                break;
            default:
                break;
        }
    }



    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("screen1", screen1.getText().toString());
        ed.putString("lastAnswer",lastAnswer);
        ed.putString("decodeState",decodeState);
        ed.putString("byteState",byteState);
        ed.putBoolean("justEvaluated",justEvaluated);
        ed.putBoolean("isChangeButtonPressed",isChangeButtonPressed);
        ed.apply();
    }
}
