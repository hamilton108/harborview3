package harborview.chart;

import com.google.common.collect.Lists;
import harborview.domain.stockmarket.StockPrice;
import harborview.dto.html.Candlestick;
import harborview.dto.html.Chart;
import harborview.dto.html.Charts;
import vega.filters.Filter;
import vega.filters.ehlers.CyberCycle;
import vega.filters.ehlers.Itrend;
import vega.filters.ehlers.RoofingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class ChartFactory {
    private final Filter calcItrend10 = new Itrend(10);
    private final Filter calcItrend50 = new Itrend(50);
    private final Filter calcCyberCycle10 = new CyberCycle(10);
    private final Filter roofingFilter = new RoofingFilter();

    private final int startYear = 2010;
    private final LocalDateTime startDate = LocalDateTime.of(startYear,1,1,0,0);
    private final LocalDate startDatex = LocalDate.of(startYear,1,1);

    public int skipNum(int totalNum) {
        return totalNum - 400;
    }
    private double roundToNumDecimals(double value) {
        return roundToNumDecimals(value,10.0);
    }
    private double roundToNumDecimals(double value, double roundFactor) {
        double tmp = Math.round(value*roundFactor);
        return tmp/roundFactor;
    }
    private String toIso8601(LocalDate d) {
        int year = d.getYear();
        int month = d.getMonthValue();
        int day = d.getDayOfMonth();
        return String.format("%d-%02d-%02d", year,month,day);
    }
    private long unixTime() {
        return startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    private long hRuler(LocalDate d) {
        return ChronoUnit.DAYS.between(startDatex,d);
    }
    private List<Double> calculateFilter(Filter filter, List<Double> values) {
        return filter.calculate(values).stream()
                .map(this::roundToNumDecimals).collect(Collectors.toList());
    }

    private final int TAKE = 100;
    private final boolean RETURN_ALL_ITEMS = true;
    private Chart mainChart(List<Double> spots, List<StockPrice> winSpots) {
        List<Double> itrend10 = calculateFilter(calcItrend10, spots);
        List<Double> itrend50 = calculateFilter(calcItrend50, spots);
        List<Candlestick> candlesticks = winSpots.stream().map(Candlestick::new).collect(Collectors.toList());
        Chart chart = new Chart();

        if (RETURN_ALL_ITEMS) {
            chart.addLine(Lists.reverse(itrend10)); 
            chart.addLine(Lists.reverse(itrend50));
            chart.setCandlesticks(Lists.reverse(candlesticks)); 
        }
        else {
            chart.addLine(Lists.reverse(itrend10).stream().limit(TAKE).collect(Collectors.toList()));
            chart.addLine(Lists.reverse(itrend50).stream().limit(TAKE).collect(Collectors.toList()));
            chart.setCandlesticks(Lists.reverse(candlesticks).stream().limit(TAKE).collect(Collectors.toList()));
        }

        return chart;
    }
    private Chart cyberCycleChart(List<Double> spots) {
        Chart chart = new Chart();
        List<Double> cc10 = calculateFilter(calcCyberCycle10, spots);
        List<Double> cc10rf = calculateFilter(roofingFilter, spots);

        if (RETURN_ALL_ITEMS) {
            chart.addLine(Lists.reverse(cc10)); 
            chart.addLine(Lists.reverse(cc10rf)); 
        }
        else {
            chart.addLine(Lists.reverse(cc10).stream().limit(TAKE).collect(Collectors.toList()));
            chart.addLine(Lists.reverse(cc10rf).stream().limit(TAKE).collect(Collectors.toList()));
        }

        return chart;
    }

    private Chart volumeChart(List<StockPrice> spots) {
        Chart chart = new Chart();
        List<Double> vol = spots.stream().map(x -> (double)x.getVolume()).collect(Collectors.toList());
        OptionalDouble maxVol = vol.stream().mapToDouble(v -> v).max();
        maxVol.ifPresent(v -> {
            List<Double> normalized = vol.stream().map(x -> x/v).collect(Collectors.toList());

            if (RETURN_ALL_ITEMS) {
                chart.addBar(Lists.reverse(normalized));
            }
            else {
                chart.addBar(Lists.reverse(normalized).stream().limit(TAKE).collect(Collectors.toList()));
            }

        });
        return chart;
    }
    public Charts elmCharts(String ticker, Collection<StockPrice> prices) {
        Charts result = new Charts();

        result.setTicker(ticker);

        int totalNum = prices.size();
        //int 
        List<StockPrice> winSpots = prices.stream().skip(skipNum(totalNum)).collect(Collectors.toList());
        List<Double> spots = winSpots.stream().map(x -> x.getCls()).collect(Collectors.toList());

        result.setChart(mainChart(spots,winSpots));
        result.setChart2(cyberCycleChart(spots));
        result.setChart3(volumeChart(winSpots));

        List<LocalDate> dx = winSpots.stream().map(StockPrice::getLocalDx).collect(Collectors.toList());
        List<Long> xAxis = dx.stream().map(this::hRuler).collect(Collectors.toList());

        if (RETURN_ALL_ITEMS == true) {
            result.setxAxis(Lists.reverse(xAxis));
        }
        else {
            result.setxAxis(Lists.reverse(xAxis).stream().limit(TAKE).collect(Collectors.toList()));
        }

        result.setMinDx(unixTime());
        return result;
    }
}
