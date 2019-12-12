package pt.ua.m.simon.view;

import pt.ua.gboard.GBoard;
import pt.ua.gboard.GBoardInputHandler;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.*;
import pt.ua.m.simon.IObservable;
import pt.ua.m.simon.IObserver;
import pt.ua.m.simon.Person;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

// This is the View
public class ElevatorGBoard implements IObserver {

    protected static GBoard board;

    private int floors;
    private LinkedList<IObservable> observables;
    public static int NUM_COLS = 8;
    public static int NUM_LINES = 8;
    public static final int FLOOR_DIST = 2;

    public ElevatorGBoard(LinkedList<IObservable> observables, int floors) {
        this.floors = floors;
        NUM_LINES = floors*2;
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

        Gelem elevatorCase = new FilledGelem(Color.orange, 90, board.numberOfLines(), 1);
        board.draw(elevatorCase, 0, 2, 0);

        Gelem floor = new FilledGelem(Color.black, 90, 1, 8 ); // transparency?, 1 row, 8 columns
        for (int i = 0; i < floors; i++) {
            int line = board.numberOfLines() - 2*i -1;
            board.draw(floor, line,0,0);//row,column,layer
            String text = String.format("Floor %d",i);
            board.draw(new StringGelem(text, Color.green,1,8), line,0,0);//row,column,layer
        }

        for(IObservable o : observables){
            board.draw(o.getGelem(),o.getLine(),o.getColumn(),o.getLayer());
        }
    }


    @Override
    public void update(IObservable observable, BuildingPosition oldPosition) {
        board.move(observable.getGelem(),oldPosition.getLine(), oldPosition.getColumn(),observable.getLine(),observable.getColumn());
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
