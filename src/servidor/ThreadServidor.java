/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robson Gomes
 */
public class ThreadServidor extends Thread{
    Socket cSocket;   
    ObjectOutputStream EnviaClient;
    ObjectInputStream RecebClient;
    ArrayList<ThreadServidor> clientes;
    HandleCliente handleCliente;
    String nomeCliente = "";

    public ThreadServidor(Socket cSocket, ArrayList<ThreadServidor> clientes, HandleCliente handleCliente) {
        this.cSocket = cSocket;
        this.clientes = clientes;
        this.handleCliente = handleCliente;
    }
    
    @Override
    public void run() {
        
        try{     	            
            EnviaClient = new ObjectOutputStream(cSocket.getOutputStream());
            RecebClient = new ObjectInputStream(cSocket.getInputStream());	            
	        
            while (cSocket.isConnected()){
                
                Mensagem m = (Mensagem) RecebClient.readObject();		                
                
                if(m.inserir) {
                    System.out.println("Servidor recebeu: " + m.getFrom() + 
                                   " do cliente " + cSocket.getInetAddress() +
                                   " no ip local " + cSocket.getLocalAddress() +
                                   " - " + cSocket.getPort());	                    
                    this.nomeCliente = m.getFrom();   
                    handleCliente.inserir(clientes, this);                             
                    //print();
                } else if(m.isMensagemFoAll()) {
                    //Envia o conteúdo da mensagem a todos os usuários conectados
                    handleCliente.sendMessageForAll(clientes, m);
                    
                } else if(m.isMensagemForOne()) {
                    handleCliente.sendMessageForOne(clientes, m);
                }                                             
            }
            EnviaClient.close();
            RecebClient.close();
            cSocket.close();
            handleCliente.remover(clientes, this);            
            
        }
	catch(Exception e){
            handleCliente.remover(clientes, this);
            System.err.println("Cliente desconectado :" + e);
            //print();
            //System.err.println("Erro ThreadServidor :" + e);
            //System.exit(0);			
	}  
    }       
    
    
    //imprime os clientes conectados para efeito de testes
    public void print() {
        clientes.forEach((tc) -> {
            System.out.println(tc.nomeCliente);
        });
    }        
    
    //métodos de acesso aos dados de cada cliente
    
    public ObjectOutputStream getEnviaClient() {
        return EnviaClient;
    }

    public ObjectInputStream getRecebClient() {
        return RecebClient;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Socket getcSocket() {
        return cSocket;
    }
    
}
