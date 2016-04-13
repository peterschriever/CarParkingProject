package nl.hanze.carpark.model;

import nl.hanze.carpark.main.CarPark;

import java.util.Random;


/**
 * The SimulatorModel class is the main Model class of the CarPark simulator
 *
 * This class contains almost all the logic for the simulator and links
 * with the CarPark main class to call view updates when new data is calculated.
 */
public class SimulatorModel extends AbstractModel {

    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;

    private CarQueue entranceCarQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;

    private int carPayPerTick = 0;
    private double revenuePerTick = 0.0;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    // Number of arriving cars per hour.
    int weekDayArrivals = 50; // average number of arriving cars per hour
    int weekendArrivals = 150; // average number of arriving cars per hour

    // Intervals for entering, paying and exiting cars.
    private double ticketPrice = 5.25 ;
    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 10; // number of cars that can pay per minute
    int exitSpeed = 9; // number of cars that can leave per minute
    int specialCarProbability = 4; // this means one in five cars will be either a Pass holder or a Reservation (0 counts)

    int carWaitingPatience = 10; // how long a car waits at the entrance when its full

    private Car[][][] cars;

    /**
     * The default constructor of the SimulatorModel. This constructor takes
     * the amount of floors, rows and parking places that should be in the new
     * simulation.
     *
     * @param floors the amount of floors for the car park
     * @param rows  the amount of rows per floor
     * @param places the amount of parking places per row
     */
    public SimulatorModel(int floors, int rows, int places) {
        numberOfFloors = floors;
        numberOfPlaces = places;
        numberOfRows = rows;

        entranceCarQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Getter for the size of the entranceQueue.
     *
     * @return retrieves the amount of cars in the entranceQueue
     */
    public int getCarsAtEntranceQueue() {
        return entranceCarQueue.size();
    }

    /**
     * This is the getter for the paymentCarQueue size
     * @return int returns the size of paymantCarQueue
     */
    public int getCarsAtPaymentQueue() {
        return paymentCarQueue.size();
    }

    /**
     * This is the getter for the exitCarQueue size
     * @return int returns the size of exitCarQueue
     */
    public int getCarsAtExitQueue() {
        return exitCarQueue.size();
    }

    /**
     * Retrieves the Car object at a specific location if there is one.
     * @param loc the location to look at for a car
     * @return either an instance of Car when one is found or null if not.
     */
    public Car getCarAt(Location loc) {
        if (locationIsValid(loc)) {
            return cars[loc.getFloor()][loc.getRow()][loc.getPlace()];
        }
        return null;
    }

    /**
     * Set a specific location with a specific car
     * @param loc location corresponding to a location in the car park
     * @param car car you wish to park in the location
     * @return true when the car was parked. false if not.
     */
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

    /**
     * Advance the time in the simulation by one minute.
     */
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

    /**
     * Retrieves the amount of cars that arrive hourly.
     * @return the amount of cars that arrive hourly in either the weekend or weekdays
     */
    private int getCarsArrivingHourlyAverage() {
        // Calculate the number of cars that arrive this minute.
        return day < 5 ? weekDayArrivals : weekendArrivals;
    }

    /**
     * Calculate and return the amount of cars arriving every minute.
     * @param hourlyAverage the amount of cars arriving hourly
     * @param random any fresh instance of the java Random class
     * @return the amount of cars arriving per minute
     */
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
                if(getFirstFreeReservatedLocation() != null) {
                    Car car = new Reservation();
                    this.entranceCarQueue.addCar(car);
                }
            } else {
                Car car = new AdHocCar();
                this.entranceCarQueue.addCar(car);
            }
        }
        // update views
        CarPark.updateViews();
    }

    /**
     * Empties the entranceCarQueue and finds parking places
     * for the corresponding cars. When no parking places can be
     * found the cars will be directed to the exitCarQueue. Also
     * calculates how long a car will stay in the parking place.
     *
     * @param random any fresh instance of the java Random class
     */
    private void fillParkingSpacesForTick(Random random) {
        // Remove car from the front of the queue and assign to a parking space.
        for (int i = 0; i < enterSpeed; i++) {
            Car car = entranceCarQueue.removeCar();
            CarPark.updateViews();

            if (car == null) {
                break;
            }
            // Find a space for this car.
            Location freeLoc;
            if (car instanceof Reservation) {
                freeLoc = this.getFirstFreeReservatedLocation();
            } else {
                freeLoc = this.getFirstFreeLocation();
            }

            if (freeLoc != null) {
                parkCar(freeLoc, car);
                int stayMinutes = (int) (15 + random.nextFloat() * 10 * 60);
                car.setMinutesLeft(stayMinutes);
            } else {
                exitCarQueue.addCar(car);
            }
            CarPark.updateViews();
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

    /**
     * Retrieves cars that are leaving this tick. Behaves differently for
     * each type of Car subclass. ParkingPassCars get send to the exitCarQueue,
     * Reservations also get send to the exitCarQueue and all other cars
     * get send to the paymentCarQueue.
     */
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

    /**
     * Retrieve the revenue from cars in the paymentCarQueue.
     */
    private void getCarPayments() {
        // Let cars pay.
        for (int i = 0; i < paymentSpeed; i++) {
            carPayPerTick++;
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

    /**
     * Perform a single step in the simulation.
     */
    public void tick() {
        advanceTime();
        revenuePerTick = 0;
        Random random = new Random();
        int carsArrivingHourlyAverage = getCarsArrivingHourlyAverage();
        int carsArrivingPerMinute = getCarsArrivingPerMinute(carsArrivingHourlyAverage, random);
        fillEntranceQueueForTick(carsArrivingPerMinute, random);
        fillParkingSpacesForTick(random);
        tickCars();
        fillExitQueue();
        getCarPayments();
        calculateRevenue();
        resetCarsPaying();
        cleanupExitQueue();
    }

    private void resetCarsPaying() {
        carPayPerTick = 0;
    }

    private void calculateRevenue() {
        revenuePerTick = carPayPerTick * ticketPrice;
    }

    /**
     * Let cars leave the exitCarQueue. Amount of cars that can leave
     * depends on the exitSpeed field.
     */
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
        for (int floor = 0; floor < this.getNumberOfFloors() - 1; floor++) {
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

    /**
     * Gets the first free reservable parking place that is available.
     * @return null | Location object when free place is found, otherwise null
     */
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

    /**
     * Retrieve the amount of floors the car park has.
     * @return amount of floors
     */
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Retrieve the amount of parking places per row the car park has.
     * @return amount of parking places per row
     */
    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    /**
     * Retrieve the amount of rows per floor the car park has.
     * @return amount of rows per floor
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Checks whether a Location within the car park is valid and existing.
     * @param loc the location you wish to check
     * @return either true when the location exists or false if not.
     */
    private boolean locationIsValid(Location loc) {
        int floor = loc.getFloor();
        int row = loc.getRow();
        int place = loc.getPlace();
        return !(floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces);
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public double getRevenuePerTick() {
        return revenuePerTick;
    }
}
