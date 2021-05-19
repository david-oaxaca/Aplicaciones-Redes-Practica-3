package chat_multicast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 *
 */
public class Receptor extends Thread {

    private MulticastSocket cl;
    private DefaultListModel<String> MsgListModel = new DefaultListModel<>();
    private RWLock chat_options = new RWLock();

    public Receptor(MulticastSocket socket, DefaultListModel<String> ListModel) {
        this.cl = socket;
        this.MsgListModel = ListModel;
    }

    public void run() {
        try {
            MulticastSocket cl = new MulticastSocket(7779);
            cl.setReuseAddress(true);

            InetAddress gpo = InetAddress.getByName("229.1.2.3");
            cl.joinGroup(gpo);

            DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
            cl.receive(p);
            ByteArrayInputStream bais = new ByteArrayInputStream(p.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ArrayList<String> usuarios = (ArrayList<String>) ois.readObject();
            System.out.println(usuarios.toString());
            usuarios.forEach((usuario) -> {
                chat_options.escribirMensaje(MsgListModel, usuario + " se ha unido a la sala de chat");
            });

            cl = new MulticastSocket(7778);
            cl.setReuseAddress(true);
            //listModel.addElement(“new item”);

            //InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            cl.joinGroup(gpo);
            System.out.println("Servicio iniciado y unido al grupo.. comienza escucha de mensajes");
            String mensaje = "";
            for (;;) {
                cl.receive(p);
                //System.out.println("Datagrama multicast recibido desde "+p.getAddress()+":"+p.getPort()+"Con el mensaje:"+new String(p.getData(),0,p.getLength()));    
                //System.out.println(new String(p.getData(),0,p.getLength()));
                mensaje = new String(p.getData(), 0, p.getLength());
                chat_options.escribirMensaje(MsgListModel, mensaje);
                System.out.println("Mensaje recibido");
            }//for

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
