/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BaseDatos.DataBase;
import Modelo.Clasificacion.Permiso;
import Modelo.Clasificacion.Rol;
import Modelo.Clasificacion.Usuario;
import Modelo.Entidades.ObjetoBD;
import Modelo.EsquemaClasificacion.Privilegios;
import Modelo.EsquemaClasificacion.Roles;
import Modelo.EsquemaClasificacion.Usuarios;
import Modelo.InfoBD;
import Vista.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Geykel
 */
public class RegistroController implements Initializable, Observer {
    @FXML
    MenuBar menuBar;
    /*---------TabTablas---------------*/
    @FXML
    TreeTableView<ObjetoBD> tablas;
    @FXML
    TreeTableColumn<ObjetoBD, String> ts;
    @FXML
    TreeTableColumn<ObjetoBD, String> tb;
    private final TreeItem root = new TreeItem();
    private final InfoBD db = InfoBD.getInstance();
    private final DataBase basedatos = DataBase.getInstance();
    /*----------TabPermisos-------------*/
    @FXML
    TextField bperm;
    @FXML
    TableView<Permiso> Tbpermisos;
    @FXML
    TableColumn<Permiso,String> nivel;
    @FXML
    TableColumn<Permiso,String> tbPerm;
/*    @FXML
    TableColumn<Permiso,String> desc;*/
    private final Privilegios privs = Privilegios.getInstance();
    private final ObservableList<Permiso> masterDataPermisos = FXCollections.observableArrayList();
    /*----------TabRoles-------------*/
    @FXML
    TextField bRole;
    @FXML
    TableView<Rol> Tbroles;
    @FXML
    TableColumn<Rol,String> rol;
    /*@FXML
    TableColumn<Rol,String> rolDesc;*/
    private final Roles roles = Roles.getInstance();
    private final ObservableList<Rol> masterDataRoles = FXCollections.observableArrayList();
    /*----------TabUsuarios-------------------*/
    @FXML
    TextField bUsuario;
    @FXML
    TableView<Usuario> TbUsuarios;
    @FXML
    TableColumn<Usuario, String> nombreCol;
    @FXML
    TableColumn<Usuario, String> usuarioSql;
    @FXML
    TableColumn<Usuario, String> email;
    private final Usuarios usuarios = Usuarios.getInstance();
    private final ObservableList<Usuario> masterDataUsuarios = FXCollections.observableArrayList();
    
    private void InformationDialog(String ms){
        Dialogs.create()
        .owner(Principal.stage)
        .title("Information Dialog")
        .masthead(null)
        .message(ms)
        .showInformation();
    }
    
    private void ExceptionDialog(Exception e){
        Dialogs.create()
        .owner(Principal.stage)
        .title("Exception Dialog")
        .masthead("Exception")
        .message(e.getMessage())
        .showException(e);
    }
    
    private void ErrorDialog(String msg1,String msg2) {
        Dialogs.create()
        .owner(Principal.stage)
        .title("Error Dialog")
        .masthead(msg1)
        .message(msg2)
        .showError();
    }

    private boolean ComfirmDialog(String head,String ms){
        Action response = Dialogs.create()
        .owner(Principal.stage)
        .title("Confirm Dialog")
        .masthead(head)
        .message(ms)
        .showConfirm();
        return response == Dialog.Actions.YES;
    }
    
    private void limpiarDatos(){
        root.getChildren().stream().forEach((t) -> {
            ((TreeItem)t).getChildren().clear();
        });
        root.getChildren().clear();
        masterDataUsuarios.clear();
        masterDataRoles.clear();
        masterDataPermisos.clear();
    }
    
