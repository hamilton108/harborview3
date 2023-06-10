package harborview.controller;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.nordnet.RLine;
import harborview.domain.stockmarket.StockTicker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/maunaloa/risclines")
public class RiscLineController {

    private final MaunaloaCore maunaloaCore;

    public RiscLineController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @ResponseBody
    @GetMapping(value = "/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RLine> riscLines(@PathVariable("ticker") String ticker) {
        var stockTicker = new StockTicker(ticker);
        return maunaloaCore.getRiscLines(stockTicker);
    }
}
