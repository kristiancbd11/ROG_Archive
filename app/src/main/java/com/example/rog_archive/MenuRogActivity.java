package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuRogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_rog);

        Button archivoButton = findViewById(R.id.botonArchivoPersonajes);
        archivoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, ArchivoPersonajes.class);
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.botonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button crearPartida = findViewById(R.id.botonCrearPartida);
        crearPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, CrearPartidaActivity.class);
                startActivity(intent);
            }

        });

        Button unirsePartida = findViewById(R.id.botonUnirsePartida);
        unirsePartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, UnirsePartidaActivity.class);
                startActivity(intent);
            }

        });
    }
}