package ore;

import ore.Machine.*;

/**
 * Defines an interface for control strategies used to govern the behavior of machines.
 * By implementing this interface, different control strategies can be created, such as manual, automatic, or remote control.
 * Each strategy specifies how to manipulate a specific machine through the implementation of the control method.
 */
public interface ControlStrategy {
    /**
     * Controls the specified machine to perform certain actions.
     * This method contains the specific logic for control, which can vary based on the implementation.
     *
     * @param machine The machine instance to be controlled.
     */
    void control(Machine machine);
}
