package com.aixuan.switchflightmode;

import android.content.ContentResolver;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "switchAirplaneMode";
    Switch switchAirplaneMode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchAirplaneMode = (Switch) findViewById(R.id.switch_airplane_mode);
        switchAirplaneMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Intent intent = AirplaneHelper.getInstance().getOpenIntent();
                    sendBroadcast(intent);
                    Toast.makeText(MainActivity.this, "飞行模式开启", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = AirplaneHelper.getInstance().getCloseIntent();
                    sendBroadcast(intent);
                    Toast.makeText(MainActivity.this, "飞行模式关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
class Person{
    public String name = "person";
    public int count = 0;
    public int addCount(){
        return count;
    }
}
class Student extends Person{
    public String name="student";
    public int count = 100;

    public int addCount(){
        return count;
    }
}