package harborview.adapter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import harborview.adapter.NordnetAdapter;
import harborview.domain.nordnet.FindOptionResponse;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockTicker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Component("comp2")

@Component()
public class NordnetAdapterImpl implements NordnetAdapter  {

    private final HttpClient client = HttpClient.newHttpClient();

    private final String nordnetHost;

    public NordnetAdapterImpl(@Value("${adapter.nordnet.nordnet-host}") String nordnetHost) {
        this.nordnetHost = nordnetHost;
    }

    @Override
    public FindOptionResponse findOption(StockOptionTicker ticker) {
        try {
            var uri = optionUri(ticker);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), FindOptionResponse.class);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String putsOrCalls(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String calls(StockTicker ticker) {
        var uri = optionUri(ticker, true);
        return putsOrCalls(uri);
    }

    @Override
    public String puts(StockTicker ticker) {
        var uri = optionUri(ticker, false);
        return putsOrCalls(uri);
    }

    @Override
    public String demo(StockOptionTicker ticker) {
        /*
        //String uri = String.format("%s/option/YAR3A528.02X", nordnetHost);

        //var uri = optionUri(ticker);

        var uri = String.format("%s/calls/3", nordnetHost);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            var mapped = objectMapper.readValue(response.body(), FindOptionResponse.class);
            System.out.println(mapped);

            return response.body();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
             */
       return  "" ;

    }

    private String optionUri(StockOptionTicker ticker) {
        return String.format("%s/option/%s", nordnetHost, ticker.ticker());
    }
    private String optionUri(StockTicker ticker, boolean isCalls) {
        var ot = isCalls == true ? "calls" : "puts";
        return String.format("%s/%s/%d", nordnetHost, ot, ticker.oid());
    }

        /*
    {
        "stock-price": {
        "h": 458.9,
                "l": 450.6,
                "c": 452.6,
                "o": 452.0,
                "unix-time": 1685113860000
    },
        "option": {
        "brEven": 0.0,
                "expiry": "2023-01-20",
                "ticker": "YAR3A528.02X",
                "days": 117,
                "ivSell": 0.1375,
                "sell": 0.6,
                "buy": 0.0,
                "ivBuy": 0.05,
                "ot": 1,
                "x": 528.02
    }

    }
     */

}
