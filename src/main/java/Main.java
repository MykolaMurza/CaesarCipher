import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOG = Logger.getGlobal();
    private boolean isDark = false;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextArea input;
    @FXML
    private TextArea output;
    @FXML
    private TextField offset;
    @FXML
    private TextField cr;
    @FXML
    private Text errorMessageOffset;
    @FXML
    private Text errorMessageCr;
    @FXML
    private Button attackBtn;
    @FXML
    private Button themeBtn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        try {
            LOG.info("Програмне забезпечення запущено!");
            super.stop();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Ceasar Cipher by Mykola Murza");
            stage.show();
        } catch (IOException e) {
            LOG.info("Додаток не знайшов файл конфігурації.");
        }
    }

    @Override
    public void stop() {
        try {
            LOG.info("Програмне забезпечення зупинено!");
            super.stop();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    @FXML
    private void cipher() {
        int offset = checkOffsetValue(this.offset);
        int cr = checkCrValue(this.cr);
        String inputText = input.getText();
        output.setText(CaesarCipher.cipher(inputText, offset, cr));
    }

    @FXML
    private void decipher() {
        int offset = checkOffsetValue(this.offset);
        int cr = checkCrValue(this.cr);
        String inputText = input.getText();
        output.setText(CaesarCipher.decipher(inputText, offset, cr));
    }

    @FXML
    private void readFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(Window.getWindows().get(0));

        BufferedReader br = new BufferedReader(new FileReader(selectedFile));
        StringBuilder sb = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null) sb.append(st);
        input.setText(sb.toString());
    }

    @FXML
    private void cipherAndLoadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(Window.getWindows().get(0));

        try {
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
            StringBuilder sb = new StringBuilder();
            String st;
            while ((st = br.readLine()) != null) sb.append(st);
            int offset = checkOffsetValue(this.offset);
            int cr = checkCrValue(this.cr);
            Files.writeString(Path.of(selectedFile.getPath()), CaesarCipher.cipher(sb.toString(), offset, cr),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    @FXML
    private void decipherAndLoadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(Window.getWindows().get(0));

        try {
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
            StringBuilder sb = new StringBuilder();
            String st;
            while ((st = br.readLine()) != null) sb.append(st);
            int offset = checkOffsetValue(this.offset);
            int cr = checkCrValue(this.cr);
            Files.writeString(Path.of(selectedFile.getPath()), CaesarCipher.decipher(sb.toString(), offset, cr),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    @FXML
    public void refreshTheme() {
        isDark = !isDark;
        LOG.info("Тема змінена на isDark = " + isDark);
        attackBtn.setVisible(isDark);

        if (isDark) {
            themeBtn.setText("Світла тема");
            pane.setStyle("-fx-background-color: #333131;");
        } else {
            themeBtn.setText("Нічна тема");
            pane.setStyle("");
        }
    }

    @FXML
    public void attack() {
        LOG.info("РОЗПОЧАТО АТАКУ НА ШИФР!");
        String inputText = input.getText();
        output.setText("");
        for (int i = 1; i < 26; i++) {
            String curr = CaesarCipher.decipher(inputText, i, 1);
            output.setText(output.getText() + "\n---\n" + curr);
        }
    }

    private int checkCrValue(TextField cr) {
        // 1 - 10, default: 1
        try {
            int val = Integer.parseInt(cr.getText());
            if (val < 1 || val > 10) throw new IllegalArgumentException();
            errorMessageCr.setText("");
            return val;
        } catch (Exception e) {
            errorMessageCr.setText("Невірне значення. Введіть від 1 до 10.");
            throw new IllegalArgumentException("Невірне значення CR. Введіть від 1 до 10.");
        }
    }

    private int checkOffsetValue(TextField offset) {
        // 1 - 26, default: 10
        try {
            int val = Integer.parseInt(offset.getText());
            if (val < 1 || val > 26) throw new IllegalArgumentException();
            errorMessageOffset.setText("");
            return val;
        } catch (Exception e) {
            errorMessageOffset.setText("Невірне значення. Введіть від 1 до 26.");
            throw new IllegalArgumentException("Невірне значення Offset. Введіть від 1 до 26.");
        }
    }
}