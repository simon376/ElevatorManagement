package gboard;

import static java.lang.System.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;

public class TestGBoard
{
    static final public Scanner in = new Scanner(System.in);

    protected static final GBoard board = new GBoard("Test", 8, 8, 60, 60, 2);

    static public void main(String[] args)
    {
        board.pushInputHandler(new TestInputHandler());
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

      /*
      Image img = Toolkit.getDefaultToolkit().getImage("Guernica.jpg");
      board.contentPane().add(img, BorderLayout.SOUTH);
      */

        board.frame().pack();

        Gelem black = new FilledGelem(Color.black, 90);
        Gelem yellow = new FilledGelem(Color.yellow, 90);
        Gelem letters[] = new Gelem[26];
        for(int i = 0; i < letters.length; i++)
        {
            //out.println(""+(char)((int)'A'+i));
            letters[i] = new CharGelem((char)((int)'A'+i), Color.green);
        }
        Gelem bigLetter = new CharGelem('X', Color.blue, 3, 3);

        for(int l = 0; l < 8; l++)
            for(int c = 0; c < 8; c++)
                if (((l+c) % 2) == 0)
                    board.draw(black, l, c, 0);
                else
                    board.draw(yellow, l, c, 0);

        int l = 0;
        int c = 0;
        for(int i = 0; i < letters.length; i++)
        {
            //out.println("line = "+l+" column ="+c);
            board.draw(letters[i], l, c, 1);
            c++;
            if (c == 8)
            {
                l++;
                c = 0;
            }
        }

        Gelem stringGelem = new StringGelem("TEXT", Color.red, 2, 2);
        board.draw(stringGelem, 6, 6, 1);

        Gelem stringGelem2 = new StringGelem("TEXT", Color.green, 1, 1);
        board.draw(stringGelem2, 5, 7, 1);

        board.draw(bigLetter, 4, 4, 1);
        board.draw(letters[2], 5, 5, 1);

        Gelem bigBlue = new FilledGelem(Color.blue, 90, 2, 2);
        board.draw(bigBlue, 4, 0, 1);

        Gelem image = null;
        if (ImageGelem.isImage("Guernica.jpg"))
        {
            image = new ImageGelem("Guernica.jpg", board, 90, 4, 4);
            board.draw(image, 0, 0, 1);
        }

        board.draw(new StripedGelem(Color.red, 6, 5, 10, true), 7, 3, 1);
        board.draw(new RectangleGelem(Color.green, 50, true), 5, 3, 1);

        out.print("<Press RETURN>");
        in.nextLine();
        if (image != null)
        {
            board.erase(image, 0, 0, 1);
            out.print("<Press RETURN>");
            in.nextLine();
        }
        board.move(letters[0], 0, 0, 1, 3, 3, 1);
        board.erase(letters[2], 5, 5, 1);
      /*
      for(int l = 0; l < 8; l++)
         for(int c = 0; c < 8; c++)
            if (((l+c) % 2) == 0)
               ;
            else
            {
               board.erase(x, l, c, 0);
            }
      */
    }
}

class TestInputHandler extends GBoardInputHandler
{
    public TestInputHandler()
    {
        super(mousePressedMask | keyPressedMask);
    }

    public void run(GBoard board, int line, int column, int layer, int type, int code, Gelem gelem)
    {
        System.out.println("TestInputHandler: line "+line+", column="+column+", layer="+layer+", type="+type+", code="+code+", gelem="+gelem);
    }
}
