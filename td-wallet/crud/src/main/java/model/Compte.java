package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Compte {
    private int id_compte;
    private String nom; // Nom du compte (compte courant, compte épargne, etc.)
    private Integer solde; // Caractéristiques du solde (montant et date de dernière mise à jour)
    private List<Transaction> transactions; // Liste des transactions associées au compte
    private Devise devise;
    private String type; // Type de compte : Banque, Espèce, Mobile Money, etc.

    // Constructeur sans l'objet Transaction pour l'instant (peut être ajouté si nécessaire)
    public Compte(int id_compte, String nom, Integer solde, Devise devise, String type) {
        this.id_compte = id_compte;
        this.nom = nom;
        this.solde = solde;
        this.transactions = new ArrayList<>(); // Initialisation de la liste des transactions
        this.devise = devise;
        this.type = type;
    }
}

