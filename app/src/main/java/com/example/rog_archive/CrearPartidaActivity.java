package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CrearPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_partida);

        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearPartidaActivity.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });

        Button botonCargarPartida = findViewById(R.id.botonCargarPartida);
        botonCargarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearPartidaActivity.this, CargarPartidaActivity.class);
                startActivity(intent);
            }
        });

        Button botonNuevaPartida = findViewById(R.id.botonNuevaPartida);
        botonNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearPartidaActivity.this, NuevaPartidaActivity.class);
                startActivity(intent);
            }
        });

    }
}