package com.freelancer.ui;

import com.freelancer.Business;
import com.freelancer.BusinessService;
import com.freelancer.dao.BusinessRepository;
import com.freelancer.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.UUID;

public class AddEditBusinessController {

    private BusinessViewController businessViewController;

    public void setBusinessViewController(BusinessViewController businessViewController) {
        this.businessViewController = businessViewController;
    }

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField contactInfoField;

    private BusinessService businessService;
    private Business business;
    private boolean isEdit;
    private User currentUser;

    public AddEditBusinessController() {
        this.businessService = new BusinessService(new BusinessRepository("businesses.dat"));
    }

    public void setBusinessService(BusinessService businessService) {
        this.businessService = businessService;
    }

    public void setBusiness(Business business, boolean isEdit) {
        this.business = business;
        this.isEdit = isEdit;
        if (isEdit && business != null) {
            nameField.setText(business.getName());
            descriptionField.setText(business.getDescription());
            contactInfoField.setText(business.getContactInfo());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String contactInfo = contactInfoField.getText();

        if (name.isEmpty() || description.isEmpty() || contactInfo.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter name, description, and contact info.");
            return;
        }

        if (isEdit) {
            business.setName(name);
            business.setDescription(description);
            business.setContactInfo(contactInfo);
            businessService.updateBusiness(business);
        } else {
            Business newBusiness = new Business(UUID.randomUUID().toString(), name, description, contactInfo, currentUser.getUsername());
            businessService.addBusiness(newBusiness);

                businessViewController.refreshBusinessList();

        }
        if (businessViewController != null) {
            businessViewController.refreshBusinessList();
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
