package com.example.rog_archive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.gestiontcp.HostTCP;

public class MenuMasterActivity extends AppCompatActivity {

    private HostTCP hostTCP = new HostTCP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_master);

        String codigoGenerado = getIntent().getStringExtra("CODIGO_GENERADO");

        // Mostrar el código, por ejemplo en un TextView
        TextView textViewCodigo = findViewById(R.id.textCodigo);
        textViewCodigo.setText(codigoGenerado);

        // Configurar el botón "Atrás" con el diálogo de confirmación
        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(view -> {
            new AlertDialog.Builder(MenuMasterActivity.this)
                    .setMessage("¿Estás seguro de querer cerrar la partida actual?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", (dialog, id) -> {

                        // Regresar a la pantalla principal
                        Intent intent = new Intent(MenuMasterActivity.this, MenuRogActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar la actividad actual
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        Button prueba = findViewById(R.id.botonMasterEvento);
        prueba.setOnClickListener(view -> {
            hostTCP.iniciarBroadcast();
        });
    }
}
