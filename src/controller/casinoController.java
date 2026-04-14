package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CasinoDao;
import dao.ConexionDB;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Rotate;

import javafx.stage.Stage;
import javafx.util.Duration;
import model.Entrenador;

public class casinoController implements DatosEntrenador {
	
	private model.Casino casinoModel = new model.Casino();

    @FXML
    private ImageView botonPantalla;

    @FXML
    private Button btnAdivinar;

    @FXML
    private Button btnCaraCruz;

    @FXML
    private Button btnRuleta;

    @FXML
    private Button btnVolver;

    @FXML
    private Label labelSaldo;

    @FXML
    private Pane panelFondoCasino;

    @FXML
    private MediaView videoFondoCasino;
    
    @FXML
    private ImageView imgMoneda;

    @FXML
    private Label labelResultadoJuego;
    
    @FXML
    private Button btnElegirCara;

    @FXML
    private Button btnElegirCruz;

    @FXML
    private Button btnCancelar;
    
    @FXML 
    private TextField txtApuestaRuleta;
    
    @FXML 
    private TextField txtNumeroRuleta;
    
    @FXML 
    private Button btnElegirRojo;
    
    @FXML 
    private Button btnElegirNegro;
    
    @FXML 
    private Button btnGirarRuleta;
    
    @FXML 
    private ImageView imgRuleta;

    private String colorRuletaSeleccionado = ""; // COLORRULETA
    private Image imagenRuleta = new Image(getClass().getResourceAsStream("/img/Casino/ruleta.png")); // CAMBIAR IMAGEN RULETA
    
    @FXML 
    private TextField txtNumeroAdivinar;
    
    @FXML 
    private Label labelIntentos;
    
    @FXML 
    private Button btnProbarSuerte;
    

    private int numeroSecreto;
    private int intentosConsumidos;
    
	// Variable para los vídeos
	private javafx.scene.media.MediaPlayer mediaPlayer;
    
	private Image imgAgrandar = new Image(getClass().getResourceAsStream("/img/Menu/botonAgrandar.png"));
	private Image imgEncoger = new Image(getClass().getResourceAsStream("/img/Menu/botonMinimizar.png"));
	
	private Image imagenCara = new Image(getClass().getResourceAsStream("/img/Casino/cara.png"));
    private Image imagenCruz = new Image(getClass().getResourceAsStream("/img/Casino/cruz.png"));
	
	private javafx.scene.media.MediaPlayer musicaMenu;

	private Entrenador entrenadorLog;

