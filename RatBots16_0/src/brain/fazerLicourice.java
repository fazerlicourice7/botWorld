/*
 * Copyright (C) 2016 18balanagav
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author 18balanagav
 */
public class fazerLicourice extends BotBrain {

    Random rand;
    static int lastDirection = -1;
    static GameObject[][] theArena;
    int colorDisplacement;
    ArrayList<Location> blacklist;
    int stationaryCounter;

    public fazerLicourice() {
        rand = new Random();
        setName("fazerLicourice");
    }

    @Override
    public void initForRound() {
        //System.out.println("Bot was stationary for " + stationaryCounter + " moves.");
        stationaryCounter = 0;
        blacklist = new ArrayList<>();
        colorDisplacement = rand.nextInt(15);
    }

    @Override
    public int chooseAction() {
        this.setPreferredColor(Color.getHSBColor(((float) (getMoveNumber() + colorDisplacement) / 50), 1f, 1f));
        theArena = getArena();
        Location Bot = getLocation();
        Location prize = searchForPrize(Bot);
        int direction = -1;
        //System.out.println("");
        if (getScore() == getBestScore() && getScore() >= 500 && getRoundNumber() < 125 && !isGoldBlocked() && numberOfBots() > 2) {
            return blockGold(Bot);
        }
        direction = moveTo(Bot, prize, true);
        if (direction == -1) {
            stationaryCounter++;
        }
        //System.out.println(direction);
        return direction;
    }

    private Location searchForPrize(Location Bot) {
        Location prize = findPrize(Bot, new ArrayList());
        int counter = 0;
        while (!isLocationAccesible(prize) && counter < 25) {
            blacklist.add(prize);
            prize = findPrize(Bot, blacklist);
            counter++;
        }
        return prize;
    }

    private boolean isGoldBlocked() {
        boolean right = theArena[10][19] instanceof Block,
                rightRoom = theArena[9][16] instanceof Block && theArena[11][16] instanceof Block,
                left = theArena[10][1] instanceof Block,
                leftRoom = theArena[9][4] instanceof Block && theArena[11][4] instanceof Block,
                top = theArena[1][10] instanceof Block,
                topRoom = theArena[4][9] instanceof Block && theArena[4][11] instanceof Block,
                bottom = theArena[19][10] instanceof Block,
                bottomRoom = theArena[16][9] instanceof Block && theArena[16][11] instanceof Block;
        return (right || rightRoom) && (left || leftRoom) && (top || topRoom) && (bottom || bottomRoom);
    }

    private int blockGold(Location Bot) {
        //top
        if (Bot.equals(new Location(4, 10)) && (!(theArena[4][9] instanceof Block) || !(theArena[4][11] instanceof Block))) {
            if (!(theArena[4][9] instanceof Block)) {
                return BLOCK_WEST;
            } else if (!(theArena[4][11] instanceof Block)) {
                return BLOCK_EAST;
            } else {
                return REST;
            }
        }//left
        else if (Bot.equals(new Location(10, 4)) && (!(theArena[9][4] instanceof Block) || !(theArena[11][4] instanceof Block))) {
            if (!(theArena[9][4] instanceof Block)) {
                return BLOCK_NORTH;
            } else if (!(theArena[11][4] instanceof Block)) {
                return BLOCK_SOUTH;
            } else {
                return REST;
            }
        }//right
        else if (Bot.equals(new Location(10, 16)) && (!(theArena[9][16] instanceof Block) || !(theArena[11][16] instanceof Block))) {
            if (!(theArena[9][16] instanceof Block)) {
                return BLOCK_NORTH;
            } else if (!(theArena[11][16] instanceof Block)) {
                return BLOCK_SOUTH;
            } else {
                return REST;
            }
        }//bottom
        else if (Bot.equals(new Location(16, 10)) && (!(theArena[16][9] instanceof Block) || !(theArena[16][11] instanceof Block))) {
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
            if (min == right && (!(theArena[10][18] instanceof Block) && (!(theArena[9][16] instanceof Block) || !(theArena[11][16] instanceof Block)))) {
                return moveTo(Bot, new Location(10, 18), true);
            } else if (min == left && (!(theArena[10][2] instanceof Block) && (!(theArena[9][4] instanceof Block) || !(theArena[11][14] instanceof Block)))) {
                return moveTo(Bot, new Location(10, 2), true);
            } else if (min == top && (!(theArena[2][10] instanceof Block) && (!(theArena[4][9] instanceof Block) || !(theArena[4][11] instanceof Block)))) {
                return moveTo(Bot, new Location(2, 10), true);
            } else if (min == bottom && (!(theArena[18][10] instanceof Block) && (!(theArena[16][9] instanceof Block) || !(theArena[16][11] instanceof Block)))) {
                return moveTo(Bot, new Location(18, 10), true);
            } else {
                return moveTo(Bot, new Location(9, 10), true);
            }
        }
    }

