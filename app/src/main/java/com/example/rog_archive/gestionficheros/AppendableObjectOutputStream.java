package com.example.rog_archive.gestionficheros;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class AppendableObjectOutputStream extends ObjectOutputStream {
    AppendableObjectOutputStream(FileOutputStream fos) throws IOException {
        super(fos);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // No escribimos una nueva cabecera.
        reset();
    }
}