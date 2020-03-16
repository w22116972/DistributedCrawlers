package example.spring.license.model;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="licenses")
public class License extends RepresentationModel<License> { // RepresentationModel<?>: able to add links to License model

    @Id
    @Column(name = "license_id", nullable = false)
    private String licenseId;
    @Column(name="description")
    private String description;
    @Column(name = "organization_id", nullable = false)
    private String organizationId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Override
    public String toString() {
        return "License{" +
                "licenseId='" + licenseId + '\'' +
                ", description='" + description + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", productName='" + productName + '\'' +
                ", licenseType='" + licenseType + '\'' +
                '}';
    }

    public License withExample(String example) {
        this.setDescription(example);
        return this;
    }


    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
}
