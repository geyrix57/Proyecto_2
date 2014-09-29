/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Permiso;
import Modelo.Entidades.Columna;
import Modelo.Entidades.ObjetoBD;
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

    public boolean cargarDatos() throws SQLException {
        ActualizarTablespace();
        ActualizarListaTablas();
        return true;
    }

}
