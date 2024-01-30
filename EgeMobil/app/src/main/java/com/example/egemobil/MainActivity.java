package com.example.egemobil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "GX5632";
    private static final String TOPIC = "GX5632AC/deneme";
    private static final String TAG = "MyTag";

    private String oldMsg = "";
    private MqttAndroidClient client;
    public static String arrivedMessage;

    private static ImageButton button1, button3;

    public static TextView txt1;
    private static  boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button1 = findViewById(R.id.imageButton4);
        button3 = findViewById(R.id.imageButton6);
        txt1 = findViewById(R.id.textView2);

        initConnectButton();

        client = new MqttAndroidClient(this.getApplicationContext(), BROKER_URL, CLIENT_ID);
        conectX();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isConnected) {
                            isConnected = true;
                            button1.setImageResource(R.drawable.signal_stream_slash);
                        }
                        else
                        {
                            button1.setImageResource(R.drawable.signal_stream);
                            isConnected = false;
                        }
                    }
                });

            }
        });



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(MainActivity.this, ControlScreen.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });

    }

    private void conectX()
    {

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess!!!");
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure!!!");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void initConnectButton()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isConnected) {
                    button1.setImageResource(R.drawable.signal_stream);
                }
                else
                {
                    button1.setImageResource(R.drawable.signal_stream_slash);
                }
            }
        });
    }

    private void sub()
    {
        try {
            client.subscribe(TOPIC, 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String msg = new String(message.getPayload());
                    Log.d(TAG, "topic: " + topic);
                    Log.d(TAG, "message: " + msg);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }catch (MqttException e){

        }
    }

    public void publish(String topic, String message)
    {
        try {
            MqttMessage mqttMessage = new MqttMessage((message.getBytes()));
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}