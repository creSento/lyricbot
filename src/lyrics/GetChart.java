package lyrics;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

public class GetChart {

	public void getChartRun() throws IOException {
		ChartReader reader = new ChartReader();
		WebDriver drv = reader.getUrl();
		
		for (int i = 10; i > 0; i--) {	// year
			for (int j = 1; j < 13; j++) {	//month
				reader.selectMonth(drv, i, j);
				reader.getSong(drv, i, j);
			}
		}
	}

}
