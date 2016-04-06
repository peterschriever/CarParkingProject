package nl.hanze.carpark.model;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorModel extends AbstractModel {
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;

    private Car[][][] cars;

    public SimulatorModel(int floors, int rows, int places) {
        numberOfFloors = floors;
        numberOfPlaces = places;
        numberOfRows = rows;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    public Car getCarAt(Location loc) {
        if(locationIsValid(loc)) {
            return cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
        }
        return null;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    private boolean locationIsValid(Location loc) {
        int floor = loc.getFloor();
        int row = loc.getRow();
        int place = loc.getPlace();
        return !(floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces);
    }
}
