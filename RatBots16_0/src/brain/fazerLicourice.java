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
    static int lastDirection = -1;

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
        Location Bot = getLocation();
        Location prize = findPrize(Bot, theArena);
        System.out.println("Destination: " + prize.toString());
        return moveTo(prize, Bot, theArena);
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
        if (lastDirection == MOVE_NORTH && toNorth) {
            direction = MOVE_NORTH;
        } else if (lastDirection == MOVE_SOUTH && toSouth) {
            direction = MOVE_SOUTH;
        } else if (lastDirection == MOVE_EAST && toEast) {
            direction = MOVE_EAST;
        } else if (lastDirection == MOVE_WEST && toWest) {
            direction = MOVE_WEST;
        } else if (true) {
            if (Math.abs(Bot.getCol() - Destination.getCol()) < Math.abs(Bot.getRow() - Destination.getRow())) {
                if (toNorth) {
                    direction = MOVE_NORTH;
                } else if (toSouth) {
                    direction = MOVE_SOUTH;
                }
            } else if (Math.abs(Bot.getRow() - Destination.getRow()) < Math.abs(Bot.getCol() - Destination.getCol())) {
                if (toWest) {
                    direction = MOVE_WEST;
                } else if (toEast) {
                    direction = MOVE_EAST;
                }
            } else {
                boolean vertical = rand.nextBoolean();
                if (vertical || (!toWest && !toEast)) {
                    System.out.println("Vertical: " + vertical + ", toWest: " + toWest + ", toEast: " + toEast + ", toNorth: " + toNorth + ", toSouth: " + toSouth);
                    if (toNorth) {
                        direction = MOVE_NORTH;
                    } else if (toSouth) {
                        direction = MOVE_SOUTH;
                    }
                } else if (!vertical || (!toNorth && !toSouth)) {
                    System.out.println("Horizontal: " + !vertical + ", toNorth: " + toNorth + ", toSouth: " + toSouth + ", toWest: " + toWest + ", toEast: " + toEast);
                    if (toWest) {
                        direction = MOVE_WEST;
                    } else if (toEast) {
                        direction = MOVE_EAST;
                    }
                }
            }
        }
        boolean samerow = (Bot.getRow() == Destination.getRow());
        boolean samecol = (Bot.getCol() == Destination.getCol());
        lastDirection = direction;
        if (!canMove(direction, Bot)) {
            if (!onPerimeter(Bot) || samecol || samerow) {
                System.out.println("called moveAroundObstacle from " + Bot.toString());
                System.out.println("initialDirection: " + direction);
                direction = moveAroundObstacle(direction, Bot);
            } else {
                direction = moveTowardCenter(Bot);
            }
        }
        System.out.println(direction + " from " + Bot.toString());
        return direction;
    }

    private int moveAroundObstacle(int initialDirection, Location Bot) {
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
        int closestDistance = 0, numberCurrentMVP = 0;
        for (int row = 0; row < arena.length; row++) {
            for (int col = 0; col < arena[0].length; col++) {
                if (arena[row][col] instanceof Prize) {
                    Prize prize = (Prize) arena[row][col];
                    //make sure that this only returns the closest, highest value prize
                    if (prize.getValue() > currentMV) {
                        currentMV = prize.getValue();
                        currentMostV = prize.getLocation();
                        closestDistance = Bot.distanceTo(currentMostV);
                    } else if (prize.getValue() == currentMV) {
                        numberCurrentMVP++;
                    }
                }
            }
        }
        if (currentMV == 10 || numberCurrentMVP > 0) {
            currentMostV = findClosestPrize(currentMV, Bot, arena);
        }
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
