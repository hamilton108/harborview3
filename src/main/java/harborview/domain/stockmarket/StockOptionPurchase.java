package harborview.domain.stockmarket;

import com.fasterxml.jackson.annotation.JsonGetter;
import harborview.domain.critter.Critter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockOptionPurchase {
    private int oid;
    private LocalDate localDx;
    private int status;
    private double price;
    private double buyAtPurchase;
    private long volume;

    private String ticker;
    private String optionName;
    private String optionType;

    private int optionId;
    private int purchaseType;
    private double spotAtPurchase;
    //private Derivative myDerivative;

    // private EtradeRepository<Tuple<String>,Tuple2<String,File>> repository;
    private List<Critter> critters;
    private List<StockOptionSale> sales;

    public StockOptionPurchase() {}

    //endregion Init

    //region Properties
    @JsonGetter("oid")
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //region Dates
    public Date getDx() {
        return Date.valueOf(getLocalDx());
    }

    public void setDx(Date dx) {
        this.localDx = dx.toLocalDate();
    }
    public LocalDate getLocalDx() {
        if (localDx == null) {
            localDx = LocalDate.now();
        }
        return localDx;
    }

    public void setLocalDx(LocalDate localDx) {
        this.localDx = localDx;
    }

    //endregion Dates
    //--------------------------------------------------
    //------------- Expiry
    //--------------------------------------------------
    private LocalDate expiry;
    public LocalDate getExpiry() {
        return expiry;
    }

    public Date getExpirySql() {
        return Date.valueOf(expiry);
    }

    public void setExpirySql(Date expirySql) {
        this.expiry = expirySql.toLocalDate();
    }
    //--------------------------------------------------
    //------------- X
    //--------------------------------------------------
    private double x;
    public double getX() {
        return x;
    }

    public void setX(double value) {
        x = value;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public List<Critter> getCritters() {
        if (critters == null) {
            critters = new ArrayList<>();
        }
        return critters;
    }

    public void setCritters(List<Critter> critters) {
        this.critters = critters;
    }

    public List<StockOptionSale> getSales() {
        return sales;
    }
    public void setSales(List<StockOptionSale> sales) {
        this.sales = sales;
    }
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getPurchaseType() {
        return purchaseType;
    }
    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public double getSpotAtPurchase() {
        return spotAtPurchase;
    }
    public void setSpotAtPurchase(double spotAtPurchase) {
        this.spotAtPurchase = spotAtPurchase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBuyAtPurchase() {
        return buyAtPurchase;
    }

    public void setBuyAtPurchase(double buyAtPurchase) {
        this.buyAtPurchase = buyAtPurchase;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }
    //endregion Properties

    private double spot;
    public void setSpot(double value) {
        spot = value;
    }
    public double getSpot() {
        return spot;
    }
    private Double _watermark = null;
    public void setWatermark(double value) {
        _watermark = value;
    }
    public Double getWatermark() {
        if ((_watermark == null) || (stockOptionBuy > _watermark)) {
            _watermark = stockOptionBuy;
        }
        //region Obsolete
        /*
        if (_watermark == null) {
            _watermark = curDeriv.getBuy();
        }
        else {
            if (curDeriv.getBuy() > _watermark) {
                _watermark = curDeriv.getBuy();
            }
        }
        */
        //endregion Obsolete
        return _watermark;
    }

    //endregion

    //region Utility Methods
    public long remainingVolume() {
        return volume - volumeSold() - volumeActiveCritters();
    }
    public long volumeActiveCritters() {
        if ((critters == null) || (critters.size() == 0)) return 0;

        return critters.stream().mapToLong(crit -> {
            if (crit.getStatus() == 7) {
                return crit.getSellVolume();
            }
            else {
                return 0;
            }
        }).sum();
    }
    public long volumeSold() {
        if ((sales == null) || (sales.size() == 0)) return 0;

        return sales.stream().mapToLong(StockOptionSale::getVolume).sum();
    }
    /*
    public void addSale(StockOptionSale sale) {
        if (sales == null) {
            sales = new ArrayList<>();
        }
        sales.add(sale);
        if (isFullySold()) {
            status = 2;
        }
    }
    */

    public boolean isFullySold() {
        return volumeSold() < volume ? false : true;
    }

    /*
    private SellRuleArgs collectArgs() {
        StockPrice spot = getSpot();
        if (spot == null) {
            return null;
        }

        if ((_watermark == null) || (stockOptionBuy > _watermark)) {
            //log.info("Changing watermark from {} to {}",_watermark, p.getBuy());
            _watermark = stockOptionBuy;
        }
        double dfb = getPrice() - stockOptionBuy; //p.getBuy();
        double dfw = _watermark - stockOptionBuy; //p.getBuy();

        SellRuleArgs result = new SellRuleArgs(dfb,dfw,spot.getCls(),getPrice());
        return result;
    }
    */

    public void inspect() {
        System.out.println("\n\n********************************* Critters **********************************");
        System.out.println(String.format("[%d] Option name: %s, price: %.2f, volume: %d, rem.vol: %d", oid, optionName, price, volume, remainingVolume()));
        if (critters == null || critters.size() == 0) {
            System.out.println("\tNo critters");
        }
        else {
            for (Critter c : critters) {
                c.inspect();
            }
        }
    }

    //endregion Utility Methods

    //region interface OptionPurchase
    /*
    public List<Critter> acceptedForSale() {
        List<Critter> result = new ArrayList<>();
        if (isFullySold()) {
            return result;
        }
        List<Critter> crits = getCritters().stream().filter(c -> c.getStatus() == 7).collect(Collectors.toList());
        SellRuleArgs args = collectArgs();
        if (args == null) {
            return result;
        }
        crits.forEach(c -> {
            if (c.apply(args)) {
                result.add(c);
            }
        });
        return result;
    }
    */

    public String getOptionName() {
        return optionName;
    }

    private double stockOptionBuy = 0.0;
    public double getStockOptionBuy() {
        return stockOptionBuy; //repository.findDerivativePrice(new Tuple<>(ticker,optionName));
    }
    public void setStockOptionBuy(double value) {
        stockOptionBuy = value;
    }
}
