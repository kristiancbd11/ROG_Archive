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

public class SeleccionarPersonajeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccionar_personaje);

        Button seleccionarPersonaje = findViewById(R.id.botonPersonaje);
        seleccionarPersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeleccionarPersonajeActivity.this, MenuPartidaActivity.class);
                startActivity(intent);
            }
        });

        ImageView botonAtras = findViewById(R.id.imageViewAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeleccionarPersonajeActivity.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });

        // botonPersonaje
    }
}