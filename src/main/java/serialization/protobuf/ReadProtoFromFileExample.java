package serialization.protobuf;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadProtoFromFileExample {

    private static final String exampleFile = "src/main/java/serialization/protobuf/WriteProtoToFileExample";

    public static void main(String[] args) {
        try {
            Websites websites = Websites.parseFrom(new FileInputStream(exampleFile));
            printProto(websites);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printProto(Websites websites) {
        for (Website website : websites.getWebsiteList()) {
            System.out.println(website.getName());
            System.out.println(website.getContent());
            System.out.println(website.getUrl());
            System.out.println("#########");
        }
    }
}
