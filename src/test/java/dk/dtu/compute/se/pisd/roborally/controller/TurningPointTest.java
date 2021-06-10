package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurningPointTest {


    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;
    private GameController gameController;
    private Player player;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        player = new Player(board, null, "Player 1");
        board.addPlayer(player);
        player.setSpace(board.getSpace(2, 1));
        player.setHeading(Heading.EAST);
        board.setCurrentPlayer(player);

        TurningPoint turn1 = new TurningPoint(Heading.EAST);
        board.getSpace(2, 1).getFieldActions().add(turn1);
        TurningPoint turn2 = new TurningPoint(Heading.WEST);
        board.getSpace(3, 1).getFieldActions().add(turn2);
        //

        //board.getSpace(1, 1).getFieldActions().add()
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void doAction() {
        Assertions.assertEquals(player.getHeading(), Heading.EAST);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.SOUTH);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.WEST);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.NORTH);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.EAST);

        gameController.moveForward(player);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.NORTH);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.WEST);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.SOUTH);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }
        Assertions.assertEquals(player.getHeading(), Heading.EAST);
    }
}