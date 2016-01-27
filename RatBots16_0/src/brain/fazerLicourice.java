/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.Block;
import actor.Bot;
import actor.BotBrain;
import actor.GameObject;
import actor.Prize;
import grid.Location;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author 18balanagav
 */
public class fazerLicourice extends BotBrain {

    //ArrayList<Integer> path = new ArrayList<>();
    Random rand;
    //int timesBeenThere[][] = new int[getArena().length][getArena()[0].length];
    static int lastDirection = -1;
    static Location Bot = new Location(0, 0);
    static GameObject[][] theArena;
    int colorDisplacement;
    ArrayList<Location> blacklist;

    /**
     *
     */
    public fazerLicourice() {
        rand = new Random();
        setName("fazerLicourice");
    }

    @Override
    public void initForRound() {
        blacklist = new ArrayList<>();
        colorDisplacement = rand.nextInt(15);
    }

    @Override
    public int chooseAction() {
        this.setPreferredColor(Color.getHSBColor(((float) (getMoveNumber() + colorDisplacement) / 50), 1f, 1f));
        theArena = getArena();
        Bot = getLocation();
        Location prize = searchForPrize();
        if (getScore() == getBestScore() && getScore() >= 500 && getRoundNumber() < 125 && !isGoldBlocked() && numberOfBots() > 2) {
            return blockGold();
        }
        System.out.println("Destination: " + prize.toString());
        return moveTo(prize);
    }

    private Location searchForPrize() {
        Location prize = findPrize(new ArrayList());
        int counter = 0;
        while (!isLocationAccesible(prize) && counter < 5) {
            blacklist.add(prize);
            prize = findPrize(blacklist);
            counter++;
        }
        return prize;
    }

    private boolean isGoldBlocked() {
        boolean right = theArena[10][19] instanceof Block,
                left = theArena[10][1] instanceof Block,
                top = theArena[1][10] instanceof Block,
                bottom = theArena[19][10] instanceof Block;
        return right && left && top && bottom;
    }

    private int blockGold() {
        if (Bot.equals(new Location(4, 10)) && (!(theArena[4][9] instanceof Block) || !(theArena[4][11] instanceof Block))) {//top
            if (!(theArena[4][9] instanceof Block)) {
                return BLOCK_WEST;
            } else if (!(theArena[4][11] instanceof Block)) {
                return BLOCK_EAST;
            } else {
                return REST;
            }
        } else if (Bot.equals(new Location(10, 4)) && (!(theArena[9][4] instanceof Block) || !(theArena[11][4] instanceof Block))) {//left
            if (!(theArena[9][4] instanceof Block)) {
                return BLOCK_NORTH;
            } else if (!(theArena[11][4] instanceof Block)) {
                return BLOCK_SOUTH;
            } else {
                return REST;
            }
        } else if (Bot.equals(new Location(10, 16)) && (!(theArena[9][16] instanceof Block) || !(theArena[11][16] instanceof Block))) {//right
            if (!(theArena[9][16] instanceof Block)) {
                return BLOCK_NORTH;
            } else if (!(theArena[11][16] instanceof Block)) {
                return BLOCK_SOUTH;
            } else {
                return REST;
            }
        } else if (Bot.equals(new Location(16, 10)) && (!(theArena[16][9] instanceof Block) || !(theArena[16][11] instanceof Block))) {//bottom
            if (!(theArena[16][9] instanceof Block)) {
                return BLOCK_WEST;
            } else if (!(theArena[16][11] instanceof Block)) {
                return BLOCK_EAST;
            } else {
                return REST;
            }
        } else if (Bot.equals(new Location(10, 18)) && !(theArena[10][19] instanceof Block)) {
            return BLOCK_EAST;
        } else if (Bot.equals(new Location(10, 2)) && !(theArena[10][1] instanceof Block)) {
            return BLOCK_WEST;
        } else if (Bot.equals(new Location(18, 10)) && !(theArena[19][10] instanceof Block)) {
            return BLOCK_SOUTH;
        } else if (Bot.equals(new Location(2, 10)) && !(theArena[1][10] instanceof Block)) {
            return BLOCK_NORTH;
        } else {
            int right = 0, left = 0, top = 0, bottom = 0;
            if (!(theArena[10][19] instanceof Block)) {
                right = Bot.distanceTo(new Location(10, 18));
            } else if (theArena[10][19] instanceof Block) {
                right = 1000;
            }
            if (!(theArena[10][1] instanceof Block)) {
                left = Bot.distanceTo(new Location(10, 2));
            } else if (theArena[10][1] instanceof Block) {
                left = 1000;
            }
            if (!(theArena[19][10] instanceof Block)) {
                bottom = Bot.distanceTo(new Location(18, 10));
            } else if (theArena[19][10] instanceof Block) {
                bottom = 1000;
            }
            if (!(theArena[1][10] instanceof Block)) {
                top = Bot.distanceTo(new Location(2, 10));
            } else if (theArena[1][10] instanceof Block) {
                top = 1000;
            }
            int min = Math.min(right, Math.min(left, Math.min(bottom, top)));
            if (min == right && !(theArena[10][18] instanceof Block)) {
                return moveTo(new Location(10, 18));
            } else if (min == left && !(theArena[10][2] instanceof Block)) {
                return moveTo(new Location(10, 2));
            } else if (min == top && !(theArena[2][10] instanceof Block)) {
                return moveTo(new Location(2, 10));
            } else if (min == bottom && !(theArena[18][10] instanceof Block)) {
                return moveTo(new Location(18, 10));
            } else {
                return moveTo(new Location(9, 10));
            }
        }
    }

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

