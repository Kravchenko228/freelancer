package com.freelancer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessServiceTest {
    private BusinessService businessService;

    @Before
    public void setUp() {
        businessService = new BusinessService();
    }

    @Test
    public void testCreateAndRetrieveBusiness() {
        Business business = new Business();
        business.setId("1");
        business.setName("Test Business");
        business.setDescription("This is a test business.");
        business.setContactInfo("test@example.com");

        businessService.createBusiness(business);

        Business retrievedBusiness = businessService.getBusiness("1");
        assertNotNull(retrievedBusiness);
        assertEquals("Test Business", retrievedBusiness.getName());
    }

    
}
