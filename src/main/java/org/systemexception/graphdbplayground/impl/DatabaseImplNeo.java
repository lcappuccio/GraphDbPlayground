/**
 * @author leo
 * @date 16/09/15 15:53
 */
package org.systemexception.graphdbplayground.impl;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;
import org.apache.commons.csv.CSVRecord;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;
import org.neo4j.kernel.impl.util.FileUtils;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.enums.ErrorCodes;
import org.systemexception.graphdbplayground.enums.OrientConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.pojo.CsvParser;
import org.systemexception.graphdbplayground.pojo.Territories;
import org.systemexception.graphdbplayground.pojo.Territory;
import org.systemexception.graphdbplayground.pojo.Timer;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseImplNeo extends DatabaseImplDefault {

	private Neo4jGraph graph;
	private String dbFolder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialSetup(String dbFolder, String storageType) {
		this.dbFolder = dbFolder;
		graph = new Neo4jGraph(dbFolder);
		super.graph = graph;
		index = graph.createIndex(OrientConfiguration.VERTEX_INDEX.toString(), Vertex.class, new Parameter("analyzer",
				LowerCaseKeywordAnalyzer.class.getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportDatabase(String exportFileName) {

		try {
			OutputStream outStream = new FileOutputStream(exportFileName);
			GraphSONWriter.outputGraph(graph, outStream);
		} catch (IOException e) {
			logger.error("Error in export", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void backupDatabase(String backupFileName) {
		exportDatabase(backupFileName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop() {
		graph.shutdown();
		try {
			FileUtils.deleteRecursively(new File(dbFolder));
		} catch (IOException e) {
			logger.error("Could not delete database folder", e);
		}
	}
}
