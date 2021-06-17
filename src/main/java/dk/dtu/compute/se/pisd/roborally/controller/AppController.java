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

import com.mysql.cj.conf.url.LoadbalanceConnectionUrl;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<Integer> BOARDOPTIONS = Arrays.asList(1, 2, 3);

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Starts a new game. Prompts the user to select board and number of players.
     * @author Tobias Hansen, s164270
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        ChoiceDialog<Integer> dialog2 = new ChoiceDialog<>(BOARDOPTIONS.get(0), BOARDOPTIONS);
        dialog2.setTitle("Board");
        dialog2.setHeaderText("Select board");
        Optional<Integer> result2 = dialog2.showAndWait();
        String selectedBoard;
        switch (result2.get())
        {
            case 1:
                selectedBoard = "defaultBoard";
                break;
            case 2:
                selectedBoard = "rollercoaster";
                break;
            case 3:
                selectedBoard = "LOL";
                break;
            default:
                selectedBoard = "defaultBoard";
        }

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.

            Board board = LoadBoard.loadBoard(selectedBoard); //new Board(8,8);
            gameController = new GameController(board);
            gameController.attach(this); //observe the gameController to react when someone wins the game
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }

    /**
     * Save the current game to the database
     * @author Tobias Hansen, s164270
     */
    public void saveGame() {
        IRepository repository = RepositoryAccess.getRepository();
        if(gameController.board.getGameId() == null)
        {
            repository.createGameInDB(gameController.board);
            System.out.println("New save created with ID " + gameController.board.getGameId());
        }
        else
        {
            repository.updateGameInDB(gameController.board);
            System.out.println("Updated ID " + gameController.board.getGameId());
        }
    }

    /**
     * Load a game from the database
     * @author Tobias Hansen, s164270
     */
    public void loadGame() {
        //Prompt user for a game ID to load
        IRepository repository = RepositoryAccess.getRepository();
        List<GameInDB> savedGames = repository.getGames();

        List<Integer> ids = new ArrayList<>();
        for(GameInDB tempGame : savedGames)
        {
            ids.add(tempGame.id);
        }

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(ids.get(0), ids);
        dialog.setTitle("Select a game to load");
        dialog.setHeaderText("Load a game");
        Optional<Integer> result = dialog.showAndWait();

        System.out.println("You chose game " + result.get());
        for (Integer j : ids)
        {
            System.out.println("Game ID: " + j);
        }

        //load the saved game
        Board loadedBoard = repository.loadGameFromDB(result.get());

        //start the loaded game
        gameController = new GameController(loadedBoard);
        gameController.attach(this);
        roboRally.createBoardView(gameController);
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            //saveGame(); // here we save the game (without asking the user).

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Prompts the user and exits the application if they click OK
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * Display the game over screen and closes the current game.
     * @author Markus Visvaldis Ingemann Thieden, s164920
     * @author Tobias Hansen, s164270
     */
    public void winnerWinnerChickenDinner() {
        if (gameController != null) {

            int winnerIndex = 0;
            int curMax = 0;
            for (int i = 0; i < gameController.board.getPlayersNumber(); i++) {
                if (gameController.board.getPlayer(i).getNextCheckpoint() > curMax) {
                    winnerIndex = i;
                    curMax = gameController.board.getPlayer(i).getNextCheckpoint();
                }
            }

            Alert alert = new Alert(AlertType.INFORMATION); //CONFIRMATION
            alert.setTitle("Congratulationz you won");
            alert.setContentText(gameController.board.getPlayer(winnerIndex).getName() + " has won the game!");
            alert.showAndWait();

            stopGame();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        winnerWinnerChickenDinner();
    }

}
