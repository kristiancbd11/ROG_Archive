package com.example.rog_archive.gestionpartidas;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NuevaPartidaFragment extends Fragment {

    private DBHandler dbHandler;
    private LinearLayout linearLayoutPersonajes;

    public NuevaPartidaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nueva_partida, container, false);

        // Lista de personajes seleccionados
        ArrayList<Personaje> personajesSeleccionados = new ArrayList<Personaje>();

        // Inicializar DBHandler
        dbHandler = new DBHandler(requireContext());

        // Inicializar el LinearLayout para los personajes
        linearLayoutPersonajes = rootView.findViewById(R.id.linear_layout_recuadros2);

        // Configurar Spinner
        Spinner spinner = rootView.findViewById(R.id.spinnerNumeroJugadores);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, getNumeros());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Referencia al campo de texto para el nombre de la partida
        EditText editTextNombrePartida = rootView.findViewById(R.id.editTextNombrePartida);

        // Llamar a cargar los personajes
        cargarYMostrarPersonajes(rootView, personajesSeleccionados);

        // Creación del botón para generar la nueva partida y su funcionalidad
        Button botonCrearNuevaPartida = rootView.findViewById(R.id.botonCrearNuevaPartida);
        botonCrearNuevaPartida.setOnClickListener(view -> {
            String nombrePartida = editTextNombrePartida.getText().toString().trim();
            int numeroJugadores = (int) spinner.getSelectedItem();

            if (nombrePartida.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, ingrese un nombre de partida.", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String usuarioUid = user.getUid();
                    Partida partida = new Partida(nombrePartida, numeroJugadores, usuarioUid);

                    dbHandler.open();
                    long partidaId = dbHandler.insertarPartida(partida);

                    if (partidaId != -1) {
                        // Guardar la relación entre partida y personajes en la base de datos
                        for (Personaje personaje : personajesSeleccionados) {
                            dbHandler.insertarPersonajeEnPartida(partida.getNombre(), personaje.getNombre());
                        }

                        Toast.makeText(getContext(), "Partida guardada correctamente.", Toast.LENGTH_SHORT).show();
                        editTextNombrePartida.setText(""); // Limpiar campo de texto
                        spinner.setSelection(0); // Resetear spinner
                        personajesSeleccionados.clear(); // Limpiar la selección
                    } else {
                        Toast.makeText(getContext(), "Error al guardar la partida.", Toast.LENGTH_SHORT).show();
                    }
                    dbHandler.close();
                }
            }
        });

        return rootView;
    }

    // Método para obtener los números del 1 al 10
    private Integer[] getNumeros() {
        Integer[] numeros = new Integer[10];
        for (int i = 0; i < 10; i++) {
            numeros[i] = i + 1;
        }
        return numeros;
    }

    /**
     * Carga los personajes desde la base de datos y los muestra como botones dinámicos.
     */
    private void cargarYMostrarPersonajes(View rootView, ArrayList<Personaje> personajesSeleccionados) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            String usuarioUid = user.getUid();

            // Obtener la lista de partidas del usuario
            dbHandler.open();
            ArrayList<Personaje> personajes = dbHandler.obtenerPersonajesPorUsuario(usuarioUid);
            dbHandler.close();


                if (personajes.isEmpty()) {
                    Toast.makeText(getContext(), "No hay personajes para mostrar", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    for (Personaje personaje : personajes) {
                        // Crear el botón para cada personaje
                        Button botonPersonaje = new Button(getContext());
                        String texto = personaje.getNombre() + " - " + personaje.getClase() + " | Nv " + personaje.getNivel();
                        botonPersonaje.setText(texto);

                        // Aplicar estilos
                        botonPersonaje.setTextColor(0xFFFFFFFF); // Texto blanco
                        botonPersonaje.setTextSize(8); // Tamaño de texto 8sp

                        // Establecer el fondo del botón
                        botonPersonaje.setBackgroundResource(R.drawable.rounded_button2);

                        // Establecer un comportamiento cuando el botón es presionado
                        botonPersonaje.setOnClickListener(v -> {

                            if (personajesSeleccionados.contains(personaje)) {
                                // Si el personaje ya está en la lista, eliminarlo
                                botonPersonaje.setBackgroundResource(R.drawable.rounded_button2);
                                personajesSeleccionados.remove(personaje);
                                botonPersonaje.setBackgroundResource(R.drawable.rounded_button2);
                            } else {
                                // Si el personaje no está en la lista, agregarlo
                                botonPersonaje.setBackgroundResource(R.drawable.rounded_button_selected);
                                personajesSeleccionados.add(personaje);
                                botonPersonaje.setBackgroundResource(R.drawable.rounded_button_selected);
                            }
                        });

                        // Añadir el botón al LinearLayout
                        linearLayoutPersonajes.addView(botonPersonaje);
                    }
                }

        }
    }
}
