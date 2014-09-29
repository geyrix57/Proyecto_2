/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.Clasificacion.Rol;
import Modelo.Clasificacion.Usuario;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Geykel
 */
public class RolSeleccionable {
    public RolSeleccionable(Rol r, Usuario u, Boolean Seleccionado){
        nombreRol = new SimpleStringProperty(r.getNombre());
        agregar = new SimpleBooleanProperty(Seleccionado);
        this.hashcodeRol = r.getHashCode();
        this.usuario = u; 
        this.agregar.addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if(t1) usuario.agregarRol(hashcodeRol);
            else usuario.eliminarRol(hashcodeRol);
        });
    }
    
    public String getNombre(){
        return nombreRol.get();
    }
    
    public StringProperty nombreRolProperty(){
        return nombreRol;
    }
    
    public Boolean getAgregar(){
        return agregar.get();
    }
    
    public BooleanProperty agregarProperty() {
            return agregar;
    }
    
    private final StringProperty nombreRol;
    private final BooleanProperty agregar;
    private int hashcodeRol;
    private Usuario usuario;
}
