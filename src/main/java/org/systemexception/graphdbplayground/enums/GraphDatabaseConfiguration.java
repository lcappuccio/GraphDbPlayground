/**
 * @author leo
 * @date 06/03/2015 20:59
 */
package org.systemexception.graphdbplayground.enums;

public enum GraphDatabaseConfiguration {

	ORIENT_DB_STORAGE_MEMORY("memory"),
	ORIENT_DB_STORAGE_DISK("plocal"),
	NEO_INDEX_PARAMETER("exact"),
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

	GraphDatabaseConfiguration(String orientConfiguration) {
		this.orientConfiguration = orientConfiguration;
	}

	@Override
	public String toString() {
		return orientConfiguration;
	}

}
