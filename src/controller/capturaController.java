package controller;

import java.io.InputStream;
import java.util.Optional;
import java.util.regex.Pattern;
import dao.CapturaDao;
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
import javafx.stage.Stage;
import model.*;

// Implementamos la interfaz DatosEntrenador para poder el jugador desde otras pantallas
public class capturaController implements DatosEntrenador {

	// Elementos de la interfaz FXML
    @FXML private ImageView fondoBatalla, imgEntrenador, imgPokemon, botonPantalla;
    @FXML private Label lblNombrePokemon, lblNivelPokemon, lblDialogo;
    @FXML private ProgressBar barraVida;
    @FXML private Button btnCapturar, btnHuir;

    // Imagenes para el botón de pantalla completa
    private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
    private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));

    // Variables
    private Entrenador jugadorActual; // Guarda los datos del jugador que esta jugando
    private Pokemon pokemonSalvaje; // Guarda los datos del pokemon que vamos a capturar
    private CapturaDao dao = new CapturaDao(); // Instancia de nuestra conexion a Base de Datos

    // Este método recibe al jugador desde el Menú Principal, sirve para saber quien esta jugando en ese momento.
    @Override
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
        
        // Ponemos la imagen por defecto del entrenador
        String rutaImagen = "/img/Entrenador/pokemon_entrenador.png";

        // Si el jugador existe en nuestra Base de Datos y tiene una ruta de imagen de un entrenador la usaremos.
        if (jugadorActual != null) {
            String r = jugadorActual.getRutaImgFrontal();
            if (r != null && !r.trim().isEmpty()) {
                rutaImagen = r; 
            }
        }

        // Intentamos cargar la imagen en la posición de la pantalla
        try {
            InputStream st = getClass().getResourceAsStream(rutaImagen.startsWith("/") ? rutaImagen : "/" + rutaImagen);
            if (st != null) imgEntrenador.setImage(new Image(st));
        } catch (Exception e) { System.out.println("Error imagen entrenador"); }
    }

    // Este método se ejecuta automáticamente cuando se abre la pantalla
    @FXML
    public void initialize() {
    	
    	// Mensaje que aparece en la caja de texto grande
        lblDialogo.setText("¡Un Pokémon salvaje\napareció!");
        cargarPokemonDesdeDao();

        // El Platform.runLater espera que la ventana se cree para ajustar los tamaños
        Platform.runLater(() -> {
            if (fondoBatalla.getScene() != null) {
                Scene scene = fondoBatalla.getScene();
                Stage stage = (Stage) scene.getWindow();
                
                // Hacemos que la imagen del fondo se estire al tamaño de la ventana
                fondoBatalla.fitWidthProperty().bind(scene.widthProperty());
                fondoBatalla.fitHeightProperty().bind(scene.heightProperty());
                
                // Calculamos la posicion del Pokemon para que quede en su lugar siempre ya sea en pantalla pequeña o grande
                fondoBatalla.fitWidthProperty().addListener((obs, ov, nv) -> imgPokemon.setLayoutX(nv.doubleValue() * 0.65));
                fondoBatalla.fitHeightProperty().addListener((obs, ov, nv) -> imgPokemon.setLayoutY(nv.doubleValue() * 0.50));
                
                // Al pulsar el botón de pantalla completa el icono cambia
                if (botonPantalla != null) {
                    botonPantalla.setImage(stage.isFullScreen() ? imgEncoger : imgAgrandar);
                    stage.fullScreenProperty().addListener((obs, ov, nv) -> botonPantalla.setImage(nv ? imgEncoger : imgAgrandar));
                }
            }
        });
    }

    // Traemos un Pókemon aleatorio usando DAO y preparamos la pantalla
    private void cargarPokemonDesdeDao() {
        Object[] datosBD = dao.generarPokemonSalvaje();
        
        // Si la base de datos nos devolvió información correctamente.
        if (datosBD != null && datosBD[0] != null) {
            pokemonSalvaje = (Pokemon) datosBD[0];
            
            // Ponemos los datos del Pókemon en las etiquetas Label de la interfaz.
            lblNombrePokemon.setText(pokemonSalvaje.getMote());
            lblNivelPokemon.setText("Lv " + pokemonSalvaje.getNivel());
            barraVida.setProgress(1.0); // Siempre tiene la vida al máximo
            
            // Cargamos la imagen del Pókemon
            try {
                String r = pokemonSalvaje.getNumPokedex().getRutaImgFront();
                String arch = r.substring(r.lastIndexOf("/") + 1);
                imgPokemon.setImage(new Image(getClass().getResourceAsStream("/img/Pokemon/Front/" + arch)));
            } catch (Exception e) {}
            
            // Cambiamos el fondo de pantalla dependiendo del tipo de Pókemon que sea
            ponerFondoSegunTipo(((String) datosBD[1]).trim().toUpperCase());
        }
    }

 // Método que se ejecuta al pulsar el botón CAPTURAR
    @FXML
    public void accionCapturar(ActionEvent event) {
        if (pokemonSalvaje == null || jugadorActual == null) return;

        try {
            // Preparamos la Pókeball para lanzarla
            Objeto pb = new Objeto(); 
            pb.setNomObjeto("Pokéball");
            if (jugadorActual.getMochila() == null) return;
            
            // Nos tenemos que asegurar de que haya una Pókeball en la mochila del jugador
            boolean tiene = false;
            for (Objeto o : jugadorActual.getMochila()) {
                if (o.getNomObjeto().equalsIgnoreCase("Pokéball")) { tiene = true; break; }
            }
            if (!tiene) jugadorActual.getMochila().add(pb);

            // Intentamos la captura usando el método de los compañeros.
            if (jugadorActual.capturarPokemon(pokemonSalvaje, pb)) {
                boolean guardadoExitoso = false;

                // Seguira pidiendo el nombre hasta que sea válido y lo guarde
                while (!guardadoExitoso) {
                    
                    // Ventana emergente que pide el mote
                    TextInputDialog tid = new TextInputDialog(pokemonSalvaje.getMote());
                    tid.setTitle("¡Capturado!");
                    tid.setHeaderText("¡Has atrapado a " + pokemonSalvaje.getMote() + "!");
                    tid.setContentText("Introduce un mote (Solo letras):");
                    
                    Optional<String> res = tid.showAndWait();
                    
                    // Si el usuario cierra la ventana o pulsa cancelar, salimos del bucle para crasheos despues
                    if (!res.isPresent()) {
                        break;
                    }

                    String moteElegido = res.get();

                    // Comprobamos que el nombre sea Legal sin palabrotas, números ni espacios en blanco.
                    if (validarMote(moteElegido)) {
                        
                        // Comprueba que el entrenador que esta en ese momento no tenga repetido un mismo mote
                        if (dao.existeMoteParaEntrenador(jugadorActual.getIdEntrenador(), moteElegido)) {
                            mostrarAlerta("Mote repetido", "Tú ya usas ese nombre. ¡Elige otro!");
                        } else {
                            // Si todo es correcto, asigna el mote
                            pokemonSalvaje.setMote(moteElegido);
                            
                            // Comprobamos si el equipo ya esta llenó (ya tiene 6 Pokemons) y los envia a la caja
                            int cantidadEnEquipo = dao.contarPokemonEquipo(jugadorActual.getIdEntrenador());
                            String ub = (cantidadEnEquipo < 6) ? "EQUIPO" : "CAJA";
                            
                            // Guardamos en la base de datos
                            guardadoExitoso = dao.guardarCapturaEnBD(jugadorActual.getIdEntrenador(), pokemonSalvaje, ub);
                            
                            if (guardadoExitoso) {
                                lblDialogo.setText("¡Atrapado con éxito!\nGuardado en " + ub);
                                btnCapturar.setDisable(true);
                                btnHuir.setText("VOLVER");
                            }
                        }
                    } else {
                        mostrarAlerta("Mote no válido", "¡No se pueden usar palabrotas, números ni espacios en blanco!");
                    }
                }
            } else {
                // Si al capturar fallamos
                lblDialogo.setText("¡Oh no! ¡El Pokémon\nse ha escapado!");
                btnCapturar.setDisable(true);
                btnHuir.setText("VOLVER");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Validamos que el texto solo sean letras y que no hayan espacios en blanco, números, palabrotas...
    private boolean validarMote(String mote) {
    	
    	// Permitimos letras de la A a la Z con ñ y tíldes incluidas.
        boolean regexOk = Pattern.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+$", mote);
        
        // Filtro de palabras prohibidas
        String[] prohibidas = {"tonto", "feo", "caca", "mierda", "imbecil", "puta"}; 
        for (String p : prohibidas) {
            if (mote.toLowerCase().contains(p)) return false;
        }
        return regexOk;
    }

    // Método para mostrar mensajes de advertencia si algo sale mal
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Seleccionamos la imagen de fondo correcta dependiendo del tipo Pókemon que salga.
    private void ponerFondoSegunTipo(String tipo) {
        String rf = switch (tipo) {
            case "AGUA" -> "/img/Captura/Imagenes/VISTA_AGUA.jpg";
            case "BICHO", "VENENO" -> "/img/Captura/Imagenes/VISTA_BICHO-VENENO.jpg";
            case "FUEGO", "DRAGON" -> "/img/Captura/Imagenes/VISTA_FUEGO-DRAGON.jpg";
            case "HIELO" -> "/img/Captura/Imagenes/VISTA_HIELO.jpg";
            case "FANTASMA", "PSIQUICO" -> "/img/Captura/Imagenes/VISTA_FANTASMA-PSIQUICO.jpg";
            case "LUCHA" -> "/img/Captura/Imagenes/VISTA_LUCHA.jpg";
            case "VOLADOR", "ELECTRICO" -> "/img/Captura/Imagenes/VISTA_VOLADOR-ELECTRICO.jpg";
            default -> "/img/Captura/Imagenes/VISTA_PLANTA-ROCA-TIERRA-NORMAL.jpg";
        };
        try { fondoBatalla.setImage(new Image(getClass().getResourceAsStream(rf))); } catch (Exception e) {}
    }
    
    // Activa o desactiva la pantalla completa al hacer clic en el botón de la esquina.
    @FXML
    void gestionarPantalla(MouseEvent event) {
        Stage stage = (Stage) fondoBatalla.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    // Si usamos el botón HUIR sale de la pantalla captura y regresa al menú principal
    @FXML
    public void accionHuir(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            
            // Pasamos el jugador actual al menú para que no pierda la sesión
            Object controlador = loader.getController();
            if (controlador instanceof DatosEntrenador && this.jugadorActual != null) {
                ((DatosEntrenador) controlador).setEntrenador(this.jugadorActual);
            }
            
            // Cambiamos la escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }
}