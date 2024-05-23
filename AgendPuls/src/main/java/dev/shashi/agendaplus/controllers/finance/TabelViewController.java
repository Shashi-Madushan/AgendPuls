package dev.shashi.agendaplus.controllers.finance;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TabelViewController {

    @FXML
    private TableColumn<?, ?> amountColumn;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableView<?> transactionTable;

    @FXML
    private TableColumn<?, ?> typeColumn;

}
