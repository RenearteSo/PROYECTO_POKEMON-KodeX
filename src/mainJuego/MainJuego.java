package mainJuego;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Añadimos el "extends Application" para que sea una ventana
public class MainJuego extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Aquí es donde usamos las distintas clases de proyecto
        Parent root = FXMLLoader.load(getClass().getResource("../view/menu.fxml"));
        primaryStage.setTitle("Mundo Pokemon - KodeX");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        //Para poner la pantalla completa
        primaryStage.setFullScreen(true);
        //Para quitar el mensaje de pantalla completa pulsar ESC
        primaryStage.setFullScreenExitHint("");
 
        // primaryStage.setFullScreen(true);
        // Aquí llamamos a nuestro pokemons
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Comando que arranca la interfaz gráfica
        launch(args);
    }
}