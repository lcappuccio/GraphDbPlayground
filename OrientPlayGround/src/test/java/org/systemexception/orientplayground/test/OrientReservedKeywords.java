/**
 * $Id$
 *
 * @author lcappuccio
 * @date 26/03/2015 17:03
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.orientplayground.enums.OrientConfiguration;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.impl.OrientActionImpl;

public class OrientReservedKeywords {

	private static OrientActionImpl sut;
	private static IndexableGraph graph;
	private final static String dbName = "test_keywords";

	@BeforeClass
	public static void setUp() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
		graph = sut.getGraph();
	}

	@AfterClass
	public static void tearDown() {
	}

	@Test
	public void add_DISTINCT_vertex() {
		Vertex distinctNamedVertex = graph.addVertex(OrientConfiguration.VERTEX_TERRITORY_CLASS.toString());
		distinctNamedVertex.setProperty(OrientConfiguration.NODE_ID.toString(), "1");
		distinctNamedVertex.setProperty(OrientConfiguration.NODE_DESC.toString(), "DISTINCT");
		distinctNamedVertex.setProperty(OrientConfiguration.NODE_TYPE.toString(), "SomeName");
		Iterable<Vertex> vertexIterable = graph.getVertices(OrientConfiguration.NODE_ID.toString(), "1");
		Iterator<Vertex> vertexIterator = vertexIterable.iterator();
		if (vertexIterator.hasNext()) {
			assertTrue(vertexIterator.next().getProperty(OrientConfiguration.NODE_ID.toString()) == "1");
		}
	}

}
