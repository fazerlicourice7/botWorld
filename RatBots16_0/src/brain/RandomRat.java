package brain;

import actor.BotBrain;
import java.util.Random;
/**
 * @author Spock
 * RandomRat chooses a random move each turn.
 */
public class RandomRat extends BotBrain
{
    Random randy = new Random();
    
    public RandomRat()
    {
        setName("RandomRat");
    }
    
    @Override
    public int chooseAction()
    {        
        int direction = randy.nextInt(4)*90;
        int extra = randy.nextInt(30);
        if(extra==9) 
            direction += 1000; //DARTING!
        else if (extra == 1)
            direction += 2000; //BUILD BLOCK
        else if (extra == 2)
            direction += 3000; //BUILD BLOCK WALL
        return direction;
    }
    
}
