/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.BaseDatos;

import java.util.Observable;

/**
 *
 * @author Geykel
 */
public class Coneccion extends Observable{

    public Coneccion(String host, int port, String SID, String user, String password) {
        this.host = host;
        this.port = port;
        this.SID = SID;
        this.user = user;
        this.password = password;
    }
    
    public Coneccion(String user, String password){
        host = "localhost";
        port = 1521;
        SID = "XE";
        this.user = user;
        this.password = password;
    }
    
    public String getUrl(){
        StringBuilder url = new StringBuilder("jdbc:oracle:thin:");
        url.append("@//").append(host).append(":").append(port).append("/").append(SID);
        return url.toString();
    }
    
    public void changeConnection(String host, int port, String SID, String user, String password){
        this.host = host;
        this.port = port;
        this.SID = SID;
        this.user = user;
        this.password = password;
        this.setChanged();
        this.notifyObservers();
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        this.setChanged();
        this.notifyObservers();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        this.setChanged();
        this.notifyObservers();
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
        this.setChanged();
        this.notifyObservers();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user, String password) {
        this.user = user;
        this.password = password;
        this.setChanged();
        this.notifyObservers();
    }

    public String getPassword() {
        return password;
    }

    /*public void setPassword(String password) {
        this.password = password;
        this.setChanged();
        this.notifyObservers();
    }*/
    
    private String host;
    private int port;
    private String SID;
    private String user;
    private String password;
}
