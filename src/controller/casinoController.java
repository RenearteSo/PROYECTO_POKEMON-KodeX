package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class casinoController implements Initializable {

    @FXML
    private ImageView botonPantalla;

    @FXML
    private Button btnAdivinar;

    @FXML
    private Button btnCaraCruz;

    @FXML
    private Button btnRuleta;

    @FXML
    private Button btnVolver;

    @FXML
    private Label labelSaldo;

    @FXML
    private Pane panelFondoCasino;

    @FXML
    private MediaView videoFondoCasino;
    
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));


	@FXML
    void animarEntradaBoton(javafx.scene.input.MouseEvent event) {
        // Capturamos el botón sobre el que está el ratón
        Button btn = (Button) event.getSource();
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), btn);
        tt.setToY(-5); 
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(1.05);
        st.setToY(1.05);
        st.play();
    }

    @FXML
    void animarSalidaBoton(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), btn);
        tt.setToY(0);
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }
	// Método para los botones de agrandar y minimizar según en la pantalla que
	// estemos
	@FXML
	void gestionarPantalla(MouseEvent event) {
		// Obtenemos la ventana actual
		Stage stage = (Stage) botonPantalla.getScene().getWindow();

		if (!stage.isFullScreen()) {
			stage.setFullScreen(true);
			botonPantalla.setImage(imgEncoger);
		} else {
			stage.setFullScreen(false);
			botonPantalla.setImage(imgAgrandar);
		}
	}   
	@FXML
	 private void volverMenu() {
	        try {
	            // cargamos la nueva vista desde el paquete view
	            FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("../view/menu.fxml"));
	            Parent root = loader.load(); // cargamos la vista
	            
	         // Obtenemos la ventana (Stage) actual desde la escena donde esta el boton entrar.
	            Stage stage = (javafx.stage.Stage) btnVolver.getScene().getWindow();
	            
	            // cambiamos la escena y la mostramos
	            stage.setScene(new javafx.scene.Scene(root));
	            stage.show();
	            
	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        }
	    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	// VIDEO FONDO//
    	MediaPlayer mediaPlayer;
    	try {
    		// CAMBIAR FONDO!!!
            URL mediaUrl = getClass().getResource("/img/Login/fondo_login.mp4");
            
            if (mediaUrl == null) {
                throw new Exception("No se encontró el archivo fondo_login.mp4 en /img/Login/");
            }
            
    		// Esto obliga al vídeo a estirarse al tamaño de la ventana
    		Platform.runLater(() -> {
    			// CAMBIA "idDelVideo" POR EL NOMBRE QUE TENGA TU MEDIAVIEW EN EL CÓDIGO
    			videoFondoCasino.fitWidthProperty().bind(videoFondoCasino.getScene().widthProperty());
    			videoFondoCasino.fitHeightProperty().bind(videoFondoCasino.getScene().heightProperty());
    		});

            // creamos media y player
            Media media = new Media(mediaUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            videoFondoCasino.setMediaPlayer(mediaPlayer);
            // lo mandamos al fondo
            videoFondoCasino.toBack();
            
            // arreglamos resolucion
            videoFondoCasino.setPreserveRatio(false);
            videoFondoCasino.fitWidthProperty().bind(panelFondoCasino.widthProperty());
            videoFondoCasino.fitHeightProperty().bind(panelFondoCasino.heightProperty());
            videoFondoCasino.setSmooth(true);

            // reproducimos en mute y infinito
            mediaPlayer.setMute(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

        //    System.out.println("Video funciona.");

            // --- ANIMACIÓN DE ENTRADA VIDEO ---
            videoFondoCasino.setOpacity(0);
            FadeTransition ftVideo = new FadeTransition(Duration.seconds(2), videoFondoCasino); // fadeTransition
            ftVideo.setToValue(1.0);
            ftVideo.play();

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            // Si el video falla, ponemos un fondo para que no se vea vacio
            panelFondoCasino.setStyle("-fx-background-color: #bc2f2f;");
        }
		
	}
}
