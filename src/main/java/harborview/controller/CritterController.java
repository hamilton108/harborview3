package harborview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/critter")
public class CritterController {

    @GetMapping(value = "/overlook")
    public String charts() {
        return "critter/overlook";
    }

    @GetMapping(value = "/purchases/{ptype} ")
    public String purchases(@PathVariable("ptype") int ptype) {
        return "";
    }
}
