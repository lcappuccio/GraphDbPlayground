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

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 59 * hash + Objects.hashCode(this.parentId);
		hash = 59 * hash + Objects.hashCode(this.nodeId);
		hash = 59 * hash + Objects.hashCode(this.nodeDescr);
		hash = 59 * hash + Objects.hashCode(this.nodeType);
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
		final Territory other = (Territory) obj;
		if (!Objects.equals(this.parentId, other.parentId)) {
			return false;
		}
		if (!Objects.equals(this.nodeId, other.nodeId)) {
			return false;
		}
		if (!Objects.equals(this.nodeDescr, other.nodeDescr)) {
			return false;
		}
		if (!Objects.equals(this.nodeType, other.nodeType)) {
			return false;
		}
		return true;
	}
}
