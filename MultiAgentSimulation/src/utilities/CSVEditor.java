package utilities;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class CSVEditor {

	private CSVWriter writer;

	public CSVEditor(String file) {
		try {
			FileWriter outputFile = new FileWriter(file);
			writer = new CSVWriter(outputFile, ';');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeLine(String[] nextLine) {
		if (writer != null) {
			writer.writeNext(nextLine);
		}
	}

	public void closeEditor() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
