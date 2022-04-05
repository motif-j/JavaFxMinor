import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class PhoneController implements Initializable {

    @FXML
    public Label lblDuration;

    @FXML
    public Label lblNetworkName;

    @FXML
    public Label lblCallMode;

    @FXML
    public Label lblVat;

    @FXML
    public Label lblCallCost;

    @FXML
    public Label lblCallTimer;

    @FXML
    public Label lblUserNumber;

    @FXML
    public Label lblUserNetwork;

    @FXML
    public ProgressIndicator callProgress;

    @FXML
    public Button btnCall;

    @FXML
    public Button btnEndCall;

    public PhoneController() {


    }

    public void setCallDuration(int duration) {
        lblDuration.setText(String.valueOf(duration) + " minutes");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        callProgress.setVisible(false);
        System.out.println("Called");
    }
}
