package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime; // Import nécessaire pour LocalDateTime

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    private int id_transaction;
    private String label; // Label de la transaction (ex: prêt bancaire)
    private double montant; // Montant de la transaction
    private LocalDateTime dateHeure; // Date et heure de la transaction
    private boolean type_transaction; // true pour crédit, false pour débit

    // Constructeur sans l'objet LocalDateTime pour l'instant (peut être ajouté si nécessaire)
    public Transaction(int id_transaction, String label, double montant, boolean type_transaction) {
        this.id_transaction = id_transaction;
        this.label = label;
        this.montant = montant;
        this.dateHeure = LocalDateTime.now(); // Date et heure actuelles par défaut
        this.type_transaction = type_transaction;
    }
}
