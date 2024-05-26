package com.freelancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BusinessRepository {
    private List<Business> businesses = new ArrayList<>();

    public void save(Business business) {
        businesses.add(business);
    }

    public Optional<Business> findById(String id) {
        return businesses.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public List<Business> findAll() {
        return new ArrayList<>(businesses);
    }

    public void update(Business business) {
        findById(business.getId()).ifPresent(existingBusiness -> {
            existingBusiness.setName(business.getName());
            existingBusiness.setDescription(business.getDescription());
            existingBusiness.setContactInfo(business.getContactInfo());
        });
    }

    public void delete(String id) {
        businesses.removeIf(b -> b.getId().equals(id));
    }
}
