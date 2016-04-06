package nl.hanze.carpark.controller;

import java.awt.event.ActionEvent;

/**
 * Created by peterzen on 4/6/16.
 * Part of the CarParkingProject project.
 */
public class SimulatorController extends AbstractController {


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
    }

}
