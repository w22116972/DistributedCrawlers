package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BodyHandler {

    private static final String findTitle(String body) {
        return Jsoup.parse(body).title();
    }

    public static final Function<String, String> filterTitle = s -> findTitle(s);


    // example
    public static final Pattern FILTER = Pattern.compile("");

    public static final Function<String, String> processResult = s -> s;

}
