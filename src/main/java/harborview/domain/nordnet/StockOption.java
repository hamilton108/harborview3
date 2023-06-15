package harborview.domain.nordnet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import vega.financial.StockOptionType;

import static vega.financial.StockOptionType.CALL;
import static vega.financial.StockOptionType.PUT;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOption {
    private double ask;
    private double ivAsk;
    private double bid;
    private double ivBid;
    private double x;
    private int optionType;
    private int days;
    private String ticker;
    private String expiry;

    public StockOption() {
    }

    public StockOption(String ticker,
                       String expiry,
                       double ask,
                       double ivAsk,
                       double bid,
                       double ivBid,
                       double x,
                       int days,
                       int optionType) {
        this.ticker = ticker;
        this.expiry = expiry;
        this.ask = ask;
        this.ivAsk = ivAsk;
        this.bid = bid;
        this.ivBid = ivBid;
        this.x = x;
        this.days = days;
        this.optionType = optionType;
    }

    public double getAsk() {
        return ask;
    }

    @JsonSetter("sell")
    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getIvAsk() {
        return ivAsk;
    }

    @JsonSetter("ivSell")
    public void setIvAsk(double ivAsk) {
        this.ivAsk = ivAsk;
    }

    public double getBid() {
        return bid;
    }

    @JsonSetter("buy")
    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getIvBid() {
        return ivBid;
    }

    @JsonSetter("ivBuy")
    public void setIvBid(double ivBid) {
        this.ivBid = ivBid;
    }

    public double getX() {
        return x;
    }

    @JsonSetter("x")
    public void setX(double x) {
        this.x = x;
    }

    public StockOptionType getOptionType() {
        return optionType == 1 ?
                CALL :
                PUT;
    }

    @JsonSetter("ot")
    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public int getDays() {
        return days;
    }

    @JsonSetter("days")
    public void setDays(int days) {
        this.days = days;
    }

    public String getTicker() {
        return ticker;
    }

    @JsonSetter("ticker")
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExpiry() {
        return expiry;
    }

    @JsonSetter("expiry")
    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
