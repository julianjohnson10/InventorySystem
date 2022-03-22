package Model;

//import Controller.InventorySystem;
import Main.InventorySystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Inventory Class
 * @author Julian Johnson
 */
public class Inventory {
    /**
     * Empty array, that holds all parts.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * Empty array that holds all products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart When this method is called, it will add newPart to the allparts array.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct Adds newProduct to the allProducts array.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partId the part id that is being searched.
     * @return the Part that corresponds with the id.
     */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     *
     * @param productId the product ID that is being searched.
     * @return the Product that corresponds with the provided id.
     */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     *
     * @return the size of the allparts array, and adds 1.
     */
    public static int generatePartID() {
        return allParts.size() + 1;
    }

    /**
     *
     * @return the size of the allproducts array, and adds 1.
     */
    public static int generateProductID() {
        return allProducts.size() + 1;
    }

    /**
     *
     * @param partName the part name to search.
     * @return the part(s) associated with the string partName.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();

        for(Part part:allParts){
            if (part.getName().contains(partName)){
                searchedPart.add(part);
            }
        }
        return searchedPart;
    }

    /**
     *
     * @param productName the name of the product being searched.
     * @return the product(s) being searched. They match string productName
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();

        for(Product product:allProducts){
            if (product.getName().contains(productName)){
                searchedProduct.add(product);
            }
        }
        return searchedProduct;
    }

    /**
     *
     * @param index the index of the part
     * @param selectedPart  the selected part in the tableview of the main screen.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    /**
     *
     * @param index The index of the product within the allproducts array.
     * @param newProduct the product that is being modified.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     *
     * @param selectedPart the part that is selected in the tableview. Removes the selected part from the allParts array.
     * @return true
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     *
     * @param selectedProduct the product that is selected in the tableview. Removes the selected product from the allProducts array.
     * @return true
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     *
     * @return Calling getAllParts() returns all of the parts in the allParts array.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return all of the products in the allProducts array.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Boolean to determine if this is the first run or not. This stops the test data from getting duplicated every time the main form controller is called.
     */
    private static boolean first = true;

    /**
     * Test data being added to the Inventory. This is so you can have initial parts/products in the main menu. I'm using computer-jargon in here, so parts will be something like computer components, where Products will be more like computer packages, or bundles.
     */
    public static void addData(){
        if (!first){
            return;
        }
        first = false;

        InHouse Motherboard = new InHouse(InventorySystem.generatePartID(),"Motherboard",199.99, 5, 2, 20, 1111);
        Inventory.addPart(Motherboard);

        InHouse Keyboard = new InHouse(InventorySystem.generatePartID(),"Keyboard", 299.89,4, 1, 10, 2222);
        Inventory.addPart(Keyboard);

        Product Gaming_PC = new Product(InventorySystem.generateProductID(), "Gaming PC",699.99, 3, 1, 5);
        Gaming_PC.addAssociatedPart(Motherboard);
        Inventory.addProduct(Gaming_PC);

        Product Gaming_Bundle = new Product(InventorySystem.generateProductID(), "Gaming Bundle", 100.99, 2, 1, 5);
        Gaming_Bundle.addAssociatedPart(Keyboard);
        Inventory.addProduct(Gaming_Bundle);

    }

    /**
     *
     * @return An alert Confirmation box that asks to confirm deletion with yes/cancel buttons.
     */
    public static Optional<ButtonType> alertDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setContentText("Are you sure you want to delete the selected item?");
        return alert.showAndWait();
    }

    /**
     *
     * @param message The custom message to add to the alert error box. Makes it easy to throw an alert, noptifying the user of incorrect data being added.
     */
    public static void alertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     *
     * @param event Main menu method. This is called for cancel buttons, save button events, etc... Brings back to main menu.
     * @throws IOException
     */
    public static void mainMenu(Event event) throws IOException {
        Parent root = FXMLLoader.load(InventorySystem.class.getResource("/View/Main_Form.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
