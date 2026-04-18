package controller;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import dao.CombateDao;

public class combateController implements DatosEntrenador {

    // enlazamos todas las cosas de la interfaz visual
    @FXML 
    private ImageView fondoBatalla, imgPokemonJugador, imgPokemonRival, botonPantalla;
    
    @FXML
    private Label lblNombreJugador, lblNivelJugador, lblNombreRival, lblNivelRival;
    
    @FXML 
    private Label lblKOJugador, lblKORival;
    
    @FXML 
    private ProgressBar barraVidaJugador, barraVidaRival, barraEstaminaJugador, barraEstaminaRival;
    
    @FXML 
    private TextArea txtLog;
    
    @FXML 
    private Button btnAtaque1, btnAtaque2, btnAtaque3, btnAtaque4, btnDescansar, btnEquipo, btnHuir;
    
    @FXML 
    private AnchorPane panelPrincipal;

    private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
    private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));

    private Entrenador jugadorActual;
    private Entrenador rivalActual;
    private Combate combate;
    private CombateDao dao = new CombateDao();
    
    private Pokemon pokeActivoJugador;
    private Pokemon pokeActivoRival;
    
    private int maxHpJugador = 1;
    private int maxHpRival = 1;

    // pillamos los datos del jugador desde la otra pantalla y cargamos su equipo
    @Override
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
        dao.cargarEquipoJugador(this.jugadorActual);
        iniciarCombate();
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (fondoBatalla.getScene() != null) {
                Scene scene = fondoBatalla.getScene();
                Stage stage = (Stage) scene.getWindow();

                // hacemos que el fondo y los pokemon se adapten si cambiamos el tamaño de la ventana
                fondoBatalla.fitWidthProperty().bind(scene.widthProperty());
                fondoBatalla.fitHeightProperty().bind(scene.heightProperty());

                scene.widthProperty().addListener((obs, oldVal, newVal) -> {
                    double ancho = newVal.doubleValue();
                    imgPokemonJugador.setLayoutX((ancho * 0.28) - (imgPokemonJugador.getFitWidth() / 2));
                    imgPokemonRival.setLayoutX((ancho * 0.60) - (imgPokemonRival.getFitWidth() / 2));
                });

                scene.heightProperty().addListener((obs, oldVal, newVal) -> {
                    double alto = newVal.doubleValue();
                    imgPokemonJugador.setLayoutY((alto * 0.70) - imgPokemonJugador.getFitHeight());
                    imgPokemonRival.setLayoutY((alto * 0.50) - imgPokemonRival.getFitHeight());
                });
                
                double wInicial = scene.getWidth();
                double hInicial = scene.getHeight();
                imgPokemonJugador.setLayoutX((wInicial * 0.28) - (imgPokemonJugador.getFitWidth() / 2));
                imgPokemonRival.setLayoutX((wInicial * 0.60) - (imgPokemonRival.getFitWidth() / 2));
                imgPokemonJugador.setLayoutY((hInicial * 0.70) - imgPokemonJugador.getFitHeight());
                imgPokemonRival.setLayoutY((hInicial * 0.50) - imgPokemonRival.getFitHeight());

                if (botonPantalla != null) {
                    // cambiamos el icono dependiendo de si estamos en pantalla completa o no
                    botonPantalla.setImage(stage.isFullScreen() ? imgEncoger : imgAgrandar);
                    stage.fullScreenProperty().addListener((obs, ov, nv) -> botonPantalla.setImage(nv ? imgEncoger : imgAgrandar));
                }
            }
        });
    }

    // preparamos todo para empezar la pelea
    private void iniciarCombate() {
        if(jugadorActual == null) return;
        
        int[] niveles = dao.obtenerRangoNivelesEquipo(jugadorActual.getIdEntrenador());
        
        // miramos cuantos pokemon llevamos para que el rival saque los mismos y sea justo
        int numPokemonsJugador = 0;
        for(Pokemon p : jugadorActual.getEquipoPrincipal()) {
            if(p != null && p.getVitalidad() > 0) numPokemonsJugador++;
        }
        if (numPokemonsJugador == 0) numPokemonsJugador = 1;

        rivalActual = dao.generarRivalAleatorio(niveles[0], niveles[1], numPokemonsJugador);
        combate = new Combate(jugadorActual, rivalActual);
        
        pokeActivoJugador = obtenerSiguientePokemonVivo(jugadorActual);
        pokeActivoRival = obtenerSiguientePokemonVivo(rivalActual);
        
        // guardamos la vida maxima para poder calcular los porcentajes de las barras
        if (pokeActivoJugador != null) maxHpJugador = Math.max(1, pokeActivoJugador.getVitalidad());
        if (pokeActivoRival != null) maxHpRival = Math.max(1, pokeActivoRival.getVitalidad());
        
        txtLog.setText("¡Comienza el combate contra " + rivalActual.getNombreEntrenador() + "!\n\n");

        if (pokeActivoJugador == null) {
            txtLog.appendText("¡No tienes Pokémon en condiciones para luchar!\n");
            bloquearBotones(true);
            btnHuir.setDisable(false);
            btnHuir.setText("VOLVER");
            return;
        }
        actualizarUI();
    }

    // buscamos el primer pokemon que no este debilitado
    private Pokemon obtenerSiguientePokemonVivo(Entrenador ent) {
        if (ent == null || ent.getEquipoPrincipal() == null) return null;
        for(Pokemon p : ent.getEquipoPrincipal()) {
            if(p != null && p.getVitalidad() > 0) return p;
        }
        return null; 
    }

    // refrescamos las barras de vida, estamina y los textos de la pantalla
    private void actualizarUI() {
        if(pokeActivoJugador != null) {
            lblNombreJugador.setText(pokeActivoJugador.getMote());
            lblNivelJugador.setText("Lv " + pokeActivoJugador.getNivel());
            
            double porcentajeJ = (double) pokeActivoJugador.getVitalidad() / maxHpJugador;
            barraVidaJugador.setProgress(Math.max(0.0, Math.min(1.0, porcentajeJ))); 
            
            double estaminaJ = (double) pokeActivoJugador.getEstamina() / 100.0;
            barraEstaminaJugador.setProgress(Math.max(0.0, Math.min(1.0, estaminaJ)));

            lblKOJugador.setText("Tus Bajas: " + combate.getKoRival() + "/6");
            cargarImagen(imgPokemonJugador, pokeActivoJugador.getNumPokedex().getRutaImgBack(), true);
            
            Movimiento[] movs = pokeActivoJugador.getMovimiento();
            configurarBotonAtaque(btnAtaque1, movs != null && movs.length > 0 ? movs[0] : null);
            configurarBotonAtaque(btnAtaque2, movs != null && movs.length > 1 ? movs[1] : null);
            configurarBotonAtaque(btnAtaque3, movs != null && movs.length > 2 ? movs[2] : null);
            configurarBotonAtaque(btnAtaque4, movs != null && movs.length > 3 ? movs[3] : null);
        } else {
            // si no nos quedan pokemon ocultamos la foto
            imgPokemonJugador.setVisible(false);
        }
        
        if(pokeActivoRival != null) {
            lblNombreRival.setText(pokeActivoRival.getMote());
            lblNivelRival.setText("Lv " + pokeActivoRival.getNivel());
            
            double porcentajeR = (double) pokeActivoRival.getVitalidad() / maxHpRival;
            barraVidaRival.setProgress(Math.max(0.0, Math.min(1.0, porcentajeR)));
            
            double estaminaR = (double) pokeActivoRival.getEstamina() / 100.0;
            barraEstaminaRival.setProgress(Math.max(0.0, Math.min(1.0, estaminaR)));

            lblKORival.setText("Bajas Enemigas: " + combate.getKoJugador() + "/6"); 
            cargarImagen(imgPokemonRival, pokeActivoRival.getNumPokedex().getRutaImgFront(), false);
            
            if(!pokeActivoRival.getTipoPokemon().isEmpty()) {
                ponerFondoSegunTipo(pokeActivoRival.getTipoPokemon().get(0).name());
            }
        } else {
            // si el rival se queda sin pokemon ocultamos la foto
            imgPokemonRival.setVisible(false);
        }
        
        revisarFinCombate();
    }

    // le ponemos el nombre y el coste a los botones de los ataques
    private void configurarBotonAtaque(Button btn, Movimiento mov) {
        if (mov != null && mov.getNomMovimiento() != null && !mov.getNomMovimiento().isEmpty()) {
            btn.setText(mov.getNomMovimiento() + "\n(-" + mov.getCosteEstamina() + " EST)");
            btn.setVisible(true);
            btn.setDisable(pokeActivoJugador.getEstamina() < mov.getCosteEstamina());
        } else {
            btn.setVisible(false);
        }
    }

    // cargamos la foto usando un try-catch por si acaso no encuentra la ruta y crashea
    private void cargarImagen(ImageView visor, String rutaBD, boolean esBack) {
        try {
        	// buscamos al final del rutaDB para obtener el nombre del archivo
        	String nombreArchivo = rutaBD.substring(rutaBD.lastIndexOf("/") + 1);
            // miramos en que carpeta buscar dependiendo de si es espalda o frente 
            String carpeta = esBack ? "Back" : "Front";
            
            // montamos la ruta usando la carpeta y el nombre que viene de la base de datos 
            String rutaFinal = "/img/Pokemon/" + carpeta + "/" + nombreArchivo;
            
            // intentamos cargar la imagen y mostrarla 
            visor.setImage(new Image(getClass().getResourceAsStream(rutaFinal)));
            visor.setVisible(true);

        } catch (Exception e) {
            // si algo falla (ruta mal puesta o archivo borrado) ocultamos el visor 
            visor.setVisible(false);
        }
    }

    // apagamos los botones un rato para que no se rompa nda si el usuario pulsa muy rapido
    private void bloquearBotones(boolean bloquear) {
        if (btnAtaque1 != null) btnAtaque1.setDisable(bloquear);
        if (btnAtaque2 != null) btnAtaque2.setDisable(bloquear);
        if (btnAtaque3 != null) btnAtaque3.setDisable(bloquear);
        if (btnAtaque4 != null) btnAtaque4.setDisable(bloquear);
        if (btnDescansar != null) btnDescansar.setDisable(bloquear);
        if (btnEquipo != null) btnEquipo.setDisable(bloquear);
        if (btnHuir != null) btnHuir.setDisable(bloquear);
    }

    // vemos quien es mas rapido para hacer la animacion en el orden correcto
    private void animarTurno(Turno turno, Runnable onComplete) {
        boolean jAtaca = (turno.getAccionEntrenador() == Turno.TipoAccion.ATACAR);
        boolean rAtaca = (turno.getAccionRival() == Turno.TipoAccion.ATACAR);

        boolean jPrimero = true;
        if (pokeActivoJugador != null && pokeActivoRival != null) {
            int velJ = pokeActivoJugador.getVelocidad(); 
            int velR = pokeActivoRival.getVelocidad();
            if (velR > velJ) jPrimero = false;
            else if (velR == velJ && Math.random() < 0.5) jPrimero = false; // si empatan tiramos una moneda al aire
        }

        if (jAtaca && rAtaca) {
            if (jPrimero) {
                // encadenamos las animaciones para que no pasen a la vez
                animarAtaque(imgPokemonJugador, imgPokemonRival, 1, () -> {
                    if (pokeActivoRival != null && pokeActivoRival.getVitalidad() > 0) {
                        animarAtaque(imgPokemonRival, imgPokemonJugador, -1, onComplete);
                    } else {
                        onComplete.run();
                    }
                });
            } else {
                animarAtaque(imgPokemonRival, imgPokemonJugador, -1, () -> {
                    if (pokeActivoJugador != null && pokeActivoJugador.getVitalidad() > 0) {
                        animarAtaque(imgPokemonJugador, imgPokemonRival, 1, onComplete);
                    } else {
                        onComplete.run();
                    }
                });
            }
        } else if (jAtaca) {
            animarAtaque(imgPokemonJugador, imgPokemonRival, 1, onComplete);
        } else if (rAtaca) {
            animarAtaque(imgPokemonRival, imgPokemonJugador, -1, onComplete);
        } else {
            onComplete.run();
        }
    }

    // hacemos el efecto visual de saltar hacia delante y que el otro parpadee
    private void animarAtaque(ImageView atacante, ImageView defensor, int direccion, Runnable onFinish) {
        if (atacante == null || defensor == null || !atacante.isVisible() || !defensor.isVisible()) {
            if (onFinish != null) onFinish.run();
            return;
        }

        TranslateTransition salto = new TranslateTransition(Duration.millis(150), atacante);
        salto.setByX(50 * direccion);
        salto.setAutoReverse(true);
        salto.setCycleCount(2);

        FadeTransition flashCuerpo = new FadeTransition(Duration.millis(100), defensor);
        flashCuerpo.setFromValue(1.0);
        flashCuerpo.setToValue(0.1);
        flashCuerpo.setCycleCount(4);
        flashCuerpo.setAutoReverse(true);

        salto.setOnFinished(e -> flashCuerpo.play());
        flashCuerpo.setOnFinished(e -> {
            if (onFinish != null) onFinish.run();
        });
        
        salto.play();
    }

    // ACCIONES DINAMICAS PARA LOS BOTONES, DEPENDIENDO DEL POKEMON CAMBIA
    @FXML 
    public void accionAtacar(ActionEvent event) 
    { ejecutarAtaque(0); }
    
    @FXML 
    void accionAtaque1(ActionEvent event) 
    { ejecutarAtaque(0); }
    
    @FXML 
    void accionAtaque2(ActionEvent event) 
    { ejecutarAtaque(1); }
    @FXML 
    void accionAtaque3(ActionEvent event) 
    { ejecutarAtaque(2); }
    
    @FXML void accionAtaque4(ActionEvent event) 
    { ejecutarAtaque(3); }

    
    // pillamos el ataque y pasamos turno
    private void ejecutarAtaque(int indiceAtaque) {
        if (combate.getGanador() != null || pokeActivoJugador == null) return;
        bloquearBotones(true);
        
        // sacamos cuanta vida tenian antes del golpe para apuntarlo en el log
        int hpRivalAntes = pokeActivoRival.getVitalidad();
        int hpJugadorAntes = pokeActivoJugador.getVitalidad();

        Movimiento movJugador = pokeActivoJugador.getMovimiento()[indiceAtaque];
        Turno turno = new Turno(combate.getContadorTurnos(), jugadorActual, rivalActual);
        turno.prepararAtaqueEntrenador(movJugador);
        
        gestionarTurnoRival(turno);
        
        combate.ejecutarTurno(turno);
        
        turno.setDanioEntrenador(hpRivalAntes - Math.max(0, pokeActivoRival.getVitalidad()));
        turno.setDanioRival(hpJugadorAntes - Math.max(0, pokeActivoJugador.getVitalidad()));

        txtLog.appendText(turno.toString() + "\n\n");
        
        animarTurno(turno, () -> {
            revisarEstadoPostTurno();
            if (combate.getGanador() == null) {
                // reactivamos los botones si el combate sigue
                bloquearBotones(false); 
            }
        });
    }

    // pasamos el turno para recuperar un poco de estamina
    @FXML
    public void accionDescansar(ActionEvent event) {
        if (combate.getGanador() != null || pokeActivoJugador == null) return;
        bloquearBotones(true);
        
        pokeActivoJugador.descansar();
        
        int hpJugadorAntes = pokeActivoJugador.getVitalidad();

        Turno turno = new Turno(combate.getContadorTurnos(), jugadorActual, rivalActual);
        turno.setAccionEntrenador(Turno.TipoAccion.NADA); 
        
        gestionarTurnoRival(turno);
        combate.ejecutarTurno(turno);
        
        turno.setDanioRival(hpJugadorAntes - Math.max(0, pokeActivoJugador.getVitalidad()));
        txtLog.appendText(turno.toString() + "\n\n");
        
        animarTurno(turno, () -> {
            revisarEstadoPostTurno();
            if (combate.getGanador() == null) {
                bloquearBotones(false);
            }
        });
    }

    // sacamos una ventanita para cambiar de pokemon si nos quedan mas vivos
    @FXML
    public void accionEquipo(ActionEvent event) {
        if (combate.getGanador() != null || pokeActivoJugador == null) return;

        List<Pokemon> vivos = new ArrayList<>();
        List<String> nombres = new ArrayList<>();
        
        // metemos en la lista solo a los que pueden pelear y no estan ya fuera
        for(Pokemon p : jugadorActual.getEquipoPrincipal()) {
            if(p != null && p.getVitalidad() > 0 && p != pokeActivoJugador) {
                vivos.add(p);
                nombres.add(p.getMote() + " (Lv " + p.getNivel() + " | HP: " + p.getVitalidad() + ")");
            }
        }
        
        if(vivos.isEmpty()) {
            txtLog.appendText("No te quedan otros Pokémon vivos en el equipo para cambiar.\n");
            return;
        }
        // DIALOGO DE ELECCION
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(nombres.get(0), nombres);
        dialog.setTitle("Cambio de Pokémon");
        dialog.setHeaderText("Elige un nuevo Pokémon para salir a luchar:");
        dialog.initOwner(fondoBatalla.getScene().getWindow());

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            bloquearBotones(true);
            int indiceSel = nombres.indexOf(result.get());
            Pokemon seleccionado = vivos.get(indiceSel);
            
            int hpJugadorAntes = pokeActivoJugador.getVitalidad();

            Turno turno = new Turno(combate.getContadorTurnos(), jugadorActual, rivalActual);
            turno.prepararCambioEntrenador(seleccionado);
            
            gestionarTurnoRival(turno);
            combate.ejecutarTurno(turno);
            
            turno.setDanioRival(hpJugadorAntes - Math.max(0, pokeActivoJugador.getVitalidad()));
            
            pokeActivoJugador = seleccionado;
            maxHpJugador = Math.max(1, pokeActivoJugador.getVitalidad());
            
            txtLog.appendText(turno.toString() + "\n\n");
            
            animarTurno(turno, () -> {
                revisarEstadoPostTurno();
                if (combate.getGanador() == null) {
                    bloquearBotones(false);
                }
            });
        }
    }

    @FXML
    public void accionSonidoJugador(ActionEvent event) {
        if(pokeActivoJugador != null && pokeActivoJugador.getNumPokedex() != null) {
            reproducirSonido(pokeActivoJugador.getNumPokedex().getSonido());
        }
    }

    @FXML
    public void accionSonidoRival(ActionEvent event) {
        if(pokeActivoRival != null && pokeActivoRival.getNumPokedex() != null) {
            reproducirSonido(pokeActivoRival.getNumPokedex().getSonido());
        }
    }

    // reproducimos el audio con el grito del pokemon
    private void reproducirSonido(String rutaBD) {
        try {
            if (rutaBD != null && !rutaBD.isEmpty()) {
                String rutaFinal = rutaBD.startsWith("/") ? rutaBD : "/" + rutaBD;
                URL url = getClass().getResource(rutaFinal);
                if (url != null) {
                    MediaPlayer player = new MediaPlayer(new Media(url.toExternalForm()));
                    player.play();
                }
            }
        } catch (Exception e) {}
    }

    // la ia del rival, pilla el primer ataque que pueda pagar con estamina o descansa
    private void gestionarTurnoRival(Turno turno) {
        Movimiento movRiv = null;
        for(Movimiento m : pokeActivoRival.getMovimiento()) {
            if(m != null && pokeActivoRival.getEstamina() >= m.getCosteEstamina()) {
                movRiv = m; 
                break;
            }
        }
        if (movRiv != null) {
            turno.prepararAtaqueRival(movRiv);
        } else {
            turno.setAccionRival(Turno.TipoAccion.NADA);
            pokeActivoRival.descansar();
        }
    }

    // miramos si alguno ha muerto para sacar al siguiente
    private void revisarEstadoPostTurno() {
        if(pokeActivoJugador != null && pokeActivoJugador.getVitalidad() <= 0) {
            txtLog.appendText("¡" + pokeActivoJugador.getMote() + " se ha debilitado!\n");
            pokeActivoJugador = obtenerSiguientePokemonVivo(jugadorActual);
            if (pokeActivoJugador != null) {
                maxHpJugador = Math.max(1, pokeActivoJugador.getVitalidad());
                txtLog.appendText("¡Adelante " + pokeActivoJugador.getMote() + "!\n");
            }
        }
        if(pokeActivoRival != null && pokeActivoRival.getVitalidad() <= 0) {
            txtLog.appendText("¡El " + pokeActivoRival.getMote() + " enemigo se ha debilitado!\n");
            pokeActivoRival = obtenerSiguientePokemonVivo(rivalActual);
            if (pokeActivoRival != null) {
                maxHpRival = Math.max(1, pokeActivoRival.getVitalidad());
                txtLog.appendText("El rival saca a " + pokeActivoRival.getMote() + ".\n");
            }
        }
        actualizarUI();
    }

    // comprobamos si el combate ya termino para dar la exp y guardar
    private void revisarFinCombate() {
        if (combate.getGanador() != null) {
            if (combate.getGanador() == jugadorActual) {
                txtLog.appendText("\n>>> ¡HAS GANADO EL COMBATE! <<<");
                txtLog.appendText("\nHas obtenido " + combate.getTotalDineroGanado() + " PokéDólares.");
                txtLog.appendText("\nTus Pokémon han ganado " + combate.getTotalExpGanada() + " puntos de EXP.\n");
            } else {
                txtLog.appendText("\n>>> TE HAS QUEDADO SIN POKÉMON. HAS PERDIDO... <<<\n");
            }
            bloquearBotones(true);
            btnHuir.setDisable(false);
            btnHuir.setText("VOLVER AL MENÚ");
            
            // guardamos el progreso de exp y dinero en la base de datos
            dao.actualizarRecompensasBD(jugadorActual);
        }
    }

    // nos rendimos y volvemos al menu
    @FXML
    public void accionHuir(ActionEvent event) {
        if(combate.getGanador() == null && pokeActivoJugador != null) {
            Turno tHuir = new Turno(combate.getContadorTurnos(), jugadorActual, rivalActual);
            tHuir.setAccionEntrenador(Turno.TipoAccion.HUIR);
            combate.ejecutarTurno(tHuir);
            dao.actualizarRecompensasBD(jugadorActual);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            Object controlador = loader.getController();
            
            // le pasamos de vuelta el jugador al menu para no perder la sesion
            if (controlador instanceof DatosEntrenador && this.jugadorActual != null) {
                ((DatosEntrenador) controlador).setEntrenador(this.jugadorActual);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {}
    }
    
    @FXML
    void gestionarPantalla(MouseEvent event) {
        Stage stage = (Stage) fondoBatalla.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    // cambiamos el fondo de la batalla dependiendo del tipo que tenga el rival
    private void ponerFondoSegunTipo(String tipo) {
        String rf = switch (tipo.toUpperCase()) {
            case "AGUA" -> "/img/Captura/Imagenes/VISTA_AGUA.jpg";
            case "BICHO", "VENENO" -> "/img/Captura/Imagenes/VISTA_BICHO-VENENO.jpg";
            case "FUEGO", "DRAGON" -> "/img/Captura/Imagenes/VISTA_FUEGO-DRAGON.jpg";
            case "HIELO" -> "/img/Captura/Imagenes/VISTA_HIELO.jpg";
            case "FANTASMA", "PSIQUICO" -> "/img/Captura/Imagenes/VISTA_FANTASMA-PSIQUICO.jpg";
            case "LUCHA" -> "/img/Captura/Imagenes/VISTA_LUCHA.jpg";
            case "VOLADOR", "ELECTRICO" -> "/img/Captura/Imagenes/VISTA_VOLADOR-ELECTRICO.jpg";
            default -> "/img/Captura/Imagenes/VISTA_PLANTA-ROCA-TIERRA-NORMAL.jpg";
        };
        try {
            InputStream is = getClass().getResourceAsStream(rf);
            if (is != null) { fondoBatalla.setImage(new Image(is)); } 
            else { panelPrincipal.setStyle("-fx-background-color: #2b2b2b;"); }
        } catch (Exception e) {}
    }
}