/**
 *
 * @author leo
 * @date 06/03/2015 20:59
 *
 */
package org.systemexception.graphdbplayground.enums;

public enum OrientConfiguration {

	DB_STORAGE_MEMORY("memory"),
	DB_STORAGE_DISK("plocal"),
	VERTEX_TERRITORY_CLASS("class:Territory"),
	VERTEX_INDEX("vertexIndex"),
	NODE_ID("nodeId"),
	NODE_DESC("nodeDesc"),
	NODE_TYPE("nodeType"),
	REPORTS_TO("reportsTo"),
	EDGE_TYPE("edgeType"),
	EDGE_SOURCE_NODE("sourceNode"),
	EDGE_DESTINATION_NODE("destinationNode");

	private final String orientConfiguration;

	OrientConfiguration(String orientConfiguration) {
		this.orientConfiguration = orientConfiguration;
	}

	@Override
	public String toString() {
		return orientConfiguration;
	}

}
