package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dao.ConexionDB;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
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
    private ImageView botonPantalla;
    
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));
	
    
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
    void login(ActionEvent event) throws SQLException {
    	// ANIMACION AL PULSAR ENTRAR
        ScaleTransition click = new ScaleTransition(Duration.millis(200), confirmarLogin);
        click.setToX(0.99);
        click.setToY(0.99);
        click.setCycleCount(2);
        click.setAutoReverse(true);
        click.play();
    	
        // Si hay campos vacios:
    	if(usuario.getText().isEmpty()) {
    		errorLogin.setText("Error: Inserta un nombre de usuario.");
    		errorLogin.setVisible(true);
    	} else if(passwd.getText().isEmpty()) { 
    		errorLogin.setText("Error: Inserta una contraseña.");
    		errorLogin.setVisible(true);
    	} else { 
    		// campos rellenos
    		errorLogin.setVisible(false);
    		String loginUsuario = usuario.getText();
    		String loginContrasenia = passwd.getText();
    		
    		
    		// CONEXIÓN BASE DE DATOS //
    		ConexionDB conexionObj = new ConexionDB();  // nos conectamos desde ConexionDB
            Connection connectDB = conexionObj.getConnection(); // obtenemos esa conexión
    		
            if (connectDB != null) { // si la conexion es correcta
            	try {
            	String verificarLogin = "SELECT PASSWORD " // buscamos la contraseña en la base de datos
            			+ "FROM ENTRENADOR "
            			+ "WHERE NOM_ENTRENADOR = ? "; // ? para luego reemplazar por el valor introducido
            	
            	PreparedStatement statement = connectDB.prepareStatement(verificarLogin); // preparamos la consulta
            	
            	statement.setString(1, loginUsuario); // cambiamos el primer ? del prepareStatement
            	
            	ResultSet rs = statement.executeQuery(); // realizamos la consulta
            	
            	    if (rs.next()) { // si existe
            	    	String passwordBD = rs.getString("PASSWORD"); // Obtenemos la contraseña de la base de datos
            	    	if (passwordBD.equals(loginContrasenia)) {
            	        abrirMenu();
            	    } else {
                  // el usuario existe pero la contraseña está mal
                       errorLogin.setText("Contraseña incorrecta. Intentalo de nuevo.");
                       errorLogin.setVisible(true);
            	    }
            	    } else { // el usuario no existe, lo registramos
            	    	preguntarRegistro(loginUsuario, loginContrasenia, connectDB);
            	    }
            	}	catch(SQLException e) {
            	e.printStackTrace(); 
                errorLogin.setText("Error al conectar con la base de datos.");
                errorLogin.setVisible(true);
        	} finally {
        		connectDB.close();
        	}
           } else {
        	   errorLogin.setText("Error: No se ha podido conectar a la base de datos.");
       		   errorLogin.setVisible(true);
           }
    	}
    }
    
    // metodo para preguntar si crear un usuario en la base de datos
    private void preguntarRegistro(String nuevoUsuario, String nuevaContra, Connection connectDB) { 
    	// mostramos una alerta de confirmacion si el usuario no existe para poder registrarlo.
    	javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Usuario no encontrado");
        alert.setHeaderText("El entrenador '" + nuevoUsuario + "' no existe.");
        alert.setContentText("¿Quieres crear un nuevo entrenador con estas credenciales?");
        
        // mostramos la ventana y esperamos a que el usuario pulse un botón
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        
        // si el usuario pulsa aceptar, lo registramos
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            crearNuevoEntrenador(nuevoUsuario, nuevaContra, connectDB);
        } else {
            // si cancela, ponemos en el label que no existe
            errorLogin.setText("El usuario no existe.");
            errorLogin.setVisible(true);
        }
    }
    
    // registro de un nuevo entrenador en la base de datos
    private void crearNuevoEntrenador(String usuario, String contrasenia, Connection connectDB) {
    	
    	// pokedolares iniciales (800-1000)
    	int dineroInicial = (int) (Math.random() * (1000 - 800 + 1) + 800);
    	
    	// preparamos la consulta SQL
    	
    	String sql = "INSERT INTO ENTRENADOR (NOM_ENTRENADOR, PASSWORD, TIPO_ENTRENADOR, POKEDOLLAR, NIVEL_ENTRENADOR) "
                + "VALUES (?, ?, 'JUGADOR', ?, 1)";
    	
    	try (java.sql.PreparedStatement pstmt = connectDB.prepareStatement(sql)) {
            
            // le asignamos los parametros al prepareStatement
            pstmt.setString(1, usuario);       // sustituye el primer '?' por el nombre.
            pstmt.setString(2, contrasenia);         
            pstmt.setInt(3, dineroInicial);    // pokedollars entre 800 y 1000
            
            int filasAfectadas = pstmt.executeUpdate(); // realizamos la ejecucion a la base de datos
            
            if (filasAfectadas > 0) {
                // USUARIO CREADO
            	abrirMenu(); // pasamos al menu principal
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            errorLogin.setText("Error al registrar en la base de datos.");
            errorLogin.setVisible(true);
        }
    }
 
    private void abrirMenu() {
        try {
            // cargamos la nueva vista desde el paquete view
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("../view/menu.fxml"));
            javafx.scene.Parent root = loader.load(); // cargamos la vista
            
         // Obtenemos la ventana (Stage) actual desde la escena donde esta el boton entrar.
            javafx.stage.Stage stage = (javafx.stage.Stage) confirmarLogin.getScene().getWindow();
            
            // cambiamos la escena y la mostramos
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
            
        } catch (java.io.IOException e) {
            errorLogin.setText("Error al cargar la pantalla de menú.");
            errorLogin.setVisible(true);
            e.printStackTrace();
        }
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
		try {
			//Cargamos la fuente real desde el archivo
		    Font pokemonFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PokemonGb-RAeo.ttf"), 25);
		    
		    // Si no se carga, lanzamos una excepción para que se registre en el log de errores
		    if (pokemonFont == null) {
		        throw new Exception("La fuente /fonts/PokemonGb-RAeo.ttf no pudo ser localizada.");
		    }
		} catch (Exception e) {
		    System.err.println("Error al cargar recursos: " + e.getMessage());
		}
		
		// Esto obliga al vídeo a estirarse al tamaño de la ventana
		javafx.application.Platform.runLater(() -> {
			// CAMBIA "idDelVideo" POR EL NOMBRE QUE TENGA TU MEDIAVIEW EN EL CÓDIGO
			videoFondo.fitWidthProperty().bind(videoFondo.getScene().widthProperty());
			videoFondo.fitHeightProperty().bind(videoFondo.getScene().heightProperty());
		});
		
		Platform.runLater(() -> {
			Stage stage = (Stage) botonPantalla.getScene().getWindow();
			// Esto mira si la pantalla cambia a FullScreen por cualquier motivo
			stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal) { // Si es true está en grande
					botonPantalla.setImage(imgEncoger);
				} else { // Si es false está en pequeño
					botonPantalla.setImage(imgAgrandar);
				}
			});
		});
		
		botonPantalla.setImage(imgEncoger);
		
		

    	// VIDEO FONDO//
    	MediaPlayer mediaPlayer;
    	try {
            URL mediaUrl = getClass().getResource("/img/Login/fondo_login.mp4");
            
            if (mediaUrl == null) {
                throw new Exception("No se encontró el archivo fondo_login.mp4 en /img/Login/");
            }

            // creamos media y player
            Media media = new Media(mediaUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
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
