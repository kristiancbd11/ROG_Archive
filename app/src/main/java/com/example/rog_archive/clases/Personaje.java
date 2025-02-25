package com.example.rog_archive.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Personaje implements Serializable {
    private long id;
    private String nombre;
    private String raza;
    private String clase;
    private int nivel;
    private String imagenUri; // Campo para almacenar la URI de la imagen
    private int salud;
    private ArrayList<Integer> estadisticas = new ArrayList<Integer>();
    private String usuarioUid;

    // Constructor sin imagen
    public Personaje(String nombre, String raza, String clase, String usuarioUid) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = 1;
        this.imagenUri = null; // Por defecto no hay imagen
        this.usuarioUid = usuarioUid;
    }

    // Constructor con imagen
    public Personaje(String nombre, String raza, String clase, String imagenUri, String usuarioUid) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = 1;
        this.imagenUri = imagenUri;
        this.usuarioUid = usuarioUid;
    }

    // Constructor completo
    public Personaje(String nombre, String raza, String clase, int nivel, String imagenUri, String usuarioUid) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.nivel = nivel;
        this.imagenUri = imagenUri;
        this.usuarioUid = usuarioUid;
    }

    public void instertarEstadisticas(int m, int r, int s, int h, int l, int co, int saludActual) {
        salud = saludActual;
        estadisticas.add(m);
        estadisticas.add(r);
        estadisticas.add(s);
        estadisticas.add(h);
        estadisticas.add(l);
        estadisticas.add(co);
    }

    // Getters
    public ArrayList<Integer> getEstadisticas() {
        return new ArrayList<>(estadisticas);
    }

    public String getUsuarioUid() {
        return this.usuarioUid;
    }

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

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}