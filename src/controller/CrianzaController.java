package controller;

import javafx.event.ActionEvent;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

import dao.CrianzaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Configuracion;
import model.Entrenador;
import model.Pokedex;
import model.Pokemon;

public class CrianzaController implements DatosEntrenador {

	// Elementos de scene builder
	@FXML
	private Button botonAbrirHuevo, botonCriar, botonVolver;
	@FXML
	private VBox cajaHuevo, cajaMacho;
	@FXML
	private ComboBox<Pokemon> desplegableMacho, desplegableHembra; // Cambiado a <Pokemon>
	@FXML
	private Label etiquetaDinero, etiquetaMensaje;
	@FXML
	private ImageView imgHembra, imgHuevo, imgMacho, fondoCrianza;
	@FXML
	private ImageView botonPantalla;
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));
	private javafx.scene.media.MediaPlayer musicaCrianza;

	// Variables
	private Entrenador entrenadorActual;
	private Pokemon huevoObtenido;
	// Guardará todas las hembras y machos del jugador sin filtrar
	private LinkedList<Pokemon> todasLasHembras = new LinkedList<>();
	private LinkedList<Pokemon> todosLosMachos = new LinkedList<>();

	public void initialize() {
		// Ocultamos el botón de abrir huevo y la imagen al principio.
		botonAbrirHuevo.setVisible(false);
		imgHuevo.setVisible(false);
		etiquetaMensaje.setText("Selecciona un padre y una madre.");
		
		//Código para que el fondo se adapte a la pantalla completa y minimizada
		javafx.application.Platform.runLater(() -> {
			if (fondoCrianza.getScene() != null) {
				fondoCrianza.fitWidthProperty().bind(fondoCrianza.getScene().widthProperty());
				fondoCrianza.fitHeightProperty().bind(fondoCrianza.getScene().heightProperty());
			}
		});

		// Iniciamos la música
		iniciarMusica();

		// Aplicamos la fábrica a la lista desplegable, cuando abrimos el desplegable
		desplegableMacho.setCellFactory(crearCeldaPokemon());
		desplegableHembra.setCellFactory(crearCeldaPokemon());

		// Aplicamos la fábrica al botón" principal, cuando está cerrado y ya hemos
		// elegido
		desplegableMacho.setButtonCell(crearCeldaPokemon().call(null));
		desplegableHembra.setButtonCell(crearCeldaPokemon().call(null));

		// Listener del macho
		desplegableMacho.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				cargarImagenPokemon(newVal, imgMacho);

				// Comprobamos qué hembra hay seleccionada ahora mismo
				Pokemon hembraActual = desplegableHembra.getValue();

				// Si no hay hembra, o la hembra es de otra especie, entonces filtramos las
				// hembras
				if (hembraActual == null
						|| hembraActual.getNumPokedex().getNumPokedex() != newVal.getNumPokedex().getNumPokedex()) {
					desplegableHembra.getItems().clear();
					imgHembra.setImage(null);

					for (Pokemon hembra : todasLasHembras) {
						if (hembra.getNumPokedex().getNumPokedex() == newVal.getNumPokedex().getNumPokedex()) {
							desplegableHembra.getItems().add(hembra);
						}
					}

					if (desplegableHembra.getItems().isEmpty()) {
						etiquetaMensaje.setText("No tienes ninguna hembra compatible para este Pokémon.");
					} else {
						etiquetaMensaje.setText("Selecciona una pareja compatible.");
					}
				}
			}
		});

		// Listener de la hembra
		desplegableHembra.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				cargarImagenPokemon(newVal, imgHembra);

				// Comprobamos qué macho hay seleccionado ahora mismo
				Pokemon machoActual = desplegableMacho.getValue();

				// Si no hay macho, o el macho es de otra especie, entonces filtramos los machos
				if (machoActual == null
						|| machoActual.getNumPokedex().getNumPokedex() != newVal.getNumPokedex().getNumPokedex()) {
					desplegableMacho.getItems().clear();
					imgMacho.setImage(null);

					for (Pokemon macho : todosLosMachos) {
						if (macho.getNumPokedex().getNumPokedex() == newVal.getNumPokedex().getNumPokedex()) {
							desplegableMacho.getItems().add(macho);
						}
					}

					if (desplegableMacho.getItems().isEmpty()) {
						etiquetaMensaje.setText("No tienes ningún macho compatible para este Pokémon.");
					} else {
						etiquetaMensaje.setText("Selecciona una pareja compatible.");
					}
				}
			}
		});

		// Forzamos que empiece con el icono de encoger
		botonPantalla.setImage(imgEncoger);

	}

	// Método de la interfaz para recibir los datos del entrenador actual
	@Override
	public void setEntrenador(Entrenador e) {
		this.entrenadorActual = e;
		etiquetaDinero.setText("Pokedóllares: " + e.getPokedollar());
		cargarDesplegables();

	}

	// Método para llenar los ComboBox filtrando por sexos
	private void cargarDesplegables() {
		try {
			CrianzaDao dao = new CrianzaDao();
			LinkedList<Pokemon> listaPokemon = dao.ObtenerEquipoEntrenador(entrenadorActual.getIdEntrenador());

			// Limpiamos todo por si acaso
			desplegableMacho.getItems().clear();
			desplegableHembra.getItems().clear();
			todasLasHembras.clear();
			todosLosMachos.clear();

			for (Pokemon p : listaPokemon) {
				if (p.getSexo().equalsIgnoreCase("M")) {
					todosLosMachos.add(p);
					desplegableMacho.getItems().add(p);
				} else if (p.getSexo().equalsIgnoreCase("H")) {
					// Guardamos las hembras en nuestro almacén global
					todasLasHembras.add(p);
					// Y también las mostramos al principio
					desplegableHembra.getItems().add(p);
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			etiquetaMensaje.setText("Error al conectar con la base de datos.");
		}

	}

	@FXML
	void accionCriar(ActionEvent event) {
		Pokemon padre = desplegableMacho.getValue();
		Pokemon madre = desplegableHembra.getValue();

		// Comprobamos que se hayan seleccionado los dos.
		if (padre == null || madre == null) {
			etiquetaMensaje.setText("Falta seleccionar a uno de los padres");
			return;
		}
		// Comprobamos que se hayan seleccionado los dos
		if (padre == null || madre == null) {
			etiquetaMensaje.setText("Falta seleccionar a uno de los padres.");
			return;
		}
		// Comprobamos si son la misma especie
		if (padre.getNumPokedex().getNumPokedex() != madre.getNumPokedex().getNumPokedex()) {
			etiquetaMensaje.setText("¡Deben ser de la misma especie para criar!");
			return; // Cortamos la ejecución aquí
		}

		// Comprobamos la fertilidad
		if (padre.getFertilidad() <= 0 || madre.getFertilidad() <= 0) {
			etiquetaMensaje.setText("¡Uno de los padres ya no es fértil!");
			return;
		}

		// 4. Comprobamos el dinero (asumimos que cuesta 200)
		if (entrenadorActual.getPokedollar() < 200) {
			etiquetaMensaje.setText("¡No tienes dinero suficiente (200$)!");
			return;
		}
		// Llamamos al método que creamos en la clase Entrenador (Cobra 200$ y creal al
		// hijo)
		this.huevoObtenido = entrenadorActual.ponerACriar(padre, madre);

		if (this.huevoObtenido != null) {
			etiquetaMensaje.setText("¡Han puesto un huevo!");
			etiquetaDinero.setText("Pokédollars: " + entrenadorActual.getPokedollar()); // Actualiza el dinero

			// Bloqueamos los controller para que no se críen más veces seguidas
			desplegableMacho.setDisable(true);
			desplegableHembra.setDisable(true);
			botonCriar.setDisable(true);

			// Hacemos aparecer el huevo y el botón para abrirlo
			imgHuevo.setImage(new Image(getClass().getResourceAsStream("/img/Crianza/huevoPokemon.png")));
			imgHuevo.setVisible(true);

			botonAbrirHuevo.setVisible(true);

		} else {
			// Por si acaso falla por algún otro motivo extraño
			etiquetaMensaje.setText("Error desconocido al intentar criar.");

		}
	}

	// MEvento para abrir el huevo pokemon y que aparezca nuestro nuevo pokemon
	@FXML
	void accionAbrirHuevo(ActionEvent event) {
		CrianzaDao dao = new CrianzaDao(); // Lo creamos aquí arriba para usarlo en todo el método

		try {
			// El bebé con id corregido
			int idReal = huevoObtenido.getNumPokedex().getNumPokedex();

			// Le pedimos al DAO la info real 
			Pokedex infoReal = dao.obtenerInfoPokedex(idReal);

			// Se lo ponemos a nuestro huevo
			huevoObtenido.setNumPokedex(infoReal);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error obteniendo la Pokédex del bebé real.");
		}

		// Variable para comprobar el mote
		boolean moteValido = false;
		// Bucle para pedir el mote hasta que sea correcto
		while (!moteValido) {
			// Obtenemos el nombre de la especie para sugerirlo
			String nombreEspecie = huevoObtenido.getNumPokedex().getNomPokemon();

			TextInputDialog ventanaPregunta = new TextInputDialog(nombreEspecie);

			// Para que no se cierre la pantalla completa!!!
			ventanaPregunta.initOwner(imgHuevo.getScene().getWindow());

			ventanaPregunta.setTitle("¡Ha nacido!");
			ventanaPregunta.setHeaderText("¡Felicidades! Ha salido un " + nombreEspecie + " del huevo.");
			ventanaPregunta.setContentText("Ponle un mote a tu nuevo compañero (Solo letras):");

			Optional<String> respuesta = ventanaPregunta.showAndWait();

			if (!respuesta.isPresent()) {
				// Si el jugador cierra la ventana o le da a cancelar, le dejamos el nombre de
				// la especie por defecto
				huevoObtenido.setMote(nombreEspecie);
				break;
			}

			String moteElegido = respuesta.get();

			// Comprobamos que el nombre no tenga números ni palabrotas
			if (validarMote(moteElegido)) {
				// Comprobamos si está repetido primero
				try {
					dao.CapturaDao daoCaptura = new dao.CapturaDao(); // Usamos método de la clase captura DAO para
																		// comprobar si existe ya el mote
					if (daoCaptura.existeMoteParaEntrenador(entrenadorActual.getIdEntrenador(), moteElegido)) {
						mostrarAlerta("Mote repetido", "Tú ya tienes un Pokémon con ese nombre. ¡Elige otro!");
					} else {
						// Asignamos el mote
						huevoObtenido.setMote(moteElegido);
						moteValido = true; // Salimos del bucle
					}
				} catch (Exception e) {
					System.out.println("Error comprobando mote repetido");
				}
			}
		}

		try {

			// Lo guardamos de verdad en la base de datos
			dao.guardarNacimiento(entrenadorActual, desplegableMacho.getValue(), desplegableHembra.getValue(),
					huevoObtenido);
			// Mostramos mensaje con el nombre del pokemon y el numero pokedex
			etiquetaMensaje
					.setText("¡Felicidades! Ha nacido un " + huevoObtenido.getNumPokedex().getNomPokemon() + ".");
			botonAbrirHuevo.setDisable(true);

			// Cargamos la foto del recién nacido donde antes estaba el huevo
			cargarImagenPokemon(huevoObtenido, imgHuevo);
		} catch (SQLException ex) {
			ex.printStackTrace();
			etiquetaMensaje.setText("Error al registrar el nacimiento en la BD.");
		}
	}

	// Reutilizamos el método para cambiar pantalla al menú principal
	// Método genérico para cambiar de vistas
	private void cambiarEscena(String fxml, Button botonReferencia) {
		try {
			// Paramos la música antes de salir de la guardería
			if (musicaCrianza != null) {
				musicaCrianza.stop();
				musicaCrianza.dispose(); // Liberamos memoria
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

	// Acción para volver al menú
	@FXML
	void accionVolver(ActionEvent event) {
		// Usamos tu método genérico para volver al menú
		cambiarEscena("menu.fxml", botonVolver);
	}

	// Método para cargar la imagen del pokemon
	private void cargarImagenPokemon(Pokemon p, ImageView visor) {
		try {
			String rutaDB = p.getNumPokedex().getRutaImgFront();

			// Cambiamos el texto .png por .gif
			rutaDB = rutaDB.replace(".png", ".gif");

			// Cogemos solamente el nombre del archivo
			String nombreArchivo = rutaDB.substring(rutaDB.lastIndexOf("/") + 1);
			String rutaFinal = "/img/Pokemon/gif/Front/" + nombreArchivo;

			InputStream stream = getClass().getResourceAsStream(rutaFinal);
			if (stream != null) {
				visor.setImage(new Image(stream));
			} else {
				visor.setImage(null);
			}
		} catch (Exception e) {
			visor.setImage(null);
		}

	}

	// Validamos que el mote introducido solo sean letras y sin espacios ni
	// palabrotas. Reciclamos código
	private boolean validarMote(String mote) {
		// Comprobamos que no se pase del límite del tamaño que asignamos en la base de
		// datos
		if (mote.length() > 20 || mote.length() < 1) {
			return false;
		}

		// Necesitas importar java.util.regex.Pattern arriba del todo si no lo tienes
		boolean regexOk = java.util.regex.Pattern.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+$", mote);

		String[] prohibidas = { "tonto", "feo", "caca", "mierda", "imbecil", "puta" };
		for (String p : prohibidas) {
			if (mote.toLowerCase().contains(p))
				return false;
		}
		return regexOk;
	}

	// Método para mostrar mensajes de advertencia si intenta poner un nombre feo
	private void mostrarAlerta(String titulo, String contenido) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
		// Para que no quite la pantalla completa
		alert.initOwner(imgHuevo.getScene().getWindow());
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		alert.showAndWait();
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

	// Método para poner la música
	private void iniciarMusica() {
		try {
			String ruta = getClass().getResource("/audio/musica/musicaCrianza.mp3").toExternalForm();
			javafx.scene.media.Media sonido = new javafx.scene.media.Media(ruta);
			musicaCrianza = new javafx.scene.media.MediaPlayer(sonido);

			musicaCrianza.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
			// Usamos el valor global del volumen
			musicaCrianza.setVolume(Configuracion.volumenMusica);
			musicaCrianza.play();
		} catch (Exception e) {
			System.err.println("Error música: " + e.getMessage());
		}
	}

	// Método para dibujar las filas del desplegable con foto y texto
	private javafx.util.Callback<javafx.scene.control.ListView<Pokemon>, javafx.scene.control.ListCell<Pokemon>> crearCeldaPokemon() {
		return param -> new javafx.scene.control.ListCell<Pokemon>() {
			@Override
			protected void updateItem(Pokemon p, boolean empty) {
				super.updateItem(p, empty);

				if (empty || p == null) {
					setText(null);
					setGraphic(null);
				} else {
					// Ponemos el texto
					setText(p.getMote() + " (Nv. " + p.getNivel() + ")");

					// Preparamos el icono
					ImageView icono = new ImageView();
					icono.setFitHeight(30); // Tamaño miniatura
					icono.setFitWidth(30);
					icono.setPreserveRatio(true);

					// Buscamos la foto
					try {
						String rutaDB = p.getNumPokedex().getRutaImgFront();
						String nombreArchivo = rutaDB.substring(rutaDB.lastIndexOf("/") + 1);
						String rutaFinal = "/img/Pokemon/Front/" + nombreArchivo;
						InputStream stream = getClass().getResourceAsStream(rutaFinal);
						if (stream != null) {
							icono.setImage(new Image(stream));
						}
					} catch (Exception e) {
						// Si falla la imagen, se queda solo con el texto
					}

					// Se lo metemos a la celda
					setGraphic(icono);
				}
			}
		};
	}

}
