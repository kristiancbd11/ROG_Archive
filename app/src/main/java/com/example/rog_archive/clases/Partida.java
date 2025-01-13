package com.example.rog_archive.clases;

import java.io.Serializable;

public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private Integer nJugadores;

    public Partida(String nombre, int nJugadores) {
        this.nombre = nombre;
        this.nJugadores = nJugadores;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNJugadores() {
        return nJugadores;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNJugadores(int nJugadores) {
        this.nJugadores = nJugadores;
    }

}