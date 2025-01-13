package com.example.rog_archive.inicio;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;

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

        // Botón Login corregido
        ImageButton loginButton = findViewById(R.id.botonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ImageButton registerButton = findViewById(R.id.botonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
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