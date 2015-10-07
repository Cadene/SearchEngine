package evaluation;

public abstract class QueryParser {
	public abstract Query nextQuery();
	public abstract void init(String filenameQuery, String filenameRel);
}
