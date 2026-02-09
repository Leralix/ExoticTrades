package io.github.leralix.exotictrades.market;

/**
 * @param demandMultiplier   The demand multiplier.
 *                           The actual demand will be set as NB_PLAYER_CONNECTED_LAST_X_DAYS * demandMultiplier
 * @param volatility         The volatility of the price when tending to the desired price.
 *                           Set between 0 (not moving) and 1 (instantly set to desired price)
 * @param maxPrice           The maximum price of the item
 * @param percentForMaxPrice The amount of item sold compared to the demand in order for the desired price
 *                           to reach the maxPrice
 *                           percentForMaxPrice = 5.0 : 500%
 * @param minPrice           The minimum price of the item
 * @param percentForMinPrice The amount of item sold compared to the demand in order for the desired price
 *                           to reach the maxPrice
 *                           percentForMaxPrice = 0.2 : 20%
 */
public record MarketConstants(
        double basePrice,
        double maxPrice,
        double percentForMaxPrice,
        double minPrice,
        double percentForMinPrice,
        double demandMultiplier,
        double volatility) {

}
