package pt.ua.m.simon.view;

import pt.ua.gboard.GBoard;
import pt.ua.gboard.GBoardInputHandler;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.shapes.ShapeGelem;
import pt.ua.m.simon.IObservable;
import pt.ua.m.simon.IObserver;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

// This is the View
public class ElevatorGBoard implements IObserver {

    private static GBoard board;

    private LinkedList<IObservable> observables;
    private int NUM_COLS = 8;
    private int NUM_LINES = 8;

    public static GBoard getBoard() {
        return board;
    }

    public ElevatorGBoard(LinkedList<IObservable> observables, int floors) {
        NUM_LINES = floors+1;
        board = new GBoard("Test", NUM_LINES, NUM_COLS, 60, 60, 3);
        this.observables = observables;
        for(IObservable u : this.observables) {
            u.registerObserver(this);
        }
        setupBoard();
    }

    private void setupBoard()
    {
        board.pushInputHandler(new InputHandler());
        JSlider slider = new JSlider(2,120,60);
        slider.addChangeListener(e -> {
            int size = ((JSlider)e.getSource()).getValue();
            board.frame().setSize(size*8, size*8);
        });
        board.contentPane().add(slider, BorderLayout.NORTH);
        board.frame().pack();

        // TEST
        Object[] c1 = {
                "line", 0.25, 0.25, 0.75, 0.25,
                "color", Color.green,
                "line-width", 0.1};
        board.draw(new ShapeGelem(c1), 0, 0, 0);


//        Gelem elevatorCase = new FilledGelem(Color.orange, 100, board.numberOfLines(), 1);
//        board.draw(elevatorCase, 0, 2, 0);

        int noLines = board.numberOfLines();
        for (int line = 0; line < noLines; line++) {
            String text = String.format("Floor %d",noLines - line);
            board.draw(new StringGelem(text, Color.green,1,board.numberOfColumns()), line, 0, 0);//row,column,layer
        }


        for(IObservable o : observables){
            BuildingPosition pos = o.getPosition();
            board.draw(o.getGelem(),pos.getLine(),pos.getColumn(),pos.getLayer());
        }
    }


    @Override
    public void update(IObservable o) {
        BuildingPosition new_pos = o.getPosition();
        BuildingPosition old_pos = o.getPrevPosition();

        board.move(o.getGelem(),old_pos.getLine(), old_pos.getColumn(),new_pos.getLine(),new_pos.getColumn());
    }
}

class InputHandler extends GBoardInputHandler
{
    public InputHandler()
    {
        super(mousePressedMask | keyPressedMask);
    }

    public void run(GBoard board, int line, int column, int layer, int type, int code, Gelem gelem)
    {
        System.out.println("TestInputHandler: line "+line+", column="+column+", layer="+layer+", type="+type+", code="+code+", gelem="+gelem);
    }
}
