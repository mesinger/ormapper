package mesi.orm.query;

public abstract class Query {
    protected StringBuilder head = new StringBuilder();
    protected StringBuilder tail = new StringBuilder();
    public String raw() {
        var query = head.append(tail);
        query.setLength(query.length() - 2);
        return query.toString() + "\n);";
    }
}
