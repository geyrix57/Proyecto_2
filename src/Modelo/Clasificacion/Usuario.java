/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.Clasificacion;

import Modelo.EsquemaClasificacion.Roles;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Geykel
 */
public class Usuario {
    public Usuario(String nombre, String SqlName, String email, ArrayList<Integer> roles){
        this.nombre =  new SimpleStringProperty(nombre);
        this.email = new SimpleStringProperty(email);
        this.sqlName = new SimpleStringProperty(SqlName);
        this.roles = roles;
        this.hashcode = SqlName.hashCode();
    }
    
    public Usuario(){
        this.nombre =  new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.sqlName = new SimpleStringProperty();
        this.roles = new ArrayList();
        this.hashcode = 0;
    }
    
    public Usuario(Usuario u){
        this.nombre =  new SimpleStringProperty(u.getNombre());
        this.sqlName = new SimpleStringProperty(u.getSqlName());
        this.email = new SimpleStringProperty(u.getEmail());
        this.roles = (ArrayList)u.getRoles().clone();
        this.hashcode = u.getHashCode();
    }
    
    public String getNombre() {
        return nombre.get();
    }
    
    public void setNombre(String nombre){
        this.nombre.set(nombre);
    }
    
    public StringProperty nombreProperty() {
	return nombre;
    }
    
    public String getSqlName(){
        return this.sqlName.get();
    }
    
    public void setSqlName(String sqlName){
        this.hashcode = sqlName.hashCode();
        this.sqlName.set(sqlName);
    }
    
    public StringProperty sqlNameProperty(){
        return this.sqlName;
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public void setEmail(String email){
        this.email.set(email);
    }
    
    public StringProperty emailProperty() {
	return email;
    }

    public ArrayList<Integer> getRoles() {
        return roles;
    }
    
    public void agregarRol(Integer permiso){
        if(!roles.contains(permiso)) roles.add(permiso);
    }
    
    public void eliminarRol(Integer permiso){
        roles.remove(permiso);
    }
    
    public int getHashCode(){
        return this.hashcode;
    }
    
    public boolean tieneRoles(){
        if(roles == null) return false;
        else return (!roles.isEmpty());
    }
    
    public String generarSqlRol(){
        StringBuilder sql;
        sql = new StringBuilder("CREATE ROLE ").append(this.getSqlName());
        return sql.toString();
    }
    
    public String generarDropRole(){
        StringBuilder sql = new StringBuilder();
        sql.append("DROP ROLE ").append(this.getSqlName());
        return sql.toString();
    }

    public String generarSqlRolUsuario(){
        StringBuilder sql = new StringBuilder("GRANT ");
        StringBuilder rev = new StringBuilder("REVOKE ");
        StringBuilder perm = null;
        Roles rls = Roles.getInstance();
        for(Integer i:roles){
            if(perm == null){
                perm = new StringBuilder(rls.getRol(i).getNombre());
                rev.append(rls.getRol(i).getNombre());
            }
            else{
                perm.append(", ").append(rls.getRol(i).getNombre());
                rev.append(", ").append(rls.getRol(i).getNombre());
            }
        }
        this.revoke = rev.append(" FROM ").append(this.getSqlName()).toString();
        return sql.append(perm).append(" TO ").append(this.getSqlName()).toString();
    }
    
    public String generarRevokeSql(){
        return revoke;
    }
    
    private String revoke = null;
    private final StringProperty nombre;
    private final StringProperty sqlName;
    private final StringProperty email;
    private int hashcode;
    private final ArrayList<Integer> roles; 
}
