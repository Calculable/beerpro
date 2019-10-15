package ch.beerpro.presentation.profile.mybeers;

import java.util.HashMap;
import java.util.List;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

public class CombinedBeer {

    private List<Wish> lastWishlist;
    private List<Rating> lastRatings;
    private List<FridgeItem> lastFridgeItems;
    private HashMap<String, Beer> lastBeers;


    public CombinedBeer(List<Wish> lastWishlist, List<Rating> lastRatings, List<FridgeItem> lastFridgeItems, HashMap<String, Beer> lastBeers) {
        this.lastWishlist = lastWishlist;
        this.lastRatings = lastRatings;
        this.lastFridgeItems = lastFridgeItems;
        this.lastBeers = lastBeers;
    }

    public List<Wish> getLastWishlist() {
        return lastWishlist;
    }

    public List<Rating> getLastRatings() {
        return lastRatings;
    }

    public List<FridgeItem> getLastFridgeItems() {
        return lastFridgeItems;
    }

    public HashMap<String, Beer> getLastBeers() {
        return lastBeers;
    }
}
