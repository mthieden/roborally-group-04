package dk.dtu.compute.se.pisd.roborally.exceptions;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class ImpossibleMoveExceptions extends Exception
{
    private Player player;
    private Space space;
    private Heading heading;

    public ImpossibleMoveExceptions(Player player, Space space, Heading heading )
    {
        super("Cant make that move mate!");
        this.player=player;
        this.space=space;
        this.heading=heading;
    }
}
