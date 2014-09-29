/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.EsquemaClasificacion;

import Modelo.Clasificacion.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author Geykel
 */
public class Usuarios extends Observable {
    public static Usuarios getInstance(){
        if(usuarios == null){
            usuarios = new Usuarios();
        }
        return usuarios;
    }
    
    public void agregarUsuario(Usuario u){
        contenedor.put(u.getHashCode(), u);
        last = u;
        this.setChanged();
        this.notifyObservers();
    }
    
    public void eliminarUsuario(Usuario u){
        this.last = contenedor.remove(u.getHashCode());
        this.setChanged();
        this.notifyObservers();
    }
    
    public Usuario getUsuario(Integer key){
        return contenedor.get(key);
    }
    
    public boolean existe(Usuario u){
        return (contenedor.get(u.getHashCode()) != null);
    }
    
    public ArrayList<Usuario> getUsuarios(){
        return new ArrayList(contenedor.values());
    }
    
    public Usuario getLast(){
        return last;
    }
    
    private Usuarios(){
        contenedor = new HashMap();
    }
    
    private static Usuarios usuarios = null;
    private HashMap<Integer,Usuario> contenedor;
    private Usuario last;
}
