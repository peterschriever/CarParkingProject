package nl.hanze.carpark.model;

/**
 * The abstract Car super class. This class contains all the
 * common code of sub classes. 
 */
public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;

    /**
     * Constructor for objects of class testpackage.mvc.Car
     */
    public Car() {


    }

    /**
     * Getter for the current location of the Car
     * @return instance of Location corresponding to the Car instance
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Setter for the current location of the Car
     * @param location instance of Location corresponding to the Car location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Getter for the amount of minutes the car wishes to stay in its parking place
     * @return amount of minutes the car wishes to stay in its parking place
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * Setter for the amount of minutes the car wishes to stay in its parking place
     * @param minutesLeft amount of minutes the car wishes to stay in its parking place
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }

    /**
     * Getter for the isPaying field. Retrieve if the car is a paying car.
     * @return true when the car is paying, False if not.
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * Setter for the isPaying field. Set whether the car is a paying car
     * @param isPaying true when the car is a paying car, false if not.
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * Reduce the amount of time the car wishes to stay in its current location
     */
    public void tick() {
        minutesLeft--;
    }

}