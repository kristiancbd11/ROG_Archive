package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestionficheros.GestorFicheros;

import java.util.ArrayList;

public class SeleccionarPersonajeActivity extends AppCompatActivity {

    private LinearLayout linearLayoutPersonajes; // LinearLayout donde se añadirán los botones de personajes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_personaje);

        // Inicializamos el LinearLayout
        linearLayoutPersonajes = findViewById(R.id.linear_layout_personajes);

        // Cargar y mostrar personajes
        cargarYMostrarPersonajes();

        // Configurar el botón de atrás
        ImageView botonAtras = findViewById(R.id.imageViewAtras);
        botonAtras.setOnClickListener(view -> {
            Intent intent = new Intent(SeleccionarPersonajeActivity.this, MenuRogActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Cargar los personajes desde el GestorFicheros y mostrar botones dinámicamente.
     */
    private void cargarYMostrarPersonajes() {
        GestorFicheros gestorFicheros = new GestorFicheros();
        ArrayList<Personaje> personajes = gestorFicheros.cargarPersonajes(getApplicationContext());

        if (personajes.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No hay personajes para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Personaje personaje : personajes) {
            // Crear un botón para cada personaje
            Button botonPersonaje = new Button(this);
            String texto = personaje.getNombre() + " - " + personaje.getClase() + " | Nv " + personaje.getNivel();
            botonPersonaje.setText(texto);

            // Configurar el comportamiento al hacer clic en el botón
            botonPersonaje.setOnClickListener(v -> {
                // Abrir MenuPartidaActivity al hacer clic en un personaje
                Intent intent = new Intent(SeleccionarPersonajeActivity.this, MenuPartidaActivity.class);
                startActivity(intent);
            });

            // Añadir el botón al LinearLayout
            linearLayoutPersonajes.addView(botonPersonaje);
        }
    }
}