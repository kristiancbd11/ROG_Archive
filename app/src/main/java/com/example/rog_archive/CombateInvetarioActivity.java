package com.example.rog_archive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CombateInvetarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_combate_invetario);

        ImageView iconoPrincipal = findViewById(R.id.pestanya_combate);
        iconoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CombateInvetarioActivity.this, CombateActivity.class);
                startActivity(intent);
            }
        });

        ImageView iconoEquipacion = findViewById(R.id.pestanya_equipacion);
        iconoEquipacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CombateInvetarioActivity.this, CombateEquipacionActivity.class);
                startActivity(intent);
            }
        });

        ImageView iconoInformacion = findViewById(R.id.pestanya_informacion);
        iconoInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CombateInvetarioActivity.this, CombateInformacionActivity.class);
                startActivity(intent);
            }
        });

        ImageView cruzSalir = findViewById(R.id.cruzSalir);
        cruzSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoSalir();
            }
        });
    }

    private void mostrarDialogoSalir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CombateInvetarioActivity.this);
        builder.setTitle("Salir de la partida");
        builder.setMessage("¿Estás seguro de que quieres abandonar la partida?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CombateInvetarioActivity.this, MenuPartidaActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el diálogo
            }
        });
        builder.create().show(); // Muestra el cuadro de diálogo
    }
}