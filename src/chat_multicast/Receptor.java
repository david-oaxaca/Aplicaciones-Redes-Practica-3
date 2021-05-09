/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public Receptor(MulticastSocket socket, DefaultListModel<String> ListModel){
        this.cl = socket;
        this.MsgListModel = ListModel;
    }
    
    public void run(){
        try{
            MulticastSocket cl = new MulticastSocket(7778);
            cl.setReuseAddress(true);
            //listModel.addElement(“new item”);
            //InetAddress gpo = InetAddress.getByName("229.1.2.3");
            InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            cl.joinGroup(gpo);
            System.out.println("Servicio iniciado y unido al grupo.. comienza escucha de mensajes");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                cl.receive(p);
                //System.out.println("Datagrama multicast recibido desde "+p.getAddress()+":"+p.getPort()+"Con el mensaje:"+new String(p.getData(),0,p.getLength()));    
                //System.out.println(new String(p.getData(),0,p.getLength()));
                chat_options.escribirMensaje(MsgListModel, new String(p.getData(),0,p.getLength()));
                System.out.println("Mensaje recibido");
            }//for
          
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
