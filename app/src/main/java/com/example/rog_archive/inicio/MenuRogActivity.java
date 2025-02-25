package com.example.rog_archive.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.example.rog_archive.gestionpartidas.CreadorPartidaActivity;
import com.example.rog_archive.gestionpersonajes.ArchivoPersonajesActivity;
import com.example.rog_archive.gestionpersonajes.CreadorPersonajesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MenuRogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ROG_Archive);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rog);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageButton creadorButton = findViewById(R.id.botonCrearPersonaje);
        creadorButton.setOnClickListener(view -> {
            Intent intent = new Intent(MenuRogActivity.this, CreadorPersonajesActivity.class);
            startActivity(intent);
        });

        ImageButton archivoButton = findViewById(R.id.botonArchivoPersonajes);
        archivoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MenuRogActivity.this, ArchivoPersonajesActivity.class);
            startActivity(intent);
        });

        ImageButton logoutButton = findViewById(R.id.botonLogout);
        logoutButton.setOnClickListener(view -> logout());

        ImageButton crearPartida = findViewById(R.id.botonCrearPartida);
        crearPartida.setOnClickListener(view -> {
            Intent intent = new Intent(MenuRogActivity.this, CreadorPartidaActivity.class);
            startActivity(intent);
        });
    }

    private void logout() {
        // Cerrar sesión en Firebase
        mAuth.signOut();

        // Cerrar sesión de Google si el usuario inició sesión con Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Redirigir a la pantalla principal
            Intent intent = new Intent(MenuRogActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
