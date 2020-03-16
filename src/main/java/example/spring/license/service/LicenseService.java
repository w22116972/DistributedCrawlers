package example.spring.license.service;

import example.spring.license.model.License;
import example.spring.license.config.ServiceConfig;
import example.spring.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

// create CRUD for model License
@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    public License getLicense(String licenseId, String organizationId){
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license.search.err or.message", null, null),licenseId, organizationId));
        }
        return license.withExample(config.getExampleProperty());
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withExample(config.getExampleProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withExample(config.getExampleProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null),licenseId);
        return responseMessage;
    }
}
