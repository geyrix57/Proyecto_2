/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Permiso;
import Modelo.Entidades.Columna;
import Modelo.EsquemaClasificacion.Privilegios;
import Modelo.InfoBD;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
public class CrearPermisoController implements Initializable {
    @FXML
    Button guardar;
    @FXML
    TextField nombre;
    @FXML
    Label tabla;
    @FXML
    TableView<Columna> columnas;
    @FXML
    TableColumn<Columna, Boolean> insert;
    @FXML
    TableColumn<Columna, Boolean> update;
    @FXML
    TableColumn<Columna, String> nombreColumna;
    @FXML
    CheckBox select;
    @FXML
    CheckBox delete;
    @FXML
    CheckBox insertAll;
    @FXML
    CheckBox updateAll;
    private Stage stage;
    private boolean crear;

    private ObservableList<Columna> data;
    private String nomTab;
    private Permiso perm;
    private Permiso nuevoPerm;
    private final Privilegios priv = Privilegios.getInstance();

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
            /*if(des.equals("")) perm.setDesc("Sin descripcion.");
            else perm.setDesc(des);*/
            if(nom.equals(""))  ErrorDialog("Error Nombre","Debe proporcionar un nombre valido.");
            else{
                perm.setNombre(nom);
                if(!priv.existe(perm))
                    if(!perm.tienePermisos())ErrorDialog("Error Privilegios","Debe selecionar al menos un privilegio.");
                    else
                    try {
                        DataBase.getInstance().ExecuteQuery(perm.generarSqlRole());
                        DataBase.getInstance().ExecuteQuery(perm.generarSql());
                        //*/System.out.println(perm.generarSql());
                        priv.agregarPermiso(perm);
                        InformationDialog("Permiso creado Correctamente!");
                        stage.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                        this.ExceptionDialog(ex);
                    }
                else ErrorDialog("Error Nombre","Ya existe un permiso con ese nombre");
            }
        }
        else{ //modificar
            /*String des = desc.getText();
            if(des.equals("")) nuevoPerm.setDesc("Sin descripcion.");
            else nuevoPerm.setDesc(des);*/
            
            if(!nuevoPerm.tienePermisos())ErrorDialog("Error Privilegios","Debe selecionar al menos un privilegio.");
             else
                try {
                    DataBase.getInstance().ExecuteQuery(perm.generarRevokeSql());
                    DataBase.getInstance().ExecuteQuery(nuevoPerm.generarSql());
                    //*/System.out.println(perm.generarRevokeSql());System.out.println(nuevoPerm.generarSql());
                    priv.eliminarPermiso(perm);
                    priv.agregarPermiso(nuevoPerm);
                    InformationDialog("Permiso Modificado!");
                    stage.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
                    this.ExceptionDialog(ex);
                }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        insert.setCellValueFactory(new PropertyValueFactory("insert"));
        insert.setCellFactory((TableColumn<Columna, Boolean> p) -> new CheckBoxTableCell<>());
        update.setCellValueFactory(new PropertyValueFactory("update"));
        update.setCellFactory((TableColumn<Columna, Boolean> p) -> new CheckBoxTableCell<>());
        nombreColumna.setCellValueFactory(new PropertyValueFactory("nombre"));
        insertAll.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            //if (new_val) {
                data.stream().forEach((p) -> {
                    p.insertProperty().set(new_val);
                });
            //}
        });
        updateAll.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val)->{
            //if(new_val){
                data.stream().forEach((p)->{
                    p.updateProperty().set(new_val);
                });
            //}
        });
        delete.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val)->{
            if(crear)
                perm.setDelete(new_val);
            else
                nuevoPerm.setDelete(new_val);
        });
        select.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val)->{
            if(crear)
                perm.setSelect(new_val);
            else
                nuevoPerm.setSelect(new_val);
        });
    }

    public void initData(String tabla, Stage stage, boolean crear, Permiso p) {
        this.crear = crear;
        nombre.setDisable(!crear);
        this.stage = stage;
        this.nomTab = tabla;
        this.tabla.setText(tabla);
        if(this.crear) perm = new Permiso(tabla);
        else{
            perm = p;
            nuevoPerm = new Permiso(perm);
            nombre.setText(perm.getNombre());
        }
        actualizarTabla();
    }

    private void actualizarTabla() {
        try {
            if(this.crear)
                data = InfoBD.getInstance().ActualizarTablaColumn(nomTab, perm);
            else{
                ArrayList<String> l = InfoBD.getInstance().getColumnasTabla(nomTab);
                data = observableArrayList();
                l.stream().forEach((c) -> {
                    data.add(new Columna(c,nuevoPerm.getInsert().contains(c),nuevoPerm.getUpdate().contains(c),nuevoPerm));
                });
                if(nuevoPerm.isDelete()) delete.selectedProperty().set(true);
                if(nuevoPerm.isSelect()) select.selectedProperty().set(true);
            }
            columnas.setItems(data);
        } catch (SQLException ex) {
            Logger.getLogger(CrearPermisoController.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionDialog(ex);
        }
    }
}
