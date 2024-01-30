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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "EGEMOBIL";
    //private static final String TOPIC = "EGEMOBIL/speed";
    private static final String TAG = "MyTag";

    private String oldMsg = "";
    private MqttAndroidClient client;
    public static String arrivedMessage;

    private static ImageButton button1, button3;

    public static TextView txtSpeed, txtAngle, txtAcclr, txtBrake, txtLMSpeed, txtRMSpeed, txtLMTemp,
            txtRMTemp, txtBatLvl, txtBatVol, txtBatTemp, txtBatSmk, txtDistnc, txtOTemp, txtPing, txtSysTime;
    private static  boolean isConnected = false;
    private Timer timer;
    public static String topicToSend = "";
    public static String messageToSend = "";
    public static int toastMessageKey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button1 = findViewById(R.id.imageButton4);
        button3 = findViewById(R.id.imageButton6);
        txtSpeed = findViewById(R.id.textView2);
        txtAngle = findViewById(R.id.textView3);
        txtAcclr = findViewById(R.id.textView4);
        txtBrake = findViewById(R.id.textView5);
        txtLMSpeed = findViewById(R.id.textView6);
        txtRMSpeed = findViewById(R.id.textView7);
        txtLMTemp = findViewById(R.id.textView8);
        txtRMTemp = findViewById(R.id.textView9);
        txtBatLvl = findViewById(R.id.textView10);
        txtBatVol = findViewById(R.id.textView11);
        txtBatTemp = findViewById(R.id.textView12);
        txtBatSmk = findViewById(R.id.textView13);
        txtDistnc = findViewById(R.id.textView14);
        txtOTemp = findViewById(R.id.textView15);
        txtPing = findViewById(R.id.textView16);
        txtSysTime = findViewById(R.id.textView17);

        initConnectButton();

        client = new MqttAndroidClient(this.getApplicationContext(), BROKER_URL, CLIENT_ID);
        //conectX();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(messageToSend != "")
                {
                    publish(topicToSend, messageToSend);
                    topicToSend = "";
                    messageToSend = "";
                }

                if(toastMessageKey == 1)
                {
                    Toast.makeText(getApplicationContext(),"The key has already been added.", Toast.LENGTH_SHORT).show();
                }

            }
        }, 0, 500);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isConnected) {
                            isConnected = true;
                            button1.setImageResource(R.drawable.signal_stream_slash);
                            conectX();
                        }
                        else
                        {
                            button1.setImageResource(R.drawable.signal_stream);
                            isConnected = false;
                            try {
                                client.disconnect();
                            } catch (MqttException e) {
                                throw new RuntimeException(e);
                            }
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
                    sub("EGEMOBIL/speed");
                    sub("EGEMOBIL/angle");
                    sub("EGEMOBIL/acclr");
                    sub("EGEMOBIL/brake");
                    sub("EGEMOBIL/lmspeed");
                    sub("EGEMOBIL/rmspeed");
                    sub("EGEMOBIL/lmtemp");
                    sub("EGEMOBIL/rmtemp");
                    sub("EGEMOBIL/batlvl");
                    sub("EGEMOBIL/batvol");
                    sub("EGEMOBIL/battemp");
                    sub("EGEMOBIL/batsmk");
                    sub("EGEMOBIL/distnc");
                    sub("EGEMOBIL/otemp");
                    sub("EGEMOBIL/ping");
                    sub("EGEMOBIL/systime");
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

    private void sub(String topic)
    {
        try {
            client.subscribe(topic, 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                button1.setImageResource(R.drawable.signal_stream);
                                isConnected = false;
                        }
                    });
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String msg = new String(message.getPayload());
                    Log.d(TAG, "topic: " + topic);
                    Log.d(TAG, "message: " + msg);

                    switch(topic) {
                        case "EGEMOBIL/speed": txtSpeed.setText("Speed: " + msg); break;
                        case "EGEMOBIL/angle": txtAngle.setText("Angle: " + msg); break;
                        case "EGEMOBIL/acclr": txtAcclr.setText("Acclr: " + msg); break;
                        case "EGEMOBIL/brake": txtBrake.setText("Brake: " + msg); break;
                        case "EGEMOBIL/lmspeed": txtLMSpeed.setText("LM Speed :" + msg); break;
                        case "EGEMOBIL/rmspeed": txtRMSpeed.setText("RM Speed: " + msg); break;
                        case "EGEMOBIL/lmtemp": txtLMTemp.setText("LM Temp: " + msg); break;
                        case "EGEMOBIL/rmtemp": txtRMTemp.setText("RM Tempd: " + msg); break;
                        case "EGEMOBIL/batlvl": txtBatLvl.setText("Bat Level: " + msg); break;
                        case "EGEMOBIL/batvol": txtBatVol.setText("Bat Voltage: " + msg); break;
                        case "EGEMOBIL/battemp": txtBatTemp.setText("Bat Temp: " + msg); break;
                        case "EGEMOBIL/batsmoke": txtBatSmk.setText("Bat Smoke: " + msg); break;
                        case "EGEMOBIL/distnc": txtDistnc.setText("Distance: " + msg); break;
                        case "EGEMOBIL/otemp": txtOTemp.setText("Out Temp: " + msg); break;
                        case "EGEMOBIL/ping": txtPing.setText("Ping: " + msg); break;
                        case "EGEMOBIL/systime": txtSysTime.setText("Sys Time: " + msg); break;
                    }
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