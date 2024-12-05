package com.example.rog_archive;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_partida);

        // Obtener la referencia al ImageView
        ImageView descanso = findViewById(R.id.hoguera);

        // Establecer la animación en el ImageView
        descanso.setBackgroundResource(R.drawable.animacion);

        // Obtener el AnimationDrawable
        AnimationDrawable animation = (AnimationDrawable) descanso.getBackground();

        // Iniciar la animación
        animation.start();

        ImageView combate = findViewById(R.id.espada);
        combate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPartidaActivity.this, CombateActivity.class);
                startActivity(intent);
            }
        });

        descanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPartidaActivity.this, TipoDescansoActivity.class);
                startActivity(intent);
            }
        });

        ImageView exploracion = findViewById(R.id.arbol);
        exploracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPartidaActivity.this, ExploracionActivity.class);
                startActivity(intent);
            }
        });

        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPartidaActivity.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });
    }
}