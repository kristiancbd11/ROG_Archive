package com.example.rog_archive.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rog_archive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText editTextUsuario, editTextCorreoElectronico, editTextPassword, editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase
        auth = FirebaseAuth.getInstance();

        //Instanciación de los elementos visuales en el código
        ImageButton continuarButton = findViewById(R.id.botonRegContinuar);
        ImageButton botonAtras = findViewById(R.id.botonRegAtras);
        editTextUsuario = findViewById(R.id.editTextUsuarioRegister);
        editTextCorreoElectronico = findViewById(R.id.editTextCorreoElectronicoRegister);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);

        botonAtras.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });

        continuarButton.setOnClickListener(view -> validarYRegistrarUsuario());
    }

    private void validarYRegistrarUsuario() {
        String usuario = editTextUsuario.getText().toString().trim();
        String correo = editTextCorreoElectronico.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextPasswordConfirm.getText().toString();

        //Comprobamos que se haya agregado un nombre de usuario
        if (TextUtils.isEmpty(usuario)) {
            Toast.makeText(this, "Ingrese nombre de usuario.", Toast.LENGTH_SHORT).show();
            return;
        } else if (usuario.contains(" ")) {
            Toast.makeText(this, "El nombre de usuario no debe contener espacios.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Comprobamos que se haya agregado un correo electrónico
        if (TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Ingrese correo electrónico.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Comprobamos que se haya agregado una contreseña y que esta sea válida
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(this, "La contraseña debe tener entre 8 y 16 caracteres, al menos un número, una mayúscula y una minúscula.", Toast.LENGTH_LONG).show();
            return;
        }

        //Comprobamos que las contraseñas coincidan
        if (TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }



            auth.createUserWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usuario)
                                        .build();
                                user.updateProfile(profileUpdates);
                            }
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 16 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*");
    }

    private boolean isRecaptchaValid(String token) {
        return token.equals("0000000-0000000-000000-0000000");
    }
}
