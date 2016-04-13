package nl.hanze.carpark.main;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.model.AbstractModel;
import nl.hanze.carpark.model.SimulatorModel;
import nl.hanze.carpark.view.AbstractView;
import nl.hanze.carpark.view.CarParkView;
import nl.hanze.carpark.view.GridView;
import nl.hanze.carpark.view.QueueView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * The programs Main class. This class is the central point from which
 * Everything gets initiated.
 *
 * @author peter
 *
 */
public class CarPark {

    private static HashMap<String, AbstractController> controllers = new HashMap<>();
    private static HashMap<String, AbstractModel> models = new HashMap<>();
    private static HashMap<String, AbstractView> views = new HashMap<>();

    private static final int width = 1200;
    private static final int height = 900;

    private boolean run;

    public static int simulationSpeed = 1000;

    /**
     * The default constructor of CarPark, this starts of the whole program.
     * The default behaviour is to start the simulator with a tick every 1000ms.
     */
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
        QueueView queueView = new QueueView();

        screen.getContentPane().add(carParkView);
        screen.getContentPane().add(queueView);
        screen.getContentPane().add(simController);

        carParkView.setBounds(180, 20, carParkView.getWidth(), carParkView.getHeight());
        queueView.setBounds(190 + carParkView.getWidth(), 20, queueView.getWidth(), queueView.getHeight());
        simController.setBounds(20, 20, simController.getWidth(), simController.getHeight());

        // important: thirdly views
        views.put("CarParkView", carParkView);
        views.put("GridView", gridView);
        views.put("QueueView", queueView);

        screen.setVisible(true);

        /**
         * Debug: Draw a grid with 10 by 10 squares
         */
        gridView = new GridView(width, height);
        gridView.setBounds(0, 0, width, height);
        screen.getContentPane().add(gridView);

        updateViews();

        run = true;
        while (run) {
            simController.tick();
            try {
                Thread.sleep(simulationSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                run = false;
            }
        }
    }

    /**
     * Retrieve the instance of an AbstractController from the CarPark controllers HashMap.
     * @param controller Name of the controller class you wish to retrieve.
     * @return This returns either an instance of AbstractController or null if not found.
     */
    public static AbstractController getController(String controller) {
        return controllers.get(controller) != null ? controllers.get(controller) : null;
    }

    /**
     * Retrieve the instance of an AbstractModel from the CarPark models HashMap.
     * @param model Name of the model class you wish to retrieve.
     * @return This returns either an instance of AbstractModel or null if not found.
     */
    public static AbstractModel getModel(String model) {
        return models.get(model) != null ? models.get(model) : null;
    }

    /**
     * Retrieve the instance of an AbstractModel from the CarPark views HashMap.
     * @param view Name of the view class you wish to retrieve.
     * @return This returns either an instance of AbstractModel or null if not found.
     */
    public static AbstractView getView(String view) {
        return views.get(view) != null ? views.get(view) : null;
    }

    /**
     * Loop through every view in the views HashMap and call its updateView method.
     * Makes every view in the program effectively repaint.
     */
    public static void updateViews() {
        for (Object o : views.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            String key = pair.getKey() instanceof String ? (String) pair.getKey() : "";
            views.get(key).updateView();
        }
    }
}
