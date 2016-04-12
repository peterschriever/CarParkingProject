package nl.hanze.carpark.controller;

import nl.hanze.carpark.main.CarPark;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.Car;
import nl.hanze.carpark.model.Location;
import nl.hanze.carpark.model.SimulatorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author peter
 * Part of the CarParkingProject project.
 * Class to communicate between views and models.
 *  set size of the total frame.
 * @see nl.hanze.carpark.controller.AbstractController
 *
 */
public class SimulatorController extends AbstractController {
    private static final AbstractModel simModel = CarPark.getModel("SimulatorModel");
    private JButton oneStep;
    private JButton hundredStep;
    private JButton applySimulatorSpeed;
    private JTextField simulatorSpeed;
    private JLabel simulatorSpeedLabel;

    private static final int width = 150;
    private static final int height = 120;


    /**
     * Constructor of the SimulatorController class.
     * Creates the buttons in the simulation.
     * Sets the sizes of the buttons, and adds them to the SimulatorController Jpanel.
     */
    public SimulatorController() {
        oneStep = new JButton("One Step");
        hundredStep = new JButton("Hundred Steps");
        applySimulatorSpeed = new JButton("Apply");
        simulatorSpeed = new JTextField("" + CarPark.simulationSpeed);

        setLayout(null);

        oneStep.setBounds(0, 0, width, 20);
        oneStep.addActionListener(this);
        add(oneStep);

        hundredStep.setBounds(0, 22, width, 20);
        hundredStep.addActionListener(this);
        add(hundredStep);

        simulatorSpeedLabel = new JLabel("Change Tick Speed:");
        simulatorSpeedLabel.setBounds(0, 50, width, 20);
        add(simulatorSpeedLabel);

        simulatorSpeed.setBounds((width / 2) - (width / 3), 70, (width / 3) * 2, 20);
        add(simulatorSpeed);

        applySimulatorSpeed.setBounds(0, 92, width, 20);
        applySimulatorSpeed.addActionListener(this);
        add(applySimulatorSpeed);

        setSize(width, height);

    }

    /**
     * @param e actionevent triggered by button.
     * check if e is a instanceof Jbutton, if so get the source. Check if its a button, if oneStep do a tick.
     *          if hundredStep make a new TickThread with a value of 100.
     *          if its applySimulatorSpeed, change the simulatorspeed, by the value given.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton jb = (JButton) e.getSource();
            if (jb == oneStep) {
                tick();
            } else if (jb == hundredStep) {
                Runnable r = new TickThread(100);
                new Thread(r).start();
            } else if (jb == applySimulatorSpeed) {
                CarPark.simulationSpeed = Integer.parseInt(simulatorSpeed.getText());
            }
        }
    }

    /**
     * Get the cars at the entrance queue.
     * @return The number of cars, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getCarsAtEntranceQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtEntranceQueue();
        }
        return 0;
    }

    /**
     * Get the cars at the payment queue.
     * @return The number of cars, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getCarsAtPaymentQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtPaymentQueue();
        }
        return 0;
    }


    /**
     * Get the cars at the exit queue.
     * @return the number of cars, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getCarsAtExitQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtExitQueue();
        }
        return 0;
    }

    /**
     * get the number of floors in the parking garage simulation.
     * @return the number of floors, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getNumberOfFloors() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfFloors();
        }
        return 0;
    }

    /**
     *
     * get the number of rows in the parking garage simulation.
     * @return the number of rows, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getNumberOfRows() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfRows();
        }
        return 0;
    }

    /**
     * get the number of places per row in the parking garage simulation.
     * @return the number of places, if simModel is a instanceof SimulatorModel otherwise return 0.
     */
    public int getNumberOfPlaces() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfPlaces();
        }
        return 0;
    }

    /**
     *  get the location of an car.
     * @param loc location of the car.
     * @return return the location of an car, if simModel is a instanceof SimulatorModel otherwise return null.
     */
    public Car getCarAt(Location loc) {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarAt(loc);
        }
        return null;
    }

    /**
     * execute the tick method if simModel is a instanceof SimulatorModel.
     */
    public void tick() {
        if (simModel instanceof SimulatorModel) {
            ((SimulatorModel) simModel).tick();
        }
    }
    /**
     * @author peter
     *  class TickThread implements class Runnable, so the instances of this class can be executed by a thread.
     */
    private class TickThread implements Runnable {
        private int steps;


        /**
         * @param  stepsIn the amount of int steps for variable steps
         * print out "TickThread constructor invoked."
         */
        public TickThread(int stepsIn) {
            steps = stepsIn;
            System.out.println("TickThread constructor invoked.");
        }


        /**
         * disable jbutton hundredStep
         * Print out "TickThread run invoked."
         * for int i lower than or same value as steps execute an tick.
         * enable jbutton hundredStep
         */
        @Override
        public void run() {
            hundredStep.setEnabled(false);
            System.out.println("TickThread run invoked.");
            for (int i = 0 ; i <= steps ; i++) {
                tick();
            }
            hundredStep.setEnabled(true);
        }
    }
}
