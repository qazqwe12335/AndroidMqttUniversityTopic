package com.example.mqtthome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ScreenActivity extends AppCompatActivity {

    TextView ssid;
    TextView password;
    Button sbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        ssid = findViewById(R.id.textssid);
        password = findViewById(R.id.textpassword);

        Intent intent = getIntent();
        String wifiuserssid = intent.getStringExtra("wifissid");
        String wifiuserpassword = intent.getStringExtra("wifipassword");

        String textuserssid = "WIFI帳號 : " + wifiuserssid;
        String textuserpassword = "WIFI密碼 : " + wifiuserpassword;
        ssid.setText(textuserssid);
        password.setText(textuserpassword);
    }

}
