package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Devise {
    private int id; // Identifiant de la devise
    private String nom; // Nom de la devise (ex: Euro)
    private String code; // Code de la devise (ex: EUR)
}
