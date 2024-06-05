package com.freelancer;

import java.util.List;

public class BusinessService {
    private IDAO<Business> businessDao;

    public BusinessService(IDAO<Business> businessDao) {
        this.businessDao = businessDao;
    }

    public void createBusiness(Business business) {
        businessDao.save(business);
    }

    public void updateBusiness(Business business) {
        businessDao.update(business);
    }

    public void deleteBusiness(String id) {
        businessDao.delete(id);
    }

    public Business getBusiness(String id) {
        return businessDao.get(id);
    }

    public List<Business> getAllBusinesses() {
        return businessDao.getAll();
    }
}
