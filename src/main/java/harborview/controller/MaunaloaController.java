package harborview.controller;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.stockmarket.StockPrice;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@RequestMapping("/maunaloa")
public class MaunaloaController {

    private final MaunaloaCore maunaloaCore;

    public MaunaloaController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @GetMapping(value = "/charts")
    public String charts() {
        return "maunaloa/charts";
    }

    @ResponseBody
    @GetMapping(value = "/stockprice/tickers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<SelectItem> tickers() {
        return maunaloaCore.getStockTickers();
    }

    @ResponseBody
    @GetMapping(value = "/stockprice/days/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts days(@PathVariable("oid") int oid) {
        return maunaloaCore.days(oid);
    }
    @ResponseBody
    @GetMapping(value = "/stockprice/weeks/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts weeks(@PathVariable("oid") int oid) {
        return maunaloaCore.weeks(oid);
    }
    @ResponseBody
    @GetMapping(value = "/stockprice/months/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Charts months(@PathVariable("oid") int oid) {
        return maunaloaCore.months(oid);
    }
}
