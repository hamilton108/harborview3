package harborview.controller.maunaloa;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.stockmarket.request.PurchaseOptionRequest;
import harborview.domain.stockmarket.request.RegpurRequest;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.StatusDTO;
import harborview.dto.ValueDTO;
import harborview.transform.maunaloa.RequestTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static harborview.dto.StatusCode.*;

@Controller
@RequestMapping("/maunaloa/stockoption")
public class StockOptionController {

    private final Logger logger = LoggerFactory.getLogger(StockOptionController.class);

    private final MaunaloaCore maunaloaCore;

    private final RequestTransform requestTransform;

    public StockOptionController(MaunaloaCore maunaloaCore,
                                 RequestTransform requestTransform) {
        this.maunaloaCore = maunaloaCore;
        this.requestTransform = requestTransform;
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
        /*
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            var errorMessages = StockOptionUtil.validationErrorsToString(result.getAllErrors());
            logger.error(errorMessages);
            return new StatusDTO(false, errorMessages, REQUEST_VALIDATION_ERROR.getStatus());
        }

         */
        try {
            request.validate();
        }
        catch (Exception ex) {
            logger.error(String.format("Request validation error, ticker: %s", request.ticker()));
            return new StatusDTO(false, ex.getMessage(), REQUEST_VALIDATION_ERROR.getStatus());
        }

        logger.info(String.format("Purchasing option, ticker: %s", request.ticker()));
        var purchase = requestTransform.mapPurchaseOptionRequest(request);
        if (purchase == null) {
            return new StatusDTO(false, "Option was null", RETRY.getStatus());
        }
        return maunaloaCore.purchaseOption(purchase);
    }

    @ResponseBody
    @PostMapping(value = "/regpur", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO registerPurchasedOption(@RequestBody RegpurRequest request) {
        logger.info(String.format("Registering and purchasing option, ticker: %s", request.ticker()));
        try {
            request.validate();
        }
        catch (Exception ex) {
            logger.error(String.format("Request validation error, ticker: %s", request.ticker()));
            return new StatusDTO(false, ex.getMessage(), REQUEST_VALIDATION_ERROR.getStatus());
        }

        var purchase = requestTransform.mapRegPurRequest(request);

        if (purchase.first() == null || purchase.second() == null) {
            return new StatusDTO(false, "Could not transform RegpurRequest", ERROR.getStatus());
        }

        return maunaloaCore.registerAndPurchaseOption(purchase);
    }

    @ResponseBody
    @PostMapping(value = "/sell", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO sellOption() {

        return new StatusDTO(true, "demo", OK.getStatus());
    }

    /*
    @ResponseBody
    @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO demo() {
        maunaloaCore.demo();
        return new StatusDTO(true, "demo", OK.getStatus());
    }
     */

}
