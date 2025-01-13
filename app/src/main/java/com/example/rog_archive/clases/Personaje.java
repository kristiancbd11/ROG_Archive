package com.example.rog_archive.clases;

import java.io.Serializable;

public class Personaje implements Serializable {
    private String nombre;
    private String raza;
    private String clase;
    private int nivel;
    private String imagenUri; // Campo para almacenar la URI de la imagen

    // Constructor sin imagen
    public Personaje(String nombre, String raza, String clase) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = 1;
        this.imagenUri = null; // Por defecto no hay imagen
    }

    // Constructor con imagen
    public Personaje(String nombre, String raza, String clase, String imagenUri) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = 1;
        this.imagenUri = imagenUri;
    }

    // Constructor completo
    public Personaje(String nombre, String raza, String clase, int nivel, String imagenUri) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = nivel;
        this.imagenUri = imagenUri;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public String getClase() {
        return clase;
    }

    public int getNivel() {
        return nivel;
    }

    public String getImagenUri() {
        return imagenUri;
    }

    // Setters
    public void setImagenUri(String imagenUri) {
        this.imagenUri = imagenUri;
    }
}