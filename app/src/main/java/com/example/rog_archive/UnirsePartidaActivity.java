package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UnirsePartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unirse_partida);

        ImageView atrasImage = findViewById(R.id.imageViewAtras);
        atrasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnirsePartidaActivity.this, MenuRogActivity.class);
                startActivity(intent);
            }
        });

        Button botonUnirse = findViewById(R.id.botonConfirmarUnirse);
        final EditText editTextCodigoUnirse = findViewById(R.id.editTextCodigoUnirse); // Asegúrate de que este ID coincida con el ID real del EditText

        botonUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = editTextCodigoUnirse.getText().toString().trim(); // Obtener el texto del EditText

                // Comprobar si el EditText está vacío
                if (codigo.isEmpty()) {
                    // Si está vacío, mostrar un mensaje de error
                    Toast.makeText(UnirsePartidaActivity.this, "No has ingresado ningún código", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no está vacío, continuar con el flujo normal y abrir la siguiente actividad
                    Intent intent = new Intent(UnirsePartidaActivity.this, SeleccionarPersonajeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
