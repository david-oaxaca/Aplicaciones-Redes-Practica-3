package chat_multicast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 *
 */
public class Servidor {

    static final int PUERTO_SERVIDOR = 7777;
    static final int PUERTO_CLIENTE_CHAT = 7778;
    static final int PUERTO_CLIENTE_USUARIOS = 7779;
    static final String MULTICAST_ADDRESS = "229.1.2.3";
    static final int MAX_BITS_DATAGRAM = 65535;

    public static void main(String[] args) {
        ArrayList<String> usuariosOnline = new ArrayList();
        try {
            MulticastSocket cl = new MulticastSocket(PUERTO_SERVIDOR);
            cl.setReuseAddress(true);
            InetAddress gpo = InetAddress.getByName(MULTICAST_ADDRESS);
            //InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            cl.joinGroup(gpo);
            System.out.println("Servidor iniciado, recibiendo mensajes...");

            String usuario = "";
            
            for (;;) {
                DatagramPacket recibo = new DatagramPacket(new byte[MAX_BITS_DATAGRAM], MAX_BITS_DATAGRAM);
                cl.receive(recibo);
                String mensaje = new String(recibo.getData(), 0, recibo.getLength());
                System.out.println("Datagrama multicast recibido desde " + recibo.getAddress() + ":" + recibo.getPort() + " con el mensaje: " + mensaje);

                /* Tenemos que en todos los mensajes antes de un espacio recibimos el remitente */
                usuario = (mensaje.split(" "))[0];

                if (mensaje.contains("ha salido del chat")) {
                    usuariosOnline.remove(usuario);
                    System.out.println("Usuarios: " + usuariosOnline.toString());
                } else if (!usuariosOnline.contains(usuario)) {
                    usuariosOnline.add(usuario);
                    System.out.println("Usuarios: " + usuariosOnline.toString());
                }
                /* Checamos si se unio un nuevo usuario */
                if (mensaje.contains("se ha unido a la sala de chat")) {
                    new EmisorServidor(usuariosOnline, gpo, PUERTO_CLIENTE_USUARIOS, true).start();
                }
                new EmisorServidor(mensaje, gpo, PUERTO_CLIENTE_CHAT, false).start();
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
