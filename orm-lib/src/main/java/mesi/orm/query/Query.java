package mesi.orm.query;

/**
 * base class for building sql statements
 */
public abstract class Query {
    protected StringBuilder head = new StringBuilder();
    protected StringBuilder tail = new StringBuilder();

    /**
     * @return raw representation of the built sql statement
     */
    public String raw() {
        return head.toString() + tail.toString();
    }
}
