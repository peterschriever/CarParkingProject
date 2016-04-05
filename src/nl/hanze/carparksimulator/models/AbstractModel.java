package nl.hanze.carparksimulator.models;

import nl.hanze.carparksimulator.view.AbstractView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterzen on 4/5/16.
 * Part of the CarParkingProject project.
 */
public abstract class AbstractModel {
    private List<AbstractView> views;

    public AbstractModel() {
        views = new ArrayList<>();
    }

    public void addView(AbstractView view) {
        views.add(view);
    }

    public void notifyViews() {
        for (AbstractView v : views)
            v.updateView();
    }

}
