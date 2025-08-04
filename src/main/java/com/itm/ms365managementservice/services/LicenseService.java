package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.repositories.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public List<License> getAllLicenses() {
        return licenseRepository.findAll();
    }

    public Optional<License> getLicenseById(String id) {
        return licenseRepository.findById(id);
    }

    public License createLicense(License license) {
        return licenseRepository.save(license);
    }

    public License updateLicense(String id, License updatedLicense) {
        updatedLicense.setId(id);
        return licenseRepository.save(updatedLicense);
    }

    public void deleteLicense(String id) {
        licenseRepository.deleteById(id);
    }
}

