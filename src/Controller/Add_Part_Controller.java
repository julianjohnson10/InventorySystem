package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Add Part Controller. Controls the Add Part menu.
 * @author Julian Johnson
 */
public class Add_Part_Controller {
    /**
     * Label that changes depending on if the Part is InHouse or Outsourced. InHouse parts get an ID, and Outsourced parts get a company name field.
     * COMPILE ERROR: java: cannot find symbol variable toggledlabel. I forgot to add toggledlabel in this code, so I'm declaring it.
     */
    public Label toggledlabel;
    /**
     * Radio Button for InHouse part selection.
     */
    public RadioButton InHouseRadio;
    /**
     * RadioButton for Outsourced part selection.
     */
    public RadioButton OutsourcedRadio;
    /**
     * Save button on Add Part menu.
     */
    public Button saveButton;
    /**
     * name of the Part in add part menu.
     */
    public TextField nameField;
    /**
     * Inv field in add part menu. This determines stock amount.
     */
    public TextField invField;
    /**
     * Price textfield in add part menu.
     */
    public TextField priceField;
    /**
     * Id_Companyfield is either an ID, or a company name, depending on whether InHouse or Outsourced part is created.
     */
    public TextField id_companyField;
    /**
     * Min field in add part menu. Determines minimum stock amount allowed.
     */
    public TextField minField;
    /**
     * Max field in add part menu. Determines maximum stock amount allowed.
     */
    public TextField maxField;

    /**
     *
     * @param event cancel button event. If cancel is pressed, the menu will change from the Add Part Form to Main Form.
     * @throws IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Main_Form.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     *
     * @param event if InHouse Radio gets selected, then the toggledlabel will be "Machine ID".
     */
    @FXML
    public void InHouseRadio(ActionEvent event) {
        toggledlabel.setText("Machine ID");
    }
    /**
     *
     * @param event if Outsourced radio gets selected, then the toggledlabel will be "Company Name".
     */
    @FXML
    public void OutsourcedRadio(ActionEvent event) {
        toggledlabel.setText("Company Name");
    }

    /**
     *
     * @param event Save button in Add Part Menu. When a part is typed into the form, the save button validates all of the information entered, and with the new inputs, creates a Part.
     * @throws IOException
     */
    @FXML
    private void savePart(ActionEvent event) throws IOException{
        try {
            Double.parseDouble(priceField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price must be double.");
            return;
        }
        try {
            Integer.parseInt(invField.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Inv must be an integer.");
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
        String name = nameField.getText();
        double p = Double.parseDouble(priceField.getText());
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        double price = Double.parseDouble(priceFormat.format(p));

        int stock = Integer.parseInt(invField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());

        if (InHouseRadio.isSelected()) {
            int id_company = Integer.parseInt(id_companyField.getText());


            InHouse newPart = new InHouse(Inventory.generatePartID(),name,price,stock,min, max,id_company);
            int id = newPart.getMachineId();

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
                return; //I forgot return statements at one point. it would keep going back to the main menu.
            }
            Inventory.addPart(newPart);
        }
        else if (OutsourcedRadio.isSelected()) {
            String id_company = id_companyField.getText();
            Outsourced newPart = new Outsourced(Inventory.generateProductID(),name, price, stock, min, max, id_company);

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
                return; //I forgot return statements at one point. it would keep going back to the main menu.
            }
            Inventory.addPart(newPart);
        }
        //back to main menu.
        Inventory.mainMenu(event);
    }
}
