package util;

public class AsyncDatabaseAccess {

//    public CompletionStage<List<Item>> itemsForAnswer(DataSource ds, int answer) {
//        String sql = "select id, name, answer from tab where answer = :target";
//        try (Session session = ds.getSession()) {
//            return session.<List<Item>>rowOperation(sql)
//                    .set("target", answer, AdbaType.NUMERIC)
//                    .collect(Collectors.mapping(
//                            row -> new Item(row.at("id").get(Integer.class),
//                                    row.at("name").get(String.class),
//                                    row.at("answer").get(Integer.class)),
//                            Collectors.toList()))
//                    .submit()
//                    .getCompletionStage();
//        }
//    }
}
