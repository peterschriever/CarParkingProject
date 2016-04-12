package nl.hanze.carpark.controller;

import nl.hanze.carpark.main.CarPark;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.Car;
import nl.hanze.carpark.model.Location;
import nl.hanze.carpark.model.SimulatorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorController extends AbstractController implements Runnable {
    private static final AbstractModel simModel = CarPark.getModel("SimulatorModel");
    private JButton oneStep;
    private JButton hundredStep;
    private JButton applySimulatorSpeed;
    private JTextField simulatorSpeed;
    private JLabel simulatorSpeedLabel;

    private static final int width = 150;
    private static final int height = 120;


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

    public int getCarsAtEntranceQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtEntranceQueue();
        }
        return 0;
    }

    public int getCarsAtPaymentQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtPaymentQueue();
        }
        return 0;
    }
    public double getTicketPrice() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getTicketPrice();
        }
        return 0;
    }
    public double getRevenuePerTick() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getRevenuePerTick();
        }
        return 0;
    }

    public int getCarsAtExitQueue() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarsAtExitQueue();
        }
        return 0;
    }

    public int getNumberOfFloors() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfFloors();
        }
        return 0;
    }

    public int getNumberOfRows() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfRows();
        }
        return 0;
    }

    public int getNumberOfPlaces() {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfPlaces();
        }
        return 0;
    }

    public Car getCarAt(Location loc) {
        if (simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarAt(loc);
        }
        return null;
    }

    public void tick() {
        if (simModel instanceof SimulatorModel) {
            ((SimulatorModel) simModel).tick();
        }
    }

    @Override
    public void run() {
        System.out.println("new Thread in SimulatorController made.");
        if (simModel instanceof SimulatorModel) {
            for (int i = 0; i <= 100; i++) {
                ((SimulatorModel) simModel).tick();
            }
        }
    }

    private class TickThread implements Runnable {
        private int steps;

        public TickThread(int stepsIn) {
            steps = stepsIn;
            System.out.println("TickThread constructor invoked.");
        }

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
