package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;

class LocalizationServiceImplTest {


    @ParameterizedTest
    @CsvSource(value = {
            "Добро пожаловать, RUSSIA",
            "Welcome, USA"
    })
    void locateTets(String text, Country country) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String actual = localizationService.locale(country);
        //
        Assertions.assertEquals(text, actual);
    }
}