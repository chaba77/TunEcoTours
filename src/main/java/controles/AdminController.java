package controles;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Enntities.User;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.userservice;
import javafx.scene.image.Image;

import java.io.IOException;

public class AdminController {
    public TableColumn<User, Void> actionsColumn;
     // Assuming you have a VBox layout, add the TextField here
    @FXML
    private TextField search; // TextField for search input
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
    private FilteredList<User> filteredData;


    public AdminController() {
        userService = new userservice(); // Assume UserService handles all user-related operations
    }

    @FXML
    private void initialize() {
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        verifiedColumn.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        setupActionsColumn();

        loadUsers();
        setupSearchField();
    }


    private void setupSearchField() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                for (String role : user.getRoles()) { // Assuming getRoles returns List<String>
                    if (role.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }
                return false;
            });
        });
    }




    @FXML
    private void handleSortUsers(ActionEvent event) {
        if (!filteredData.isEmpty()) {
            SortedList<User> sortedUsers = new SortedList<>(filteredData);
            // Example sorting by email, switch criteria as needed
            sortedUsers.setComparator((user1, user2) -> user1.getEmail().compareToIgnoreCase(user2.getEmail()));
            usersTable.setItems(sortedUsers);
        }
    }


    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
        filteredData = new FilteredList<>(users, p -> true);

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedData);
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
        userService.deleteUser(user.getId()); // First, attempt to delete the user from the backend or service layer.
        loadUsers(); // Reload the users from the backend to the table, ensures that the ObservableList is in sync with backend state.
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
