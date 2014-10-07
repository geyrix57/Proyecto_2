/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Permiso;
import Modelo.Clasificacion.Rol;
import Modelo.EsquemaClasificacion.Privilegios;
import Modelo.EsquemaClasificacion.Roles;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;


/**
 * FXML Controller class
 *
 * @author Geykel
 */
public class CrearRoleController implements Initializable {
    
    @FXML
    TextField nombre;
    /*@FXML
    TextField desc;*/
    @FXML
    Button guardar;
    @FXML
    TextField bPermisos;
    @FXML
    TableView<PermisoSeleccionable> tabla;
    @FXML
    TableColumn<PermisoSeleccionable, String> permiso;
    @FXML
    TableColumn<PermisoSeleccionable, Boolean> agregar;
    private ObservableList<PermisoSeleccionable> masterDataPermisos = observableArrayList();
    private Rol rol = null;
    private Rol nuevoRol = null;
    private boolean crear;
    private Stage stage;
    private final Roles roles = Roles.getInstance();
    
    private void ExceptionDialog(Exception e) {
        Dialogs.create()
            .owner(stage)
            .title("Exception Dialog")
            .masthead("Exception")
            .message(e.getMessage())
            .showException(e);
    }
    
    private void ErrorDialog(String msg1,String msg2) {
        Dialogs.create()
        .owner(stage)
        .title("Error Dialog")
        .masthead(msg1)
        .message(msg2)
        .showError();
    }
    
    private void InformationDialog(String ms){
        Dialogs.create()
        .owner(stage)
        .title("Information Dialog")
        .masthead(null)
        .message(ms)
        .showInformation();
    }
    
    @FXML
    private void guardarAction(ActionEvent event) {
        if(crear){
            String nom = nombre.getText();
            /*String des = desc.getText();
            if(des.equals("")) rol.setDesc("Sin descripcion.");
            else rol.setDesc(des);*/
            if(nom.equals(""))  ErrorDialog("Error Nombre","Debe proporcionar un nombre valido.");
            else{
                rol.setNombre(nom);
                if(!roles.existe(rol))
                    if(!rol.tienePermisos())ErrorDialog("Error Permiso","Debe selecionar al menos un permiso.");
                    else
                    try {
                        DataBase.getInstance().ExecuteQuery(rol.generarSqlRol());
                        DataBase.getInstance().ExecuteQuery(rol.generarSqlRolUsuario());
                        //*/System.out.println(rol.generarSqlRol());System.out.println(rol.generarSqlRolUsuario());
                        roles.agregarRol(rol);
                        InformationDialog("Rol creado Correctamente!");
                        stage.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                        this.ExceptionDialog(ex);
                    }
                else ErrorDialog("Error Nombre","Ya existe un Rol con ese nombre.");
            }
        }
        else{ //modificar
            /*String des = desc.getText();
            if(des.equals("")) nuevoRol.setDesc("Sin descripcion.");
            else nuevoRol.setDesc(des);*/
            
            if(!nuevoRol.tienePermisos())ErrorDialog("Error Permiso","Debe selecionar al menos un permiso.");
            else
                try {
                    DataBase.getInstance().ExecuteQuery(rol.generarRevokeSql());
                    DataBase.getInstance().ExecuteQuery(nuevoRol.generarSqlRolUsuario());
                    //*/System.out.println(rol.generarRevokeSql());System.out.println(nuevoRol.generarSqlRolUsuario());
                    roles.eliminarRol(rol);
                    roles.agregarRol(nuevoRol);
                    InformationDialog("Rol Modificado!");
                    stage.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                    this.ExceptionDialog(ex);
                }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        permiso.setCellValueFactory(new PropertyValueFactory("nombre"));
        agregar.setCellValueFactory(new PropertyValueFactory("agregar"));
        agregar.setCellFactory((TableColumn<PermisoSeleccionable, Boolean> p) -> new CheckBoxTableCell<>());
        
        FilteredList<PermisoSeleccionable> filteredData = new FilteredList<>(masterDataPermisos, r -> true);
        SortedList<PermisoSeleccionable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(sortedData);
        
        bPermisos.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(r -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
		return r.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
	});
    }
    
    private void initCrear(){
        rol = new Rol();
        Privilegios.getInstance().getPermisos().stream().forEach((p) -> {
            masterDataPermisos.add(new PermisoSeleccionable(p, rol, false));
        });
    }
    
    private void initModificar(Rol r){
        rol = r;
        nombre.setText(r.getNombre());
        nombre.setDisable(!crear);
//        desc.setText(rol.getDesc());
        nuevoRol = new Rol(rol);
        Privilegios.getInstance().getPermisos().stream().forEach((p) -> {
            masterDataPermisos.add(new PermisoSeleccionable(p, nuevoRol, rol.getPermisos().contains(p.getHashCode())));
        });
    }
    
    public void initData(Boolean crear, Rol r, Stage stage){
        this.stage = stage;
        this.crear = crear;
        if(crear) initCrear();
        else initModificar(r);
    }
    
}
