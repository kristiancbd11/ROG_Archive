package com.example.rog_archive.gestionpersonajes;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rog_archive.R;
import com.example.rog_archive.clases.Personaje;

public class VistaPersonajeFragment extends Fragment {

    private Personaje personaje;

    public VistaPersonajeFragment() {
        // Required empty public constructor
    }

    public static VistaPersonajeFragment newInstance(Personaje personaje) {
        VistaPersonajeFragment fragment = new VistaPersonajeFragment();
        Bundle args = new Bundle();
        args.putSerializable("personaje", personaje); // Pasa el objeto Personaje al Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vista_personaje, container, false);

        // Recuperar el personaje desde los argumentos
        if (getArguments() != null) {
            personaje = (Personaje) getArguments().getSerializable("personaje");
        }

        // Mostrar detalles del personaje
        if (personaje != null) {
            TextView nombreTextView = view.findViewById(R.id.nombrePj);
            TextView razaTextView = view.findViewById(R.id.razaPj);
            TextView claseTextView = view.findViewById(R.id.clasePj);
            TextView nivelTextView = view.findViewById(R.id.nivelPj);

            nombreTextView.setText(personaje.getNombre());
            razaTextView.setText(personaje.getRaza());
            claseTextView.setText(personaje.getClase());
            nivelTextView.setText(String.valueOf(personaje.getNivel()));
        }

        ImageView imagenPj = view.findViewById(R.id.imagenPj);

        // Configurar el comportamiento de la flecha atrás
        ImageView flechaAtras = view.findViewById(R.id.imageViewAtras);
        flechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver al fragmento anterior en la pila
                getActivity().onBackPressed(); // Esto simula la acción de "Atrás" del sistema
            }
        });

        return view;
    }
}