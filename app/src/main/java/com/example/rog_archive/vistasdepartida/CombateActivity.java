package com.example.rog_archive.vistasdepartida;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestionpersonajes.VistaPersonajeFragment;
import com.example.rog_archive.gestordb.DBHandler;

import java.util.ArrayList;

public class CombateActivity extends AppCompatActivity {

    private Partida partida;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combate);

        partida = (Partida) getIntent().getSerializableExtra("PARTIDA");

        if (partida == null) {
            Toast.makeText(this, "Error: No se recibió la partida", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHandler = new DBHandler(this);
        dbHandler.open();

        cargarYMostrarPersonajes(partida);
    }

    private void mostrarDialogoSalir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir de la partida")
                .setMessage("¿Estás seguro de que quieres abandonar la partida?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(this, MenuPartidaActivity.class);
                    intent.putExtra("PARTIDA", partida);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Volver a cargar y mostrar los personajes actualizados
        cargarYMostrarPersonajes(partida);
    }

    private void cargarYMostrarPersonajes(Partida partida) {
        LinearLayout linearLayoutPersonajes = findViewById(R.id.linear_layout_recuadros2);

        if (linearLayoutPersonajes == null) {
            Log.e("CombateActivity", "Error: LinearLayout no encontrado en el layout");
            return;
        }

        ArrayList<Personaje> personajes = dbHandler.obtenerPersonajesDePartida(partida.getNombre());

        if (personajes == null || personajes.isEmpty()) {
            Toast.makeText(this, "No hay personajes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        // Limpiar cualquier vista anterior antes de agregar los nuevos botones
        linearLayoutPersonajes.removeAllViews();

        for (Personaje personaje : personajes) {
            Button botonPersonaje = new Button(this);
            botonPersonaje.setText(personaje.getNombre() + " || Salud: " + personaje.getSalud());
            botonPersonaje.setTextColor(0xFFFFFFFF);
            botonPersonaje.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

            // Aplicar la fuente personalizada
            Typeface typeface = ResourcesCompat.getFont(this, R.font.dungeon_depths);
            if (typeface != null) {
                botonPersonaje.setTypeface(typeface);
            }

            // Reducción del padding
            botonPersonaje.setPadding(10, 5, 10, 5);  // Ajusta estos valores según lo necesites

            // Verificar si la salud es 0 para asignar el fondo correspondiente
            if (personaje.getSalud() == 0) {
                botonPersonaje.setBackgroundResource(R.drawable.rounded_button_red);  // Fondo rojo si la salud es 0
            } else {
                botonPersonaje.setBackgroundResource(R.drawable.rounded_button2);  // Fondo normal si tiene salud
            }

            // Crear un objeto LayoutParams para establecer márgenes entre los botones
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho del botón
                    LinearLayout.LayoutParams.WRAP_CONTENT   // Alto del botón
            );

            // Establecer márgenes (espacio entre los botones)
            params.setMargins(0, 10, 0, 10);  // Margen superior e inferior de 10dp entre los botones
            botonPersonaje.setLayoutParams(params);

            botonPersonaje.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(this, v);
                popupMenu.getMenu().add("Movimiento");
                popupMenu.getMenu().add("Ataque");
                popupMenu.getMenu().add("Información"); // Añadir la opción "Información"

                popupMenu.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle().toString();
                    if ("Movimiento".equals(title)) {
                        String mensaje = "Este personaje puede moverse " + personaje.getEstadisticas().get(0) + " cm.\n"
                                + "o realizar una carga de " + personaje.getEstadisticas().get(0) + " + 2d6 cm.";
                        MovimientoDialogFragment dialog = MovimientoDialogFragment.newInstance();
                        dialog.setMensaje(mensaje);
                        dialog.show(getSupportFragmentManager(), "MovimientoDialog");
                    } else if ("Ataque".equals(title)) {
                        if (partida != null) {
                            if (findViewById(R.id.fragment_container) == null) {
                                Log.e("CombateActivity", "Error: fragment_container no encontrado en el layout");
                                return false;
                            }

                            Intent intent = new Intent(this, RealizarAtaqueActivity.class);
                            intent.putExtra("PARTIDA", partida);
                            intent.putExtra("ATACANTE", personaje); // Enviar el personaje atacante
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Error: No se pudo cargar la partida", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("Información".equals(title)) {  // Cuando se selecciona "Información"
                        // Crear el fragmento VistaPersonajeFragment y pasar el personaje
                        VistaPersonajeFragment vistaPersonajeFragment = VistaPersonajeFragment.newInstance(personaje);

                        // Reemplazar el fragmento actual con VistaPersonajeFragment
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, vistaPersonajeFragment)
                                .addToBackStack(null) // Agregar a la pila de retroceso
                                .commit();
                    }
                    return true;
                });

                popupMenu.show();
            });

            linearLayoutPersonajes.addView(botonPersonaje);
        }
    }
}
