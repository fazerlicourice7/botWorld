/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.Block;
import actor.BotBrain;
import actor.GameObject;
import actor.Prize;
import grid.Location;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author 18balanagav
 */
public class fazerLicourice extends BotBrain {

    //ArrayList<Integer> path = new ArrayList<>();
    Random rand;
    //int timesBeenThere[][] = new int[getArena().length][getArena()[0].length];

    /**
     *
     */
    public fazerLicourice() {
        rand = new Random();
        setName("fazerLicourice");
        setPreferredColor(java.awt.Color.MAGENTA);
    }

    @Override
    public int chooseAction() {
        GameObject[][] theArena = getArena();
        int selection;
        Location Bot = getLocation();
        Location prize = findPrize(Bot, theArena);
        System.out.println("Destination: " + prize.toString());
        selection = moveTo(prize, Bot, theArena);
        return selection;
    }

    /**
     *
     * @param directionChoice
     * @param myLoc
     * @return
     */
    public boolean canMove(int directionChoice, Location myLoc) {
        int direction = directionChoice % 1000;

        GameObject[][] theArena = getArena();
        //Location myLoc = new Location(getRow(),getCol());
        Location next = myLoc.getAdjacentLocation(direction);
        if (!next.isValidLocation()) {
            return false;
        }

        GameObject onNext = theArena[next.getRow()][next.getCol()];
        if (onNext == null || onNext instanceof Prize) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Chooses the direction to move given the destination
     *
     * @param xDestination x coordinate of the prize(where we want to be)
     * @param yDestination y coordinate of the prize(where we want to be)
     * @param xBot x coordinate of the bot
     * @param yBot y coordinate of the bot
     * @return
     */
    private int moveTo(Location Destination, Location Bot, GameObject[][] theArena) {
        boolean toEast, toWest, toNorth, toSouth;
        int verticalDist, horizontalDist, direction = -1;
        int destinationCol = Destination.getCol(), destinationRow = Destination.getRow();
        int botCol = Bot.getCol(), botRow = Bot.getRow();
        //timesBeenThere[Bot.getRow()][Bot.getCol()]++;
        if (destinationRow > botRow) {
            toSouth = true;
            toNorth = false;
        } else if (botRow > destinationRow) {
            toSouth = false;
            toNorth = true;
        } else {
            toSouth = false;
            toNorth = false;
        }
        if (destinationCol > botCol) {
            toEast = true;
            toWest = false;
        } else if (botCol > destinationCol) {
            toEast = false;
            toWest = true;
        } else {
            toEast = false;
            toWest = false;
        }
        boolean vertical = rand.nextBoolean();
        if (vertical || (!toWest && !toEast)) {
            System.out.println("Vertical: " + vertical + ", toWest: " + toWest + ", toEast: " + toEast);
            if (toNorth) {
                direction = MOVE_NORTH;
            } else if (toSouth) {
                direction = MOVE_SOUTH;
            }
        } else if (!vertical || (!toNorth && !toSouth)) {
            System.out.println("Horizontal: " + !vertical + ", toNorth: " + toNorth + ", toSouth: " + toSouth);
            if (toWest) {
                direction = MOVE_WEST;
            } else if (toEast) {
                direction = MOVE_EAST;
            }
        }
        /*if (!canMove(MOVE_NORTH, Bot)) {
            if (theArena[Bot.getAdjacentLocation(MOVE_NORTH).getRow()][Bot.getAdjacentLocation(MOVE_NORTH).getCol()] instanceof Block) {
                timesBeenThere[Bot.getAdjacentLocation(MOVE_NORTH).getRow()][Bot.getAdjacentLocation(MOVE_NORTH).getCol()] += 100;
            }
        }
        if (!canMove(MOVE_SOUTH, Bot)) {
            if (theArena[Bot.getAdjacentLocation(MOVE_SOUTH).getRow()][Bot.getAdjacentLocation(MOVE_SOUTH).getCol()] instanceof Block) {
                timesBeenThere[Bot.getAdjacentLocation(MOVE_SOUTH).getRow()][Bot.getAdjacentLocation(MOVE_SOUTH).getCol()] += 100;
            }
        }
        if (!canMove(MOVE_EAST, Bot)) {
            if (theArena[Bot.getAdjacentLocation(MOVE_EAST).getRow()][Bot.getAdjacentLocation(MOVE_EAST).getCol()] instanceof Block) {
                timesBeenThere[Bot.getAdjacentLocation(MOVE_EAST).getRow()][Bot.getAdjacentLocation(MOVE_EAST).getCol()] += 100;
            }
        }
        if (!canMove(MOVE_WEST, Bot)) {
            if (theArena[Bot.getAdjacentLocation(MOVE_WEST).getRow()][Bot.getAdjacentLocation(MOVE_WEST).getCol()] instanceof Block) {
                timesBeenThere[Bot.getAdjacentLocation(MOVE_WEST).getRow()][Bot.getAdjacentLocation(MOVE_WEST).getCol()] += 100;
            }
        }
        int timesBeenNorth = timesBeenThere[Bot.getAdjacentLocation(MOVE_NORTH).getRow()][Bot.getAdjacentLocation(MOVE_NORTH).getCol()],
                timesBeenSouth = timesBeenThere[Bot.getAdjacentLocation(MOVE_SOUTH).getRow()][Bot.getAdjacentLocation(MOVE_SOUTH).getCol()],
                timesBeenEast = timesBeenThere[Bot.getAdjacentLocation(MOVE_EAST).getRow()][Bot.getAdjacentLocation(MOVE_EAST).getCol()],
                timesBeenWest = timesBeenThere[Bot.getAdjacentLocation(MOVE_WEST).getRow()][Bot.getAdjacentLocation(MOVE_WEST).getCol()];
        if (timesBeenNorth == Math.min(timesBeenWest, Math.min(timesBeenEast, Math.min(timesBeenSouth, timesBeenNorth)))) {
            direction = MOVE_NORTH;
        } else if (timesBeenSouth == Math.min(timesBeenWest, Math.min(timesBeenEast, Math.min(timesBeenSouth, timesBeenNorth)))) {
            direction = MOVE_SOUTH;
        } else if (timesBeenEast == Math.min(timesBeenWest, Math.min(timesBeenEast, Math.min(timesBeenSouth, timesBeenNorth)))) {
            direction = MOVE_EAST;
        } else if (timesBeenWest == Math.min(timesBeenWest, Math.min(timesBeenEast, Math.min(timesBeenSouth, timesBeenNorth)))) {
            direction = MOVE_WEST;
        }*/
 /*if (toNorth && toWest) {
            if (canMove(MOVE_NORTH, Bot)) {
                direction = MOVE_NORTH;
            } else if (canMove(MOVE_WEST, Bot)) {
                direction = MOVE_WEST;
            }
        } else if (toNorth && toEast) {
            if (canMove(MOVE_NORTH, Bot)) {
                direction = MOVE_NORTH;
            } else if (canMove(MOVE_EAST, Bot)) {
                direction = MOVE_EAST;
            }
        } else if (toSouth && toWest) {
            if (canMove(MOVE_SOUTH, Bot)) {
                direction = MOVE_SOUTH;
            } else if (canMove(MOVE_WEST, Bot)) {
                direction = MOVE_WEST;
            }
        } else if (toSouth && toEast) {
            if (canMove(MOVE_SOUTH, Bot)) {
                direction = MOVE_SOUTH;
            } else if (canMove(MOVE_EAST, Bot)) {
                direction = MOVE_EAST;
            }
        } else if (toNorth) {
            //if (canMove(MOVE_NORTH, Bot)) {
            direction = MOVE_NORTH;
            //}
        } else if (toSouth) {
            //if (canMove(MOVE_SOUTH, Bot)) {
            direction = MOVE_SOUTH;
            //}
        } else if (toEast) {
            //if (canMove(MOVE_EAST, Bot)) {
            direction = MOVE_EAST;
            //}
        } else if (toWest) {
            //if (canMove(MOVE_WEST, Bot)) {
            direction = MOVE_WEST;
            //}
        }*/
 /*else {
            direction = BLOCK_WEST;
        }*/
        int counter = 0;
        boolean samerow = (Bot.getRow() == Destination.getRow());
        boolean samecol = (Bot.getCol() == Destination.getCol());

        while (!canMove(direction, Bot)
                && counter < 7) {
            if (!onPerimeter(Bot) || samecol || samerow) {
                System.out.println("called moveAroundObstacle from " + Bot.toString());
                System.out.println("initialDirection: " + direction);
                direction = moveAroundObstacle(direction, Bot);
            } else {
                direction = moveTowardCenter(Bot);
            }
            counter++;
        }

        System.out.println(direction + " from " + Bot.toString());
        return direction;
    }

    private int moveAroundObstacle(int initialDirection, Location Bot) {
        /*if (canMove(initialDirection, Bot)) {
            return initialDirection;
        } else if (canMove(initialDirection + 90, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection + 90))) {
            return initialDirection + 90;
        } else if (canMove(initialDirection + 180, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection + 180))) {
            return initialDirection + 180;
        } else if (canMove(initialDirection + 270, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection + 270))) {
            return initialDirection + 270;
        } else if (canMove(initialDirection - 90, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection - 90))) {
            return initialDirection - 90;
        } else if (canMove(initialDirection - 180, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection - 180))) {
            return initialDirection - 180;
        } else if (canMove(initialDirection - 270, Bot) && canMove(initialDirection, Bot.getAdjacentLocation(initialDirection - 270))) {
            return initialDirection - 270;
        } else if (canMove(initialDirection + 90, Bot)) {
            return initialDirection + 90;
        } else if (canMove(initialDirection + 180, Bot)) {
            return initialDirection + 180;
        } else if (canMove(initialDirection + 270, Bot)) {
            return initialDirection + 270;
        } else if (canMove(initialDirection - 90, Bot)) {
            return initialDirection - 90;
        } else if (canMove(initialDirection - 180, Bot)) {
            return initialDirection - 180;
        } else if (canMove(initialDirection - 270, Bot)) {
            return initialDirection - 270;
        } else {
            return REST;
        }*/
        boolean canMoveDirection;
        Location currentLocation = new Location(Bot.getRow(), Bot.getCol());
        int direction, Destination = -1;
        int counter = 0;
        if (initialDirection == 0 || initialDirection == 180) {
            do {
                direction = MOVE_EAST;
                canMoveDirection = canMove(initialDirection, currentLocation);
                System.out.println("Can Move: " + canMoveDirection + " " + initialDirection + " from " + currentLocation.toString());
                currentLocation = new Location(currentLocation.getAdjacentLocation(direction).getRow(), currentLocation.getAdjacentLocation(direction).getCol());
                counter++;
            } while (!canMoveDirection && counter < 10);
            if (canMoveDirection) {
                Destination = direction;
            }
            currentLocation = new Location(Bot.getRow(), Bot.getCol());
            counter = 0;
            do {
                direction = MOVE_WEST;
                canMoveDirection = canMove(initialDirection, currentLocation);
                System.out.println("Can Move: " + canMoveDirection + " " + initialDirection + " from " + currentLocation.toString());
                currentLocation = new Location(currentLocation.getAdjacentLocation(direction).getRow(), currentLocation.getAdjacentLocation(direction).getCol());
                counter++;
            } while (!canMoveDirection && counter < 10);
            if (canMoveDirection) {
                Destination = direction;
            }
        } else if (initialDirection == 90 || initialDirection == 270) {
            counter = 0;
            do {
                direction = MOVE_NORTH;
                canMoveDirection = canMove(initialDirection, currentLocation);
                System.out.println("Can Move: " + canMoveDirection + " " + initialDirection + " from " + currentLocation.toString());
                currentLocation = new Location(currentLocation.getAdjacentLocation(direction).getRow(), currentLocation.getAdjacentLocation(direction).getCol());
                counter++;
            } while (!canMoveDirection && counter < 10);
            if (canMoveDirection) {
                Destination = direction;
            }
            currentLocation = new Location(Bot.getRow(), Bot.getCol());
            counter = 0;
            do {
                direction = MOVE_SOUTH;
                canMoveDirection = canMove(initialDirection, currentLocation);
                System.out.println("Can Move: " + canMoveDirection + " " + initialDirection + " from " + currentLocation.toString());
                currentLocation = new Location(currentLocation.getAdjacentLocation(direction).getRow(), currentLocation.getAdjacentLocation(direction).getCol());
                counter++;
            } while (!canMoveDirection && counter < 10);
            if (canMoveDirection) {
                Destination = direction;
            }
        }
        return Destination;
        //return REST;
    }

    /**
     * Moves toward the center.
     *
     * @param Bot
     * @return
     */
    private int moveTowardCenter(Location Bot) {
        int row = Bot.getRow(), col = Bot.getCol();
        int vertical = -1, horizontal = -1;
        if (row > 10) {
            vertical = MOVE_NORTH;
        } else {
            vertical = MOVE_SOUTH;
        }
        if (col > 10) {
            horizontal = MOVE_WEST;
        } else {
            horizontal = MOVE_EAST;
        }
        if (rand.nextBoolean()) {
            return vertical;
        } else {
            return horizontal;
        }
    }

    /**
     * Checks if the location is on the perimeter.
     *
     * @param Bot
     * @return true if onPerimeter, else false.
     */
    private boolean onPerimeter(Location Bot) {
        return (Bot.getCol() == 0 || Bot.getCol() == 20 || Bot.getRow() == 0 || Bot.getRow() == 20);
    }

    /**
     *
     * @param Bot
     * @param arena
     * @return
     */
    private Location findPrize(Location Bot, GameObject[][] arena) {
        int x = 0, y = 0, currentMV = 0;
        Location currentMostV = new Location(9, 10);
        int numberCurrentMVP = 0;
        for (int row = 0; row < arena.length; row++) {
            for (int col = 0; col < arena[0].length; col++) {
                if (arena[row][col] instanceof Prize) {
                    Prize prize = (Prize) arena[row][col];
                    //make sure that this only returns the highest value prize
                    if (prize.getValue() > currentMV) {
                        currentMV = prize.getValue();
                        currentMostV = prize.getLocation();
                        numberCurrentMVP = 0;
                    } else if (prize.getValue() == currentMV) {
                        numberCurrentMVP++;
                    }
                }
            }
        }
        if (currentMV == 10 || numberCurrentMVP > 0) {
            currentMostV = findClosestPrize(currentMV, Bot, arena);
        }
        //System.out.println("No Prizes in arena.");
        return currentMostV;
    }

    /**
     * Finds the location of the closest prize.
     *
     * @param Bot
     * @param arena
     * @return the location of the closest prize.
     */
    private Location findClosestPrize(int value, Location Bot, GameObject[][] arena) {
        int lowestDistance = 100;
        Location lowest_Distance = new Location(0, 0);
        for (int row = 0; row < arena.length; row++) {
            for (int col = 0; col < arena[0].length; col++) {
                int verticalDist = 0, horizontalDist = 0;
                //calculate the difference in the x and y position and then add the 2 differences 
                //together to get the number of moves required to reach that prize.
                if (arena[row][col] instanceof Prize) {
                    Prize prize = (Prize) arena[row][col];
                    if (prize.getValue() == value) {
                        int distance = prize.getLocation().distanceTo(Bot);
                        if (distance < lowestDistance) {
                            lowestDistance = distance;
                            lowest_Distance = prize.getLocation();
                        }
                    }
                }
            }
        }
        return lowest_Distance;
    }
}
