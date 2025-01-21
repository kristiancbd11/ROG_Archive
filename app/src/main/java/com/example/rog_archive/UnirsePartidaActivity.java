package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.gestiontcp.BuscadorTCP;

import java.util.ArrayList;

public class UnirsePartidaActivity extends AppCompatActivity {

    private LinearLayout layoutBotones; // LinearLayout para los botones
    private BuscadorTCP buscadorTCP; // Instancia de BuscadorTCP
    private ArrayList<String> hostsDetectados = new ArrayList<>(); // Lista local para controlar hosts ya agregados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unirse_partida);

        // Configurar el botón "Atrás"
        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(view -> {
            Intent intent = new Intent(UnirsePartidaActivity.this, MenuRogActivity.class);
            startActivity(intent);
        });

        // Referencias a los elementos del diseño
        layoutBotones = findViewById(R.id.layoutBotones);
        buscadorTCP = new BuscadorTCP(); // Instanciar el BuscadorTCP

        ImageView refreshButton = findViewById(R.id.imageRefresh);
        refreshButton.setOnClickListener((view -> {
            escuchar();
        }));

    }

    private void escuchar() {
        buscadorTCP.buscarHost();
        hostsDetectados = buscadorTCP.getListaHosts();
        crearBotonesDinamicos(hostsDetectados);
    }

    private void crearBotonesDinamicos(ArrayList<String> listaHosts) {
        // Limpiar los botones existentes antes de añadir nuevos
        layoutBotones.removeAllViews();

        // Iterar sobre la lista de hosts
        for (String host : listaHosts) {
            Button botonHost = new Button(this);
            botonHost.setText(host); // Asignar el texto del host al botón
            botonHost.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Asignar un listener al botón
            botonHost.setOnClickListener(view -> {
                Toast.makeText(UnirsePartidaActivity.this, "Host seleccionado: " + host, Toast.LENGTH_SHORT).show();

                // Iniciar actividad con el host seleccionado
                Intent intent = new Intent(UnirsePartidaActivity.this, SeleccionarPersonajeActivity.class);
                intent.putExtra("host_seleccionado", host); // Pasar el host seleccionado a la siguiente actividad
                startActivity(intent);
            });

            // Añadir el botón al LinearLayout
            layoutBotones.addView(botonHost);
        }
    }
}
