package lyrics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class SongReader {
	
	
	BufferedReader br = null;
	WebDriver driver;
	CsvWrite csvwriter = new CsvWrite();

	public ArrayList<String> rankSongID() throws IOException {
		HashMap<String, Integer> rankedList = new HashMap<String, Integer>();
		ArrayList<String> rankedSongID = new ArrayList<String>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(Files.SONGFILE)));
		String line = "";
		String[] temp;
		int grade = 51;
		int inputGrade = 0;
		int totalGrade = 0;
		while ((line = br.readLine()) != null) {
			temp = line.split(",");
			inputGrade = grade - Integer.parseInt(temp[2]);
			if (!rankedList.containsKey(temp[3])) {
				rankedList.put(temp[3], inputGrade);
			} else {
				totalGrade = rankedList.get(temp[3]) + inputGrade;
				rankedList.put(temp[3], totalGrade);
			}
		}
		Iterator<String> keys = rankedList.keySet().iterator();
		String data = "";
		String key = "";
		int value = 0;
		while (keys.hasNext()) {
			key = keys.next();
			value = rankedList.get(key);
			data = key + "#" + value;
			rankedSongID.add(data);
		}
		return rankedSongID;
	}

	public String getLyric(String songId) throws IOException {
		String lyric = "";
		String songDetail = Property.DETAILURL + songId;

		try {
			Document doc = Jsoup.connect(songDetail).header("Content-Type", "application/json;charset=UTF-8")
					.userAgent(Property.USER_AGENT).get();
			lyric = doc.select("#lyricArea").text();
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return lyric;
	}

}
