package com.freelancer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.freelancer.dao.IDAO;
import com.freelancer.dao.BusinessRepository;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BusinessServiceTest {

    private BusinessService businessService;
    private IDAO<Business> businessDao;
    private BusinessRepository businessRepository;

    @Before
    public void setUp() {
        businessRepository = mock(BusinessRepository.class);
        businessDao = mock(IDAO.class);
        businessService = new BusinessService(businessRepository);
    }

    @Test
    public void testCreateBusiness() {
        Business business = new Business("name", "description", "contactInfo", "ownerUsername");
        business.setId("1");

        doNothing().when(businessRepository).save(any(Business.class));

        businessService.addBusiness(business);
        verify(businessRepository, times(1)).save(business);
    }

    @Test
    public void testGetBusiness() {
        Business business = new Business("1", "Test Business", "description", "contactInfo", "ownerUsername");

        when(businessRepository.get(anyString())).thenReturn(business);

        Optional<Business> fetchedBusinessOptional = businessService.getBusiness("1");
        assertTrue(fetchedBusinessOptional.isPresent());
        Business fetchedBusiness = fetchedBusinessOptional.get();
        assertEquals("Test Business", fetchedBusiness.getName());
    }

    @Test
    public void testUpdateBusiness() {
        Business business = new Business("name", "description", "contactInfo", "ownerUsername");
        business.setId("1");

        doNothing().when(businessRepository).update(any(Business.class));

        businessService.updateBusiness(business);
        verify(businessRepository, times(1)).update(business);
    }

    @Test
    public void testDeleteBusiness() {
        Business business = new Business("name", "description", "contactInfo", "ownerUsername");
        business.setId("1");

        doNothing().when(businessRepository).delete(anyString());

        businessService.deleteBusiness(business);
        verify(businessRepository, times(1)).delete("1");
    }
}
