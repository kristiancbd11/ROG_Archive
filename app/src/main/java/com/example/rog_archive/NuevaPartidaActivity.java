package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NuevaPartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_partida);

        Spinner spinner = findViewById(R.id.spinnerNumeroJugadores);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getNumeros());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText editTextNombrePartida = findViewById(R.id.editTextNombrePartida);
        Button botonCrearNuevaPartida = findViewById(R.id.botonCrearNuevaPartida);

        botonCrearNuevaPartida.setOnClickListener(view -> {
            String nombrePartida = editTextNombrePartida.getText().toString().trim();
            int numeroJugadores = (int) spinner.getSelectedItem();

            if (nombrePartida.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre de partida.", Toast.LENGTH_SHORT).show();
            } else {
                guardarPartida(nombrePartida, numeroJugadores);
                Toast.makeText(this, "Partida creada exitosamente.", Toast.LENGTH_SHORT).show();
                editTextNombrePartida.setText(""); // Limpiar campo de texto
                spinner.setSelection(0); // Resetear spinner
            }
        });

        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(view -> {
            Intent intent = new Intent(NuevaPartidaActivity.this, CrearPartidaActivity.class);
            startActivity(intent);
        });
    }

    private Integer[] getNumeros() {
        Integer[] numeros = new Integer[10];
        for (int i = 0; i < 10; i++) {
            numeros[i] = i + 1;
        }
        return numeros;
    }

    private void guardarPartida(String nombrePartida, int numeroJugadores) {
        String partidaFormato = nombrePartida + "_" + numeroJugadores + "\n";

        File archivo = new File(getFilesDir(), "partidas.txt");

        try {
            // Crear el archivo si no existe
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            try (FileOutputStream fos = new FileOutputStream(archivo, true)) { // true para agregar sin sobrescribir
                fos.write(partidaFormato.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la partida.", Toast.LENGTH_SHORT).show();
        }
    }
}
