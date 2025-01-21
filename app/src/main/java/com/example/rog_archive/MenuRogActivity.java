package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.gestionpartidas.CreadorPartidaActivity;
import com.example.rog_archive.gestionpersonajes.ArchivoPersonajesActivity;
import com.example.rog_archive.gestionpersonajes.CreadorPersonajesActivity;
import com.example.rog_archive.inicio.MainActivity;

public class MenuRogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_rog);

        ImageButton creadorButton = findViewById(R.id.botonCrearPersonaje);
        creadorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, CreadorPersonajesActivity.class);
                startActivity(intent);
            }
        });

        ImageButton archivoButton = findViewById(R.id.botonArchivoPersonajes);
        archivoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, ArchivoPersonajesActivity.class);
                startActivity(intent);
            }
        });

        ImageButton logoutButton = findViewById(R.id.botonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton crearPartida = findViewById(R.id.botonCrearPartida);
        crearPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, CreadorPartidaActivity.class);
                startActivity(intent);
            }

        });

        ImageButton unirsePartida = findViewById(R.id.botonUnirsePartida);
        unirsePartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuRogActivity.this, UnirsePartidaActivity.class);
                startActivity(intent);
            }

        });
    }
}