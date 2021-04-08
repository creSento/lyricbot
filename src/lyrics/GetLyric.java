package lyrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetLyric {
	private CsvWrite csvwriter = new CsvWrite();
	private SongReader sr = new SongReader();
	private Analyze ana = new Analyze();
	private String songId = "";
	private int score = 0;
	private String lyric = "";
	private ArrayList<String> rankedList = new ArrayList<String>();
	private HashMap<String, Integer> normalWordList = new HashMap<String, Integer>();
	private HashMap<String, Integer> weightedWordList = new HashMap<String, Integer>();
	private final int WORKUNIT = 300;

	public void getLylicRun() throws IOException {
		rankedList = sr.rankSongID();
		System.out.println("rankedList : " + rankedList.size());

		int totalSize = rankedList.size();
		int breakPoint = totalSize / WORKUNIT;
		for (int i = 1; i < breakPoint + 1; i++) {
			if (i < breakPoint) {
				doJob(WORKUNIT * i, WORKUNIT * (i + 1));
				try {
					Thread.sleep(1000 * 60 * 5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				doJob(WORKUNIT * i, totalSize);
			}
		}

	}

	public void doJob(int startPoint, int breakPoint) throws IOException {
		String[] temp = null;
		for (int i = startPoint; i < breakPoint; i++) {
			temp = rankedList.get(i).split("#");
			songId = temp[0];
			score = Integer.parseInt(temp[1]);
			lyric = sr.getLyric(songId);
			ana.runWork(lyric, normalWordList);
			ana.runWork(lyric, score, weightedWordList);	// weighted score
			System.out.println(i);
			csvwriter.writeFile(Files.LOGFILE, i + "," + songId + "\n");
		}
		ana.writeWordFile(Files.NORMALWORDFILE, normalWordList);
		ana.writeWordFile(Files.WEIGHTWORDFILE, weightedWordList);
		System.out.println("wordList : " + normalWordList.size());
		System.out.println("wordList : " + weightedWordList.size());
	}
}
