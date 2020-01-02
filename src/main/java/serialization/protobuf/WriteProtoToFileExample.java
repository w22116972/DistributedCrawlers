package serialization.protobuf;

import serialization.protobuf.Website;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteProtoToFileExample {
    public static void main(String[] args) {
        Website helloWebsite = Website.newBuilder()
                .setContent("Hello World Content")
                .setName("Hello Website Name")
                .setUrl("www.google.com")
                .build();

        Website helloWebsite2 = Website.newBuilder()
                .setContent("Hello 2 Cotent")
                .setName("Hello 2 Name")
                .setUrl("www.youtube.com")
                .build();

        final Websites.Builder websitesBuilder = Websites.newBuilder();
        // add object from code
        websitesBuilder.addWebsite(helloWebsite);
        websitesBuilder.addWebsite(helloWebsite2);
        // add object from file
//        try {
//            websitesBuilder.mergeFrom(new FileInputStream(args[0]));
//        } catch (FileNotFoundException e) {
//            System.out.println(args[0] + ": File not found.  Creating a new file.");
//        }

        try (FileOutputStream output = new FileOutputStream("WriteProtoToFileExample");) {
            websitesBuilder.build().writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
