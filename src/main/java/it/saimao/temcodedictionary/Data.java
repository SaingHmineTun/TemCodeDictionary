package it.saimao.temcodedictionary;

import java.io.Serializable;

public record Data(int id, String word, String state, String def) implements Serializable {
}
