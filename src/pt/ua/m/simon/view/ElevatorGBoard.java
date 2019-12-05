package pt.ua.m.simon.view;

import pt.ua.gboard.GBoard;
import pt.ua.gboard.GBoardInputHandler;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.PacmanGelem;
import pt.ua.gboard.shapes.ShapeGelem;
import pt.ua.gboard.shapes.Shapes;
import pt.ua.m.simon.Person;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.LinkedList;

import static java.lang.System.out;

public class ElevatorGBoard {

    protected static final GBoard board = new GBoard("Test", 8, 8, 60, 60, 2);

    private Gelem user;
    private int[] userPosition; //r,c
    private Gelem elevator;
    private int elevatorColumn = 2; //r,c

    private LinkedList<Person> users;

    public ElevatorGBoard(LinkedList users) {
        this.users = users;
        setupBoard();
    }

    // TODO setup UI based on input (number of floors, elevators, etc.)
    private void setupBoard()
    {
        board.pushInputHandler(new InputHandler());
        JSlider slider = new JSlider(2,120,60);
        slider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                int size = ((JSlider)e.getSource()).getValue();
                board.frame().setSize(size*8, size*8);
            }
        });
        board.contentPane().add(slider, BorderLayout.NORTH);

        board.frame().pack();

        Gelem floor = new FilledGelem(Color.black, 90, 1, 8 ); // transparency?, 1 row, 8 columns
        Gelem elevatorCase = new FilledGelem(Color.orange, 90, 8, 1);
        //TODO animate moving animator
        elevator = new StripedGelem(Color.blue,4,1.0,1.0,false);
//        elevator = new FilledGelem(Color.orange, 90, 8, 1);

        user = new CharGelem('C', Color.red);

        board.draw(elevatorCase, 0, elevatorColumn, 0);

        // TODO: create converter function - bottom floor = high row value, etc.
        // ground floor
        board.draw(floor, 7,0,0);//row,column,layer
        board.draw(new StringGelem("Ground", Color.green,1,8), 7,0,0);//row,column,layer
        // third floor
        board.draw(floor, 4,0,0);//row,column,layer
        board.draw(new StringGelem("Third Floor", Color.green, 1, 8), 4,0,0);//row,column,layer
        // fifth floor
        board.draw(floor, 2,0,0);//row,column,layer
        board.draw(new StringGelem("Fifth Floor", Color.green, 1, 8), 2,0,0);//row,column,layer


        for(Person u : users){
            board.draw(u.getGelem(),u.getLine(),u.getColumn(),1);
        }
    }


    // TODO use one interface for person & elevator
    public void moveToElevator(Person p){
        while (p.getColumn() < elevatorColumn){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            moveRight(p);
        }
    }

    public void moveToEnd(Person p){
        while(p.getColumn() < board.numberOfColumns() - 1){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            moveRight(p);
        }
    }


    private void moveRight(Person p){
        if(p.getColumn() < board.numberOfColumns() - 1){
            int c = p.getColumn(), l = p.getLine();
            int new_c = (c+1);
            board.move(p.getGelem(),l,c,l,new_c);
            p.setColumn(new_c);
        }
    }

    public void moveUp(Person p){
        int l = p.getLine();
        if(l > 0){
            int c = p.getColumn();
            int new_l = (l-1);
            board.move(p.getGelem(),l,c,new_l,c);
            p.setLine(new_l);
        }
    }

    void mainGameLoop(){
        // go right until user.intersects(elevator)

        // call Elevator Actor ...

        // update position

        // go right again until hit back wall. finished.

        // Erase board and re-draw
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
