/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Usuario;
import Modelo.EsquemaClasificacion.Roles;
import Modelo.EsquemaClasificacion.Usuarios;
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
public class CrearUsuarioController implements Initializable {

    @FXML
    TextField nombre;
    @FXML
    TextField email;
    @FXML
    Button guardar;
    @FXML
    TextField bRoles;
    @FXML
    TableView<RolSeleccionable> tabla;
    @FXML
    TableColumn<RolSeleccionable, String> rol;
    @FXML
    TableColumn<RolSeleccionable, Boolean> agregar;
    private ObservableList<RolSeleccionable> masterDataRoles = observableArrayList();
    private Usuario user = null;
    private Usuario newUser = null;
    private boolean crear;
    private Stage stage;
    private final Usuarios usuarios = Usuarios.getInstance();
    
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
            String des = email.getText();
            /*if(des.equals("")) ErrorDialog("Error e-mail","Debe proporcionar un e-mail valido.");
            else user.setEmail(des);*/
            if(nom.equals(""))  ErrorDialog("Error Nombre","Debe proporcionar un nombre valido.");
            else{
                user.setSqlName(nom);
                if(!usuarios.existe(user))
                    if(!user.tieneRoles())ErrorDialog("Error Rol","Debe selecionar al menos un Rol.");
                    else
                    try {
                        DataBase.getInstance().ExecuteQuery(user.generarSqlRol());
                        DataBase.getInstance().ExecuteQuery(user.generarSqlRolUsuario());
                        //*/user.setSqlName(nom);System.out.println(user.generarSqlRol());System.out.println(user.generarSqlRolUsuario());
                        usuarios.agregarUsuario(user);
                        InformationDialog("Usuario creado Correctamente!");
                        stage.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                        this.ExceptionDialog(ex);
                    }
                else ErrorDialog("Error Nombre","Ya existe un usuario con ese nombre.");
            }
        }
        else{ //modificar
            String des = email.getText();
            /*if(des.equals("")) ErrorDialog("Error e-mail","Debe proporcionar un e-mail valido.");
            else newUser.setEmail(des);*/
            if(!newUser.tieneRoles())ErrorDialog("Error Rol","Debe selecionar al menos un rol.");
            else
            try {
                    DataBase.getInstance().ExecuteQuery(user.generarRevokeSql());
                    DataBase.getInstance().ExecuteQuery(newUser.generarSqlRolUsuario());
                    //*/System.out.println(user.generarRevokeSql());System.out.println(newUser.generarSqlRolUsuario());
                    usuarios.eliminarUsuario(user);
                    usuarios.agregarUsuario(newUser);
                    InformationDialog("Usuario Modificado!");
                    stage.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                    this.ExceptionDialog(ex);
                }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rol.setCellValueFactory(new PropertyValueFactory("nombreRol"));
        agregar.setCellValueFactory(new PropertyValueFactory("agregar"));
        agregar.setCellFactory((TableColumn<RolSeleccionable, Boolean> p) -> new CheckBoxTableCell<>());
        
        FilteredList<RolSeleccionable> filteredData = new FilteredList<>(masterDataRoles, r -> true);
        SortedList<RolSeleccionable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(sortedData);
        
        bRoles.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(r -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
		return r.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
	});
    }
    
    private void initCrear(){
        user = new Usuario();
        Roles.getInstance().getRoles().stream().forEach((r) -> {
            masterDataRoles.add(new RolSeleccionable(r, user, false));
        });
    }
    
    private void initModificar(Usuario u){
        user = u;
        nombre.setText(u.getSqlName());
        nombre.setDisable(!crear);
        //email.setText(user.getEmail());
        newUser = new Usuario(user);
        Roles.getInstance().getRoles().stream().forEach((r) -> {
            masterDataRoles.add(new RolSeleccionable(r, newUser, user.getRoles().contains(r.getHashCode())));
        });
    }
    
    public void initData(Boolean crear, Usuario u, Stage stage){
        this.stage = stage;
        this.crear = crear;
        if(crear) initCrear();
        else initModificar(u);
    }
    
}
