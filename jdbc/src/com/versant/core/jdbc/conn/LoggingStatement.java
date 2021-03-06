
/*
 * Copyright (c) 1998 - 2005 Versant Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Versant Corporation - initial API and implementation
 */
package com.versant.core.jdbc.conn;

import com.versant.core.logging.LogEventStore;
import com.versant.core.jdbc.logging.JdbcStatementEvent;
import com.versant.core.jdbc.logging.JdbcLogEvent;
import com.versant.core.logging.LogEventStore;
import com.versant.core.logging.LogEventStore;

import java.sql.*;

/**
 * A JDBC Statement wrapped for event logging.
 */
public class LoggingStatement implements Statement {

    protected final LoggingConnection con;
    private final Statement statement;
    protected final LogEventStore pes;

    public LoggingStatement(LoggingConnection con, Statement statement,
            LogEventStore pes) {
        this.con = con;
        this.statement = statement;
        this.pes = pes;
    }

    /**
     * Get the real statement.
     */
    public Statement getStatement() {
        return statement;
    }

    public boolean getMoreResults(int current) throws SQLException {
        return statement.getMoreResults(current);
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeUpdate(sql, autoGeneratedKeys);
    }

    public int executeUpdate(String sql, int columnIndexes[]) throws SQLException {
        return statement.executeUpdate(sql, columnIndexes);
    }

    public int executeUpdate(String sql, String columnNames[]) throws SQLException {
        return statement.executeUpdate(sql, columnNames);
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.execute(sql, autoGeneratedKeys);
    }

    public boolean execute(String sql, int columnIndexes[]) throws SQLException {
        return statement.execute(sql, columnIndexes);
    }

    public boolean execute(String sql, String columnNames[]) throws SQLException {
        return statement.execute(sql, columnNames);
    }

    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if (!pes.isFine()) return statement.executeQuery(sql);
        JdbcStatementEvent ev = new JdbcStatementEvent(0, this,
            sql, JdbcStatementEvent.STAT_EXEC_QUERY);
        pes.log(ev);
        try {
            ResultSet rs = statement.executeQuery(sql);
            ev.updateResultSetID(rs);
            if (pes.isFiner()) rs = new LoggingResultSet(this, sql, rs, pes);
            return rs;
        } catch (SQLException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } finally {
            ev.updateTotalMs();
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        if (!pes.isFine()) {
            return statement.executeUpdate(sql);
        }
        JdbcStatementEvent ev = new JdbcStatementEvent(0, this,
            sql, JdbcStatementEvent.STAT_EXEC_UPDATE);
        pes.log(ev);
        try {
            int c = statement.executeUpdate(sql);
            ev.setUpdateCount(c);
            return c;
        } catch (SQLException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } finally {
            ev.updateTotalMs();
        }
    }

    public void close() throws SQLException {
        if (!pes.isFiner()) {
            statement.close();
            return;
        }
        JdbcStatementEvent ev = new JdbcStatementEvent(0, this,
            null, JdbcStatementEvent.STAT_CLOSE);
        pes.log(ev);
        try {
            statement.close();
        } catch (SQLException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } finally {
            ev.updateTotalMs();
        }
    }

    public int getMaxFieldSize() throws SQLException {
        return statement.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        statement.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        return statement.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        JdbcLogEvent ev = null;
        if (pes.isFiner()) {
            ev = new JdbcLogEvent(0,
                JdbcLogEvent.STAT_MAX_ROWS, Integer.toString(max));
            pes.log(ev);
        }
        try {
            statement.setMaxRows(max);
        } catch (SQLException e) {
            if (ev != null) ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            if (ev != null) ev.setErrorMsg(e);
            throw e;
        } finally {
            if (ev != null) ev.updateTotalMs();
        }
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        return statement.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        statement.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        return statement.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        statement.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        statement.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException {
        if (!pes.isFine()) return statement.execute(sql);
        JdbcStatementEvent ev = new JdbcStatementEvent(0, this,
            sql, JdbcStatementEvent.STAT_EXEC);
        pes.log(ev);
        try {
            boolean ans = statement.execute(sql);
            ev.setHasResultSet(ans);
            return ans;
        } catch (SQLException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } finally {
            ev.updateTotalMs();
        }
    }

    public java.sql.ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        return statement.getMoreResults();
    }

    public void setFetchDirection(int direction) throws SQLException {
        statement.setFetchDirection(direction);
    }

    public int getFetchDirection() throws SQLException {
        return statement.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException {
        statement.setFetchSize(rows);
    }

    public int getFetchSize() throws SQLException {
        return statement.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        return statement.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        return statement.getResultSetType();
    }

    public void addBatch(String sql) throws SQLException {
        statement.addBatch(sql);
    }

    public void clearBatch() throws SQLException {
        statement.clearBatch();
    }

    public int[] executeBatch() throws SQLException {
        if (!pes.isFine()) return statement.executeBatch();
        JdbcStatementEvent ev = new JdbcStatementEvent(0, this,
            getSql(), JdbcStatementEvent.STAT_EXEC_BATCH);
        pes.log(ev);
        try {
            int[] a = statement.executeBatch();
            ev.setUpdateCounts(a);
            return a;
        } catch (SQLException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } catch (RuntimeException e) {
            con.setNeedsValidation(true);
            ev.setErrorMsg(e);
            throw e;
        } finally {
            ev.updateTotalMs();
        }
    }

    /**
     * PreparedStatement subclasses override this to return the SQL for
     * event logging.
     */
    protected String getSql() {
        return null;
    }

    public Connection getConnection() throws SQLException {
        return con;
    }

}

