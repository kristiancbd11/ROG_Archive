package com.example.rog_archive.vistasdepartida;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;

public class MenuPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_partida);

        // Obtener el objeto partida
        Partida partida = (Partida) getIntent().getSerializableExtra("PARTIDA");

        // Obtener la referencia al ImageView
        ImageView descanso = findViewById(R.id.hoguera);

        // Establecer la animación en el ImageView
        descanso.setBackgroundResource(R.drawable.animacion);

        // Obtener el AnimationDrawable
        AnimationDrawable animation = (AnimationDrawable) descanso.getBackground();

        // Iniciar la animación
        animation.start();

        // Settear el nombre de la partida en la vista
        TextView nombrePartida = findViewById(R.id.nombrePartida);
        if (partida != null) {
            nombrePartida.setText(partida.getNombre());
        } else {
            nombrePartida.setText("No se ha recibido la partida.");
        }

        ImageView combate = findViewById(R.id.espada);
        combate.setOnClickListener(view -> {
            Intent intent = new Intent(MenuPartidaActivity.this, CombateActivity.class);
            intent.putExtra("PARTIDA", partida); // Enviar la partida
            startActivity(intent);
        });

        descanso.setOnClickListener(view -> {
            String nPartida = partida.getNombre();
            Intent intent = new Intent(MenuPartidaActivity.this, TipoDescansoActivity.class);
            intent.putExtra("PARTIDA", nPartida);  // Enviar la partida
            startActivity(intent);
        });

        ImageView exploracion = findViewById(R.id.arbol);
        exploracion.setOnClickListener(view -> {
            String nPartida = partida.getNombre();
            Intent intent = new Intent(MenuPartidaActivity.this, ExploracionActivity.class);
            intent.putExtra("PARTIDA", nPartida);  // Enviar la partida
            startActivity(intent);
        });
    }
}
