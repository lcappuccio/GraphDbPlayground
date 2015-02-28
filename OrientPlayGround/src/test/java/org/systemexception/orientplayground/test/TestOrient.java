/**
 *
 * @author leo
 * @date 28/02/2015 20:03
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.orientplayground.pojo.Person;

public class TestOrient {

	private final Logger log = Logger.getLogger(TestOrient.class);
	OrientGraphFactory orientGraphFactory;
	OrientGraph orientGraph;
	private final String dbPath = "target/orientdb";
	private final File dbFolder = new File(dbPath);

	@Before
	public void setUp() throws IOException {
		if (dbFolder.exists()) {
			log.info("Deleting previous database folder");
			dbFolder.delete();
		}
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getTx();
	}

	@After
	public void tearDown() {
		orientGraph.drop();
		orientGraph.shutdown();
		orientGraphFactory.close();
		log.info("Deleting test database");
		dbFolder.delete();
	}

	@Test
	public void has_a_graph_factory() {
		assertTrue(orientGraphFactory.exists());
	}

	@Test
	public void add_vertex() {
		Person person = new Person("John", "Doe", 30);
		Vertex vperson = orientGraph.addVertex("class:Person");
		vperson.setProperty("name", person.getName());
		vperson.setProperty("surname", person.getSurname());
		vperson.setProperty("age", person.getAge());

		Iterator<Vertex> vertexIterator = orientGraph.getVertices("name", "John").iterator();
		while (vertexIterator.hasNext()) {
			assertTrue(person.getName() == vertexIterator.next().getProperty("name"));
		}

	}
}
