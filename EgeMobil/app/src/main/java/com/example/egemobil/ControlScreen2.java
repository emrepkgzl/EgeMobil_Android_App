package com.example.egemobil;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ControlScreen2 extends AppCompatActivity {

    private static ImageButton sendButton;
    private static EditText eTxtTopic;
    private static EditText eTxtMsg;

    private static ImageButton button1, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sendButton = findViewById(R.id.imageButton);
        eTxtTopic = findViewById(R.id.editTextText2);
        eTxtMsg = findViewById(R.id.editTextText3);
        button1 = findViewById(R.id.imageButton4);
        button3 = findViewById(R.id.imageButton6);

        initConnectButton();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.topicToSend = eTxtTopic.getText().toString();
                MainActivity.messageToSend = eTxtMsg.getText().toString();

                Toast.makeText(getApplicationContext(),"Message sent.", Toast.LENGTH_SHORT).show();

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(ControlScreen2.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(ControlScreen2.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });
    }

    private void initConnectButton()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!MainActivity.isConnected) {
                    button1.setImageResource(R.drawable.signal_stream);
                }
                else
                {
                    button1.setImageResource(R.drawable.signal_stream_slash);
                }
            }
        });
    }
}