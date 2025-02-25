package com.example.rog_archive.gestionpersonajes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreadorPersonajesActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code para la galería
    private static final int CAPTURE_IMAGE_REQUEST = 2; // Request code para la cámara
    private ImageView imageViewSeleccionarImagen; // Referencia al ImageView para la imagen seleccionada
    private Uri imagenSeleccionadaUri; // URI de la imagen seleccionada
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creador_personajes);

        // Declaraciones para el uso de la base de datos
        DBHandler dbHandler = new DBHandler(CreadorPersonajesActivity.this);
        dbHandler.open();

        // Declaración de array para cargar los spinner
        String[] razas = {"Astarte", "Tiranido", "Nekron", "T'au", "Orko", "Aeldari", "Drukari", "Kain"};
        String[] clases = {"Asalto", "Táctico", "Apoyo", "Explorador", "Artilleria", "Psíquico", "Guardian", "Asesino"};

        // Cargar los spinner
        Spinner spinnerRazas = findViewById(R.id.spinnerRazas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, razas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRazas.setAdapter(adapter);

        Spinner spinnerClases = findViewById(R.id.spinnerClases);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClases.setAdapter(adapter);

        // Información de los edittext de estadísticas
        EditText e1 = findViewById(R.id.estadisticaM);
        EditText e2 = findViewById(R.id.estadisticaR);
        EditText e3 = findViewById(R.id.estadisticaS);
        EditText e4 = findViewById(R.id.estadisticaH);
        EditText e5 = findViewById(R.id.estadisticaL);
        EditText e6 = findViewById(R.id.estadisticaCO);

        // Configuración del botón "Crear"
        ImageButton buttonCrear = findViewById(R.id.buttonCrear);
        buttonCrear.setOnClickListener(view -> {
            String clase = spinnerClases.getSelectedItem().toString();
            String raza = spinnerRazas.getSelectedItem().toString();
            String nombre = ((EditText) findViewById(R.id.editTextNombrePj)).getText().toString();
            if (user != null) {
                String usuarioUid = user.getUid();
                Personaje pj = new Personaje(nombre, raza, clase, usuarioUid);
                pj.instertarEstadisticas(Integer.parseInt(e1.getText().toString().trim()),
                        Integer.parseInt(e2.getText().toString().trim()),
                        Integer.parseInt(e3.getText().toString().trim()),
                        Integer.parseInt(e4.getText().toString().trim()),
                        Integer.parseInt(e5.getText().toString().trim()),
                        Integer.parseInt(e6.getText().toString().trim()),
                        Integer.parseInt(e4.getText().toString().trim()));

                // Guardar la imagen seleccionada
                if (imagenSeleccionadaUri != null) {
                    pj.setImagenUri(imagenSeleccionadaUri.toString());
                }

                // Guardar el personaje en la base de datos
                dbHandler.insertarPersonaje(pj);
                dbHandler.close();
            } else {
                Toast.makeText(CreadorPersonajesActivity.this, "Error al crear el personaje", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuración del ImageView para seleccionar la imagen
        imageViewSeleccionarImagen = findViewById(R.id.imageViewSeleccionarImagen);
        imageViewSeleccionarImagen.setOnClickListener(v -> {
            // Mostrar la ventana emergente para elegir entre galería o cámara
            mostrarOpcionesImagen();
        });
    }

    private void mostrarOpcionesImagen() {
        // Crear un diálogo con las opciones "Usar imagen existente" o "Tomar nueva imagen"
        new AlertDialog.Builder(this)
                .setTitle("Selecciona una imagen")
                .setItems(new String[]{"Usar imagen existente", "Tomar nueva imagen"}, (dialog, which) -> {
                    if (which == 0) {
                        // Opción: Usar imagen existente
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    } else if (which == 1) {
                        // Opción: Tomar nueva imagen
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
                        } else {
                            Toast.makeText(CreadorPersonajesActivity.this, "No se puede acceder a la cámara", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Imagen seleccionada de la galería
                imagenSeleccionadaUri = data.getData();
                imageViewSeleccionarImagen.setImageURI(imagenSeleccionadaUri); // Mostrar la imagen seleccionada
            } else if (requestCode == CAPTURE_IMAGE_REQUEST && data != null) {
                // Imagen tomada con la cámara
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // Convertir la imagen a URI
                imagenSeleccionadaUri = getImageUri(imageBitmap);
                imageViewSeleccionarImagen.setImageURI(imagenSeleccionadaUri); // Mostrar la imagen tomada
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        // Función para convertir la imagen Bitmap en URI
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }
}
