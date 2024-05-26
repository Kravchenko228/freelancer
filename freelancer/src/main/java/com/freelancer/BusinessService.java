package com.freelancer;

import java.util.List;
import java.util.Optional;

public class BusinessService {
    private BusinessRepository businessRepository = new BusinessRepository();

    public void createBusiness(Business business) {
        businessRepository.save(business);
    }

    public Business getBusiness(String id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        return optionalBusiness.orElse(null);  
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public void updateBusiness(Business business) {
        businessRepository.update(business);
    }

    public void deleteBusiness(String id) {
        businessRepository.delete(id);
    }
}
