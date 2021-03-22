package com.example.mqtthome;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OpenActivity extends AppCompatActivity {

    TextView mTxt;
    ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        setContentView(R.layout.activity_open);
        mTxt = findViewById(R.id.mtext);

        objectAnimator = ObjectAnimator.ofFloat(mTxt, "alpha", 1, 0);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 4000);//4秒轉跳
    }

    private static final int GOTO_MAIN_ACTIVITY = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(OpenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
