package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;


/**
 * The QueueView makes sure the user can see a textual
 * representation of how the current CarQueues are looking
 * like. This view displays the amount of cars in the entranceCarQueue,
 * paymentCarQueue and exitCarQueue.
 */
public class QueueView extends AbstractView {
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private JLabel inEntranceLabel;
    private JLabel inPaymentLabel;
    private JLabel inExitLabel;

    private int atEntrance;
    private int atPayment;
    private int atExit;

    /**
     * The default constructor of QueueView.
     * This constructor sets the total size of the view and initiates some
     * Labels.
     */
    public QueueView() {
        Dimension size = new Dimension(250, 130);
        setSize(size);

        JLabel titleLabel = new JLabel("Queue Status");
        inEntranceLabel = new JLabel();
        inPaymentLabel = new JLabel();
        inExitLabel = new JLabel();

        add(titleLabel);
        add(inEntranceLabel);
        add(inPaymentLabel);
        add(inExitLabel);
    }

    /**
     * Override of the updateView method. This makes sure that
     * the view labels get updated with the new data calculated
     * by the models.
     */
    @Override
    public void updateView() {
        // retrieve new values
        if (!(simController instanceof SimulatorController)) {
            return;
        }
        
        atEntrance = ((SimulatorController) simController).getCarsAtEntranceQueue();
        atPayment = ((SimulatorController) simController).getCarsAtPaymentQueue();
        atExit = ((SimulatorController) simController).getCarsAtExitQueue();

        inEntranceLabel.setText("In Entrance Queue: " + atEntrance);
        inPaymentLabel.setText("In Payment Queue: " + atPayment);
        inExitLabel.setText("In Exit Queue: " + atExit);

        setVisible(true);
        super.updateView();
    }
}
