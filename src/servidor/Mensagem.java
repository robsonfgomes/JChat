/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Robson Gomes
 */
public class Mensagem implements Serializable{    
    //inserir
    protected boolean inserir;    
    protected String from;
    protected int fromPort;
    
    protected boolean remover;     
        
    protected boolean mensagemFoAll;
    protected boolean mensagemForOne;        
    protected String Conteudo;       
        
    protected String to;
    protected int toPort;
    
    protected boolean getUsersOnline;
    
    protected int posicaoLista;
    
    ArrayList<Mensagem> usersOnline;
    

    public Mensagem() {}

    public String getConteudo() {
        return Conteudo;
    }

    public void setConteudo(String Conteudo) {
        this.Conteudo = Conteudo;
    }        

    public boolean isInserir() {
        return inserir;
    }

    public void setInserir(boolean inserir) {
        this.inserir = inserir;
    }

    public boolean isRemover() {
        return remover;
    }

    public void setRemover(boolean remover) {
        this.remover = remover;
    }   

    public boolean isMensagemFoAll() {
        return mensagemFoAll;
    }

    public void setMensagemFoAll(boolean mensagemFoAll) {
        this.mensagemFoAll = mensagemFoAll;
    }

    public boolean isMensagemForOne() {
        return mensagemForOne;
    }

    public void setMensagemForOne(boolean mensagemForOne) {
        this.mensagemForOne = mensagemForOne;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getFromPort() {
        return fromPort;
    }

    public void setFromPort(int fromPort) {
        this.fromPort = fromPort;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getToPort() {
        return toPort;
    }

    public void setToPort(int toPort) {
        this.toPort = toPort;
    }

    public boolean isGetUsersOnline() {
        return getUsersOnline;
    }

    public void setGetUsersOnline(boolean getOnline) {
        this.getUsersOnline = getOnline;
    }        

    public ArrayList<Mensagem> getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(ArrayList<Mensagem> usersOnline) {
        this.usersOnline = usersOnline;
    }

    public int getPosicaoLista() {
        return posicaoLista;
    }

    public void setPosicaoLista(int posicaoLista) {
        this.posicaoLista = posicaoLista;
    }        
        
}
