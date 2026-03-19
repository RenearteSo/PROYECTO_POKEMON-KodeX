package controller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import model.Entrenador;
import model.Objeto;
import model.Pokedex;
import model.Pokemon;

public class capturaController implements DatosEntrenador{

    // Vinculamos los elementos del Scene Builder
    @FXML private ImageView fondoBatalla; 
    @FXML private ImageView imgEntrenador; 
    @FXML private ImageView imgPokemon; 
    @FXML private Label lblNombrePokemon;
    @FXML private Label lblNivelPokemon;
    @FXML private ProgressBar barraVida;
    @FXML private Label lblDialogo;
    @FXML private Button btnCapturar;   
    @FXML private Button btnHuir;       

    // Iconos para el control de la ventana
    @FXML private ImageView botonPantalla;
    private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
    private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));

    private Entrenador jugadorActual; 
    private Pokemon pokemonSalvaje;

    // Datos de conexión a la base de datos local
    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/pokemon?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String DB_USER = "root"; 
    private final String DB_PASS = "";     

    // Recibimos los datos del entrenador desde la pantalla anterior
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
        
        if (jugadorActual != null && jugadorActual.getRutaImgFrontal() != null) {
            try {
                String rutaEnt = jugadorActual.getRutaImgFrontal();
                String rutaFinal = rutaEnt.startsWith("/") ? rutaEnt : "/" + rutaEnt;
                InputStream stream = getClass().getResourceAsStream(rutaFinal);
                if (stream != null) imgEntrenador.setImage(new Image(stream));
            } catch (Exception e) {
                // Si falla la imagen del entrenador, el juego sigue
            }
        }
    }

    @FXML
    public void initialize() {
        lblDialogo.setText("¡Un Pokémon salvaje\napareció!");
        barraVida.setProgress(1.0); // Empezamos con la vida llena
        
        generarPokemonAleatorioBD();

        // Configuramos el comportamiento de la ventana al arrancar
        Platform.runLater(() -> {
            Stage stage = (Stage) botonPantalla.getScene().getWindow();
            
            // Vinculamos el tamaño del fondo al de la ventana para que sea responsive
            fondoBatalla.fitWidthProperty().bind(fondoBatalla.getScene().widthProperty());
            fondoBatalla.fitHeightProperty().bind(fondoBatalla.getScene().heightProperty());
            
            // Ajustamos el icono de pantalla según el estado actual
            botonPantalla.setImage(stage.isFullScreen() ? imgEncoger : imgAgrandar);

            // Por si pulsan ESC para actualizar el icono
            stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
                botonPantalla.setImage(newVal ? imgEncoger : imgAgrandar);
            });
        });
    }

    // Buscamos un Pokémon al azar en la BD y cargamos su info y tipo
    private void generarPokemonAleatorioBD() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            
            // Query para sacar los datos del Pokémon y su tipo principal
            String query = "SELECT p.NUM_POKEDEX, p.NOM_POKEMON, p.IMG_FRONT, t.NOM_TIPO " +
                           "FROM pokedex p " +
                           "LEFT JOIN pokedex_tipo pt ON p.NUM_POKEDEX = pt.NUM_POKEDEX " +
                           "LEFT JOIN tipos_pokemon t ON pt.ID_TIPO = t.ID_TIPO " +
                           "ORDER BY RAND() LIMIT 1";
                           
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("NOM_POKEMON");
                String rutaImgFront = rs.getString("IMG_FRONT"); 
                String tipo = rs.getString("NOM_TIPO");
                
                if (tipo == null || tipo.trim().isEmpty()) tipo = "NORMAL";

                // Creamos la instancia del Pokémon salvaje
                Pokedex infoPokedex = new Pokedex();
                infoPokedex.setNumPokedex(rs.getInt("NUM_POKEDEX"));
                pokemonSalvaje = new Pokemon(infoPokedex, nombre);
                pokemonSalvaje.setNivel(1);

                lblNombrePokemon.setText(nombre);
                
                // Cargamos la imagen desde la carpeta de recursos
                try {
                    String nombreArchivo = rutaImgFront.substring(rutaImgFront.lastIndexOf("/") + 1);
                    String rutaFinal = "/img/Pokemon/Front/" + nombreArchivo;
                    InputStream stream = getClass().getResourceAsStream(rutaFinal);
                    if (stream != null) imgPokemon.setImage(new Image(stream));
                } catch (Exception e) {}

                // Cambiamos el escenario según el tipo de pokemon que haya salido
                ponerFondoSegunTipo(tipo.trim().toUpperCase());
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblDialogo.setText("Error de conexión.");
        }
    }

    // Selección lógica del fondo de batalla
    private void ponerFondoSegunTipo(String tipo) {
        String rutaFondo = "";
        
        switch (tipo) {
            case "AGUA": rutaFondo = "/img/Captura/Imagenes/VISTA_AGUA.jpg"; break;
            case "BICHO": case "VENENO": rutaFondo = "/img/Captura/Imagenes/VISTA_BICHO-VENENO.jpg"; break;
            case "FANTASMA": case "PSIQUICO": rutaFondo = "/img/Captura/Imagenes/VISTA_FANTASMAS-PSIQUICO.jpg"; break;
            case "FUEGO": case "DRAGON": rutaFondo = "/img/Captura/Imagenes/VISTA_FUEGO-DRAGON.jpg"; break;
            case "HIELO": rutaFondo = "/img/Captura/Imagenes/VISTA_HIELO.jpg"; break;
            case "LUCHA": rutaFondo = "/img/Captura/Imagenes/VISTA_LUCHA.jpg"; break;
            case "VOLADOR": case "ELECTRICO": rutaFondo = "/img/Captura/Imagenes/VISTA_VOLADOR-ELECTRICO.jpg"; break;
            default: rutaFondo = "/img/Captura/Imagenes/VISTA_PLANTA-ROCA-TIERRA-NORMAL.jpg"; break;
        }

        try {
            InputStream stream = getClass().getResourceAsStream(rutaFondo);
            if (stream != null) fondoBatalla.setImage(new Image(stream));
        } catch (Exception e) {}
    }

    // Lógica al pulsar el botón de lanzar Pokéball
    @FXML
    public void accionCapturar(ActionEvent event) {
        lblDialogo.setText("¡Lanzaste una\nPokéball!");
        
        Objeto pokeball = new Objeto(); 
        pokeball.setNomObjeto("Pokéball");
        
        if(jugadorActual != null) jugadorActual.getMochila().add(pokeball);

        // Calculamos si hay éxito en la captura
        boolean capturado = (jugadorActual != null) 
            ? jugadorActual.capturarPokemon(pokemonSalvaje, pokeball) 
            : Math.random() < (2.0/3.0);

        if (capturado) {
            lblDialogo.setText("¡Ya está! ¡" + pokemonSalvaje.getMote() + "\natrapado!");
            btnCapturar.setDisable(true);
            btnHuir.setText("VOLVER"); 
        } else {
            lblDialogo.setText("¡Oh no! ¡El Pokémon\nse ha escapado!");
            btnCapturar.setDisable(true);
            btnHuir.setText("VOLVER");
        }
    }

    // Volvemos al menú principal manteniendo el modo de pantalla
    @FXML
    public void accionHuir(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            
            //Caarga los datos de jugador
            Object controlador = loader.getController();
            if (controlador instanceof DatosEntrenador) {
                ((DatosEntrenador) controlador).setEntrenador(this.jugadorActual);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            boolean enPantallaCompleta = stage.isFullScreen();
            stage.getScene().setRoot(root);
			stage.setFullScreen(enPantallaCompleta);
			stage.setFullScreenExitHint("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switch manual para alternar pantalla completa
    @FXML
    void gestionarPantalla(MouseEvent event) {
        Stage stage = (Stage) botonPantalla.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }
}