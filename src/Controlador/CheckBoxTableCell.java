/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 *
 * @author Geykel
 */
public class CheckBoxTableCell<S, T> extends TableCell<S, T>{
    private final CheckBox checkBox;
    private ObservableValue<T> ov;

    public CheckBoxTableCell() {
        this.checkBox = new CheckBox();
        this.checkBox.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        setGraphic(checkBox);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } 
        else {
            setGraphic(checkBox);
            if (ov instanceof BooleanProperty) {
                checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
            }
            ov = getTableColumn().getCellObservableValue(getIndex());
            if (ov instanceof BooleanProperty) {
                checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
            }
        }
    }
}
