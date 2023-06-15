package harborview.controller;

import harborview.adapter.NordnetAdapter;
import harborview.adapter.StockMarketAdapter;
import harborview.core.critter.CritterCore;
import harborview.core.maunaloa.MaunaloaCore;
import harborview.mybatis.MyBatisUtil;
import harborview.transform.maunaloa.RequestTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected NordnetAdapter nordnetAdapter;

    @MockBean
    protected StockMarketAdapter stockMarketAdapter;

    @MockBean
    protected MyBatisUtil myBatisUtil;

}