    public boolean canMove(int directionChoice, Location myLoc) {
        int direction = directionChoice % 1000;

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

    private int moveTo(Location Bot, Location Destination, boolean considerDarting) {
        boolean toEast, toWest, toNorth, toSouth;
        int verticalDist, horizontalDist, direction = -1;
        int destinationCol = Destination.getCol(), destinationRow = Destination.getRow();
        int botCol = Bot.getCol(), botRow = Bot.getRow();
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
                    if (toNorth) {
                        direction = MOVE_NORTH;
                    } else if (toSouth) {
                        direction = MOVE_SOUTH;
                    }
                } else if (!vertical || (!toNorth && !toSouth)) {
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
                direction = moveAroundObstacle(Bot, direction, Destination);
            } else {
                direction = moveTowardCenter(Bot);
            }
        } else if (canMove(direction, Bot) && considerDarting) {
            int dartDirection = dartDirection(Bot, Destination);
            if (dartDirection != 999) {
                direction = dartDirection;
            } else {
            }
        }
        return direction;
    }

    private int distanceBetween(Location currentLocation, Location Destination) {
        int counter = 0;
        int direction = -1;
        while (!currentLocation.equals(Destination) && counter < 400) {
            direction = moveTo(currentLocation, Destination, false);
            currentLocation = currentLocation.getAdjacentLocation(direction);
            counter++;
        }
        return counter;
    }

    private int dartDirection(Location Bot, Location destination) {
        int minimumDistance = Bot.distanceTo(destination);
        int Direction = -1;
        for (int direction = 0; direction <= 270; direction += 90) {
            Location currentLocation = Bot;
            while (currentLocation.getAdjacentLocation(direction).isValidLocation() && !(theArena[currentLocation.getAdjacentLocation(direction).getRow()][currentLocation.getAdjacentLocation(direction).getCol()] instanceof Block) && !(theArena[currentLocation.getAdjacentLocation(direction).getRow()][currentLocation.getAdjacentLocation(direction).getCol()] instanceof Bot)) {
                currentLocation = currentLocation.getAdjacentLocation(direction);
            }
            if (distanceBetween(currentLocation, destination) < minimumDistance && distanceBetween(currentLocation, destination) != 400) {
                minimumDistance = distanceBetween(currentLocation, destination);
                Direction = direction;
            }
        }
        Direction += 1000;
        return Direction;
    }

    private int moveAroundObstacle(Location Bot, int initialDirection, Location destination) {
        boolean canMoveDirection;
        Location currentLocation = Bot;
        int tempDirection, direction = -1;
        int counter = 0, lowestDistanceToPrize = 1000;
        //System.out.println("From " + currentLocation.toString());
        if ((initialDirection == 0 || initialDirection == 180) && (canMove(MOVE_EAST, Bot) || canMove(MOVE_WEST, Bot))) {
            //System.out.println("Want to move:" + initialDirection + ". Can move East:" + canMove(MOVE_EAST, Bot) + ". Can move West: " + canMove(MOVE_WEST, Bot));
            do {
                tempDirection = MOVE_EAST;
                canMoveDirection = canMove(initialDirection, currentLocation);
                //System.out.println("can move " + initialDirection + " " + canMove(initialDirection, currentLocation) + " from " + currentLocation.toString());
                currentLocation = currentLocation.getAdjacentLocation(tempDirection);
                counter++;
                //} while (!canMoveDirection && counter < 10 && currentLocation.isValidLocation() && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Block) && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Bot));
            } while (!canMoveDirection && counter < 10);
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
                direction = tempDirection;
            }
            currentLocation = Bot;
            counter = 0;
            do {
                tempDirection = MOVE_WEST;
                canMoveDirection = canMove(initialDirection, currentLocation);
                //System.out.println("can move " + initialDirection + " " + canMove(initialDirection, currentLocation) + " from " + currentLocation.toString());
                currentLocation = currentLocation.getAdjacentLocation(tempDirection);
                counter++;
                //while (!canMoveDirection && counter < 10 && currentLocation.isValidLocation() && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Block) && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Bot));
            } while (!canMoveDirection && counter < 10);
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
                direction = tempDirection;
            }
            //System.out.println("End up moving " + direction);
        } else if ((initialDirection == 0 || initialDirection == 180) && !canMove(MOVE_EAST, Bot) && !canMove(MOVE_WEST, Bot)) {
            //System.out.println("Want to move:" + initialDirection + ". Can move East:" + canMove(MOVE_EAST, Bot) + ". Can move West: " + canMove(MOVE_WEST, Bot));
            direction = Math.abs(initialDirection - 180);
            //System.out.println("End up moving " + direction);
        } else if ((initialDirection == 90 || initialDirection == 270) && (canMove(MOVE_NORTH, Bot) || canMove(MOVE_SOUTH, Bot))) {
            //System.out.println("Want to move:" + initialDirection + ". Can move North:" + canMove(MOVE_NORTH, Bot) + ". Can move South: " + canMove(MOVE_SOUTH, Bot));
            counter = 0;
            do {
                tempDirection = MOVE_NORTH;
                canMoveDirection = canMove(initialDirection, currentLocation);
                //System.out.println("can move " + initialDirection + " " + canMove(initialDirection, currentLocation) + " from " + currentLocation.toString());
                currentLocation = currentLocation.getAdjacentLocation(tempDirection);
                counter++;
                //} while (!canMoveDirection && counter < 10 && currentLocation.isValidLocation() && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Block) && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Bot));
            } while (!canMoveDirection && counter < 10);
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
                direction = tempDirection;
            }
            currentLocation = Bot;
            counter = 0;
            do {
                tempDirection = MOVE_SOUTH;
                canMoveDirection = canMove(initialDirection, currentLocation);
                //System.out.println("can move " + initialDirection + " " + canMove(initialDirection, currentLocation) + " from " + currentLocation.toString());
                currentLocation = currentLocation.getAdjacentLocation(tempDirection);
                counter++;
                //} while (!canMoveDirection && counter < 10 && currentLocation.isValidLocation() && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Block) && !(theArena[currentLocation.getRow()][currentLocation.getCol()] instanceof Bot));
            } while (!canMoveDirection && counter < 10);
            lowestDistanceToPrize = (lowestDistanceToPrize > currentLocation.distanceTo(destination)) ? currentLocation.distanceTo(destination) : lowestDistanceToPrize;
            if (canMoveDirection && lowestDistanceToPrize == currentLocation.distanceTo(destination)) {
                direction = tempDirection;
            }
            //System.out.println("End up moving " + direction);
        } else if ((initialDirection == 90 || initialDirection == 270) && !canMove(MOVE_NORTH, Bot) && !canMove(MOVE_SOUTH, Bot)) {
            //System.out.println("Want to move:" + initialDirection + ". Can move North:" + canMove(MOVE_NORTH, Bot) + ". Can move South: " + canMove(MOVE_SOUTH, Bot));
            direction = Math.abs(initialDirection - 180);
            //System.out.println("End up moving " + direction);
        }
        return direction;
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

    private boolean onPerimeter(Location Bot) {
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

    private Location findPrize(Location Bot, ArrayList<grid.Location> blacklist) {
        int x = 0, y = 0, currentMV = 0;
        Location currentMostV = new Location(9, 9);
        int closestDistance = 0, numberCurrentMVP = 0;
        for (int row = 0; row < theArena.length; row++) {
            for (int col = 0; col < theArena[0].length; col++) {
                /*if (theArena[row][col] instanceof Block) {
                    System.out.println("(" + row + "," + col + ")");
                }*/
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
            currentMostV = findClosestPrize(Bot, currentMV);
        }
        return currentMostV;
    }

    private Location findClosestPrize(Location Bot, int value) {
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
