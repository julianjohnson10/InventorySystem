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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Modify Product controller class
 * @author Julian Johnson
 */
public class Modify_Product_Controller implements Initializable {

    /**
     * Table of parts.
     */
    @FXML private TableView<Part> partTableView;
    /**
     * part ID column in the parts table.
     */
    @FXML private TableColumn<Part, String> partIDColumn;
    /**
     * Part name column in the parts table.
     */
    @FXML private TableColumn<Part, String> partNameColumn;
    /**
     * Part stock column in the parts table.
     */
    @FXML private TableColumn<Part, String> partInvColumn;
    /**
     * Part price column.
     */
    @FXML private TableColumn<Part, String> partPriceColumn;
    /**
     * Associated parts table in the modify product menu.
     */
    @FXML private TableView<Part> associatedPartsTable;
    /**
     * Part ID column in associated parts table.
     */
    @FXML private TableColumn<Part, String> associatedPartIDColumn;
    /**
     * Part Name column in the associated parts table.
     */
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    /**
     * Part stock column in associated parts table.
     */
    @FXML private TableColumn<Part, String> associatedPartInvColumn;
    /**
     * Part price column in associated parts table.
     */
    @FXML private TableColumn<Part, String> associatedPartPriceColumn;
    /**
     * Stock textfield. Determines Inventory amount of the modified Product.
     */
    @FXML public TextField stockField;
    /**
     * price textfield. Determines price of the modified Product.
     */
    @FXML public TextField priceField;
    /**
     * Max textfield.
     */
    @FXML public TextField maxField;
    /**
     * Min textfield. Minimum stock allowed to be entered per Product.
     */
    @FXML public TextField minField;
    /**
     * Name textfield. Determines the name of the product, once modified.
     */
    @FXML private TextField nameField;
    /**
     * Searchbar at the top right of the Modify Product Menu.
     */
    @FXML private TextField searchParts;
    /**
     * empty object that is defined within the methods. It will return the selected part in the parts tableview.
     */
    private Part selectedPart;
    /**
     * empty object that is defined within the methods. It will return the selected product in the product tableview.
     */
    private Product selectedProduct;
    /**
     * Empty array for associated parts to go.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     *
     * @param event Cancel button will take you back to the main menu.
     * @throws IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Inventory.mainMenu(event);
    }

    /**
     *
     * @param event Searches for the part in the partstableview.
     */
    @FXML
    private void partSearch(ActionEvent event){

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
     * @param event When the save button is clicked, Whatever is in the textfields will be added to the Product object. This includes all associated parts that were put in the associated parts table.
     *              Goes back to main menu once complete. All errors will be based on alert boxes.
     *              "COMPILE ERROR": Illegal start of expression. It was an extra semicolon within a closing parenthesis.
     *              "COMPILE ERROR": Cannot find symbol newName. I forgot that I had renamed all of the variables to make it easier. I did not change the variables in the product
     * @throws IOException
     */
    @FXML
    private void saveProduct(ActionEvent event) throws IOException {
        String name = nameField.getText();
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
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());

        int index = Inventory.getAllProducts().indexOf(selectedProduct);

        Product newProduct = new Product(index+1,name,price,stock,min,max);

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
        Inventory.updateProduct(index, newProduct);
        if (associatedPartsTable != null){
            for (Part newPart : associatedParts){
                newProduct.addAssociatedPart(newPart);
            }
        }
        Inventory.mainMenu(event);
    }
    /**
     * Removes the selected part from the associated parts tableview. if nothing is selected, an alertbox will show.
     */
    @FXML
    public void removeAssociatedPart(){
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
     * Adds the selected part from the partstableview to the associatedparts array. If nothing is selected, then an alertbox will show up.
     */
    @FXML
    public void addAssociatedPart(){
        selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (partTableView.getSelectionModel().getSelectedItem() != null){
            associatedParts.add(selectedPart);
        }
        else {
            Inventory.alertError("Please select a part to add.");
        }
    }

    /**
     * Sets the tableviews and the textfields.
     * @param url
     * @param resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        selectedProduct = Main_Form_Controller.getSelectedProduct();
        associatedParts = selectedProduct.getAllAssociatedParts();
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        if(selectedProduct != null){
            associatedPartsTable.setItems(associatedParts);
            nameField.setText(selectedProduct.getName());
            stockField.setText(String.valueOf(selectedProduct.getStock()));
            maxField.setText(String.valueOf(selectedProduct.getMax()));
            minField.setText(String.valueOf(selectedProduct.getMin()));
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
        }
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }
}
