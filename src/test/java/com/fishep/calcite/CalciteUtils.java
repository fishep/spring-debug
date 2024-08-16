package com.fishep.calcite;

import org.apache.calcite.adapter.jdbc.JdbcTableScan;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CalciteUtils {

    /**
     * 获取{@link CalciteConnection}
     *
     * @param modelPath
     * @return
     * @throws SQLException
     */
    public static CalciteConnection getConnection(String modelPath) throws SQLException {
        Properties properties = new Properties();
        properties.put("model", modelPath);
        properties.put("lex", "MYSQL");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", properties);
        return connection.unwrap(CalciteConnection.class);
    }

    /**
     * 获取一个规划器{@link Planner}
     * <p><b>{@link Planner}不是线程安全的</b></p>
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public static Planner getPlanner(CalciteConnection connection) throws SQLException {
        SchemaPlus rootSchema = connection.getRootSchema();
        FrameworkConfig config = Frameworks.newConfigBuilder()
            .defaultSchema(rootSchema.getSubSchema(connection.getSchema()))
            .parserConfig(SqlParser.Config.DEFAULT
                .withLex(Lex.MYSQL))
            .build();
        return Frameworks.getPlanner(config);
    }

    /**
     * SQL校验
     *
     * @param planner
     * @param sql
     * @return
     * @throws SqlParseException
     * @throws ValidationException
     */
    public static SqlNode getValidatedSqlNode(Planner planner, String sql) throws SqlParseException, ValidationException {
        SqlNode sqlNode = planner.parse(sql);
        return planner.validate(sqlNode);
    }

    /**
     * SQL执行计划
     *
     * @param planner
     * @param validateSqlNode
     * @return
     * @throws RelConversionException
     */
    public static RelNode getBasicRelNode(Planner planner, SqlNode validateSqlNode) throws RelConversionException {
        RelRoot relRoot = planner.rel(validateSqlNode);
        return relRoot.project();
    }

    /**
     * SQL执行计划（谓词下推）
     *
     * @param planner
     * @param validateSqlNode
     * @return
     * @throws RelConversionException
     */
    public static RelNode getOptimizerRelNode(Planner planner, SqlNode validateSqlNode) throws RelConversionException {
        RelNode basicRelNode = getBasicRelNode(planner, validateSqlNode);
        return getOptimizerRelNode(basicRelNode);
    }

    /**
     * SQL执行计划（谓词下推）
     *
     * @param basicRelNode
     * @return
     */
    public static RelNode getOptimizerRelNode(RelNode basicRelNode) {
        HepProgramBuilder builder = new HepProgramBuilder();
        builder.addRuleInstance(CoreRules.FILTER_INTO_JOIN);
        HepPlanner hepPlanner = new HepPlanner(builder.build());
        hepPlanner.setRoot(basicRelNode);
        return hepPlanner.findBestExp();
    }

    /**
     * 关系代数中的表
     *
     * @param project
     * @return
     */
    public static Set<JdbcTableScan> getTables(RelNode project) {
        Set<JdbcTableScan> tables = new HashSet<>();
        if (project instanceof JdbcTableScan) {
            tables.add((JdbcTableScan) project);
        }
        for (RelNode input : project.getInputs()) {
            tables.addAll(getTables(input));
        }
        return tables;
    }

}
