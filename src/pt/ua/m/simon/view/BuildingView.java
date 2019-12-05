package pt.ua.m.simon.view;

import pt.ua.gboard.GBoard;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;
import pt.ua.gboard.basic.Position;
import pt.ua.gboard.games.LabyrinthGelem;

import java.awt.*;

public class BuildingView {

    private final int numberOfLines;
    private final int[][] map;
    private final int numberOfColumns;
    protected static CharGelem[][] gelems = null;
    GBoard gBoard;
    private int gelemCellsSize;

    public BuildingView(String filename, int gelemCellsSize) {
        assert filename != null : "invalid maze (or file not read)";

        assert gelemCellsSize > 0 : "invalid gelem cell size: " + gelemCellsSize;

        char[] var5 = new char[0];

        int var6;
        int var7;

        this.gelemCellsSize = gelemCellsSize;
        this.numberOfLines = filename.length();
        var6 = filename.length();

        this.numberOfColumns = var6;
        this.map = new int[this.numberOfLines][this.numberOfColumns];

        int var10;
        for(var7 = 0; var7 < this.numberOfLines; ++var7) {
            for(var10 = 0; var10 < this.numberOfColumns; ++var10) {
                this.map[var7][var10] = -1;
            }
        }

        String windowName = "window";
        int numberOfLayers = 2;
        this.gBoard = new GBoard(windowName, this.numberOfLines * gelemCellsSize, this.numberOfColumns * gelemCellsSize, 25 / gelemCellsSize, 25 / gelemCellsSize, numberOfLayers);
        this.createGelems();

        for(var7 = 0; var7 < this.numberOfLines; ++var7) {
            for(var10 = 0; var10 < this.numberOfColumns; ++var10) {
                this.gBoard.draw(this.gelems[var7][var10], var7 * gelemCellsSize, var10 * gelemCellsSize, 0);
            }
        }
    }

    protected void createGelems() {
        if (gelems == null) {
            gelems = new CharGelem[this.numberOfColumns][this.numberOfLines];

            for(int var7 = 0; var7 < this.numberOfLines; ++var7) {
                for(int var10 = 0; var10 < this.numberOfColumns; ++var10) {

                    gelems[var7][var10] = new CharGelem('#', Color.BLACK);
                }
            }

        }

    }

}
