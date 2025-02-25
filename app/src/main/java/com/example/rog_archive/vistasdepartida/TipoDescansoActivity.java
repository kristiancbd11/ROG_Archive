package com.example.rog_archive.vistasdepartida;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;

import java.util.ArrayList;

public class TipoDescansoActivity extends AppCompatActivity {

    private ImageView estrella1, estrella2;
    private Button botonDescansoCorto, botonDescansoLargo;
    private Partida partida; // Agregar la variable para almacenar el objeto Partida
    private TextView descansosDisponibles, recursosDisponibles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_descanso);

        //Activar animación de la hoguera
        ImageView imageView = findViewById(R.id.hoguera);
        imageView.setBackgroundResource(R.drawable.animacion);
        AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
        animation.start();

        // Crear una instancia de DBHandler y obtener los personajes de la partida
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.open();

        String nombrePartida = (String) getIntent().getSerializableExtra("PARTIDA");
        partida = dbHandler.obtenerPartidaPorNombre(nombrePartida);

        if (partida == null) {
            // Manejar el caso donde no se recibe la partida
            Toast.makeText(this, "Error: No se recibió la partida.", Toast.LENGTH_SHORT).show();
            finish(); // Salir de la actividad si no hay partida
        }

        // Mostrar el nombre de la partida
        TextView textViewPartida = findViewById(R.id.nombrePartidaDescanso);
        textViewPartida.setText(partida.getNombre());

        ArrayList<Personaje> personajes = dbHandler.obtenerPersonajesDePartida(partida.getNombre());
        String[] nombresPersonajes = new String[personajes.size()];
        for (int i = 0; i < personajes.size(); i++) {
            nombresPersonajes[i] = personajes.get(i).getNombre();
        }

        // Mostrar información de la partida
        descansosDisponibles = findViewById(R.id.descansosDisponibles);
        recursosDisponibles = findViewById(R.id.recursosDisponibles);
        descansosDisponibles.setText(String.valueOf(partida.getDescansos()));
        recursosDisponibles.setText(String.valueOf(partida.getRecursos()));

        // Referencias a las estrellas
        estrella1 = findViewById(R.id.estrella1);
        estrella2 = findViewById(R.id.estrella2);

        // Configurar la visibilidad de las estrellas según los descansos de la partida
        if (partida.getDescansos() >= 1) {
            estrella1.setVisibility(View.VISIBLE);
        } else {
            estrella1.setVisibility(View.INVISIBLE);
        }

        if (partida.getDescansos() == 2) {
            estrella2.setVisibility(View.VISIBLE);
        } else {
            estrella2.setVisibility(View.INVISIBLE);
        }

        // Referencias a los botones
        botonDescansoCorto = findViewById(R.id.botonsDescansoCorto);
        botonDescansoLargo = findViewById(R.id.botonsDescansoLargo);

        // Evento de clic para el botón de descanso corto
        // Evento de clic para el botón de descanso corto
        // Evento de clic para el botón de descanso corto
        botonDescansoCorto.setOnClickListener(view -> {
            // Comprobar si hay descansos disponibles
            if (partida.getDescansos() == 0) {
                Toast.makeText(TipoDescansoActivity.this, "¡No hay tiempo para más descansos!", Toast.LENGTH_SHORT).show();
                return; // Salir del método sin continuar
            }

            // Crear el array de selección múltiple con la lista de personajes
            boolean[] seleccionados = new boolean[nombresPersonajes.length];

            // Mostrar el selector con casillas de verificación
            new AlertDialog.Builder(TipoDescansoActivity.this)
                    .setTitle("Selecciona los personajes para descanso corto")
                    .setMultiChoiceItems(nombresPersonajes, seleccionados, (dialog, which, isChecked) -> {
                        seleccionados[which] = isChecked;
                    })
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        // Recuperar la lista de personajes desde la base de datos
                        ArrayList<Personaje> personajesSeleccionados = new ArrayList<>();
                        for (int i = 0; i < nombresPersonajes.length; i++) {
                            if (seleccionados[i]) {
                                for (Personaje personaje : personajes) {
                                    if (personaje.getNombre().equals(nombresPersonajes[i])) {
                                        personajesSeleccionados.add(personaje);
                                        break;
                                    }
                                }
                            }
                        }

                        // Aplicar curación a los personajes seleccionados
                        for (Personaje personaje : personajesSeleccionados) {
                            int curacion = calcularCuracionCorta(personaje);
                            dbHandler.modificarSaludDePersonaje(personaje.getNombre(), curacion, true);
                        }

                        // Restar un descanso disponible a la partida
                        dbHandler.restarDescanso(partida.getNombre());

                        // Actualizar el objeto partida localmente
                        partida.setDescansos(partida.getDescansos() - 1);

                        // Actualizar las estrellas en la interfaz
                        actualizarInterfaz();

                        // Mostrar un mensaje de éxito
                        Toast.makeText(TipoDescansoActivity.this, "Descanso corto realizado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Evento de clic para el botón de descanso largo
        // Evento de clic para el botón de descanso largo
        botonDescansoLargo.setOnClickListener(view -> {
            boolean[] seleccionados = new boolean[nombresPersonajes.length];

            new AlertDialog.Builder(TipoDescansoActivity.this)
                    .setTitle("Selecciona los personajes para descanso largo")
                    .setMultiChoiceItems(nombresPersonajes, seleccionados, (dialog, which, isChecked) -> {
                        seleccionados[which] = isChecked;
                    })
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ArrayList<Personaje> personajesSeleccionados = new ArrayList<>();
                        for (int i = 0; i < nombresPersonajes.length; i++) {
                            if (seleccionados[i]) {
                                for (Personaje personaje : personajes) {
                                    if (personaje.getNombre().equals(nombresPersonajes[i])) {
                                        personajesSeleccionados.add(personaje);
                                        break;
                                    }
                                }
                            }
                        }
                        // Restablecer descansos a 2 en la base de datos y en la partida
                        if (dbHandler.reponerDescansos(partida.getNombre())) {
                            // Aplicar descanso largo: restaurar salud al máximo
                            for (Personaje personaje : personajesSeleccionados) {
                                int curacion = calcularCuracionLarga(personaje);
                                dbHandler.modificarSaludDePersonaje(personaje.getNombre(), curacion, true);
                            }

                            partida.setDescansos(2);
                            partida.setRecursos(partida.getRecursos() - 25);
                            // Actualizar las estrellas en la interfaz
                            actualizarInterfaz();

                            // Mostrar un mensaje de éxito
                            Toast.makeText(TipoDescansoActivity.this, "Todos los personajes han sido completamente curados", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TipoDescansoActivity.this, "Recursos insuficientes para pasar la noche a salvo", Toast.LENGTH_SHORT).show();

                        }
                        })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }

        private void actualizarInterfaz() {
            descansosDisponibles.setText(String.valueOf(partida.getDescansos()));
            recursosDisponibles.setText(String.valueOf(partida.getRecursos()));

            if (partida.getDescansos() >= 1) {
                estrella1.setVisibility(View.VISIBLE);
            } else {
                estrella1.setVisibility(View.INVISIBLE);
            }

            if (partida.getDescansos() == 2) {
                estrella2.setVisibility(View.VISIBLE);
            } else {
                estrella2.setVisibility(View.INVISIBLE);
            }
        }


        public int calcularCuracionCorta(Personaje personaje) {
        int curacion = 0;
        if(personaje.getSalud() < personaje.getEstadisticas().get(3)) {
            if(personaje.getSalud() <= personaje.getEstadisticas().get(3)/2) {
                curacion = personaje.getEstadisticas().get(3)/2;
            } else {
                curacion = personaje.getEstadisticas().get(3) - personaje.getSalud();
            }
        }

        return curacion; // Retorna la cantidad curada
    }

    public int calcularCuracionLarga(Personaje personaje) {
        int curacion = 0;
        if(personaje.getSalud() < personaje.getEstadisticas().get(3)) {
            curacion = personaje.getEstadisticas().get(3) - personaje.getSalud();
        }

        return curacion; // Retorna la cantidad curada
    }
}
