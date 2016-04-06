package nl.hanze.carpark.main;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.SimulatorModel;
import nl.hanze.carpark.view.AbstractView;
import nl.hanze.carpark.view.CarParkView;
import nl.hanze.carpark.view.ControlsView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by peterzen on 4/5/16.
 * Part of the CarParkingProject project.
 */
public class CarPark {

    private static HashMap<String, AbstractController> controllers;
    private static HashMap<String, AbstractModel> models;
    private static HashMap<String, AbstractView> views;

    public CarPark() {
        // important: first models
        models = new HashMap<>();
        models.put("SimulatorModel", new SimulatorModel(3, 6, 30)); // set the simulator to 3 floors, 6 rows and 30 spaces

        // important: second controllers
        controllers = new HashMap<>();
        controllers.put("SimulatorController", new SimulatorController());

        JFrame screen = new JFrame("CityParking Simulator Tool");
        screen.setSize(850, 600);
        screen.setResizable(false);
        screen.setLayout(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the views to the screen
        ControlsView controlsView = new ControlsView();
        CarParkView carParkView = new CarParkView();

        screen.getContentPane().add(controlsView);
        screen.getContentPane().add(carParkView);

        carParkView.setBounds(100, 100, 750, 550);

        // important: thirdly views
        views = new HashMap<>();
        views.put("ControlsView", controlsView);
        views.put("CarParkView", carParkView);

        screen.setVisible(true);

        updateViews();
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

    private void updateViews() {
        for (Object o : views.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            String key = pair.getKey() instanceof String ? (String)pair.getKey():"";
            views.get(key).updateView();
        }
    }
}
