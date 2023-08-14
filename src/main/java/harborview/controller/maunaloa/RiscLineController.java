package harborview.controller.maunaloa;

import harborview.core.maunaloa.MaunaloaCore;
import harborview.domain.nordnet.RLine;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.StatusDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/maunaloa/risclines")
public class RiscLineController {

    private final MaunaloaCore maunaloaCore;

    public RiscLineController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    //public List<RLine> riscLines(@PathVariable("ticker") String ticker) {
    @ResponseBody
    @GetMapping(value = "/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RLine> riscLines(@PathVariable("ticker") int ticker) {
        var stockTicker = new StockTicker(ticker);
        return maunaloaCore.getRiscLines(stockTicker);
    }

    @ResponseBody
    @DeleteMapping(value = "/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO deleteAllRiscLines(@PathVariable("ticker") int ticker) {
        var stockTicker = new StockTicker(ticker);
        return maunaloaCore.deleteAllRiscLines(stockTicker);
    }

    @ResponseBody
    @GetMapping(value = "/spot/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String spot(@PathVariable("ticker") int ticker) {
        var stockTicker = new StockTicker(ticker);
        return maunaloaCore.spot(stockTicker);
    }
}
