/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robson Gomes
 */
public class HandleCliente {
    
    public synchronized void inserir(ArrayList<ThreadServidor> clientes, ThreadServidor tc) {
        boolean existe = false;
        for(ThreadServidor i : clientes) {
            if(i.cSocket.getPort() == tc.cSocket.getPort()) {
                existe = true;
            }
        }
        if(!existe) {
            clientes.add(tc);         
            sendUsersOnline(clientes);
        }
    }
    
    public synchronized void remover(ArrayList<ThreadServidor> clientes, ThreadServidor tc) {
        clientes.remove(tc);
        sendUsersOnline(clientes);        
    }
    
    public void sendUsersOnline(ArrayList<ThreadServidor> clientes) {        
        Mensagem m = new Mensagem();
        m.setGetUsersOnline(true);
        //gerando a lista de usuários online
        ArrayList<Mensagem> users = new ArrayList();
        clientes.forEach((tc) -> {
            //System.out.println(tc.nomeCliente);
            Mensagem user = new Mensagem();
            user.setFrom(tc.nomeCliente);
            user.setFromPort(tc.cSocket.getPort());
            users.add(user);
        });
        m.setUsersOnline(users);
        //enviado a lista para todos os clientes conectados
        clientes.forEach((tc) -> {
            try {                
                tc.getEnviaClient().writeObject(m);
                tc.getEnviaClient().reset();
            } catch (IOException ex) {
                Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void sendMessageForAll(ArrayList<ThreadServidor> clientes, Mensagem m) {
                
        //enviado a mensagem para todos os clientes conectados
        clientes.forEach((tc) -> {
            try {                
                tc.getEnviaClient().writeObject(m);
                tc.getEnviaClient().reset();
            } catch (IOException ex) {
                Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void sendMessageForOne(ArrayList<ThreadServidor> clientes, Mensagem m) {
        //enviado a mensagem para um cliente específico
        clientes.forEach((tc) -> {
            try {
                if(m.getToPort() == tc.getcSocket().getPort()) {
                    System.out.println("Encontro o destino no servidor: " + m.getToPort());
                    
                    Mensagem m2 = new Mensagem();
                    m2.setMensagemForOne(m.isMensagemForOne());
                    m2.setFrom(m.getFrom());
                    m2.setFromPort(m.getFromPort());
                    m2.setTo(m.getTo());
                    m2.setToPort(m.getToPort());
                    m2.setConteudo(m.getConteudo());
                    
                    tc.getEnviaClient().writeObject(m2);
                    tc.getEnviaClient().reset();
                }                    
            } catch (IOException ex) {
                Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
