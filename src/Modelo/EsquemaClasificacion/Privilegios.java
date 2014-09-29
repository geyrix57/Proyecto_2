/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.EsquemaClasificacion;

import Modelo.Clasificacion.Permiso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;


/**
 *
 * @author Geykel
 */
public class Privilegios extends Observable {
    
    public static Privilegios getInstance(){
        if(priv == null){
            priv = new Privilegios();
        }
        return priv;
    }
    
    public void agregarPermiso(Permiso perm){
        contenedor.put(perm.getHashCode(), perm);
        last = perm;
        this.setChanged();
        this.notifyObservers();
    }
    
    public void eliminarPermiso(Permiso perm){
        last = contenedor.remove(perm.getHashCode());
        this.setChanged();
        this.notifyObservers();
    }
    
    public Permiso getPermiso(Integer key){
        return contenedor.get(key);
    }
    
    public boolean existe(Permiso perm){
        return contenedor.get(perm.getHashCode()) != null;
    }
    
    public ArrayList<Permiso> getPermisos(){
        return new ArrayList(contenedor.values());
    }
    
    public Permiso getLast(){
        return last;
    }
    
    private Privilegios(){
        contenedor = new HashMap();
    }
    
    private static Privilegios priv = null;
    private final HashMap<Integer,Permiso> contenedor;
    private Permiso last;
}
