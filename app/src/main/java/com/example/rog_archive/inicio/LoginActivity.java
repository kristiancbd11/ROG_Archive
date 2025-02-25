package com.example.rog_archive.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Variables para la autenticación de Firebase
    private FirebaseAuth auth;
    private EditText editTextCorreo, editTextContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Declaración de los elementos de la vista
        ImageButton atrasButton = findViewById(R.id.botonLoginAtras);
        ImageButton continuarButton = findViewById(R.id.botonLoginContinuar);
        TextView irRegistro = findViewById(R.id.textRegister);
        editTextCorreo = findViewById(R.id.editTextCorreoElectronicoLogin);  // Suponiendo que tienes un campo para el correo
        editTextContrasena = findViewById(R.id.editTextPassword);  // Suponiendo que tienes un campo para la contraseña

        // Botón para regresar a la pantalla principal
        atrasButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Botón para continuar con el login
        continuarButton.setOnClickListener(view -> {
            String correo = editTextCorreo.getText().toString().trim();
            String contrasena = editTextContrasena.getText().toString().trim();

            // Validaciones de las credenciales
            if (TextUtils.isEmpty(correo)) {
                Toast.makeText(LoginActivity.this, "Por favor ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(contrasena)) {
                Toast.makeText(LoginActivity.this, "Por favor ingresa tu contraseña.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intentar iniciar sesión con Firebase
            auth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Si el inicio de sesión es exitoso, redirigir al menú principal
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(LoginActivity.this, MenuRogActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Si el inicio de sesión falla, mostrar mensaje de error
                            Toast.makeText(LoginActivity.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Botón para ir a la pantalla de registro
        irRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
