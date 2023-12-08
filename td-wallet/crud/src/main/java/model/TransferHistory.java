package model;

import java.time.LocalDateTime;

public class TransferHistory {
    private int id;
    private int idTransactionDebiteur;
    private int idTransactionCrediteur;
    private LocalDateTime dateTransfert;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTransactionDebiteur() {
        return idTransactionDebiteur;
    }

    public void setIdTransactionDebiteur(int idTransactionDebiteur) {
        this.idTransactionDebiteur = idTransactionDebiteur;
    }

    public int getIdTransactionCrediteur() {
        return idTransactionCrediteur;
    }

    public void setIdTransactionCrediteur(int idTransactionCrediteur) {
        this.idTransactionCrediteur = idTransactionCrediteur;
    }

    public LocalDateTime getDateTransfert() {
        return dateTransfert;
    }

    public void setDateTransfert(LocalDateTime dateTransfert) {
        this.dateTransfert = dateTransfert;
    }
}
