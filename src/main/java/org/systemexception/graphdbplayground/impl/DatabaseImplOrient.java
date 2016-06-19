/**
 * @author leo
 * @date 01/03/2015 19:17
 */
package org.systemexception.graphdbplayground.impl;

import com.orientechnologies.common.util.OCallable;
import com.orientechnologies.orient.core.command.OCommandOutputListener;
import com.orientechnologies.orient.core.db.tool.ODatabaseExport;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DatabaseImplOrient extends DatabaseImplDefault {

	private OrientGraphNoTx graph;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialSetup(String dbName, String dbStorageType) {
		String dbPath = System.getProperty("user.dir") + File.separator + dbName;
		OrientGraphFactory orientGraphFactory = new OrientGraphFactory(dbStorageType + ":" + dbPath, "admin", "admin");
		graph = orientGraphFactory.getNoTx();
		super.graph = graph;
		graph.executeOutsideTx(new OCallable<Object, OrientBaseGraph>() {
			@Override
			public Object call(OrientBaseGraph arg0) {
				index = graph.createIndex(GraphDatabaseConfiguration.VERTEX_INDEX.toString(), Vertex.class);
				return null;
			}
		});
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportDatabase(String exportFileName) throws IOException {
        logger.info("Database export started");
        OCommandOutputListener listener = System.out::print;
        ODatabaseExport export = new ODatabaseExport(graph.getRawGraph(), exportFileName, listener);
        export.exportDatabase();
        export.close();
        logger.info("Database export completed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backupDatabase(String backupFileName) throws IOException {
        logger.info("Database backup started");
        if (graph.getRawGraph().getStorage().getType().equals(GraphDatabaseConfiguration.DB_STORAGE_MEMORY
                .toString())) {
            logger.error("Unsupported in-memory database backup");
            throw new UnsupportedOperationException();
        }
        OCommandOutputListener listener = System.out::print;
        OutputStream out = new FileOutputStream(backupFileName);
        graph.getRawGraph().backup(out, null, null, listener, 9, 2048);
        logger.info("Database backup completed");
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop() {
		graph.drop();
	}
}
