package com.example.mqtthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View v){

        switch (v.getId()){
            case R.id.kitchen:
                Intent intent = new Intent(this,KitchenActivity.class);
                startActivity(intent);
                break;
            case R.id.living:
                Intent intentliving = new Intent(this,LivingActivity.class);
                startActivity(intentliving);
                break;
            case R.id.room:
                Intent intentroom = new Intent(this,RoomActivity.class);
                startActivity(intentroom);
                break;
            case R.id.change:
                Intent intentC = new Intent(this,ChangeActivity.class);
                startActivity(intentC);
                break;
            case R.id.data:
                Intent intentdata = new Intent(this,DataActivity.class);
                startActivity(intentdata);
                break;
        }
    }
}
