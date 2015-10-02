package evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import indexation.Parser;

public abstract class QueryParser {
	public abstract Query nextQuery();  
}

class QueryParserCISI_CACM extends QueryParser {

	private Parser parser;
	private HashMap<String, Query> queries;
	private Iterator<HashMap.Entry<String, Query>> iter;
	private HashMap<String, ArrayList<String>> relevantsDocsForQuery;
	
	QueryParserCISI_CACM(Parser parser) {
		this.parser = parser;
		this.iter = null;
	}
	
	public void init(String filenameQuery, String filenameRel)
	{
		this.parser.init(filenameQuery);
	}
	
	@Override
	public Query nextQuery() {
		if (this.iter == null)
			this.iter = queries.entrySet().iterator();
		return (Query)this.iter.next();
	}
}
