import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MergeSortedFile {
	
	
	public static void main(String arg[]) {
	
	try {
		File file1 = new File("C:\\ICE\\ICETest\\src\\SortedFile1.txt");
		File file2 = new File("C:\\ICE\\ICETest\\src\\SortedFile2.txt");
		
		PrintWriter pw = new PrintWriter("C:\\ICE\\ICETest\\src\\MergedFile.txt");
		
		Scanner scanner1 = new Scanner(file1);
		Scanner scanner2 = new Scanner(file2);
		
		String line1 = scanner1.nextLine();
		String line2 = scanner2.nextLine();
		
		while(line1 != null || line2 != null) {
			 
			if(line1 == null){
				 System.out.println("Read from from file 2 :"+line2);
				 pw.println(line2);
				 line2 = readNextLine(scanner2);
			}else if(line2 == null){
				System.out.println("Read from from file 1 :"+line1);
				pw.println(line1);
				line1 = readNextLine(scanner1);				
			}else if(line1.compareToIgnoreCase(line2) <=0){
				System.out.println("Read from from file 1 :"+line1);
				pw.println(line1);
				line1 = readNextLine(scanner1);
			}else {
				System.out.println("Read from from file 2 :"+line2);
				pw.println(line2);
				line2 = readNextLine(scanner2);
			}
		}
		pw.flush();
		pw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
			
		}
	
	}
	
	/**
	 * Reads the next line available from the file scanner
	 * @param scanner
	 * @return
	 */
	static String readNextLine(Scanner scanner){
		
		if(scanner.hasNextLine()){
			return scanner.nextLine();
		}else{
			return null;
	}	
		
	}
}
