package com.example.edytor_tekstu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        Label labelTekst = new Label("Tekst:");
        TextArea tekstArea = new TextArea();
        tekstArea.setWrapText(true);

        Label labelKlucz = new Label("Klucz:");
        TextField kluczField = new TextField();
        kluczField.setPromptText("Wpisz liczbę całkowitą");

        Button szyfrujButton = new Button("Zaszyfruj");
        Button deszyfrujButton = new Button("Deszyfruj");
        Button zapiszButton = new Button("Zapisz");
        Button otworzButton = new Button("Otwórz");
        Button wyjdzButton = new Button("Wyjdź");

        TextArea wynikArea = new TextArea();
        wynikArea.setWrapText(true);
        wynikArea.setEditable(false);

        szyfrujButton.setOnAction(e -> {
            try {
                int klucz = Integer.parseInt(kluczField.getText());
                String tekst = tekstArea.getText();
                String zaszyfrowany = szyfrujTekst(tekst, klucz);
                wynikArea.setText(zaszyfrowany);
            } catch (NumberFormatException ex) {
                wynikArea.setText("Klucz musi być liczbą całkowitą!");
            }
        });

        deszyfrujButton.setOnAction(e -> {
            try {
                int klucz = Integer.parseInt(kluczField.getText());
                String tekst = tekstArea.getText();
                String odszyfrowany = deszyfrujTekst(tekst, klucz);
                wynikArea.setText(odszyfrowany);
            } catch (NumberFormatException ex) {
                wynikArea.setText("Klucz musi być liczbą całkowitą!");
            }
        });

        zapiszButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz plik");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
            File fileToSave = fileChooser.showSaveDialog(stage);

            if (fileToSave != null) {
                try {
                    String tekstDoZapisu = wynikArea.getText();
                    Files.writeString(fileToSave.toPath(), tekstDoZapisu, StandardOpenOption.CREATE);
                } catch (IOException ex) {
                    wynikArea.setText("Błąd podczas zapisywania pliku: " + ex.getMessage());
                }
            }
        });

        otworzButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Otwórz plik");
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    String zawartosc = Files.readString(selectedFile.toPath());
                    tekstArea.setText(zawartosc);
                } catch (IOException ex) {
                    wynikArea.setText("Błąd podczas odczytu pliku: " + ex.getMessage());
                }
            }
        });

        wyjdzButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(labelTekst, 0, 0);
        gridPane.add(tekstArea, 1, 0, 2, 1);
        gridPane.add(labelKlucz, 0, 1);
        gridPane.add(kluczField, 1, 1);
        gridPane.add(szyfrujButton, 0, 2);
        gridPane.add(deszyfrujButton, 1, 2);
        gridPane.add(zapiszButton, 0, 3);
        gridPane.add(otworzButton, 1, 3);
        gridPane.add(wyjdzButton, 2, 3);
        gridPane.add(wynikArea, 1, 4, 2, 1);

        Scene scene = new Scene(gridPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Edytor tekstu - Szyfr Cezara");
        stage.show();
    }

    private String szyfrujTekst(String tekst, int klucz) {
        StringBuilder wynik = new StringBuilder();
        for (char c : tekst.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                char baza = Character.isUpperCase(c) ? 'A' : (Character.isLowerCase(c) ? 'a' : '0');
                int przesuniecie = Character.isDigit(c) ? 10 : 26;
                wynik.append((char) ((c - baza + klucz) % przesuniecie + baza));
            } else {
                wynik.append(c);
            }
        }
        return wynik.toString();
    }

    private String deszyfrujTekst(String tekst, int klucz) {
        return szyfrujTekst(tekst, -klucz);
    }

    public static void main(String[] args) {
        launch();
    }
}
