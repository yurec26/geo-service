package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;


class GeoServiceImplTest {


    @ParameterizedTest
    @CsvSource(value = {
            "96.69.59.85, USA",
            "96.67.119.015, USA",
            "172.69.59.85, RUSSIA",
            "172.69.59.85, RUSSIA",
    })
    void locateByIpTest(String ip, Country expected) {
        GeoService geoService = new GeoServiceImpl();
        Country actual = geoService.byIp(ip).getCountry();
        //
        Assertions.assertEquals(actual, expected);
    }
}