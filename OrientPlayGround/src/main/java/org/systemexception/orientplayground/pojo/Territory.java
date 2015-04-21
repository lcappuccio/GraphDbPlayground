/**
 *
 * @author leo
 * @date 01/03/2015 17:15
 *
 */
package org.systemexception.orientplayground.pojo;

import java.util.Objects;

public class Territory {

	private final String parentId, nodeId, nodeDescr, nodeType;

	public Territory(String parentId, String nodeId, String nodeDescr, String nodeType) {
		this.parentId = parentId;
		this.nodeId = nodeId;
		this.nodeDescr = nodeDescr;
		this.nodeType = nodeType;
	}

	public String getParentId() {
		return parentId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public String getNodeDescr() {
		return nodeDescr;
	}

	public String getNodeType() {
		return nodeType;
	}
}
