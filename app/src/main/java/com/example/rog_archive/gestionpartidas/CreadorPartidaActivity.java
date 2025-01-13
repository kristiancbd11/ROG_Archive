package com.example.rog_archive.gestionpartidas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;

public class CreadorPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partida);  // Asegúrate de tener el layout correcto para este Activity

        // Si quieres que el fragmento se cargue nada más entrar a esta actividad,
        // simplemente reemplaza el contenedor del fragmento con CargarPartidaFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CargarPartidaFragment()) // R.id.fragment_container debe ser un FrameLayout o contenedor adecuado en tu layout
                    .commit();
        }
    }
}