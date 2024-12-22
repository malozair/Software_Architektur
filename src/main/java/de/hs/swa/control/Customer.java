package de.hs.swa.control;

import de.hs.swa.boundary.MocktailDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class Customer implements CustomerService {

    @Inject
    KatalogService katalogService;

    @Override
    public String addMocktail(MocktailDTO mocktailDTO) {
        return katalogService.addMocktail(mocktailDTO);
    }

    @Override
    public String deleteMocktailById(String id) {
        return katalogService.deleteMocktail(id);
    }

    @Override
    public String editMocktail(MocktailDTO mocktailDTO) {
        return katalogService.editMocktail(mocktailDTO);
    }

    @Override
    public MocktailDTO readMocktail(String name) {
        return katalogService.readMocktail(name);
    }

    @Override
    public List<MocktailDTO> readAllMocktails() {
        return katalogService.readMocktails().stream().toList();
    }
}
