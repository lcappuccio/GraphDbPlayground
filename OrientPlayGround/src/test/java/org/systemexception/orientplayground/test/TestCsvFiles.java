/**
 *
 * @author leo
 * @date 01/03/2015 14:46
 *
 */
package org.systemexception.orientplayground.test;

import java.io.File;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestCsvFiles {

	private static final String testPath = "target/territories/";

	@Test
	public void findTerritoriesIT() {
		File territoriesIT = new File(testPath + "geonames_it.csv");
		assertTrue(territoriesIT.exists());
	}

	@Test
	public void findTerritoriesCH() {
		File territoriesIT = new File(testPath + "geonames_ch.csv");
		assertTrue(territoriesIT.exists());
	}

	@Test
	public void findTerritoriesES() {
		File territoriesIT = new File(testPath + "geonames_es.csv");
		assertTrue(territoriesIT.exists());
	}

}
