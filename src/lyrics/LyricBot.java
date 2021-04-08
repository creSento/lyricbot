package lyrics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LyricBot {
	private final int WORDRANGE = 100;
	private final int SENTENCERANGE = 15;
	Scanner sc;
	BufferedReader br = null;
	WebDriver driver;

	public ArrayList<String> wordList() throws IOException {
		ArrayList<String> wordList = new ArrayList<String>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(Files.TOP100FILE), "utf-8"));
		String line = "";
		String[] temp;
		while ((line = br.readLine()) != null) {
			temp = line.split(",");
			wordList.add(temp[0]);
		}
		System.out.println("Words top : " + wordList.size());
		return wordList;
	}

	public String makeSerchLine(ArrayList<String> list) {
		String word = "";
		String serchLine = "";
		int[] numList = makeRandomNum(3, WORDRANGE);
		int wordIndex = 0;
		for (int i = 0; i < numList.length; i++) {
			wordIndex = numList[i];
			word = list.get(wordIndex);
			if (i == numList.length - 1) {
				System.out.printf("%s\n", word);
				serchLine = serchLine + word;
			} else {
				System.out.printf("%s, ", word);
				serchLine = serchLine + word + ",";
			}
		}
		return serchLine;
	}

	public ArrayList<String> getSentencesMelon(String serchLine) throws IOException {
		// serch
		ArrayList<String> lyricList = new ArrayList<String>();
		System.setProperty(Property.DRIVERKEY, Property.DRIVERVALUE);
		driver = new ChromeDriver();
		driver.get(Property.SEARCHURL);
		term(3);
		WebElement serchBox = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/fieldset/input[1]"));
		serchBox.sendKeys(serchLine);
		term(3);
		driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/fieldset/button[2]")).click();
		term(3);
		String url = driver.getCurrentUrl();

		// get results
		Document doc = Jsoup.connect(url).get();
		Elements searchResult = doc.select("#pageList > div > ul > li");
		String song = "";

		// set max item
		int itemSize = 0;
		int result = searchResult.size();
		if (result < SENTENCERANGE) {
			itemSize = result;
		} else {
			itemSize = SENTENCERANGE;
		}
		System.out.println("==============[RESULT]==============");
		for (int i = 0; i < itemSize; i++) {
			searchResult = doc.select("#pageList > div > ul > li:nth-child(" + i + ") > dl > dd.lyric > a");
			song = searchResult.attr("title");
			lyricList.add(song);
		}
		driver.close();

		return lyricList;
	}

	public void findWord(String serchLine, ArrayList<String> lyricList) {
		String[] words = serchLine.split(",");
		String[] result = { "", "", "" };
		String line = "";
		for (int i = 0; i < lyricList.size(); i++) {
			line = lyricList.get(i);
			if (line.contains(words[0]) && line.contains(words[1]) && line.contains(words[2])) {
				result[2] = result[2] + line + "\n";
			} else if (line.contains(words[0]) && line.contains(words[1])
					|| line.contains(words[0]) && line.contains(words[2])
					|| line.contains(words[1]) && line.contains(words[2])) {
				result[1] = result[1] + line + "\n";
			} else if (line.contains(words[0]) || line.contains(words[1]) || line.contains(words[2])) {
				result[0] = result[0] + line + "\n";
			}
		}
		
		for (int i = result.length - 1; i >= 0; i--) {
			System.out.printf("Contains %d Words :\n", (i + 1));
			System.out.println(result[i].replace("...", "").replace("- 페이지 이동", ""));
		}
	}

	private int[] makeRandomNum(int size, int range) {
		int[] numList = new int[size];
		for (int i = 0; i < size; i++) {
			Random ran = new Random();
			int ranNum = ran.nextInt(range);
			numList[i] = ranNum;
			for (int j = 0; j < i; j++) {
				if (numList[i] == numList[j]) {
					i--;
				}
			}
		}

		return numList;
	}

	private void term(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
