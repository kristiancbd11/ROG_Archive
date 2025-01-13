package com.example.rog_archive.gestionpersonajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestionficheros.GestorFicheros;

import java.util.ArrayList;

public class ArchivoPersonajesFragment extends Fragment {

    private LinearLayout linearLayoutPersonajes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_archivo_personajes, container, false);

        // Inicializamos el layout para los botones dinámicos
        linearLayoutPersonajes = rootView.findViewById(R.id.linear_layout_personajes);

        // Llamar a cargar personajes
        cargarYMostrarPersonajes(rootView);

        // Botón para regresar al menú anterior (si es necesario)
        ImageView atrasImage = rootView.findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(view -> getActivity().onBackPressed());

        // Mostrar aviso "Personaje subido con éxito"
        ImageView flechaSubida = rootView.findViewById(R.id.imageViewFlechaSubida);
        flechaSubida.setOnClickListener(view ->
                Toast.makeText(getContext(), "Personaje subido con éxito", Toast.LENGTH_SHORT).show()
        );

        // Mostrar aviso "Personajes descargados"
        Button botonDescarga = rootView.findViewById(R.id.botonDescarga);
        botonDescarga.setOnClickListener(view ->
                Toast.makeText(getContext(), "Personajes descargados", Toast.LENGTH_SHORT).show()
        );

        return rootView;
    }

    /**
     * Carga los personajes desde el gestor de ficheros y los muestra como botones dinámicos.
     */
    private void cargarYMostrarPersonajes(View rootView) {
        GestorFicheros gestorFicheros = new GestorFicheros();
        ArrayList<Personaje> personajes = gestorFicheros.cargarPersonajes(getContext());

        if (personajes.isEmpty()) {
            Toast.makeText(getContext(), "No hay personajes para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Personaje personaje : personajes) {
            // Crear un LinearLayout horizontal para cada personaje
            LinearLayout layoutHorizontal = new LinearLayout(getContext());
            layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            layoutHorizontal.setPadding(8, 8, 8, 8);

            // Crear el botón del personaje
            Button botonPersonaje = new Button(getContext());
            String texto = personaje.getNombre() + " - " + personaje.getClase() + " | Nv " + personaje.getNivel();
            botonPersonaje.setText(texto);

            // Configurar LayoutParams para que el botón ocupe todo el ancho del layout
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0, // El ancho será calculado proporcionalmente
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Altura ajustada al contenido
                    1.0f // Peso para distribuir el espacio
            );
            botonPersonaje.setLayoutParams(buttonParams);

            botonPersonaje.setOnClickListener(v -> {
                // Crear el PopupMenu
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                // Inflar el menú con opciones
                popupMenu.getMenu().add("Ver");
                popupMenu.getMenu().add("Eliminar");

                // Configurar el listener para manejar las selecciones del menú
                popupMenu.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle().toString();
                    switch (title) {
                        case "Ver":
                            // Acción para "Ver"
                            VistaPersonajeFragment fragment = VistaPersonajeFragment.newInstance(personaje);

                            // Reemplazamos el fragmento de la actividad
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment) // Reemplazamos el contenido actual con el fragmento
                                    .addToBackStack(null) // Agregamos el fragmento a la pila de retroceso para poder volver
                                    .commit();
                            break;
                        case "Eliminar":
                            // Acción para "Eliminar"
                            GestorFicheros gestorFicheros1 = new GestorFicheros();
                            gestorFicheros1.eliminarPersonaje(getContext(), personaje.getNombre());

                            // Recargar la lista de personajes después de eliminar
                            linearLayoutPersonajes.removeAllViews();
                            cargarYMostrarPersonajes(rootView);

                            Toast.makeText(getContext(), "Personaje eliminado: " + personaje.getNombre(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                });

                // Mostrar el menú
                popupMenu.show();
            });

            // Añadir los elementos al layout horizontal
            layoutHorizontal.addView(botonPersonaje);

            // Añadir el layout horizontal al LinearLayout principal
            linearLayoutPersonajes.addView(layoutHorizontal);
        }
    }
}