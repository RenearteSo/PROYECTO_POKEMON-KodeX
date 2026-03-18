package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.util.Duration;

public class menuController {

	@FXML
	private Button botonCaptura, botonCombate, botonEntrenamiento, botonEquipo;
	@FXML
	private MediaView menuFondo;
	// Declaramos las variables para gestionar los botones de pantalla completa y
	// pantalla pequeña
	@FXML
	private ImageView botonPantalla;
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));
	// Inicializamos las variables para el sonido
	@FXML
	private ImageView botonSonido;
	@FXML
	private Slider sliderVolumen;
	private Image imgVolumen = new Image(getClass().getResourceAsStream("/img/Menu/volumenBajo.png"));

	private javafx.scene.media.MediaPlayer mediaPlayer;

	// ESTO SE EJECUTA AL ABRIR EL JUEGO
	@FXML
	public void initialize() {

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
		// Ponemos a Rayquaza directamente la primera vez
		// Aquí no hace falta fundido porque la ventana se está abriendo
		ponerVideo("VIDEO INICIO.mp4");

		// Esto obliga al vídeo a estirarse al tamaño de la ventana
		javafx.application.Platform.runLater(() -> {
			// CAMBIA "idDelVideo" POR EL NOMBRE QUE TENGA TU MEDIAVIEW EN EL CÓDIGO
			menuFondo.fitWidthProperty().bind(menuFondo.getScene().widthProperty());
			menuFondo.fitHeightProperty().bind(menuFondo.getScene().heightProperty());
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

		// Vinculamos el volumen del MediaPlayer al valor del Slider
		sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (mediaPlayer != null) {
				mediaPlayer.setVolume(newVal.doubleValue());
			}
		});

		// Mostramos el botón volumen
		botonSonido.setImage(imgVolumen);

		// Forzamos que empiece con el icono de encoger
		botonPantalla.setImage(imgEncoger);
	}

	// MÉTODO MAESTRO PARA CAMBIAR VÍDEOS CON FUNDIDO SUAVE
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

	// Método auxiliar para no repetir código de carga de vídeo
	private void ponerVideo(String nombreArchivo) {
		try {
			String ruta = getClass().getResource("/img/Menu/" + nombreArchivo).toExternalForm();
			javafx.scene.media.Media video = new javafx.scene.media.Media(ruta);
			mediaPlayer = new javafx.scene.media.MediaPlayer(video);
			mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
			menuFondo.setMediaPlayer(mediaPlayer);
			mediaPlayer.play();
		} catch (Exception e) {
			System.out.println("Error cargando " + nombreArchivo + ": " + e.getMessage());
		}
	}

	// EVENTOS DE LOS BOTONES (ENTRAR Y SALIR)

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

	// Método para mostrar el slider del volumen
	@FXML
	private void mostrarBarraVolumen(MouseEvent event) {
		// Si la barra está invisible, la muestra. Si está visible, la esconde.
		sliderVolumen.setVisible(!sliderVolumen.isVisible());
	}

	// Método para ocultar la barra del slider
	@FXML
	private void ocultarSlider(MouseEvent event) {
		sliderVolumen.setVisible(false);
	}
}