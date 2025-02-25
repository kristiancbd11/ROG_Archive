package com.example.rog_archive.pdfgenerator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.example.rog_archive.clases.Personaje;
import com.example.rog_archive.clases.Arma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PDFGenerator {

    public static void generarPDF(Personaje personaje, ArrayList<Arma> armas, android.content.Context context) {
        // Crear el documento PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Definir la página en formato A4 (595x842)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Configurar el estilo del texto
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);

        // Agregar título
        canvas.drawText("Ficha del Personaje", 50, 50, paint);

        if (personaje != null) {
            int y = 100;

            // Información del personaje
            canvas.drawText("Nombre: " + personaje.getNombre(), 50, y, paint);
            y += 30;
            canvas.drawText("Raza: " + personaje.getRaza(), 50, y, paint);
            y += 30;
            canvas.drawText("Clase: " + personaje.getClase(), 50, y, paint);
            y += 30;
            canvas.drawText("Nivel: " + personaje.getNivel(), 50, y, paint);
            y += 30;
            canvas.drawText("Salud: " + personaje.getSalud(), 50, y, paint);
            y += 50;

            // Estadísticas del personaje
            ArrayList<Integer> estadisticas = personaje.getEstadisticas();
            if (estadisticas != null && estadisticas.size() >= 6) {
                canvas.drawText("Estadísticas:", 50, y, paint);
                y += 30;
                canvas.drawText("M: " + estadisticas.get(0), 50, y, paint);
                y += 30;
                canvas.drawText("R: " + estadisticas.get(1), 50, y, paint);
                y += 30;
                canvas.drawText("S: " + estadisticas.get(2), 50, y, paint);
                y += 30;
                canvas.drawText("H: " + estadisticas.get(3), 50, y, paint);
                y += 30;
                canvas.drawText("L: " + estadisticas.get(4), 50, y, paint);
                y += 30;
                canvas.drawText("CO: " + estadisticas.get(5), 50, y, paint);
                y += 50;
            }

            // Armas del personaje
            if (armas != null && !armas.isEmpty()) {
                canvas.drawText("Armas:", 50, y, paint);
                y += 30;
                for (Arma arma : armas) {
                    String armaTexto = arma.getNombre() + " || AL: " + arma.getAlcance() + " || AT: " + arma.getAtaques() +
                            " || PRE: " + arma.getPrecision() + " || F: " + arma.getFuerza() + " || PRF: " + arma.getPerforacion() +
                            " || D: " + arma.getDanio();
                    canvas.drawText(armaTexto, 50, y, paint);
                    y += 30;
                }
            }
        }

        pdfDocument.finishPage(page);

        // Guardar el archivo PDF en el directorio de Descargas
        String nombrePdf = "Personaje_" + personaje.getNombre() + ".pdf";
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + nombrePdf;
        File file = new File(filePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF guardado en: " + filePath, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();
    }
}
