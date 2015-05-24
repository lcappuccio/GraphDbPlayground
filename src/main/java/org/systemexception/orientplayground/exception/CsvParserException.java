/**
 *
 * @author leo
 * @date 01/03/2015 16:48
 *
 */
package org.systemexception.orientplayground.exception;

public class CsvParserException extends Exception {

	private static final long serialVersionUID = 223605635294014864L;

	/**
	 * Creates a new instance of <code>CsvParserException</code> without detail message.
	 */
	public CsvParserException() {
	}

	/**
	 * Constructs an instance of <code>CsvParserException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public CsvParserException(String msg) {
		super(msg);
	}
}
