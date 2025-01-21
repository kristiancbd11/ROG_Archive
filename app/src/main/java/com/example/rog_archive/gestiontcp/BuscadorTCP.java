package com.example.rog_archive.gestiontcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class BuscadorTCP {
    private ArrayList<String> listaHosts = new ArrayList<>();
    private static final int BROADCAST_PORT = 9876; // Puerto usado para escuchar mensajes de broadcast

    public void buscarHost() {
        listaHosts.clear(); // Limpiar la lista de hosts previos

        DatagramSocket socket = null;

        try {
            // Crear un socket para escuchar en el puerto de broadcast
            socket = new DatagramSocket(BROADCAST_PORT);
            socket.setBroadcast(true);

            byte[] buffer = new byte[1024]; // Buffer para recibir los mensajes

            System.out.println("Esperando un mensaje de broadcast...");

            // Preparar un paquete para recibir datos
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Recibir un único mensaje (no bloquea indefinidamente)
            socket.receive(packet); // Bloquea hasta recibir un mensaje

            // Obtener el mensaje recibido
            String mensaje = new String(packet.getData(), 0, packet.getLength());
            String ipServidor = packet.getAddress().getHostAddress(); // Dirección IP del emisor

            System.out.println("Mensaje recibido: " + mensaje + " desde " + ipServidor);

            // Comprobar si el mensaje recibido es válido
            if (mensaje.equalsIgnoreCase("Servidor disponible")) {
                // Si no está en la lista, agregar la dirección IP
                if (!listaHosts.contains(ipServidor)) {
                    listaHosts.add(ipServidor);
                    System.out.println("Servidor encontrado: " + ipServidor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cerrar el socket si no es nulo
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    public ArrayList<String> getListaHosts() {
        return listaHosts;
    }

    public void setListaHosts(ArrayList<String> listaHosts) {
        this.listaHosts = listaHosts;
    }
}
