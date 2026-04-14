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
import javafx.stage.Stage;

import model.Entrenador;
import model.Pokemon;
import dao.EquipoDao;

import java.util.List;

// Implementamos DatosEntrenador para recibir y mandar al jugador entre pantallas
public class equipoController implements DatosEntrenador {

    // --- CONEXIONES CON SCENE BUILDER (fx:id) ---
    @FXML private ListView<Pokemon> listEquipo;
    @FXML private GridPane gridCaja;
    
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

    // --- VARIABLES INTERNAS ---
    private Entrenador jugadorActual;
    private EquipoDao dao = new EquipoDao();
    private Pokemon pokemonSeleccionado; 
    
    private int cajaActual = 1; 
    private final int MAX_POR_CAJA = 21; 
    private List<Pokemon> todosLosPokemonCaja;

    // --- MÉTODOS DE INICIO ---

    @Override
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
        cargarDatosBaseDeDatos();
    }

    @FXML
    public void initialize() {
        // Apagamos los botones de mover al entrar
        btnMoverEquipo.setDisable(true);
        btnMoverCaja.setDisable(true);
        
        // Configuramos la lista del equipo simple 
        listEquipo.setCellFactory(param -> new ListCell<Pokemon>() {
            @Override
            protected void updateItem(Pokemon p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(p.getMote() + " (Nvl." + p.getNivel() + ")");
                }
            }
        });

        // Lógica automática para el icono de pantalla completa
        Platform.runLater(() -> {
            if (botonPantalla != null && botonPantalla.getScene() != null) {
                Stage stage = (Stage) botonPantalla.getScene().getWindow();
                botonPantalla.setImage(stage.isFullScreen() ? imgEncoger : imgAgrandar);
                stage.fullScreenProperty().addListener((obs, ov, nv) -> botonPantalla.setImage(nv ? imgEncoger : imgAgrandar));
            }
        });
    }

    private void cargarDatosBaseDeDatos() {
        if (jugadorActual == null) return;
        
        // 1. Cargar el EQUIPO (Máx 6)
        listEquipo.getItems().clear();
        listEquipo.getItems().addAll(dao.obtenerPokemonPorUbicacion(jugadorActual.getIdEntrenador(), "EQUIPO"));
        
        // 2. Cargar TODA LA CAJA y actualizar la cuadrícula
        todosLosPokemonCaja = dao.obtenerPokemonPorUbicacion(jugadorActual.getIdEntrenador(), "CAJA");
        actualizarVistaCaja();
    }

    // --- LÓGICA DE LA CUADRÍCULA Y PAGINACIÓN ---

    private void actualizarVistaCaja() {
        gridCaja.getChildren().clear(); 
        lblNombreCaja.setText("Caja " + cajaActual);

        int indiceInicio = (cajaActual - 1) * MAX_POR_CAJA;
        int indiceFin = Math.min(indiceInicio + MAX_POR_CAJA, todosLosPokemonCaja.size());

        int columna = 0;
        int fila = 0;

        for (int i = indiceInicio; i < indiceFin; i++) {
            Pokemon p = todosLosPokemonCaja.get(i);

            Button btnPokemon = new Button();
            btnPokemon.setPrefSize(70, 90); 
            
            try {
                String r = p.getNumPokedex().getRutaImgFront();
                String arch = r.substring(r.lastIndexOf("/") + 1);
                Image img = new Image(getClass().getResourceAsStream("/img/Pokemon/Front/" + arch));
                
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(50); 
                imgView.setFitHeight(50);
                imgView.setPreserveRatio(true);
                btnPokemon.setGraphic(imgView);
                btnPokemon.setText(p.getMote());
                btnPokemon.setContentDisplay(ContentDisplay.TOP);
            } catch (Exception e) {
                btnPokemon.setText(p.getMote()); 
            }

            btnPokemon.setOnAction(event -> {
                pokemonSeleccionado = p;
                listEquipo.getSelectionModel().clearSelection(); 
                actualizarPanelEstadisticas(p);
                btnMoverEquipo.setDisable(false); 
                btnMoverCaja.setDisable(true);
            });

            gridCaja.add(btnPokemon, columna, fila);

            columna++;
            if (columna >= 7) { 
                columna = 0;
                fila++;
            }
        }

        btnCajaAnterior.setDisable(cajaActual == 1); 
        boolean haySiguiente = todosLosPokemonCaja.size() > (cajaActual * MAX_POR_CAJA);
        btnCajaSiguiente.setDisable(!haySiguiente);
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

    @FXML
    public void seleccionarDeEquipo(MouseEvent event) {
        pokemonSeleccionado = listEquipo.getSelectionModel().getSelectedItem();
        if (pokemonSeleccionado != null) {
            actualizarPanelEstadisticas(pokemonSeleccionado);
            btnMoverCaja.setDisable(false); 
            btnMoverEquipo.setDisable(true);
        }
    }

    private void actualizarPanelEstadisticas(Pokemon p) {
        // Envolvemos todo en un Try-Catch para que no explote si falta el id en algún Label
        try {
            // Textos básicos
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
                        lblStatSexo.setText("");
                    }
                } else {
                    lblStatSexo.setText("");
                }
            }
        } catch (Exception e) {
            System.err.println("Fallo al poner los textos en estadísticas: " + e.getMessage());
        }

        // Carga de la foto independiente para que si falla, no afecte a los números
        try {
            if (imgStatsFoto != null) {
                String r = p.getNumPokedex().getRutaImgFront();
                String arch = r.substring(r.lastIndexOf("/") + 1);
                imgStatsFoto.setImage(new Image(getClass().getResourceAsStream("/img/Pokemon/Front/" + arch)));
            }
        } catch (Exception e) { 
            System.err.println("Fallo al cargar la foto: " + e.getMessage());
            if (imgStatsFoto != null) imgStatsFoto.setImage(null); 
        }
    }

    // --- MOVER POKÉMON ---

    @FXML
    public void accionMoverAEquipo(ActionEvent event) {
        if (listEquipo.getItems().size() >= 6) {
            mostrarAlerta("Equipo Lleno", "Ya tienes 6 Pokémon en el equipo.");
            return;
        }
        if (dao.moverPokemon(pokemonSeleccionado.getIdPokemon(), "EQUIPO")) {
            pokemonSeleccionado = null;
            cargarDatosBaseDeDatos(); 
        }
    }

    @FXML
    public void accionMoverACaja(ActionEvent event) {
        if (listEquipo.getItems().size() <= 1) {
            mostrarAlerta("Atención", "¡No puedes quedarte sin Pokémon en el equipo!");
            return;
        }
        if (dao.moverPokemon(pokemonSeleccionado.getIdPokemon(), "CAJA")) {
            pokemonSeleccionado = null;
            cargarDatosBaseDeDatos(); 
        }
    }

    // --- BOTONES DE LA VENTANA ---

    @FXML
    void gestionarPantalla(MouseEvent event) {
        Stage stage = (Stage) botonPantalla.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    public void accionVolverMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            
            // Le devolvemos el jugador al menú para que no se cierre la sesión
            Object controlador = loader.getController();
            if (controlador instanceof DatosEntrenador && this.jugadorActual != null) {
                ((DatosEntrenador) controlador).setEntrenador(this.jugadorActual);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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