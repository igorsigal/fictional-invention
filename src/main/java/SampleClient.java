import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;


import java.io.IOException;
import java.util.List;


import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SampleClient.class);

    public static void main(String[] theArgs) {
        
        String peopleCsvFileName = "src\\main\\resources\\people.csv";
        
        String pathToCsvFile = Utils.buildAbsoluteFilePath(peopleCsvFileName);
        List<String> listOfPeopleLastNames = null;
        try{
        	listOfPeopleLastNames = Utils.loadPepleLastNamesFromCsvFileByScanner(pathToCsvFile);
        }catch(Exception ex){
        	logger.error("Problem occurred while reading a file " + pathToCsvFile + ex.getMessage());
        }
        
        
        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        IClientInterceptor clientInterceptor = new ResponseInterceptor(false);
        client.registerInterceptor(clientInterceptor);

        // Search for Patient resources
        for(int i = 0; i < 3; i++){
        	
        	logger.info("============== " + ( i+1 ) + " LOOP STARTS  =================");
        	
	        for(String personLastName : listOfPeopleLastNames){
	        		        	
	        	logger.info("Sending request for " + personLastName);
	        	
	        	Bundle response;
	        	
	        	if(i == 0 || i == 1){	        			        
		        	response = client
			                .search()
			                .forResource("Patient")
			                .where(Patient.FAMILY.matches().value(personLastName))
			                .returnBundle(Bundle.class)
			                .execute();		        	
		        	
	        	} else if (i == 2) {
	        		response = client
			        	   .search()
			        	   .forResource("Patient")
			        	   .where(Patient.FAMILY.matches().value(personLastName))
			        	   .returnBundle(Bundle.class)
			        	   .cacheControl(new CacheControlDirective().setNoCache(true)) // <-- add a directive
			        	   .execute();
	        		
	        	}
	        }
	        
	        logger.info("============== " + (i+1) + " LOOP RESULTS =================");
	        
	        ((ResponseInterceptor)clientInterceptor).calculateAverage();
	        
	        logger.info("============== " + (i+1) + "  LOOP ENDS   =================");
	        
	        if(i == 0 || i == 1){	
		        try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
        }
        
        logger.info("==============     THE END    =================");
		
    }

}
