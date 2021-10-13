package pl.sages.javadevpro.concurrency.homework;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SourcesCounter {


    Map<String, Integer> map = new HashMap<>();
    NewsApiClient newsApiClient = new NewsApiClient("a75c36d4503f43f0b1cf3fa646d19107");

    public static void main(String[] args) throws InterruptedException {
        new SourcesCounter().invoke();
    }

    private void invoke() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            Thread t = new Thread(() -> {
                EverythingRequest query = new EverythingRequest.Builder().from("2021-09-09")
                                                                         .sortBy("publishedAt")
                                                                         .q("tesla")
                                                                         .build();
                newsApiClient.getEverything(query, new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse articleResponse) {
                        List<Article> articles = articleResponse.getArticles();
                        for (Article article : articles) {
                            String sourceName = article.getSource().getName();

                            map.compute(sourceName, (key, value) -> (value == null) ? 1 : value + 1);
                            System.out.println(map);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            });
            t.start();
            t.join();
        }
    }
}
