

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTest {

	@Test	
	public void test1_readFile() {
        String peopleCsvFileName = "src\\main\\resources\\people.csv";
        
        String pathToCsvFile = Utils.buildAbsoluteFilePath(peopleCsvFileName);
        List<String> listOfPeopleLastNames = null;
        try{
        	listOfPeopleLastNames = Utils.loadPepleLastNamesFromCsvFileByScanner(pathToCsvFile);
        }catch(IOException ex){
        	System.out.println("");
        }
        Assert.assertTrue(!listOfPeopleLastNames.isEmpty());
        Assert.assertTrue(listOfPeopleLastNames.size() == 20);
	}
	
	@Test	
	public void test2_calculateAverage() {
		
		List<Long> arrayList = new ArrayList<Long>();
		
		for(int i = 1; i <= 20; i++){
			arrayList.add(new Long(i));
		}
		
		long average = Utils.calculateAverage(arrayList);

        Assert.assertTrue(average == 10);
	}
	
	@Test
	public void test3_FHIR(){
        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        IClientInterceptor clientInterceptor = new ResponseInterceptor(false);
        client.registerInterceptor(clientInterceptor);
        
    	Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH")) 
                .returnBundle(Bundle.class)
                .execute();	
    	
    	long timeResponse = ((ResponseInterceptor)clientInterceptor).calculateAverage();
    	Assert.assertTrue(timeResponse > 0);

	}

	@AfterClass
	public static void cleanUp(){

	}
}
