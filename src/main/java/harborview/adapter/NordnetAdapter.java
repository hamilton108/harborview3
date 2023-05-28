package harborview.adapter;

import harborview.domain.nordnet.FindOptionResponse;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockOptionTicker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface NordnetAdapter {
    String demo(StockOptionTicker ticker);
    FindOptionResponse findOption(StockOptionTicker ticker);

}
