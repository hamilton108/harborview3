package harborview.controller;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.stockmarket.StockTicker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/maunaloa/stockoption")
public class StockOptionController {
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
}
