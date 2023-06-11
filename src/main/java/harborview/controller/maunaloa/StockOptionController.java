package harborview.controller.maunaloa;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.stockmarket.PurchaseOptionRequest;
import harborview.domain.stockmarket.RegpurRequest;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.StatusDTO;
import harborview.dto.ValueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static harborview.dto.StatusCode.Ok;
import static harborview.dto.StatusCode.Error;

@Controller
@RequestMapping("/maunaloa/stockoption")
public class StockOptionController {

    private final Logger logger = LoggerFactory.getLogger(StockOptionController.class);

    private final MaunaloaCore maunaloaCore;

    public StockOptionController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @ResponseBody
    @GetMapping(value = "/calls/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calls(@PathVariable("oid") int oid) {
        return maunaloaCore.calls(new StockTicker(oid));
    }

    @ResponseBody
    @GetMapping(value = "/puts/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String puts(@PathVariable("oid") int oid) {
        return maunaloaCore.puts(new StockTicker(oid));
    }

    @ResponseBody
    @GetMapping(value = "/price/{ticker}/{stockPrice}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ValueDTO<Double> calcOptionPrice(@PathVariable("ticker") String ticker, @PathVariable("stockPrice") double stockPrice) {
        var price = maunaloaCore.optionPriceFor(new StockOptionTicker(ticker), stockPrice);
        return new ValueDTO<>(price);
    }

    @ResponseBody
    @PostMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO purchaseOption(@RequestBody PurchaseOptionRequest request) {
        logger.info(String.format("Purchasing option, ticker: %s", request.ticker()));
        return new StatusDTO(true, "demo", Ok.getStatus());
    }

    @ResponseBody
    @PostMapping(value = "/regpur", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO registerPurchasedOption(@RequestBody RegpurRequest request) {
        logger.info(String.format("Registering and purchasing option, ticker: %s", request.ticker()));
        return new StatusDTO(true, "demo", Ok.getStatus());
    }

    @ResponseBody
    @PostMapping(value = "/sell", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO sellOption() {
        return new StatusDTO(true, "demo", Ok.getStatus());
    }

}
