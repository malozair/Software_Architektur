package de.hs.swa.control;

import de.hs.swa.boundary.MocktailDTO;
import jakarta.json.JsonObject;

import java.util.Collection;
import java.util.List;

public interface CustomerService {
    String addMocktail(MocktailDTO mocktailDTO);

    MocktailDTO readMocktail(String name);

    List<MocktailDTO> readAllMocktails();

    String editMocktail(MocktailDTO mocktailDTO);

    public String deleteMocktailById(String id);
}
