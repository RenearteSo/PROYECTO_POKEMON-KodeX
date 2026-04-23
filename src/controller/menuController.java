package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import model.Configuracion;
import model.Entrenador;

public class menuController implements DatosEntrenador{

	@FXML
	private Button botonCaptura, botonCombate, botonEntrenamiento, botonEquipo, botonCrianza, botonCasino;
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
	private Image imgVolMax = new Image(getClass().getResourceAsStream("/img/Menu/volumenMax.png"));
	private Image imgVolMin = new Image(getClass().getResourceAsStream("/img/Menu/volumenMin.png"));
	private Image imgVolMute = new Image(getClass().getResourceAsStream("/img/Menu/sinVolumen.png"));
	
	//Etiquetas para nombre y pokedollar
	@FXML
    private Label etiquetaPokeDollar, etiquetaUsuario;

	// Variable para los vídeos
	private javafx.scene.media.MediaPlayer mediaPlayer;

	// Variable global para la música, fuera de los métodos
	private javafx.scene.media.MediaPlayer musicaMenu;
	private Entrenador entrenadorLog;

	// ESTO SE EJECUTA AL ABRIR EL JUEGO
	@FXML
	public void initialize() {
		try {
			// Cargamos la fuente real desde el archivo
			Font pokemonFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PokemonGb-RAeo.ttf"), 25);

			// Si no se carga, lanzamos una excepción para que se registre en el log de
			// errores
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
		
		//Sincronizamos el slider con la configuración global para mantener el volumen entre vistas
		sliderVolumen.setValue(Configuracion.volumenMusica);

		// Iniciamos la música primero
		iniciarMusica();

		// Vinculamos el volumen del MediaPlayer al valor del Slider luego de instanciar
		// el método
		sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
			double vol = newVal.doubleValue();
			Configuracion.volumenMusica = vol;
			if (musicaMenu != null) {
				musicaMenu.setVolume(vol);

				// Cambiamos el icono según el nivel
				if (vol == 0) {
					botonSonido.setImage(imgVolMute);
				} else if (vol < 0.5) {
					botonSonido.setImage(imgVolMin);
				} else {
					botonSonido.setImage(imgVolMax);
				}
			}
		});
		
		// Esto fuerza al slider a moverse al arrancar para que el icono se actualice
		sliderVolumen.setValue(sliderVolumen.getValue());

		// Forzamos que empiece con el icono de encoger
		botonPantalla.setImage(imgEncoger);
	}
	
	//Método para recibir datos
	public void recibirDatos(Entrenador ent) {
		this.entrenadorLog = ent;
		//Mostramos el nombre del usuario/entrenador en el menú y sus pokedólares
				etiquetaUsuario.setText(ent.getNombreEntrenador());
				etiquetaPokeDollar.setText(Integer.toString(ent.getPokedollar()));
				
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

	// Método para poner la música
	private void iniciarMusica() {
		try {
			String ruta = getClass().getResource("/audio/musica/musicaMenu.mp3").toExternalForm();
			javafx.scene.media.Media sonido = new javafx.scene.media.Media(ruta);
			musicaMenu = new javafx.scene.media.MediaPlayer(sonido);

			musicaMenu.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
			// Usamos el valor global del volumen
			musicaMenu.setVolume(Configuracion.volumenMusica);
			musicaMenu.play();
		} catch (Exception e) {
			System.err.println("Error música: " + e.getMessage());
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
	void entrarCrianza(MouseEvent event) {
		botonCrianza.setScaleX(1.1);
		botonCrianza.setScaleY(1.1);
		cambiarVideoConEfecto("Crianza.mp4");
	}

	@FXML
	void salirCrianza(MouseEvent event) {
		botonCrianza.setScaleX(1.0);
		botonCrianza.setScaleY(1.0);
		cambiarVideoConEfecto("VIDEO INICIO.mp4");
	}

	@FXML
	void entrarCasino(MouseEvent event) {
		botonCasino.setScaleX(1.1);
		botonCasino.setScaleY(1.1);
		cambiarVideoConEfecto("Casino.mp4");
	}

	@FXML
	void salirCasino(MouseEvent event) {
		botonCasino.setScaleX(1.0);
		botonCasino.setScaleY(1.0);
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
	
	@FXML
	void irEquipo(MouseEvent event) {
	    cambiarEscena("equipo.fxml", botonEquipo);
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
		if (event.getClickCount() == 2) { // Si hace doble clic, mutea
			sliderVolumen.setValue(0);
		} else {
			sliderVolumen.setVisible(!sliderVolumen.isVisible());
		}
	}

	// Método para ocultar la barra del slider
	@FXML
	private void ocultarSlider(MouseEvent event) {
		sliderVolumen.setVisible(false);
	}

	// Creamos método genérico para cambiar de vistas
	// Se le pasa el nombre del FXML y el botón que tendrá el enveto clickar
	private void cambiarEscena(String fxml, Button botonReferencia) {
		try {
			// Paramos la música antes de cargar nada nuevo
			if (musicaMenu != null) {
				musicaMenu.stop();
				musicaMenu.dispose(); // Liberamos RAM 
			}

			// También paramos el vídeo de Rayquaza para que no consuma CPU
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.dispose(); //Liberar la memoria
			}

			// Buscamos el archivo FXML
			javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("../view/" + fxml));
			javafx.scene.Parent root = loader.load();

			Object controlador = loader.getController();
			if(controlador instanceof DatosEntrenador) {
				((DatosEntrenador) controlador).setEntrenador(this.entrenadorLog);
			}
			
			// Obtenemos el Stage y mantenemos el FullScreen
			javafx.stage.Stage stage = (javafx.stage.Stage) botonReferencia.getScene().getWindow();
			boolean enPantallaCompleta = stage.isFullScreen();

			// Lanzamos el escenario
			// Cambiamos la escena
			stage.getScene().setRoot(root);
			stage.setFullScreen(enPantallaCompleta);
			stage.setFullScreenExitHint("");

			stage.show();

		} catch (java.io.IOException e) {
			System.err.println("Error al cargar " + fxml + ": " + e.getMessage());
		}
	}

	// Creamos método para ir a la vista Captura
	@FXML
	void irCaptura(MouseEvent event) {
		// Usamos el método genérico
		cambiarEscena("captura.fxml", botonCaptura);
	}
	
	@FXML
	void irCasino(MouseEvent event) {
		// Usamos el método genérico
		cambiarEscena("casino.fxml", botonCasino);
	}
	
	@FXML
	void irCrianza(MouseEvent event) {
		// Usamos el método genérico
		cambiarEscena("crianza.fxml", botonCrianza);
	}
	//Método para ir a Entrenamiento
	@FXML
	void irEntrenamiento(MouseEvent event) {
		// Usamos el método genérico
		cambiarEscena("entrenamiento.fxml", botonEntrenamiento);
	}
	
	@FXML
	void irCombate(MouseEvent event) {
		// Usamos el método genérico
		cambiarEscena("eleccionCombate.fxml", botonCombate);
	}
	//Método implementado de la interfaz Datos Entrenador
	@Override
	public void setEntrenador(Entrenador e) {
		recibirDatos(e);
		
	}
}