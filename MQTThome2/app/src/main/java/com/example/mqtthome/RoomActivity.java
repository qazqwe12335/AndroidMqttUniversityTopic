package com.example.mqtthome;

import android.app.Service;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
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

public class RoomActivity extends AppCompatActivity implements WattDialog.WattDialogListener {

    static String MQTTHOST = "tcp://192.168.0.155:1883";
    MqttAndroidClient client;

    private ImageView tableic, lampic, lightic, screenic, fanic, windowic, hostic;
    private Switch swscreen, swfan, auto, swhost;
    private SeekBar sblamp, sblight, sbtable, sbwindow;
    private CheckBox cbscreen, cbfan, cblight, cblamp, cbtable, cbhost;
    private ImageView connectimg;
    private TextView textmoney;

    int imgstute, roomch = 0;
    String message, topic, Topic_In, On = "true", Off = "false", kittopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        textmoney = findViewById(R.id.money);

        tableic = findViewById(R.id.tablelighticon);
        lampic = findViewById(R.id.lampicon);
        lightic = findViewById(R.id.lighticon);
        screenic = findViewById(R.id.screenicon);
        fanic = findViewById(R.id.fanicon);
        windowic = findViewById(R.id.window_icon);
        hostic = findViewById(R.id.hosticon);

        swscreen = findViewById(R.id.sw_screen);
        swfan = findViewById(R.id.sw_fan);
        swhost = findViewById(R.id.sw_host);
        auto = findViewById(R.id.AUTO);

        sblamp = findViewById(R.id.sb_lamp);
        sblight = findViewById(R.id.sb_light);
        sbtable = findViewById(R.id.sb_table);
        sbwindow = findViewById(R.id.sb_sb_window);

        cbscreen = findViewById(R.id.cb_screen);
        cbfan = findViewById(R.id.cb_fan);
        cblamp = findViewById(R.id.cb_lamp);
        cblight = findViewById(R.id.cb_light);
        cbtable = findViewById(R.id.cb_table);
        cbhost = findViewById(R.id.cb_host);

