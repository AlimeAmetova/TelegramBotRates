package ru.ametova.bot_exchange.model.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Eur {
    @SerializedName("Name")
    private String nameRate;
    @SerializedName("Value")
    private Double rate;
    @SerializedName("CharCode")
    private String code;

}
