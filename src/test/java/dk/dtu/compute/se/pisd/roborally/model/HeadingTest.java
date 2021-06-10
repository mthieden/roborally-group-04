package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeadingTest {

    @Test
    void next() {
        Assertions.assertEquals(Heading.NORTH.next(), Heading.EAST);
        Assertions.assertEquals(Heading.EAST.next(), Heading.SOUTH);
        Assertions.assertEquals(Heading.SOUTH.next(), Heading.WEST);
        Assertions.assertEquals(Heading.WEST.next(), Heading.NORTH);

    }

    @Test
    void prev() {
        Assertions.assertEquals(Heading.NORTH.prev(), Heading.WEST);
        Assertions.assertEquals(Heading.WEST.prev(), Heading.SOUTH);
        Assertions.assertEquals(Heading.SOUTH.prev(), Heading.EAST);
        Assertions.assertEquals(Heading.EAST.prev(), Heading.NORTH);
    }
}