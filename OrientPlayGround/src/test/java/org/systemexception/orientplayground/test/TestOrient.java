/**
 *
 * @author leo
 * @date 28/02/2015 20:03
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
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

	OrientGraphFactory orientGraphFactory;
	OrientGraphNoTx orientGraph;
	private final String dbPath = "target/orientdb_test_database";
	private final File dbFolder = new File(dbPath);

	@Before
	public void setUp() throws IOException {
		if (dbFolder.exists()) {
			System.out.println("Deleting previous database folder");
			dbFolder.delete();
		}
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getNoTx();
	}

	@After
	public void tearDown() {
		orientGraph.drop();
		orientGraph.shutdown();
		orientGraphFactory.close();
		System.out.println("Deleting test database");
		dbFolder.delete();
	}

	@Test
	public void has_a_graph_factory() {
		assertTrue(orientGraphFactory.exists());
	}

	@Test
	public void add_vertex() {
		Person person = new Person("John", "Doe", 40);
		Vertex vperson = addPersonVertex(person);
		System.out.println("Added record " + vperson.getId());
		Iterator<Vertex> vertexIterator = orientGraph.getVertices("name", "John").iterator();
		while (vertexIterator.hasNext()) {
			assertTrue(person.getName() == vertexIterator.next().getProperty("name"));
		}
	}

	@Test
	public void create_edge_for_vertex() {
		Person person1 = new Person("Foo", "Wombat", 35);
		Vertex vperson1 = addPersonVertex(person1);
		System.out.println("Added record " + vperson1.getId());
		Person person2 = new Person("Faa", "Wombat", 30);
		Vertex vperson2 = addPersonVertex(person2);
		System.out.println("Added record " + vperson2.getId());
		Edge edge = orientGraph.addEdge(null, vperson1, vperson2, "brothers");
		// Add a property to the edge, otherwise it will not be created. Nice feature.
		// see: https://github.com/orientechnologies/orientdb/wiki/Graph-Database-Tinkerpop
		edge.setProperty("type", "relation");
		System.out.println("Added edge " + edge.getId());
		for (Edge e : orientGraph.getEdges()) {
			System.out.println(e.getProperty("type").toString());
		}
		assertTrue(edge.getVertex(Direction.IN).equals(vperson2) && edge.getVertex(Direction.OUT).equals(vperson1));
	}

	private Vertex addPersonVertex(Person person) {
		Vertex vperson = orientGraph.addVertex(null);
		vperson.setProperty("name", person.getName());
		vperson.setProperty("surname", person.getSurname());
		vperson.setProperty("age", person.getAge());
		return vperson;
	}
}
