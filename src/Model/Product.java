package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class
 * @author Julian Johnson
 */
public class Product {
    /**
     * Array list of associated Parts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * Int id. Each product has an ID.
     */
    private int id;
    /**
     * Product name.
     */
    private String name;
    /**
     * product price.
     */
    private double price;
    /**
     * Product stock.
     */
    private int stock;
    /**
     * Product minimum value.
     */
    private int min;
    /**
     * Product maximum value.
     */
    private int max;

    /**
     * Product constructor
     * @param id product id
     * @param name product name
     * @param price product price
     * @param stock product stock
     * @param min product min
     * @param max product max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @param id the id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price the price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @param stock The stock to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @param min The minimum value to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @param max The max value to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return the ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return The product name.
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return The product price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return The product stock.
     */
    public int getStock() {
        return stock;
    }
    /**
     * @return The minimum value.
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return The maximum value.
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param part The part to add to the associatedParts table.
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
