import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @author danmeixu
 *
 */
public class DocumentsProcessorTest {

	/**
	 * create a new DocumentsProcessor object, and
	 * test process documents to map
	 */
	@Test
	public void testProcessDocuments() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/Desktop/test", 5);
		Map<String, List<String>> testMap = new HashMap<>();
		List<String> entry = new ArrayList<>();
		entry.add("abcde");
		entry.add("bcdef");
		entry.add("cdefg");
		testMap.put("test.txt", entry);
		List<String> entry2 = new ArrayList<>();
		entry2.add("cdefg");
		entry2.add("defgh");
		entry2.add("efghi");
		testMap.put("test2.txt", entry2);
		List<String> entry3 = new ArrayList<>();
		entry3.add("efghi");
		entry3.add("fghij");
		entry3.add("ghijk");
		testMap.put("test3.txt", entry3);
		assertEquals(map.size(), testMap.size());
		assertTrue(map.equals(testMap));
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null path and throws
	 * IllegalArgumentException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testProcessDocumentsException1() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map = d.processDocuments(null, 5);
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles n=0 and throws
	 * IllegalArgumentException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testProcessDocumentsException2() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map2 = d.processDocuments("/Users/danmeixu/Desktop/test", 0);
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test process documents to map IOException
	 */
	@Test(expected = IOException.class)
	public void testProcessDocumentsException3() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map2 = d.processDocuments("", 5);
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test store word sequences in map to list
	 */
	@Test
	public void testStoreNWordSequences() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/Desktop/test", 5);
		List<Tuple<String, Integer>> list = d.storeNWordSequences(map, "/Users/danmeixu/Desktop/result.txt");
		List<Tuple<String, Integer>> testList = new ArrayList<>();
		testList.add(new Tuple<String, Integer>("test.txt", 18));
		testList.add(new Tuple<String, Integer>("test2.txt", 18));
		testList.add(new Tuple<String, Integer>("test3.txt", 18));
		for (int i = 0; i < list.size(); i++) {
			assertTrue(list.get(i).getLeft().equals(testList.get(i).getLeft()));
			assertTrue(list.get(i).getRight().equals(testList.get(i).getRight()));
		}
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null path and throws
	 * IllegalArgumentException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStoreNWordSequences2() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/Desktop/test", 5);
		List<Tuple<String, Integer>> list = d.storeNWordSequences(map, null);
	}



	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles invalid path and throws
	 * IOException 
	 */
	@Test(expected = IOException.class)
	public void testStoreNWordSequences3() {
		DocumentsProcessor d = new DocumentsProcessor();
		Map<String, List<String>> map = d.processDocuments("/Users/danmeixu/Desktop/test", 5);
		List<Tuple<String, Integer>> list = d.storeNWordSequences(map, "");
	}


	/**
	 * create a new DocumentsProcessor object, and
	 * test if function processes and stores file word sequences
	 */
	@Test
	public void testProcessAndStore() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		List<Tuple<String, Integer>> testList = new ArrayList<>();
		testList.add(new Tuple<String, Integer>("test2.txt", 18));
		testList.add(new Tuple<String, Integer>("test3.txt", 18));
		testList.add(new Tuple<String, Integer>("test.txt", 18));
		assertTrue(list.get(0).getRight().equals(testList.get(0).getRight()));
		for (int i = 0; i < list.size(); i++) {
			assertTrue(list.get(i).getLeft().equals(testList.get(i).getLeft()));
			assertTrue(list.get(i).getRight().equals(testList.get(i).getRight()));
		}
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null path and throws
	 * IllegalArgumentException 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testProcessAndStoreException1() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore(null, "/Users/danmeixu/Desktop/result2.txt", 5);
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null path and throws
	 * IllegalArgumentException 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testProcessAndStoreException2() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", null, 5);
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles n=-1 and throws
	 * IllegalArgumentException 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testProcessAndStoreException3() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", -1);
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles invalid path and throws
	 * IOException 
	 */
	@Test (expected = FileNotFoundException.class)
	public void testProcessAndStoreException4() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("", "", 5);
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test if compute similarities based on word sequences 
	 * returns the correct result
	 */
	@Test
	public void testComputeSimilarities() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		TreeSet<Similarities> set = d.computeSimilarities("/Users/danmeixu/Desktop/result2.txt", list);
		TreeSet<Similarities> test = new TreeSet<>();
		Similarities sim1 = new Similarities("test2.txt", "test.txt");
		sim1.setCount(1);
		test.add(sim1);
		Similarities sim2 = new Similarities("test3.txt", "test.txt");
		test.add(sim2);
		Similarities sim3 = new Similarities("test2.txt", "test3.txt");
		sim3.setCount(1);
		test.add(sim3);
		assertTrue(set.equals(test));
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null path and throws
	 * IllegalArgumentException 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testComputeSimilaritiesException() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		TreeSet<Similarities> set = d.computeSimilarities(null, list);
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles null list input and throws
	 * IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testComputeSimilaritiesException2() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		TreeSet<Similarities> set = d.computeSimilarities("/Users/danmeixu", null);
	}

	/**
	 * create a new DocumentsProcessor object, and
	 * test if function handles n = -1 and throws
	 * IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPrintSimilaritiesException() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		TreeSet<Similarities> set = d.computeSimilarities("/Users/danmeixu/Desktop/result2.txt", list);
		d.printSimilarities(set, -1);
	}
	
	/**
	 * create a new DocumentsProcessor object, and
	 * test if print function works as designed
	 * to print similarities
	 */
	@Test
	public void testPrintSimilarities() {
		DocumentsProcessor d = new DocumentsProcessor();
		List<Tuple<String, Integer>> list = d.processAndStore("/Users/danmeixu/Desktop/test", "/Users/danmeixu/Desktop/result2.txt", 5);
		TreeSet<Similarities> set = d.computeSimilarities("/Users/danmeixu/Desktop/result2.txt", list);
		d.printSimilarities(set, 1);
	}

}
