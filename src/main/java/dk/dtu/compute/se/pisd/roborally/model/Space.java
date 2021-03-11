/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import java.util.Random;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    private Player player;

    public final boolean northWall;
    public final boolean eastWall;
    public final boolean southWall;
    public final boolean westWall;

    public final boolean checkPoint;
    public final Heading conveyorBelt;
    public final boolean turnPoint;

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;

        Random rand = new Random();
        int temp = rand.nextInt(10);
        northWall = temp == 1;
        eastWall = temp == 2;
        southWall = temp == 3;
        westWall = temp == 4;

        checkPoint = true;
        conveyorBelt = null;
        turnPoint = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     *
     * @author Markus Visvaldis Ingemann Thieden, s164920
     *
     * function to get all wall orientation of the space
     * @return: array of all walls on the space
     */
    public Heading[] getWallOrientation() {
        // val is used to initialize the return array with the correct number of elements
        int val = 0;
        val += eastWall? 1 : 0;
        val += westWall? 1 : 0;
        val += southWall? 1 : 0;
        val += northWall? 1 : 0;
        Heading[] result = new Heading[val];

        int count = 0;
        if(this.eastWall)
        {
            result[count]= Heading.EAST;
            count++;
        }
        if(this.westWall)
        {
            result[count]= Heading.WEST;
            count++;
        }
        if(this.southWall)
        {
            result[count]= Heading.SOUTH;
            count++;
        }
        if(this.northWall)
        {
            result[count]= Heading.NORTH;
            count++;
        }
        return result;
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}
