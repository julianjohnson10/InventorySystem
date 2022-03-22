package Model;

/**
 * class InHouse.java
 * @author Julian Johnson
 */
public class InHouse extends Part{
    /**
     * machine id of InHouse part.
     */
    private int machineId;

    /**
     * Constructor for InHouse class.
     * @param id Part ID.
     * @param name Part Name.
     * @param price Price of InHouse Part.
     * @param stock Stock of InHouse Part.
     * @param min Minimum stock.
     * @param max Maximum stock of InHouse parts.
     * @param machineId Machine ID.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @param machineId sets the machine id.
     */
    public void setMachineId(int machineId) {

        this.machineId = machineId;
    }

    /**
     *
     * @return machineID of the InHouse Part.
     */
    public int getMachineId(){

        return machineId;
    }
}
