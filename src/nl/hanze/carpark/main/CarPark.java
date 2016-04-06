package nl.hanze.carpark.main;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.SimulatorModel;
import nl.hanze.carpark.view.AbstractView;
import nl.hanze.carpark.view.SimulatorView;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by peterzen on 4/5/16.
 * Part of the CarParkingProject project.
 */
public class CarPark {

    private static HashMap<String, AbstractController> controllers;
    private static HashMap<String, AbstractModel> models;
    private static HashMap<String, AbstractView> views;

    public CarPark() {
        controllers = new HashMap<>();
        controllers.put("SimulatorController", new SimulatorController());

        models = new HashMap<>();
        models.put("SimulatorModel", new SimulatorModel());

        JFrame screen = new JFrame("CityParking Simulator Tool");
        screen.setSize(600, 400);
        screen.setResizable(false);
        screen.setLayout(null);

        // add the standard SimulatorView with 3 floors, 6 rows and 30 parking places
        SimulatorView simView = new SimulatorView(3, 6, 30);
        screen.getContentPane().add(simView);

        views = new HashMap<>();
        views.put("SimulatorView", simView);

        screen.setVisible(true);
    }

    public static AbstractController getController(String controller) {
        return controllers.get(controller);
    }

    public static AbstractModel getModel(String model) {
        return models.get(model);
    }

    public static AbstractView getView(String view) {
        return views.get(view);
    }
}
