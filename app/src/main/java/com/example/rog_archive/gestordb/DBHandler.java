package com.example.rog_archive.gestordb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.rog_archive.clases.Arma;
import com.example.rog_archive.clases.Partida;
import com.example.rog_archive.clases.Personaje;

import java.util.ArrayList;

public class DBHandler {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBHandler(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Método para insertar un personaje en la base de datos
    public long insertarPersonaje(Personaje personaje) {
        ContentValues values = new ContentValues();
        values.put("nombre", personaje.getNombre());
        values.put("raza", personaje.getRaza());
        values.put("clase", personaje.getClase());
        values.put("nivel", personaje.getNivel());
        values.put("imagenUri", personaje.getImagenUri());
        values.put("usuarioUid", personaje.getUsuarioUid());
        values.put("estadisticas", personaje.getEstadisticas().toString());  // Convertir estadisticas a String
        values.put("salud", personaje.getSalud());

        // Insertar el personaje en la base de datos
        long personajeId = db.insert(DBHelper.TABLA_PERSONAJES, null, values);

        // Asignar el ID al personaje
        personaje.setId(personajeId);

        return personajeId;  // Retornar el ID del personaje insertado
    }

    // Obtener todos los personajes de un usuario
    public ArrayList<Personaje> obtenerPersonajesPorUsuario(String usuarioUid) {
        ArrayList<Personaje> personajes = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLA_PERSONAJES, null,
                "usuarioUid = ?", new String[]{usuarioUid}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String raza = cursor.getString(cursor.getColumnIndexOrThrow("raza"));
                String clase = cursor.getString(cursor.getColumnIndexOrThrow("clase"));
                int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
                String imagenUri = cursor.getString(cursor.getColumnIndexOrThrow("imagenUri"));
                String estadisticasString = cursor.getString(cursor.getColumnIndexOrThrow("estadisticas"));
                int salud = cursor.getInt(cursor.getColumnIndexOrThrow("salud"));  // Obtener salud

                // Convertir la cadena de estadísticas a una lista de enteros
                String[] estadisticasArray = estadisticasString.replace("[", "").replace("]", "").split(", ");
                ArrayList<Integer> estadisticas = new ArrayList<>();
                for (String stat : estadisticasArray) {
                    estadisticas.add(Integer.parseInt(stat));
                }

                // Crear el objeto Personaje
                Personaje personaje = new Personaje(nombre, raza, clase, nivel, imagenUri, usuarioUid);
                personaje.instertarEstadisticas(
                        estadisticas.get(0), estadisticas.get(1), estadisticas.get(2),
                        estadisticas.get(3), estadisticas.get(4), estadisticas.get(5), salud  // Usar salud y las otras estadísticas
                );

                personajes.add(personaje);
            }
            cursor.close();
        }
        return personajes;
    }

    // Método para eliminar un personaje específico asociado a un usuario
    public int eliminarPersonaje(String usuarioUid, String nombrePersonaje) {
        return db.delete(DBHelper.TABLA_PERSONAJES,
                "usuarioUid = ? AND nombre = ?",
                new String[]{usuarioUid, nombrePersonaje});
    }

    // Método para insertar una partida en la base de datos
    public long insertarPartida(Partida partida) {
        ContentValues values = new ContentValues();
        values.put("nombre", partida.getNombre());
        values.put("nJugadores", partida.getNJugadores());
        values.put("usuarioUid", partida.getUsuarioUid());
        values.put("descansos", partida.getDescansos());  // Agregar descansos
        values.put("recursos", partida.getRecursos());  // Agregar recursos

        // Insertar la partida en la base de datos
        long partidaId = db.insert(DBHelper.TABLA_PARTIDAS, null, values);

        // Asignar el ID de la partida al objeto
        partida.setId(partidaId);

        // Insertar los personajes en la tabla de relación usando el nombre en lugar de ID
        for (Personaje personaje : partida.getPersonajes()) {
            insertarPersonajeEnPartida(partida.getNombre(), personaje.getNombre());
        }

        return partidaId;
    }

    // Método para insertar la relación entre una partida y un personaje usando nombres
    public void insertarPersonajeEnPartida(String partidaNombre, String personajeNombre) {
        ContentValues values = new ContentValues();
        values.put("partida_nombre", partidaNombre);
        values.put("personaje_nombre", personajeNombre);

        db.insert(DBHelper.TABLA_PARTIDAS_PERSONAJES, null, values);
    }

    public Partida obtenerPartidaPorNombre(String nombrePartida) {
        Partida partida = null;
        Cursor cursor = db.query(DBHelper.TABLA_PARTIDAS, null,
                "nombre = ?", new String[]{nombrePartida}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            int nJugadores = cursor.getInt(cursor.getColumnIndexOrThrow("nJugadores"));
            String usuarioUid = cursor.getString(cursor.getColumnIndexOrThrow("usuarioUid"));
            int descansos = cursor.getInt(cursor.getColumnIndexOrThrow("descansos"));
            int recursos = cursor.getInt(cursor.getColumnIndexOrThrow("recursos"));

            // Crear el objeto Partida
            partida = new Partida(nombre, nJugadores, usuarioUid);
            partida.setDescansos(descansos);
            partida.setRecursos(recursos);

            // Obtener y asignar los personajes asociados a la partida
            ArrayList<Personaje> personajes = obtenerPersonajesDePartida(nombrePartida);
            for (Personaje p : personajes) {
                partida.aniadirUnPersonaje(p);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return partida; // Puede devolver null si no se encuentra la partida
    }


    // Obtener todas las partidas de un usuario
    public ArrayList<Partida> obtenerPartidasPorUsuario(String usuarioUid) {
        ArrayList<Partida> partidas = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLA_PARTIDAS, null,
                "usuarioUid = ?", new String[]{usuarioUid}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                int nJugadores = cursor.getInt(cursor.getColumnIndexOrThrow("nJugadores"));
                int descansos = cursor.getInt(cursor.getColumnIndexOrThrow("descansos"));
                int recursos = cursor.getInt(cursor.getColumnIndexOrThrow("recursos"));

                // Crear el objeto Partida
                Partida partida = new Partida(nombre, nJugadores, usuarioUid);
                partida.setDescansos(descansos);
                partida.setRecursos(recursos);

                // Obtener los personajes asociados con la partida
                ArrayList<Personaje> personajes = obtenerPersonajesDePartida(partida.getNombre());
                for (Personaje p : personajes) {
                    partida.aniadirUnPersonaje(p);
                }

                partidas.add(partida);
            }
            cursor.close();
        }
        return partidas;
    }

    // Obtener los personajes asociados a una partida usando nombre en lugar de ID
    public ArrayList<Personaje> obtenerPersonajesDePartida(String partidaNombre) {
        ArrayList<Personaje> personajes = new ArrayList<>();
        String query = "SELECT p.* FROM " + DBHelper.TABLA_PERSONAJES + " p " +
                "JOIN " + DBHelper.TABLA_PARTIDAS_PERSONAJES + " pp ON p.nombre = pp.personaje_nombre " +
                "WHERE pp.partida_nombre = ?";
        Cursor cursor = db.rawQuery(query, new String[]{partidaNombre});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String raza = cursor.getString(cursor.getColumnIndexOrThrow("raza"));
                String clase = cursor.getString(cursor.getColumnIndexOrThrow("clase"));
                int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
                String imagenUri = cursor.getString(cursor.getColumnIndexOrThrow("imagenUri"));
                String estadisticasString = cursor.getString(cursor.getColumnIndexOrThrow("estadisticas"));
                int salud = cursor.getInt(cursor.getColumnIndexOrThrow("salud"));  // Obtener salud

                // Convertir la cadena de estadísticas a una lista de enteros
                String[] estadisticasArray = estadisticasString.replace("[", "").replace("]", "").split(", ");
                ArrayList<Integer> estadisticas = new ArrayList<>();
                for (String stat : estadisticasArray) {
                    estadisticas.add(Integer.parseInt(stat));
                }

                // Crear el objeto Personaje
                Personaje personaje = new Personaje(nombre, raza, clase, nivel, imagenUri, ""); // Agregar la lógica de usuarioUid si es necesario
                personaje.instertarEstadisticas(
                        estadisticas.get(0), estadisticas.get(1), estadisticas.get(2),
                        estadisticas.get(3), estadisticas.get(4), estadisticas.get(5), salud  // Usar salud y las otras estadísticas
                );

                personajes.add(personaje);
            }
            cursor.close();
        }
        return personajes;
    }

    // Método para eliminar una partida específica asociada a un usuario
    public int eliminarPartida(String usuarioUid, String nombrePartida) {
        return db.delete(DBHelper.TABLA_PARTIDAS,
                "usuarioUid = ? AND nombre = ?",
                new String[]{usuarioUid, nombrePartida});
    }

    // Método para insertar un arma en la base de datos
    public void insertarArma(Arma arma) {
        ContentValues values = new ContentValues();
        values.put("nombre", arma.getNombre());
        values.put("alcance", arma.getAlcance());
        values.put("ataques", arma.getAtaques());
        values.put("precision", arma.getPrecision());
        values.put("fuerza", arma.getFuerza());
        values.put("perforacion", arma.getPerforacion());
        values.put("danio", arma.getDanio());
        values.put("portador", arma.getPortador());

        // Insertar el arma en la base de datos
        db.insert(DBHelper.TABLA_ARMAS, null, values);
    }

    // Método para eliminar un arma por su nombre
    public int eliminarArma(String nombreArma) {
        return db.delete("armas", "nombre = ?", new String[]{nombreArma});
    }

    // Método para obtener todas las armas asociadas a un personaje
    public ArrayList<Arma> obtenerArmasPorPersonaje(String nombrePersonaje) {
        ArrayList<Arma> armas = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLA_ARMAS + " WHERE portador = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombrePersonaje});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                int alcance = cursor.getInt(cursor.getColumnIndexOrThrow("alcance"));
                int ataques = cursor.getInt(cursor.getColumnIndexOrThrow("ataques"));
                int precision = cursor.getInt(cursor.getColumnIndexOrThrow("precision"));
                int fuerza = cursor.getInt(cursor.getColumnIndexOrThrow("fuerza"));
                int perforacion = cursor.getInt(cursor.getColumnIndexOrThrow("perforacion"));
                int danio = cursor.getInt(cursor.getColumnIndexOrThrow("danio"));
                String portador = cursor.getString(cursor.getColumnIndexOrThrow("portador"));

                // Crear el objeto Arma y agregarlo a la lista
                Arma arma = new Arma(nombre, alcance, ataques, precision, fuerza, perforacion, danio, portador);
                armas.add(arma);
            }
            cursor.close();
        }

        return armas;
    }

    // Método para modificar la salud de un personaje (sumar o restar según el valor de 'recupera')
    public void modificarSaludDePersonaje(String nombrePersonaje, int valor, boolean recupera) {
        // Obtener el personaje por su nombre
        Cursor cursor = db.query(DBHelper.TABLA_PERSONAJES, null,
                "nombre = ?", new String[]{nombrePersonaje}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Obtener la salud actual del personaje
            int saludActual = cursor.getInt(cursor.getColumnIndexOrThrow("salud"));

            // Modificar la salud dependiendo del valor de 'recupera'
            int nuevaSalud;
            if (recupera) {
                nuevaSalud = saludActual + valor;
            } else {
                // Si 'recupera' es false, restar el valor a la salud
                nuevaSalud = saludActual - valor;
            }

            // Asegurarse de que la salud no sea negativa
            if (nuevaSalud < 0) {
                nuevaSalud = 0;
            }

            // Actualizar la salud en la base de datos
            ContentValues values = new ContentValues();
            values.put("salud", nuevaSalud);

            db.update(DBHelper.TABLA_PERSONAJES, values,
                    "nombre = ?", new String[]{nombrePersonaje});
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void restarDescanso(String nombrePartida) {
        // Consultar los descansos actuales de la partida
        Cursor cursor = db.query(DBHelper.TABLA_PARTIDAS, new String[]{"descansos"},
                "nombre = ?", new String[]{nombrePartida}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int descansosActuales = cursor.getInt(cursor.getColumnIndexOrThrow("descansos"));
            cursor.close();

            // Restar 1 a los descansos, asegurándose de que no sea menor que 0
            int nuevosDescansos = Math.max(descansosActuales - 1, 0);

            // Actualizar la base de datos
            ContentValues values = new ContentValues();
            values.put("descansos", nuevosDescansos);
            db.update(DBHelper.TABLA_PARTIDAS, values, "nombre = ?", new String[]{nombrePartida});
        }
    }

    public boolean reponerDescansos(String nombrePartida) {
        boolean resultado = false;
        // Consultar los recursos actuales de la partida
        Cursor cursor = db.query(DBHelper.TABLA_PARTIDAS, new String[]{"recursos"},
                "nombre = ?", new String[]{nombrePartida}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int recursosActuales = cursor.getInt(cursor.getColumnIndexOrThrow("recursos"));

            // Verificar si los recursos son suficientes
            if (recursosActuales >= 25) {
                resultado = true;
                // Restar 25 a los recursos
                int nuevosRecursos = recursosActuales - 25;
                ContentValues recursosValues = new ContentValues();
                recursosValues.put("recursos", nuevosRecursos);
                db.update(DBHelper.TABLA_PARTIDAS, recursosValues, "nombre = ?", new String[]{nombrePartida});

                // Crear un objeto ContentValues para actualizar los descansos
                ContentValues descansosValues = new ContentValues();
                descansosValues.put("descansos", 2); // Establecer descansos a 2

                // Actualizar los descansos en la base de datos para la partida especificada
                db.update(DBHelper.TABLA_PARTIDAS, descansosValues, "nombre = ?", new String[]{nombrePartida});
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return resultado;
    }


    public void aniadirRecursos(String nombrePartida, int cantidad) {
        // Consultar los recursos actuales de la partida
        Cursor cursor = db.query(DBHelper.TABLA_PARTIDAS, new String[]{"recursos"},
                "nombre = ?", new String[]{nombrePartida}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int recursosActuales = cursor.getInt(cursor.getColumnIndexOrThrow("recursos"));
            cursor.close();

            // Sumar la cantidad especificada
            int nuevosRecursos = recursosActuales + cantidad;

            // Actualizar la base de datos
            ContentValues values = new ContentValues();
            values.put("recursos", nuevosRecursos);
            db.update(DBHelper.TABLA_PARTIDAS, values, "nombre = ?", new String[]{nombrePartida});
        }

        if (cursor != null) {
            cursor.close();
        }
    }

}
