package gui;

import actor.Block;
import actor.Prize;
import grid.Location;
import grid.RatBotsGrid;
import java.util.ArrayList;
import java.util.Random;
import world.RatBotWorld;
import world.World;

/**
 * The Arena class includes all of the methods needed to setup the arena 
 * according to the rules of the game.  
 * @author Spock
 */
public class RatBotsArena 
{
    /**
     * The size of a side of the central starting room in the arena. 
     */
    private Random randy = new Random();
    private boolean withBlocks = true;
    
    public static final int CHALLENGE_1 = 1; //1 material - no blocks
    public static final int CHALLENGE_2 = 2; //Super Points Mode! - no blocks

    public static final int NORMAL = 6;
    
    private int playMode = NORMAL; 
    public void setPlayMode(int in) 
    { 
        playMode = in;
        if(playMode < 3) withBlocks = false;
        else withBlocks = true;
    }
    public int getPlayMode()
    {
        return playMode;
    }
    
    /**
     * Toggles whether the grid will include Blocks or not.  
     * This is an option in the Arena menu.
     */
    public void toggleShowBlocks(World world) 
    { 
        withBlocks = ! withBlocks; 
    }

    /**
     * Initializes the Arena based on the selected rules.  
     * @param world the world that the Arena is within
     */
    public void initializeArena(World world)
    {
        if(withBlocks)
            addStandardBlocks(world);
        addRandomSuperPrize(world);
    }
    
    private void addStandardBlocks(World world)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        int centerRow = rows/2; 
        int centerCol = cols/2;

        //One Block in the center
        new Block(1000).putSelfInGrid(grid, new Location(centerRow,centerCol));
        
        //Containers
        new Block(1000).putSelfInGrid(grid, new Location(3,centerCol));
        new Block(1000).putSelfInGrid(grid, new Location(rows-4,centerCol));
        new Block(1000).putSelfInGrid(grid, new Location(centerRow,3));
        new Block(1000).putSelfInGrid(grid, new Location(centerRow,cols-4));
        for(int off=0; off<4;off++)
        {
            //Top
            new Block(1000).putSelfInGrid(grid, new Location(off,centerCol-2));
            new Block(1000).putSelfInGrid(grid, new Location(off,centerCol+2));
            //Bottom
            new Block(1000).putSelfInGrid(grid, new Location(rows-off-1,centerCol-2));
            new Block(1000).putSelfInGrid(grid, new Location(rows-off-1,centerCol+2));
            //Left
            new Block(1000).putSelfInGrid(grid, new Location(centerRow-2,off));
            new Block(1000).putSelfInGrid(grid, new Location(centerRow+2,off));
            //Right
            new Block(1000).putSelfInGrid(grid, new Location(centerRow-2,cols-off-1));
            new Block(1000).putSelfInGrid(grid, new Location(centerRow+2,cols-off-1));
        }
    }
    
    private void addRandomSuperPrize(World world)
    {
        RatBotWorld rbw = (RatBotWorld)world;
        ArrayList<Location> places = rbw.getSuperPrizeLocations();
        int choice = randy.nextInt(places.size());
        Prize awesome = new Prize(Prize.SUPER_PRIZE_TYPE,Prize.SUPER_PRIZE_VALUE);
        awesome.putSelfInGrid(world.getGrid(), places.get(choice));
        
    }
    
}
