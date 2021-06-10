package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;
    private GameController gameController;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
    }

    @Test
    void getNeighbour() {

        Assertions.assertSame(board.getNeighbour(board.getSpace(4,4), Heading.NORTH), board.getSpace(4, 3));
        Assertions.assertSame(board.getNeighbour(board.getSpace(4,4), Heading.EAST), board.getSpace(5, 4));
        Assertions.assertSame(board.getNeighbour(board.getSpace(4,4), Heading.SOUTH), board.getSpace(4, 5));
        Assertions.assertSame(board.getNeighbour(board.getSpace(4,4), Heading.WEST), board.getSpace(3, 4));

        //Check wraparound
        Assertions.assertSame(board.getNeighbour(board.getSpace(0,0), Heading.NORTH), board.getSpace(0, 7));
        Assertions.assertSame(board.getNeighbour(board.getSpace(0,0), Heading.EAST), board.getSpace(1, 0));
        Assertions.assertSame(board.getNeighbour(board.getSpace(0,0), Heading.SOUTH), board.getSpace(0, 1));
        Assertions.assertSame(board.getNeighbour(board.getSpace(0,0), Heading.WEST), board.getSpace(7, 0));


    //    board.getNeighbour(board.getSpace(1,1), Heading.NORTH);
    }
}