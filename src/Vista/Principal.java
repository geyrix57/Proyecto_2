/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Modelo.Clasificacion.Usuario;
import Modelo.EsquemaClasificacion.Usuarios;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Geykel
 */
public class Principal extends Application {
    public static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
       
        Principal.stage = stage;
        Parent root = FXMLLoader.load(Principal.class.getResource("Registro.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
