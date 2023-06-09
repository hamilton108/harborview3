package harborview.domain.stockmarket;

import java.sql.Date;
import java.time.LocalDate;

public class StockOptionSale {
    // oid | purchase_id | dx | price | volume | status | transaction_cost

    private int oid;
    private int purchaseOid;
    private Date dx;
    private double price;
    private int volume;

    public StockOptionSale() {
    }
    public StockOptionSale(int purchaseOid,
                           double price,
                           int volume) {
        this.purchaseOid = purchaseOid;
        this.dx = Date.valueOf(LocalDate.now());
        this.price = price;
        this.volume = volume;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getPurchaseOid() {
        return purchaseOid;
    }

    public void setPurchaseOid(int purchaseOid) {
        this.purchaseOid = purchaseOid;
    }

    public Date getDx() {
        return dx;
    }

    public void setDx(Date dx) {
        this.dx = dx;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
