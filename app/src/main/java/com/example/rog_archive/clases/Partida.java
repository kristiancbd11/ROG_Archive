package com.example.rog_archive.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String nombre;
    private Integer nJugadores;
    private String usuarioUid;
    private int descansos = 2;
    private int recursos = 0;
    private ArrayList<Personaje> personajes;

    public Partida(String nombre, int nJugadores, String usuarioUid) {
        this.nombre = nombre;
        this.nJugadores = nJugadores;
        this.usuarioUid = usuarioUid;
        this.personajes = new ArrayList<>();
    }

    public String getUsuarioUid() {
        return usuarioUid;
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

    public int getDescansos() {
        return descansos;
    }

    public void setDescansos(int descansos) {
        this.descansos = descansos;
    }

    public int getRecursos() {
        return recursos;
    }

    public void setRecursos(int recursos) {
        this.recursos = recursos;
    }

    public void setNJugadores(int nJugadores) {
        this.nJugadores = nJugadores;
    }

    public ArrayList<Personaje> getPersonajes() {
        return personajes;
    }

    public void aniadirUnPersonaje(Personaje personaje) {
        personajes.add(personaje);
    }

    public void setPersonajes(ArrayList<Personaje> personajes) {
        this.personajes = personajes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


}