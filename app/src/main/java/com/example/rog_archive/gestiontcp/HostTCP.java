package com.example.rog_archive.gestiontcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HostTCP {
    private static final int BROADCAST_PORT = 9876;
    private static final int PAUSA_ENTRE_ENVIO = 2000; // Pausa entre envíos en milisegundos

    public void iniciarBroadcast() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);

            String mensaje = "Servidor disponible"; // Mensaje que enviará el servidor
            byte[] buffer = mensaje.getBytes();

            // Dirección de broadcast
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            while (true) { // Loop infinito para emitir continuamente
                // Crear y enviar el paquete de broadcast
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, BROADCAST_PORT);
                socket.send(packet);
                System.out.println("Mensaje de broadcast enviado");

                // Pausa entre envíos
                Thread.sleep(PAUSA_ENTRE_ENVIO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
