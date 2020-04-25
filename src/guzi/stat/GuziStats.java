package guzi.stat;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import guzi.agent.HumanGuziUser;
import guzi.money.GuziOperationService;
import guzi.world.GuziWorld;

public class GuziStats  {

	private CSVPrinter csvPrinter;

	public GuziStats() {
		this.initCsvFile();
	}

	public void notifyNewDayPlayed(GuziWorld world) {
	  // extract guzis in circulation number from the world
		Integer guzisInCirculation = 0;
		// extract number of guzi users
		Integer nbUsers = 0;
		// extract total daily income
		Integer totalDailyIncome = 0;
		for(int i = 0; i < world.getWidth(); i++) {
			for(int j = 0; j < world.getHeight(); j++) {
				HumanGuziUser user = world.getAgent(i, j);
				if (user != null) {
					nbUsers++;
					guzisInCirculation += GuziOperationService.getNbGuzisInWallet(user);
					guzisInCirculation += GuziOperationService.getNbGuzasInPortfolio(user);
					totalDailyIncome += GuziOperationService.calculateDailyIncome(user);
				}
			}
		}
		
		// put the values in the csv
		this.printNewCsvLine(nbUsers, guzisInCirculation, totalDailyIncome);
	}
	
	public void notifySimulationStopped() {
		this.closeCsvFile();
	}
	
	private void initCsvFile() {
		try {
			this.csvPrinter = new CSVPrinter(new FileWriter(generateCsvFileName()), CSVFormat.RFC4180);
			// first csv line : columns names
			this.printNewCsvLine("nbUser", "guzisInCirculation", "totalDailyIncome");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generateCsvFileName() {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String dateStr = simpleDateFormat.format(new Date());
			return "datas/guzi_output_" + dateStr + ".txt";
	}
	
	private void printNewCsvLine(Object... values) {
		try {
			this.csvPrinter.printRecord(values);
			this.csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void closeCsvFile() {
		try {
			this.csvPrinter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
