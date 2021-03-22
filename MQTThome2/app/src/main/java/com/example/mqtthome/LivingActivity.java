package com.example.mqtthome;

import android.app.Service;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LivingActivity extends AppCompatActivity implements SceneDialog.SceneDialogListener {

    static String MQTTHOST = "tcp://192.168.0.155:1883";
    MqttAndroidClient client;

    SeekBar skall;

    String topic;
    String message;
    String Topic_In,kittopic;

    ToggleButton tvbtn, lightbtn;

    ImageView connect_img;
    Button senbtn;
    String t = "true";
    String f = "false";
    int ch = 0;

    ImageView paintimage;
    View colorview;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);


        skall = findViewById(R.id.skright);

        connect_img = findViewById(R.id.img);
        senbtn = findViewById(R.id.sencebtn);
        paintimage = findViewById(R.id.colorpaint);
        colorview = findViewById(R.id.colorview);

        tvbtn = findViewById(R.id.tvbtn);
        lightbtn = findViewById(R.id.lightbtn);

        paintimage.setDrawingCacheEnabled(true);
        paintimage.buildDrawingCache(true);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        //Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    connect_img.setBackgroundResource(R.drawable.ic_connect);
                    Toast.makeText(LivingActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    sub();
                    kitsub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    connect_img.setBackgroundResource(R.drawable.ic_close);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        // paint
        paintimage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                    bitmap = paintimage.getDrawingCache();
                    int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    colorview.setBackgroundColor(Color.rgb(r, g, b));
                    topic = "home/2f/livingroom/light/RGBled";
                    //String ge = "#"+Integer.toHexString(r)+Integer.toHexString(g)+Integer.toHexString(b);
                    String hex = "#" + Integer.toHexString(pixel);
                    message = hex;
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }
        });

        skall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //移動一直觸發
                message = String.valueOf(seekBar.getProgress());
                topic = "home/2f/livingroom/light/led";
                if (ch == 0) {
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                ch = 0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //點擊觸發，只有第一下
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //放開觸發
            }
        });

        senbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SceneDialog scenedialog = new SceneDialog();
                scenedialog.show(getSupportFragmentManager(), "sceneDialog");
            }
        });

        tvbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/2f/livingroom/tv";
                if (tvbtn.isChecked() == true) {
                    message = "on";
                } else {
                    message = "off";
                }

                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });

        lightbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/2f/livingroom/light/RGBled";
                if (isChecked == true) {
                    message = "on";
                } else {
                    message = "off";
                }

                try {
                    client.publish(topic, message.getBytes(), 0, false);
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
                final String msg = message.toString();

                if (topic.equals("home/2f/livingroom/light/led")) {
                    final int i = Integer.parseInt(msg);
                    ch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            skall.setProgress(i);
                        }
                    });
                } else if (topic.equals("home/2f/livingroom/light/RGBled")) {
                    ch = 1;
                    if (msg.equals("off") || msg.equals("#ff000000")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lightbtn.setChecked(false);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lightbtn.setChecked(true);
                            }
                        });
                    }
                } else if (topic.equals("home/2f/livingroom/tv")) {
                    ch = 1;
                    if (msg.equals("on")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvbtn.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvbtn.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/2f/kitchen/MQ2")) {
                    if (msg.equals("WARNING")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setVibrate(5000);
                                ldialog();
                            }
                        });
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void sub() {
        Topic_In = "home/2f/livingroom/#";
        try {
            client.subscribe(Topic_In, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void kitsub() {
        kittopic = "home/2f/kitchen/MQ2";
        try {
            client.subscribe(Topic_In, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void applytext(String scene) {
        topic = "home/2f/livingroom/mode";
        if (scene.equals("正常模式")) {
            message = "normal";
            paintimage.setVisibility(View.GONE);
            colorview.setVisibility(View.GONE);
        } else if (scene.equals("電影院模式")) {
            message = "movie";
            paintimage.setVisibility(View.GONE);
            colorview.setVisibility(View.GONE);
        } else if (scene.equals("氣氛燈模式")) {
            paintimage.setVisibility(View.VISIBLE);
            colorview.setVisibility(View.VISIBLE);
            message = "scenelight";
        }
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    //對話框
    public void ldialog() {

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
