package org.systemexception.graphdbplayground.enums;

/**
 * @author leo
 * @date 19/07/15 17:06
 */
public enum ErrorCodes {

	NODE_DOES_NOT_EXIST("Node does not exist"),
	NODE_HAS_NO_CHILDS("Node has no child nodes"),
	NODE_HAS_NO_PARENT("Node has no parent node");

	private final String errorCodes;

	ErrorCodes(String errorCodes) {
		this.errorCodes = errorCodes;
	}

	@Override
	public String toString() {
		return errorCodes;
	}
}
