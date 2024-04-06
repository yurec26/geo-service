package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageSenderImplTest {

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
        when(geoService.byIp(startsWith("172."))).thenReturn(new Location("someRussianCity", Country.RUSSIA, null, 0));
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
        when(geoService.byIp(startsWith("96."))).thenReturn(new Location("someUSACity", Country.USA, null, 0));
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
}