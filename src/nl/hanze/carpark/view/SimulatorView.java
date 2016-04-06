package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorView extends AbstractView {

    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;

    private static final AbstractController simController = CarPark.getController("SimulatorController");

    public SimulatorView(int floors, int rows, int places) {
        super(new BorderLayout());
        numberOfFloors = floors;
        numberOfRows = rows;
        numberOfPlaces = places;

        JButton oneStep = new JButton("One Step");
        JButton hundredStep = new JButton("Hundred Steps");

        add(oneStep, BorderLayout.NORTH);
        add(hundredStep, BorderLayout.SOUTH);

        oneStep.addActionListener(simController);
        hundredStep.addActionListener(simController);

        setSize(550, 300);
    }


}
