package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class menuController {

    @FXML private Button botonCaptura, botonCombate, botonEntrenamiento, botonEquipo;
    @FXML private MediaView menuFondo;

    //ESTO SE EJECUTA AL ABRIR EL JUEGO
    @FXML
    public void initialize() {
        // Ponemos a Rayquaza directamente la primera vez
        // Aquí no hace falta fundido porque la ventana se está abriendo
        ponerVideo("VIDEO INICIO.mp4");
    }

    //MÉTODO MAESTRO PARA CAMBIAR VÍDEOS CON FUNDIDO SUAVE
    private void cambiarVideoConEfecto(String nombreArchivo) {
        // Duración muy rápida 
        FadeTransition fundidoSalida = new FadeTransition(Duration.millis(200), menuFondo);
        fundidoSalida.setFromValue(menuFondo.getOpacity());
        fundidoSalida.setToValue(0.0);

        fundidoSalida.setOnFinished(event -> {
            ponerVideo(nombreArchivo);
            
            // Aparecer suavemente
            FadeTransition fundidoEntrada = new FadeTransition(Duration.millis(300), menuFondo);
            fundidoEntrada.setFromValue(0.0);
            fundidoEntrada.setToValue(1.0);
            fundidoEntrada.play();
        });
        fundidoSalida.play();
    }

    //Método auxiliar para no repetir código de carga de vídeo
    private void ponerVideo(String nombreArchivo) {
        try {
            String ruta = getClass().getResource("/img/Menu/" + nombreArchivo).toExternalForm();
            javafx.scene.media.Media video = new javafx.scene.media.Media(ruta);
            javafx.scene.media.MediaPlayer reproductor = new javafx.scene.media.MediaPlayer(video);
            reproductor.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
            menuFondo.setMediaPlayer(reproductor);
            reproductor.play();
        } catch (Exception e) {
            System.out.println("Error cargando " + nombreArchivo + ": " + e.getMessage());
        }
    }

    //EVENTOS DE LOS BOTONES (ENTRAR Y SALIR)

    @FXML
    void entrarEntrenamiento(MouseEvent event) {
        botonEntrenamiento.setScaleX(1.1);
        botonEntrenamiento.setScaleY(1.1);
        cambiarVideoConEfecto("Entrenamiento.mp4");
    }

    @FXML
    void salirEntrenamiento(MouseEvent event) {
        botonEntrenamiento.setScaleX(1.0);
        botonEntrenamiento.setScaleY(1.0);
        cambiarVideoConEfecto("VIDEO INICIO.mp4");
    }

    @FXML
    void entrarCombate(MouseEvent event) {
        botonCombate.setScaleX(1.1);
        botonCombate.setScaleY(1.1);
        cambiarVideoConEfecto("Combate.mp4");
    }

    @FXML
    void salirCombate(MouseEvent event) {
        botonCombate.setScaleX(1.0);
        botonCombate.setScaleY(1.0);
        cambiarVideoConEfecto("VIDEO INICIO.mp4");
    }

    @FXML
    void entrarCaptura(MouseEvent event) {
        botonCaptura.setScaleX(1.1);
        botonCaptura.setScaleY(1.1);
        cambiarVideoConEfecto("Captura.mp4");
    }

    @FXML
    void salirCaptura(MouseEvent event) {
        botonCaptura.setScaleX(1.0);
        botonCaptura.setScaleY(1.0);
        cambiarVideoConEfecto("VIDEO INICIO.mp4");
    }

    @FXML
    void entrarEquipo(MouseEvent event) {
        botonEquipo.setScaleX(1.1);
        botonEquipo.setScaleY(1.1);
        cambiarVideoConEfecto("Snorlax.mp4");
    }

    @FXML
    void salirEquipo(MouseEvent event) {
        botonEquipo.setScaleX(1.0);
        botonEquipo.setScaleY(1.0);
        cambiarVideoConEfecto("VIDEO INICIO.mp4");
    }
}