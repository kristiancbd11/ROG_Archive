package com.example.rog_archive.vistasdepartida;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MovimientoDialogFragment extends DialogFragment {

    private String mensaje;

    public static MovimientoDialogFragment newInstance() {
        return new MovimientoDialogFragment();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Crear el layout para el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Configurar el contenido del dialogo
        builder.setTitle("Información de Movimiento")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", (dialog, id) -> dismiss()) // Cerrar el diálogo
                .setCancelable(true);

        // Retornar el dialogo
        return builder.create();
    }
}
