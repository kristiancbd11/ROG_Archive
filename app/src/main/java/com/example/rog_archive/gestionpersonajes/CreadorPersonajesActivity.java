package com.example.rog_archive.gestionpersonajes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.MenuRogActivity;
import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestionficheros.GestorFicheros;

public class CreadorPersonajesActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code para la galería
    private ImageView imageViewSeleccionarImagen; // Referencia al ImageView para la imagen seleccionada
    private Uri imagenSeleccionadaUri; // URI de la imagen seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creador_personajes);

        // Declaración de array para cargar los spinner
        String[] razas = {"Druk", "Innosita", "Graug", "Nagi", "Nordastri", "Oncaciano", "Sirivi", "Uglat"};
        String[] clases = {"Bárbaro", "Bardo", "Clérigo", "Explorador", "Guerrero", "Mago", "Paladín", "Pícaro"};

        // Cargar los spinner
        Spinner spinnerRazas = findViewById(R.id.spinnerRazas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, razas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRazas.setAdapter(adapter);

        Spinner spinnerClases = findViewById(R.id.spinnerClases);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClases.setAdapter(adapter);

        // Configuración del botón "Crear"
        ImageButton buttonCrear = findViewById(R.id.buttonCrear);
        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clase = spinnerClases.getSelectedItem().toString();
                String raza = spinnerRazas.getSelectedItem().toString();
                String nombre = ((EditText) findViewById(R.id.editTextNombrePj)).getText().toString();
                Personaje pj = new Personaje(nombre, raza, clase);

                // Guardar la imagen seleccionada (opcionalmente en Personaje)
                if (imagenSeleccionadaUri != null) {
                    pj.setImagenUri(imagenSeleccionadaUri.toString()); // Debe haber un método en Personaje para esto
                }

                // Guardar el personaje
                GestorFicheros gf = new GestorFicheros();
                gf.guardarPersonaje(CreadorPersonajesActivity.this, pj);
            }
        });

        // Configuración del ImageView para seleccionar la imagen
        imageViewSeleccionarImagen = findViewById(R.id.imageViewSeleccionarImagen);
        imageViewSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la galería
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Botón para regresar al menú anterior
        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreadorPersonajesActivity.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenSeleccionadaUri = data.getData(); // Obtener URI de la imagen seleccionada
            imageViewSeleccionarImagen.setImageURI(imagenSeleccionadaUri); // Mostrar la imagen en el ImageView
        }
    }
}