package com.app.app_rea;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

public class HelloApplication extends Application {

    // Déclaration des listes observables
    private final ObservableList<Property> properties = FXCollections.observableArrayList();
    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gestion Immobilière");

        // Tabs pour les différentes sections
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createPropertiesTab(),
                createClientsTab(),
                createTransactionsTab(),
                createAppointmentsTab()
        );

        Scene scene = new Scene(tabPane, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Gestion des biens immobiliers
    private Tab createPropertiesTab() {
        Tab tab = new Tab("Biens Immobiliers");
        tab.setClosable(false);

        VBox layout = new VBox(5);
        layout.setPadding(new Insets(5));

        // Formulaire de gestion des biens
        TextField typeField = new TextField();
        typeField.setPromptText("Type");
        TextField sizeField = new TextField();
        sizeField.setPromptText("Taille (m²)");
        TextField priceField = new TextField();
        priceField.setPromptText("Prix (€)");
        TextField locationField = new TextField();
        locationField.setPromptText("Localisation");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description détaillée");

        // Recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par prix, type ou localisation");

        Button addButton = new Button("Ajouter");
        Button modifyButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button searchButton = new Button("Rechercher");

        HBox form = new HBox(5, typeField, sizeField, priceField, locationField, addButton, modifyButton, deleteButton);

        TableView<Property> table = new TableView<>(properties);
        table.setPlaceholder(new Label("Aucun bien ajouté."));
        table.getColumns().addAll(
                createTableColumn("Type", "type", 100),
                createTableColumn("Taille", "size", 100),
                createTableColumn("Prix", "price", 100),
                createTableColumn("Localisation", "location", 150),
                createTableColumn("Description", "description", 300)
        );

        addButton.setOnAction(e -> {
            properties.add(new Property(
                    typeField.getText(),
                    sizeField.getText(),
                    priceField.getText(),
                    locationField.getText(),
                    descriptionField.getText()
            ));
            clearFields(typeField, sizeField, priceField, locationField, descriptionField);
        });

        modifyButton.setOnAction(e -> {
            Property selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setType(typeField.getText());
                selected.setSize(sizeField.getText());
                selected.setPrice(priceField.getText());
                selected.setLocation(locationField.getText());
                selected.setDescription(descriptionField.getText());
                table.refresh();
            }
        });

        deleteButton.setOnAction(e -> {
            Property selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) properties.remove(selected);
        });

        searchButton.setOnAction(e -> {
            String criteria = searchField.getText().toLowerCase();
            ObservableList<Property> filteredList = FXCollections.observableArrayList(
                    properties.stream()
                            .filter(p -> p.getType().toLowerCase().contains(criteria)
                                    || p.getPrice().toLowerCase().contains(criteria)
                                    || p.getLocation().toLowerCase().contains(criteria))
                            .toList()
            );
            table.setItems(filteredList);
        });

        layout.getChildren().addAll(form, descriptionField, searchField, searchButton, table);
        tab.setContent(layout);
        return tab;
    }

    // Gestion des clients
    private Tab createClientsTab() {
        Tab tab = new Tab("Clients");
        tab.setClosable(false);

        VBox layout = new VBox(5);
        layout.setPadding(new Insets(5));

        TextField nameField = new TextField();
        nameField.setPromptText("Nom");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact");
        TextArea preferencesField = new TextArea();
        preferencesField.setPromptText("Préférences et historique");

        Button addButton = new Button("Ajouter");
        Button modifyButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        HBox form = new HBox(5, nameField, contactField, addButton, modifyButton, deleteButton);

        TableView<Client> table = new TableView<>(clients);
        table.setPlaceholder(new Label("Aucun client ajouté."));
        table.getColumns().addAll(
                createTableColumn("Nom", "name", 150),
                createTableColumn("Contact", "contact", 150),
                createTableColumn("Préférences", "preferences", 300)
        );

        addButton.setOnAction(e -> {
            clients.add(new Client(nameField.getText(), contactField.getText(), preferencesField.getText()));
            clearFields(nameField, contactField, preferencesField);
        });

        modifyButton.setOnAction(e -> {
            Client selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setName(nameField.getText());
                selected.setContact(contactField.getText());
                selected.setPreferences(preferencesField.getText());
                table.refresh();
            }
        });

        deleteButton.setOnAction(e -> {
            Client selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) clients.remove(selected);
        });

        layout.getChildren().addAll(form, preferencesField, table);
        tab.setContent(layout);
        return tab;
    }

    // Gestion des transactions
    private Tab createTransactionsTab() {
        Tab tab = new Tab("Transactions");
        tab.setClosable(false);

        VBox layout = new VBox(5);
        layout.setPadding(new Insets(5));

        TextField propertyField = new TextField();
        propertyField.setPromptText("Bien");
        TextField clientField = new TextField();
        clientField.setPromptText("Client");
        TextField typeField = new TextField();
        typeField.setPromptText("Type (Vente/Location)");
        TextField amountField = new TextField();
        amountField.setPromptText("Montant (€)");

        Button addButton = new Button("Ajouter");
        Button deleteButton = new Button("Supprimer");
        Button generateContractButton = new Button("Générer Contrat");

        HBox form = new HBox(5, propertyField, clientField, typeField, amountField, addButton, deleteButton, generateContractButton);

        TableView<Transaction> table = new TableView<>(transactions);
        table.setPlaceholder(new Label("Aucune transaction ajoutée."));
        table.getColumns().addAll(
                createTableColumn("Bien", "property", 200),
                createTableColumn("Client", "client", 150),
                createTableColumn("Type", "type", 100),
                createTableColumn("Montant", "amount", 100),
                createTableColumn("Date de Transaction", "transactionDate", 150)
        );

        addButton.setOnAction(e -> {
            transactions.add(new Transaction(propertyField.getText(), clientField.getText(), typeField.getText(), amountField.getText(), LocalDate.now().toString()));
            clearFields(propertyField, clientField, typeField, amountField);
        });

        deleteButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) transactions.remove(selected);
        });

        generateContractButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Exemple d'ajout d'un contrat de vente ou location automatique
                String contract = "Contrat " + selected.getType() + "\n"
                        + "Bien: " + selected.getProperty() + "\n"
                        + "Client: " + selected.getClient() + "\n"
                        + "Montant: " + selected.getAmount() + " €\n"
                        + "Date de Transaction: " + selected.getTransactionDate();
                System.out.println(contract);
                // Affichage dans une fenêtre ou génération d'un fichier PDF à ce stade
            }
        });

        layout.getChildren().addAll(form, table);
        tab.setContent(layout);
        return tab;
    }

    // Gestion des rendez-vous
    private Tab createAppointmentsTab() {
        Tab tab = new Tab("Rendez-vous");
        tab.setClosable(false);

        VBox layout = new VBox(5);
        layout.setPadding(new Insets(5));

        TextField clientField = new TextField();
        clientField.setPromptText("Client");
        TextField propertyField = new TextField();
        propertyField.setPromptText("Bien");
        TextField dateField = new TextField();
        dateField.setPromptText("Date (JJ/MM/AAAA)");
        TextField timeField = new TextField();
        timeField.setPromptText("Heure (HH:MM)");

        Button addButton = new Button("Ajouter");
        Button deleteButton = new Button("Supprimer");

        HBox form = new HBox(5, clientField, propertyField, dateField, timeField, addButton, deleteButton);

        TableView<Appointment> table = new TableView<>(appointments);
        table.setPlaceholder(new Label("Aucun rendez-vous ajouté."));
        table.getColumns().addAll(
                createTableColumn("Client", "client", 150),
                createTableColumn("Bien", "property", 200),
                createTableColumn("Date", "date", 100),
                createTableColumn("Heure", "time", 100)
        );

        addButton.setOnAction(e -> {
            appointments.add(new Appointment(clientField.getText(), propertyField.getText(), dateField.getText(), timeField.getText()));
            clearFields(clientField, propertyField, dateField, timeField);
        });

        deleteButton.setOnAction(e -> {
            Appointment selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) appointments.remove(selected);
        });

        layout.getChildren().addAll(form, table);
        tab.setContent(layout);
        return tab;
    }

    private <T> TableColumn<T, String> createTableColumn(String title, String property, int width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(data -> {
            try {
                return new SimpleStringProperty(data.getValue().getClass().getMethod("get" + capitalize(property)).invoke(data.getValue()).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        return column;
    }

    private void clearFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Classes internes représentant les entités
    public static class Property {
        private String type, size, price, location, description;

        public Property(String type, String size, String price, String location, String description) {
            this.type = type;
            this.size = size;
            this.price = price;
            this.location = location;
            this.description = description;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Client {
        private String name, contact, preferences;

        public Client(String name, String contact, String preferences) {
            this.name = name;
            this.contact = contact;
            this.preferences = preferences;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContact() { return contact; }
        public void setContact(String contact) { this.contact = contact; }
        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }
    }

    public static class Transaction {
        private String property, client, type, amount, transactionDate;

        public Transaction(String property, String client, String type, String amount, String transactionDate) {
            this.property = property;
            this.client = client;
            this.type = type;
            this.amount = amount;
            this.transactionDate = transactionDate;
        }

        public String getProperty() { return property; }
        public void setProperty(String property) { this.property = property; }
        public String getClient() { return client; }
        public void setClient(String client) { this.client = client; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public String getTransactionDate() { return transactionDate; }
        public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    }

    public static class Appointment {
        private String client, property, date, time;

        public Appointment(String client, String property, String date, String time) {
            this.client = client;
            this.property = property;
            this.date = date;
            this.time = time;
        }

        public String getClient() { return client; }
        public void setClient(String client) { this.client = client; }
        public String getProperty() { return property; }
        public void setProperty(String property) { this.property = property; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}