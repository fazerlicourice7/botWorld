/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.BotBrain;
import actor.GameObject;
import actor.Bot;
import actor.Prize;
import grid.Location;
import java.util.Random;
import java.awt.Color;

/**
 *
 * @author 17noort
 */
public class BestBot_NoDart extends BotBrain {

    int direction = 0;
    GameObject[][] arena;
    Bot b;
    Location distance;
    static int row;
    static int column;
    int i;
    int j;
    int[][] timesBeenThere = new int[21][21];
    Random randy = new Random();
    Location next = getLocation().getAdjacentLocation(direction);
    int lowestDistance = 100000;

    public BestBot_NoDart() {
        setName("=^.^=");
        //(\_/)
        //

    }

    @Override
    public int chooseAction() {
        // int x = col2, int y = row2.
        // for getArena --- int x = 0, int y = 0, x < 20, y < 20, X++, y ++
        float hue = (float) (getMoveNumber()) / 50;
        //It will go through all the colors in 250 turns
        setPreferredColor(Color.getHSBColor(hue, 1f, 1f));
        timesBeenThere[getRow()][getCol()]++;
        arena = getArena();
        row = getRow();
        column = getCol();

        Location prize = getBestPrizeLocation();
        System.out.println(prize);
        direction = chooseBestDirectiontoLocation(prize);
        return direction;
    }

    @Override
    public void initForRound() {
        //Each new round we should reset the counters.  
        for (int row2 = 0; row2 < 21; row2++) {
            for (int col = 0; col < 21; col++) {
                timesBeenThere[row2][col] = 0;
            }
        }
    }

    /**
     * calculates the distance from a block in a certain direction to
     * goalLocation
     *
     * @param goal
     * @param dir
     * @return
     */
    public double distanceTo(Location goal, int dir) {
        double dx = 0;
        double dy = 0;
        if (dir == 0) {
            dx = Math.abs((row - 1) - goal.getRow());
            dy = Math.abs(column - goal.getCol());
        }
        if (dir == 90) {
            dx = Math.abs(row - goal.getRow());
            dy = Math.abs((column + 1) - goal.getCol());
        }
        if (dir == 180) {
            dx = Math.abs((row + 1) - goal.getRow());
            dy = Math.abs(column - goal.getCol());
        }
        if (dir == 270) {
            dx = Math.abs(row - goal.getRow());
            dy = Math.abs((column - 1) - goal.getCol());
        }
        return dx + dy;
    }

    /**
     * basic pathfinding using score system to return bestDirection to location
     *
     * @param goal
     * @return
     */
    public int chooseBestDirectiontoLocation(Location goal) {
        int lowestScore = 1000;
        int bestDir = 0;
        for (int dir = 0; dir < 360; dir += 90) { //loops through all possible directions
            int score = 0;
            if(isThereGoldPrize()){
            if(dir == 0 && row == 16 && column == 10){
              score += 5;  
            }
            if(dir == 180 && row == 4 && column == 10){
              score += 5;  
            }
            if(dir == 90 && row == 10 && column == 4){
              score += 5;  
            }
            if(dir == 270 && row == 10 && column == 16){
              score += 5;  
            }
            }
            if (!canMove(dir)) { //if cant move in direction - score +1000
                score += 1000;
            }
            if (ifAdjacentToWalls(dir) > 0) {
                //System.out.println("ifAdjacentToWalls: " + ifAdjacentToWalls(dir) + ", towards: " + dir + " CurrentLocation: " + row + ", " + column);
            }
            if (ifInWalls(dir) > 0) {
                //System.out.println("ifInWalls: " + ifInWalls(dir) + ", towards: " + dir + " CurrentLocation: " + row + ", " + column);
            }
            score += ifAdjacentToWalls(dir);
            score += ifInWalls(dir); //score increases if inside walled locations
            score += getTimesBeenThere(dir); //score increases depending on how manyTimesBeen to set direction
            score += distanceTo(goal, dir); //score adds distance from block in set dir to goal location
            
            //ystem.out.println("Score in Direction " + dir + ": " + score + " CurrentLocation: " + row + ", " + column);
            
            if (score < lowestScore) { //if score lower than lowestScore, current direction becomes the best
                bestDir = dir;
                lowestScore = score;
            }
        }
        
        return bestDir;
    }

