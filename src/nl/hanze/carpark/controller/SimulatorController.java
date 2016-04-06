package nl.hanze.carpark.controller;

import nl.hanze.carpark.main.CarPark;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.Car;
import nl.hanze.carpark.model.Location;
import nl.hanze.carpark.model.SimulatorModel;

import java.awt.event.ActionEvent;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorController extends AbstractController {
    private static final AbstractModel simModel = CarPark.getModel("SimulatorModel");

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
    }

    public int getNumberOfFloors() {
        if(simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfFloors();
        }
        return 0;
    }

    public int getNumberOfRows() {
        if(simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfRows();
        }
        return 0;
    }

    public int getNumberOfPlaces() {
        if(simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getNumberOfPlaces();
        }
        return 0;
    }

    public Car getCarAt(Location loc) {
        if(simModel instanceof SimulatorModel) {
            return ((SimulatorModel) simModel).getCarAt(loc);
        }
        return null;
    }

    public void tick() {
        if(simModel instanceof SimulatorModel) {
            ((SimulatorModel) simModel).tick();
        }
    }
}
