package com.freelancer.ui;

import com.freelancer.Business;
import com.freelancer.BusinessService;
import com.freelancer.dao.BusinessRepository;
import com.freelancer.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BusinessViewController {

    @FXML
    private TableView<Business> businessTable;
    @FXML
    private TableColumn<Business, String> nameColumn;
    @FXML
    private TableColumn<Business, String> descriptionColumn;
    @FXML
    private TableColumn<Business, String> contactInfoColumn;
    @FXML
    private Button addBusinessButton;
    @FXML
    private Button editBusinessButton;
    @FXML
    private Button deleteBusinessButton;
    @FXML
    private Button backButton;

    private BusinessService businessService;
    private ObservableList<Business> businessList;
    private User currentUser;

    public BusinessViewController() {
        this.businessService = new BusinessService(new BusinessRepository("businesses.dat"));
        this.businessList = FXCollections.observableArrayList();
    }

    public void refreshBusinessList() {
        businessList.setAll(businessService.getAllBusinesses());

    }
    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        businessTable.setItems(businessList);
        businessList.setAll(businessService.getAllBusinesses());
    }

    public void setGuest(boolean isGuest) {
        addBusinessButton.setVisible(!isGuest);
        editBusinessButton.setVisible(!isGuest);
        deleteBusinessButton.setVisible(!isGuest);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    private void handleAddBusiness() {
        openAddEditBusiness(null, false);
    }

    @FXML
    private void handleEditBusiness() {
        Business selectedBusiness = businessTable.getSelectionModel().getSelectedItem();
        if (selectedBusiness != null) {
            openAddEditBusiness(selectedBusiness, true);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a business to edit.");
        }
    }

    @FXML
    private void handleDeleteBusiness() {
        Business selectedBusiness = businessTable.getSelectionModel().getSelectedItem();
        if (selectedBusiness != null) {
            businessService.deleteBusiness(selectedBusiness);
            businessList.setAll(businessService.getAllBusinesses());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a business to delete.");
        }
    }

    private void openAddEditBusiness(Business business, boolean isEdit) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddEditBusinessView.fxml"));
            Parent root = fxmlLoader.load();
            AddEditBusinessController controller = fxmlLoader.getController();
            controller.setBusiness(business, isEdit);

            controller.setCurrentUser(currentUser);
            controller.setBusinessViewController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root,900,600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    public void setStageTitle(Stage stage, String title) {
        stage.setTitle(title);
    }
}
