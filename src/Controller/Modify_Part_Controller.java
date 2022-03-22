package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Modify Part Controller.
 * @author Julian Johnson
 */
public class Modify_Part_Controller implements Initializable {

    /**
     * Save button on modify part menu.
     */
    @FXML public Button saveButton;
    /**
     * Part ID textfield. Its disabled because numbers are auto-genereated.
     */
    @FXML public TextField tf_partid;
    /**
     * Inv textfield. Determines stock amount.
     */
    @FXML public TextField tf_inv;
    /**
     * Price textfield.
     */
    @FXML public TextField tf_price;
    /**
     * MachineID textfield.
     */
    @FXML public TextField tf_machineid;
    /**
     * max textfield in modify part menu.
     */
    @FXML public TextField tf_max;
    /**
     * min textfield in modify part menu.
     */
    @FXML public TextField tf_min;
    /**
     * InHouse radio button in the modify Part menu.
     */
    @FXML public RadioButton InHouseRadio;
    /**
     * Outsourced radio button in the modify Part menu.
     */
    @FXML public RadioButton OutsourcedRadio;
    /**
     * Toggled label. If inhouse, it gets set to machine ID. if Outsourced, it gets set to Company Name.
     */
    @FXML public Label toggledlabel;
    /**
     * textfield for the part name.
     */
    @FXML private TextField tf_name;
    /**
     * empty Part. When called, it will get the selected part from the parts list.
     */
    private Part selectedPart;

    /**
     *
     * @param event Cancel button in the Modify Part Menu. Once called, it takes you back to the Main Menu.
     * @throws IOException
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Inventory.mainMenu(event);
    }

    /**
     *
     * @param event Save button in the modify Part menu. Once saved, it will update the part with the new data from the textfields. Alert boxes will show up when incorrect data is entered.
     * @throws IOException
     */
    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        try {
            Double.parseDouble(tf_price.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price must be double.");
            return;
        }
        try {
            Integer.parseInt(tf_inv.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Stock must be an integer.");
            return;
        }
        try {
            Integer.parseInt(tf_min.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Min must be an integer.");
            return;
        }
        try {
            Integer.parseInt(tf_max.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Max must be an integer.");
            return;
        }
        String newName = tf_name.getText();

        double p = Double.parseDouble(tf_price.getText());
        DecimalFormat formatPrice = new DecimalFormat("0.00");
        double price = Double.parseDouble(formatPrice.format(p));
        int newStock = Integer.parseInt(tf_inv.getText());
        int newMin = Integer.parseInt(tf_min.getText());
        int newMax = Integer.parseInt(tf_max.getText());
        int stock = Integer.parseInt(tf_inv.getText());
        int index = Inventory.getAllParts().indexOf(selectedPart);

        if(InHouseRadio.isSelected()) {
            int newMID = Integer.parseInt(tf_machineid.getText());

            if (tf_name.getText().matches("[0-9]")){
                Inventory.alertError("Name must not contain numbers.");
            }
            if(tf_machineid.getText().matches("[a-zA-Z]")){
                Inventory.alertError("Machine ID must not contain letters. Only numbers.");
            }
            else if(stock > newMax){
                Inventory.alertError("Inventory must not be more than the max value.");
                return;
            }
            else if(newMin > newMax){
                Inventory.alertError("Minimum must not be more than the max value.");
                return;
            }
            else if(stock < newMin) {
                Inventory.alertError("Inventory must not be less than the minimum value.");
                return; //I forgot return statements at one point. it would keep going back to the main menu.
            }
            InHouse newPart = new InHouse(index+1, newName, price, newStock, newMin, newMax, newMID);
            Inventory.updatePart(index, newPart);
            Inventory.mainMenu(event);
        }
        else if (OutsourcedRadio.isSelected()){
            String company = (tf_machineid.getText());

            if (tf_name.getText().matches("[0-9]")) {
                Inventory.alertError("Name must not contain numbers.");
            }
            else if(stock > newMax){
                Inventory.alertError("Inventory must not be more than the max value.");
                return;
            }
            else if(newMin > newMax){
                Inventory.alertError("Minimum must not be more than the max value.");
                return;
            }
            else if(stock < newMin) {
                Inventory.alertError("Inventory must not be less than the minimum value.");
                return; //I forgot return statements at one point. it would keep going back to the main menu.
            }
            Outsourced newPart = new Outsourced(index+1, newName, price, newStock, newMin, newMax, company);
            Inventory.updatePart(index, newPart);
            Inventory.mainMenu(event);
        }


    }

    /**
     *
     * @param event Sets the label to "Machine ID", once the InHouse RadioButton is clicked.
     */
    @FXML
    public void InHouseRadio(ActionEvent event) {
        toggledlabel.setText("Machine ID");
    }

    /**
     *
     * @param event Sets the label to "Company Name", once the Outsourced RadioButton is clicked.
     */
    @FXML
    public void OutsourcedRadio(ActionEvent event) {
        toggledlabel.setText("Company Name");
    }

    /**
     * When the Modify Part menu is open, this is what is initialized. The textfields are set to the value of the selected part in the main menu.
     * @param url
     * @param resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resources){
        selectedPart = Main_Form_Controller.getSelectedPart();

        if (selectedPart instanceof InHouse){
            tf_machineid.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
            InHouseRadio.setSelected(true);
            toggledlabel.setText("Machine ID");
        }
        else if (selectedPart instanceof Outsourced){
            OutsourcedRadio.setSelected(true);
            toggledlabel.setText("Company Name");
            tf_machineid.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }
        tf_name.setText(selectedPart.getName());
        tf_inv.setText(String.valueOf(selectedPart.getStock()));
        tf_max.setText(String.valueOf(selectedPart.getMax()));
        tf_min.setText(String.valueOf(selectedPart.getMin()));
        tf_price.setText(String.valueOf(selectedPart.getPrice()));
    }
}
