package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import model.Configuracion;
import model.Entrenador;
import model.Pokemon;
import dao.EquipoDao;

import java.util.List;

// Implementamos DatosEntrenador para recibir y mandar al jugador entre pantallas
public class equipoController implements DatosEntrenador {

    // Listas y cuadrículas
    @FXML private ListView<Pokemon> listEquipo; // Muestra los Pokemon actualmente en el equipo (Maximo 6)
    @FXML private GridPane gridCaja; // Muestra los Pókemons guardados en la caja actual
    
    @FXML private Button btnMoverEquipo, btnMoverCaja, btnSalir; 
    @FXML private Button btnCajaAnterior, btnCajaSiguiente;
    @FXML private Label lblNombreCaja;
    
    // Panel de Estadísticas
    @FXML private ImageView imgStatsFoto;
    @FXML private Label lblStatSexo;
    @FXML private Label lblStatsNombre, lblStatsNivel;
    @FXML private Label lblStatVit, lblStatAtq, lblStatDef;
    @FXML private Label lblStatAtqEsp, lblStatDefEsp, lblStatVel;
    @FXML private Label lblStatEstamina, lblStatExp;

    // Botón de Pantalla Completa
    @FXML private ImageView botonPantalla;
    private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
    private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));

    @FXML private MediaView fondoVideo;
    private MediaPlayer mediaPlayer;

    // Variables para la música y el control de volumen
    private MediaPlayer musicaEquipo;
    @FXML private ImageView botonSonido;
    @FXML private Slider sliderVolumen;
    private Image imgVolMax = new Image(getClass().getResourceAsStream("/img/Menu/volumenMax.png"));
    private Image imgVolMin = new Image(getClass().getResourceAsStream("/img/Menu/volumenMin.png"));
    private Image imgVolMute = new Image(getClass().getResourceAsStream("/img/Menu/sinVolumen.png"));

    // --- VARIABLES INTERNAS ---
    private Entrenador jugadorActual; 
    private EquipoDao dao = new EquipoDao(); // Conexión a base de datos para el equipo
    private Pokemon pokemonSeleccionado; // Guarda temporalmente el Pokémon sobre el que se hace clic
    
    private int cajaActual = 1; // Número de la caja que se está visualizando
    private final int MAX_POR_CAJA = 21; 
    private List<Pokemon> todosLosPokemonCaja; // Lista completa de Pokémon en almacenamiento, se cambia según la caja actual

    // --- MÉTODOS DE INICIO ---
    //Recibe los datos del jugador desde la pantalla anterior, este método se llama automáticamente antes de mostrar la escena.
    
    @Override
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
        cargarDatosBaseDeDatos(); // Una vez sabemos quién es el jugador, cargamos sus Pokemons
    }

    @FXML
    public void initialize() {
    	// Deshabilitamos los botones de mover hasta que el jugador seleccione un Pokémon válido
        btnMoverEquipo.setDisable(true);
        btnMoverCaja.setDisable(true);
        
        // Vídeo del fondo 
        try {
            String ruta = getClass().getResource("/img/Equipo/Fondo_Equipo.mp4").toExternalForm();
            Media media = new Media(ruta);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            fondoVideo.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciamos la música y configuramos la barra de volumen
        iniciarMusica();
        if (sliderVolumen != null) {
            sliderVolumen.setVisible(false);
            sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
                double vol = newVal.doubleValue();
                if (musicaEquipo != null) musicaEquipo.setVolume(vol);
                if (vol == 0) {
                    botonSonido.setImage(imgVolMute);
                } else if (vol < 0.5) {
                    botonSonido.setImage(imgVolMin);
                } else {
                    botonSonido.setImage(imgVolMax);
                }
            });
            sliderVolumen.setValue(sliderVolumen.getValue());
        }

        // --- CÓDIGO PARA MOSTRAR EL EQUIPO EN CAJAS ---
        // Personalizamos cómo se dibuja cada celda de la lista del equipo 
        listEquipo.setCellFactory(param -> new ListCell<Pokemon>() {
            @Override
            protected void updateItem(Pokemon p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                 // Si la celda no tiene Pokémon la dejamos vacía e invisible
                    setStyle("-fx-background-color: transparent;"); 
                } else {
                    setText(null); // Ocultamos el texto por defecto de JAVAFX
                    
                    // Creamos la etiqueta del texto
                    Label lbl = new Label(p.getMote() + " (Nvl." + p.getNivel() + ")");
                    lbl.setStyle("-fx-text-fill: white; -fx-font-family: 'Pokemon GB'; -fx-font-size: 10;");
                    
                    // Metemos el texto en un Hbox para darle apariencia de rectangulo
                    HBox caja = new HBox(lbl);
                    caja.setStyle("-fx-background-color: rgba(10, 15, 35, 0.8); " + 
                                  "-fx-border-color: #88aaff; " +                   
                                  "-fx-border-radius: 5; " + 
                                  "-fx-background-radius: 5; " + 
                                  "-fx-padding: 8;");                               
                    
                    // Ponemos las cajas en las celdas y le damos padding para separarlas entre si
                    setGraphic(caja);
                    setStyle("-fx-background-color: transparent; -fx-padding: 4;");
                }
            }
        });

        // Lógica para el icono de pantalla completa
        Platform.runLater(() -> {
            if (botonPantalla != null && botonPantalla.getScene() != null) {
                Stage stage = (Stage) botonPantalla.getScene().getWindow();
                
                // Actualiza el icono de maximizar/minimizar
                botonPantalla.setImage(stage.isFullScreen() ? imgEncoger : imgAgrandar);
                stage.fullScreenProperty().addListener((obs, ov, nv) -> botonPantalla.setImage(nv ? imgEncoger : imgAgrandar));
                
                // Ancla el tamaño del vídeo al tamaño de la ventana para que se estire al maximizar
                fondoVideo.fitWidthProperty().bind(botonPantalla.getScene().widthProperty());
                fondoVideo.fitHeightProperty().bind(botonPantalla.getScene().heightProperty());
            }
        });
    }

    // Consultamos a la base de datos para obtener el Pókemon del jugador actual y llena la caja y el equipo actual.
    private void cargarDatosBaseDeDatos() {
        if (jugadorActual == null) return;
        
        // Cargamos y mostramos los Pókemons que se encuentran en EQUIPO
        listEquipo.getItems().clear();
        listEquipo.getItems().addAll(dao.obtenerPokemonPorUbicacion(jugadorActual.getIdEntrenador(), "EQUIPO"));
        
        // Cargamos los Pókemons que van en CAJA y la lógica de mientras mas Pókemons haya, la caja va cambiando.
        todosLosPokemonCaja = dao.obtenerPokemonPorUbicacion(jugadorActual.getIdEntrenador(), "CAJA");
        actualizarVistaCaja();
    }

    // --- LÓGICA DE LA CAJA: CUADRÍCULA Y PAGINACIÓN ---
    // Dibuja los Pókemons en la cuadrícula central (GridPane) y muestra solo los Pókemons correspondientes a la caja actual. (1,2,3...)

    private void actualizarVistaCaja() {
        gridCaja.getChildren().clear(); // Limpiamos los botones viejos
        lblNombreCaja.setText("Caja " + cajaActual);

        // Calculamos que rango de la lista toca mostrar en la caja actual.
        int indiceInicio = (cajaActual - 1) * MAX_POR_CAJA;
        int indiceFin = Math.min(indiceInicio + MAX_POR_CAJA, todosLosPokemonCaja.size());

        int columna = 0;
        int fila = 0;

        // Generamos un botón invisible por cada Pokémon de la página actual
        for (int i = indiceInicio; i < indiceFin; i++) {
            Pokemon p = todosLosPokemonCaja.get(i);

            Button btnPokemon = new Button();
            btnPokemon.setPrefSize(70, 90); 
            btnPokemon.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Pokemon GB'; -fx-font-size: 9;");
            
            // Botón transparente para que solo se vea la imagen  y el texto blanco
            try {
            	
            	// Obtenemos el nombre del archivo de la imagen y la cambiamos a GIF
                String r = p.getNumPokedex().getRutaImgFront();
                String arch = r.substring(r.lastIndexOf("/") + 1).replace(".png", ".gif");
                String rutaFinal = "/img/Pokemon/gif/Front/" + arch;
                Image img = new Image(getClass().getResourceAsStream(rutaFinal));
                
                // Configuramos el ícono del botón
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(50); 
                imgView.setFitHeight(50);
                imgView.setPreserveRatio(true);
                btnPokemon.setGraphic(imgView);
                btnPokemon.setText(p.getMote());
                btnPokemon.setContentDisplay(ContentDisplay.TOP);
            } catch (Exception e) {
            	
            	// Si no carga la imagen solo mostramos el nombre
                btnPokemon.setText(p.getMote()); 
            }

            // Al hacer click en un Pókemon de la caja
            btnPokemon.setOnAction(event -> {
                pokemonSeleccionado = p;
                listEquipo.getSelectionModel().clearSelection();  // Deseleccionamos la lista de la derecha
                actualizarPanelEstadisticas(p); // Cargamos sus datos abajo
                btnMoverEquipo.setDisable(false);  // Activamos botón "A EQUIPO"
                btnMoverCaja.setDisable(true); // Desactivamos "A CAJA" porque ya está ahí
            });

            gridCaja.add(btnPokemon, columna, fila);

            // Control de salto de línea en la cuadrícula
            columna++;
            if (columna >= 7) { 
                columna = 0;
                fila++;
            }
        }

     // Lógica para habilitar o deshabilitar las flechas de paginación de la caja
        btnCajaAnterior.setDisable(cajaActual == 1); // No podemos bajar de la Caja 1
        boolean haySiguiente = todosLosPokemonCaja.size() > (cajaActual * MAX_POR_CAJA);
        btnCajaSiguiente.setDisable(!haySiguiente); // Solo activamos > si quedan Pokémon por mostrar
    }

    @FXML
    public void accionCajaSiguiente(ActionEvent event) {
        cajaActual++;
        actualizarVistaCaja();
    }

    @FXML
    public void accionCajaAnterior(ActionEvent event) {
        if (cajaActual > 1) {
            cajaActual--;
            actualizarVistaCaja();
        }
    }

    // --- EVENTOS Y STATS ---
    // Al hacer clic en la lista del Equipo
    @FXML
    public void seleccionarDeEquipo(MouseEvent event) {
        pokemonSeleccionado = listEquipo.getSelectionModel().getSelectedItem();
        if (pokemonSeleccionado != null) {
            actualizarPanelEstadisticas(pokemonSeleccionado); // Cargamos sus datos abajo
            btnMoverCaja.setDisable(false);  // Activamos botón "A CAJA"
            btnMoverEquipo.setDisable(true); // Desactivamos "A EQUIPO" porque ya está ahí
        }
    }

    // Rellena el panel inferior negro con los atributos del Pokémon proporcionado.
    private void actualizarPanelEstadisticas(Pokemon p) {
        // Try-Catch de seguridad por si algún Label falla o no existe
        try {
            // Información básica
            if (lblStatsNombre != null) lblStatsNombre.setText(p.getMote());
            if (lblStatsNivel != null) lblStatsNivel.setText("Nivel: " + p.getNivel());
            
            // Estadísticas
            if (lblStatVit != null) lblStatVit.setText(String.valueOf(p.getVitalidad()));
            if (lblStatAtq != null) lblStatAtq.setText(String.valueOf(p.getAtaque()));
            if (lblStatDef != null) lblStatDef.setText(String.valueOf(p.getDefensa()));
            if (lblStatAtqEsp != null) lblStatAtqEsp.setText(String.valueOf(p.getAtaqueEspecial()));
            if (lblStatDefEsp != null) lblStatDefEsp.setText(String.valueOf(p.getDefensaEspecial()));
            if (lblStatVel != null) lblStatVel.setText(String.valueOf(p.getVelocidad()));
            if (lblStatEstamina != null) lblStatEstamina.setText(String.valueOf(p.getEstamina()));
            if (lblStatExp != null) lblStatExp.setText(String.valueOf(p.getExp()));

            // Lógica del Sexo
            if (lblStatSexo != null) {
                if (p.getSexo() != null) {
                    if (p.getSexo().equalsIgnoreCase("M")) {
                        lblStatSexo.setText("♂");
                        lblStatSexo.setStyle("-fx-text-fill: #00bfff; -fx-font-weight: bold;"); // Azul para macho
                    } else if (p.getSexo().equalsIgnoreCase("H")) {
                        lblStatSexo.setText("♀");
                        lblStatSexo.setStyle("-fx-text-fill: #ff69b4; -fx-font-weight: bold;"); // Rosa para hembra
                    } else {
                        lblStatSexo.setText(""); // Sin género
                    }
                } else {
                    lblStatSexo.setText(""); 
                }
            }
        } catch (Exception e) {
            System.err.println("Fallo al poner los textos en estadísticas: " + e.getMessage());
        }

        // // Carga de la foto del Pokémon en grande 
        try {
            if (imgStatsFoto != null) {
                String r = p.getNumPokedex().getRutaImgFront();
                String arch = r.substring(r.lastIndexOf("/") + 1).replace(".png", ".gif");
                String rutaFinal = "/img/Pokemon/gif/Front/" + arch;
                imgStatsFoto.setImage(new Image(getClass().getResourceAsStream(rutaFinal)));
            }
        } catch (Exception e) { 
            System.err.println("Fallo al cargar la foto: " + e.getMessage());
            if (imgStatsFoto != null) imgStatsFoto.setImage(null); 
        }
    }

    // --- MOVER POKÉMON ---

    //Intentamos mover el Pókemon seleccionado de la Caja al Equipo, falla si el Equipo ya tiene 6 integrantes.
    @FXML
    public void accionMoverAEquipo(ActionEvent event) {
    	
    	// Máximo 6 en el equipo
        if (listEquipo.getItems().size() >= 6) {
            mostrarAlerta("Equipo Lleno", "Ya tienes 6 Pokémon en el equipo.");
            return;
        }
        
        // Ejecuta el UPDATE en la BD y recarga la pantalla si hay éxito
        if (dao.moverPokemon(pokemonSeleccionado.getIdPokemon(), "EQUIPO")) {
            pokemonSeleccionado = null;
            cargarDatosBaseDeDatos(); 
        }
    }

    // Intenamos mover el Pókemon seleccionado desde el Equipo a la Caja, falla si el jugador intenta dejar el Equipo vacío.
    @FXML
    public void accionMoverACaja(ActionEvent event) {
    	
    	// No podemos quedarnos sin Pokémon activos
        if (listEquipo.getItems().size() <= 1) {
            mostrarAlerta("Atención", "¡No puedes quedarte sin Pokémon en el equipo!");
            return;
        }
        
        // Ejecuta el UPDATE en la BD y recarga la pantalla si hay éxito
        if (dao.moverPokemon(pokemonSeleccionado.getIdPokemon(), "CAJA")) {
            pokemonSeleccionado = null;
            cargarDatosBaseDeDatos(); 
        }
    }

    // --- BOTONES DE LA VENTANA ---

    // Bóton de pantalla completa 
    @FXML
    void gestionarPantalla(MouseEvent event) {
        Stage stage = (Stage) botonPantalla.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    // Método para reproducir la música de fondo
    private void iniciarMusica() {
        try {
            String ruta = getClass().getResource("/audio/musica/musicaEquipo.mp3").toExternalForm();
            javafx.scene.media.Media sonido = new javafx.scene.media.Media(ruta);
            musicaEquipo = new javafx.scene.media.MediaPlayer(sonido);
            musicaEquipo.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
            if (sliderVolumen != null) {
                musicaEquipo.setVolume(sliderVolumen.getValue());
            } else {
                musicaEquipo.setVolume(Configuracion.volumenMusica);
            }
            musicaEquipo.play();
        } catch (Exception e) {
            System.err.println("Error música: " + e.getMessage());
        }
    }

    // Muestra u oculta la barra de volumen al hacer clic en el icono
    @FXML
    private void mostrarBarraVolumen(MouseEvent event) {
        if (event.getClickCount() == 2) { 
            sliderVolumen.setValue(0);
        } else {
            sliderVolumen.setVisible(!sliderVolumen.isVisible());
        }
    }

    // Oculta la barra de volumen cuando el ratón sale de ella
    @FXML
    private void ocultarSlider(MouseEvent event) {
        sliderVolumen.setVisible(false);
    }

    // Cierra el menú de equipo y regresa al menú principal del juego.
    @FXML
    public void accionVolverMenu(ActionEvent event) {
        try {
        	// Detenemos el video
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            
            // Detenemos la música al salir de la pantalla
            if (musicaEquipo != null) {
                musicaEquipo.stop();
                musicaEquipo.dispose();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            
            // Devolvemos el jugador actual al menú para no perder la sesión
            Object controlador = loader.getController();
            if (controlador instanceof DatosEntrenador && this.jugadorActual != null) {
                ((DatosEntrenador) controlador).setEntrenador(this.jugadorActual);
            }
            
            // Aplicamos el cambio de escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostramos ventanas emergentes de aviso sin romper la pantalla completa
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        if (botonPantalla != null && botonPantalla.getScene() != null) {
            alert.initOwner(botonPantalla.getScene().getWindow());
        }
        
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}