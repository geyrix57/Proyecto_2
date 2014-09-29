/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.BaseDatos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Geykel
 */
public class DataBase extends Observable implements Observer{

    private DataBase(){
        info = new Coneccion("sys as sysdba", "root");
        conectado = false;
    }
    
    public static DataBase getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataBase();
            INSTANCE.getConeccionInfo().addObserver(INSTANCE);
        }
        return INSTANCE;
    }
    
    public boolean conectar() throws SQLException{
            if(conectado == false){
                con = DriverManager.getConnection(info.getUrl(),info.getUser(),info.getPassword());
                conectado = true;
                this.setChanged();
                this.notifyObservers();
            }
        return conectado;
    }
    
    public boolean isConnected(){
        return conectado;
    }
    
    public void close() throws SQLException{
            if(conectado = true && con != null){
                conectado = false;
                this.setChanged();
                this.notifyObservers();
                con.close();  
            }
    }
    
    public Coneccion getConeccionInfo(){
        return info;
    }
    
    public ResultSet ExecuteQuery(String sql) throws SQLException{
        ResultSet resp = null;
            if(this.conectado == true){
                Statement stm = con.createStatement();
                resp = stm.executeQuery(sql);
            }
        return resp;
    }
    
    public void setConnection(String host, int port, String SID, String user, String password){
        info.changeConnection(host, port, SID, user, password);
    }
    
    //viene predeterminada para localhost XE
    public void changeUser(String user, String password){
        info.setUser(user, password);
    }

    @Override
    public void update(Observable o, Object arg) {
            if(conectado == true){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
                }
                conectado = false;
                this.setChanged();
                this.notifyObservers();
            }
        try {
            this.conectar();//al cambiar la informacion del servidor se desconecta
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private static DataBase INSTANCE = null;
    private boolean conectado;
    private Coneccion info;
    private Connection con;
}
