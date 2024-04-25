package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Enntities.User;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.userservice;

import java.io.IOException;

public class AdminController {
    public TableColumn actionsColumn;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> rolesColumn;
    @FXML
    private TableColumn<User, Integer> verifiedColumn;

    private userservice userService;

    public AdminController() {
        userService = new userservice(); // Assume UserService handles all user-related operations
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        verifiedColumn.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        setupActionsColumn();
        loadUsers();
    }
    @FXML
    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
        usersTable.setItems(users);
    }
    @FXML
    public void deleteUser(int userId) {
        userService.deleteUser(userId);
        loadUsers(); // Refresh the list after deleting a user
    }
    @FXML
    public void updateUser(User user) {
        userService.updateUser(user);
        loadUsers(); // Refresh the list after updating a user
    }
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, updateBtn, deleteBtn);

            {
                updateBtn.setOnAction(event -> handleUpdateAction(getTableView().getItems().get(getIndex()), getTableRow()));
                deleteBtn.setOnAction(event -> handleDeleteAction(getTableView().getItems().get(getIndex()), getTableRow()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void handleDeleteAction(User user, TableRow<User> tableRow) {
        usersTable.getItems().remove(user);
        deleteUser(user.getId());
        loadUsers();

    }

    private void handleUpdateAction(User user, TableRow<User> tableRow) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/editBack.fxml"));
            Parent root = loader.load();
            editBack editController = loader.getController();
            editController.setCurrentEmail(user.getEmail());  // Pass the current email to the EditController

            Scene scene = new Scene(root);
            Stage stage = (Stage) tableRow.getScene().getWindow(); // Use the existing window
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception, maybe show an error message
        }
    }
    @FXML
    private void logout(ActionEvent event) {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from any node, here we use the event source
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene to stage and show it
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions properly in real applications
        }
    }

}
