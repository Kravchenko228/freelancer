package com.freelancer;


import java.util.List;
import com.freelancer.dao.BusinessRepository;
import java.util.Optional;

public class BusinessService {
    private BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public void addBusiness(Business business) {
        businessRepository.save(business);

    }

    public void updateBusiness(Business business) {
        businessRepository.update(business);
    }

    public void deleteBusiness(Business business) {
        businessRepository.delete(business.getId());
    }

    public Optional<Business> getBusiness(String id) {
        return Optional.ofNullable(businessRepository.get(id));
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.getAll();
    }
}
