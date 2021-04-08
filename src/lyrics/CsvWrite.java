package lyrics;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CsvWrite {
	BufferedWriter bw = null;
	
	public void writeFile(String fileName, String line) throws IOException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "euc-kr"));
		bw.append(line);
		bw.flush();
		bw.close();
	}
	
}
