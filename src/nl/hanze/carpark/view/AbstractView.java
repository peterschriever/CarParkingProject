package nl.hanze.carpark.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by peterzen on 4/5/16.
 * Part of the CarParkingProject project.
 */
public abstract class AbstractView extends JPanel {

    public AbstractView() {
    }

    public AbstractView(BorderLayout bl) {
        super(bl);
    }

    public void updateView() {
        repaint();
    }

}
