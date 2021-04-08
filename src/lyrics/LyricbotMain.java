package lyrics;

import java.io.IOException;
import java.util.ArrayList;

public class LyricbotMain {

	public static void main(String[] args) throws IOException {
		LyricBot bot = new LyricBot();
		ArrayList<String> wordList = bot.wordList();
		String line = bot.makeSerchLine(wordList);
		ArrayList<String> searchList = bot.getSentencesMelon(line);
		bot.findWord(line, searchList);
	}

}
