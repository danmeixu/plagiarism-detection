import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author danmeixu
 *
 */
public class DocumentsProcessor implements IDocumentsProcessor {

	@Override

	public Map<String, List<String>> processDocuments(String directoryPath, int n) {
		Map<String, List<String>> result = new HashMap<>();
		try {
			if (directoryPath == null || n <= 0) {
				throw new IllegalArgumentException();
			}
			File folder = new File(directoryPath);
			File[] fileList = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String str) {
					return !str.equals(".DS_Store");
				}
			});
			for (File file : fileList) {
				List<String> entry = new ArrayList<>();
				Reader r = new FileReader(file);
				BufferedReader br = new BufferedReader(r);
				DocumentIterator di = new DocumentIterator(br, n);

				while (di.hasNext()) {
					String str = di.next();
					if (!str.equals("")) {
						entry.add(str.toLowerCase());
					}
				}
				result.put(file.getName(), entry);
				r.close();
				br.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (NoSuchElementException e) {
			System.out.println("No such element found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public List<Tuple<String, Integer>> storeNWordSequences(Map<String, List<String>> docs, String nwordFilePath) {

		List<Tuple<String, Integer>> ret = new ArrayList<>();

		try {
			if (nwordFilePath == null) {
				throw new IllegalArgumentException();
			}
			File nwordFile = new File(nwordFilePath);
			FileWriter raf = new FileWriter(nwordFile);

			for (Map.Entry<String, List<String>> entry : docs.entrySet()) {
				int len = 0;
				for (String s : entry.getValue()) {
					raf.write(s);
					raf.write(" ");
					len += (s.length() + 1);
				}
				ret.add(new Tuple<>(entry.getKey(), len));
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * @param directoryPath the path to the document folder
	 * @param sequenceFile  the path to the N-word sequences files
	 * @param n             the number of words in the sequence
	 * @return n word file The method will not create a Map associating each file to
	 *         its n-word sequences. It will store the sequences directly in the
	 *         sequence file. The method will return the List of tuples of files and
	 *         their size in sequenceFile.
	 */
	public List<Tuple<String, Integer>> processAndStore(String directoryPath, String sequenceFile, int n) {
		List<Tuple<String, Integer>> ret = new ArrayList<>();
		try {
			if (directoryPath == null || sequenceFile == null || n <= 0) {
				throw new IllegalArgumentException();
			}
			File folder = new File(directoryPath);
			File[] fileList = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String str) {
					return !str.equals(".DS_Store");
				}
			});
			File nwordFile = new File(sequenceFile);
			FileWriter raf = new FileWriter(nwordFile);

			for (File file : fileList) {
				int len = 0; 
				Reader r = new FileReader(file);
				BufferedReader br = new BufferedReader(r);
				DocumentIterator di = new DocumentIterator(br, n);

				while (di.hasNext()) {
					String tmp = di.next().toLowerCase();
					if (tmp.length() != 0) {
						raf.write(tmp);
						raf.write(" ");
						len += (tmp.length() + 1);
					}
				}
				ret.add(new Tuple<>(file.getName(), len));
				r.close();
				br.close();
			}
			raf.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (NoSuchElementException e) {
			System.out.println("No such element found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public TreeSet<Similarities> computeSimilarities(String nwordFilePath, List<Tuple<String, Integer>> fileindex) {
		TreeSet<Similarities> result = new TreeSet<>();
		ArrayList<HashSet<String>> fileList = new ArrayList<>();

		try {
			if (nwordFilePath == null || fileindex == null || fileindex.size() == 0) {
				throw new IllegalArgumentException();
			}
			File nwordFile = new File(nwordFilePath);
			RandomAccessFile raf = new RandomAccessFile(nwordFile, "r");

			for (Tuple<String, Integer> tuple : fileindex) {
				byte[] file = new byte[(int) tuple.getRight()];
				raf.read(file);
				String currentFile = new String(file);
				HashSet<String> curSet = new HashSet<>();
				String[] tmp = currentFile.split(" ");
				List<String> tmpList = Arrays.asList(tmp);
				curSet.addAll(tmpList);
				fileList.add(curSet);
			} 

			int len = fileList.size();

			for (int i = 0; i < len - 1; i++) {
				for (int j = i + 1; j < len; j++) {
					HashSet<String> file1 = fileList.get(i);
					HashSet<String> file2 = fileList.get(j);
					HashSet<String> existingWords = new HashSet<>();
					Similarities sim = new Similarities(fileindex.get(i).getLeft(), fileindex.get(j).getLeft());
					result.add(sim);
					for (String s : file1) {
						if (file2.contains(s) && !existingWords.contains(s)) {
							existingWords.add(s);
							sim.setCount(sim.getCount() + 1);
						}
					}
				}
			}
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void printSimilarities(TreeSet<Similarities> sims, int threshold) {
		if (threshold < 0) {
			throw new IllegalArgumentException();
		}
		Comparator<Similarities> comp = new Comparator<Similarities>() {
			@Override
			public int compare(Similarities s1, Similarities s2) {
				return s2.getCount() - s1.getCount();
			}
		};
		TreeSet<Similarities> finalSet = new TreeSet<>(comp);

		for (Similarities sim : sims) {
			if (sim.getCount() > threshold) {
				finalSet.add(sim);
			}
		}

		for (Similarities sim : finalSet) {
			System.out.println(sim.getFile1() + " " + sim.getFile2() + " " + sim.getCount());
		}
	}

}
