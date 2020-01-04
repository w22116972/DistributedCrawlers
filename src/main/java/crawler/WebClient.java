package crawler;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class WebClient {

    private AsyncHttpClient client;

    public WebClient() {
        this.client = Dsl.asyncHttpClient(buildConfigBuilder());
    }

    private DefaultAsyncHttpClientConfig.Builder buildConfigBuilder() {
        return Dsl.config()
                .setConnectTimeout(500);  // maybe read from config
        // .setProxyServer(new ProxyServer(...));
    }

    public static boolean isValidURL(String url) {
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

    public CompletableFuture<String> getBodyWithCompletableFuture(String url) throws IllegalStateException {
        if (!isValidURL(url)) {
            // log.error("");
            System.out.println("Url is invalid");
            throw new IllegalStateException("Url is invalid");
        }
        return this.client
                .prepareGet(url)
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    // log.error("...");
                    return null;
                })
                .thenApply(Response::getResponseBody);
    }

    public Optional<String> getBodyWithSynchronousFuture(String url) {
        try {
            return Optional.of(this.client.prepareGet(url).execute().get().getResponseBody());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public String getBodyWithHttpUrlConnection(String url) {
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            try(InputStream raw = connection.getInputStream()) {
                return convertInputStreamToStringByStream(raw);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertInputStreamToStringByStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    private String convertInputStreamToStringByByteArrayOutputStream(InputStream inputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (IOException e) {

        }
        return null;
    }

//    private String getStringFromInputStream(InputStream in) {
//        InputStream buffer = new BufferedInputStream(in);
//        Reader reader = new InputStreamReader(buffer);
////        reader.
//
//    }

//    public void get(String url) {
//        this.client.prepareGet(url).execute(new AsyncCompletionHandler<Object>() {
//            @Override
//            public Object onCompleted(Response response) throws Exception {
//                return response;
//            }
//        });
//
//    }

    public void close() {
        try {
            this.client.close();
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }


//    // https://prod-edxapp.edx-cdn.org/assets/courseware/v1/da649d2e82fec592ca152edd01ff37b5/asset-v1:EPFLx+scala-reactiveX+2T2019+type@asset+block/reactive2-4-scaled.pdf
//    public String get(String url) {
//        final ListenableFuture<Response> responseListenableFuture = client.prepareGet(url).execute();
//        final Promise<String> promise = null;
//        responseListenableFuture.addListener(new Runnable() {
//            public void run() {
//                Response response = null;
//                try {
//                    response = responseListenableFuture.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                if (response.getStatusCode() < 400) {
//                    promise.setSuccess(response.getResponseBody());
//                } else {
//                    promise.setFailure(new RuntimeException("BAD_REQUEST"));
//                }
//            }
//        }, );
//
//    }


}