    private int moveTo(Location Destination) {
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
            if (!onPerimeter() || samecol || samerow) {
                System.out.println("called moveAroundObstacle from " + Bot.toString());
                System.out.println("initialDirection: " + direction);
                direction = moveAroundObstacle(direction, Destination);
            } else {
                direction = moveTowardCenter();
            }
        }
        System.out.println(direction + " from " + Bot.toString());
        return direction;
    }

    private int moveAroundObstacle(int initialDirection, Location destination) {
        boolean canMoveDirection;
        Location currentLocation = new Location(Bot.getRow(), Bot.getCol());
        int direction, Destination = -1;
        int counter = 0, lowestDistanceToPrize = 1000;
        if (initialDirection == 0 || initialDirection == 180) {
            do {
                direction = MOVE_EAST;
                canMoveDirection = canMove(initialDirection, currentLocation);
                System.out.println("Can Move: " + canMoveDirection + " " + initialDirection + " from " + currentLocation.toString());
                currentLocation = new Location(currentLocation.getAdjacentLocation(direction).getRow(), currentLocation.getAdjacentLocation(direction).getCol());
                counter++;
            } while (!canMoveDirection && counter < 10);
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
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
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
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
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
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
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
                Destination = direction;
            }
        }
        return Destination;
        //return REST;
    }

    private boolean isLocationAccesible(Location destination) { // currently specific for gold prizes, and custom block locations that I plan to put down.
        if (destination.equals(new Location(1, 10))) { //top
            if (!(theArena[4][9] instanceof Block) && !(theArena[4][11] instanceof Block)) {
                return true;
            } else {
                return false;
            }
        } else if (destination.equals(new Location(10, 19))) { //right
            if (!(theArena[9][16] instanceof Block) && !(theArena[11][16] instanceof Block)) {
                return true;
            } else {
                return false;
            }
        } else if (destination.equals(new Location(10, 1))) {//left
            if (!(theArena[9][4] instanceof Block) && !(theArena[11][4] instanceof Block)) {
                return true;
            } else {
                return false;
            }
        } else if (destination.equals(new Location(19, 10))) {//bottom
            if (!(theArena[16][9] instanceof Block) && !(theArena[16][11] instanceof Block)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private int moveTowardCenter() {
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

    private boolean onPerimeter() {
        return (Bot.getCol() == 0 || Bot.getCol() == 20 || Bot.getRow() == 0 || Bot.getRow() == 20);
    }

    private int numberOfBots() {
        int counter = 0;
        for (int row = 0; row < theArena.length; row++) {
            for (int col = 0; col < theArena[0].length; col++) {
                if (theArena[row][col] instanceof Bot) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private Location findPrize(ArrayList<grid.Location> blacklist) {
        int x = 0, y = 0, currentMV = 0;
        Location currentMostV = new Location(9, 10);
        int closestDistance = 0, numberCurrentMVP = 0;
        for (int row = 0; row < theArena.length; row++) {
            for (int col = 0; col < theArena[0].length; col++) {
                if (theArena[row][col] instanceof Prize) {
                    boolean ignore = false;
                    for (int i = 0; i < blacklist.size(); i++) {
                        if (row == blacklist.get(i).getRow() && col == blacklist.get(i).getCol()) {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore) {
                        Prize prize = (Prize) theArena[row][col];
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
        }
        if (currentMV == 10 || numberCurrentMVP > 0) {
            currentMostV = findClosestPrize(currentMV);
        }
        return currentMostV;
    }

    private Location findClosestPrize(int value) {
        int lowestDistance = 100;
        Location lowest_Distance = new Location(0, 0);
        for (int row = 0; row < theArena.length; row++) {
            for (int col = 0; col < theArena[0].length; col++) {
                int verticalDist = 0, horizontalDist = 0;
                //calculate the difference in the x and y position and then add the 2 differences 
                //together to get the number of moves required to reach that prize.
                if (theArena[row][col] instanceof Prize) {
                    Prize prize = (Prize) theArena[row][col];
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
