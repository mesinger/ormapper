package mesi.orm.persistence;

import mesi.orm.exception.ORMesiException;
import mesi.orm.exception.ORMesiFetchException;

import java.sql.ResultSet;
import java.sql.SQLException;

class SQLiteResultSetParser implements ResultSetParser{
    @Override
    public Object parseObjectFromResultSet(String name, ResultSet rs, Class cls) throws SQLException {

        if(cls.equals(int.class) || cls.equals(Integer.class)) {
            return rs.getInt(name);
        }
        else if(cls.equals(long.class) || cls.equals(Long.class)) {
            return rs.getLong(name);
        }
        else if(cls.equals(float.class) || cls.equals(Float.class)) {
            return rs.getFloat(name);
        }
        else if(cls.equals(double.class) || cls.equals(Double.class)) {
            return rs.getDouble(name);
        }
        else if(cls.equals(String.class)) {
            return rs.getString(name);
        }
        else {
            if(PersistenceManager.isObjectPersistent(cls)) {
                return null;
            }
            else {
                throw new ORMesiFetchException("unsupported class type");
            }
        }
    }
}
