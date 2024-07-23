package com.freelancer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BusinessServiceTest {

    private BusinessService businessService;
    private IDAO<Business> businessDao;

    @Before
    public void setUp() {
        businessDao = Mockito.mock(IDAO.class);
        businessService = new BusinessService(businessDao);
    }

    @Test
    public void testCreateBusiness() {
        Business business = new Business();
        business.setId("1");
        business.setName("Test Business");
        business.setDescription("Description");
        business.setContactInfo("Contact");

        doNothing().when(businessDao).save(any(Business.class));

        businessService.createBusiness(business);
        verify(businessDao, times(1)).save(business);
    }

    @Test
    public void testGetBusiness() {
        Business business = new Business();
        business.setId("1");
        business.setName("Test Business");
        business.setDescription("Description");
        business.setContactInfo("Contact");

        when(businessDao.get(anyString())).thenReturn(business);

        Business fetchedBusiness = businessService.getBusiness("1");
        assertNotNull(fetchedBusiness);
        assertEquals("Test Business", fetchedBusiness.getName());
    }

    @Test
    public void testUpdateBusiness() {
        Business business = new Business();
        business.setId("1");
        business.setName("Test Business");
        business.setDescription("Description");
        business.setContactInfo("Contact");

        doNothing().when(businessDao).update(any(Business.class));

        businessService.updateBusiness(business);
        verify(businessDao, times(1)).update(business);
    }

    @Test
    public void testDeleteBusiness() {
        doNothing().when(businessDao).delete(anyString());

        businessService.deleteBusiness("1");
        verify(businessDao, times(1)).delete("1");
    }
}
