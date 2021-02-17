import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {
	
	static final String tmpDir = System.getProperty("java.io.tmpdir");
	static final String userHome = System.getProperty("user.home");
	static final String currentDir = System.getProperty("user.dir");
	
	
    static public List<String> loadPeopleLastNamesFromCsvFileByBufferedReader(String pathToCsvFile) throws FileNotFoundException, IOException {
    	String line = "";  
    	String splitBy = ",";
    	List<String> out_listOfPeopleLastNames = new ArrayList<String>();
    	
    	File csvFile = new File(pathToCsvFile);
    	if (!csvFile.isFile()) {
    		System.out.println("File " + pathToCsvFile + " doesn't exist");
    		throw new FileNotFoundException();
    	}
    	
    	try   
    	{  
	    	//parsing a CSV file into BufferedReader class constructor  
	    	BufferedReader br = new BufferedReader(new FileReader(pathToCsvFile));  
	    	while ((line = br.readLine()) != null){  
	    		String[] tmpArrayOfPeopleLastNames = line.split(splitBy);
	    		List<String> tmpListOfPeopleLastNames = Arrays.asList(tmpArrayOfPeopleLastNames);
	    		out_listOfPeopleLastNames.addAll(tmpListOfPeopleLastNames);
	    	}  
	    }catch (IOException e){  
	    	e.printStackTrace();  
    	}
    	
    	return out_listOfPeopleLastNames;
    }
    
    static public List<String> loadPepleLastNamesFromCsvFileByScanner(String pathToCsvFile) throws FileNotFoundException, IOException {
    	
    	List<String> out_listOfPeopleLastNames = new ArrayList<String>();
    	
    	File csvFile = new File(pathToCsvFile);
    	if (!csvFile.isFile()) {
    		System.out.println("File " + pathToCsvFile + " doesn't exist");
    	    throw new FileNotFoundException();
    	}
    	
    	//parsing a CSV file into Scanner class constructor  
    	Scanner sc = new Scanner(csvFile);  
    	sc.useDelimiter(",");  	
    	
    	while (sc.hasNext()){  
    		String tmpText = sc.next();
    		tmpText = cleanText(tmpText);
    		System.out.print(tmpText); 
    		out_listOfPeopleLastNames.add(tmpText);
    	}   
    	
    	sc.close();    	
    	
    	return out_listOfPeopleLastNames;
    }
    
    static private String cleanText(String text){
    	text = text.trim();
		text = text.replaceAll("\\n", "");
		text = text.replaceAll("\\r", "");
		return text;
    }	
    
    static public String buildAbsoluteFilePath(String filePath){                
        
        StringBuffer filePathBuffer = new StringBuffer();
        filePathBuffer.append(currentDir);
        filePathBuffer.append("\\");
        filePathBuffer.append(filePath);
        
        return filePathBuffer.toString();
    }
    
	static public long calculateAverage(List<Long> arrayList) {
		long average = 0;	    	    
	    Long sum = new Long(0);
	    
	    if(!arrayList.isEmpty()) {
	      for (Long mark : arrayList) {
	          sum += mark;
	      }
	      average = sum.longValue() / arrayList.size();
	    }
	    return average;
	}
			
}