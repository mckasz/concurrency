package pl.sages.javadevpro.concurrency.future;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class CompletableFuture_task {
    OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        new CompletableFuture_task().invoke();
    }

    private void invoke() throws Exception {
//        String currency1 = "EUR";
//        String currency2 = "GBP";
//        String conversionRate = getConversionRate(currency1, currency2);
//        System.out.println(conversionRate);
        // TODO: Użyj completable future w ten sposób by uzyskać wartości dla 2 różnych par
        CompletableFuture<String> usdChf = CompletableFuture.supplyAsync(() -> getConversionRate("USD", "CHF"));
        CompletableFuture<String> usdPln = CompletableFuture.supplyAsync(() -> getConversionRate("USD", "PLN"));
        CompletableFuture<Double> biggerRate = usdChf.thenCombine(usdPln, (pair1, pair2) ->
        {
            System.out.println("USD/CHF: " + pair1 + "\nUSD/PLN: " + pair2);
            return Math.max(Double.parseDouble(pair1), Double.parseDouble(pair2));
        });
        System.out.println(biggerRate.get());
    }

    private String getConversionRate(String currency1, String currency2) {
        Request request = new Request.Builder()
                .url(" https://v6.exchangerate-api.com/v6/11ef7ae4aa67097f2ec7eb9f/pair/" + currency1 + "/" + currency2)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String content = Objects.requireNonNull(response.body()).string();
            ObjectMapper mapper = new ObjectMapper();

            Pair pair = mapper.readValue(content, Pair.class);
            return pair.conversionRate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Pair {
        public final String result;
        public final String documentation;
        public final String termsOfUse;
        public final String timeLastUpdateUnix;
        public final String timeLastUpdateUtc;
        public final String timeNextUpdateUnix;
        public final String timeNextUpdateUtc;
        public final String baseCode;
        public final String targetCode;
        public final String conversionRate;

        public Pair(@JsonProperty("result") String result,
                    @JsonProperty("documentation") String documentation,
                    @JsonProperty("terms_of_use") String termsOfUse,
                    @JsonProperty("time_last_update_unix") String timeLastUpdateUnix,
                    @JsonProperty("time_last_update_utc") String timeLastUpdateUtc,
                    @JsonProperty("time_next_update_unix") String timeNextUpdateUnix,
                    @JsonProperty("time_next_update_utc") String timeNextUpdateUtc,
                    @JsonProperty("base_code") String baseCode,
                    @JsonProperty("target_code") String targetCode,
                    @JsonProperty("conversion_rate") String conversionRate) {
            this.result = result;
            this.documentation = documentation;
            this.termsOfUse = termsOfUse;
            this.timeLastUpdateUnix = timeLastUpdateUnix;
            this.timeLastUpdateUtc = timeLastUpdateUtc;
            this.timeNextUpdateUnix = timeNextUpdateUnix;
            this.timeNextUpdateUtc = timeNextUpdateUtc;
            this.baseCode = baseCode;
            this.targetCode = targetCode;
            this.conversionRate = conversionRate;
        }
    }

}
