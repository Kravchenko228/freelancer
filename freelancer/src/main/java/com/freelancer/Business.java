package com.freelancer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.IOException;

import java.io.Serializable;

public class Business implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private transient StringProperty name;
    private transient StringProperty description;
    private String ownerUsername;
    private String contactInfo;


    private String nameValue;
    private String descriptionValue;

    public Business(String name, String description, String contactInfo, String ownerUsername) {
        this.nameValue = name;
        this.descriptionValue = description;
        this.contactInfo = contactInfo;
        this.ownerUsername = ownerUsername;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public Business(String id, String name, String description, String contactInfo, String ownerUsername) {
        this(name, description, contactInfo, ownerUsername);
        this.id = id;
    }

    public Business(String name, String description, String ownerUsername) {
        this.nameValue = name;
        this.descriptionValue = description;
        this.ownerUsername = ownerUsername;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        this.nameValue = name;
    }

    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(this, "name", nameValue);
        }
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
        this.descriptionValue = description;
    }

    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty(this, "description", descriptionValue);
        }
        return description;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.name = new SimpleStringProperty(nameValue);
        this.description = new SimpleStringProperty(descriptionValue);
    }
}
