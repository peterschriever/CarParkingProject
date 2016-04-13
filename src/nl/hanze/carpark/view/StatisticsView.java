package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;


/**
 * The StatisticsView shows important Revenue based
 * statistics. Displays a textual representation of the
 * revenue from last tick and the current normal ticket price.
 */
public class StatisticsView extends AbstractView {
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private JLabel revenuePerTick;

    /**
     * The default constructor for the StatisticsView.
     * This creates some labels and sets the total size of this view.
     */
    public StatisticsView() {
        if(simController instanceof SimulatorController){
            JLabel ticketPrice = new JLabel("price of a ticket:"+((SimulatorController) simController).getTicketPrice());
            JLabel revenueLabel = new JLabel("normal ticket revenue:");
            revenuePerTick = new JLabel(""+((SimulatorController) simController).getRevenuePerTick());
            add(ticketPrice);
            add(revenueLabel);
            add(revenuePerTick);
        }
        Dimension size = new Dimension(150, 130);
        setSize(size);

    }

    /**
     * Overrides the updateView method to load new logic data
     * into the view labels.
     */
    @Override
    public void updateView() {
        if(simController instanceof SimulatorController)
            revenuePerTick.setText(((SimulatorController) simController).getRevenuePerTick()+"");
        super.updateView();
    }
}


