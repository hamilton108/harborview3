package harborview.controller.maunaloa;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/maunaloa/stockprice")
public class StockPriceController {

    private final MaunaloaCore maunaloaCore;

    public StockPriceController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @ResponseBody
    @GetMapping(value = "/tickers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<SelectItem> tickers() {
        return maunaloaCore.getStockTickers();
    }

    @ResponseBody
    @GetMapping(value = "/days/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts days(@PathVariable("oid") int oid) {
        return maunaloaCore.days(new StockTicker(oid));
    }

    @ResponseBody
    @GetMapping(value = "/weeks/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts weeks(@PathVariable("oid") int oid) {
        return maunaloaCore.weeks(new StockTicker(oid));
    }

    @ResponseBody
    @GetMapping(value = "/months/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts months(@PathVariable("oid") int oid) {
        return maunaloaCore.months(new StockTicker(oid));
    }

    @ResponseBody
    @PostMapping(value = "/calculate/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RiscResponse> calcRiscStockPrices(@PathVariable("oid") int oid, @RequestBody List<RiscRequest> riscs) {
        return maunaloaCore.calcRiscStockPrices(riscs);
    }

}
