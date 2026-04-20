package controller;

import javafx.event.ActionEvent;
import java.io.InputStream;
import java.util.LinkedList;

import dao.EntrenamientoDao;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Configuracion;
import model.Entrenador;
import model.Entrenamiento;
import model.Pokedex;
import model.Pokemon;

public class EntrenamientoController implements DatosEntrenador {

	@FXML
	private Button botonEntrenar, botonVolver;
	@FXML
	private ComboBox<Entrenamiento> desplegableEntrenamiento;
	@FXML
	private Label etiquetaAtaque, etiquetaAtaqueEsp, etiquetaDefensa, etiquetaDefensaEsp, etiquetaMensaje,
			etiquetaNombre, etiquetaVelocidad, etiquetaVitalidad, preAtaque, preAtaqueEsp, preDefensa, preDefensaEsp,
			preVelocidad, preVitalidad, etiquetaPokedollares;
	@FXML
	private MediaView fondoVideo;
	@FXML
	private ImageView imagenPokemon;
	@FXML
	private TilePane panelCuadricula;
	@FXML
	private TilePane panelEquipo;

	private Entrenador entrenadorActual;
	private Pokemon pokemonSeleccionado;
	private EntrenamientoDao dao = new EntrenamientoDao();

	@FXML
	private ImageView botonPantalla;
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));
	// Variable para la música
	private javafx.scene.media.MediaPlayer musicaEntrenamiento;

	// Método para iniciar la vista

	@FXML
	public void initialize() {
		// 1. Cargamos el vídeo de fondo
		try {
			String rutaVideo = getClass().getResource("/img/Entrenamiento/entrenamientoSakura.mp4").toExternalForm();
			MediaPlayer reproductor = new MediaPlayer(new Media(rutaVideo));
			fondoVideo.setMediaPlayer(reproductor);
			reproductor.setCycleCount(MediaPlayer.INDEFINITE);
			reproductor.setMute(true);
			reproductor.play();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Aviso: No se pudo cargar el vídeo de fondo.");
		}

		// Código para que el fondo se adapte a la pantalla completa y minimizada
		javafx.application.Platform.runLater(() -> {
			if (fondoVideo.getScene() != null) {
				fondoVideo.fitWidthProperty().bind(fondoVideo.getScene().widthProperty());
				fondoVideo.fitHeightProperty().bind(fondoVideo.getScene().heightProperty());
			}
		});
		// Forzamos que empiece con el icono de encoger
		botonPantalla.setImage(imgEncoger);

		iniciarMusica();

		// Tenemos que cargar el comboBox con los tipos de entrenamiento
		desplegableEntrenamiento.getItems().addAll(Entrenamiento.values());

		// Líneas para mostrar las bonificaciones de los entrenamientos
		desplegableEntrenamiento.setOnAction(event -> {
			Entrenamiento entrenoElegido = desplegableEntrenamiento.getValue();
			if (entrenoElegido != null && pokemonSeleccionado != null) {
				mostrarPreEntrenamiento(entrenoElegido);
			}
		});

	}

	// Reutilizamos el método para cambiar pantalla al menú principal
	// Método genérico para cambiar de vistas
	private void cambiarEscena(String fxml, Button botonReferencia) {
		try {
			// Paramos la música antes de salir de la guardería
			if (musicaEntrenamiento != null) {
				musicaEntrenamiento.stop();
				musicaEntrenamiento.dispose(); // Liberamos memoria
			}

			// Buscamos el archivo FXML
			javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/" + fxml));
			javafx.scene.Parent root = loader.load();

			// Pasamos el entrenador a la siguiente vista
			Object controlador = loader.getController();
			if (controlador instanceof DatosEntrenador) {
				((DatosEntrenador) controlador).setEntrenador(this.entrenadorActual);
			}

			// Obtenemos el Stage y mantenemos el FullScreen
			javafx.stage.Stage stage = (javafx.stage.Stage) botonReferencia.getScene().getWindow();
			boolean enPantallaCompleta = stage.isFullScreen();

			// Cambiamos la escena
			stage.getScene().setRoot(root);
			stage.setFullScreen(enPantallaCompleta);
			stage.setFullScreenExitHint("");
			stage.show();

		} catch (java.io.IOException e) {
			System.err.println("Error al cargar " + fxml + ": " + e.getMessage());
		}
	}

	// Método para poner la música
	private void iniciarMusica() {
		try {
			String ruta = getClass().getResource("/audio/musica/musicaEntrenamiento.mp3").toExternalForm();
			javafx.scene.media.Media sonido = new javafx.scene.media.Media(ruta);
			musicaEntrenamiento = new javafx.scene.media.MediaPlayer(sonido);

			musicaEntrenamiento.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
			// Usamos el valor global del volumen
			musicaEntrenamiento.setVolume(Configuracion.volumenMusica);
			musicaEntrenamiento.play();
		} catch (Exception e) {
			System.err.println("Error música: " + e.getMessage());
		}
	}

	// Método para generar los pokemons que tenemos guardados en la base de datos
	private void cargarCuadriculaPokemon() {
		panelEquipo.getChildren().clear(); // Limpiamos el equipo
		panelCuadricula.getChildren().clear(); // Limpiamos la caja
		// Obtenemos todos nuestros pokemons
		LinkedList<Pokemon> misPokemons = dao.obtenerTodosLosPokemon(entrenadorActual.getIdEntrenador());
		int contador = 0; // Para saber cuántos metemos en el equipo

		for (Pokemon p : misPokemons) {
			VBox tarjeta = new VBox();
			// Damos estilo a la cuadrícula y a los contenedores de ella
			tarjeta.setAlignment(Pos.CENTER);
			tarjeta.setSpacing(5);
			tarjeta.setStyle(
					"-fx-padding: 10; -fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10; -fx-border-color: #A9A9A9; -fx-border-radius: 10; -fx-cursor: hand;");

			// Estilo de la imagen
			ImageView foto = new ImageView();
			foto.setFitWidth(50);
			foto.setFitHeight(50);
			cargarImagenPokemon(p, foto);

			// Damos estilo a las etiquetas con el nombre y nivel
			Label nombre = new Label(p.getMote() + " (Nv." + p.getNivel() + ")");
			nombre.setStyle(
					"-fx-font-family: 'Pokemon GB'; -fx-font-size: 10px; -fx-text-fill: #000000; -fx-font-weight: bold;");

			tarjeta.getChildren().addAll(foto, nombre);

			// Al hacer clic en la tarjeta del Pokémon mostramos stats
			tarjeta.setOnMouseClicked(event -> {
				this.pokemonSeleccionado = p;
				mostrarStatsEnPantalla(p);
				desplegableEntrenamiento.getSelectionModel().clearSelection();
				limpiarPrevias();
				etiquetaMensaje.setText("");
			});
			// Lógica para separar las dos cuadrículas
			if (contador < 6) {
				panelEquipo.getChildren().add(tarjeta);
			} else {
				panelCuadricula.getChildren().add(tarjeta);
			}
			contador++;

		}
	}

	// Método del botón entrenar
	@FXML
	void accionEntrenar(ActionEvent event) {
		// Comprobaciones previas antes de generar el entrenamiento como tal
		if (pokemonSeleccionado == null) {
			etiquetaMensaje.setText("¡Elige un Pokémon primero!");
			return;
		}
		Entrenamiento entreno = desplegableEntrenamiento.getValue();
		if (entreno == null) {
			etiquetaMensaje.setText("¡Elige un tipo de entrenamiento!");
			return;
		}
		// Calculamos el conste de estamina que gasta el entrenamiento y los
		// pokedóllares que tiene el entrenados
		int coste = entreno.calcularCoste(pokemonSeleccionado);
		if (entrenadorActual.getPokedollar() < coste) {
			etiquetaMensaje.setText("¡No tienes dinero! Cuesta " + coste + "$.");
			return;
		}
		// Entrenamos y guardamos en la base de datos las nuevas stats
		entrenadorActual.entrenarPokemon(pokemonSeleccionado, entreno);

		// Damos extra de experiencia por haber completado el entrenamiento
		pokemonSeleccionado.setExp(pokemonSeleccionado.getExp() + 25);

		// Comprobamos si puede subir de nivel
		boolean subioNivel = pokemonSeleccionado.subirNivel();
		boolean evoluciono = false;

		// Comprobamos si ese nuevo nivel provoca evolución
		if (subioNivel) {
			int idNuevaEvolucion = pokemonSeleccionado.comprobarEvolucion(); // Devuelve la ID o -1

			if (idNuevaEvolucion != -1) {
				// Vamos a la BD a buscar cómo es este nuevo pokemon
				Pokedex nuevaPokedex = dao.obtenerPokedexPorId(idNuevaEvolucion);

				if (nuevaPokedex != null) {
					// Evoluciona al nuevo pokemon
					pokemonSeleccionado.evolucionar(nuevaPokedex);
					evoluciono = true;
				}
			}
		}
		boolean guardadoOk = dao.guardarEntrenamiento(entrenadorActual, pokemonSeleccionado);

		// Cambiamos los textos y fotos según lo que haya pasado
		if (guardadoOk) {

			if (evoluciono) {
				etiquetaMensaje.setText("¡Hala! ¡" + pokemonSeleccionado.getMote() + " ha evolucionado!");
				etiquetaMensaje.setStyle("-fx-text-fill: #00FFFF; -fx-font-family: 'Pokemon GB'; -fx-font-size: 11px;");

				// Cambiar la foto principal por la del nuevo Pokémon
				cargarImagenPokemon(pokemonSeleccionado, imagenPokemon);

			} else if (subioNivel) {
				etiquetaMensaje.setText("¡" + pokemonSeleccionado.getMote() + " subió al Nivel "
						+ pokemonSeleccionado.getNivel() + "!");
				etiquetaMensaje.setStyle("-fx-text-fill: #FFD700; -fx-font-family: 'Pokemon GB'; -fx-font-size: 11px;");

			} else {
				etiquetaMensaje.setText("¡" + pokemonSeleccionado.getMote() + " ha mejorado sus stats!");
				etiquetaMensaje.setStyle("-fx-text-fill: white; -fx-font-family: 'Pokemon GB'; -fx-font-size: 11px;");
			}

			// Refrescamos los números de las stats
			mostrarStatsEnPantalla(pokemonSeleccionado);
			limpiarPrevias();
			desplegableEntrenamiento.getSelectionModel().clearSelection();
			animarPokemon();

			// Refrescamos el dinero
			etiquetaPokedollares.setText("Pokédollars: " + entrenadorActual.getPokedollar());

			// Recargamos la lista de la izquierda para que se actualicen los niveles y
			// miniatura si evolucionó
			cargarCuadriculaPokemon();

		} else {
			etiquetaMensaje.setText("Error en la Base de Datos...");
			etiquetaMensaje.setStyle("-fx-text-fill: red;");
		}
	}

	// Método para limpiar datos de pokemon de la pantalla, ponemos stats a 0
	private void limpiarPantalla() {
		etiquetaNombre.setText("Selecciona un Pokémon");
		imagenPokemon.setImage(null);
		etiquetaAtaque.setText("-");
		etiquetaDefensa.setText("-");
		etiquetaVitalidad.setText("-");
		etiquetaAtaqueEsp.setText("-");
		etiquetaDefensaEsp.setText("-");
		etiquetaVelocidad.setText("-");
		etiquetaMensaje.setText("");
		limpiarPrevias();

	}

	// Método para mostrar las stats al seleccionar pokemons actualiza todas las
	// labels que hemos usado
	private void mostrarStatsEnPantalla(Pokemon p) {
		etiquetaNombre.setText(p.getMote() + " - Nivel " + p.getNivel());
		cargarImagenPokemon(p, imagenPokemon);

		etiquetaAtaque.setText(String.valueOf(p.getAtaque()));
		etiquetaDefensa.setText(String.valueOf(p.getDefensa()));
		etiquetaVitalidad.setText(String.valueOf(p.getVitalidad()));
		etiquetaAtaqueEsp.setText(String.valueOf(p.getAtaqueEspecial()));
		etiquetaDefensaEsp.setText(String.valueOf(p.getDefensaEspecial()));
		etiquetaVelocidad.setText(String.valueOf(p.getVelocidad()));

	}

	// Método para cargar la imagen del pokemon de nuestra base de datos
	private void cargarImagenPokemon(Pokemon p, ImageView foto) {
		try {
			// Buscamos los gifs que queremos para mostrar al pokemon antes de entrenar
			String rutaDB = p.getNumPokedex().getRutaImgFront();
			rutaDB = rutaDB.replace(".png", ".gif");
			String nombreArchivo = rutaDB.substring(rutaDB.lastIndexOf("/") + 1);
			String rutaFinal = "/img/Pokemon/gif/Front/" + nombreArchivo;

			InputStream stream = getClass().getResourceAsStream(rutaFinal);
			if (stream != null) {
				foto.setImage(new Image(stream));
			} else {
				foto.setImage(null);
			}
		} catch (Exception e) {
			foto.setImage(null);
		}
	}

	// Método para mostrar una previa de las bonificaciones de cada entrenamiento
	private void mostrarPreEntrenamiento(Entrenamiento entrenoElegido) {
		limpiarPrevias();
		// Damos estilo a los numeritos que serán las modificaciones y lo que subirán
		// las stats
		String mejora = "(+5)";
		String estilo = "-fx-text-fill: #32CD32; -fx-font-family: 'Pokemon GB'; -fx-font-size: 10px;";

		switch (entrenoElegido) {
		case PESADO:
			preDefensa.setText(mejora);
			preDefensa.setStyle(estilo);
			preDefensaEsp.setText(mejora);
			preDefensaEsp.setStyle(estilo);
			preVitalidad.setText(mejora);
			preVitalidad.setStyle(estilo);
			break;
		case FURIOSO:
			preAtaque.setText(mejora);
			preAtaque.setStyle(estilo);
			preAtaqueEsp.setText(mejora);
			preAtaqueEsp.setStyle(estilo);
			preVelocidad.setText(mejora);
			preVelocidad.setStyle(estilo);
			break;
		case FUNCIONAL:
			preAtaque.setText(mejora);
			preAtaque.setStyle(estilo);
			preDefensa.setText(mejora);
			preDefensa.setStyle(estilo);
			preVelocidad.setText(mejora);
			preVelocidad.setStyle(estilo);
			preVitalidad.setText(mejora);
			preVitalidad.setStyle(estilo);
			break;
		case ONIRICO:
			preAtaqueEsp.setText(mejora);
			preAtaqueEsp.setStyle(estilo);
			preDefensaEsp.setText(mejora);
			preDefensaEsp.setStyle(estilo);
			preVelocidad.setText(mejora);
			preVelocidad.setStyle(estilo);
			preVitalidad.setText(mejora);
			preVitalidad.setStyle(estilo);
			break;
		}

	}

	// Animación para cuando se complete el entrenamiento
	private void animarPokemon() {
		TranslateTransition salto = new TranslateTransition(Duration.millis(150), imagenPokemon);
		salto.setByY(-25);
		salto.setCycleCount(4);
		salto.setAutoReverse(true);
		salto.play();
	}

	// Método para recibir los datos del entrenador
	public void setEntrenador(Entrenador entrenador) {
		this.entrenadorActual = entrenador;
		cargarCuadriculaPokemon();
		limpiarPantalla();
		etiquetaPokedollares.setText("Pokédollars: " + entrenadorActual.getPokedollar()); // Actualiza el dinero
	}

	// Método que usamos para limpiar las stats al seleccionar otro pokemon de la
	// cuadrícula
	private void limpiarPrevias() {
		preAtaque.setText("");
		preDefensa.setText("");
		preVitalidad.setText("");
		preAtaqueEsp.setText("");
		preDefensaEsp.setText("");
		preVelocidad.setText("");

	}

	// Método para volver al menú principal
	@FXML
	void accionVolver(ActionEvent event) {
		// Usamos tu método genérico para volver al menú
		cambiarEscena("menu.fxml", botonVolver);
	}

	// Método para controlar el tamaño de la pantalla
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
	

}
