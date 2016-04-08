package nl.hanze.carpark.view;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;
import nl.hanze.carpark.model.AdHocCar;
import nl.hanze.carpark.model.Car;
import nl.hanze.carpark.model.Location;
import nl.hanze.carpark.model.ParkingPassCar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class CarParkView extends AbstractView {
    private Dimension size;
    private Image carParkImage;
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private int numberOfFloors = 0;
    private int numberOfRows = 0;
    private int numberOfPlaces = 0;

    public CarParkView() {
        size = new Dimension(680, 360);
        setSize(size);

        if(simController instanceof SimulatorController) {
            numberOfFloors = ((SimulatorController) simController).getNumberOfFloors();
            numberOfRows = ((SimulatorController) simController).getNumberOfRows();
            numberOfPlaces = ((SimulatorController) simController).getNumberOfPlaces();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        if(carParkImage == null) {
            return;
        }

        g.drawImage(carParkImage, 0, 0, null);
    }

    @Override
    public void updateView() {
        // Create a new car park image if the size has changed.
        carParkImage = createImage(size.width, size.height);

        Graphics graphics = carParkImage.getGraphics();

        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    Location location = new Location(floor, row, place);
                    Car car = ((SimulatorController) simController).getCarAt(location);
                    Color color;
                    if (car == null) {
                        if(floor == 2) {
                            color = Color.ORANGE;
                        } else {
                            color = Color.WHITE;
                        }
                    } else if(car instanceof AdHocCar) {
                        color = Color.RED;
                    } else if(car instanceof ParkingPassCar) {
                        color = Color.BLUE;
                    } else {
                        color = Color.MAGENTA;
                    }

                    drawPlace(graphics, location, color);
                }
            }

            setVisible(true);
            super.updateView();
        }
    }

    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect((location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 60 + (location.getRow() % 2) * 20) -59,
                location.getPlace() * 10 + 30,
                20 - 1,
                10 - 1); // TODO use dynamic size or constants
    }
}
