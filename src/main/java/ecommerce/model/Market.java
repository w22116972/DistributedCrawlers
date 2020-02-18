package ecommerce.model;

import java.util.Collection;

public class Market {

    private Collection<Category> categoryList;
    private Long totalCount;

    public Market(Collection<Category> categoryList, Long totalCount) {
        this.categoryList = categoryList;
        this.totalCount = totalCount;
    }

    public Collection<Category> getCategoryList() {
        return categoryList;
    }

    public Long getTotalCount() {
        return totalCount;
    }
}
