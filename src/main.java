import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author danmeixu
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocumentsProcessor d = new DocumentsProcessor();
//		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/Desktop/test", 5);
//		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/OneDrive/Documents Surface/Courses/CIT 594/HW1/sm_doc_set", 2);
//		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//			System.out.println(entry.toString());
//		}
//		List<Tuple<String, Integer>> list = d.storeNWordSequences(map, "/Users/danmeixu/Desktop/result.txt");
//		for (Tuple<String, Integer> tuple : list) {
//			System.out.println(tuple.getLeft() + " " + tuple.getRight().toString());
//		}
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/OneDrive/Documents Surface/Courses/CIT 594/HW1/test.txt", 5);
//		for (Tuple<String, Integer> tuple : list) {
//		System.out.println(tuple.getLeft() + " " + tuple.getRight().toString());
//	}
		TreeSet<Similarities> similarity = d.computeSimilarities("/Users/danmeixu/OneDrive/Documents Surface/Courses/CIT 594/HW1/test.txt", list);
		for (Similarities sim : similarity) {
			System.out.println(sim.getFile1() + " " + sim.getFile2() + " " + sim.getCount());
		}
//		d.printSimilarities(similarity, 100);
	}
	
	//File folder = new File("/Users/danmeixu/Desktop/sm_doc_set");

}
