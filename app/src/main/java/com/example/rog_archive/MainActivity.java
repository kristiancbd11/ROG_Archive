package com.example.rog_archive;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Iniciar música de fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.theme);
        mediaPlayer.setLooping(true);  // La música se repite indefinidamente
        mediaPlayer.start();

        // Obtener la referencia al ImageView principal
        ImageView imageView = findViewById(R.id.hoguera);

        // Establecer la animación en el ImageView
        imageView.setBackgroundResource(R.drawable.animacion);

        // Obtener el AnimationDrawable y iniciar la animación
        AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
        animation.start();

        // Botón Login
        ImageView loginButton = findViewById(R.id.botonLogin);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setImageResource(R.drawable.boton_ini_ses_press);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        loginButton.setImageResource(R.drawable.boton_ini_ses);
                        view.performClick();
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Botón Registro
        ImageView registerButton = findViewById(R.id.botonRegistro);
        registerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        registerButton.setImageResource(R.drawable.boton_reg_press);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        registerButton.setImageResource(R.drawable.boton_reg);
                        view.performClick();
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        }
                        return true;
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausar la música si está reproduciendo
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar la música si está pausada
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libera los recursos del MediaPlayer para evitar fugas de memoria
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
