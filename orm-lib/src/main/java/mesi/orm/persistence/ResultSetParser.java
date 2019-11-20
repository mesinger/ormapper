package mesi.orm.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

interface ResultSetParser {
    Object parseObjectFromResultSet(String name, ResultSet rs, Class cls) throws SQLException;
}
