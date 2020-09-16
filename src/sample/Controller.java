package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Controller {
    @FXML
    private TextField txtFile;

    public void onClickFile(javafx.event.ActionEvent event) {
        FileChooser win = null;
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();
        File path = null;
        try {
            win = new FileChooser();
            win.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
            path = win.showOpenDialog(stage);
            if (path != null) {
                txtFile.setText(path.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.print("ProyectoController.actionSearchDestino. Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionConvertir(ActionEvent event) {
        try {
            StringBuilder stringCsv = new StringBuilder();
            stringCsv.append("Position;Id;Title;Type;Description;Points;Effort Estimated;Effort Remaining;Effort Spent;Status;Created by;Created on;Responsible;Sprint;Release;Component;Epic;Due Date;Priority;Severity;Issue Priority;Followers;Other Responsibles;Tags;Steps To Reproduce;User Story Id;User Story Title");
            List<Object> headerOfExcel = new ArrayList<>();
            FileInputStream file = new FileInputStream(txtFile.getText());
            XSSFWorkbook book = new XSSFWorkbook(file);
            //Tomamos la primera hoja
            XSSFSheet sheet = book.getSheetAt(0);
            //Recorremos una especie de lista
            Iterator<Row> rows = sheet.iterator();
            Iterator<Cell> celdas;
            Row fila;
            Cell celda;
            while (rows.hasNext()) {
                fila = rows.next();
                celdas = fila.cellIterator();
                while (celdas.hasNext()) {
                    celda = celdas.next();
                    switch (celda.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            if (fila.getRowNum()!=0) {
                                System.out.println("Numero de columna "+celda.getColumnIndex());
                                System.out.println(celda.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            if (fila.getRowNum()!=0) {
                                System.out.println("Numero de columna "+celda.getColumnIndex());
                                System.out.println(celda.getStringCellValue());
                            }
                            break;
                    }
                }
            }
            book.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
