package com.example.rog_archive.clases;

public class Arma {
    String nombre;
    int alcance;
    int ataques;
    int precision;
    int fuerza;
    int perforacion;
    int danio;
    String portador;

    public Arma(String nombre, int alcance, int ataques, int precision, int fuerza, int perforacion, int danio, String portador) {
        this.nombre = nombre;
        this.alcance = alcance;
        this.ataques = ataques;
        this.precision = precision;
        this.fuerza = fuerza;
        this.perforacion = perforacion;
        this.danio = danio;
        this.portador = portador;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAlcance() {
        return alcance;
    }

    public int getAtaques() {
        return ataques;
    }

    public int getPrecision() {
        return precision;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getPerforacion() {
        return perforacion;
    }

    public int getDanio() {
        return danio;
    }

    public String getPortador() {
        return portador;
    }

    public String toString() {
        return "Nombre: " + nombre + alcance + ataques + precision + fuerza + perforacion + danio + portador;
    }

}
