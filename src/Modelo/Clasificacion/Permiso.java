/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Clasificacion;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Geykel
 */
public class Permiso {

    public Permiso(String nombre, String tabla, boolean select, boolean delete, ArrayList<String> insert, ArrayList<String> update) {
        this.nombre = new SimpleStringProperty(nombre);
        this.select = select;
        this.delete = delete;
        this.insert = insert;
        this.update = update;
        this.tabla = new SimpleStringProperty(tabla);
        this.desc = new SimpleStringProperty();
        this.hascode = nombre.hashCode();
    }
    public Permiso(Permiso p){
        this.nombre = new SimpleStringProperty(p.getNombre());
        this.select = p.isSelect();
        this.delete = p.isDelete();
        this.insert = (ArrayList)p.getInsert().clone();
        this.update = (ArrayList)p.getUpdate().clone();
        this.tabla = new SimpleStringProperty(p.getTabla());
        this.desc = new SimpleStringProperty();
        this.ind = p.ind;
        this.hascode = p.getHashCode();
    }
    public Permiso(String tabla){
        this.tabla = new SimpleStringProperty(tabla);
        this.insert = new ArrayList();
        this.update = new ArrayList();
        this.nombre = new SimpleStringProperty();
        this.delete = false;
        this.select = false;
        this.desc = new SimpleStringProperty();
        this.hascode = 0;
    }

    public String getTabla() {
        return tabla.get();
    }

    public void setTabla(String tabla) {
        this.tabla.set(tabla);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.hascode = nombre.hashCode();
        this.nombre.set(nombre);
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        if(!this.select && select) agregar();
        if(this.select && !select) eliminar();
        this.select = select;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        if(!this.delete && delete) agregar();
        if(this.delete && !delete) eliminar();
        this.delete = delete;
    }

    public ArrayList<String> getInsert() {
        return insert;
    }

    public void setInsert(ArrayList<String> insert) {
        this.insert = insert;
    }
    
    public void agregarInsert(String in){
        if(!insert.contains(in)){
            insert.add(in);
            agregar();
        }
    }
    
    public void eliminarInsert(String in){
        insert.remove(in);
        eliminar();
    }
    
    public void agregarUpdate(String in){
        if(!update.contains(in)){
            update.add(in);
            agregar();
        }
    }
    
    public void eliminarUpdate(String in){
        update.remove(in);
        eliminar();
    }

    public ArrayList<String> getUpdate() {
        return update;
    }

    public void setUpdate(ArrayList<String> update) {
        this.update = update;
    }

    public String generarSqlRole() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE ROLE ").append(this.getNombre());
        return sql.toString();
    }
    
    public String generarDropRole(){
        StringBuilder sql = new StringBuilder();
        sql.append("DROP ROLE ").append(this.getNombre());
        return sql.toString();
    }
    
    public boolean tienePermisos(){
        return ind > 0;
    }
    
    public int getInd(){return ind;}
    
    public void agregar(){ ind++; /*System.out.println(ind);*/ }
    public void eliminar(){ ind--; /*System.out.println(ind);*/}

    public String generarSql() {
        StringBuilder rev = new StringBuilder("REVOKE ");
        StringBuilder sqlT = new StringBuilder("GRANT ");
        StringBuilder sql = new StringBuilder();
        if (this.select) {
            sql.append("SELECT");
            rev.append("SELECT");
        }
        if (this.delete) {
            if (sql.length() == 0) {
                sql.append("DELETE");
                rev.append("DELETE");
            } else {
                sql.append(", DELETE");
                rev.append(", DELETE");
            }
        }
        if (!insert.isEmpty()) {
            if (sql.length() == 0) {
                rev.append("INSERT");
                sql.append("INSERT (");
            } else {
                rev.append(", INSERT");
                sql.append(", INSERT (");
            }
            StringBuilder col = null;
            for (String i : insert) {
                if (col == null) {
                    col = new StringBuilder(i);
                } else {
                    col.append(", ").append(i);
                }
            }
            sql.append(col).append(")");
        }
        if (!update.isEmpty()) {
            if (sql.length() == 0) {
                sql.append("UPDATE (");
                rev.append("UPDATE");
            } else {
                sql.append(", UPDATE (");
                rev.append(", UPDATE");
            }
            StringBuilder col = null;
            for (String i : update) {
                if (col == null) {
                    col = new StringBuilder(i);
                } else {
                    col.append(", ").append(i);
                }
            }
            sql.append(col).append(")");
        }
        sqlT.append(sql).append(" ON ").append(tabla.get()).append(" TO ").append(nombre.get());
        rev.append(" ON ").append(tabla.get()).append(" FROM ").append(nombre.get());
        revoke = rev.toString();
        return sqlT.toString();
    }
    
    public String generarRevokeSql(){
        return revoke;
    }

    public int getHashCode() {
        return this.hascode;
    }

    public String getDesc() {
        return desc.get();
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }
    
    public StringProperty nombreProperty() {
	return nombre;
    }
    
    public StringProperty tablaProperty() {
	return tabla;
    }
    
    public StringProperty descProperty() {
	return desc;
    }
   
    private String revoke = null;
    private final StringProperty nombre;
    private final StringProperty tabla;
    private final StringProperty desc;
    private boolean select;
    private boolean delete;
    private ArrayList<String> insert;
    private ArrayList<String> update;
    private int ind = 0; 
    private int hascode;
}
