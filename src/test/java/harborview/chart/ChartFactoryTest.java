package harborview.chart;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import harborview.domain.stockmarket.StockPrice;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ChartFactoryTest {
    ChartMonthFactory factory;

    @BeforeEach
    public void init() {
        factory = new ChartMonthFactory();
    }

    @Test
    public void monthPrices_null_toStockPrice() {
        StockPrice price = factory.monthToStockPrice(null);
        StockPrice expected = new StockPrice(LocalDate.of(2019,1,1), 100,120,90, 110,1000000);
        assertThat(expected).isEqualTo(price);
    }

    @Test
    public void monthPrices_one_item_toStockPrice() {
        StockPrice expected = createStockPrice(1,1,100,120,90,110, 1000000);
        List<StockPrice> items = new ArrayList<>();
        items.add(expected);
        StockPrice price = factory.monthToStockPrice(items);
        assertThat(expected).isEqualTo(price);
    }

    @Test
    public void monthPrices_toMonthChart() {
        List<StockPrice> jan = monthlyStockPrices(1, 0, 0);
        List<StockPrice> feb = monthlyStockPrices(2, -10, 0);
        List<StockPrice> mar = monthlyStockPrices(3, -25, 0);
        List<StockPrice> apr = monthlyStockPrices(4, 20, 0);
        List<StockPrice> may = monthlyStockPrices(5, 15, 0);
        jan.addAll(feb);
        jan.addAll(mar);
        jan.addAll(apr);
        jan.addAll(may);
        Map<Integer, Map<Integer, List<StockPrice>>> tmx = factory.treeMap(jan);
        List<StockPrice> pricesByMonth = factory.pricesByMonth(tmx);
        assertThat(5).isEqualTo(pricesByMonth.size());

        StockPrice p0 = pricesByMonth.get(0);
        StockPrice p0e = createStockPrice(1,1,110,145,80,140,10000000);
        assertThat(p0e).isEqualTo(p0);

        StockPrice p1 = pricesByMonth.get(1);
        StockPrice p1e = createStockPrice(2,1,100,135,70,130,10000000);
        assertThat(p1e).isEqualTo(p1);
    }

    private StockPrice createStockPrice(int month, int day, double o, double hi, double lo, double c, long vol) {
        return new StockPrice(LocalDate.of(2019,month,day), o,hi,lo, c,vol);
    }
    private List<StockPrice> monthlyStockPrices(int m, double addToPrices, long addToVolume) {
        List<StockPrice> items = new ArrayList<>();
        items.add(createStockPrice(m,2,100,120,90,110+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,3,105,125,95,115+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,4,95,110,90,105+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,7,90,100,80,80+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,8,80,90,80,85+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,9,100,120,100,100+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,10,110,125,100,105+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,11,120,135,120,145+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,14,115,115,90,90+addToPrices,1000000+addToVolume));
        items.add(createStockPrice(m,15,130,140,130,140+addToPrices,1000000+addToVolume));
        return items;
    }
}
