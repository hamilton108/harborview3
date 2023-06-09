package harborview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maunaloa")
public class MaunaloaController {

    public MaunaloaController() {
    }

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
    /*
    @ResponseBody
    @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String demo() {
        var result = maunaloaCore.demo();
        return result;
        //return new Demo(result);
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
     */
}
