/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_multicast;

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
    public static void main(String[] args){
        ArrayList <String> usuariosOnline = new ArrayList();
        try{
            MulticastSocket cl = new MulticastSocket(7777);
            cl.setReuseAddress(true);
            //InetAddress gpo = InetAddress.getByName("229.1.2.3");
            InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            cl.joinGroup(gpo);
            System.out.println("Servidor iniciado, recibiendo mensajes...");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                cl.receive(p);
                System.out.println("Datagrama multicast recibido desde "+p.getAddress()+":"+p.getPort()+"Con el mensaje:"+new String(p.getData(),0,p.getLength()));    
            }//for
          
        }catch(Exception e){
            e.printStackTrace();
        }
    } 
}
