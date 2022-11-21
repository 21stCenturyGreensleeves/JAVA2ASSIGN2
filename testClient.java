import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client client = new Client();
        Scene scene = new Scene(client.getChess().getRoot());
        stage.setScene(scene);
        stage.setTitle("TC1");
        stage.show();
    }
}
