package com.example.mqtthome;

import android.app.Service;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DataActivity extends AppCompatActivity {

    static String MQTTHOST = "tcp://192.168.0.155:1883";
    MqttAndroidClient client;

    String Topic_In,topic,datamessage;

    CardView cardv;

    TextView tvtmp,tvgas,tvwet;

    ImageView connect_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        connect_img = findViewById(R.id.img);
        cardv = findViewById(R.id.card_view);
        tvtmp = findViewById(R.id.tmptv);
        tvgas = findViewById(R.id.gasserver);
        tvwet = findViewById(R.id.wettv);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    connect_img.setBackgroundResource(R.drawable.ic_connect);
                    Toast.makeText(DataActivity.this, "connected", Toast.LENGTH_SHORT).show();
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

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                datamessage = message.toString();
                if (topic.equals("home/2f/kitchen/MQ2")) {
                    if (datamessage.equals("WARNING")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvgas.setTextColor(Color.parseColor("#990000"));
                                tvgas.setText("危險 !");
                                setVibrate(5000);
                                //datadialog();
                            }
                        });
                    }
                }else if (topic.equals("home/outdoor/temperature")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvtmp.setText(datamessage);
                        }
                    });
                }else if (topic.equals("home/outdoor/humidity")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvwet.setText(datamessage);
                        }
                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void sub(){
        Topic_In = "home/#";
        try {
            client.subscribe(Topic_In, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //震動
    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    /*public void datadialog(){
        AlertDialog.Builder databuild = new AlertDialog.Builder(this);
        databuild.setMessage("警告瓦斯外洩")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog data_about_dialog = databuild.create();
        data_about_dialog.show();
    }*/
    /*
    //對話框
    public void ddialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("警告瓦斯外洩")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }*/
}
