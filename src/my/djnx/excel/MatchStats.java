// MatchStats djnx
package my.djnx.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class MatchStats {

	private static String[] countries = new String[14];
	private static float minRange, maxRange;
	private static float averageWin;
	private static int howManyMatches, howManyWins;
	private static int yourBet;
	private static float wygrana;

	private static void setCountries() {
		countries[0] = "ameryka poludniowa";
		countries[1] = "azja";
		countries[2] = "bialorus";
		countries[3] = "dania";
		countries[4] = "estonia";
		countries[5] = "europa";
		countries[6] = "finlandia";
		countries[7] = "islandia";
		countries[8] = "kolumbia";
		countries[9] = "norwegia";
		countries[10] = "rosja";
		countries[11] = "slowacja";
		countries[12] = "szwecja";
		countries[13] = "swiat";
	}

	private static void setRange(float a, float b) {
		minRange = a;
		maxRange = b;
		howManyMatches = 0;
		howManyWins = 0;
		yourBet = 50;
		averageWin = 0;
		wygrana = 0;
	}

	private static boolean checkCountry(String country) {
		for (String s : countries) {
			if (s.equals(country))
				return true;
		}
		return false;
	}
	// 3628

	private static void analyzeResult(String result, String x1, String x, String x2) {
		int team1 = Character.getNumericValue(result.charAt(0));
		int team2 = Character.getNumericValue(result.charAt(4));
		float multiply;
		float mx1 = Float.parseFloat(x1);
		float mx = Float.parseFloat(x);
		float mx2 = Float.parseFloat(x2);

		if ((mx1 > minRange && mx1 < maxRange) || (mx > minRange && mx < maxRange) || (mx2 > minRange && mx2 < maxRange)) {
	//		System.out.print("Wynik: " + result + "    ");
	//		System.out.print("Przebitka: " + x1 + "  " + x + "  " + x2 + "\n");

			howManyMatches++;
			if (team1 == team2) {
	//			System.out.println("Remis: " + x);
				multiply = Float.parseFloat(x);
				if (multiply > minRange && multiply < maxRange) {
	//				System.out.println("Wygrana");
					howManyWins++;
					averageWin += multiply;
					wygrana += (yourBet * multiply);
				}
			} else if (team1 > team2) {
	//			System.out.println("Wygra³ Gospodarz: x" + x1);
				multiply = Float.parseFloat(x1);
				if (multiply > minRange && multiply < maxRange) {
	//				System.out.println("Wygrana");
					howManyWins++;
					averageWin += multiply;
					wygrana += (yourBet * multiply);
				}
			} else if (team1 < team2) {
	//			System.out.println("Wygrali Goœcie: x" + x2);
				multiply = Float.parseFloat(x2);
				if (multiply > minRange && multiply < maxRange) {
	//				System.out.println("Wygrana");
					howManyWins++;
					averageWin += multiply;
					wygrana += (yourBet * multiply);
				}

			}
	//		System.out.println("-------------------------------");
		}
	}
	
	public static void checkStats()	{
		try {

			FileInputStream file = new FileInputStream(new File("tabela.xls"));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			// odczytuj z pierwszej zak³adki
			HSSFSheet sheet = workbook.getSheetAt(0);

			// przelec przez ka¿dy wiersz
			Iterator<Row> rowIterator = sheet.iterator();
			String cell1, cell2, cell3, cell4;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// pobierz pozycje dla kolejnej lini
				Iterator<Cell> cellIterator = row.cellIterator();

				Cell cell = cellIterator.next();
				cell1 = cell.getStringCellValue();	// panstwo lub gospodarze
				if (checkCountry(cell1)) {
					// System.out.println("Pañstwo: "+ cell1);
				} else {
//					System.out.print("Wynik: " + cell1 + "    ");
//					cell = cellIterator.next();	// gospodarze
			//		cell = cellIterator.next();	// goscie
					
					cell = cellIterator.next();	// wynik
					cell2 = cell.getStringCellValue();
					cell = cellIterator.next();
					cell3 = cell.getStringCellValue();
					cell = cellIterator.next();
					cell4 = cell.getStringCellValue();

//					System.out.print("Przebitka: " + cell2 + "  " + cell3 + "  " + cell4 + "\n");
					analyzeResult(cell1, cell2, cell3, cell4);
				//	analyzeResult(cell2, x1, x, x2);
				}

			}
			int howMuchCash = howManyMatches * yourBet;
			float averageStake = averageWin / howManyWins;
			float winProcent = ((float)howManyWins / (float)howManyMatches) * 100;
			float yourWin = ((averageStake * yourBet) * howManyWins) - howMuchCash;

			System.out.println("Zakres: "+minRange+" - "+maxRange);
			System.out.println("By³o " + howManyMatches + " meczów.\n Wygranych: " + howManyWins);
			System.out.println("Œrednia przebitki: x" + averageStake);
			System.out.println("Procent wygranych: %" + winProcent);
			System.out.println("Postawi³eœ " + howMuchCash + "PLN   Wygra³eœ: "+ (yourWin + howMuchCash) +"("+yourWin+")PLN");
			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		setCountries();
		setRange(1.4f, 2.1f);
		checkStats();
		System.out.println("----------------");
		setRange(1.7f, 2.0f);
		checkStats();
		System.out.println("----------------");
		setRange(1.3f, 2.65f);
		checkStats();

	}

}
