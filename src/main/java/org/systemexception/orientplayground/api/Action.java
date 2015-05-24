/**
 *
 * @author leo
 * @date 01/03/2015 19:12
 *
 */
package org.systemexception.orientplayground.api;

import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;

public interface Action {

	/**
	 * Sets up the database on the specified folder
	 *
	 * @param dbName
	 */
	void initialSetup(String dbName);

	/**
	 * Reads all lines from a csv file and creates all nodes
	 *
	 * @param fileName
	 * @throws org.systemexception.orientplayground.exception.CsvParserException
	 * @throws org.systemexception.orientplayground.exception.TerritoriesException
	 */
	void addTerritories(String fileName) throws CsvParserException, TerritoriesException;

	/**
	 * Drops the database
	 */
	void drop();

}
