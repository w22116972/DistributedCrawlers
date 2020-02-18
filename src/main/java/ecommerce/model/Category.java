package ecommerce.model;

public class Category {

    private String name;
    private Long count;

    public Category(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public Long getCount() {
        return this.count;
    }
}
