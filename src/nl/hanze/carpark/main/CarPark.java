package nl.hanze.carpark.main;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.SimulatorModel;
import nl.hanze.carpark.view.AbstractView;
import nl.hanze.carpark.view.CarParkView;
import nl.hanze.carpark.view.GridView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by peterzen on 4/5/16.
 * Part of the CarParkingProject project.
 */
public class CarPark {

    private static HashMap<String, AbstractController> controllers = new HashMap<>();
    private static HashMap<String, AbstractModel> models = new HashMap<>();
    private static HashMap<String, AbstractView> views = new HashMap<>();

    private static final int width = 1200;
    private static final int height = 900;

    public CarPark() {
        // important: first models
        models.put("SimulatorModel", new SimulatorModel(3, 6, 30)); // set the simulator to 3 floors, 6 rows and 30 spaces

        // important: second controllers
        SimulatorController simController = new SimulatorController();
        controllers.put("SimulatorController", simController);

        JFrame screen = new JFrame("CityParking Simulator Tool");
        screen.setSize(width, height);
        screen.setResizable(false);
        screen.setLayout(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the views to the screen
        CarParkView carParkView = new CarParkView();
        GridView gridView = new GridView();

        screen.getContentPane().add(carParkView);
        screen.getContentPane().add(simController);

        carParkView.setBounds(100, 100, 750, 550);

        // important: thirdly views
        views.put("CarParkView", carParkView);
        views.put("GridView", gridView);

        screen.setVisible(true);

        /**
         * Debug: Draw a grid with 10 by 10 squares
         */
        gridView = new GridView(width, height);
        gridView.setBounds(0, 0, width, height);
        screen.getContentPane().add(gridView);

        updateViews();

        for (int i = 0; i < 20; i++) {
            simController.tick();
        }
    }

    public static AbstractController getController(String controller) {
        return controllers.get(controller) != null ? controllers.get(controller) : null;
    }

    public static AbstractModel getModel(String model) {
        return models.get(model) != null ? models.get(model) : null;
    }

    public static AbstractView getView(String view) {
        return views.get(view) != null ? views.get(view) : null;
    }

    public static void updateViews() {
        for (Object o : views.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            String key = pair.getKey() instanceof String ? (String) pair.getKey() : "";
            views.get(key).updateView();
        }
    }
}
