package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ArchivoPersonajes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_archivo_personajes);

        // Botón para regresar al menú anterior
        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArchivoPersonajes.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });

        // Mostrar aviso "Personaje subido con éxito"
        ImageView flechaSubida = findViewById(R.id.imageViewFlechaSubida);
        flechaSubida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ArchivoPersonajes.this, "Personaje subido con éxito", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar aviso "Personajes descargados"
        Button botonDescarga = findViewById(R.id.botonDescarga);
        botonDescarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ArchivoPersonajes.this, "Personajes descargados", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
