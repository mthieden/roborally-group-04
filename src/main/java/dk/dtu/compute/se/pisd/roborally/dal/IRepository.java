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
package dk.dtu.compute.se.pisd.roborally.dal;

import dk.dtu.compute.se.pisd.roborally.model.Board;

import java.util.List;

/**
 * An interface for saving and loading games to/from a database
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public interface IRepository {

	/**
	 * Create a new save in the database for the specified game
	 * @param game the game being saved
	 * @return true if successful
	 */
 	boolean createGameInDB(Board game);

	/**
	 * Update an existing save in the database for the specified game
	 * @param game the game being saved
	 * @return true if successful
	 */
	boolean updateGameInDB(Board game);

	/**
	 * Loads a game specified by the game id, if a save with that id exists in the database
	 * @param id the id of the game being loaded
	 * @return true if successful
	 */
	Board loadGameFromDB(int id);

	/**
	 * Get a list of all games saved in the database
	 * @return a list of games saved in the database
	 */
	List<GameInDB> getGames();

}
