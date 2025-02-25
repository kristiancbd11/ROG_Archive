package com.example.rog_archive.gestordb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "rogarchive.db";
    private static final int DATABASE_VERSION = 4; // Incrementar la versión a 4

    // Nombres de las tablas
    public static final String TABLA_PERSONAJES = "personajes";
    public static final String TABLA_PARTIDAS = "partidas";
    public static final String TABLA_PARTIDAS_PERSONAJES = "partidas_personajes";
    public static final String TABLA_ARMAS = "armas";

    // Sentencia SQL para crear la tabla "personajes" con los nuevos campos
    public static final String CREATE_TABLE_PERSONAJES = "CREATE TABLE " + TABLA_PERSONAJES + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT," +
            "raza TEXT," +
            "clase TEXT," +
            "nivel INTEGER," +
            "imagenUri TEXT," +
            "usuarioUid TEXT," +
            "estadisticas TEXT," +
            "salud INTEGER" + // Nueva columna
            ");";

    // Sentencia SQL para crear la tabla "partidas" con los nuevos campos
    public static final String CREATE_TABLE_PARTIDAS = "CREATE TABLE " + TABLA_PARTIDAS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT NOT NULL," +
            "nJugadores INTEGER NOT NULL," +
            "usuarioUid TEXT NOT NULL," +
            "descansos INTEGER DEFAULT 2," +  // Nueva columna
            "recursos INTEGER DEFAULT 0" +  // Nueva columna
            ");";

    // Sentencia SQL para crear la tabla "partidas_personajes" para la relación entre partidas y personajes
    public static final String CREATE_TABLE_PARTIDAS_PERSONAJES = "CREATE TABLE " + TABLA_PARTIDAS_PERSONAJES + " (" +
            "partida_nombre TEXT," +  // Cambiar a nombre de la partida
            "personaje_nombre TEXT," +  // Cambiar a nombre del personaje
            "FOREIGN KEY(partida_nombre) REFERENCES " + TABLA_PARTIDAS + "(nombre)," +
            "FOREIGN KEY(personaje_nombre) REFERENCES " + TABLA_PERSONAJES + "(nombre)," +
            "PRIMARY KEY(partida_nombre, personaje_nombre)" + // Relación muchos a muchos
            ");";

    // Sentencia SQL para crear la tabla "armas"
    public static final String CREATE_TABLE_ARMAS = "CREATE TABLE " + TABLA_ARMAS + " (" +
            "nombre TEXT PRIMARY KEY," +
            "alcance INTEGER," +
            "ataques INTEGER," +
            "precision INTEGER," +
            "fuerza INTEGER," +
            "perforacion INTEGER," +
            "danio INTEGER," +
            "portador TEXT" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas cuando la base de datos es creada
        db.execSQL(CREATE_TABLE_PERSONAJES);
        db.execSQL(CREATE_TABLE_PARTIDAS);
        db.execSQL(CREATE_TABLE_PARTIDAS_PERSONAJES);
        db.execSQL(CREATE_TABLE_ARMAS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {  // Si la versión anterior es menor a 5, agregar la nueva tabla
            db.execSQL(CREATE_TABLE_ARMAS);  // Crear la nueva tabla de armas
        }

        // Realizar la migración si la versión de la base de datos cambia
        if (oldVersion < 4) {
            // Añadir las nuevas columnas "descansos" y "recursos" en la tabla "partidas"
            db.execSQL("ALTER TABLE " + TABLA_PARTIDAS + " ADD COLUMN descansos INTEGER DEFAULT 2;");
            db.execSQL("ALTER TABLE " + TABLA_PARTIDAS + " ADD COLUMN recursos INTEGER DEFAULT 0;");
        }

        // Eliminar las tablas anteriores y crear nuevas si es necesario
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERSONAJES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PARTIDAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PARTIDAS_PERSONAJES);
        db.execSQL("DROP TABLE IF EXISTS armas");  // Eliminar la tabla de armas si existe
        onCreate(db); // Volver a crear las tablas
    }

}
