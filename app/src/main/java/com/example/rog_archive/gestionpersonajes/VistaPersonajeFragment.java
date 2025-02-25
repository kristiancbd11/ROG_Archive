package com.example.rog_archive.gestionpersonajes;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.clases.Arma;
import com.example.rog_archive.gestordb.DBHandler;
import com.example.rog_archive.pdfgenerator.PDFGenerator;

import java.util.ArrayList;

public class VistaPersonajeFragment extends Fragment {

    private Personaje personaje;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    // Request code for image picking
    private static final int PICK_IMAGE_REQUEST = 100;

    public VistaPersonajeFragment() {
        // Constructor vacío requerido
    }

    public static VistaPersonajeFragment newInstance(Personaje personaje) {
        VistaPersonajeFragment fragment = new VistaPersonajeFragment();
        Bundle args = new Bundle();
        args.putSerializable("personaje", personaje); // Pasar el objeto Personaje al Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar el lanzador de permisos
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (result.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false)) {
                        // Permiso concedido, abrir el selector de imagen
                        openImagePicker();
                    } else {
                        // Permiso denegado
                        Toast.makeText(getContext(), "Permiso denegado, no se puede acceder a las imágenes", Toast.LENGTH_SHORT).show();
                    }
                });

        // Inicializar el lanzador para seleccionar imágenes
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                personaje.setImagenUri(selectedImageUri.toString());
                                ImageView imageView = getView().findViewById(R.id.imagenPj);
                                Glide.with(getContext())
                                        .load(selectedImageUri)
                                        .into(imageView);
                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_personaje, container, false);

        // Recuperar el personaje desde los argumentos
        if (getArguments() != null) {
            personaje = (Personaje) getArguments().getSerializable("personaje");
        }

        // Mostrar detalles del personaje
        if (personaje != null) {
            // Mostrar nombre, raza, clase y nivel
            TextView nombreTextView = view.findViewById(R.id.nombrePj);
            TextView razaTextView = view.findViewById(R.id.razaPj);
            TextView claseTextView = view.findViewById(R.id.clasePj);
            TextView nivelTextView = view.findViewById(R.id.nivelPj);

            nombreTextView.setText(personaje.getNombre());
            razaTextView.setText(personaje.getRaza());
            claseTextView.setText(personaje.getClase());
            String nivel = "Nivel: " + String.valueOf(personaje.getNivel());
            nivelTextView.setText(nivel);

            // Mostrar estadísticas
            TextView estadisticaM = view.findViewById(R.id.estadisticaM);
            TextView estadisticaR = view.findViewById(R.id.estadisticaR);
            TextView estadisticaS = view.findViewById(R.id.estadisticaS);
            TextView estadisticaH = view.findViewById(R.id.estadisticaH);
            TextView estadisticaL = view.findViewById(R.id.estadisticaL);
            TextView estadisticaCO = view.findViewById(R.id.estadisticaCO);
            TextView saludTextView = view.findViewById(R.id.valorSalud);

            ArrayList<Integer> estadisticas = personaje.getEstadisticas();
            String textoSalud = personaje.getSalud() + " / " + estadisticas.get(3);

            saludTextView.setText(String.valueOf(textoSalud));
            estadisticaM.setText("Movilidad: " + estadisticas.get(0));
            estadisticaR.setText("Resistencia: " + estadisticas.get(1));
            estadisticaS.setText("Salvación: " + estadisticas.get(2) + "+");
            estadisticaH.setText("Heridas: " + estadisticas.get(3));
            estadisticaL.setText("Liderazgo: " + estadisticas.get(4) + "+");
            estadisticaCO.setText("Control: " + estadisticas.get(5));

            if (personaje.getImagenUri() != null) {
                String imagenUri = personaje.getImagenUri();
                Log.d("VistaPersonajeFragment", "Imagen URI: " + imagenUri);
                ImageView imageView = view.findViewById(R.id.imagenPj);

                Glide.with(getContext())
                        .load(Uri.parse(imagenUri))
                        .into(imageView);
            } else {
                Log.e("VistaPersonajeFragment", "La URI de la imagen es nula.");
            }

            // Actualización de la ProgressBar de salud
            ProgressBar barraSalud = view.findViewById(R.id.barraSalud);

            // Establecer el progreso de la barra de salud basado en el valor de salud
            int saludActual = personaje.getSalud();
            int saludMaxima = personaje.getEstadisticas().get(3);  // Esto es solo un ejemplo, ajusta según el valor máximo de salud que manejes
            barraSalud.setMax(saludMaxima);
            barraSalud.setProgress(saludActual);

        }

        // Recuperar las armas del personaje
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.open();
        ArrayList<Arma> armas = dbHandler.obtenerArmasPorPersonaje(personaje.getNombre()); // Obtener armas por nombre

        LinearLayout armasLayout = view.findViewById(R.id.armas);

        if (armas == null || armas.isEmpty()) {
            TextView noArmasTextView = new TextView(getContext());
            noArmasTextView.setText("Este personaje no tiene armas asignadas.");
            noArmasTextView.setTextSize(16);
            noArmasTextView.setTextColor(0xffbe506f);
            armasLayout.addView(noArmasTextView);
        } else {
            for (Arma arma : armas) {
                TextView armaTextView = new TextView(getContext());
                String armaTexto = arma.getNombre() + "\n|| AL: " + arma.getAlcance() + " || AT: " + arma.getAtaques() +
                        " ||: PRE " + arma.getPrecision() + " ||\n|| F: " + arma.getFuerza() + " || PRF: " + arma.getPerforacion() +
                        " || D: " + arma.getDanio() + " ||";
                armaTextView.setText(armaTexto);
                armaTextView.setTextSize(14);
                armaTextView.setGravity(Gravity.CENTER);

                // Aplica el fondo de estilo_azul_oscuro al TextView
                armaTextView.setBackgroundResource(R.drawable.estilo_azul_oscuro);  // Aplica el estilo de fondo

                // Establece el color del texto a blanco
                armaTextView.setTextColor(0xffbe506f);  // Blanco

                // Establecer layout_width a match_parent
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Ancho completo
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Altura ajustable
                );
                armaTextView.setLayoutParams(layoutParams);

                armasLayout.addView(armaTextView);
            }
        }

        ImageButton exportarPDFButton = view.findViewById(R.id.botonGenerarPDF);
        exportarPDFButton.setOnClickListener(v -> {
            // Llamar a PDFGenerator para crear el PDF
            PDFGenerator.generarPDF(personaje, armas, getContext());
        });

        // Botón para añadir un arma
        ImageButton aniadirArmaButton = view.findViewById(R.id.aniadirArma);
        aniadirArmaButton.setOnClickListener(v -> {
            AniadirArmaFragment aniadirArmaFragment = AniadirArmaFragment.newInstance(personaje);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, aniadirArmaFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    // Método para abrir el selector de imágenes
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }
}
