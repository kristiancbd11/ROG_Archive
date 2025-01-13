package com.example.rog_archive.gestionpartidas;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.gestionficheros.GestorFicheros;

public class NuevaPartidaFragment extends Fragment {

    public NuevaPartidaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nueva_partida, container, false);

        Spinner spinner = rootView.findViewById(R.id.spinnerNumeroJugadores);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, getNumeros());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Referencia al campo de texto para el nombre de la partida
        EditText editTextNombrePartida = rootView.findViewById(R.id.editTextNombrePartida);

        // Creación del botón para generar la nueva partida y su funcionalidad
        Button botonCrearNuevaPartida = rootView.findViewById(R.id.botonCrearNuevaPartida);
        botonCrearNuevaPartida.setOnClickListener(view -> {
            String nombrePartida = editTextNombrePartida.getText().toString().trim();
            int numeroJugadores = (int) spinner.getSelectedItem();

            if (nombrePartida.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, ingrese un nombre de partida.", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar la partida en el archivo
                GestorFicheros gf = new GestorFicheros();
                Partida partida = new Partida(nombrePartida, numeroJugadores);
                gf.guardarPartida(getContext(), partida);

                editTextNombrePartida.setText(""); // Limpiar campo de texto
                spinner.setSelection(0); // Resetear spinner
            }
        });

        // Configurar el botón "Atrás"
        ImageView atrasImage = rootView.findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(view -> {
            // Volver al fragmento anterior en la pila
            requireActivity().onBackPressed();
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
}