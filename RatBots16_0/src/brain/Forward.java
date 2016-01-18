package brain;

import actor.BotBrain;
import grid.Location;
import java.util.Random;

/**
 * Forward always goes forward the same direction as last time if possible.
 *
 * @author spockm
 */
public class Forward extends BotBrain {

    Random randy = new Random();
    int lastDirection = NORTH;

    public Forward() {
        setName("Forward");
    }

    @Override
    public int chooseAction() {
        if (!canMove(lastDirection)) {
            lastDirection = randy.nextInt(4) * 90;
        }

        return lastDirection;
    }

}
