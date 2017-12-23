import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeSet;

public class CusipReader {

	private static String FILENAME= "C:\\ICE\\ICETest\\src\\Cusip.txt";
	
	public static void main(String arg[]){
		
		LinkedHashMap<String, TreeSet<Double>> linkedHashMap = loadCusip(FILENAME);	
		
		for(String cusipId : linkedHashMap.keySet()){
			TreeSet<Double> set = linkedHashMap.get(cusipId);
			System.out.println("CUSIP ID : "+cusipId +  " ,Closing (or Latest) Price :"+ set.last());
		}
	}
	
	static LinkedHashMap<String, TreeSet<Double>> loadCusip(String fileName){
		
		LinkedHashMap<String, TreeSet<Double>> hashMap = new LinkedHashMap<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		
			String line = null;
			TreeSet <Double> set = null;
			String cusip = null;
			boolean isCusipValid = true;
			
			while((line = br.readLine()) != null) {
					
					if(!isDouble(line) && line.length() != 8){
						isCusipValid = false;
						continue;
					}
					
					if(!isDouble(line) && line.length() == 8){
						isCusipValid = true;
						cusip = line;
						set = new TreeSet<>();
					}else if(isDouble(line) && isCusipValid){
						set.add(Double.parseDouble(line));
					}
					
					if(isCusipValid && !hashMap.containsKey(cusip)){
						hashMap.put(cusip, set);
					}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return hashMap;
	}
	
	
	 static boolean isDouble(String line){
		
		try{
			Double.parseDouble(line);
			return true;
		}catch(NumberFormatException e){
			return false;
			
		}
	}
}
