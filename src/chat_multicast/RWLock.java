package chat_multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 * 
 */
public class RWLock {

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
  
    public RWLock(){
        
    }
    
    public void escribirServidor(MulticastSocket socket, String msj) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
 
        try {
            //list.add(element);
            
        } finally {
            writeLock.unlock();
        }
    }
 
    public void escribirMensaje(DefaultListModel<String> MsgListModel, String msj) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
 
        try {
            //return list.get(index);
            MsgListModel.addElement(msj);
            
        } finally {
            writeLock.unlock();
        }
    }
 
    
}
