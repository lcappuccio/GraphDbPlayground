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
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestOrient {

	OrientGraphFactory orientGraphFactory;
	Graph orientGraph;
	private final String dbPath = "target/orientdb";

	@Before
	public void setUp() throws IOException {
		FileUtils.delete(new File(dbPath));
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getNoTx();
	}

	@After
	public void tearDown() {
		FileUtils.delete(new File(dbPath));
	}

	@Test
	public void has_a_graph_factory() {
		assertTrue(orientGraphFactory.exists());
	}
}
