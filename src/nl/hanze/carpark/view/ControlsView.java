package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.main.CarPark;

import javax.swing.*;
import java.awt.*;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class ControlsView extends AbstractView {

    private static final AbstractController simController = CarPark.getController("SimulatorController");

    public ControlsView() {
        super(new BorderLayout());

        JButton oneStep = new JButton("One Step");
        JButton hundredStep = new JButton("Hundred Steps");

        add(oneStep, BorderLayout.NORTH);
        add(hundredStep, BorderLayout.SOUTH);
//        CarParkView carParkView = new CarParkView();
//        add(carParkView, BorderLayout.CENTER);

        oneStep.addActionListener(simController);
        hundredStep.addActionListener(simController);

        setSize(150, 50);

        setPreferredSize(new Dimension(150, 50));

    }



}
