package it.saimao.burma_dictionary;

import java.io.Serializable;

public record Data(int id, String word, String stripWord, String title, String definition, String keywords, String synonyms, String picture) implements Serializable {
}
