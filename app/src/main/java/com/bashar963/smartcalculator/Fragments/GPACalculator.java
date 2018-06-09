package com.bashar963.smartcalculator.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bashar963.smartcalculator.R;


import java.util.ArrayList;


public class GPACalculator extends Fragment {

    private TextView display;
     ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences mPrefs = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        int myTheme = mPrefs.getInt("theme", R.style.AppTheme);
        int myMode = mPrefs.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        boolean recreateTheme = mPrefs.getBoolean("recreateTheme", false);
        AppCompatDelegate.setDefaultNightMode(myMode);

        getContext().getTheme().applyStyle(myTheme,true);

        View mView= inflater.inflate(R.layout.fragment_gpacalculator, container, false);


        if (!recreateTheme){
            getActivity().recreate();
            recreateTheme = true;
        }

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("recreateTheme", recreateTheme);
        ed.apply();


        ArrayList<AdapterItems> adapterItems = new ArrayList<>();
        final MyCustomAdapter adapter = new MyCustomAdapter(getContext(), adapterItems);
         listView = mView.findViewById(R.id.courseList);
        listView.setAdapter(adapter);
        display= mView.findViewById(R.id.display);
        adapter.add(new AdapterItems("","",0));
        adapter.add(new AdapterItems("","",0));
        adapter.add(new AdapterItems("","",0));
        adapter.add(new AdapterItems("","",0));

        Button addSubject = mView.findViewById(R.id.addsubject);
        Button removeSubject = mView.findViewById(R.id.remSubject);
        removeSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = adapter.getCount();
                if (count<=0)return;
                if (count==1){
                    Toast.makeText(getContext(),"You can not delete more subject !",Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete subject")
                        .setMessage("Are you sure you want to delete this subject?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.remove(adapter.getItem(count -1));
                                adapter.setNotifyOnChange(true);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(new AdapterItems("","",0));
                adapter.notifyDataSetChanged();
            }
        });
        Button reset= mView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count =listView.getCount();
                for (int i=0;i<count;i++) {
                    View view = getViewByPosition(i, listView);
                    Spinner s = view.findViewById(R.id.card_degree);
                    EditText e = view.findViewById(R.id.card_crdit);
                    EditText e1= view.findViewById(R.id.card_subject);
                    s.setSelection(0);
                    e.setText("");
                    e1.setText("");
                }
                display.setText(getResources().getString(R.string.gpa));
            }
        });
        Button calculate= mView.findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
            }
        });


        return  mView;
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    private void Calculate(){
        int count =listView.getCount();
        double credit;
        String degree;
        double totSum=0;
        double totCredit=0;
        float res;
        for (int i=0;i<count;i++){
            View view=getViewByPosition(i,listView);
            Spinner s= view.findViewById(R.id.card_degree);
            EditText e= view.findViewById(R.id.card_crdit);
            degree=s.getSelectedItem().toString();
            if (e.getText().toString().equals("")||e.getText().toString().isEmpty())continue;
            credit=Double.valueOf(e.getText().toString());
            switch (degree){
                case "A":
                    totCredit+=credit;
                    totSum+=credit*4;
                    break;
                case "B+":
                    totCredit+=credit;
                    totSum+=credit*3.5;
                    break;
                case "B":
                    totCredit+=credit;
                    totSum+=credit*3;
                    break;
                case "C+":
                    totCredit+=credit;
                    totSum+=credit*2.5;
                    break;
                case "C":
                    totCredit+=credit;
                    totSum+=credit*2;
                    break;
                case "D+":
                    totCredit+=credit;
                    totSum+=credit*1.5;
                    break;
                case "D":
                    totCredit+=credit;
                    totSum+=credit*1;
                    break;
                case "F":
                    totCredit+=credit;
                    totSum+=credit*0;
                    break;
            }
        }
        res = (float) (totSum/totCredit);
        display.setText(getResources().getString(R.string.gpa)+String.valueOf(res));


    }

    private class MyCustomAdapter extends ArrayAdapter<AdapterItems>{

        MyCustomAdapter(Context context,ArrayList<AdapterItems> adapterItems){
            super(context,0,adapterItems);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            AdapterItems adapterItems=getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_card, parent, false);
            }
            EditText subject= convertView.findViewById(R.id.card_subject);
            EditText credit= convertView.findViewById(R.id.card_crdit);
            Spinner degree= convertView.findViewById(R.id.card_degree);




            return convertView;
        }
    }
    private class AdapterItems{
        String credit;
        String subject;
        int degree;
        AdapterItems(String credit,String subject,int degree){
            this.credit=credit;
            this.subject=subject;
            this.degree=degree;
        }

}

}
