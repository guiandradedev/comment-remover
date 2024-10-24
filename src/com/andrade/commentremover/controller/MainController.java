package com.andrade.commentremover.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonInsert;

    @FXML
    private Button buttonRemove;

    private File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setFocusTraversable(true);

        root.requestFocus();
    }

    @FXML
    public void insertFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um Arquivo");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Programas C", "*.c"),
            new FileChooser.ExtensionFilter("Arquivos de Cabecalho", "*.h")
            // new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("Arquivo selecionado: " + file.getAbsolutePath());
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }

    public void submitFile(ActionEvent event) {
        if(file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação Importante");
            alert.setHeaderText("Arquivo inválido");
            alert.showAndWait();
            alert.setContentText("Insira arquivos do tipo .c ou .h");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String line = br.readLine();
            List<String> data = new ArrayList<>();
			
			int index = 0;
            boolean bigComment = false;
			while(line != null) {
                if(!bigComment) {
                    index = line.indexOf("//");
                    if(index != -1) {
                        // existe comentario na linha
                        line = line.substring(0, index);
                    }
                    index = line.indexOf("/*");
                    if(index != -1) {
                        bigComment = true;
                        int index2 = line.indexOf("*/");
                        if(index2 != -1) {
                            line = line.substring(0, index) + line.substring(index2 + 2); // +2 para remover "*/"
                            bigComment = false;
                        } else {
                            line = line.substring(0, index);
                        }
                    }
                } else {
                    index = line.indexOf("*/");
                    if(index != -1) {
                        line = line.substring(index + 2);
                        bigComment = false;
                    } else {
                        line = line.substring(0,0);
                    }
                }
                data.add(line);
                line = br.readLine();
			}

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Arquivo");

            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Programas C", "*.c"),
                new FileChooser.ExtensionFilter("Arquivos de Cabecalho", "*.h")
            );
            
            fileChooser.setInitialFileName(file.getName());
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File newFile = fileChooser.showSaveDialog(stage);

            if(newFile != null) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
                    for (String registro : data) {
                        bw.write(String.join(" ", registro));
                        bw.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		} catch(IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
    }
}
