package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class CargarPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_partida);

        LinearLayout linearLayout = findViewById(R.id.linear_layout_recuadros);

        // Leer el archivo desde almacenamiento interno
        File archivoInterno = new File(getFilesDir(), "partidas.txt");

        if (archivoInterno.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(archivoInterno);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {

                String linea;
                while ((linea = reader.readLine()) != null) {
                    crearRectangulo(linearLayout, linea);
                }

            } catch (IOException e) {
                Log.e("CargarPartidaActivity", "Error al leer el archivo desde almacenamiento interno", e);
            }
        } else {
            Log.e("CargarPartidaActivity", "El archivo partidas.txt no existe en el almacenamiento interno");
        }

        // Configurar el botón "Atrás"
        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargarPartidaActivity.this, CrearPartidaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void crearRectangulo(LinearLayout linearLayout, String linea) {
        // Separar "Nombre Partida" y "número de jugadores"
        int separadorIndex = linea.lastIndexOf('_');
        if (separadorIndex == -1 || separadorIndex >= linea.length() - 1) return;

        String nombrePartida = linea.substring(0, separadorIndex).trim();
        String numeroJugadores = linea.substring(separadorIndex + 1).trim();

        TextView textView = new TextView(this);
        textView.setText(nombrePartida + " | Jugadores: " + numeroJugadores);
        textView.setPadding(50, 50, 50, 50);
        textView.setBackgroundResource(android.R.color.holo_blue_light);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setClickable(true);

        // Configurar el click en el rectángulo
        textView.setOnClickListener(v -> {
            // Generar el código
            String codigoGenerado = generarCodigo();

            // Crear el Intent para ir a la siguiente actividad
            Intent intent = new Intent(CargarPartidaActivity.this, MenuMasterActivity.class);
            // Pasar el código generado al MenuMasterActivity
            intent.putExtra("CODIGO_GENERADO", codigoGenerado);
            startActivity(intent);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 20, 20, 20);
        textView.setLayoutParams(params);

        linearLayout.addView(textView);
    }

    private String generarCodigo() {
        String codigo = "";
        String alfa = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rd = new Random();
        for (int i = 0; i < 4; i++) {
            codigo += alfa.charAt(rd.nextInt(26));  // Uso de charAt para seleccionar caracteres individuales
        }
        codigo += "-" + rd.nextInt(10) + rd.nextInt(10); // Añadir dos dígitos aleatorios
        return codigo;
    }
}
