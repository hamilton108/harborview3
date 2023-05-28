package harborview.domain.nordnet;

import com.fasterxml.jackson.annotation.JsonSetter;

public class StockPrice {
    private double hi;
    private double lo;
    private double open;
    private double close;
    private long unixTime;

    public StockPrice() {
    }

    public StockPrice(double open, double hi, double lo, double close, long unixTime) {
        this.open = open;
        this.hi = hi;
        this.lo = lo;
        this.close = close;
        this.unixTime = unixTime;
    }

    public double getHi() {
        return hi;
    }

    @JsonSetter("h")
    public void setHi(double hi) {
        this.hi = hi;
    }

    public double getLo() {
        return lo;
    }

    @JsonSetter("l")
    public void setLo(double lo) {
        this.lo = lo;
    }

    public double getOpen() {
        return open;
    }

    @JsonSetter("o")
    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }


    @JsonSetter("c")
    public void setClose(double close) {
        this.close = close;
    }

    public long getUnixTime() {
        return unixTime;
    }

    @JsonSetter("unix-time")
    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }
}
