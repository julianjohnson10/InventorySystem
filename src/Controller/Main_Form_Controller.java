package Controller;

import Main.InventorySystem;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls every element that exists on the Main Screen.
 * @author Julian Johnson
 * FUTURE ENHANCEMENT: Add a "Clean" button that will clean up inventory numbers for Parts/Products that are no longer in use.
 * Add an undo button. Save old Parts to an archived array.
 */
public class Main_Form_Controller implements Initializable {

    /**
     * Table of all Parts
     */
    @FXML private TableView<Part> partTableView;
    /**
     * Part ID column
     */
    @FXML private TableColumn<Part, Integer> partIDColumn;
    /**
     * Part Name column in Parts table
     */
    @FXML private TableColumn<Part, String> partNameColumn;
    /**
     * Inventory Level column in Parts table
     */
    @FXML private TableColumn<Part, Integer> partInvColumn;
    /**
     * Price/Cost Per Unit column in Parts table.
     */
    @FXML private TableColumn<Part, Double> partPriceColumn;
    /**
     * Table of all Products
     */
    @FXML private TableView<Product> productTableView;
    /**
     * Product ID column in Products table.
     */
    @FXML private TableColumn<Product, Integer> productIDColumn;
    /**
     * Product Name column in Products table.
     */
    @FXML private TableColumn<Product, String> productNameColumn;
    /**
     * Inventory Level column in Products table.
     */
    @FXML private TableColumn<Product, Integer> productInvColumn;
    /**
     * Price/Cost Per Unit column in Products table.
     */
    @FXML private TableColumn<Product, Double> productPriceColumn;
    /**
     * Parts table searchbar.
     */
    @FXML private TextField searchParts;
    /**
     * Products table searchbar.
     */
    @FXML private TextField searchProducts;
    /**
     * Part type selectedPart. I use this to determine which part is selected in the parts table.
     */
    private static Part selectedPart;
    /**
     * Product type selectedProduct. I use this to determine which product is selected in the products table.
     */
    private static Product selectedProduct;
    /**
     * Main Screen exit button. Closes the program.
     */
    @FXML
    private void exitButton() {
        Platform.exit();
    }

    /**
     *
     * @param event loads the Add Part menu
     * @throws IOException
     */
    @FXML
    private void addPartMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(InventorySystem.class.getResource("/View/Add_Part_Form.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event Loads the Modify Part menu.
     * @throws IOException
     */
    @FXML
    private void modifyPartMenu(ActionEvent event) throws IOException {
        if(partTableView.getSelectionModel().getSelectedItem() != null){
            selectedPart = partTableView.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(InventorySystem.class.getResource("/View/Modify_Part_Form.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Inventory.alertError("Please select a part to modify.");
        }
    }
    /**
     *
     * @return Part selectedPart. When I call this method, the selected Part will get sent to the proper controller.
     */
    public static Part getSelectedPart(){
        return selectedPart;
    }
    /**
     *
     * @return Product selectedProduct. When I call this method, the selected Part will get sent to the proper controller.
     */
    public static Product getSelectedProduct(){
        return selectedProduct;
    }
    /**
     *
     * @param event "Add" button in the products pane. Takes you to the Add Product Menu
     * @throws IOException
     */
    @FXML
    private void addProductMenu(ActionEvent event) throws IOException {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(InventorySystem.class.getResource("/View/Add_Product_Form.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     *
     * @param event Modify button in the Products pane of the main menu. Takes you to the Modify Product screen.
     * @throws IOException
     */
    @FXML
    private void modifyProductMenu(ActionEvent event) throws IOException {
        if(productTableView.getSelectionModel().getSelectedItem() != null){
            selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(InventorySystem.class.getResource("/View/Modify_Product_Form.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Inventory.alertError("Please select a part to modify.");
        }
    }

    /**
     *
     * @param event Delete button in the Parts pane. Deletes the selected part from the list of parts. Handles errors with an alert box.
     */
    @FXML
    private void deletePart(ActionEvent event) {
        Part selected = partTableView.getSelectionModel().getSelectedItem();
        if(partTableView.getSelectionModel().getSelectedItem() != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selected);
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                partTableView.getSelectionModel().clearSelection();
                return;
            }
            partTableView.getSelectionModel().clearSelection();
            ObservableList<Part> allParts = Inventory.getAllParts();
            partTableView.setItems(allParts);
        }
        else{
            Inventory.alertError("Please select a part to delete.");
        }
    }

    /**
     *
     * @param event Delete button in the Products pane. Deletes the selected Product, as long as it doesn't have any associated
     *              parts. If it does, an alert box will pop up.
     */
    @FXML
    private void deleteProduct(ActionEvent event) {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                if(selectedProduct.getAllAssociatedParts().size() == 0){
                    Inventory.deleteProduct(selectedProduct);
                }
                else{
                    Inventory.alertError("In order to delete this product, you must first remove all associated parts.");
                }
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                productTableView.getSelectionModel().clearSelection();
                return;
            }
            productTableView.getSelectionModel().clearSelection();
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            productTableView.setItems(allProducts);
        }
        else{
            Inventory.alertError("Please select a part to delete.");
        }
    }

    /**
     *
     * @param event searches through the parts list for the string that gets entered into the Parts search box. If any part contains
     *              the string that is entered into the search box, the list will repopulate with the searched for data. If a number
     *              is entered instead, the list will repopulate with parts matching the ID. The ID is an exact match.
     *              COMPILE ERROR: java: ';' expected. Fixed multiple semicolon errors.
     *
     */
    @FXML
    public void partSearch(ActionEvent event){
        String search = searchParts.getText();
        ObservableList<Part> parts = Inventory.lookupPart(search);
        ObservableList<Part> allParts = Inventory.getAllParts();
        if(parts.size() == 0){
            try {
                int partID = Integer.parseInt(search);
                Part part = Inventory.lookupPart(partID);
                if(part != null){

                    partTableView.getSelectionModel().select(part);
                    return;
                }
                Inventory.alertError("ID was not found");
                return;
            }
            catch (NumberFormatException ignored){
                partTableView.getSelectionModel().clearSelection();
                partTableView.setItems(parts);
            }
        }
        partTableView.setItems(parts);
        partTableView.getSelectionModel().clearSelection();
    }

    /**
     *
     * @param event searches through the products list for the string that gets entered into the Products search box. If any product contains
     *              the string that is entered into the search box, the list will repopulate with the searched for data. If a number
     *              is entered instead, the list will repopulate with products that match the ID. The ID is an exact match.
     *              COMPILE ERROR: cannot find symbol allProducts. I forgot to declare the symbol in prodSearch.
     */
    @FXML
    public void prodSearch(ActionEvent event){
        String search = searchProducts.getText();
        ObservableList<Product> products = Inventory.lookupProduct(search);
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        if(products.size() == 0) {
            try {
                int prodID = Integer.parseInt(search);
                Product product = Inventory.lookupProduct(prodID);

                if (product != null) {
                    products.setAll(allProducts);
                    productTableView.getSelectionModel().select(product);
                    return;
                }
                Inventory.alertError("ID was not found");
                return;
            }
            catch (NumberFormatException ignored) {
                productTableView.getSelectionModel().clearSelection();
                productTableView.setItems(products);
            }
        }
        productTableView.setItems(products);
        productTableView.getSelectionModel().clearSelection();
    }
    /**
     * This will be the first thing that happens when the Main Menu stage is set. The Parts and Products panes fill up with test data.
     * @param url
     * @param resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        Inventory.addData();
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }
}
