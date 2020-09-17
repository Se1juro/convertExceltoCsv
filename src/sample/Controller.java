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

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Controller {
    @FXML
    private TextField txtFile;
    @FXML
    private TextField txtDestino;

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
            JOptionPane.showMessageDialog(null, "ProyectoController.actionOnClickFile. Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionConvertir(ActionEvent event) {
        try {
            StringBuilder stringCsv = new StringBuilder();
            stringCsv.append("Position,Id,Title,Type,Description,Points,Effort Estimated,Effort Remaining,Effort Spent,Status,Created by,Created on,Responsible,Sprint,Release," +
                    "Component,Epic,Due Date,Priority,Severity,Issue Priority,Followers,Other Responsibles,Tags,Steps To Reproduce,User Story Id,User Story Title\n");
            List<Object> headerOfExcel = new ArrayList<>();
            FileInputStream file = new FileInputStream(txtFile.getText());
            XSSFWorkbook book = new XSSFWorkbook(file);
            ;

            //Tomamos la primera hoja
            XSSFSheet sheet = book.getSheetAt(0);
            //Recorremos una especie de lista
            Iterator<Row> rows = sheet.iterator();
            Iterator<Cell> celdas;
            Row fila;
            Cell celda;
            List<String> dataToCsv = new ArrayList<String>();
            int contadorUS = 0;
            int contadorTK = 0;
            double horasTarea = 0;
            while (rows.hasNext()) {
                fila = rows.next();
                celdas = fila.cellIterator();
                if (fila.getRowNum() != 0) {
                    if (fila.getCell(2).getStringCellValue().equals("US")) {
                        contadorUS++;
                        stringCsv.append(contadorUS).append(",US-").append(contadorUS).append(",").append(fila.getCell(0).getStringCellValue())
                                .append(",userstory").append(",").append(fila.getCell(3).getStringCellValue()).append(",10").append(",0.0").append(",0.0")
                                .append(",0.0").append(",New").append(",").append(",").append(",").append(",Sprint 1").append(",").append(",").append(",")
                                .append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",")
                                .append(",").append("\n");
                    }
                }
                while (celdas.hasNext()) {
                    celda = celdas.next();
                    switch (celda.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            if (fila.getCell(2).getStringCellValue().equals("TK")) {
                                horasTarea = celda.getNumericCellValue();
                            }
                            break;
                        case Cell.CELL_TYPE_BLANK:
                        case Cell.CELL_TYPE_STRING:
                            if (fila.getRowNum() != 0) {
                                if (fila.getCell(2).getStringCellValue().equals("TK") && fila.getCell(3).getNumericCellValue()>0) {
                                    String data = celda.getStringCellValue().replace("\n", " ").replace(",", "-");
                                    dataToCsv.add(data);
                                }
                            }
                            break;
                    }
                }
                if (dataToCsv.size() != 0) {
                    contadorTK++;
                    System.out.println("Tamaño list " + dataToCsv.size());
                    System.out.println("Lista en la posicion 0 "+dataToCsv.get(0));
                    System.out.println(dataToCsv);
                    stringCsv.append("" + ",TK-").append(contadorTK).append(",").append(dataToCsv.get(1)).append(",task").append(",").append(dataToCsv.get(3))
                            .append(dataToCsv.get(4)).append(" ,").append(",").append(horasTarea).append(",0.0").append(",0.0").append(",New").append(",").append(",").append(",")
                            .append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",")
                            .append(",").append("US-").append(contadorUS).append(",").append(dataToCsv.get(0)).append("\n");
                    dataToCsv.clear();
                    horasTarea = 0;
                }
            }
            book.close();
            generateFileCsv(txtDestino.getText(), stringCsv);
            JOptionPane.showMessageDialog(null, "Se ha generado correctamente tu archivo CSV");
            cleanData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ProyectoController.actionConvertir. Causa: " + e.getMessage());
        }
    }

    public void onClickDestino(ActionEvent event) {
        DirectoryChooser win = null;
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();
        File path = null;

        try {

            win = new DirectoryChooser();
            path = win.showDialog(stage);

            if (path != null) {
                txtDestino.setText(path.getAbsolutePath() + "\\");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ProyectoController.actionOnClickDestino. Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void generateFileCsv(String destino, StringBuilder sb) throws IOException {
        File file = new File(destino + "/" + "file.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(String.valueOf(sb));
        bw.close();
    }

    public void cleanData() {
        txtFile.setText("");
        txtDestino.setText("");
    }
}


