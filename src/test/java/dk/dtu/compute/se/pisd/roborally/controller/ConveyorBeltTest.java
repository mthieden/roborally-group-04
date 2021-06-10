package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConveyorBeltTest {

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

        ConveyorBelt belt1 = new ConveyorBelt(Heading.EAST);
        board.getSpace(2, 1).getFieldActions().add(belt1);
        ConveyorBelt belt2 = new ConveyorBelt(Heading.WEST);
        board.getSpace(3, 1).getFieldActions().add(belt2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void doAction() {
        Assertions.assertEquals(player.getSpace().x, 2);
        Assertions.assertEquals(player.getSpace().y, 1);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }

        Assertions.assertEquals(player.getSpace().x, 3);
        Assertions.assertEquals(player.getSpace().y, 1);

        for (FieldAction action : player.getSpace().getFieldActions()) {
            action.doAction(gameController, player.getSpace());
        }

        Assertions.assertEquals(player.getSpace().x, 2);
        Assertions.assertEquals(player.getSpace().y, 1);
    }
}