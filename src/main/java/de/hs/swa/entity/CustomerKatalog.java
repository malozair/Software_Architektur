package de.hs.swa.entity;

import java.util.Set;

public interface CustomerKatalog {
    public boolean addMocktail(Mocktail mocktail);

    public boolean deleteMocktail(Mocktail mocktail);

    public boolean editMocktail(Mocktail mocktail);

    public Set<Mocktail> getMocktailList();
}
