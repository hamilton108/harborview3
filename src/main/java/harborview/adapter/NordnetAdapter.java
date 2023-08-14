package harborview.adapter;

import harborview.domain.nordnet.FindOptionResponse;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockTicker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface NordnetAdapter {
    FindOptionResponse findOption(StockOptionTicker ticker);
    String calls(StockTicker ticker);
    String puts(StockTicker ticker);
    String spot(StockTicker ticker);

}
