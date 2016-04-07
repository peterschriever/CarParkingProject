package nl.hanze.carpark.view;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Class GridView
 *
 * @author Koen Hendriks
 * @version 0.1 (06-04-2016)
 */

// Edited by Peter Schriever - Changed implementation slightly to suit this project - 2016.04.07
public class GridView extends AbstractView {

    private Dimension size;

    /**
     * Constructor of GridView. This sets the initial size.
     *
     */
    public GridView() {
        this.size = new Dimension(1200,750);
    }

    /**
     * Constructor of GridView that expects a given width
     * and height to set the grid to.
     *
     * @param width int width of the application
     * @param height int height of the application
     */
    public GridView(int width, int height) {
        this.size = new Dimension(width,height);
    }

    @Override
    /**
     * Paint the grid on the JPanel
     */
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        /**
         * Draw the vertical lines of the grid
         */
        for(int i = 10; i < size.width; i=i+10){
            Line2D line = new Line2D.Float(i,0,i,size.height);
            g2.draw(line);
        }

        /**
         * Draw the horizontal lines of the grid
         */
        for(int i = 10; i < size.height; i=i+10){
            Line2D line = new Line2D.Float(0,i,size.width,i);
            g2.draw(line);
        }
    }
}