    /**
     * method helps bot to more easily maneuver around walls to get to gold
     * prize. If bot not inRange() (the center cross of grid), bot should get
     * "inRange" and then go for gold prize
     *
     * @param goal
     * @return
     */
    public Location areaForGold(Location goal) {
        arena = getArena();
        Location outsideGold;
        int row2 = goal.getRow();
        int col2 = goal.getCol();
        if (!inRange()) {
          //  System.out.println("NOT in Range for Gold. CurrentLocation: " + row + ", " + column);
        }
        //(row2 == 1 && col2 == 10) && (row <= 3 && row >= 0) && ((column <= 20 && column >= 7) || (column <= 7) && column >= 0)
        if (row2 == 1 && col2 == 10 && !inRange()) {
            outsideGold = new Location(6, 10);
        } else if (row2 == 19 && col2 == 10 && !inRange()) {
            outsideGold = new Location(14, 10);
        } else if (row2 == 10 && col2 == 1 && !inRange()) {
            outsideGold = new Location(10, 6);
        } else if (row2 == 10 && col2 == 19 && !inRange()) {
            outsideGold = new Location(10, 14);
        } else {
            outsideGold = goal;
        }
        return outsideGold;
    }

    public Location areaForOtherPrizes(Location goal) {
        arena = getArena();
        Location outsidePrize;
        int row2 = goal.getRow();
        int col2 = goal.getCol();
        //(row2 == 1 && col2 == 10) && (row <= 3 && row >= 0) && ((column <= 20 && column >= 7) || (column <= 7) && column >= 0)
        if (row2 <= 7 && row2 >= 0 && col2 <= 7 && col2 >= 0 && !inRange2()) {
            outsidePrize = new Location(4, 4);
        } else if (row2 <= 7 && row2 >= 0 && col2 <= 20 && col2 >= 13 && !inRange1()) {
            outsidePrize = new Location(4, 16);
        } else if (row2 <= 20 && row2 >= 13 && col2 <= 7 && col2 >= 0 && !inRange3()) {
            outsidePrize = new Location(16, 4);
        } else if (row2 <= 20 && row2 >= 13 && col2 <= 20 && col2 >= 13 && !inRange4()) {
            outsidePrize = new Location(16, 16);
        } else {
            outsidePrize = goal;
        }
        return outsidePrize;
    }

    /**
     * finds best possible location for prize prioritizes highest prize values,
     * and then finds closest one using getClosestPrize method
     *
     * @return
     */
    public Location getBestPrizeLocation() {
        arena = getArena();

        Location bestPrizeLocation = new Location(10, 10);
        int bestPrizeValue = 0;

        for (int row2 = 0; row2 < 21; row2++) {
            for (int col = 0; col < 21; col++) {
                if (arena[row2][col] instanceof Prize) {
                    Prize p = (Prize) arena[row2][col];
                    int value = p.getValue();
                    if (value > bestPrizeValue) {
                        bestPrizeValue = value;
                        bestPrizeLocation = getClosestPrize(value);
                        if (bestPrizeValue == 500) {
                            bestPrizeLocation = areaForGold(bestPrizeLocation);
                        } else {
                            bestPrizeLocation = areaForOtherPrizes(bestPrizeLocation);
                        }
                    }
                }
            }
        }

        //System.out.println(bestPrizeLocation);
        return bestPrizeLocation;
    }

    /**
     * returns number of times bot has gone to a certain grid location in set
     * direction
     *
     * @param dir
     * @return
     */
    public int getTimesBeenThere(int dir) {
        int times = 0;
        if (canMove(dir)) {
            if (dir == 0) {
                times = timesBeenThere[getRow() - 1][getCol()];

            }
            if (dir == 90) {
                times = timesBeenThere[getRow()][getCol() + 1];
            }
            if (dir == 180) {
                times = timesBeenThere[getRow() + 1][getCol()];
            }
            if (dir == 270) {
                times = timesBeenThere[getRow()][getCol() - 1];
            }

            return times;
        }
        return 1000; // can't move there. 
    }

    public int makeBlockInCorner() {
        Location corner1 = new Location(20, 19);
        chooseBestDirectiontoLocation(corner1);
        while (canMove(WEST)) {
            direction = MOVE_WEST;
            direction = BLOCK_EAST;
        }

        return direction;
    }

    /**
     * gets the location of closestPrize for largest prize value seen in grid
     *
     * @param value
     * @return
     */
    public Location getClosestPrize(int value) {
        arena = getArena();
        int lowestScore = 1000;
        Location closestPrize = new Location(10, 10);
        for (int row2 = 0; row2 < 21; row2++) {
            for (int col = 0; col < 21; col++) {
                int score = 0;

                if (arena[row2][col] instanceof Prize) {
                    Prize p = (Prize) arena[row2][col];
                    if (p.getValue() == value) {
                        score += Math.abs(row - row2);
                        score += Math.abs(column - col);
                        if (score < lowestScore) {
                            lowestScore = score;
                            closestPrize = new Location(row2, col);
                        }
                    }
                }
            }
        }
        return closestPrize;

    }

