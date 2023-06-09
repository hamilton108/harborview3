package harborview.controller;

import harborview.core.maunaloa.MaunaloaCore;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/maunaloa/risclines")
public class RiscLineController {

    private final MaunaloaCore maunaloaCore;

    public RiscLineController(MaunaloaCore maunaloaCore) {
        this.maunaloaCore = maunaloaCore;
    }

    @ResponseBody
    @GetMapping(value = "/{oid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int riscLines() {
        return 2;
    }
}
