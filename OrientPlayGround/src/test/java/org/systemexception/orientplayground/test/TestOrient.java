/**
 *
 * @author leo
 * @date 28/02/2015 20:03
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestOrient {

	private final Logger log = Logger.getLogger(TestOrient.class);
	OrientGraphFactory orientGraphFactory;
	Graph orientGraph;
	private final String dbPath = "target/orientdb";
	private final File dbFolder = new File(dbPath);

	@Before
	public void setUp() throws IOException {
		if (dbFolder.exists()) {
			log.info("Deleting previous database folder");
			dbFolder.delete();
		}
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getNoTx();
	}

	@After
	public void tearDown() {
		orientGraph.shutdown();
		orientGraphFactory.close();
		log.info("Deleting test database");
		dbFolder.delete();
	}

	@Test
	public void has_a_graph_factory() {
		assertTrue(orientGraphFactory.exists());
	}
}