    /**
     * returns true if bot is in "cross" grid area with walls expanding from the
     * center
     *
     * @return
     */
    public boolean inRange() {
        boolean result;
        if ((row >= 8 && row <= 12 && column >= 0 && column <= 20) || (row >= 0 && row <= 20 && column >= 8 && column <= 12)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean inRange1() {
        boolean result;
        if ((row >= 0 && row <= 7 && column >= 13 && column <= 20)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean inRange2() {
        boolean result;
        if ((row >= 0 && row <= 7 && column >= 0 && column <= 7)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean inRange3() {
        boolean result;
        if ((row >= 13 && row <= 20 && column >= 0 && column <= 7)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean inRange4() {
        boolean result;
        if ((row >= 13 && row <= 20 && column >= 13 && column <= 20)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * returns true if there are gold prizes in grid
     *
     * @return
     */
    public boolean isThereGoldPrize() {
        boolean result;
        arena = getArena();
        if (arena[1][10] instanceof Prize || arena[10][1] instanceof Prize || arena[10][19] instanceof Prize || arena[19][10] instanceof Prize) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    public boolean isTherePrize() {
        boolean result;
        arena = getArena();
        if (arena[1][10] instanceof Prize || arena[10][1] instanceof Prize || arena[10][19] instanceof Prize || arena[19][10] instanceof Prize || arena[3][9] instanceof Prize || arena[3][11] instanceof Prize || arena[9][3] instanceof Prize || arena[11][3] instanceof Prize || arena[9][17] instanceof Prize || arena[11][17] instanceof Prize || arena[17][9] instanceof Prize || arena[17][11] instanceof Prize) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean isTherePrize2() {
        boolean result;
        arena = getArena();
        int row2;
        int col2;
        result = false;
        for (row2 = 0; row2 <= 3; row2++) {
            for (col2 = 0; col2 <= 3; col2++) {
                if (arena[row2][7] instanceof Prize || arena[row2][13] instanceof Prize || arena[7][col2] instanceof Prize || arena[13][col2] instanceof Prize) {
                    result = true;
                }
            }
        }
        for (row2 = 17; row2 <= 20; row2++) {
            for (col2 = 17; col2 <= 20; col2++) {
                if (arena[row2][7] instanceof Prize || arena[row2][13] instanceof Prize || arena[7][col2] instanceof Prize || arena[13][col2] instanceof Prize) {
                    result = true;
                }
            }
        }
        return result;
    }

    public int ifAdjacentToWalls(int dir) {
        int times = 0;
        arena = getArena();
        if (!isTherePrize2()) {
            if (dir == 0) {
                if ((row - 1) == 13 && ((column >= 0 && column <= 3) || (column >= 17 && column <= 20))) {
                    times += 100;
                }
                if ((row - 1) == 3 && (column == 7 || column == 13)) {
                    times += 100;
                }
            }
            if (dir == 180) {
                if ((row + 1) == 7 && ((column >= 0 && column <= 3) || (column >= 17 && column <= 20))) {
                    times += 100;
                }
                if ((row + 1) == 17 && (column == 7 || column == 13)) {
                    times += 100;
                }
            }
            if (dir == 90) {
                if ((column + 1) == 7 && ((row >= 0 && row <= 3) || (row >= 17 && row <= 20))) {
                    times += 100;
                }
                if ((column + 1) == 17 && (row == 7 || row == 13)) {
                    times += 100;
                }
            }
            if (dir == 270) {
                if ((column - 1) == 13 && ((row >= 0 && row <= 3) || (row >= 17 && row <= 20))) {
                    times += 100;
                }
                if ((column - 1) == 3 && (row == 7 || row == 13)) {
                    times += 100;
                }
            }
        }

        return times;
    }

    /**
     * ifInWalls() ensures if any adjacent direction looked at is in the walled
     * off location where gold prizes are, the score gets increased. Increase in
     * score depends on how "deep inside" bot will be in walls. Deeper areas
     * having higher scores result in bot always coming out more quickly.
     *
     * @param dir - the adjacent direction being looked at
     * @return
     */
    public int ifInWalls(int dir) {
        int score = 0;
        arena = getArena();
        if (!isTherePrize()) { //method only takes places when there is no prizes inside walls, making it accessible only then
            if (dir == 0) { //goes through all possible wall locations when direction is NORTH
                if (((row - 1) == 0 && column >= 9 && column <= 11)) {
                    score += 400;
                }
                if (((row - 1) == 1 && column >= 9 && column <= 11)) {
                    score += 300;
                }
                if (((row - 1) == 2 && column >= 9 && column <= 11)) {
                    score += 200;
                }
                if (((row - 1) == 3 && column >= 9 && column <= 11)) {
                    score += 100;
                }
                if (((row - 1) == 19 && column >= 9 && column <= 11)) {
                    score += 300;
                }
                if (((row - 1) == 18 && column >= 9 && column <= 11)) {
                    score += 200;
                }
                if (((row - 1) == 17 && column >= 9 && column <= 11)) {
                    score += 100;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 20)) {
                    score += 400;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 19)) {
                    score += 300;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 18)) {
                    score += 200;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 17)) {
                    score += 100;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 0)) {
                    score += 400;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 1)) {
                    score += 300;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 2)) {
                    score += 200;
                }
                if ((((row - 1) == 9 || (row - 1) == 10) && column == 3)) {
                    score += 100;
                }
            }
            if (dir == 180) { //goes through all possible wall locations when direction is SOUTH
                if (((row + 1) == 1 && column >= 9 && column <= 11)) {
                    score += 300;
                }
                if (((row + 1) == 2 && column >= 9 && column <= 11)) {
                    score += 200;
                }
                if (((row + 1) == 3 && column >= 9 && column <= 11)) {
                    score += 100;
                }
                if (((row + 1) == 20 && column >= 9 && column <= 11)) {
                    score += 400;
                }
                if (((row + 1) == 19 && column >= 9 && column <= 11)) {
                    score += 300;
                }
                if (((row + 1) == 18 && column >= 9 && column <= 11)) {
                    score += 200;
                }
                if (((row + 1) == 17 && column >= 9 && column <= 11)) {
                    score += 100;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 20)) {
                    score += 400;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 19)) {
                    score += 300;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 18)) {
                    score += 200;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 17)) {
                    score += 100;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 0)) {
                    score += 400;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 1)) {
                    score += 300;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 2)) {
                    score += 200;
                }
                if ((((row + 1) == 10 || (row + 1) == 11) && column == 3)) {
                    score += 100;
                }

            }

