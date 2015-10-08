/**
 * @author leo
 * @date 01/03/2015 18:25
 */
package org.systemexception.graphdbplayground.model;

import org.systemexception.graphdbplayground.exception.TerritoriesException;

import java.util.ArrayList;
import java.util.List;

public class Territories {

	private final List<Territory> territories;

	public Territories() {
		territories = new ArrayList();
	}

	public void addTerritory(Territory territory) throws TerritoriesException {
		for (Territory territory1 : territories) {
			if (territory.getNodeId().equals(territory1.getNodeId())) {
				throw new TerritoriesException("Territory already exists");
			}
		}
		territories.add(territory);
	}

	public List<Territory> getTerritories() {
		return territories;
	}
}
