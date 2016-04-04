package nl.hanze.carparksimulator.controller;

import nl.hanze.carparksimulator.view.AbstractView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by peterzen on 4/4/16.
 * Part of the CarParkingProject project.
 */
public abstract class AbstractController extends JPanel {
    private List<AbstractView> views;

    public AbstractController() {
        JButton jb = new JButton("text");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("do something");
            }
        });
    }
}
