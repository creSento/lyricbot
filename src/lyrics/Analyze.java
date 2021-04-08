package lyrics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class Analyze {
	private final String KOMORANPOS = "NNG";
	CsvWrite csvwriter = new CsvWrite();
	BufferedReader br = null;
	Komoran komoran = new Komoran(DEFAULT_MODEL.EXPERIMENT);
	KomoranResult result;

	public void runWork(String lyric, HashMap<String, Integer> wordList) {
		List<Token> tokens;
		int score = 1;

		try {
			result = komoran.analyze(lyric);
			tokens = result.getTokenList();
			for (Token token : tokens) {
				if (token.getPos().equals(KOMORANPOS)) {
					if (!wordList.containsKey(token.getMorph())) {
						wordList.put(token.getMorph(), 1);
					} else {
						score = wordList.get(token.getMorph());
						wordList.put(token.getMorph(), score + 1);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	public void runWork(String lyric, int score, HashMap<String, Integer> wordList) {
		List<Token> tokens;
		int value = 0;
		try {
			result = komoran.analyze(lyric);
			tokens = result.getTokenList();
			for (Token token : tokens) {
				if (token.getPos().equals(KOMORANPOS)) {
					if (!wordList.containsKey(token.getMorph())) {
						value = score;
						wordList.put(token.getMorph(), value);
					} else {
						value = wordList.get(token.getMorph());
						wordList.put(token.getMorph(), value + score);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	public void writeWordFile(String fileName, HashMap<String, Integer> hash) throws IOException {
		CsvWrite writer = new CsvWrite();
		String line = "";
		String key = "";
		Iterator<String> keys = hash.keySet().iterator();
		while (keys.hasNext()) {
			key = keys.next();
			line = key + "," + hash.get(key) + "\n";
			writer.writeFile(fileName, line);
		}
	}
	
	public void deleteDuplicate(String fileName, String outFile) throws IOException {
		HashMap<String, Integer> singleWordList = new HashMap<String, Integer>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "euc-kr"));
		String line = "";
		String[] temp;
		int inputCount = 0;
		int totalCount = 0;
		while ((line = br.readLine()) != null) {
			temp = line.split(",");
			inputCount = Integer.parseInt(temp[1]);
			if (!singleWordList.containsKey(temp[0])) {
				totalCount = inputCount;
				singleWordList.put(temp[0], inputCount);
			} else {
				totalCount = singleWordList.get(temp[0]) + inputCount;
				singleWordList.put(temp[0], totalCount);
			}
		}
		
		Iterator<String> key = singleWordList.keySet().iterator();
		String word = "";
		int value = 0;
		String inputLine = "";
		while (key.hasNext()) {
			word = key.next();
			value = singleWordList.get(word);
			inputLine = word + "," + value + "\n";
			csvwriter.writeFile(outFile, inputLine);
		}
	}

}
