package com.example.mqtthome;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class KitchenActivity extends AppCompatActivity {

    static String MQTTHOST = "tcp://192.168.0.155:1883";
    MqttAndroidClient client;

    String Topic_In;
    String topic;
    String message;

    Switch sw_light, sw_fan;
    ImageView connect_img;
    TextView wwaring;
    int kitchench = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        sw_fan = findViewById(R.id.kitchen_fan);
        sw_light = findViewById(R.id.kitchen_light);
        connect_img = findViewById(R.id.img);
        wwaring = findViewById(R.id.stutstext);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        //Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    connect_img.setBackgroundResource(R.drawable.ic_connect);
                    Toast.makeText(KitchenActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    connect_img.setBackgroundResource(R.drawable.ic_close);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        connect_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(KitchenActivity.this, "連線狀態", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // switch onclick
        sw_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/2f/kitchen/light";
                if (isChecked == true) {
                    message = sw_light.getTextOn().toString();
                } else {
                    message = sw_light.getTextOff().toString();
                }

                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        sw_fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/2f/kitchen/fan";
                if (isChecked == true) {
                    message = sw_light.getTextOn().toString();
                } else {
                    message = sw_light.getTextOff().toString();
                }

                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = message.toString();
                //Toast.makeText(KitchenActivity.this, ""+topic+msg, Toast.LENGTH_SHORT).show();
                if (topic.equals("home/2f/kitchen/light")){
                    if (msg.equals("on")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(true);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(false);
                            }
                        });
                    }
                }if (topic.equals("home/2f/kitchen/fan")){
                    if (msg.equals("on")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_fan.setChecked(true);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_fan.setChecked(false);
                            }
                        });
                    }
                }else if (topic.equals("home/2f/kitchen/MQ2")){
                    if (msg.equals("WARNING")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wwaring.setTextColor(Color.parseColor("#990000"));
                                wwaring.setText("危險 !");
                                setVibrate(5000);
                                dialog();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wwaring.setText("良好");
                                wwaring.setTextColor(Color.parseColor("#009999"));
                            }
                        });
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        /*client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(final String topic, MqttMessage message) throws Exception {
                final String msg = message.toString();

                Toast.makeText(KitchenActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                String on = "on";
                String warning = "home/2f/kitchen/MQ2";
                String text = "WARNING";
                String light = "home/2f/kitchen/light";
                if (topic.equals("home/2f/kitchen/light")) {
                    if (msg.equals("on")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/2f/kitchen/fan")) {
                    if (msg.equals(on)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals(warning) && msg.equals(text)){
                    setVibrate(5000);
                    dialog();
                }/*else if (topic.equals(warning)) {
                    if (msg.equals(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wwaring.setTextColor(Integer.parseInt("#990000"));
                                wwaring.setText("危險 !");
                                setVibrate(5000);
                                dialog();
                            }
                        });
                    } else if (msg.equals("SAFE")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wwaring.setTextColor(Integer.parseInt("#009999"));
                                wwaring.setText("良好");
                            }
                        });
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });*/
    }

    public void sub() {
        Topic_In = "home/2f/kitchen/#";
        try {
            client.subscribe(Topic_In, 1);
            //Toast.makeText(this, "KITCHEN_on", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //震動
    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    //對話框
    public void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("警告瓦斯外洩")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }
}
