package example.spring.license.repository;

import example.spring.license.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data framework will pull apart the name of the methods to build a query to access the underlying data
 * */

@Repository
public interface LicenseRepository extends CrudRepository<License,String> {
    public List<License> findByOrganizationId(String organizationId);
    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

}
