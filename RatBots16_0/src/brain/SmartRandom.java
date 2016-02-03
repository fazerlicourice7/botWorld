package brain;

import actor.BotBrain;
import grid.Location;
import java.util.Random;

/**
 * @author Spock SmartRandom chooses a random move each turn. However, if the
 * move it has chosen is not possible, it chooses another.
 */
public class SmartRandom extends BotBrain {

    Random randy = new Random();

    public SmartRandom() {
        setName("SmartRandom");
    }

    @Override
    public int chooseAction() {
        int direction = randy.nextInt(4) * 90; //Choose a random move.

        
        /*if (randy.nextInt(15) == 9) {
            direction += 1000; //DARTING! (occasionally)
        }*/
        if (getMoveNumber() % 2 == 0) {
            direction = BLOCK_NORTH;
        } else {
            direction = MOVE_NORTH;
        }
        int loopCount = 0;
        //need this loopCounter so that we never end up in an infinite loop!
        while (!canMove(direction) && loopCount < 10) {
            //Choose a new direction if I can't move the way I've chosen. 
            loopCount++;
            direction = randy.nextInt(4) * 90;
        }
       if(!canMove(MOVE_NORTH)){
            System.out.println("can't move north");
        } else if(!canMove(MOVE_EAST)){
            System.out.println("can't move east");
        }else if(!canMove(DART_WEST)){
            System.out.println("can't move west");
        }else if(!canMove(MOVE_SOUTH)){
            System.out.println("can't move south");
        }

        return direction;
    }

}
