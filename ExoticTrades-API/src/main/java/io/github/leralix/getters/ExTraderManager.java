package io.github.leralix.getters;

import io.github.leralix.interfaces.ExTrader;

import java.util.List;

public interface ExTraderManager {

    ExTrader getTrader(String id);

    List<ExTrader> getTraders();
}
