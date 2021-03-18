package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public enum SpaceFunction {

    // This is a very simplistic way of realizing different commands.

    CHECKPOINT("check"),
    CONVEYORBELT("conveyor"),
    TURNINGPOINT("turn");

    final public String displayName;
    public Heading head;

    SpaceFunction(String displayName)
    {
        this.displayName = displayName;
        this.head = null;
    }

    SpaceFunction(String displayName, Heading head)
    {
        this.displayName = displayName;
        this.head = head;
    }

    public Heading getHeading()
    {
        return this.head;
    }

    public void setHeading(Heading head)
    {
        this.head = head;
    }
}
