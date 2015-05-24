/**
 *
 * @author leo
 * @date 06/03/2015 21:13
 *
 */
package org.systemexception.orientplayground.enums;

public enum CsvHeaders {

	NODE_ID("NODE_ID"),
	PARENT_ID("PARENT_ID"),
	DESCRIPTION("DESCRIPTION"),
	TYPE("TYPE");

	private final String csvHeader;

	CsvHeaders(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	@Override
	public String toString() {
		return csvHeader;
	}

}
