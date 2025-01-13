package com.example.rog_archive.gestionpartidas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rog_archive.MenuMasterActivity;
import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.gestionficheros.GestorFicheros;

import java.util.ArrayList;
import java.util.Random;

public class CargarPartidaFragment extends Fragment {

    private GestorFicheros gestorFicheros; // Declarar el gestor de ficheros

    public CargarPartidaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cargar_partida, container, false);

        // Inicializar el gestor de ficheros
        gestorFicheros = new GestorFicheros();
        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_recuadros);

        // Cargar las partidas usando el método del GestorFicheros
        ArrayList<Partida> listaPartidas = gestorFicheros.cargarPartidas(getContext());

        if (listaPartidas != null && !listaPartidas.isEmpty()) {
            for (Partida partida : listaPartidas) {
                String linea = partida.getNombre() + "_" + partida.getNJugadores();
                crearRectangulo(linearLayout, linea);
            }
        } else {
            Log.e("CargarPartidaFragment", "No se encontraron partidas para cargar.");
        }

        // Configurar el botón "Atrás"
        ImageView atrasImage = view.findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(v -> {
            requireActivity().onBackPressed(); // Simular el botón "Atrás" de la actividad
        });

        Button botonNuevaPartida = view.findViewById(R.id.botonNuevaPartida);
        botonNuevaPartida.setOnClickListener(v -> {
            // Crear una instancia del fragmento
            NuevaPartidaFragment nuevaPartidaFragment = new NuevaPartidaFragment();

            // Reemplazar el fragmento actual con el nuevo fragmento
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, nuevaPartidaFragment) // fragment_container es el contenedor donde se colocan los fragmentos
                    .addToBackStack(null) // Permitir regresar al fragmento anterior
                    .commit();
        });


        return view;
    }

    private void crearRectangulo(LinearLayout linearLayout, String linea) {
        Log.d("CrearRectangulo", "Añadiendo partida con línea: " + linea);

        // Separar "Nombre Partida" y "número de jugadores"
        int separadorIndex = linea.lastIndexOf('_');
        if (separadorIndex == -1 || separadorIndex >= linea.length() - 1) {
            Log.e("CrearRectangulo", "Línea mal formateada: " + linea);
            return;
        }

        String nombrePartida = linea.substring(0, separadorIndex).trim();
        String numeroJugadores = linea.substring(separadorIndex + 1).trim();

        TextView textView = new TextView(getContext());
        textView.setText(nombrePartida + " | Jugadores: " + numeroJugadores);
        textView.setPadding(50, 50, 50, 50);
        textView.setBackgroundResource(android.R.color.holo_blue_light);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setClickable(true);

        // Configurar el click en el rectángulo
        textView.setOnClickListener(v -> {

            String codigoGenerado = generarCodigo();
            Intent intent = new Intent(getActivity(), MenuMasterActivity.class);
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