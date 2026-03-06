package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private Button cancelarLogin;

    @FXML
    private Button confirmarLogin;

    @FXML
    private Label errorLogin;

    @FXML
    private Label labelUsuario;

    @FXML
    private ImageView logoKodexTexto;

    @FXML
    private Pane panelFondoIzq;

    @FXML
    private PasswordField passwd;

    @FXML
    private TextField usuario;

    @FXML
    void cerrarLogin(ActionEvent event) {
    	Stage st = (Stage) cancelarLogin.getScene().getWindow();
    	st.close();
    }
    
    @FXML
    void login(ActionEvent event) {
    	
    	if(usuario.getText().isEmpty()) {
    		errorLogin.setText("Error: Inserta un nombre de usuario.");
    		errorLogin.setVisible(true);
    	} else if(passwd.getText().isEmpty()) {
    		errorLogin.setText("Error: Inserta una contraseña.");
    		errorLogin.setVisible(true);
    	} else {
    		errorLogin.setVisible(false);
    		String loginUsuario = usuario.getText();
    		String loginContraseña = passwd.getText();
    	}
    } 
}