    private void actualizarTabTablas() {
        try {
            limpiarDatos();
            if (db.cargarDatos()) {
                db.getTableSpaces().stream().map((tspace) -> {
                    TreeItem<ObjetoBD> tsItem = new TreeItem(tspace);
                    tspace.getChildren().stream().map((tsTabla) -> new TreeItem(tsTabla)).forEach((tbItem) -> {
                        tsItem.getChildren().add(tbItem);
                    });
                    return tsItem;
                }).forEach((tsItem) -> {
                    root.getChildren().add(tsItem);
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionDialog(ex);
        }
    }
    
    private void initializeTabTablas(){
        tablas.setShowRoot(false);
        root.setExpanded(true);
        tablas.setRoot(root);

        ts.setCellValueFactory((TreeTableColumn.CellDataFeatures<ObjetoBD, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().getTableSpace()));

        tb.setCellValueFactory((TreeTableColumn.CellDataFeatures<ObjetoBD, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().getTabla()));

        tb.setCellFactory( (TreeTableColumn<ObjetoBD, String> param) -> {
            final TreeTableCell<ObjetoBD, String> cell = new TreeTableCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.itemProperty().addListener((ObservableValue<? extends String> obs, String oldValue, String newValue) -> {
                if (newValue != null && !cell.getItem().equals("")) {
                    final ContextMenu cellMenu = new ContextMenu();
                    final MenuItem mkItem = new MenuItem("Crear Permiso");
                    mkItem.setOnAction((ActionEvent event) -> {
                        //System.out.println(cell.getItem());
                        FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearPermiso.fxml"));
                        final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                        try {
                            secondaryStage.setScene(new Scene((Pane) loader.load()));
                        } catch (IOException ex) {
                            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        CrearPermisoController controller = loader.<CrearPermisoController>getController();
                        controller.initData(cell.getItem(), secondaryStage, true, null);
                        secondaryStage.initOwner(Principal.stage);
                        secondaryStage.initModality(Modality.APPLICATION_MODAL);
                        secondaryStage.show();
                    });
                    cellMenu.getItems().add(mkItem);
                    cell.setContextMenu(cellMenu);
                }
                else cell.setContextMenu(null);
            });
            return cell;
        });
        /*if(!basedatos.isConnected()){
            ErrorDialog("Error","No se encontro coneccion disponible");
        }*/
    }
    
    private void eliminarPermiso(Permiso p){
        if(ComfirmDialog("Eliminar Permiso","¿Esta seguro que quiere eliminar el permiso?")){
            try {
                basedatos.ExecuteQuery(p.generarRevokeSql());
                basedatos.ExecuteQuery(p.generarDropRole());
                //*/System.out.println(p.generarRevokeSql());System.out.println(p.generarDropRole());
                privs.eliminarPermiso(p);
                InformationDialog("El Permiso ha sido eliminado!!!");
            } catch (SQLException ex) {
                ExceptionDialog(ex);
                Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void actualizarTablaPermisos(){
        Permiso p = privs.getLast();
        if(privs.existe(p)) masterDataPermisos.add(p);
        else masterDataPermisos.remove(p);
    }
    
    private void initializeTabPermisos(){
        nivel.setCellValueFactory(new PropertyValueFactory("nombre"));
        tbPerm.setCellValueFactory(new PropertyValueFactory("tabla"));
        //desc.setCellValueFactory(new PropertyValueFactory("desc"));
        
        //masterDataPermisos.addAll(privs.getPermisos());
        FilteredList<Permiso> filteredData = new FilteredList<>(masterDataPermisos, p -> true);
        SortedList<Permiso> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Tbpermisos.comparatorProperty());
        Tbpermisos.setItems(sortedData);
        
        bperm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(permiso -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
		return permiso.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
	});
        
        Tbpermisos.setRowFactory((TableView<Permiso> param) -> {
            final TableRow<Permiso> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Editar");
            MenuItem removeItem = new MenuItem("Eliminar");
            removeItem.setOnAction((ActionEvent event) -> {
                /*if(*/eliminarPermiso(row.getItem());//) Tbpermisos.getItems().remove(row.getItem());
            });
            editItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearPermiso.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                CrearPermisoController controller = loader.<CrearPermisoController>getController();
                controller.initData(row.getItem().getTabla(), secondaryStage, false, row.getItem());
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.setResizable(false);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
            rowMenu.getItems().addAll(editItem, removeItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
               .then(rowMenu)
               .otherwise((ContextMenu)null));
            return row;
        });
        
    }
    
    public void eliminarRol(Rol r){
        if(ComfirmDialog("Eliminar Rol","¿Esta seguro que quiere eliminar el Rol?")){
            try {
                basedatos.ExecuteQuery(r.generarRevokeSql());
                basedatos.ExecuteQuery(r.generarDropRole());
            //*/System.out.println(r.generarRevokeSql());System.out.println(r.generarDropRole());
                roles.eliminarRol(r);
                InformationDialog("El rol ha sido eliminado!!!");
            } catch (SQLException ex) {
                ExceptionDialog(ex);
                Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void actualizarTablaRoles(){
        Rol r = roles.getLast();
        if(roles.existe(r)) masterDataRoles.add(r);
        else masterDataRoles.remove(r);
    }
    
    private void initializeTabRoles(){
        rol.setCellValueFactory(new PropertyValueFactory("nombre"));
//        rolDesc.setCellValueFactory(new PropertyValueFactory("desc"));
        
        //masterDataRoles.addAll(roles.getRoles());
        FilteredList<Rol> filteredData = new FilteredList<>(masterDataRoles, r -> true);
        SortedList<Rol> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Tbroles.comparatorProperty());
        Tbroles.setItems(sortedData);
        
        bRole.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(r -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
		return r.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
	});
        
        Tbroles.setRowFactory((TableView<Rol> param) -> {
            final TableRow<Rol> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Editar");
            MenuItem removeItem = new MenuItem("Eliminar");
            removeItem.setOnAction((ActionEvent event) -> {
                /*if(*/eliminarRol(row.getItem());//) Tbroles.getItems().remove(row.getItem());
            });
            editItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearRole.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                CrearRoleController controller = loader.<CrearRoleController>getController();
                controller.initData(false, row.getItem(), secondaryStage);
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
            rowMenu.getItems().addAll(editItem, removeItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
               .then(rowMenu)
               .otherwise((ContextMenu)null));
            return row;
        });
    }
    
    private void eliminarUsuario(Usuario u){
        if(ComfirmDialog("Eliminar Usuario","¿Esta seguro que quiere eliminar el usuario?")){
            try {
                basedatos.ExecuteQuery(u.generarRevokeSql());
                basedatos.ExecuteQuery(u.generarDropRole());
            //*/System.out.println(u.generarRevokeSql());System.out.println(u.generarDropRole());
                usuarios.eliminarUsuario(u);
                InformationDialog("El usuario ha sido eliminado!!!");
            } catch (SQLException ex) {
                ExceptionDialog(ex);
                Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void actualizarTablaUsuarios(){
        Usuario u = usuarios.getLast();
        if(usuarios.existe(u)) masterDataUsuarios.add(u);
        else masterDataUsuarios.remove(u);
    }
    
    private void initializeTabUsuarios(){
        nombreCol.setCellValueFactory(new PropertyValueFactory("nombre"));
        usuarioSql.setCellValueFactory(new PropertyValueFactory("sqlName"));
        email.setCellValueFactory(new PropertyValueFactory("email"));
        
        masterDataUsuarios.addAll(usuarios.getUsuarios());
        FilteredList<Usuario> filteredData = new FilteredList<>(masterDataUsuarios, u -> true);
        SortedList<Usuario> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(TbUsuarios.comparatorProperty());
        TbUsuarios.setItems(sortedData);
        
        bUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(u -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return u.getNombre().toLowerCase().contains(lowerCaseFilter);
		/*if(u.getNombre().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if(u.getSqlName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else return false;*/
            });
	});
        
        TbUsuarios.setRowFactory((TableView<Usuario> param) -> {
            final TableRow<Usuario> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Editar");
            MenuItem removeItem = new MenuItem("Eliminar");
            removeItem.setOnAction((ActionEvent event) -> {
                eliminarUsuario(row.getItem());
            });
            editItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearUsuario.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                CrearUsuarioController controller = loader.<CrearUsuarioController>getController();
                controller.initData(false, row.getItem(), secondaryStage);
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
            rowMenu.getItems().addAll(editItem, removeItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
               .then(rowMenu)
               .otherwise((ContextMenu)null));
            return row;
        });
    }
    
    private MenuItem crearRoleMenuItem(){
        MenuItem menuItem = new MenuItem("Crear Role");
        menuItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearRole.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                CrearRoleController controller = loader.<CrearRoleController>getController();
                controller.initData(true, null, secondaryStage);
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
        return menuItem;
    }
    
    private MenuItem crearUsuarioMenuItem(){
        MenuItem menuItem = new MenuItem("Crear Usuario");
        menuItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("CrearUsuario.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                CrearUsuarioController controller = loader.<CrearUsuarioController>getController();
                controller.initData(true, null, secondaryStage);
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
        return menuItem;
    }
    
    private MenuItem crearConeccionMenuItem(){
        MenuItem menuItem = new MenuItem("Crear Coneccion");
        menuItem.setOnAction((ActionEvent event)->{
                FXMLLoader loader = new FXMLLoader(Principal.class.getResource("Login.fxml"));
                final Stage secondaryStage = new Stage(StageStyle.UTILITY);
                try {
                    secondaryStage.setScene(new Scene((Pane) loader.load()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ControlLogin controller = loader.<ControlLogin>getController();
                controller.initData(secondaryStage);
                secondaryStage.initOwner(Principal.stage);
                secondaryStage.initModality(Modality.APPLICATION_MODAL);
                secondaryStage.show();
            });
        return menuItem;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBar.getMenus().clear();
        MenuItem a = crearRoleMenuItem();
        MenuItem b = crearUsuarioMenuItem();
        MenuItem c = crearConeccionMenuItem();
        
        a.setAccelerator(new KeyCodeCombination(KeyCode.R,KeyCombination.CONTROL_DOWN));
        b.setAccelerator(new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN));
        c.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
        
        Menu menu = new Menu("Crear");
        menu.getItems().add(a);
        menu.getItems().add(b);
        menuBar.getMenus().add(menu);
        
        menu = new Menu("Coneccion");
        menu.getItems().add(c);
        menuBar.getMenus().add(menu);
        
        basedatos.addObserver(this);
        privs.addObserver(this);
        roles.addObserver(this);
        usuarios.addObserver(this);
        
               basedatos.setConnection("localhost", 1521, "XE", "sys as sysdba", "gkl123");
               
        initializeTabTablas();
        initializeTabPermisos();
        initializeTabRoles();
        initializeTabUsuarios();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (basedatos.isConnected() && o.equals(basedatos))
            actualizarTabTablas();
        else if(o.equals(privs))
            actualizarTablaPermisos();
        else if(o.equals(roles))
            actualizarTablaRoles();
        else if(o.equals(usuarios))
            actualizarTablaUsuarios();
    }

}
