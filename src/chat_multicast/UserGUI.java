package chat_multicast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 *
 * @author David Oaxaca
 * @author David Madrigal
 * 
 */
public class UserGUI extends JFrame implements ActionListener{
    
    private JLabel lbl_Titulo, lbl_SubTitulo;
    private JButton btn_Enviar, btn_Emoji;
    private JTextField txt_msj;
    private JLabel lbl_username;
    
    private DefaultListModel<String> MsgListModel = new DefaultListModel<>();
    private JList<String> msjList = new JList<>(MsgListModel);
    
    private RWLock chat_functions = new RWLock();
    MulticastSocket socket;
    
    
    private String nombreUsuario;
    
    public UserGUI() throws IOException, ClassNotFoundException{
        iniciarVentana("Practica 3");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                escribirMensaje(socket, nombreUsuario + " ha salido del chat");
            }
        });
        setVisible(true);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new UserGUI();
            
    }
    
    public void iniciarVentana(String nombre) throws IOException, ClassNotFoundException{
        this.setTitle(nombre);
        this.setSize(470, 430);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();
        this.setVisible(true);
        inicioChat();
        
    }
    
    
    public void addComponents() throws IOException, ClassNotFoundException{
        
        this.nombreUsuario = getUserName();
        
        lbl_username = new JLabel("Nombre de usuario: " + nombreUsuario);
        lbl_username.setBounds(150, 10, 400, 30);
        lbl_username.setVisible(true);
        this.add(lbl_username);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(30, 50, 390, 275);
        
        msjList.setVisibleRowCount(15);
        msjList.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        msjList.setLayout(new BorderLayout(3, 3));
        
        JScrollPane scrollPane = new JScrollPane(msjList);
        scrollPane.setViewportView(msjList);
        msjList.setLayoutOrientation(JList.VERTICAL);
        panel.add(scrollPane);
        
        this.add(panel);
        
        btn_Enviar = new JButton("Enviar");
        btn_Enviar.setBounds(330, 335, 90, 35);
        btn_Enviar.addActionListener(this);
        this.add(btn_Enviar);
        
        btn_Emoji = new JButton(":)");
        
        
        btn_Emoji.setBounds(275, 335, 50, 35);
        btn_Emoji.addActionListener(this);
        this.add(btn_Emoji);
        
        txt_msj = new JTextField();
        txt_msj.setBounds(30, 335, 240, 35);
        
        LineBorder lineBorder =new LineBorder(Color.white, 10, true);
        txt_msj.setBorder(lineBorder );
        
        this.add(txt_msj);
        
    }
    
    private String getUserName(){
        String nombre;
        do{
            nombre = JOptionPane.showInputDialog("Introduzca su nombre de usuario: ");
        }while(nombre == null || nombre.equals(""));
        
        return nombre;
    }
    
    private void inicioChat() throws IOException{
        socket = new MulticastSocket(7777);
        socket.setReuseAddress(true);
        socket.setTimeToLive(255);
        
        iniciarLectura(socket);
        escribirMensaje(socket, nombreUsuario + " se ha unido a la sala de chat");
    }
    
    private void escribirMensaje(MulticastSocket socket, String msj){
        new Emisor(socket, msj).start();
    }
    
    private void iniciarLectura(MulticastSocket socket){
        new Receptor(socket, this.MsgListModel).start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand();
        
        if(evento.equals("Enviar")){
            String mensaje_send = txt_msj.getText();
            txt_msj.setText(""); 
            //MsgListModel.addElement("Tu: " + mensaje_send);
            escribirMensaje(socket, nombreUsuario + " : " + mensaje_send);
            //chat_functions.enviarMensaje(nombreUsuario + ": " +mensaje_send);
        }
        
    }
}
