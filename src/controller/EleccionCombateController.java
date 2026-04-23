package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Entrenador;

public class EleccionCombateController implements DatosEntrenador {

    private Entrenador jugadorActual;

    @Override
    public void setEntrenador(Entrenador entrenador) {
        this.jugadorActual = entrenador;
    }

    @FXML
    void accionCombateNormal(ActionEvent event) {
        cargarPantallaCombate(event, false);
    }

    @FXML
    void accionLigaPokemon(ActionEvent event) {
        cargarPantallaCombate(event, true);
    }

    @FXML
    void accionVolver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
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

    private void cargarPantallaCombate(ActionEvent event, boolean esLiga) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/combate.fxml"));
            Parent root = loader.load();
            combateController controlador = loader.getController();
            
            if (esLiga) {
                controlador.modoLigaActivo = true;
                controlador.rondaLigaActual = 1;
                controlador.descansoUsadoEstaRonda = false;
                
                // Generamos los rivales del Alto Mando y al Campeón
                dao.LigaDao lDao = new dao.LigaDao();
                controlador.rivalesLiga = lDao.generarAltoMando(jugadorActual.getNivelEntrenador());
                controlador.rivalesLiga.add(lDao.generarCampeon(jugadorActual));
            }
            
            // Le pasamos el entrenador al final, ya que esto dispara el iniciarCombate() automáticamente
            controlador.setEntrenador(this.jugadorActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}