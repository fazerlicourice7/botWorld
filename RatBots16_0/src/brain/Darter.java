package brain;

import actor.BotBrain;
import java.util.Random;

/**
 * Darter just randomly darts in a random direction each move.  
 * (Not very effective.)  
 * @author spockm
 */
public class Darter extends BotBrain
{
    Random randy = new Random();
    
    public Darter()
    {
        setName("Darter");
    }
    
    @Override
    public int chooseAction()
    {        
        int direction = randy.nextInt(4)*90;
            direction += 1000; //DARTING!
        return direction;
    }
    
}