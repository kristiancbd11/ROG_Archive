package com.example.rog_archive.gestionpersonajes;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;

public class ArchivoPersonajesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivo_personajes);

        // Cargar el fragmento ArchivoPersonajesFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ArchivoPersonajesFragment()) // Reemplazamos el contenedor con el fragmento
                    .commit();
        }
    }
}