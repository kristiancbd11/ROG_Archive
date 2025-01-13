package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ExploracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploracion);

        String[] tipDesafio = {"Fuerza", "Constitución", "Destreza", "Inteligencia", "Carisma", "Sabiduría"};

        Spinner spinner = findViewById(R.id.spinnerTipoDesafio);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipDesafio);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageView imageDado = findViewById(R.id.dado);
        TextView numDado = findViewById(R.id.numDado);
        imageDado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numDado.setText(String.valueOf(generarNum()));
            }
        });

        ImageView botonAtras = findViewById(R.id.imageViewAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploracionActivity.this, MenuPartidaActivity.class);
                startActivity(intent);
            }
        });

    }

    public int generarNum() {
        Random r = new Random();
        return r.nextInt(20) + 1;
    }
}