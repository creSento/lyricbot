package lyrics;

import java.io.IOException;

public class GetWord {
	public void getWordRun() throws IOException {
		Analyze ana = new Analyze();
		// normal word
		ana.deleteDuplicate(Files.NORMALWORDFILE, Files.NORMALRESULTFILE);
		// weighted word
		ana.deleteDuplicate(Files.WEIGHTWORDFILE, Files.WEIGHTRESULTFILE);
		
	}

}
