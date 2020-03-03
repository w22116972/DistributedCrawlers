package example;

import javax.xml.bind.Element;
import java.util.List;
import java.util.concurrent.Executor;

public class Concurrency<E> {

    void processSequentially(List<E> elements) {
        for (E e: elements) {
            // process e
        }
    }

    void processInParallel(Executor executor, List<E> elements) {
        for (final E e: elements) {
            executor.execute(() -> {
                // process e
            });
        }
    }
}
