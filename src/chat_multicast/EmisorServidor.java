package chat_multicast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author David Madrigal Buendía
 */
public class EmisorServidor extends Thread {
    private Object objeto;
    private InetAddress gpo;
    private int puerto;
    private boolean esObjeto;

    public EmisorServidor( Object mensaje, InetAddress gpo, int puerto, boolean esObjeto) {
        this.objeto = mensaje;
        this.gpo = gpo;
        this.puerto = puerto;
        this.esObjeto= esObjeto;
    }

    public void run() {
        try {
            if(!esObjeto) {
                String mensaje= (String) objeto;
                enviarMensaje(mensaje, gpo, puerto);
            }else {
                enviarMensaje(objeto, gpo, puerto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * El método se encarga de enviar un mensaje a un destinatario enviandolo al
     * puerto y grupo determinado ya que hace uso de multicast y lo envía a
     * través de un datagrama.
     *
     * @param mensaje   Mensaje a enviar
     * @param gpo       Grupo de multicast de escucha
     * @param puerto    El puerto al que se envía el multicast del mensaje
     */
    public void enviarMensaje(String mensaje, InetAddress gpo, int puerto) throws Exception {
        byte[] b = mensaje.getBytes();
        DatagramPacket envio = new DatagramPacket(b, b.length, gpo, puerto);

        MulticastSocket socket = new MulticastSocket(puerto);
        socket.setReuseAddress(true);
        socket.setTimeToLive(255);
        socket.send(envio);
    }

    /**
     * El método se encarga de enviar un mensaje a un destinatario enviandolo al
     * puerto y grupo determinado ya que hace uso de multicast y lo envía a
     * través de un datagrama.
     *
     * @param mensaje   Enviara un mensaje en forma de objeto
     * @param gpo       Grupo de multicast de escucha
     * @param puerto    El puerto al que se envía el multicast del mensaje
     */
    public void enviarMensaje(Object mensaje, InetAddress gpo, int puerto) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(mensaje);
            out.flush();
            byte[] b = bos.toByteArray();
            DatagramPacket envio = new DatagramPacket(b, b.length, gpo, puerto);

            MulticastSocket socket = new MulticastSocket(puerto);
            socket.setReuseAddress(true);
            socket.setTimeToLive(255);
            socket.send(envio);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}