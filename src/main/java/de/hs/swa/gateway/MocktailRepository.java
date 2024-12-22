package de.hs.swa.gateway;

import de.hs.swa.entity.CustomerKatalog;
import de.hs.swa.entity.Mocktail;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class MocktailRepository implements CustomerKatalog {
    Set<Mocktail> mocktailList = new HashSet<>();
    public MocktailRepository() {}

    @Override
    public boolean addMocktail(Mocktail mocktail) {
        return mocktailList.add(mocktail);
    }

    @Override
    public boolean deleteMocktail(Mocktail mocktail) {
        return mocktailList.remove(mocktail);
    }

    @Override
    public boolean editMocktail(Mocktail mocktail) {
        if(mocktailList.contains(mocktail)){
            mocktailList.remove(mocktail);
            mocktailList.add(mocktail);
        }
        return mocktailList.contains(mocktail);
    }

    @Override
    public Set<Mocktail> getMocktailList() {
        return mocktailList;
    }
}
