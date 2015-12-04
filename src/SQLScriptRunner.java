import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.*;

/**
 * A class to run SQL scripts in JAVA
 */
public class SQLScriptRunner {

	private static final String SQL_DEFAULT_DELIMITER = ";";
	private Connection conn;
	private boolean stopOnError;
	private boolean autoCommit;
	private String delimiter = SQL_DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;

	/**
	 * Default constructor that takes a connection and 2 booleans
	 * @param conn - a connection
	 * @param autoCommit - autoCommit on or off
	 * @param stopOnError - stop on error or not
	 */
	public SQLScriptRunner(Connection conn, boolean autoCommit, boolean stopOnError) {
		this.conn = conn;
		this.autoCommit = autoCommit;
		this.stopOnError = stopOnError;
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		this.delimiter = delimiter;
		this.fullLineDelimiter = fullLineDelimiter;
	}

	/**
	 * Runs an SQL script (read in using the Reader parameter)
	 * @param reader - the source of the script
	 */
	public void runScript(Reader reader) throws IOException, SQLException {
		try {
			boolean autoCommit = conn.getAutoCommit();
			try {
				if (autoCommit != this.autoCommit) {
					conn.setAutoCommit(this.autoCommit);
				}
				runScript(conn, reader);
			} finally {
				conn.setAutoCommit(autoCommit);
			}
		} catch (IOException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error running script.  Cause: " + e, e);
		}
	}

	/**
	 * Runs an SQL script (read in using the Reader parameter) using the
	 * connection passed in
	 * 
	 * @param conn - the connection to use for the script
	 * @param reader - the source of the script
	 * @throws SQLException if any SQL errors occur
	 * @throws IOException if there is an error reading from the Reader
	 */
	private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
		StringBuffer command = null;
		try {
			LineNumberReader lineReader = new LineNumberReader(reader);
			String line = null;

			// iterate through the script and execute each SQL
			while ((line = lineReader.readLine()) != null) {
				if (command == null) {
					command = new StringBuffer();
				}

				// trim any leading and trailing blanks
				String trimmedLine = line.trim();

				if (trimmedLine.startsWith("--")) { // process comment lines
					println(trimmedLine);
				} else if (trimmedLine.length() < 1
						|| trimmedLine.startsWith("//")) { // process comment lines
					// Do nothing
				} else if (trimmedLine.length() < 1
						|| trimmedLine.startsWith("--")) { // process comment lines
					// Do nothing
				} else if (!fullLineDelimiter
						&& trimmedLine.endsWith(getDelimiter())
						|| fullLineDelimiter
						&& trimmedLine.equals(getDelimiter())) {
					command.append(line.substring(0, line
							.lastIndexOf(getDelimiter())));
					command.append(" ");

					//Statement statement = conn.createStatement();
					Statement statement = JDBCUtil.getStatement(conn);

					println(command);

					boolean hasResults = false;
					if (stopOnError) {
						hasResults = statement.execute(command.toString());
					} else {
						try {
							statement.execute(command.toString());
						} catch (SQLException e) {
							e.fillInStackTrace();
							printlnError("Error executing: " + command);
							printlnError(e);
						}
					}

					if (autoCommit && !conn.getAutoCommit()) {
						conn.commit();
					}

					ResultSet rs = statement.getResultSet();

					// print the result set 
					if (hasResults && rs != null) {
						ResultSetMetaData md = rs.getMetaData();
						int cols = md.getColumnCount();
						for (int i = 0; i < cols; i++) {
							String name = md.getColumnLabel(i);
							print(name + "\t");
						}
						println("");
						while (rs.next()) {
							for (int i = 0; i < cols; i++) {
								String value = rs.getString(i);
								print(value + "\t");
							}
							println("");
						}
					}

					command = null;
					try {
						//statement.close();
						JDBCUtil.closeStatement(statement);
					} catch (Exception e) {
						// Ignore to workaround a bug in Jakarta DBCP
					}
					Thread.yield();
				} else {
					command.append(line);
					command.append(" ");
				}
			}

			if (!autoCommit) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.fillInStackTrace();
			printlnError("Error executing: " + command);
			printlnError(e);
			throw e;
		} catch (IOException e) {
			e.fillInStackTrace();
			printlnError("Error executing: " + command);
			printlnError(e);
			throw e;
		} finally {
			conn.rollback();
		}
	}

	/**
	 * Get the delimiter of the parser
	 * @return the delimiter
	 */
	private String getDelimiter() {
		return delimiter;
	}

	/**
	 * Print an object to system out
	 * @param o - object to print
	 */
	private void print(Object o) {
		System.out.print(o);
	}

	/**
	 * Println an object to system out
	 * @param o - object to print
	 */
	private void println(Object o) {
		System.out.println(o);
	}

	/**
	 * Println an object to system err
	 * @param o - object to print
	 */
	private void printlnError(Object o) {
		System.err.println(o);
	}
}
