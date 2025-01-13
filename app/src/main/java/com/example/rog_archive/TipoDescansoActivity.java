package com.example.rog_archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TipoDescansoActivity extends AppCompatActivity {

    private ImageView estrella1, estrella2;
    private Button botonDescansoCorto, botonDescansoLargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tipo_descanso);

        // Referencias a las estrellas
        estrella1 = findViewById(R.id.estrella1);
        estrella2 = findViewById(R.id.estrella2);

        ImageView botonAtras = findViewById(R.id.imageViewAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoDescansoActivity.this, MenuPartidaActivity.class);
                startActivity(intent);
            }
        });

        // Referencias a los botones
        botonDescansoCorto = findViewById(R.id.botonsDescansoCorto);
        botonDescansoLargo = findViewById(R.id.botonsDescansoLargo);

        // Evento de clic para el botón de descanso corto
        botonDescansoCorto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estrella2.getVisibility() == View.VISIBLE) {
                    estrella2.setVisibility(View.GONE);
                } else if (estrella1.getVisibility() == View.VISIBLE) {
                    estrella1.setVisibility(View.GONE);
                }
            }
        });

        // Evento de clic para el botón de descanso largo
        botonDescansoLargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hacer que ambas estrellas vuelvan a aparecer
                estrella1.setVisibility(View.VISIBLE);
                estrella2.setVisibility(View.VISIBLE);
            }
        });
    }
}