            if (dir == 90) { //goes through all possible wall locations when direction is EAST
                if (((column + 1) == 1 && row >= 9 && row <= 11)) {
                    score += 300;
                }
                if (((column + 1) == 2 && row >= 9 && row <= 11)) {
                    score += 200;
                }
                if (((column + 1) == 3 && row >= 9 && row <= 11)) {
                    score += 100;
                }
                if (((column + 1) == 20 && row >= 9 && row <= 11)) {
                    score += 400;
                }
                if (((column + 1) == 19 && row >= 9 && row <= 11)) {
                    score += 300;
                }
                if (((column + 1) == 18 && row >= 9 && row <= 11)) {
                    score += 200;
                }
                if (((column + 1) == 17 && row >= 9 && row <= 11)) {
                    score += 100;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 20)) {
                    score += 400;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 19)) {
                    score += 300;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 18)) {
                    score += 200;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 17)) {
                    score += 100;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 0)) {
                    score += 400;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 1)) {
                    score += 300;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 2)) {
                    score += 200;
                }
                if ((((column + 1) == 10 || (column + 1) == 11) && row == 3)) {
                    score += 100;
                }

            }

            if (dir == 270) { //goes through all possible wall locations when direction is WEST
                if (((column - 1) == 0 && row >= 9 && row <= 11)) {
                    score += 400;
                }
                if (((column - 1) == 1 && row >= 9 && row <= 11)) {
                    score += 300;
                }
                if (((column - 1) == 2 && row >= 9 && row <= 11)) {
                    score += 200;
                }
                if (((column - 1) == 3 && row >= 9 && row <= 11)) {
                    score += 100;
                }
                if (((column - 1) == 19 && row >= 9 && row <= 11)) {
                    score += 300;
                }
                if (((column - 1) == 18 && row >= 9 && row <= 11)) {
                    score += 200;
                }
                if (((column - 1) == 17 && row >= 9 && row <= 11)) {
                    score += 100;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 20)) {
                    score += 400;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 19)) {
                    score += 300;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 18)) {
                    score += 200;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 17)) {
                    score += 100;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 0)) {
                    score += 400;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 1)) {
                    score += 300;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 2)) {
                    score += 200;
                }
                if ((((column - 1) == 9 || (column - 1) == 10) && row == 3)) {
                    score += 100;
                }
            }
        }

        return score;
    }

    // 
}
