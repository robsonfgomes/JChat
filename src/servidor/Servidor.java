/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Robson Gomes
 */
public class Servidor {
    
    static ArrayList<ThreadServidor> clientes = new ArrayList();
    static HandleCliente handleCliente = new HandleCliente();
    
    public static void main(String[] args) {
        
        ServerSocket sSocket = null;
        Socket cSocket;        
        try {
            sSocket = new ServerSocket(7000);
        } catch (IOException e) {
            System.err.println("Servidor nao pode ouvir a porta: 7000 ");
            //System.exit(0);
        }
        
        while(true) {
            try{
	        System.out.println("\n\nEsperando cliente:"+
                                    "Endereço IP do servidor: " + sSocket.getInetAddress());
                cSocket = sSocket.accept();                
                ThreadServidor t = new ThreadServidor(cSocket, clientes, handleCliente);
                t.start();
                //handleCliente.inserir(clientes, t);                             

            }catch(IOException e){
                //System.err.println("Erro Servidor :"+e);
                System.err.println("Cliente desconectado :" + e);
                //System.exit(0);	  	
            }
        }
    }
}
