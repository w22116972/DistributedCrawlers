package ecommerce.selector;

public class RutenSelector implements Selector {

    public static String getHomepageSelector() {
        return "a.category-link";
    }

    public static String getCategorySelector() {
        return "div.rt-subcategory-item";
    }
}
