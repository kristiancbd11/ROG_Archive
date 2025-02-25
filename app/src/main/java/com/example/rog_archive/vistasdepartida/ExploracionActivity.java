package com.example.rog_archive.vistasdepartida;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;

import java.util.List;
import java.util.Random;

public class ExploracionActivity extends AppCompatActivity {

    private Partida partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploracion);

        DBHandler dbHandler = new DBHandler(this);
        dbHandler.open();

        // Recuperamos el nombre de la partida desde el Intent
        String nombrePartida = (String) getIntent().getSerializableExtra("PARTIDA");
        partida = dbHandler.obtenerPartidaPorNombre(nombrePartida);

        if (partida == null) {
            Toast.makeText(this, "Error: No se recibió la partida", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Recuperamos los personajes asociados a la partida
        List<Personaje> personajes = dbHandler.obtenerPersonajesDePartida(partida.getNombre());

        // Configuración del spinner para tipo de desafío
        String[] tipDesafio = {"Fuerza", "Constitución", "Destreza", "Inteligencia", "Carisma", "Sabiduría"};
        Spinner spinnerTipoDesafio = findViewById(R.id.spinnerTipoDesafio);
        ArrayAdapter<String> adapterDesafio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipDesafio);
        adapterDesafio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDesafio.setAdapter(adapterDesafio);

        // Spinner para personajes
        Spinner spinnerPersonajes = findViewById(R.id.spinnerPersonajes); // Añadir este Spinner en el XML

        // Crear un ArrayAdapter personalizado para mostrar solo el nombre en el Spinner
        ArrayAdapter<Personaje> adapterPersonajes = new ArrayAdapter<Personaje>(this, android.R.layout.simple_spinner_item, personajes) {

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Mostrar solo el nombre en el dropdown
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).getNombre());
                return view;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                // Mostrar solo el nombre en el Spinner
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).getNombre());
                return view;
            }
        };

        adapterPersonajes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersonajes.setAdapter(adapterPersonajes);

        // Declaración de los edit text
        EditText dificultad = findViewById(R.id.dificultad);
        EditText cantidadRecursos = findViewById(R.id.cantidadRecursos);

        TextView numDado = findViewById(R.id.numDado);

        numDado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int resultadoPrueba = generarNum();
                numDado.setText(String.valueOf(resultadoPrueba));
                if(dificultad.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ExploracionActivity.this, "Ingresa la dificultad del desafío", Toast.LENGTH_SHORT).show();
                    return;
                }
                int valorDificultad = Integer.parseInt(dificultad.getText().toString());
                int modificador = 0;
                String desafio = spinnerTipoDesafio.getSelectedItem().toString();

                Personaje personajeSeleccionado = (Personaje) spinnerPersonajes.getSelectedItem();

                if(desafio.equals("Fuerza")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(1);
                    modificador = estadistica / 2;

                } else if(desafio.equals("Constitución")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(3);
                    modificador = estadistica / 2;

                } else if(desafio.equals("Destreza")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(2);
                    modificador = estadistica / 2;

                } else if(desafio.equals("Inteligencia")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(4);
                    modificador = estadistica / 2;

                } else if(desafio.equals("Carisma")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(5);
                    modificador = estadistica / 2;

                } else if(desafio.equals("Sabiduría")) {
                    int estadistica = personajeSeleccionado.getEstadisticas().get(6);
                    modificador = estadistica / 2;
                }

                if (resultadoPrueba + modificador > valorDificultad) {
                    // Si la prueba es superada
                    Toast.makeText(ExploracionActivity.this, "PRUEBA SUPERADA", Toast.LENGTH_SHORT).show();
                } else {
                    // Si la prueba falla
                    Toast.makeText(ExploracionActivity.this, "Prueba fallida...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Funcionalidad del boton de recursos
        ImageButton botonObtenerRecursos = findViewById(R.id.botonObtenerRecursos);
        botonObtenerRecursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int recursos = Integer.parseInt(cantidadRecursos.getText().toString());
                partida.setRecursos(partida.getRecursos() + recursos);
                dbHandler.aniadirRecursos(partida.getNombre(), recursos);
            }
        });
    }

    public int generarNum() {
        Random r = new Random();
        return r.nextInt(20) + 1;
    }
}
