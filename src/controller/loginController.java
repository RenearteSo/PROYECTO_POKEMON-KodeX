package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class loginController implements Initializable {

    @FXML
    private Button cancelarLogin;

    @FXML
    private Button confirmarLogin;

    @FXML
    private Label errorLogin;

    @FXML
    private Label labelUsuario;

    @FXML
    private ImageView logoKodexTexto;

    @FXML
    private Pane panelFondoIzq;

    @FXML
    private PasswordField passwd;

    @FXML
    private TextField usuario;
    
    @FXML
    private MediaView videoFondo;
    
    @FXML
    void animarEntradaBoton(javafx.scene.input.MouseEvent event) {
    	// subimos un poco y movemos
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), confirmarLogin);
        tt.setToY(-5); 
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), confirmarLogin);
        st.setToX(1.05);
        st.setToY(1.05);
        st.play();
    }

    @FXML
    void animarSalidaBoton(javafx.scene.input.MouseEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), confirmarLogin);
        tt.setToY(0);
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), confirmarLogin);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }
    

    @FXML
    void cerrarLogin(ActionEvent event) {
    	Stage st = (Stage) cancelarLogin.getScene().getWindow();
    	st.close();
    }
    
    @FXML
    void login(ActionEvent event) {
    	// Pequeño salto de escala al pulsar
        ScaleTransition click = new ScaleTransition(Duration.millis(200), confirmarLogin);
        click.setToX(0.99);
        click.setToY(0.99);
        click.setCycleCount(2);
        click.setAutoReverse(true);
        click.play();
    	
    	if(usuario.getText().isEmpty()) {
    		errorLogin.setText("Error: Inserta un nombre de usuario.");
    		errorLogin.setVisible(true);
    	} else if(passwd.getText().isEmpty()) {
    		errorLogin.setText("Error: Inserta una contraseña.");
    		errorLogin.setVisible(true);
    	} else {
    		errorLogin.setVisible(false);
    		String loginUsuario = usuario.getText();
    		String loginContraseña = passwd.getText();
    	}
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	// VIDEO FONDO//
    	MediaPlayer mediaPlayer;
    	try {
            URL mediaUrl = getClass().getResource("/img/Login/fondo_login.mp4");
            
            if (mediaUrl == null) {
                throw new Exception("No se encontró el archivo fondo_login.mp4 en /img/Login/");
            }

            // creamos media y player
            Media media = new Media(mediaUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media); // Recuerda tener 'private MediaPlayer mediaPlayer;' declarado arriba
            videoFondo.setMediaPlayer(mediaPlayer);
            // lo mandamos al fondo
            videoFondo.toBack();
            
            // arreglamos resolucion
            videoFondo.setPreserveRatio(false);
            videoFondo.fitWidthProperty().bind(panelFondoIzq.widthProperty());
            videoFondo.fitHeightProperty().bind(panelFondoIzq.heightProperty());
            videoFondo.setSmooth(true);

            // reproducimos en mute y infinito
            mediaPlayer.setMute(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

        //    System.out.println("Video funciona.");

            // --- ANIMACIÓN DE ENTRADA VIDEO ---
            videoFondo.setOpacity(0);
            FadeTransition ftVideo = new FadeTransition(Duration.seconds(2), videoFondo); // fadeTransition
            ftVideo.setToValue(1.0);
            ftVideo.play();

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            // Si el video falla, ponemos un fondo para que no se vea vacio
            panelFondoIzq.setStyle("-fx-background-color: #bc2f2f;");
        }
    	
    	// ANIMACION LOGO //
        //logo invisible
        logoKodexTexto.setOpacity(0);

        //transicion
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), logoKodexTexto);
        fadeIn.setFromValue(0); // invisible...
        fadeIn.setToValue(1); 
        
        fadeIn.play();
        
        TranslateTransition subir = new TranslateTransition(Duration.seconds(2), logoKodexTexto);
        subir.setFromY(-20); 
        subir.setToY(0); 
        subir.play();
        
 
        
    }
}
