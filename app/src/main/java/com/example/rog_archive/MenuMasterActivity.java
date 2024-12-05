package com.example.rog_archive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MenuMasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_master);

        String codigoGenerado = getIntent().getStringExtra("CODIGO_GENERADO");

        // Mostrar el código, por ejemplo en un TextView
        TextView textViewCodigo = findViewById(R.id.textCodigo);
        textViewCodigo.setText(codigoGenerado);

        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear el AlertDialog para confirmar si el usuario quiere cerrar la sesión
                new AlertDialog.Builder(MenuMasterActivity.this)
                        .setMessage("¿Estás seguro de querer cerrar la partida actual?")
                        .setCancelable(false) // El diálogo no se puede cancelar tocando fuera
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario selecciona "Sí", se regresa a MainActivity
                                Intent intent = new Intent(MenuMasterActivity.this, MenuRogActivity.class);
                                startActivity(intent);
                                finish(); // Finaliza la actividad actual
                            }
                        })
                        .setNegativeButton("No", null) // Si el usuario selecciona "No", no hace nada
                        .show();
            }
        });
    }
}
