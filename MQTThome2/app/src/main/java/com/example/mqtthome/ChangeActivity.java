package com.example.mqtthome;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.w3c.dom.Text;

public class ChangeActivity extends AppCompatActivity {

    static String MQTTHOST = "tcp://192.168.0.155:1883";
    MqttAndroidClient client;

    Button loginbtn;
    TextInputEditText useret;
    TextInputEditText password;
    TextInputEditText passagain;
    ImageView connect_img;
    /*String Topic_In;
    String topicwifiuserid;
    String messagewifiuserid;
    String topicwifipassword;
    String messagewifipassword;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        loginbtn = findViewById(R.id.login_btn);
        useret = findViewById(R.id.user_id);
        password = findViewById(R.id.pass_w);
        passagain = findViewById(R.id.pass_again);
        connect_img = findViewById(R.id.img);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(ChangeActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    connect_img.setBackgroundResource(R.drawable.ic_connect);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    connect_img.setBackgroundResource(R.drawable.ic_close);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String topicwifiuserid = "home/wifi/ssid";
                final String topicwifipassword = "home/wifi/password";
                String messagewifiuserid = useret.getText().toString() + "$";
                final String messagewifipassword = password.getText().toString() + "$";

                if (useret.getText().length() > 30) {
                    useret.setError("長度超出設定");
                    return;
                } else if (password.getText().length() > 15) {
                    password.setError("長度超出設定");
                    return;
                } else if (passagain.getText().length() > 15) {
                    passagain.setError("長度超出設定");
                    return;
                } else if (password.getText().length() <= 0) {
                    password.setError("輸入欄為空");
                    return;
                } else if (useret.getText().length() <= 0) {
                    useret.setError("輸入欄為空");
                    return;
                } else {
                    if (password.getText().toString().equals(passagain.getText().toString())) {
                        loginbtn.setEnabled(false);
                        loginbtn.setText("更改中...");
                        //Toast.makeText(ChangeActivity.this,messagewifiuserid+"/n", Toast.LENGTH_SHORT).show();
                        try {
                            client.publish(topicwifiuserid, messagewifiuserid.getBytes(), 1, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(ChangeActivity.this, "a", Toast.LENGTH_SHORT).show();
                                try {
                                    client.publish(topicwifipassword, messagewifipassword.getBytes(), 1, false);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }

                                //換頁
                                Intent intentscreen = new Intent(ChangeActivity.this, ScreenActivity.class);
                                intentscreen.putExtra("wifissid", useret.getText().toString());
                                intentscreen.putExtra("wifipassword", password.getText().toString());
                                startActivity(intentscreen);
                                finish();
                            }
                        }, 3000);
                    } else {
                        passagain.setError("確認密碼不相同");
                        return;
                    }
                    /*useret.setText("");
                    password.setText("");
                    passagain.setText("");
                    loginbtn.setEnabled(true);*/
                }

            }
        });
    }
}