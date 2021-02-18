

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Property;
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
		
	static final String DATA_RESOURCE_PATIENT_TYPE = "Patient";
	static final String DATA_PATIENT_FAMILY_SMITH = "SMITH";

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
                .forResource(DATA_RESOURCE_PATIENT_TYPE)
                .where(Patient.FAMILY.matches().value(DATA_PATIENT_FAMILY_SMITH)) 
                .returnBundle(Bundle.class)
                .execute();
    	
    	String strResourceType = null;
    	String strPatientLastName = null;
    	
    	List<BundleEntryComponent> entries = response.getEntry();
    	if(entries != null && !entries.isEmpty()){
    		strResourceType = response.getEntry().get(0).getResource().getResourceType().toString();
    		strPatientLastName = response.getEntry().get(0).getResource().
    				getNamedProperty("name").getValues().get(0).
    				getNamedProperty("family").getValues().get(0).toString();
    	}
    	
    	long timeResponse = ((ResponseInterceptor)clientInterceptor).calculateAverage();
    	Assert.assertTrue(timeResponse > 0);
    	
    	Assert.assertTrue(strResourceType.equals(DATA_RESOURCE_PATIENT_TYPE));
    	Assert.assertTrue(strPatientLastName.toUpperCase().equals(DATA_PATIENT_FAMILY_SMITH));

	}

	@AfterClass
	public static void cleanUp(){

	}
}
