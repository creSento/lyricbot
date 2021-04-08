package lyrics;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		GetChart chart = new GetChart();
		chart.getChartRun();
		
		GetLyric lylic = new GetLyric();
		lylic.getLylicRun();
		
		GetWord word = new GetWord();
		word.getWordRun();
	}

}
