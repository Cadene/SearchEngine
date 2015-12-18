package evaluation;

import indexation.Document;
import indexation.Parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class QueryParserCISI_CACM extends QueryParser {

	private HashMap<String, Query> queries;
	private Iterator<HashMap.Entry<String, Query>> iter;
	
	public QueryParserCISI_CACM() {
		this.iter = null;
		this.queries = new HashMap<String, Query>();
	}
	
	@Override
	public Query nextQuery() {
		if (this.iter.hasNext())
			return this.iter.next().getValue();
		return null;
	}
	
	public void init(String filenameQuery, String filenameRel){
		this.parseQuery(filenameQuery);
		this.parseRel(filenameRel);
		this.iter = queries.entrySet().iterator();
	}
	
	private void parseQuery(String filenameQuery){
		Parser parser = Parser.getParserCISI_CACM();
		parser.init(filenameQuery);
		while( true ){
			Document doc = parser.nextDocument();
			if (doc == null) break;
			queries.put(doc.getId(), new Query(doc));
		}
	}
	
	private void parseRel(String filenameRel){
		try {
			FileInputStream fileInputStream = new FileInputStream(filenameRel);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				String[] fields = line.replaceFirst("^\\D+","").split("\\s+");
				/* for (int i=0; i<fields.length; i++){
					//System.out.println(fields[i] + " -> " + fields[i].length());
				} */		
				String id_query = new Integer(fields[0]).toString();
				String id_doc = new Integer(fields[1]).toString();
				if (fields.length > 2){
					Double relevance = new Double(fields[2]);
					Integer id_subtopic = new Integer(fields[3]);
					queries.get(id_query).putRelevants(id_doc, relevance, id_subtopic);
				}
				else{
					queries.get(id_query).putRelevants(id_doc, 1.0);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String path = "/Users/remicadene/Documents/DAC/RI/SearchEngine/";
		QueryParserCISI_CACM parser = new QueryParserCISI_CACM();
		parser.init(path+"cacm/cacm.qry", path+"cacm/cacm.rel");
		//parser.parse(path+"cisi/cisi.qry", path+"cisi/cisi.rel");
		//parser.parseRel(path+"cacm/cacm.rel");
		//parser.parseRel(path+"cisi/cisi.rel");
		
		// test Query
		Query query = parser.nextQuery();
		while (query != null) {
			System.out.println(query.getId());
			System.out.println(query.getText());
			for (HashMap.Entry<String, Double> entry : query.getRelevants().entrySet()){
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			query = parser.nextQuery();
		}
	}
}