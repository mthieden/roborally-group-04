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
import dk.dtu.compute.se.pisd.roborally.controller.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.TurningPoint;

import java.util.ArrayList;
import java.util.List;
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

    //public final SpaceFunction[] spaceFunctions;

    public List<SpaceFunction> spaceFunctions = new ArrayList<>();
    public List<FieldAction> field = new ArrayList<>();
    public List<Heading> walls = new ArrayList<>();


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;

        Random rand = new Random();
        int temp = rand.nextInt(10);
        if( temp == 1)
        {
            walls.add(Heading.NORTH);
        }
        if( temp == 2)
        {
            walls.add(Heading.SOUTH);
        }
        if( temp == 3)
        {
            walls.add(Heading.EAST);
        }
        if( temp == 4)
        {
            walls.add(Heading.WEST);
        }

         if(temp == 9)
         {
             spaceFunctions.add( SpaceFunction.CHECKPOINT);

             field.add(new CheckPoint());
         }
         else if(temp == 8)
         {
             SpaceFunction spacefunc = SpaceFunction.CONVEYORBELT;
             spacefunc.setHeading(Heading.NORTH);
             spaceFunctions.add(spacefunc);

             field.add(new ConveyorBelt(Heading.NORTH));
         }
         else if(temp == 7)
         {
             SpaceFunction spacefunc = SpaceFunction.TURNINGPOINT;
             spacefunc.setHeading(Heading.WEST);
             spaceFunctions.add(spacefunc);
             field.add(new TurningPoint(Heading.WEST));
         }
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
    public List<Heading> getWalls() {
        return walls;
    }

    public List<FieldAction> getField() {
        return field;
    }
    public List<SpaceFunction> getSpaceFunctions() {
        return spaceFunctions;
    }

    public void setWalls(List<Heading> walls) {
        this.walls=walls;
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }


}
