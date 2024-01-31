package com.example.egemobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.view.View;

public class ControlScreen extends AppCompatActivity {

    private static ImageButton button1, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button1 = findViewById(R.id.imageButton4);
        button3 = findViewById(R.id.imageButton6);
        button4 = findViewById(R.id.imageButton);

        initConnectButton();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(ControlScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 0);

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent(ControlScreen.this, ControlScreen2.class);
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
                        final Intent intent = new Intent(ControlScreen.this, MainActivity.class);
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