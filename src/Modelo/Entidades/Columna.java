/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entidades;

import Modelo.Clasificacion.Permiso;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Geykel
 */
public class Columna {

    private StringProperty nombre;
    private BooleanProperty insert;
    private BooleanProperty update;
    private Permiso perm;

    public Columna(String nombre, boolean insert, boolean update, Permiso perm) {
        this.nombre = new SimpleStringProperty(nombre);
        this.insert = new SimpleBooleanProperty(insert);
        this.update = new SimpleBooleanProperty(update);
        this.perm = perm;

        this.insert.addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if (t1) perm.agregarInsert(nombre);
            else perm.eliminarInsert(nombre);
        });

        this.update.addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if (t1) perm.agregarUpdate(nombre);
            else perm.eliminarUpdate(nombre);
        });
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(StringProperty nombre) {
        this.nombre = nombre;
    }

    public BooleanProperty insertProperty() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert.set(insert);
    }

    public BooleanProperty updateProperty() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update.set(update);
    }

    public Permiso getPermiso() {
        return perm;
    }

    public void setPermiso(Permiso perm) {
        this.perm = perm;
    }
    
}
