package harborview.controller;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.nordnet.RiscRequest;
import harborview.domain.nordnet.RiscResponse;
import harborview.domain.stockmarket.StockPrice;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/maunaloa")
public class MaunaloaController {

    private final MaunaloaCore maunaloaCore;

    public MaunaloaController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    //------------------------ Thymeleaf ------------------------
    @GetMapping(value = "/charts")
    public String charts() {
        return "maunaloa/charts";
    }

    @GetMapping(value = "/stockoption")
    public String stockOption() {
        return "maunaloa/options";
    }

    @GetMapping(value = "/stockoption/purchases")
    public String optionPurchases() {
        return "maunaloa/optionpurchases";
    }

    //------------------------ Json ------------------------

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

    @ResponseBody
    @GetMapping(value = "/risclines/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int riscLines() {
        return 2;
    }

    @ResponseBody
    @PostMapping(value = "/stockprice/calculate/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RiscResponse> calcRiscStockPrices(@PathVariable("oid") int oid, @RequestBody List<RiscRequest> riscs) {
        return maunaloaCore.calcRiscStockPrices(riscs);
    }


    @ResponseBody
    @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Demo demo() {
        var result = maunaloaCore.demo();
        return new Demo(result);
    }
    public static class Demo {
        private String msg;
        public Demo(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}
