/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.Clasificacion.Permiso;
import Modelo.Clasificacion.Rol;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
/**
 *
 * @author Geykel
 */
public class PermisoSeleccionable {
        
    public PermisoSeleccionable(Permiso p, Rol r, Boolean Seleccionado){
        nombre = new SimpleStringProperty(p.getNombre());
        agregar = new SimpleBooleanProperty(Seleccionado);
        this.hashcodePermiso = p.getHashCode();
        rol = r; 
        this.agregar.addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if(t1) rol.agregarPermiso(hashcodePermiso);
            else rol.eliminarPermiso(hashcodePermiso);
        });
    }
    
    public String getNombre(){
        return nombre.get();
    }
    
    public StringProperty nombreProperty(){
        return nombre;
    }
    
    public Boolean getAgregar(){
        return agregar.get();
    }
    
    public BooleanProperty agregarProperty() {
            return agregar;
    }
    
    private final StringProperty nombre;
    private final BooleanProperty agregar;
    private int hashcodePermiso;
    private Rol rol;
}
