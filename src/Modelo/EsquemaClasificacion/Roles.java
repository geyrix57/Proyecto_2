/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.EsquemaClasificacion;

import Modelo.Clasificacion.Rol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author Geykel
 */
public class Roles extends Observable{
    public static Roles getInstance(){
        if(roles == null){
            roles = new Roles();
        }
        return roles;
    }
    
    public void agregarRol(Rol rol){
        contenedor.put(rol.getHashCode(), rol);
        last = rol;
        this.setChanged();
        this.notifyObservers();
    }
    
    public void eliminarRol(Rol rol){
        this.last = contenedor.remove(rol.getHashCode());
        this.setChanged();
        this.notifyObservers();
    }
    
    public Rol getRol(Integer key){
        return contenedor.get(key);
    }
    
    public boolean existe(Rol rol){
        return (contenedor.get(rol.getHashCode()) != null);
    }
    
    public ArrayList<Rol> getRoles(){
        return new ArrayList(contenedor.values());
    }
    
    public Rol getLast(){
        return last;
    }
    
    private Roles(){
        contenedor = new HashMap();
    }
    
    private static Roles roles = null;
    private HashMap<Integer,Rol> contenedor;
    private Rol last;
}
