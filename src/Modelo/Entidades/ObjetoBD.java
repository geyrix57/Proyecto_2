/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.Entidades;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Geykel
 */
public class ObjetoBD{
    final private SimpleStringProperty tablespace;
    final private SimpleStringProperty tabla;
    private ArrayList<ObjetoBD> children;
    
    public ObjetoBD(String ts, String tb){
        this.tablespace = new SimpleStringProperty(ts);
        this.tabla = new SimpleStringProperty(tb);
        children = new ArrayList();
    }
    
    public ObjetoBD(String ts, String tb, ArrayList<ObjetoBD> children){
        this.tablespace = new SimpleStringProperty(ts);
        this.tabla = new SimpleStringProperty(tb);
        this.children = children;
    }
    
    public String getTableSpace(){
        return this.tablespace.get();
    }
    
    public String getTabla(){
        return this.tabla.get();
    }

    public ArrayList<ObjetoBD> getChildren() {
        return this.children;
    }
}
