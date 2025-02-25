package com.example.rog_archive.gestionpersonajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Arma;
import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.gestordb.DBHandler;

import org.w3c.dom.Text;

public class AniadirArmaFragment extends Fragment {
    private Personaje personaje;

    public AniadirArmaFragment() {
        // Constructor vacío requerido
    }

    public static AniadirArmaFragment newInstance() {
        return new AniadirArmaFragment();
    }

    public static AniadirArmaFragment newInstance(Personaje personaje) {
        AniadirArmaFragment fragment = new AniadirArmaFragment();
        Bundle args = new Bundle();
        args.putSerializable("personaje", personaje); // Pasar el objeto Personaje al Bundle
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_aniadir_arma, container, false);

        // Abrir la base de datos
        DBHandler dbHandler = new DBHandler(getActivity());
        dbHandler.open();

        if (getArguments() != null) {
            personaje = (Personaje) getArguments().getSerializable("personaje");
        }

        // Referencias a los EditText donde el usuario ingresará los datos del arma
        final EditText nombreArmaEditText = view.findViewById(R.id.NombreArma);
        final EditText alcanceEditText = view.findViewById(R.id.alcance);
        final EditText ataquesEditText = view.findViewById(R.id.ataques);
        final EditText precisionEditText = view.findViewById(R.id.precision);
        final EditText fuerzaEditText = view.findViewById(R.id.fuerza);
        final EditText perforacionEditText = view.findViewById(R.id.perforacion);
        final EditText danioEditText = view.findViewById(R.id.danio);
        final TextView nombrePersonajeTextView = view.findViewById(R.id.nombrePersonaje);
        String nombrePersonaje = "Añadiendo arma a " + personaje.getNombre();
        nombrePersonajeTextView.setText(nombrePersonaje);

        // Botón para añadir el arma
        ImageButton aniadirArmaButton = view.findViewById(R.id.aniadirArmaButton);
        aniadirArmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar que todos los campos estén llenos
                if (nombreArmaEditText.getText().toString().isEmpty() ||
                        alcanceEditText.getText().toString().isEmpty() ||
                        ataquesEditText.getText().toString().isEmpty() ||
                        precisionEditText.getText().toString().isEmpty() ||
                        fuerzaEditText.getText().toString().isEmpty() ||
                        perforacionEditText.getText().toString().isEmpty() ||
                        danioEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Obtener los valores de los EditText y el nombre del personaje
                String nombreArma = nombreArmaEditText.getText().toString();
                int alcance = Integer.parseInt(alcanceEditText.getText().toString());
                int ataques = Integer.parseInt(ataquesEditText.getText().toString());
                int precision = Integer.parseInt(precisionEditText.getText().toString());
                int fuerza = Integer.parseInt(fuerzaEditText.getText().toString());
                int perforacion = Integer.parseInt(perforacionEditText.getText().toString());
                int danio = Integer.parseInt(danioEditText.getText().toString());
                String portador = personaje.getNombre();
                // Crear el objeto Arma
                Arma nuevaArma = new Arma(nombreArma, alcance, ataques, precision, fuerza, perforacion, danio, portador);

                // Usar DBHandler para insertar el arma en la base de datos
                dbHandler.insertarArma(nuevaArma);

                // Limpiar los campos después de añadir el arma
                nombreArmaEditText.setText("");
                alcanceEditText.setText("");
                ataquesEditText.setText("");
                precisionEditText.setText("");
                fuerzaEditText.setText("");
                perforacionEditText.setText("");
                danioEditText.setText("");
            }
        });

        return view;
    }
}