        connectimg = findViewById(R.id.img);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    connectimg.setBackgroundResource(R.drawable.ic_connect);
                    Toast.makeText(RoomActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    sub();
                    kitsub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    connectimg.setBackgroundResource(R.drawable.ic_close);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        hostic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 3;
                wattdialog();
                return false;
            }
        });
        tableic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 4;
                wattdialog();
                return false;
            }
        });
        lampic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 6;
                wattdialog();
                return false;
            }
        });
        lightic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 5;
                wattdialog();
                return false;
            }
        });
        screenic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 1;
                wattdialog();
                return false;
            }
        });
        fanic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgstute = 2;
                wattdialog();
                return false;
            }
        });

        swscreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/screen";
                if (roomch == 0) {
                    if (isChecked == true) {
                        screenic.setBackgroundResource(R.drawable.icon_red_screen);
                        message = swscreen.getTextOn().toString();
                    } else {
                        screenic.setBackgroundResource(R.drawable.icon_block_screen);
                        message = swscreen.getTextOff().toString();
                    }
                    try {
                        client.publish(topic, message.getBytes(), 1, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }
        });
        swfan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/fan";
                if (roomch == 0) {
                    if (isChecked == true) {
                        fanic.setBackgroundResource(R.drawable.icon_red_fan);
                        message = swfan.getTextOn().toString();
                    } else {
                        fanic.setBackgroundResource(R.drawable.icon_block_fan);
                        message = swfan.getTextOff().toString();
                    }
                    try {
                        client.publish(topic, message.getBytes(), 1, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }
        });

        swhost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/computer";
                if (roomch == 0) {
                    if (isChecked == true) {
                        hostic.setBackgroundResource(R.drawable.icon_red_server);
                        message = swhost.getTextOn().toString();
                    } else {
                        hostic.setBackgroundResource(R.drawable.icon_server);
                        message = swhost.getTextOff().toString();
                    }
                    try {
                        client.publish(topic, message.getBytes(), 1, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/automatic";
                if (isChecked == true) {
                    message = auto.getTextOn().toString();
                } else {
                    message = auto.getTextOff().toString();
                }

                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        sbtable.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() > 0) {
                    tableic.setBackgroundResource(R.drawable.icon_red_table_light);
                } else {
                    tableic.setBackgroundResource(R.drawable.icon_block_table_light);
                }
                topic = "home/3f/bedroom/lights/desktopLight";
                message = String.valueOf(seekBar.getProgress());
                if (roomch == 0) {
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbwindow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() > 0) {
                    windowic.setBackgroundResource(R.drawable.icon_yellow_window);
                } else {
                    windowic.setBackgroundResource(R.drawable.icon_block_window);
                }
                topic = "home/3f/bedroom/window";
                message = String.valueOf(seekBar.getProgress());
                if (roomch == 0) {
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sblight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() > 0) {
                    lightic.setBackgroundResource(R.drawable.icon_red_light);
                } else {
                    lightic.setBackgroundResource(R.drawable.icon_block_light);
                }
                topic = "home/3f/bedroom/lights/mainLED";
                message = String.valueOf(seekBar.getProgress());
                if (roomch == 0) {
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sblamp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() > 0) {
                    lampic.setBackgroundResource(R.drawable.icon_yellow_lamp);
                } else {
                    lampic.setBackgroundResource(R.drawable.icon_block_lamp);
                }
                topic = "home/3f/bedroom/lights/bedsideLight";
                message = String.valueOf(seekBar.getProgress());
                if (roomch == 0) {
                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                roomch = 0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cbscreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "1" + On;
                    //swscreen.setEnabled(false);
                } else {
                    message = "1" + Off;
                    //swscreen.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        cbfan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "2" + On;
                    //swfan.setEnabled(false);
                } else {
                    message = "2" + Off;
                    //swfan.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        cbhost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "3" + On;
                    //swhost.setEnabled(false);
                } else {
                    message = "3" + Off;
                    //swhost.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        cbtable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "4" + On;
                    //sbtable.setEnabled(false);
                } else {
                    message = "4" + Off;
                    //sbtable.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        cblight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "5" + On;
                    //sblight.setEnabled(false);
                } else {
                    message = "5" + Off;
                    //sblight.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });
        cblamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                topic = "home/3f/bedroom/retain";
                if (isChecked == true) {
                    message = "6" + On;
                    //sblamp.setEnabled(false);
                } else {
                    message = "6" + Off;
                    //sblamp.setEnabled(true);
                }
                try {
                    client.publish(topic, message.getBytes(), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
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
                String on = "on";
                final String msg = message.toString();
                //final int ii = Integer.parseInt(message.toString());
                //Toast.makeText(RoomActivity.this, topic+ "  "+msg, Toast.LENGTH_SHORT).show();
                if (topic.equals("home/3f/bedroom/automatic")) {
                    roomch = 1;
                    if (msg.equals(on)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                auto.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                auto.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/3f/bedroom/screen")) {
                    if (msg.equals(on)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swscreen.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swscreen.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/3f/bedroom/computer")) {
                    if (msg.equals(on)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swhost.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swhost.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/3f/bedroom/fan")) {
                    if (msg.equals(on)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swfan.setChecked(true);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swfan.setChecked(false);
                            }
                        });
                    }
                } else if (topic.equals("home/3f/bedroom/lights/desktopLight")) {
                    roomch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sbtable.setProgress(Integer.parseInt(msg));
                        }
                    });
                } else if (topic.equals("home/3f/bedroom/lights/mainLED")) {
                    roomch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sblight.setProgress(Integer.parseInt(msg));
                        }
                    });
                } else if (topic.equals("home/3f/bedroom/lights/bedsideLight")) {
                    roomch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sblamp.setProgress(Integer.parseInt(msg));
                        }
                    });
                } else if (topic.equals("home/3f/bedroom/window")) {
                    roomch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sbwindow.setProgress(Integer.parseInt(msg));
                        }
                    });
                } else if (topic.equals("home/3f/bedroom/retain")) {
                    if (msg.equals("1true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbscreen.setChecked(true);
                            }
                        });
                    } else if (msg.equals("1false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbscreen.setChecked(false);
                            }
                        });
                    } else if (msg.equals("2true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbfan.setChecked(true);
                            }
                        });
                    } else if (msg.equals("2false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbfan.setChecked(false);
                            }
                        });
                    } else if (msg.equals("3true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbhost.setChecked(true);
                            }
                        });
                    } else if (msg.equals("3false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbhost.setChecked(false);
                            }
                        });
                    } else if (msg.equals("4true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbtable.setChecked(true);
                            }
                        });
                    } else if (msg.equals("4false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cbtable.setChecked(false);
                            }
                        });
                    } else if (msg.equals("5true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cblight.setChecked(true);
                            }
                        });
                    } else if (msg.equals("5false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cblight.setChecked(false);
                            }
                        });
                    } else if (msg.equals("6true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cblamp.setChecked(true);
                            }
                        });
                    } else if (msg.equals("6false")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cblamp.setChecked(false);
                            }
                        });
                    }
                    /*if (msg.substring(1, 2).equals("1")) {
                        if (msg.substring(2).equals("ture")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cbscreen.setChecked(true);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cbscreen.setChecked(false);
                                }
                            });
                        }
                    }
                    if (msg.substring(1, 2).equals("2")) {
                        if (msg.substring(2).equals("ture")) {
                            cbfan.setChecked(true);
                        } else {
                            cbfan.setChecked(false);
                        }
                    }
                    if (msg.substring(1, 2).equals("3")) {
                        if (msg.substring(2).equals("ture")) {
                            cbhost.setChecked(true);
                        } else {
                            cbhost.setChecked(false);
                        }
                    }
                    if (msg.substring(1, 2).equals("4")) {
                        if (msg.substring(2).equals("ture")) {
                            cbtable.setChecked(true);
                        } else {
                            cbtable.setChecked(false);
                        }
                    }
                    if (msg.substring(1, 2).equals("5")) {
                        if (msg.substring(2).equals("ture")) {
                            cblight.setChecked(true);
                        } else {
                            cblight.setChecked(false);
                        }
                    }
                    if (msg.substring(1, 2).equals("6")) {
                        if (msg.substring(2).equals("ture")) {
                            cblamp.setChecked(true);
                        } else {
                            cblamp.setChecked(false);
                        }
                    }*/
                } else if (topic.equals("home/3f/bedroom/bill")) {
                    roomch = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textmoney.setText(msg);
                        }
                    });
                } else if (topic.equals("home/2f/kitchen/MQ2")) {
                    if (msg.equals("WARNING")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setVibrate(5000);
                                rdialog();
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

    public void wattdialog() {
        WattDialog wattDialog = new WattDialog();
        wattDialog.show(getSupportFragmentManager(), "Watt Dialog");
    }

    public void sub() {
        Topic_In = "home/3f/bedroom/#";
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
    public void applywatt(String watt) {
        topic = "home/3f/bedroom/Watt/set";
        message = imgstute + watt;
        if (roomch == 0) {
            try {
                client.publish(topic, message.getBytes(), 1, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        roomch = 0;
    }

    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    //對話框
    public void rdialog() {

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