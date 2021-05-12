package chat_multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 * 
 */
public class Emisor extends Thread{
    
    private MulticastSocket socket;
    private String msj;
    private RWLock chat_options;
    
    public Emisor(MulticastSocket s, String mensaje){
        this.socket = s;
        this.msj = mensaje;
    }
    
    public void run(){
        //InetAddress gpo;
            try {
                InetAddress gpo = InetAddress.getByName("229.1.2.3");
                //gpo = InetAddress.getByName("ff3e:40:2001::1");
                byte[] b = msj.getBytes();
                System.out.println("Mensaje enviado...");
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,7777);
                socket.send(p);
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException ie){}
            } catch (UnknownHostException ex) {
                Logger.getLogger(Emisor.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(Emisor.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
    
}
