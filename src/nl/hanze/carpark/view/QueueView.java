package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;

/**
 * Created by peterzen on 4/7/16.
 * Part of the CarParkingProject project.
 */
public class QueueView extends AbstractView {
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private JLabel inEntranceLabel;
    private JLabel inPaymentLabel;
    private JLabel inExitLabel;

    private int atEntrance;
    private int atPayment;
    private int atExit;

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
