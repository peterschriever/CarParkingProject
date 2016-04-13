package nl.hanze.carpark.view;

import javax.swing.*;
import java.awt.*;


/**
 *
 * The View super class which is extended by every view
 * Part of the CarParkingProject project.
 *
 */
public abstract class AbstractView extends JPanel {

    /**
     * The default empty constructor.
     */
    public AbstractView() {
    }

    /**
     * An alternative constructor which can be used to set the
     * BorderLayout of the current view.
     *
     * @param bl an instance of the BorderLayout you wish to use
     */
    public AbstractView(BorderLayout bl) {
        super(bl);
    }

    /**
     * An alternative name for calling the repaint method.
     */
    public void updateView() {
        repaint();
    }

}
