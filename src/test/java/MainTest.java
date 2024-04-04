import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainTest {

    @Mock
    GeoServiceImpl geoService;
    @Mock
    LocalizationServiceImpl localizationService;
    @InjectMocks
    MessageSenderImpl messageSender;


    @ParameterizedTest
    @CsvSource(value = {
            "172.256.365",
            "172.54.45.88",
            "172.89.74.56",
            "172.56.74.25"
    })
    void sendRussianRuIp(String ip) {
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        when(geoService.byIp(ip)).thenCallRealMethod();
        //
        messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        //
        String expected = "Добро пожаловать";
        String actual = messageSender.send(headers);
        //
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "96.256.365",
            "96.54.45.88",
            "96.89.74.56",
            "96.56.74.25"
    })
    void sendForeignIp(String ip) {
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");
        when(geoService.byIp(ip)).thenCallRealMethod();
        //
        messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        //
        String expected = "Welcome";
        String actual = messageSender.send(headers);
        //
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "96.69.59.85, USA",
            "96.67.119.015, USA",
            "172.69.59.85, RUSSIA",
            "172.69.59.85, RUSSIA",
    })
    void locateByIpTest(String ip, Country expected) {
        when(geoService.byIp(ip)).thenCallRealMethod();
        Country actual = geoService.byIp(ip).getCountry();
        //
        Assertions.assertEquals(actual, expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Добро пожаловать, RUSSIA",
            "Welcome, USA"
    })
    void locateTets(String text, Country country) {
        when(localizationService.locale(country)).thenCallRealMethod();
        String actual = localizationService.locale(country);
        //
        Assertions.assertEquals(text, actual);
    }
}

