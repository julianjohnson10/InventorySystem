package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add Product Controller class.
 * @author Julian Johnson
 */
public class Add_Product_Controller implements Initializable {

    /**
     * Table of parts in the Add Product Menu.
     */
    @FXML private TableView<Part> partTableView;
    /**
     * Part ID column.
     */
    @FXML private TableColumn<Part, Integer> partIDColumn;
    /**
     * Part Name column.
     */
    @FXML private TableColumn<Part, String> partNameColumn;
    /**
     * Part Inventory amount column. Displays the stock.
     */
    @FXML private TableColumn<Part, Integer> partInvColumn;
    /**
     * Part Price column.
     */
    @FXML private TableColumn<Part, Double> partPriceColumn;
    /**
     * Product name goes here when adding a new product.
     */
    @FXML private TextField nameField;
    /**
     * Stock goes here when adding a new stock.
     */
    @FXML private TextField stockField;
    /**
     * Product price goes here when adding a new product.
     */
    @FXML private TextField priceField;
    /**
     * product minimum stock field.
     */
    @FXML private TextField minField;
    /**
     * Product max stock field.
     */
    @FXML private TextField maxField;
    /**
     * Top right of add product menu. Searches parts in the parts table below it.
     */
    @FXML private TextField searchParts;
    /**
     * Products contain associated Parts from the Inventory. This table reflects the associated parts.
     */
    @FXML private TableView<Part> associatedPartsTable;
    /**
     * Associated part ID number.
     */
    @FXML private TableColumn<Part, String> associatedPartIDColumn;
    /**
     * Associated part name.
     */
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    /**
     * Associated part stock amount.
     */
    @FXML private TableColumn<Part, String> associatedPartInvColumn;
    /**
     * Associated Part price column.
     */
    @FXML private TableColumn<Part, String> associatedPartPriceColumn;
    /**
     * Array list of associated parts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * empty Part. When called, it will get the selected part from the parts list.
     */
    private static Part selectedPart;

    /**
     *
     * @param event Cancel button returns to the Main Screen.
     * @throws IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Inventory.mainMenu(event);
    }

    /**
     *
     * @param event "Remove Associated Part" button in the Add Product Menu. It will remove the selected part from the associated parts table. Handles errors with alert boxes if nothing is selected.
     */
    @FXML
    public void removePart(ActionEvent event){

        Part selected = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(associatedPartsTable.getSelectionModel().getSelectedItem() != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {

                associatedParts.remove(selected);
            }

            associatedPartsTable.getSelectionModel().clearSelection();
        }
        else{
            Inventory.alertError("Please select a part to remove from the Associated Parts list.");
        }
    }

    /**
     *
     * @param event When ENTER is pressed in the search box, this event will trigger.
     */
    @FXML
    public void partSearch(ActionEvent event){
        String search = searchParts.getText();
        ObservableList<Part> parts = Inventory.lookupPart(search);

        if(parts.size() == 0){
            try {
                int partID = Integer.parseInt(search);
                Part part = Inventory.lookupPart(partID);
                if(part != null){
                    parts.add(part);
                }
            }
            catch (NumberFormatException ignored){
                partTableView.setItems(parts);
            }
        }
        partTableView.setItems(parts);
    }

    /**
     *
     * @param event Save button on Add Product menu. Once save is pressed, a Product will be created along with its associated parts. Goes back to the main menu once complete.
     *              "RUNTIME ERROR": Number format exception when entering in letters into the price field. Using a try catch block to catch numberformatexception errors. Using the alert box when those errors are found.
     *              "COMPILE ERROR": <identifier> expected. I forgot to name the Numberformatexception
     * @throws IOException
     */
    @FXML
    private void saveProduct(ActionEvent event) throws IOException {
        selectedPart = Main_Form_Controller.getSelectedPart();
        try {
            Double.parseDouble(priceField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price must be double.");
            return;
        }
        try {
            Integer.parseInt(stockField.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Stock must be an integer.");
            return;
        }
        try {
            Integer.parseInt(minField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Min must be an integer.");
            return;
        }
        try {
            Integer.parseInt(maxField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Max must be an integer.");
            return;
        }
        double p = Double.parseDouble(priceField.getText());
        String name = nameField.getText();
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double price = Double.parseDouble(decimalFormat.format(p));

        Product newProduct = new Product(Inventory.generateProductID(),name,price,stock,min,max);
        if(stock > max){
            Inventory.alertError("Inventory must not be more than the max value.");
            return;
        }
        else if(min > max){
            Inventory.alertError("Minimum bust not be more than the max value.");
            return;
        }
        else if(stock < min) {
            Inventory.alertError("Inventory must not be less than the minimum value.");
            return;
        }
        else if(priceField.getText().matches("[a-z]") ||  stockField.getText().matches("[a-zA-Z]") || minField.getText().matches("[a-zA-Z]") || maxField.getText().matches(("[a-zA-Z]"))){
            Inventory.alertError("Price, Stock, Min, and Max fields must not contain letters.");
        }

        Inventory.addProduct(newProduct);
        if (associatedPartsTable != null){
            for (Part newPart : associatedParts){
                newProduct.addAssociatedPart(newPart);
            }
        }
        Inventory.mainMenu(event);
    }

    /**
     *
     * @param event "Add" button in the Add Product menu. Once add is clicked, the selected part from the parts table will be added to the associated parts table.
     * @throws IOException
     */
    @FXML
    private void addButton(ActionEvent event) throws IOException {

        if(partTableView.getSelectionModel().getSelectedItem() != null){
            selectedPart = partTableView.getSelectionModel().getSelectedItem();
            associatedParts.add(selectedPart);
            associatedPartsTable.setItems(associatedParts);
            associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        }
        else{
            Inventory.alertError("Please select a part to add.");
        }
    }

    /**
     * initialize is called first once the add product controller is opened.
     * @param url
     * @param resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}
