package com.example.rog_archive.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //Declaración de los elementos de la vista
        ImageButton continuarButton = findViewById(R.id.botonRegContinuar);
        ImageButton botonAtras = findViewById(R.id.botonRegAtras);
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        continuarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editTextUsuario.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String passwordConfirm = editTextPasswordConfirm.getText().toString();

                // Validación del usuario
                if (TextUtils.isEmpty(usuario)) {
                    Toast.makeText(RegisterActivity.this, "Ingrese nombre de usuario.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (usuario.contains(" ")) {
                    Toast.makeText(RegisterActivity.this, "El nombre de usuario no debe contener espacios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validación de la contraseña
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isPasswordValid(password)) {
                    Toast.makeText(RegisterActivity.this,
                            "La contraseña debe corresponder con el siguiente formato:\n" +
                                    "- Longitud entre 8 y 16 caracteres\n" +
                                    "- Al menos un carácter numérico\n" +
                                    "- Al menos una mayúscula\n" +
                                    "- Al menos una minúscula",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si todo está bien, continuar
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            // Método de validación para la contraseña
            private boolean isPasswordValid(String password) {
                if (password.length() < 8 || password.length() > 16) return false;
                if (!password.matches(".\\d.")) return false; // Contiene un número
                if (!password.matches(".[A-Z].")) return false; // Contiene una mayúscula
                if (!password.matches(".[a-z].")) return false; // Contiene una minúscula
                return true;
            }

        });
    }
}