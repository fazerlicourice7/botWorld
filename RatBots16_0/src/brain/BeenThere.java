package brain;

import actor.BotBrain;
import grid.Location;
import java.util.Random;

/**
 * BeenThere keeps track of how many times it has been to each space 
 * and uses this information to go to newer places!
 * @author spockm
 */
public class BeenThere extends BotBrain
{
    int[][] timesBeenThere = new int[21][21];
    
    Random randy = new Random();
    
    public BeenThere()
    {
        setName("BeenThere");
    }
    
    @Override
    public int chooseAction()
    {        
        timesBeenThere[getRow()][getCol()]++;
        //Choose a random direction to move.  
        int direction = randy.nextInt(4)*90;
        Location next = getLocation().getAdjacentLocation(direction);
        if(next.isValidLocation())
        {   //If I've been there too many times, choose another random direction.
            if(timesBeenThere[next.getRow()][next.getCol()] > 5)
                direction = randy.nextInt(4)*90;
        }
        
        return direction;
    }

    @Override
    public void initForRound()
    {
        //Each new round we should reset the counters.  
        for(int row=0;row<21;row++)
            for(int col=0;col<21;col++)
            {
                timesBeenThere[row][col] = 0;
            }
    }
    
    
}
