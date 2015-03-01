/**
 *
 * @author leo
 * @date 01/03/2015 18:25
 *
 */
package org.systemexception.orientplayground.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.systemexception.orientplayground.exception.TerritoriesException;

public class Territories {

	private final List<Territory> territories;

	public Territories() {
		territories = new ArrayList();
	}

	public void addTerritory(Territory territory) throws TerritoriesException {
		for (Territory territorie : territories) {
			if (territory.getNodeId().equals(territorie.getNodeId())) {
				throw new TerritoriesException("Territory already exists");
			}
		}
		territories.add(territory);
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.territories);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Territories other = (Territories) obj;
		if (!Objects.equals(this.territories, other.territories)) {
			return false;
		}
		return true;
	}

}