	@FXML
    void animarEntradaBoton(javafx.scene.input.MouseEvent event) {
        // Capturamos el botón sobre el que está el ratón
        Button btn = (Button) event.getSource();
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), btn);
        tt.setToY(-5); 
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(1.05);
        st.setToY(1.05);
        st.play();
    }

    @FXML
    void animarSalidaBoton(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), btn);
        tt.setToY(0);
        tt.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
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
	
	//Método para recibir datos
	public void recibirDatos(Entrenador ent) {
		this.entrenadorLog = ent;
		//Mostramos y sus pokedólares
				labelSaldo.setText(Integer.toString(ent.getPokedollar()));
				
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
	
	// Metodo para cambiar entre el menú principal y el menu de cara cruz con un boolean de parametro
	private void alternarMenuCaraCruz(boolean mostrarSubmenu) {
	    btnCaraCruz.setVisible(!mostrarSubmenu);
	    btnRuleta.setVisible(!mostrarSubmenu);
	    btnAdivinar.setVisible(!mostrarSubmenu);
	    btnVolver.setVisible(!mostrarSubmenu);


	    btnElegirCara.setVisible(mostrarSubmenu);
	    btnElegirCruz.setVisible(mostrarSubmenu);
	    btnCancelar.setVisible(mostrarSubmenu);
	}

	
	@FXML
	void clickCaraCruz(ActionEvent event) {
	    if (entrenadorLog == null) return;
	    
	    labelResultadoJuego.setText("¿A qué apostamos?");
	    alternarMenuCaraCruz(true); // cambiamos botones
	}

	@FXML
	void clickCancelar(ActionEvent event) {
	    labelResultadoJuego.setText(""); // limpiamos textos
	    imgMoneda.setVisible(false);
	    alternarMenuCaraCruz(false); // Volvemos al menú original
	    alternarMenuRuleta(false);
	    alternarMenuAdivinar(false);
	}
	
	// Metodo para cambiar entre el menú principal y el menu de ruleta con un boolean de parametro
	private void alternarMenuRuleta(boolean mostrar) {
	    btnCaraCruz.setVisible(!mostrar);
	    btnRuleta.setVisible(!mostrar);
	    btnAdivinar.setVisible(!mostrar);
	    btnVolver.setVisible(!mostrar);

	    txtApuestaRuleta.setVisible(mostrar);
	    txtNumeroRuleta.setVisible(mostrar);
	    btnElegirRojo.setVisible(mostrar);
	    btnElegirNegro.setVisible(mostrar);
	    btnGirarRuleta.setVisible(mostrar);
	    btnCancelar.setVisible(mostrar); // Reutilizamos el botón de cancelar
	}
	@FXML
	void clickCara(ActionEvent event) {
	    ejecutarJuegoCaraCruz("cara");
	}

	@FXML
	void clickCruz(ActionEvent event) {
	    ejecutarJuegoCaraCruz("cruz");
	}
	
	private void ejecutarJuegoCaraCruz(String eleccionUsuario) {
    int apuesta = 50; 

    // OCULTAR BOTONES
    btnElegirCara.setVisible(false);
    btnElegirCruz.setVisible(false);
    btnCancelar.setVisible(false);
    btnCaraCruz.setVisible(false);
    btnRuleta.setVisible(false);
    btnAdivinar.setVisible(false);
    btnVolver.setVisible(false);

    // centramos moneda
    imgMoneda.setLayoutY(250); 
    // texto debajo de la moneda
    labelResultadoJuego.setLayoutY(420); 
    
    labelResultadoJuego.setText("Lanzando moneda...");
    
    imgMoneda.setVisible(true);
    imgMoneda.setImage(imagenCara); 

    // ANIMACION
    RotateTransition rotacion = new RotateTransition(Duration.seconds(1.5), imgMoneda);
    rotacion.setAxis(Rotate.Y_AXIS);
    rotacion.setByAngle(1080); // 3 vueltas completas
    rotacion.setCycleCount(1);

    // LÓGICA AL TERMINAR DE GIRAR
    rotacion.setOnFinished(e -> {
        
        // resultado
        String resultadoMoneda = casinoModel.jugarCaraCruz(entrenadorLog, eleccionUsuario, apuesta);

        if (resultadoMoneda.equals("sin_dinero")) {
            labelResultadoJuego.setText("¡No tienes Pokedólares suficientes!");
        } else {
            if (resultadoMoneda.equals("cara")) {
                imgMoneda.setImage(imagenCara);
            } else {
                imgMoneda.setImage(imagenCruz);
            }

            if (eleccionUsuario.equalsIgnoreCase(resultadoMoneda)) {
                labelResultadoJuego.setText("¡Salió " + resultadoMoneda.toUpperCase() + "! ¡Has ganado!");
            } else {
                labelResultadoJuego.setText("Salió " + resultadoMoneda.toUpperCase() + ". Has perdido...");
            }

            labelSaldo.setText(Integer.toString(entrenadorLog.getPokedollar()));
        }
        
        ConexionDB connection = new ConexionDB();
        Connection con = null;
        try {
            con = connection.getConnection();
            if (con != null) {
                dao.CasinoDao.actualizarSaldo(con, entrenadorLog);
            }
        } catch (SQLException ex) {
            System.err.println("Error de base de datos al guardar: " + ex.getMessage());
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e1) { e1.printStackTrace(); }
            }
        }

        // --- RETRASO Y RESETEO DE LA PANTALLA ---
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
        pausa.setOnFinished(eventoPausa -> {
            
            imgMoneda.setVisible(false);      
            labelResultadoJuego.setText("");  
            
            // volvemos al sitio
            imgMoneda.setLayoutY(100);
            // Volvemos a subir el texto a su sitio original sobre los botones
            labelResultadoJuego.setLayoutY(220); 
            
            // Volvemos a mostrar los botones del menú principal
            alternarMenuCaraCruz(false); 
        });
        
        pausa.play(); 
    });

    rotacion.play(); 
}
	
	@FXML
	void clickRuleta(ActionEvent event) {
	    if (entrenadorLog == null) return;
	    
	    labelResultadoJuego.setText("Elige número, color y apuesta.");
	    alternarMenuRuleta(true);
	}
	
	@FXML
	void clickRojo(ActionEvent event) {
	    colorRuletaSeleccionado = "rojo";
	    // PONEMOS ESTILOS PARA CUANDO ESTA SELECCIONADO EL BOTON
	    btnElegirRojo.setStyle("-fx-background-color: #EF4444; -fx-border-color: #FFD700; -fx-border-width: 4px;"); // Resalta en amarillo
	    btnElegirNegro.setStyle("-fx-background-color: #374151; -fx-border-color: #ffffff; -fx-border-width: 2px;");
	}

	@FXML
	void clickNegro(ActionEvent event) {
	    colorRuletaSeleccionado = "negro";
	    // PONEMOS ESTILOS PARA CUANDO ESTA SELECCIONADO EL BOTON
	    btnElegirNegro.setStyle("-fx-background-color: #374151; -fx-border-color: #FFD700; -fx-border-width: 4px;");
	    btnElegirRojo.setStyle("-fx-background-color: #EF4444; -fx-border-color: #ffffff; -fx-border-width: 2px;");
	}

	@FXML
	void clickGirarRuleta(ActionEvent event) {
	    // COMPROBAMOS QUE TODOS LOS DATOS ESTEN CORRECTOS
	    if (txtApuestaRuleta.getText().isEmpty() || txtNumeroRuleta.getText().isEmpty() || colorRuletaSeleccionado.isEmpty()) {
	        labelResultadoJuego.setText("¡Elige número, color y apuesta!");
	        return;
	    }

	    try {
	        int apuesta = Integer.parseInt(txtApuestaRuleta.getText());
	        int numero = Integer.parseInt(txtNumeroRuleta.getText());

	        if (numero < 0 || numero > 36) {
	            labelResultadoJuego.setText("El número debe ser del 0 al 36.");
	            return;
	        }

	        // OCULTAMOS 
	        btnElegirCara.setVisible(false);
	        btnElegirCruz.setVisible(false);
	        btnCancelar.setVisible(false);
	        btnCaraCruz.setVisible(false);
	        btnRuleta.setVisible(false);
	        btnAdivinar.setVisible(false);
	        btnVolver.setVisible(false);
	        txtApuestaRuleta.setVisible(false);
	        txtNumeroRuleta.setVisible(false);
	        btnElegirRojo.setVisible(false);
	        btnElegirNegro.setVisible(false);
	        btnGirarRuleta.setVisible(false);
	        

	        imgRuleta.setLayoutY(150);
	        labelResultadoJuego.setLayoutY(400); 
	        labelResultadoJuego.setText("Girando Ruleta...");
	        
	        imgRuleta.setVisible(true);
	        imgRuleta.setImage(imagenRuleta); 

	        // ANIMACIÓN DE LA RULETA
	        RotateTransition rotacion = new RotateTransition(Duration.seconds(4.0), imgRuleta);
	        double gradosAleatorios = Math.random() * 360;
	        rotacion.setByAngle((360 * 5) + gradosAleatorios); 
	        rotacion.setCycleCount(1);
	        rotacion.setInterpolator(Interpolator.EASE_OUT); // Efecto de frenado

	        // LÓGICA AL TERMINAR DE GIRAR
	        rotacion.setOnFinished(e -> {
	            
	            String resultado = casinoModel.jugarRuleta(entrenadorLog, numero, colorRuletaSeleccionado, apuesta);

	            if (resultado.equals("sin_dinero")) {
	                labelResultadoJuego.setText("¡No tienes PokéDólares suficientes!");
	            } else {
	                String[] partes = resultado.split("_");
	                String estado = partes[0];      
	                String numSalido = partes[1];   
	                String colorSalido = partes[2]; 

	                String mensajeBase = "Salió el " + numSalido + " " + colorSalido.toUpperCase() + ".\n";

	                if (estado.equals("numero")) {
	                    labelResultadoJuego.setText(mensajeBase + "¡PLENO! ¡Ganas x10!");
	                } else if (estado.equals("color")) {
	                    labelResultadoJuego.setText(mensajeBase + "¡Aciertas el color! Ganas x2");
	                } else {
	                    labelResultadoJuego.setText(mensajeBase + "Oh... Has perdido.");
	                }

	                labelSaldo.setText(Integer.toString(entrenadorLog.getPokedollar()));
	                
	                // GUARDAMOS
	                ConexionDB connection = new ConexionDB();
	                Connection con = null;
	                try {
	                    con = connection.getConnection();
	                    if (con != null) dao.CasinoDao.actualizarSaldo(con, entrenadorLog);
	                } catch (SQLException ex) {
	                    System.err.println("Error BD: " + ex.getMessage());
	                } finally {
	                    try { if(con!=null) con.close(); } catch(SQLException ex){}
	                }
	            }

	            // 5. RETRASO Y VUELTA A LA NORMALIDAD
	            PauseTransition pausa = new PauseTransition(Duration.seconds(3.5)); 
	            pausa.setOnFinished(eventoPausa -> {
	                imgRuleta.setVisible(false);
	                labelResultadoJuego.setText("");
	                labelResultadoJuego.setLayoutY(220); 
	                
	                // Reseteamos colores de los botones
	                colorRuletaSeleccionado = "";
	                btnElegirRojo.setStyle("-fx-background-color: #EF4444; -fx-border-color: #ffffff; -fx-border-width: 2px;");
	                btnElegirNegro.setStyle("-fx-background-color: #374151; -fx-border-color: #ffffff; -fx-border-width: 2px;");
	                
	                // Volvemos al menu de la ruleta
	                alternarMenuRuleta(false); 
	            });
	            pausa.play();
	        });

	        // giramos
	        rotacion.play();

	    } catch (NumberFormatException e) { // excepcion de numeros
	        labelResultadoJuego.setText("Por favor, introduce solo números.");
	    }
	}
	
	// Metodo para cambiar entre el menú principal y el menu de cara cruz con un boolean de parametro
    private void alternarMenuAdivinar(boolean mostrar) {
        btnCaraCruz.setVisible(!mostrar);
        btnRuleta.setVisible(!mostrar);
        btnAdivinar.setVisible(!mostrar);
        btnVolver.setVisible(!mostrar);

        txtNumeroAdivinar.setVisible(mostrar);
        labelIntentos.setVisible(mostrar);
        btnProbarSuerte.setVisible(mostrar);
        btnCancelar.setVisible(mostrar);
    }

    @FXML
    void clickAdivinar(ActionEvent event) {
        if (entrenadorLog == null) return;
        
        // valores iniciales
        numeroSecreto = (int) (Math.random() * 20) + 1; // numero entre 1 y 20
        intentosConsumidos = 0;
        
        labelResultadoJuego.setText("He pensado un número del 1 al 20...");
        labelIntentos.setText("Intentos: 5");
        txtNumeroAdivinar.clear();
        
        alternarMenuAdivinar(true);
    }

 @FXML
    void clickProbarSuerte(ActionEvent event) {
        if (txtNumeroAdivinar.getText().isEmpty()) return;

        try {
            int intentoUsuario = Integer.parseInt(txtNumeroAdivinar.getText());
            intentosConsumidos++;
            int restantes = 5 - intentosConsumidos;
            labelIntentos.setText("Intentos: " + restantes);

            if (intentoUsuario == numeroSecreto) {
                // PREMIOS
                int premio = 0;
                switch (intentosConsumidos) {
                    case 1: premio = 1000; break;
                    case 2: premio = 750;  break;
                    case 3: premio = 500;  break;
                    case 4: premio = 250;  break;
                    default: premio = 0;   break; // 5º intento o más
                }

                entrenadorLog.setPokedollar(entrenadorLog.getPokedollar() + premio);
                
                // fin interfaz
                txtNumeroAdivinar.setVisible(false);
                btnProbarSuerte.setVisible(false);
                labelIntentos.setVisible(false);
                btnCancelar.setVisible(false);
                
                labelResultadoJuego.setLayoutY(300);
                if (premio > 0) {
                    labelResultadoJuego.setText("¡ACERTASTE! Intento: " + intentosConsumidos + "\n¡Has ganado " + premio + " PokéDollars!");
                } else {
                    labelResultadoJuego.setText("¡Lo lograste en el último suspiro!\nPero no quedan premios.");
                }
                
                labelSaldo.setText(Integer.toString(entrenadorLog.getPokedollar()));

                // GUARDAR 
                ConexionDB connection = new ConexionDB();
                Connection con = null;
                try {
                    con = connection.getConnection();
                    if (con != null) dao.CasinoDao.actualizarSaldo(con, entrenadorLog);
                } catch (SQLException ex) {
                    System.err.println("Error BBDD: " + ex.getMessage());
                } finally {
                    try { if(con != null) con.close(); } catch(SQLException ex){}
                }

                finalizarJuegoAdivinar();

            } else if (intentosConsumidos >= 5) {
                // no quedan intentos
                txtNumeroAdivinar.setVisible(false);
                btnProbarSuerte.setVisible(false);
                labelIntentos.setVisible(false);
                btnCancelar.setVisible(false);

                labelResultadoJuego.setLayoutY(300);
                labelResultadoJuego.setText("¡Se agotaron las Pokéballs!\nEl número era: " + numeroSecreto);
                
                finalizarJuegoAdivinar();

            } else {
                // pistas
                if (intentoUsuario < numeroSecreto) {
                    labelResultadoJuego.setText("¡Más ALTO!");
                } else {
                    labelResultadoJuego.setText("¡Más BAJO!");
                }
                
                // vibracion al fallar
                TranslateTransition tt = new TranslateTransition(Duration.millis(50), txtNumeroAdivinar);
                tt.setByX(10);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }

        } catch (NumberFormatException e) {
            labelResultadoJuego.setText("Introduce un número.");
        }
    }

    private void finalizarJuegoAdivinar() {
        PauseTransition pausa = new PauseTransition(Duration.seconds(3.5));
        pausa.setOnFinished(e -> {
            labelResultadoJuego.setText("");
            labelResultadoJuego.setLayoutY(220); // posición original
            alternarMenuAdivinar(false);
        });
        pausa.play();
    }
    

	@FXML
	public void initialize() {
    	// VIDEO FONDO//
    	MediaPlayer mediaPlayer;
    	try {
            URL mediaUrl = getClass().getResource("/img/Casino/fondo_casino.mp4");
            
            if (mediaUrl == null) {
                throw new Exception("No se encontró el archivo fondo_casino.mp4 en /img/Casino/");
            }
            
    		// Esto obliga al vídeo a estirarse al tamaño de la ventana
    		Platform.runLater(() -> {
    			// CAMBIA "idDelVideo" POR EL NOMBRE QUE TENGA TU MEDIAVIEW EN EL CÓDIGO
    			videoFondoCasino.fitWidthProperty().bind(videoFondoCasino.getScene().widthProperty());
    			videoFondoCasino.fitHeightProperty().bind(videoFondoCasino.getScene().heightProperty());
    		});

            // creamos media y player
            Media media = new Media(mediaUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            videoFondoCasino.setMediaPlayer(mediaPlayer);
            // lo mandamos al fondo
            videoFondoCasino.toBack();
            
            // arreglamos resolucion
            videoFondoCasino.setPreserveRatio(false);
            videoFondoCasino.fitWidthProperty().bind(panelFondoCasino.widthProperty());
            videoFondoCasino.fitHeightProperty().bind(panelFondoCasino.heightProperty());
            videoFondoCasino.setSmooth(true);

            // reproducimos en mute y infinito
            mediaPlayer.setMute(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

        //    System.out.println("Video funciona.");

            // --- ANIMACIÓN DE ENTRADA VIDEO ---
            videoFondoCasino.setOpacity(0);
            FadeTransition ftVideo = new FadeTransition(Duration.seconds(2), videoFondoCasino); // fadeTransition
            ftVideo.setToValue(1.0);
            ftVideo.play();
            

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            // Si el video falla, ponemos un fondo para que no se vea vacio
            panelFondoCasino.setStyle("-fx-background-color: #bc2f2f;");
        }
	}
	
	@FXML
	void volverMenu(ActionEvent event) {
		// Usamos el método genérico
		cambiarEscena("menu.fxml", btnVolver);
	}
	//Método implementado de la interfaz Datos Entrenador
	@Override
	public void setEntrenador(Entrenador e) {
		recibirDatos(e);
		
	}
}
