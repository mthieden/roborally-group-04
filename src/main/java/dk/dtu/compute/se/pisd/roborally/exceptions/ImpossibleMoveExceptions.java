package dk.dtu.compute.se.pisd.roborally.exceptions;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class ImpossibleMoveExceptions extends Exception
{
    private Player player;
    private Space space;
    private Heading heading;

    /**
     *
     * @param player robot trying to make an impossible move
     * @param space space it was trying to move to
     * @param heading direction it was heading in
     */
    public ImpossibleMoveExceptions(Player player, Space space, Heading heading )
    {
        super("Cant make that move mate!");
        this.player=player;
        this.space=space;
        this.heading=heading;
    }
}
