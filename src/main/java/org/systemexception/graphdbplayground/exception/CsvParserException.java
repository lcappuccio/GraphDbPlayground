/**
 * @author leo
 * @date 01/03/2015 16:48
 */
package org.systemexception.graphdbplayground.exception;

public class CsvParserException extends Exception {

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
