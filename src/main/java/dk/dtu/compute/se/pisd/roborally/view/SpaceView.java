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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.TurningPoint;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 75; // 60; // 75;
    final public static int SPACE_WIDTH = 75;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        drawBackground();

        // updatePlayer();


        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * Draw the space and all its associated field actions
     */
    private void drawBackground()
    {
        Canvas spaceBackground = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext gc = spaceBackground.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.ROUND);

        for (Heading wall:
                space.getWalls()
             )
        {
            switch (wall)
            {
                case NORTH:
                    gc.strokeLine(2, 2, SPACE_WIDTH - 2, 2);
                    break;
                case SOUTH:
                    gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    break;
                case WEST:
                    gc.strokeLine(2, 2, 2, SPACE_HEIGHT - 2);
                    break;
                case EAST:
                    gc.strokeLine(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    break;
            }
        }

        gc.setLineWidth(2);
        for(FieldAction action : space.getFieldActions())
        {
            if(action instanceof ConveyorBelt)
            {
                gc.setStroke(Color.BLUE);
                gc.setFill(Color.BLUE);

                switch (((ConveyorBelt) action).getHeading())
                {
                    case NORTH:
                        gc.fillPolygon(new double[]{SPACE_WIDTH*0.1, SPACE_WIDTH*0.5, SPACE_WIDTH*0.9},
                                       new double[]{SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.4, SPACE_HEIGHT*0.5}, 3);
                        break;
                    case EAST:
                        gc.fillPolygon(new double[]{SPACE_WIDTH*0.5, SPACE_WIDTH*0.6, SPACE_WIDTH*0.5},
                                       new double[]{SPACE_HEIGHT*0.1, SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.9}, 3);
                        break;
                    case WEST:
                        gc.fillPolygon(new double[]{SPACE_WIDTH*0.5, SPACE_WIDTH*0.4, SPACE_WIDTH*0.5},
                                       new double[]{SPACE_HEIGHT*0.1, SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.9}, 3);
                        break;
                    case SOUTH:
                        gc.fillPolygon(new double[]{SPACE_WIDTH*0.1, SPACE_WIDTH*0.5, SPACE_WIDTH*0.9},
                                       new double[]{SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.6, SPACE_HEIGHT*0.5}, 3);
                        break;
                    default:
                        this.setStyle("-fx-background-color: pink;");
                }
            }
            else if(action instanceof TurningPoint)
            {
                gc.setStroke(Color.BLUE);
                switch (((TurningPoint) action).getHeading())
                {
                    case WEST:
                        gc.strokePolyline(  new double[]{SPACE_WIDTH*0.2, SPACE_WIDTH*0.2, SPACE_WIDTH*0.8, SPACE_WIDTH*0.8, SPACE_WIDTH*0.2, SPACE_WIDTH*0.5, SPACE_WIDTH*0.2, SPACE_WIDTH*0.5},
                                            new double[]{SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.8, SPACE_HEIGHT*0.8, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.10, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.3},
                                            8);
                        break;
                    case NORTH:
                    case EAST:
                    case SOUTH:
                        gc.strokePolyline(   new double[]{SPACE_WIDTH*0.8, SPACE_WIDTH*0.8, SPACE_WIDTH*0.2, SPACE_WIDTH*0.2, SPACE_WIDTH*0.8, SPACE_WIDTH*0.5, SPACE_WIDTH*0.8, SPACE_WIDTH*0.5},
                                             new double[]{SPACE_HEIGHT*0.5, SPACE_HEIGHT*0.8, SPACE_HEIGHT*0.8, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.10, SPACE_HEIGHT*0.2, SPACE_HEIGHT*0.3},
                                            8);
                        break;
                    default:
                        this.setStyle("-fx-background-color: cyan;");
                }
            }
            else if(action instanceof CheckPoint)
            {
                gc.setStroke(Color.GREEN);
                gc.setFont(new Font("arial", 18));
                gc.strokeText(Integer.toString(((CheckPoint) action).getCheckpointNumber()), SPACE_WIDTH*0.8, SPACE_HEIGHT*0.8);
            }
        }
        this.getChildren().add(spaceBackground);
    }

    /**
     * Draw the player occupying the field (if there is one)
     */
    private void updatePlayer() {

        if(this.getChildren().size() > 1)
        {
            this.getChildren().remove(1,this.getChildren().size());
        }
        //this.getChildren().clear();


        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }
            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }

}
