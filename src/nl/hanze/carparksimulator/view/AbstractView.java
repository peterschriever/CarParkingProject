package nl.hanze.carparksimulator.view;

import javax.swing.*;

/**
 * Created by peterzen on 4/4/16.
 * Part of the CarParkingProject project.
 */
public abstract class AbstractView extends JPanel {

    public AbstractView() {
        // base view class to be extended by project view classes
    }

    public void updateView() {
        repaint();
    }

}
