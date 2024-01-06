package harborview.api.maunaloa;

import harborview.domain.core.maunaloa.MaunaloaCore;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/maunaloa/stockprice")
public class StockPriceAPI {

    private final MaunaloaCore maunaloaCore;

    public StockPriceAPI(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @GetMapping(value = "/tickers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SelectItem>> tickers() {
        return ResponseEntity.ok(maunaloaCore.getStockTickers());
    }

    @GetMapping(value = "/days/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charts> days(@PathVariable("oid") int oid) {
        return ResponseEntity.ok(maunaloaCore.days(new StockTicker(oid)));
    }

    @GetMapping(value = "/weeks/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charts> weeks(@PathVariable("oid") int oid) {
        return ResponseEntity.ok(maunaloaCore.weeks(new StockTicker(oid)));
    }

    @GetMapping(value = "/months/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charts> months(@PathVariable("oid") int oid) {
        return ResponseEntity.ok(maunaloaCore.months(new StockTicker(oid)));
    }

    @PostMapping(value = "/calculate/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RiscResponse>> calcRiscStockPrices(@PathVariable("oid") int oid, @RequestBody List<RiscRequest> riscs) {
        return ResponseEntity.ok(maunaloaCore.calcRiscStockPrices(riscs));
    }

}
