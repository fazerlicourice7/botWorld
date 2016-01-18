package grid;

/* A RatBotsGrid is adapted from the BoundedGrid class. 
 * 
 * The code for this class is adapted from the BoundedGrid class in the 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 * @author Alyce Brady
 * @author APCS Development Committee
 * @author Cay Horstmann
 * 
 * adapted by Spock
 */
//TODO: fix emptyLocations to ignore Tails!
//TODO: Make a removeAllActors method


import actor.Prize;
import actor.Bot;
import actor.GameObject;
import actor.Tail;
import java.util.ArrayList;
import java.util.Random;

/**
 * A <code>RatBotsGrid</code> is a rectangular grid with a finite number of
 * rows and columns. <br />
 * It contains objects both on the grid as well as 'off-grid' objects.  
 * @param <E> 
 */
public class RatBotsGrid<E> extends AbstractGrid<E>
{
    private Object[][] occupantArray; // the array storing the grid elements
    private boolean[][][] walls;
    private String message;

    /**
     * Constructs an empty bounded grid with the given dimensions.
     * (Precondition: <code>rows > 0</code> and <code>cols > 0</code>.)
     * @param rows number of rows in RatBotsGrid
     * @param cols number of columns in RatBotsGrid
     */
    public RatBotsGrid(int rows, int cols)
    {
        if (rows <= 0)
            throw new IllegalArgumentException("rows <= 0");
        if (cols <= 0)
            throw new IllegalArgumentException("cols <= 0");
        occupantArray = new Object[rows][cols];
        walls = new boolean[rows][cols][4];
        //TODO: set all walls to false (this is happening by default.)
    }
    
    public boolean isWall(Location loc, int dir)
    {       
        if(isValid(loc) && dir >= 0)
        {
            return walls[loc.getRow()][loc.getCol()][(dir%360)/90];
        }
        return false;
    }
    public void setWall(Location loc, int dir)
    {
//        System.out.println("Attempting to setWall at "+loc);
        if(isValid(loc) && dir >= 0)
        {
            walls[loc.getRow()][loc.getCol()][(dir%360)/90] = true;
//            System.out.println("setWall at "+loc);
        }
    }
    public void removeWall(Location loc, int dir)
    {
        if(isValid(loc) && dir >= 0)
        {
            walls[loc.getRow()][loc.getCol()][(dir%360)/90] = false;
        }
    }
    public void removeAllWallsAroundSpace(Location loc)
    {
        if(isValid(loc))
            for(int d = 0; d < 4; d++)
                walls[loc.getRow()][loc.getCol()][d] = false;
    }
    public void addWallPair(Location loc, int d)
    {
        setWall(loc, d);
        Location border = loc.getAdjacentLocation(d);
        setWall(border, d+180);
    }
    
    public void removeWallPair(Location loc, int d)
    {
        removeWall(loc, d);
        Location border = loc.getAdjacentLocation(d);
        removeWall(border, d+180);
    }
    

    @Override
    public int getNumRows()
    {
        return occupantArray.length;
    }

    @Override
    public int getNumCols()
    {
        // Note: according to the constructor precondition, numRows() > 0, so
        // theGrid[0] is non-null.
        return occupantArray[0].length;
    }

    @Override
    public boolean isValid(Location loc)
    {
        return 0 <= loc.getRow() && loc.getRow() < getNumRows()
                && 0 <= loc.getCol() && loc.getCol() < getNumCols();
    }

    @Override
    public ArrayList<Location> getOccupiedLocations()
    {
        ArrayList<Location> theLocations = new ArrayList<Location>();

        // Look at all grid locations.
        for (int r = 0; r < getNumRows(); r++)
        {
            for (int c = 0; c < getNumCols(); c++)
            {
                // If there's an object at this location, put it in the array.
                Location loc = new Location(r, c);
                if (get(loc) != null) 
                    theLocations.add(loc);
            }
        }

        return theLocations;
    }

    @Override
    public E get(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
                
        return (E) occupantArray[loc.getRow()][loc.getCol()]; // unavoidable warning
    }

    @Override
    public E put(Location loc, E obj)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        if (obj == null)
            throw new NullPointerException("obj == null");

        // Add the object to the grid.
        E oldOccupant = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = obj;
        return oldOccupant;
    }

    @Override
    public E remove(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        
        // Remove the object from the grid.
        E r = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = null;
        return r;
    }
    /**
     * Gets all of the Rats that are in the Grid
     * @return an ArrayList filled with all Rats in this grid.
     */
    public ArrayList<Bot> getAllRats()
    {
        ArrayList<Bot> rats = new ArrayList<Bot>();
        
        ArrayList<Location> occupied = getOccupiedLocations();
        
        for(Location loc : occupied)
        {
            if(get(loc) instanceof Bot)
                rats.add((Bot)get(loc));
        }
        
        return rats;
    }
    
    
    
    public boolean isInCorner(Location loc)
    {
        return (loc.getCol() == 0 || loc.getCol() == getNumCols()-1) && 
                (loc.getRow() == 0 || loc.getRow() == getNumRows()-1);
    }
        
    private boolean messageFlag = false;
    public void setMessage(String in)
    {
        messageFlag = true; //turn this on when a new message is waiting.
        message = in;
    }
    public boolean isMessageWaiting()
    {
        return messageFlag;
    }
    public String getMessage()
    {
        messageFlag = false;
        return message;
    }
    
    
    //================================================
    private static Random randy = new Random();
    
    public Location getEmptyRandomPrizeLocation()
    {
        Location loc;
        int rows = this.getNumRows();
        int cols = this.getNumCols();
        
        int loopCount = 0;
        boolean endLoop;
        do
        {
            loopCount++;
            endLoop = true;
            loc = new Location(randy.nextInt(rows),randy.nextInt(cols));
            GameObject a = (GameObject)this.get(loc);
            if(isNearRat(loc) || isInRoom(loc) ) endLoop = false;
            if(!this.isValid(loc)) endLoop = false;
            if(!(a == null || a instanceof Tail)) endLoop = false;
        } while( !endLoop && loopCount<100);
        
        if(loopCount==100) return null; //no space found!
        
        return loc;
    }
    
    public boolean isNearRat(Location loc)
    {
        int PRIZE_PROXIMITY = 10;

        ArrayList<Bot> allRats = getAllRats();
        for(Bot r : allRats)
        {
            if(loc.distanceTo(r.getLocation()) < PRIZE_PROXIMITY)
                return true;
        }
        
        return false;
    }
    
    
    public ArrayList<Location> getSuperPrizeLocations()
    {
        int rows = this.getNumRows();
        int cols = this.getNumCols();
        int centerRow = rows/2; 
        int centerCol = cols/2;
        
        ArrayList<Location> prizeSpots = new ArrayList<Location>();
        prizeSpots.add(new Location(centerRow,1));
        prizeSpots.add(new Location(centerRow,cols-2));
        prizeSpots.add(new Location(1,centerCol));
        prizeSpots.add(new Location(rows-2,centerCol));
        
        return prizeSpots;
    }
    
    public Location getRandomEmptySuperPrizeLocation()
    {
        ArrayList<Location> prizeSpots = getSuperPrizeLocations();
        for(int q=0;q<10;q++)
        {
            Location loc = prizeSpots.get(randy.nextInt(prizeSpots.size()));
            if(this.get(loc) == null || this.get(loc) instanceof Tail)
                return loc;
        }
        return null;
    }
        
    public boolean isInRoom(Location loc)
    {
        for(Location prize : getSuperPrizeLocations())
        {
            if(loc.distanceTo(prize) <= 2)
                return true;
        }
        return false;
    }
        
    
}
