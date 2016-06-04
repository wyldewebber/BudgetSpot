package za.co.wyldeweb.budgetapp.DTO;

import java.util.Calendar;

/**
 * Created by Anton on 2016/03/23.
 */
public class Income {
    private int _id;
    private String description;
    private double amount;
    private Calendar date;
    private Calendar dateAdded;

    public Income(int _id, String description, double amount, Calendar date, Calendar dateAdded) {
        this._id = _id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }
}
