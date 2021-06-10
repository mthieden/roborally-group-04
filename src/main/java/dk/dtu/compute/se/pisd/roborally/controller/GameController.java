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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.exceptions.ImpossibleMoveExceptions;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController extends Subject {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        Player current = board.getCurrentPlayer();
        if(space.getPlayer() == null) {
            current.setSpace(space);
        }
        else {
            // TODO add something

        }


    }

    /**
     * Moves the player to neighboring space depending on the heading,
     * recursively also move any player in the way
     *
     * @author Markus Visvaldis Ingemann Thieden, s164920
     *
     * @param player the player which moves
     * @param space the space to which the player should move
     * @param heading the direction the player is being moved in
     */
    public void moveToSpace(@NotNull Player player,
                            @NotNull Space space,
                            @NotNull Heading heading) throws ImpossibleMoveExceptions
    {
        Player other = space.getPlayer();
        Space target = board.getNeighbour(space, heading);
        if(other != null)
        {
            if (target != null)
            {
                moveToSpace(other, target, heading);
            } else
           {
                throw new ImpossibleMoveExceptions(player, space, heading);
           }
        }
        List<Heading> spaceWalls = player.getSpace().getWalls();

        boolean canMove = true;
        for (Heading wall : spaceWalls)
        {
            if (heading == wall)
            {
                canMove = false;
            }
        }

        spaceWalls = space.getWalls();
        for (Heading wall : spaceWalls)
        {
            if(heading == wall.next().next())
            {
                canMove=false;
            }
        }

        if(canMove)
        {
            player.setSpace(space);
        }
        else
        {
            throw new ImpossibleMoveExceptions(player, space, heading);
        }
    }

    /**
     * This is a method for the programming phase, where each player gets some random
     * command cards and can choose to place them on the card fields.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }


    /**
     * Generates a random CommandCard
     * @return a random CommandCard
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Method for finishing the programming phase and moving onto the activation phase.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    /**
     * Makes the specified field visible for all players
     * @param register the register being made visible
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }


    /**
     * Makes all program field visible for all players
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }


    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Executes the programs in step mode
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * Executes the programs in normal mode
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }


    /**
     * Executes the next step
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase (Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    executeSpaceActions();
                    checkForWinner();
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Executes a command option and sets the phase back to ACTIVATION
     * @param command the command being executed
     */
    public void executeCommandOptionAndContinue(@NotNull Command command)
    {
        Player currentPlayer = board.getCurrentPlayer();
        if(currentPlayer!=null &&
                board.getPhase()== Phase.PLAYER_INTERACTION &&
                command!= null)
        {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, command);
            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep()+1;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }
        }
    }

    /**
     * Executes a command
     * @param player the player executing the command
     * @param command the command being executed
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case FAST_FAST_FORWARD:
                    this.fastFastForward(player);
                    break;
                case BACKUP:
                    this.backUp(player);
                    break;
                case UTURN:
                    this.Uturn(player);
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * moveForward moves the robot 1 space in the current direction
     *
     * @param player thee player/robot being moved forward
     * @author Mathias Ravn, s195468
     * @author Markus Visvaldis Ingemann Thieden, s164920
     *
     */
    public void moveForward(@NotNull Player player) {
        Space current = player.getSpace();
        if(current != null && player.board == current.board) {
            Space target = board.getNeighbour(current, player.getHeading());
            if(target != null )
            {
                try
                {
                    moveToSpace(player, target, player.getHeading());
                } catch (ImpossibleMoveExceptions impossibleMoveExceptions)
                {
                    //do nothing
                }
            }
        }
    }

    /**
     * fastForward moves the robot 2 spaces in the current direction
     * fastForward calls the moveForward command to run, which is the function
     *
     * @param player the player/robot being moved forward
     * @author Mathias Ravn, s195468
     */
    public void fastForward(@NotNull Player player) {
        this.moveForward(player);
        this.moveForward(player);
    }

    /**
     * Moves the robot 3 spaces forward in the current direction of the robot
     * This is done by repeating the moveForward command three times.
     * @param player the player/robot being moved forward
     * @author Mathias Ravn, s195468
     */
    public void fastFastForward(@NotNull Player player) {
        this.moveForward(player);
        this.moveForward(player);
        this.moveForward(player);
    }
    /**
     * turnRight causes the robots direction to change 90 degrees clockwise
     * the function calls the setHeading function which has a builtin function .next
     * to do what the is intended for.
     * @param player the player/robot turning right
     * @author Mathias Ravn, s195468
     *
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }
    /**
     * turnLeft causes the robots direction to change 90 degrees anti-clockwise
     * the function calls the setHeading function which has a builtin function .next
     * to do what the is intended for.
     *
     * @param player the player/robot turning left
     * @author Mathias Ravn, s195468
     *
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * backUp causes the robots to move backward without changing the direction the robot is headed
     * @author Markus Thieden, s164920
     *
     */
    public void backUp(@NotNull Player player)
    {
        player.setHeading(player.getHeading().prev());
        player.setHeading(player.getHeading().prev());
        this.moveForward(player);
        player.setHeading(player.getHeading().prev());
        player.setHeading(player.getHeading().prev());
    }

    /**
     * Uturn turns the robot 180 degrees while staying in the same location
     * @author Haris Pelivani, s194749
     *
     */
    public void Uturn(@NotNull Player player)
    {
        player.setHeading(player.getHeading().prev());
        player.setHeading(player.getHeading().prev());
    }

    /**
     * Moves a CommandCard from one CommandCardField to another
     * @param source the source field
     * @param target the destination field
     * @return true if the card was successfully moved
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Executes the actions of all fields that have a player on them
     */
    public void executeSpaceActions()
    {
        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            Player player = board.getPlayer(i);
            Space space = player.getSpace();

            if(!space.getFieldActions().isEmpty())
            {
                for (FieldAction field : player.getSpace().getFieldActions())
                {
                    field.doAction(this, player.getSpace());
                }
            }
        }
    }

    /**
     * Checks if a player has reached the final checkpoint and won the game
     * @author Tobias Hansen, s164270
     */
    private void checkForWinner()
    {
        System.out.println(board.numberOfCheckpoints);
        boolean gameOver = false;
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if(player.getNextCheckpoint() == board.numberOfCheckpoints)
            {
                gameOver = true;
            }
        }
        if(gameOver)
        {
            notifyChange();

        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}