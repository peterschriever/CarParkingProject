package nl.hanze.carpark.model;

import nl.hanze.carpark.controller.AbstractController;
import nl.hanze.carpark.controller.SimulatorController;
import nl.hanze.carpark.main.CarPark;

import java.util.Random;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorModel extends AbstractModel {
    private static final AbstractController simController = CarPark.getController("SimulatorController");

    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;

    private CarQueue entranceCarQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private int tickPause = 100;

    // Number of arriving cars per hour.
    int weekDayArrivals = 50; // average number of arriving cars per hour
    int weekendArrivals = 90; // average number of arriving cars per hour

    // Intervals for entering, paying and exiting cars.
    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 10; // number of cars that can pay per minute
    int exitSpeed = 9; // number of cars that can leave per minute
    int specialCarProbability = 4; // this means one in five cars will be a pass holder (0 counts)

    private Car[][][] cars;

    public SimulatorModel(int floors, int rows, int places) {
        numberOfFloors = floors;
        numberOfPlaces = places;
        numberOfRows = rows;

        entranceCarQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    public Car getCarAt(Location loc) {
        if (locationIsValid(loc)) {
            return cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
        }
        return null;
    }

    public boolean parkCar(Location loc, Car car) {
        if (!locationIsValid(loc)) {
            return false;
        }
        Car oldCar = getCarAt(loc);
        if (oldCar == null) {
            cars[loc.getFloor()][loc.getRow()][loc.getPlace()] = car;
            car.setLocation(loc);
            return true;
        }
        return false;
    }

    private void advanceTime() {
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
    }

    private int getCarsArrivingHourlyAverage() {
        // Calculate the number of cars that arrive this minute.
        return day < 5 ? weekDayArrivals : weekendArrivals;
    }

    private int getCarsArrivingPerMinute(int hourlyAverage, Random random) {
        // Calculate the number of cars that arrive this minute.
        double standardDeviation = hourlyAverage * 0.1;
        double numberOfCarsPerHour = hourlyAverage + random.nextGaussian() * standardDeviation;
        return (int) Math.round(numberOfCarsPerHour / 60);
    }

    /**
     * This method fills the entranceQueue field with the arriving cars
     * for the current tick.
     *
     * @param carsPerMinute Number of cars arriving per minute in this tick.
     * @param random        An instance of the java.util.Random class
     */
    private void fillEntranceQueueForTick(int carsPerMinute, Random random) {
        // Add the cars to the back of the queue.
        for (int i = 0; i < carsPerMinute; i++) {
            int customerChance = random.nextInt(specialCarProbability);

            if (customerChance == 0) {
                Car car = new ParkingPassCar();
                this.entranceCarQueue.addCar(car);
            } else if(customerChance == 1) {
                Car car = new Reservation();
                this.entranceCarQueue.addCar(car);
            } else {
                Car car = new AdHocCar();
                this.entranceCarQueue.addCar(car);
            }
        }
        // update views
        CarPark.updateViews();
    }

    private void fillParkingSpacesForTick(Random random) {
        // Remove car from the front of the queue and assign to a parking space.
        for (int i = 0; i < enterSpeed; i++) {
            Car car = entranceCarQueue.removeCar();
            CarPark.updateViews();

            if (car == null) {
                break;
            }
            // Find a space for this car.
            if (car instanceof Reservation) {
                Location freeReservatedLocation = this.getFirstFreeReservatedLocation();
                if (freeReservatedLocation != null) {
                    parkCar(freeReservatedLocation, car);
                    int stayMinutes = (int) (15 + random.nextFloat() * 10 * 60);
                    car.setMinutesLeft(stayMinutes);
                }
            } else {
                Location freeLocation = this.getFirstFreeLocation();
                if (freeLocation != null) {
                    parkCar(freeLocation, car);
                    int stayMinutes = (int) (15 + random.nextFloat() * 10 * 60);
                    car.setMinutesLeft(stayMinutes);
                }

                CarPark.updateViews();
            }
        }
    }

    /**
     * Loop trough the car park to get all cars and call the tick method
     */
    private void tickCars() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    /**
     * Get the first leaving car in the parking garage.
     *
     * @return null | Car object when a leaving car is found, otherwise null
     */
    private Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Remove a car at a certain location in the car park.
     *
     * @param location Location object where to remove the car
     * @return null | Car object when successfully removed, null if it failed
     */
    private Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }

        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        return car;
    }

    private void fillExitQueue() {
        while (true) {
            Car car = getFirstLeavingCar();
            if (car == null) {
                break;
            }

            /**
             * If the customer is an instance of the pass holder
             * we can skip the payment and leave the car park
             * immediately by adding the car to the exit que.
             */
            if(car instanceof ParkingPassCar){
                removeCarAt(car.getLocation());
                exitCarQueue.addCar(car);
            } else if(car instanceof Reservation) {
                removeCarAt(car.getLocation());
                exitCarQueue.addCar(car);
            } else {
                car.setIsPaying(true);
                paymentCarQueue.addCar(car);
            }
            CarPark.updateViews();
        }
    }

    private void getCarPayments() {
        // Let cars pay.
        for (int i = 0; i < paymentSpeed; i++) {
            Car car = paymentCarQueue.removeCar();
            CarPark.updateViews();

            if (car == null) {
                break;
            }

            // TODO Handle payment.
            removeCarAt(car.getLocation());
            CarPark.updateViews();

            exitCarQueue.addCar(car);
            CarPark.updateViews();
        }
    }

    public void tick() {
        advanceTime();
        Random random = new Random();
        int carsArrivingHourlyAverage = getCarsArrivingHourlyAverage();
        int carsArrivingPerMinute = getCarsArrivingPerMinute(carsArrivingHourlyAverage, random);
        fillEntranceQueueForTick(carsArrivingPerMinute, random);
        fillParkingSpacesForTick(random);
        tickCars();
        fillExitQueue();
        getCarPayments();
        cleanupExitQueue();

        // Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cleanupExitQueue() {
        // Cleanup leaving cars queue.
        for (int i = 0; i < exitSpeed; i++) {
            Car car = exitCarQueue.removeCar();
            if (car == null) {
                break;
            }
            CarPark.updateViews();
        }
    }

    /**
     * Gets the first free parking place that is available in the car park.
     *
     * @return null | Location object when free place is found, otherwise null
     */
    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < this.getNumberOfFloors(); floor++) {
            for (int row = 0; row < this.getNumberOfRows(); row++) {
                for (int place = 0; place < this.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Location getFirstFreeReservatedLocation() {
        for (int floor = 2; floor < this.getNumberOfFloors(); floor++) {
            for (int row = 0; row < this.getNumberOfRows(); row++) {
                for (int place = 0; place < this.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null)
                        return location;
                }
            }
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
