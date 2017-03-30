/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Mensagem;

/**
 *
 * @author Robson Gomes
 */
public class ThreadCliente extends Thread{
    JFrameChat jFrameChat;        
    ArrayList<Mensagem> usersOnline = new ArrayList();
    ArrayList<JFrameChatPrivate> chatsPrivate = new ArrayList();    
    Socket cSocket;
    ObjectOutputStream EnviaServ;
    ObjectInputStream RecebServ;
    String usuario;

    public ThreadCliente() {}

    public ThreadCliente(JFrameChat jFrameChat, Socket cSocket, ObjectOutputStream EnviaServ, ObjectInputStream RecebServ, String usuario) {
        this.jFrameChat = jFrameChat;
        this.cSocket = cSocket;
        this.EnviaServ = EnviaServ;
        this.RecebServ = RecebServ;
        this.usuario = usuario;           
    }      
    
    @Override
    public void run() {
         try{     	            
            newCliente();
            
            while(cSocket.isConnected()) {
                
                Mensagem m = (Mensagem) RecebServ.readObject();		
                if(m.isGetUsersOnline()) {
                    setUsersOnline(m.getUsersOnline());
                    
                } else if(m.isMensagemFoAll()) {
                    newMessage(m);
                    
                } else if(m.isMensagemForOne()) {                    
                    //recebendo uma mensagem 
                    Mensagem newChat = new Mensagem();    
                    newChat.setMensagemForOne(m.isMensagemForOne());                   
                    newChat.setConteudo(m.getConteudo());
                    

                    newChat.setTo(m.getFrom());
                    newChat.setToPort(m.getFromPort());                    
                    
                    newChat.setFrom(m.getTo());
                    newChat.setFromPort(m.getToPort());
                    
                    if(!existeChat(newChat)) {                                                                                                
                        JFrameChatPrivate jFCPrivate = new JFrameChatPrivate(this, newChat);
                        inserirChatsPrivate(jFCPrivate);            
                        jFCPrivate.setTitle(m.getFrom() + " - " + m.getFromPort());
                        jFCPrivate.setVisible(true);
                    }
                    m.setToPort(newChat.getToPort());
                    m.setFromPort(newChat.getFromPort());
                    newMessagePrivate(m);
                }
                
            }
            EnviaServ.close();
            RecebServ.close();
            cSocket.close();
            
        }
	catch(Exception e){            
            System.err.println("Erro ThreadServidor :" + e);
            //System.exit(0);			
	}  
    }
    
    public void newCliente() {
        System.out.println("Setando o nome do novo cliente!");
        Mensagem m = new Mensagem();
        m.setInserir(true);
        m.setFrom(usuario);
        m.setFromPort(cSocket.getPort());
        try {
            EnviaServ.writeObject(m);
            EnviaServ.reset();
        } catch (IOException ex) {
            Logger.getLogger(JFrameChat.class.getName()).log(Level.SEVERE, null, ex);
        }                        
    }
    
    public void setUsersOnline(ArrayList<Mensagem> users) {        
        String[] nomes = new String[users.size()];
        //System.out.println("Online: ");
        int i = 0;
        for(Mensagem user : users) {           
            user.setPosicaoLista(i);            
            
            nomes[i] = user.getFrom();            
            i++;
            
            //System.out.println(user.getFrom() + " - " + user.getFromPort());            
        }
        usersOnline = users;        
        jFrameChat.setUsersOnline(nomes);
    }
    
    public void newMessage(Mensagem m) {
        jFrameChat.newMessage(m.getConteudo(), m.getFrom(), m.getFromPort());
    }
    
    public void newMessagePrivate(Mensagem m) {
        for(JFrameChatPrivate chat : chatsPrivate) {
            if(m.getToPort() == chat.getData().getToPort() && m.getFromPort() == chat.getData().getFromPort()) {
//                System.out.println("From : " + m.getFrom()); 
//                System.out.println("To : " + m.getTo());                            
                chat.newMessage(m.getConteudo(), m.getFrom(), m.getFromPort());
            }
        }
    }

    public ArrayList<Mensagem> getUsersOnline() {
        return usersOnline;
    }   

    public Socket getcSocket() {
        return cSocket;
    }

    public ObjectOutputStream getEnviaServ() {
        return EnviaServ;
    }

    public ObjectInputStream getRecebServ() {
        return RecebServ;
    }            
    
    //precisa de exclusão mútua
    public void inserirChatsPrivate(JFrameChatPrivate j) {
        chatsPrivate.add(j);
    }
    
    public void removerChatsPrivate(JFrameChatPrivate j) {
        chatsPrivate.remove(j);
    }
    
    public boolean existeChat(Mensagem m) {        
        for(JFrameChatPrivate chat : chatsPrivate) {            
            if(m.getToPort() == chat.getData().getToPort() && m.getFromPort() == chat.getData().getFromPort()) {
                return true;
            }
        }
        return false;
    }
    
    public void maximizeFormChatPrivate(Mensagem m) {
        for(JFrameChatPrivate chat : chatsPrivate) {
            if(m.getToPort() == chat.getData().getToPort() && m.getFromPort() == chat.getData().getFromPort()) {
                chat.toFront();
            }
        }
    }
}
