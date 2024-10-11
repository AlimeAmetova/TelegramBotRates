package ru.ametova.bot_exchange.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Crypto {
    String nameCrypto;
    Double amountCrypto;
    String changeCryptoAbout12h;
    String changeCryptoAbout7Days;

}
