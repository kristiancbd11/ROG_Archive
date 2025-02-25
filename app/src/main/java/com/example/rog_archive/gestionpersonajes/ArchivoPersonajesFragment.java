package com.example.rog_archive.gestionpersonajes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ArchivoPersonajesFragment extends Fragment {

    private LinearLayout linearLayoutPersonajes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Declaración para el uso de la base de datos
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.open();

        // Inflamos el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_archivo_personajes, container, false);

        // Inicializamos el layout para los botones dinámicos
        linearLayoutPersonajes = rootView.findViewById(R.id.linear_layout_personajes);

        // Llamar a cargar personajes
        cargarYMostrarPersonajes(rootView, dbHandler);

        return rootView;
    }

    /**
     * Carga los personajes desde el gestor de ficheros y los muestra como botones dinámicos.
     */
    private void cargarYMostrarPersonajes(View rootView, DBHandler dbHandler) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            String usuarioUid = user.getUid();
            ArrayList<Personaje> personajes = dbHandler.obtenerPersonajesPorUsuario(usuarioUid);

            if (personajes.isEmpty()) {
                Toast.makeText(getContext(), "No hay personajes para mostrar", Toast.LENGTH_SHORT).show();
                return;
            } else {
                for (Personaje personaje : personajes) {
                    // Crear un LinearLayout horizontal para cada personaje
                    LinearLayout layoutHorizontal = new LinearLayout(getContext());
                    layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    layoutHorizontal.setPadding(8, 8, 8, 8);

                    // Crear el botón del personaje
                    Button botonPersonaje = new Button(getContext());
                    String texto = personaje.getNombre() + " - " + personaje.getClase() + " | Nv " + personaje.getNivel();
                    botonPersonaje.setText(texto);

                    // Aplicar estilos
                    botonPersonaje.setTextColor(0xFFFFFFFF); // Texto blanco
                    botonPersonaje.setTextSize(8); // Tamaño de texto 8sp

                    // Aplicar la fuente personalizada
                    Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.dungeon_depths);
                    if (typeface != null) {
                        botonPersonaje.setTypeface(typeface);
                    }

                    // Aplicar el fondo personalizado con bordes redondeados y marco negro
                    botonPersonaje.setBackgroundResource(R.drawable.rounded_button);

                    // Configurar LayoutParams para que el botón ocupe todo el ancho del layout
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                            0, // Ancho proporcional
                            LinearLayout.LayoutParams.WRAP_CONTENT, // Altura ajustada al contenido
                            1.0f // Peso para distribuir el espacio
                    );
                    botonPersonaje.setLayoutParams(buttonParams);

                    botonPersonaje.setOnClickListener(v -> {
                        // Crear el PopupMenu
                        PopupMenu popupMenu = new PopupMenu(getContext(), v);
                        popupMenu.getMenu().add("Ver");
                        popupMenu.getMenu().add("Eliminar");

                        // Configurar el listener para manejar las selecciones del menú
                        popupMenu.setOnMenuItemClickListener(item -> {
                            String title = item.getTitle().toString();
                            switch (title) {
                                case "Ver":
                                    VistaPersonajeFragment fragment = VistaPersonajeFragment.newInstance(personaje);

                                    // Reemplazar el fragmento en la actividad
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                case "Eliminar":
                                    // Abrir la base de datos
                                    dbHandler.open();

                                    // Eliminar el personaje usando el método de DBHandler
                                    int filasAfectadas = dbHandler.eliminarPersonaje(usuarioUid, personaje.getNombre());

                                    // Cerrar la base de datos
                                    dbHandler.close();

                                    if (filasAfectadas > 0) {
                                        // Eliminar el botón del personaje en la UI
                                        layoutHorizontal.post(() -> {
                                            linearLayoutPersonajes.removeView(layoutHorizontal);
                                            Toast.makeText(getContext(), "Personaje eliminado: " + personaje.getNombre(), Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "Error al eliminar el personaje", Toast.LENGTH_SHORT).show();
                                    }
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
        dbHandler.close();
    }
}
