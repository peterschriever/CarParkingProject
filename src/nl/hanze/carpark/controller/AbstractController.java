package nl.hanze.carpark.controller;

import nl.hanze.carpark.view.AbstractView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by peterzen on 4/4/16.
 * Part of the CarParkingProject project.
 */
public abstract class AbstractController extends JPanel implements ActionListener {

    private ArrayList<AbstractView> views;

    public AbstractController() {
        views = new ArrayList<>();
    }

    public void addView(AbstractView view) {
        views.add(view);
    }

    public void notifyViews() {
        for(AbstractView v: views) v.updateView();
    }
}
