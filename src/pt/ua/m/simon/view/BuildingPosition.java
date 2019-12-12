package pt.ua.m.simon.view;

import pt.ua.gboard.basic.Position;

import java.util.logging.Logger;

// Modified version of Position.class in Gboard library
public class BuildingPosition  {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.view.BuildingPosition");

    // TODO that's not good!
    public int getFloor(){
        return (ElevatorGBoard.NUM_LINES - line) / ElevatorGBoard.FLOOR_DIST;
    }
    public void setFloor(int floor) { this.line = ElevatorGBoard.NUM_LINES - floor*ElevatorGBoard.FLOOR_DIST;}
    public int[] getInt() { return new int[]{line,column};}

//    public void setLine(int line){ this.line = line;logger.info("setting line: " + this.line);
//    }
    public void setColumn(int column){ this.column = column;logger.info("setting column: " + this.column);}
    public void setLayer(int layer){ this.layer = layer;}


    /**
     * Constructs a new Position (line x column).
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     */
    //@ requires line >= 0 && column >= 0;
    public BuildingPosition(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        this.line = line;
        this.column = column;
        this.layer = 0;
    }

    /**
     * Line.
     *
     * @return {@code int} line position
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Column.
     *
     * @return {@code int} column position
     */
    public int getColumn()
    {
        return column;
    }


    public int getLayer()
    {
        return layer;
    }

    /**
     * Is equal?
     *
     *  <P><B>requires</B>: {@code other != null}
     *
     * @param other  other position
     * @return {@code boolean} true if equal
     */
    //@ requires other != null;
    public boolean isEqual(Position other)
    {
        assert other != null;

        return isEqual(other.line(), other.column());
    }

    /**
     * Is equal?
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     * @return {@code boolean} true if equal
     */
    //@ requires line >= 0 && column >= 0;
    public boolean isEqual(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        return this.getLine() == line && this.getColumn() == column;
    }

    /**
     * {@code this.line() > other.line() && this.column() >= other.column() ||}
     * {@code this.line() >= other.line() && this.column() > other.column()}
     *
     *  <P><B>requires</B>: {@code other != null}
     *
     * @param other  other position
     * @return {@code boolean} true if greater than
     */
    //@ requires other != null;
    public boolean isGreater(Position other)
    {
        assert other != null;

        return isGreater(other.line(), other.column());
    }

    /**
     * {@code this.line() > line && this.column() >= column ||}
     * {@code this.line() >= line && this.column() > column}
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     * @return {@code boolean} true if greater than
     */
    //@ requires line >= 0 && column >= 0;
    public boolean isGreater(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        return this.getLine() > line && this.getColumn() >= column ||
                this.getLine() >= line && this.getColumn() > column;
    }

    /**
     * {@code this.line() < other.line() && this.column() <= other.column() ||}
     * {@code this.line() <= other.line() && this.column() < other.column()}
     *
     *  <P><B>requires</B>: {@code other != null}
     *
     * @param other  other position
     * @return {@code boolean} true if lower than
     */
    //@ requires other != null;
    public boolean isLower(Position other)
    {
        assert other != null;

        return isLower(other.line(), other.column());
    }

    /**
     * {@code this.line() < line && this.column() <= column ||}
     * {@code this.line() <= line && this.column() < column}
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     * @return {@code boolean} true if lower than
     */
    //@ requires line >= 0 && column >= 0;
    public boolean isLower(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        return this.getLine() < line && this.getColumn() <= column ||
                this.getLine() <= line && this.getColumn() < column;
    }

    /**
     * {@code this.line() >= other.line() && this.column() >= other.column() ||}
     * {@code this.line() >= other.line() && this.column() >= other.column()}
     *
     *  <P><B>requires</B>: {@code other != null}
     *
     * @param other  other position
     * @return {@code boolean} true if greater than
     */
    //@ requires other != null;
    public boolean isGreaterOrEqual(Position other)
    {
        assert other != null;

        return isGreaterOrEqual(other.line(), other.column());
    }

    /**
     * {@code this.line() >= line && this.column() >= column ||}
     * {@code this.line() >= line && this.column() >= column}
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     * @return {@code boolean} true if greater than
     */
    //@ requires line >= 0 && column >= 0;
    public boolean isGreaterOrEqual(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        return this.getLine() >= line && this.getColumn() >= column ||
                this.getLine() >= line && this.getColumn() >= column;
    }

    /**
     * {@code this.line() <= other.line() && this.column() <= other.column() ||}
     * {@code this.line() <= other.line() && this.column() <= other.column()}
     *
     *  <P><B>requires</B>: {@code other != null}
     *
     * @param other  other position
     * @return {@code boolean} true if lower than
     */
    //@ requires other != null;
    public boolean isLowerOrEqual(Position other)
    {
        assert other != null;

        return isLowerOrEqual(other.line(), other.column());
    }

    /**
     * {@code this.line() <= line && this.column() <= column ||}
     * {@code this.line() <= line && this.column() <= column}
     *
     *  <P><B>requires</B>: {@code line >= 0 && column >= 0}
     *
     * @param line  line position
     * @param column  column position
     * @return {@code boolean} true if lower than
     */
    //@ requires line >= 0 && column >= 0;
    public boolean isLowerOrEqual(int line, int column)
    {
        assert line >= 0: "invalid line: "+line;
        assert column >= 0: "invalid column: "+column;

        return this.getLine() <= line && this.getColumn() <= column ||
                this.getLine() <= line && this.getColumn() <= column;
    }

    @Override
    public String toString()
    {
        return "("+line+","+column+")";
    }

    protected int line;
    protected int column;
    protected int layer;
}