package com.example.rog_archive.vistasdepartida;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Arma;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RealizarAtaqueActivity extends AppCompatActivity {

    private Partida partida;
    private DBHandler dbHandler;
    private Personaje atacante; // Personaje que realiza el ataque
    private Spinner spinnerArmas;
    private List<Arma> listaArmas; // Lista de armas del atacante
    private Arma armaSeleccionada; // Referencia al arma seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_ataque);
        TextView alertaAtaque = findViewById(R.id.alertaAtaque);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        partida = (Partida) getIntent().getSerializableExtra("PARTIDA");
        atacante = (Personaje) getIntent().getSerializableExtra("ATACANTE");

        if (partida == null || atacante == null) {
            Toast.makeText(this, "Error: Datos insuficientes", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        TextView nombrePartida = findViewById(R.id.nombrePartida);
        nombrePartida.setText(partida.getNombre());

        spinnerArmas = findViewById(R.id.spinnerArmas);

        dbHandler = new DBHandler(this);
        dbHandler.open();
        cargarYMostrarPersonajes(alertaAtaque);
        cargarArmasEnSpinner(alertaAtaque); // Llamamos a la función para poblar el spinner
    }

    private void cargarArmasEnSpinner(TextView alertaAtaque) {
        listaArmas = dbHandler.obtenerArmasPorPersonaje(atacante.getNombre());

        if (listaArmas == null || listaArmas.isEmpty()) {
            Toast.makeText(this, "No hay armas disponibles para este personaje", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> listaArmasTexto = new ArrayList<>();
        for (Arma arma : listaArmas) {
            String armaTexto = arma.getNombre() + "\n|| AL " + arma.getAlcance() + " || A " + arma.getAtaques() +
                    " || PR " + arma.getPrecision() + "\n|| F " + arma.getFuerza() + " || PF " + arma.getPerforacion() +
                    " || D " + arma.getDanio();
            listaArmasTexto.add(armaTexto);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaArmasTexto);
        spinnerArmas.setAdapter(adapter);

        // Asignar el primer arma por defecto
        if (!listaArmas.isEmpty()) {
            armaSeleccionada = listaArmas.get(0);
        }

        spinnerArmas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                armaSeleccionada = listaArmas.get(position); // Guardamos el arma seleccionada
                String mensaje = "Su enemigo debe estar a " + armaSeleccionada.getAlcance() + " o menos centimetros";
                alertaAtaque.setText(mensaje);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarYMostrarPersonajes(TextView alertaAtaque) {
        LinearLayout linearLayoutPersonajes = findViewById(R.id.linear_layout_personajes);
        ArrayList<Personaje> personajes = dbHandler.obtenerPersonajesDePartida(partida.getNombre());

        if (personajes == null || personajes.isEmpty()) {
            Toast.makeText(this, "No hay personajes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        linearLayoutPersonajes.removeAllViews(); // Limpiamos el layout antes de agregar los nuevos botones.

        for (Personaje personaje : personajes) {
            if (!personaje.getNombre().equals(atacante.getNombre())) {
                Button botonPersonaje = new Button(this);
                botonPersonaje.setText(personaje.getNombre() + " || Salud: " + personaje.getSalud());
                botonPersonaje.setTextColor(0xFFFFFFFF);
                botonPersonaje.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);

                // Aplicar la fuente personalizada
                Typeface typeface = ResourcesCompat.getFont(this, R.font.dungeon_depths);
                if (typeface != null) {
                    botonPersonaje.setTypeface(typeface);
                }

                // Verificar si la salud es 0 para asignar el fondo correspondiente
                if (personaje.getSalud() == 0) {
                    botonPersonaje.setBackgroundResource(R.drawable.rounded_button_red);  // Fondo rojo si la salud es 0
                } else {
                    botonPersonaje.setBackgroundResource(R.drawable.rounded_button2);  // Fondo normal si tiene salud
                }

                botonPersonaje.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(this, v);
                    popupMenu.getMenu().add("Ataque manual");
                    popupMenu.getMenu().add("Ataque automático");

                    popupMenu.setOnMenuItemClickListener(item -> {
                        String title = item.getTitle().toString();
                        if ("Ataque manual".equals(title)) {

                            // Crear un AlertDialog para ingresar el daño
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Ingrese el daño que ha causado");

                            // Crear un EditText para ingresar el daño
                            final EditText input = new EditText(this);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER); // Solo números
                            builder.setView(input);

                            // Configurar el botón de "Aceptar"
                            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                                String danioStr = input.getText().toString();
                                if (!danioStr.isEmpty()) {
                                    int danio = Integer.parseInt(danioStr);  // Convertir el texto a número
                                    // Llamar a la función para manejar el ataque manual con el daño
                                    causarDanio(personaje, danio);
                                    dbHandler.modificarSaludDePersonaje(personaje.getNombre(), danio, false);
                                    int salud = personaje.getSalud();
                                    String mensaje = "Ataque automático con " + armaSeleccionada.getNombre() +
                                            " contra " + personaje.getNombre() + "\nDaño causado = " + danio + " al enemigo le quedan " + salud + " puntos de salud";
                                    alertaAtaque.setText(mensaje);
                                    // Refrescar la lista de personajes después del ataque
                                    cargarYMostrarPersonajes(alertaAtaque);
                                } else {
                                    Toast.makeText(this, "Debe ingresar un valor de daño", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Configurar el botón de "Cancelar"
                            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

                            // Mostrar el AlertDialog
                            builder.show();
                        } else if ("Ataque automático".equals(title)) {
                            if (armaSeleccionada != null) {
                                int danio = realizarAtaqueAutomatico(armaSeleccionada, personaje);
                                causarDanio(personaje, danio);
                                dbHandler.modificarSaludDePersonaje(personaje.getNombre(), danio, false);
                                int salud = personaje.getSalud();
                                String mensaje = "Ataque automático con " + armaSeleccionada.getNombre() +
                                        " contra " + personaje.getNombre() + "\nDaño causado = " + danio + " al enemigo le quedan " + salud + " puntos de salud";
                                alertaAtaque.setText(mensaje);
                                // Refrescar la lista de personajes después del ataque
                                cargarYMostrarPersonajes(alertaAtaque);
                            } else {
                                Toast.makeText(this, "No se ha seleccionado un arma", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    });

                    popupMenu.show();
                });

                // Crear un objeto LayoutParams para establecer márgenes entre los botones
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho del botón
                        LinearLayout.LayoutParams.WRAP_CONTENT   // Alto del botón
                );

                // Establecer márgenes (espacio entre los botones)
                params.setMargins(0, 10, 0, 10);  // Margen superior e inferior de 10dp entre los botones
                botonPersonaje.setLayoutParams(params);

                linearLayoutPersonajes.addView(botonPersonaje);
            }
        }
    }


    public void causarDanio(Personaje objetivo, int danio) {
        objetivo.setSalud(objetivo.getSalud() - danio);

        if (objetivo.getSalud() < 0) {
            objetivo.setSalud(0);
        }
    }

    public int realizarAtaqueAutomatico(Arma arma, Personaje enemigo) {
        Random random = new Random();
        int danio = 0;
        int ataquesAcertados = 0;

        //Numero de ataques que impactan sobre el enemigo
        for(int i = 0; i<arma.getAtaques(); i++) {
            int control = random.nextInt(6) + 1;
            if(control >= arma.getPrecision()) {
                ataquesAcertados++;
            }
        }

        int ataquesEfectivos = 0;
        //Numero de ataques impactados que son efectivos
        for(int i = 0; i<ataquesAcertados; i++) {
            int control = random.nextInt(6) + 1;
            int fuerzaArma = arma.getFuerza();
            int resistenciaEnemigo = enemigo.getEstadisticas().get(1);
            int dificultadDanio = comparacionFuerzaResistencia(fuerzaArma, resistenciaEnemigo);
            if(control >= dificultadDanio) {
                ataquesEfectivos++;
            }
        }

        //Salvaciones del enemigo
        for(int i = 0; i<ataquesEfectivos; i++) {
            int control = random.nextInt(6) + 1;
            if(control < enemigo.getEstadisticas().get(2)){
                danio += danio + arma.getDanio();
            }
        }
        return danio;
    }

    public int comparacionFuerzaResistencia(int fuerza, int resistencia) {
        int valor = 0;
        if (fuerza >= resistencia*2) {
            valor = 2;
        } else if (fuerza > resistencia) {
            valor = 3;
        } else if (fuerza == resistencia) {
            valor = 4;
        } else if (fuerza*2 <= resistencia) {
            valor = 6;
        } else {
            valor = 5;
        }
        return valor;
    }
}
