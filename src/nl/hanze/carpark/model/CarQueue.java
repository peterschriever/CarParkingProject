package nl.hanze.carpark.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The CarQueue class is used for the different waiting queues
 * It uses an implementation of the java LinkedList.
 * @see java.util.LinkedList;
 */
public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    /**
     * Add a car to the queue
     * @param car an instance of Car you wish to add
     * @return true when the car was added, throws IllegalStateException if there was no space
     * @throws IllegalStateException if no space is available
     */
    public boolean addCar(Car car) {
        return queue.add(car);
    }

    /**
     * Remove a car from the queue
     * @return instance of the removed car
     */
    public Car removeCar() {
        return queue.poll();
    }

    /**
     * Retrieve the amount of elements in the queue
     * @return integer amount of elements inside this queue
     */
    public int size() {
        return queue.size();
    }

}
