package com.anwios.meterviewexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import com.anwios.meterview.view.MeterView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt1= (Button) findViewById(R.id.button1);
        Button bt2= (Button) findViewById(R.id.button2);
        Button bt3= (Button) findViewById(R.id.button3);
        Button bt4= (Button) findViewById(R.id.button4);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);

        MeterView meterView= (MeterView) findViewById(R.id.mv2);
        meterView.setInterpolator(new BounceInterpolator());
    }




    @Override
    public void onClick(View view) {
        int id=view.getId();
        final MeterView meterView= (MeterView) findViewById(R.id.mv);
        final MeterView meterView2= (MeterView) findViewById(R.id.mv2);
        switch (id){
            case R.id.button1: meterView.moveHeadTo(300);break; // you can use meterView.setValue() to set value with out animation
            case R.id.button2: meterView.moveHeadTo(0);break;
            case R.id.button3: meterView2.moveHeadTo(100);break;
            case R.id.button4: meterView2.moveHeadTo(0);break;
        }
    }
}
