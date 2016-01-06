package diversite;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/* Load a dataset */
		File f = new File("iris.data");
		FileHandler.loadDataset(f, 4, ",");
		Dataset data = FileHandler.loadDataset(f, 4, ",");
		/* Create a new instance of the KMeans algorithm, with no options
		  * specified. By default this will generate 4 clusters. */
		Clusterer km = new KMeans();
		/* Cluster the data, it will be returned as an array of data sets, with
		  * each dataset representing a cluster. */
		Dataset[] clusters = km.cluster(data);
		System.out.println(clusters[0]);
		System.out.println(clusters[1]);
		System.out.println(clusters[2]);
		System.out.println(clusters[3]);
	}
}
