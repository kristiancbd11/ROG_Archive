package com.example.rog_archive.gestionficheros;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestorFicheros {

    public void guardarPersonaje(Context context, Personaje pj) {
        try {
            // Guardar personaje en modo privado (sobrescribe el archivo si ya existe)
            FileOutputStream fos = context.openFileOutput("personajes.dat", Context.MODE_APPEND);
            ObjectOutputStream oos;

            if (new File(context.getFilesDir(), "personajes.dat").length() == 0) {
                // Si el archivo está vacío o no existe, inicializamos el ObjectOutputStream normalmente.
                oos = new ObjectOutputStream(fos);
            } else {
                // Si el archivo ya tiene contenido, usamos una subclase para evitar sobrescribir el encabezado.
                oos = new AppendableObjectOutputStream(fos);
            }

            oos.writeObject(pj);
            oos.close();
            fos.close();

            // Feedback al usuario
            Toast.makeText(context, "Personaje guardado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el personaje", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarPartida(Context context, Partida partida) {
        try {
            FileOutputStream fos = context.openFileOutput("partidas.dat", Context.MODE_APPEND);
            ObjectOutputStream oos;

            if (new File(context.getFilesDir(), "partidas.dat").length() == 0) {
                oos = new ObjectOutputStream(fos);
            } else {
                oos = new AppendableObjectOutputStream(fos);
            }

            oos.writeObject(partida);
            oos.close(); // Cierra el ObjectOutputStream
            fos.close(); // Cierra el FileOutputStream

            Toast.makeText(context, "Partida guardada con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar la partida", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Personaje> cargarPersonajes(Context context) {
        ArrayList<Personaje> personajes = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput("personajes.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                try {
                    Personaje personaje = (Personaje) ois.readObject();
                    personajes.add(personaje);
                } catch (EOFException eof) {
                    // Fin del archivo, salimos del bucle
                    break;
                }
            }

            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personajes;
    }

    public ArrayList<Partida> cargarPartidas(Context context) {
        ArrayList<Partida> partidas = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput("partidas.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                try {
                    Partida partida = (Partida) ois.readObject();
                    Log.d("GestorFicheros", "Cargando partida: " + partida.getNombre() + " - Jugadores: " + partida.getNJugadores());
                    partidas.add(partida);
                } catch (EOFException eof) {
                    // Fin del archivo, salimos del bucle
                    break;
                }
            }

            ois.close();
            fis.close();
        } catch (Exception e) {
            Log.e("GestorFicheros", "Error al cargar las partidas", e);
        }
        Log.d("GestorFicheros", "Total partidas cargadas: " + partidas.size());
        return partidas;
    }

    public void eliminarPersonaje(Context context, String nombrePersonaje) {
        try {
            // Cargar todos los personajes del archivo
            ArrayList<Personaje> personajes = cargarPersonajes(context);

            // Buscar y eliminar el personaje con el nombre especificado
            boolean personajeEliminado = false;
            for (int i = 0; i < personajes.size(); i++) {
                if (personajes.get(i).getNombre().equalsIgnoreCase(nombrePersonaje)) {
                    personajes.remove(i);
                    personajeEliminado = true;
                    break;
                }
            }

            if (personajeEliminado) {
                // Reescribir el archivo con los personajes restantes
                FileOutputStream fos = context.openFileOutput("personajes.dat", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                for (Personaje personaje : personajes) {
                    oos.writeObject(personaje);
                }

                oos.close();
                fos.close();

                // Confirmación al usuario
                Toast.makeText(context, "Personaje eliminado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                // Si no se encontró el personaje
                Toast.makeText(context, "Personaje no encontrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al eliminar el personaje", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarPartida(Context context, String nombrePartida) {
        try {
            // Cargar todas las partidas del archivo
            ArrayList<Partida> partidas = cargarPartidas(context);

            // Buscar y eliminar la partida con el nombre especificado
            boolean partidaEliminada = false;
            for (int i = 0; i < partidas.size(); i++) {
                if (partidas.get(i).getNombre().equalsIgnoreCase(nombrePartida)) {
                    partidas.remove(i);
                    partidaEliminada = true;
                    break;
                }
            }

            if (partidaEliminada) {
                // Reescribir el archivo con las partidas restantes
                FileOutputStream fos = context.openFileOutput("partidas.dat", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                for (Partida partida : partidas) {
                    oos.writeObject(partida);
                }

                oos.close();
                fos.close();

                // Confirmación al usuario
                Toast.makeText(context, "Partida eliminada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                // Si no se encontró la partida
                Toast.makeText(context, "Partida no encontrada", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al eliminar la partida", Toast.LENGTH_SHORT).show();
        }
    }
}