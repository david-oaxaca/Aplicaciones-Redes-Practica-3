package chat_multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.DefaultListModel;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 * 
 */
public class Receptor extends Thread{
    
    private MulticastSocket cl;
    private DefaultListModel<String> MsgListModel = new DefaultListModel<>();
    private RWLock chat_options = new RWLock();
    private boolean nuevo_usuario = true;
    
    public Receptor(MulticastSocket socket, DefaultListModel<String> ListModel){
        this.cl = socket;
        this.MsgListModel = ListModel;
    }
    
    public void run(){
        try{
            MulticastSocket cl = new MulticastSocket(7779);
            cl.setReuseAddress(true);
            
            InetAddress gpo = InetAddress.getByName("229.1.2.3");
            cl.joinGroup(gpo);
            
            DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
            cl.receive(p);
            int numero_usuarios= Integer.parseInt(new String(p.getData(),0,p.getLength()));
            String mensaje= "";
            for(int i= 0; i < numero_usuarios; i++) {
                cl.receive(p);
                mensaje= new String(p.getData(),0,p.getLength());
                chat_options.escribirMensaje(MsgListModel, mensaje);
            }
                    
            cl = new MulticastSocket(7778);
            cl.setReuseAddress(true);
            //listModel.addElement(“new item”);
            
            //InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            cl.joinGroup(gpo);
            System.out.println("Servicio iniciado y unido al grupo.. comienza escucha de mensajes");
            for(;;){
                cl.receive(p);
                //System.out.println("Datagrama multicast recibido desde "+p.getAddress()+":"+p.getPort()+"Con el mensaje:"+new String(p.getData(),0,p.getLength()));    
                //System.out.println(new String(p.getData(),0,p.getLength()));
                mensaje= new String(p.getData(), 0, p.getLength());
                chat_options.escribirMensaje(MsgListModel, mensaje);
                System.out.println("Mensaje recibido");
            }//for
          
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
