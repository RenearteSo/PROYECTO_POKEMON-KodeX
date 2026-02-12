package mainJuego;

import javafx.application.Application;
import javafx.stage.Stage;

// Añadimos el "extends Application" para que sea una ventana
public class MainJuego extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Aquí es donde usamos las distintas clases de proyecto
        primaryStage.setTitle("Mundo Pokemon - KodeX");
        
        // Aquí llamamos a nuestro pokemons
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Comando que arranca la interfaz gráfica
        launch(args);
    }
}