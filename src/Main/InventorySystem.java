package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * InventorySystem class. main method called in this class.
 * @author Julian Johnson
 * "FUTURE ENHANCEMENT": Save the parts to a persistent dataframe, so the inventory can be saved when closing the application.
 * "FUTURE ENHANCEMENT": Possibly adding a clear search button, to make searching more intuitive.
 * "FUTURE ENHANCEMENT": Identify more input validation flaws.
 * "FUTURE ENHANCEMENT": Keep a database of the changes as well. Make a menu that allows to undo specific parts based on date. This may help in case of errors.
 * "FUTURE ENHANCEMENT": Add a scanner method that will take a unique id as input, kind of like a barcode system. This would add the items to inventory based on a scanning system.
 */
//Javadoc comments are in the javadoc folder, at the root of this Inventory Application.
public class InventorySystem extends Application {

    /**
     * Determines the ID of each part and product created. ID's always start as 1, and count up. If a part/product is deleted, and another modified, the new product/part will take the old ID.
     */
    private static int partUID = 1;
    /**
     * Determines the ID of each part and product created. ID's always start as 1, and count up. If a part/product is deleted, and another modified, the new product/part will take the old ID.
     */
    private static int projUID = 1;

    /**
     *
     * @param MainFormStage the Main menu stage. starts the Main form controller.
     * @throws Exception
     */
    @Override
    public void start(Stage MainFormStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Main_Form.fxml"));
        MainFormStage.setScene(new Scene(root));
        MainFormStage.setTitle("Inventory Management System");
        MainFormStage.show();
    }

    /**
     *
     * @return partUID++. the Part ID. Counts up for each new part.
     */
    public static int generatePartID() {
        return partUID++;
    }
    /**
     *
     * @return projUID++. the Product ID. Counts up for each new product.
     */
    public static int generateProductID() {
        return projUID++;
    }

    /**
     *
     * @param args Main method. Entry point of program.
     */
    public static void main(String[] args) {
        launch(args); }
}
