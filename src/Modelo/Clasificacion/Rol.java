/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.Clasificacion;


import Modelo.EsquemaClasificacion.Privilegios;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Geykel
 */
public class Rol {
    public Rol(String nombre, ArrayList<Integer> Permisos){
        this.nombre =  new SimpleStringProperty(nombre);
//        this.desc = new SimpleStringProperty(desc);
        this.Permisos = Permisos;
        this.hashcode = nombre.hashCode();
    }
    
    public Rol(String nombre){
        this.nombre =  new SimpleStringProperty(nombre);
        this.Permisos = new ArrayList();
        this.hashcode = nombre.hashCode();
    }
    
    public Rol(){
        this.nombre =  new SimpleStringProperty();
//        this.desc = new SimpleStringProperty();
        this.Permisos = new ArrayList();
        this.hashcode = 0;
    }
    
    public Rol(Rol r){
        this.nombre =  new SimpleStringProperty(r.getNombre());
//        this.desc = new SimpleStringProperty(r.getDesc());
        this.Permisos = (ArrayList)r.getPermisos().clone();
        this.hashcode = r.getHashCode();
    }
    
    public String getNombre() {
        return nombre.get();
    }
    
    public void setNombre(String nombre){
        this.hashcode = nombre.hashCode();
        this.nombre.set(nombre);
    }
    
    public StringProperty nombreProperty() {
	return nombre;
    }
    
    /*public String getDesc() {
        return desc.get();
    }
    
    public void setDesc(String desc){
        this.desc.set(desc);
    }
    
    public StringProperty descProperty() {
	return desc;
    }*/

    public ArrayList<Integer> getPermisos() {
        return Permisos;
    }
    
    public void agregarPermiso(Integer permiso){
        if(!Permisos.contains(permiso)) Permisos.add(permiso);
    }
    
    public void eliminarPermiso(Integer permiso){
        Permisos.remove(permiso);
    }
    
    public int getHashCode(){
        return this.hashcode;
    }
    
    public boolean tienePermisos(){
        if(Permisos == null) return false;
        else return (!Permisos.isEmpty());
    }
    
    public String generarSqlRol(){
        StringBuilder sql;
        sql = new StringBuilder("CREATE ROLE ").append(this.nombre.get());
        return sql.toString();
    }
    
    public String generarDropRole(){
        StringBuilder sql = new StringBuilder();
        sql.append("DROP ROLE ").append(this.getNombre());
        return sql.toString();
    }

    public String generarSqlRolUsuario(){
        StringBuilder sql = new StringBuilder("GRANT ");
        StringBuilder perm = null;
        Privilegios priv = Privilegios.getInstance();
        for(Integer i:Permisos){
            if(perm == null){
                perm = new StringBuilder(priv.getPermiso(i).getNombre());
            }
            else{
                perm.append(", ").append(priv.getPermiso(i).getNombre());
            }
        }
        return sql.append(perm).append(" TO ").append(this.nombre.get()).toString();
    }
    
    public String generarRevokeSql(){
        StringBuilder rev = new StringBuilder("REVOKE ");
        Privilegios priv = Privilegios.getInstance();
        StringBuilder perm = null;
        for(Integer i:Permisos){
            if(perm == null){
                perm = new StringBuilder(priv.getPermiso(i).getNombre());
            }
            else{
                perm.append(", ").append(priv.getPermiso(i).getNombre());
            }
        }
        return rev.append(perm).append(" FROM ").append(this.getNombre()).toString();
    }
    
    //private String revoke = null;
    private final StringProperty nombre;
    //private final StringProperty desc;
    private int hashcode;
    private final ArrayList<Integer> Permisos; 
}
