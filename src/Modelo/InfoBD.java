/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Permiso;
import Modelo.Clasificacion.Rol;
import Modelo.Clasificacion.Usuario;
import Modelo.Entidades.Columna;
import Modelo.Entidades.ObjetoBD;
import Modelo.EsquemaClasificacion.Privilegios;
import Modelo.EsquemaClasificacion.Roles;
import Modelo.EsquemaClasificacion.Usuarios;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author Geykel
 */
public class InfoBD {

    private ArrayList<ObjetoBD> tablas;
    private final DataBase database;
    private static InfoBD bd = null;

    private InfoBD() {
        tablas = new ArrayList();
        database = DataBase.getInstance();
    }

    public static InfoBD getInstance() {
        if (bd == null) {
            bd = new InfoBD();
        }
        return bd;
    }

    public ArrayList<ObjetoBD> getTableSpaces() {
        return this.tablas;
    }

    private void ActualizarTablespace() throws SQLException {
        tablas.clear();
        ResultSet result = this.database.ExecuteQuery("SELECT TABLESPACE_NAME FROM DBA_DATA_FILES");
        while (result.next()) {
            String tsname = result.getString("TABLESPACE_NAME");
            this.tablas.add(new ObjetoBD(tsname,""));
        }
        result.getStatement().close();
    }

    private void ActualizarListaTablas() throws SQLException {
        String l = "SELECT TABLE_NAME FROM DBA_TABLES WHERE TABLESPACE_NAME LIKE '";
        StringBuilder sql; 
        for (ObjetoBD ts : this.tablas) {
            sql = new StringBuilder();
            sql.append(l);
            sql.append(ts.getTableSpace()).append("'");
            ResultSet result = this.database.ExecuteQuery(sql.toString());
            while (result.next()) {
                String tname = result.getString("TABLE_NAME");
                ts.getChildren().add(new ObjetoBD("",tname,null));
            }
            result.getStatement().close();
        }
    }

    public ObservableList<Columna> ActualizarTablaColumn(String tabla, Permiso perm) throws SQLException {
        ObservableList<Columna> data = observableArrayList();
        String sql = "SELECT COLUMN_NAME FROM  USER_TAB_COLUMNS WHERE TABLE_NAME= '" + tabla + "'";
        ResultSet result = this.database.ExecuteQuery(sql);
        while (result.next()) {
            String cname = result.getString("COLUMN_NAME");
            data.add(new Columna(cname, false, false, perm));
        }
        result.getStatement().close();
        return data;
    }
    
    public ArrayList<String> getColumnasTabla(String tabla) throws SQLException{
        ArrayList<String> data = new ArrayList();
        String sql = "SELECT COLUMN_NAME FROM  USER_TAB_COLUMNS WHERE TABLE_NAME= '" + tabla + "'";
        ResultSet result = this.database.ExecuteQuery(sql);
        while (result.next()) {
            String cname = result.getString("COLUMN_NAME");
            data.add(cname);
        }
        result.getStatement().close();
        return data;
    }
    
    public void cargarRoles() throws SQLException{
        String sql = "SELECT ROLE FROM DBA_ROLES";
        Privilegios privs = Privilegios.getInstance();
        Roles roles = Roles.getInstance();
        ArrayList<Permiso> permisos = new ArrayList();
        ArrayList<Rol> rols = new ArrayList();
        ResultSet result = this.database.ExecuteQuery(sql);
        while (result.next()) {
            String cname = result.getString("ROLE");
            if(cname.contains("CL_")) permisos.add(new Permiso(cname,true));
            else rols.add(new Rol(cname));
        }
        result.getStatement().close();
        for(Permiso p:permisos){
            sql = "SELECT GRANTEE,TABLE_NAME,PRIVILEGE FROM DBA_TAB_PRIVS WHERE GRANTEE='"+p.getNombre()+"'";
            result = this.database.ExecuteQuery(sql);
            while (result.next()) {
                p.setTabla(result.getString("TABLE_NAME"));
                if(result.getString("PRIVILEGE").contains("SELECT")) p.setSelect(true);
                else if(result.getString("PRIVILEGE").contains("DELETE")) p.setDelete(true);
            }
            result.getStatement().close();
            sql = "SELECT GRANTEE,TABLE_NAME,PRIVILEGE,COLUMN_NAME FROM DBA_COL_PRIVS WHERE GRANTEE='"+p.getNombre()+"'";
            result = this.database.ExecuteQuery(sql);
            while (result.next()) {
                p.setTabla(result.getString("TABLE_NAME"));
                if(result.getString("PRIVILEGE").contains("UPDATE")) p.agregarUpdate(result.getString("COLUMN_NAME"));
                else if(result.getString("PRIVILEGE").contains("INSERT")) p.agregarInsert(result.getString("COLUMN_NAME"));
            }
            result.getStatement().close();
            privs.agregarPermiso(p);//new permiso p;
        } 
        for(Rol r: rols){
            sql = "SELECT ROLE,GRANTED_ROLE FROM ROLE_ROLE_PRIVS WHERE ROLE='"+r.getNombre()+"'";
            result = this.database.ExecuteQuery(sql);
            while (result.next()) {
                r.agregarPermiso(result.getString("GRANTED_ROLE").hashCode());
            }
            result.getStatement().close();
            roles.agregarRol(r);
        }
    }
    
    public void cargarUsuarios() throws SQLException{
        String sql = "SELECT USERNAME FROM DBA_USERS";
        Usuarios users = Usuarios.getInstance();
        ArrayList<Usuario> usuarios = new ArrayList();
        ResultSet result = this.database.ExecuteQuery(sql);
        while (result.next()) {
            String cname = result.getString("USERNAME");
            usuarios.add(new Usuario(cname));
        }
        result.getStatement().close();
        for(Usuario u:usuarios){
            sql = "SELECT GRANTEE,GRANTED_ROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE='"+u.getSqlName()+"'";
            result = this.database.ExecuteQuery(sql);
            while (result.next()) {
                u.agregarRol(result.getString("GRANTED_ROLE").hashCode());
            }
            users.agregarUsuario(u);
        }
    }

    public boolean cargarDatos() throws SQLException {
        ActualizarTablespace();
        ActualizarListaTablas();
        cargarRoles();
        cargarUsuarios();
        return true;
    }

}
