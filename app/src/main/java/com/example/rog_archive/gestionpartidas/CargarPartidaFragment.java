package com.example.rog_archive.gestionpartidas;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.rog_archive.vistasdepartida.MenuPartidaActivity;
import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.gestordb.DBHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

public class CargarPartidaFragment extends Fragment {

    private LinearLayout linearLayoutPartidas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cargar_partida, container, false);

        // Inicializar DBHandler
        DBHandler dbHandler = new DBHandler(requireContext());

        // Obtener el usuario actualmente autenticado
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual == null) {
            Toast.makeText(getContext(), "Error: No hay sesión iniciada.", Toast.LENGTH_SHORT).show();
            return view;
        }

        String usuarioUid = usuarioActual.getUid();
        Log.d("CargarPartidaFragment", "Usuario actual: " + usuarioUid);

        linearLayoutPartidas = view.findViewById(R.id.linear_layout_recuadros);

        // Cargar partidas desde la base de datos
        dbHandler.open();
        ArrayList<Partida> listaPartidas = dbHandler.obtenerPartidasPorUsuario(usuarioUid);
        dbHandler.close();

        if (listaPartidas != null && !listaPartidas.isEmpty()) {
            for (Partida partida : listaPartidas) {
                crearBotonPartida(view, dbHandler, partida);
            }
        } else {
            Toast.makeText(getContext(), "No se encontraron partidas guardadas.", Toast.LENGTH_SHORT).show();
        }

        // Configurar el botón para crear una nueva partida
        ImageButton botonNuevaPartida = view.findViewById(R.id.botonNuevaPartida);
        botonNuevaPartida.setOnClickListener(v -> {
            NuevaPartidaFragment nuevaPartidaFragment = new NuevaPartidaFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, nuevaPartidaFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void crearBotonPartida(View rootView, DBHandler dbHandler, Partida partida) {
        // Crear un LinearLayout horizontal para cada partida
        LinearLayout layoutHorizontal = new LinearLayout(getContext());
        layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        layoutHorizontal.setPadding(8, 8, 8, 8);

        // Extraer datos de la partida
        String nombrePartida = partida.getNombre();
        String numeroJugadores = String.valueOf(partida.getNJugadores());
        String textoBoton = nombrePartida + " | Jugadores: " + numeroJugadores;

        // Crear botón
        Button botonPartida = new Button(getContext());
        botonPartida.setText(textoBoton);
        botonPartida.setTextColor(0xFFFFFFFF); // Texto blanco
        botonPartida.setTextSize(8);

        // Aplicar la fuente personalizada
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.dungeon_depths);
        if (typeface != null) {
            botonPartida.setTypeface(typeface);
        }

        // Aplicar el fondo personalizado
        botonPartida.setBackgroundResource(R.drawable.rounded_button2);

        // Configurar LayoutParams
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, // Ancho proporcional
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f // Peso para distribuir el espacio
        );
        botonPartida.setLayoutParams(buttonParams);

        botonPartida.setOnClickListener(v -> {
            // Crear el PopupMenu
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenu().add("Jugar");
            popupMenu.getMenu().add("Eliminar");

            // Configurar el listener para manejar las selecciones del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                switch (title) {
                    case "Jugar":
                        // Enviar el objeto Partida al MenuMasterActivity
                        Intent intent = new Intent(getActivity(), MenuPartidaActivity.class);
                        intent.putExtra("PARTIDA", partida); // Enviar la partida
                        startActivity(intent);
                        break;
                    case "Eliminar":
                        // Eliminar la partida de la base de datos
                        //dbHandler.eliminarPartida(partida.getId());

                        // Recargar la lista de partidas
                        linearLayoutPartidas.removeAllViews();
                        dbHandler.open();
                        ArrayList<Partida> listaPartidas = dbHandler.obtenerPartidasPorUsuario(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dbHandler.eliminarPartida(FirebaseAuth.getInstance().getCurrentUser().getUid(),partida.getNombre());
                        dbHandler.close();

                        refreshPartidas(rootView, dbHandler);

                        Toast.makeText(getContext(), "Partida eliminada: " + partida.getNombre(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            });

            // Mostrar el menú
            popupMenu.show();
        });

        // Añadir los elementos al layout horizontal
        layoutHorizontal.addView(botonPartida);

        // Añadir el layout horizontal al LinearLayout principal
        linearLayoutPartidas.addView(layoutHorizontal);
    }

    private void refreshPartidas(View rootView, DBHandler dbHandler) {
        // Limpiar el LinearLayout antes de recargar las partidas
        linearLayoutPartidas.removeAllViews();

        // Obtener el usuario actual
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual == null) {
            Toast.makeText(getContext(), "Error: No hay sesión iniciada.", Toast.LENGTH_SHORT).show();
            return;
        }

        String usuarioUid = usuarioActual.getUid();
        Log.d("CargarPartidaFragment", "Usuario actual: " + usuarioUid);

        // Cargar partidas desde la base de datos
        dbHandler.open();
        ArrayList<Partida> listaPartidas = dbHandler.obtenerPartidasPorUsuario(usuarioUid);
        dbHandler.close();

        if (listaPartidas != null && !listaPartidas.isEmpty()) {
            for (Partida partida : listaPartidas) {
                crearBotonPartida(rootView, dbHandler, partida); // Crear un botón por cada partida
            }
        } else {
            Toast.makeText(getContext(), "No se encontraron partidas guardadas.", Toast.LENGTH_SHORT).show();
        }
    }
}