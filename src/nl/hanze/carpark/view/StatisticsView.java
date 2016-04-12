package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mathieu on 7-4-2016.
 */
public class StatisticsView extends AbstractView {
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private JLabel revenuePerTick;

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

    @Override
    public void updateView() {
        if(simController instanceof SimulatorController)
            revenuePerTick.setText(((SimulatorController) simController).getRevenuePerTick()+"");
        super.updateView();
    }
}


