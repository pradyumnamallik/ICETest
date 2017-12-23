import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CusipReaderByChunks {

	private static String FILENAME= "C:\\ICE\\ICETest\\src\\Cusip.txt";
	
	public static void main(String arg[]){
		
		int numChunks = Runtime.getRuntime().availableProcessors();
		long[] offsets = new long[numChunks];
		File file = new File(FILENAME);
		
		try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			// build offsets of each chunk
			for(int i = 1; i< numChunks;i++) {
			
				randomAccessFile.seek(i * file.length()/numChunks);
				while(true) {
					int read = randomAccessFile.read();
					if(read =='\n' || read == -1) {
						break;
					}
				}
				offsets[i] = randomAccessFile.getFilePointer();
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		// Execute each of the chunk of the file in a thread
		ExecutorService service = Executors.newFixedThreadPool(numChunks);
		for(int i = 0;i < numChunks; i++){
			long start = offsets[i];
			long end = i < numChunks -1 ? offsets[i+1] : file.length();
			service.execute(new CusipProcessor(file, start, end));
		}
	}
	
	static class CusipProcessor implements Runnable {
		
		private final File file;
		private final long  start;
		private final long  end;
		
		public CusipProcessor(File file, long start, long end) {
			this.file = file;
			this.start = start;
			this.end = end;
		}
		
		@Override
		public void run() {
			LinkedHashMap<String, TreeSet<Double>> hashMap = new LinkedHashMap<>();
			try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
				
				LinkedHashMap<String, TreeSet<Double>> linkedHashMap = buildCusipMap(hashMap, raf);
				for(String cusipId : linkedHashMap.keySet()){
					TreeSet<Double> set = linkedHashMap.get(cusipId);
					System.out.println("CUSIP ID : "+cusipId +  " ,Closing (or Latest) Price :"+ set.last());
				}
	} catch (IOException e) {
				e.printStackTrace();
		}
}

		/**
		 * This method builds a linked hash map for every valid CUSIP ID and the list of Prices
		 * @param hashMap
		 * @param raf
		 * @return LinkedHashMap
		 * @throws IOException
		 */
		private LinkedHashMap<String, TreeSet<Double>> buildCusipMap(LinkedHashMap<String, TreeSet<Double>> hashMap, RandomAccessFile raf)
				throws IOException {
			TreeSet <Double> set = null;
			String cusip = null;
			boolean isCusipValid = true;
			
				while(raf.getFilePointer() < end) {
					String line = raf.readLine();
					
					if(line == null) {
						continue;
					}
				
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
				return hashMap;
		}
		
	/**
	 * This method checks if the line item is price having a Double value, if not that would indicate that
	 * it is a CUSIP Identifier and throws Number Format Exception
	 * @param line
	 * @return boolean
	 * @throws NumberFormatException
	 */
				
		 static boolean isDouble(String line){
			
			try{
				Double.parseDouble(line);
				return true;
			}catch(NumberFormatException e){
				return false;
				
			}
		}
	}	
		
}
	
	