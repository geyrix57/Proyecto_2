/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BaseDatos.DataBase;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author aaron
 */
public class ControlLogin implements Initializable {

    //Componentes del Login.fxml
    @FXML
    TextField Hostinput;
    @FXML
    TextField Portinput;
    @FXML
    TextField Usernameinput;
    @FXML
    TextField Passwordinput;
    @FXML
    CheckBox Hostcheck;
    @FXML
    CheckBox Portcheck;
    @FXML
    Button connect;
    @FXML
    TextField SIDInput;
    @FXML
    CheckBox SIDcheck;
    @FXML
    CheckBox UsernameCheck;
    
    JPanel panel = new JPanel();
    private Stage stage;
    String host, port, username, password, sid;
    /*-------------------------------------------------*/

    /*-------------------------------------------------*/
    public void initData(Stage stage) {
        this.stage = stage;
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
    private void ConnectButtomAction(ActionEvent event) {
        this.host = Hostinput.getText();

        this.port = Portinput.getText();

        this.username = Usernameinput.getText();

        this.password = Passwordinput.getText();

        this.sid = SIDInput.getText();

        int port1 = 0;

        try {
            port1 = Integer.parseInt(port);
        } catch (NumberFormatException e) {

            this.ErrorDialog("Port","El puerto no es un entero");
            return;
        }

        DataBase db = DataBase.getInstance();
        if ("".equals(host)) {
            this.ErrorDialog("Hostname","El hostname esta vacio");
        } else if ("".equals(port)) {
            this.ErrorDialog("Port","El Port esta vacio");
        } else if ("".equals(username)) {
            this.ErrorDialog("Username","El Username esta vacio");
        } else if ("".equals(password)) {
            this.ErrorDialog("Password","El Password esta vacio");
        } else if ("".equals(sid)) {
            this.ErrorDialog("SID","El SID esta vacio");
        } else {
            db.setConnection(host, port1, sid, username, password);
            if(db.isConnected()) {
                stage.close();
                InformationDialog("Se ha conectado!");
            }
            else
            {
                ErrorDialog("Conexion","No se ha podido establecer coneccion");
            }
        }
    }

    @FXML
    private void HostCheckAction(ActionEvent event) {
        if (this.Hostcheck.isSelected()) {
            Hostinput.setText("localhost");
        } else {
            Hostinput.setText("");
        }
    }

    @FXML
    private void PortCheckAction(ActionEvent event) {
        if (this.Portcheck.isSelected()) {
            Portinput.setText("1521");
        } else {
            Portinput.setText("");
        }

    }

    @FXML
    private void SIDCheckAction(ActionEvent event) {
         if (this.SIDcheck.isSelected()) {
            SIDInput.setText("XE");
        } else {
            SIDInput.setText("");
        }
    }
    
    @FXML void UsernameCheckAction(ActionEvent event){
        if(this.UsernameCheck.isSelected()){
            this.Usernameinput.setText("sys as sysdba");
        }
        else{
            this.Usernameinput.setText("");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
