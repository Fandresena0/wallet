package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Compte {
    private int id_compte;
    private String nom;
    private Double solde;
    private List<Transaction> transactions;
    private Devise devise;
    private String type;

    public Compte(int id_compte, String nom, Double solde, Devise devise, String type) {
        this.id_compte = id_compte;
        this.nom = nom;
        this.solde = solde;
        this.transactions = new ArrayList<>();
        this.devise = devise;
        this.type = type;
    }
}


