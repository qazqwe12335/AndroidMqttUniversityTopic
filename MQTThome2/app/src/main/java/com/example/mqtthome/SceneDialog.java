package com.example.mqtthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SceneDialog extends DialogFragment {
    private String selection;
    private SceneDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String[] scenes = getActivity().getResources().getStringArray(R.array.scenes);

        AlertDialog.Builder scenebuild = new AlertDialog.Builder(getActivity());
        //scenebuild.setTitle("情境選擇 :");
        scenebuild.setSingleChoiceItems(R.array.scenes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = scenes[which];
            }
        });
        scenebuild.setPositiveButton("確 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String scene = selection;
                listener.applytext(scene);
                }

        });
        scenebuild.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return scenebuild.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SceneDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    public interface SceneDialogListener{
        void applytext(String scene);
    }
}
