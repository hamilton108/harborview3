package harborview.controller.critter;

import harborview.domain.core.critter.CritterCore;
import harborview.dto.StatusDTO;
import harborview.dto.critter.OptionPurchaseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static harborview.dto.StatusCode.OK;
import static harborview.dto.StatusCode.ERROR;

@Controller
@RequestMapping("/critter")
public class CritterController {

    private final CritterCore critterCore;

    public CritterController(CritterCore critterCore) {
        this.critterCore = critterCore;
    }


    @GetMapping(value = "/overlook")
    public String charts() {
        return "critter/overlook";
    }

    @ResponseBody
    @GetMapping(value = "/purchases/{ptype}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OptionPurchaseDTO> purchases(@PathVariable("ptype") int ptype) {
        return critterCore.activePurchasesWithCritters(ptype);
    }

    @ResponseBody
    @GetMapping(value = "/purchases/toggle/{rulecategory}/{oid}/{active}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO toggleRule(@PathVariable("rulecategory") int rulecategory,
                                @PathVariable("oid") int ruleId,
                                @PathVariable("active") boolean isActive) {
        var isAccRule = rulecategory == 1;
        try {
            critterCore.toggleRule(ruleId, isActive, isAccRule);
            return new StatusDTO(true,
                    String.format("Toggled %s with oid %d %s ok",
                            isAccRule ? "accRule" : "denyRule",
                            ruleId,
                            isActive ? "active" : "inactive"),
                    OK.getStatus());
        }
        catch (Exception ex) {
            return new StatusDTO(false, String.format("Error: %s", ex.getMessage()), ERROR.getStatus());
        }
    }
}
