package com.example.egemobil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.ImageButton;

// 1-> Main
// 2-> Control
// 3-> Map

public class MainActivity extends AppCompatActivity {

    private static ImageButton button1, button3;
    private static  boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button1 = findViewById(R.id.imageButton4);
        button3 = findViewById(R.id.imageButton6);

        initConnectButton();

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

}