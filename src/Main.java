import data.CallCalculator;
import data.NetworkManager;
import data.constants.CallMode;
import data.constants.Network;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    private int totalCallMinutes = 0;
    private int totalCallSeconds = 0;
    private int totalCallHour = 0;

    private int totalTime = 0;
    private Timer timer = new Timer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PhoneWindow.fxml"));
        Parent root = loader.load();
        stage.setTitle("Phone Controller");
        stage.setScene(new Scene(root, 400, 700));
        stage.setResizable(false);


        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        stage.show();
        PhoneController controller = loader.getController();

        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("User Phone Number  country code +254");
        inputDialog.setHeaderText("Enter Your Phone Number Starting with 7 or 1 \n Your country code is 254");

        Optional<String> userPhoneNumberOpt = inputDialog.showAndWait();
        if (userPhoneNumberOpt.isPresent()) {
            String userNumber = userPhoneNumberOpt.get();
            final NetworkManager[] networkManager = {new NetworkManager(userNumber)};
            if (networkManager[0].isNumberValid(userNumber)) {
                networkManager[0].setUserPhoneNumber(userNumber);
                if (!networkManager[0].getUserNetwork().equals(Network.UNKNOWN.name())) {

                    controller.lblUserNumber.setText("+254" + userNumber);
                    controller.lblUserNetwork.setText(networkManager[0].getUserNetwork());

                    controller.btnCall.setOnAction(e -> {
                        controller.lblCallCost.setText("00.00 Kshs");
                        controller.lblVat.setText("00.00 Kshs");
                        controller.lblCallMode.setText("nan");
                        controller.lblNetworkName.setText("nan");

                        controller.btnEndCall.setDisable(false);
                        //reset inputs
                        totalCallHour = 0;
                        totalCallMinutes = 0;
                        totalCallSeconds = 0;
                        totalTime = 0;

                        controller.btnCall.setDisable(true);
                        TextInputDialog callDialog = new TextInputDialog();
                        inputDialog.setTitle("User Phone Number country code +254");
                        inputDialog.setHeaderText("Enter Caller Phone Number Starting with 7 or 1\n Your country code is 254");

                        Optional<String> receiverPhoneNumberOpt = inputDialog.showAndWait();
                        if (receiverPhoneNumberOpt.isPresent()) {
                            String receiverNumber = receiverPhoneNumberOpt.get();
                            if (networkManager[0].isNumberValid(receiverNumber)) {
                                try {
                                    networkManager[0] = new NetworkManager(userNumber, receiverNumber);
                                    //the call can start
                                    controller.callProgress.setVisible(true);
                                    controller.lblNetworkName.setText(networkManager[0].getReceiverNetwork());
                                    controller.lblCallMode.setText(networkManager[0].getCallMode().name());

                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            totalTime = totalTime + 1;
                                            if (totalCallSeconds < 59) {
                                                totalCallSeconds += 1;
                                            } else {
                                                totalCallSeconds = 0;
                                                if (totalCallMinutes < 59) {
                                                    totalCallMinutes += 1;
                                                } else {
                                                    totalCallMinutes = 0;
                                                    totalCallHour = totalCallHour + 1;
                                                }
                                            }

                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    controller.lblCallTimer.setText(formatTime(totalCallHour, totalCallMinutes, totalCallSeconds));
                                                    controller.lblDuration.setText(formatSecondsToMinutes(totalTime));
                                                }
                                            });

                                        }
                                    }, 0, 1000);

                                } catch (IllegalStateException exception) {
                                    showError(exception.getMessage());
                                    controller.btnCall.setDisable(false);
                                }

                            } else {
                                showError("Invalid receiver number");
                                controller.btnCall.setDisable(false);
                            }
                        } else {
                            showError("Invalid receiver number");
                            controller.btnCall.setDisable(false);
                        }
                    });

                    controller.btnEndCall.setOnAction(e -> {
                        timer.cancel();
                        controller.btnCall.setDisable(false);
                        controller.btnEndCall.setDisable(true);
                        controller.callProgress.setVisible(false);

                        double totalTimeInMinutes = (double) totalTime / 60;

                        System.out.println(totalTimeInMinutes);
                        CallCalculator callCalculator = new CallCalculator.CallBuilder(totalTimeInMinutes, networkManager[0])
                                .calculateCallPerMinute()
                                .calculateTotalCharge();

                        controller.lblVat.setText(String.format("%.2f", callCalculator.getTaxation()) + " Kshs");
                        controller.lblCallCost.setText(String.format("%.2f", callCalculator.getCallCharges()) + " Kshs");

                    });

                } else {
                    shutDownApp(stage, "Unknown Network");
                }

            } else {
                shutDownApp(stage, "Invalid Data");
            }
        } else {
            shutDownApp(stage, "Invalid Data");
        }

    }

    private void shutDownApp(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.setTitle("Error");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent()) {
            Platform.exit();
            System.exit(0);
        } else {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);

        }


        stage.close();

    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.setTitle("Error");
        alert.show();
    }

    private String formatTime(int hour, int minute, int second) {

        return hour + " : " + minute + " : " + second;
    }

    private String formatSecondsToMinutes(int totalSeconds) {
        double totalTimeInMinutes = (double) totalSeconds / 60;

        return String.format("%.2f", totalTimeInMinutes);
    }
}
