package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.MyBeerFromFridge;
import ch.beerpro.domain.models.MyBeerFromRating;
import ch.beerpro.domain.models.MyBeerFromUser;
import ch.beerpro.domain.models.MyBeerFromWishlist;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.profile.mybeers.CombinedBeer;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyBeersRepository {

   /* private static List<MyBeer> getMyBeers(Triple<List<Wish>, List<Rating>, HashMap<String, Beer>> input) {
        List<Wish> wishlist = input.getLeft();
        List<Rating> ratings = input.getMiddle();
        HashMap<String, Beer> beers = input.getRight();

        ArrayList<MyBeer> result = new ArrayList<>();
        Set<String> beersAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishlist) {
            String beerId = wish.getBeerId();
            result.add(new MyBeerFromWishlist(wish, beers.get(beerId)));
            beersAlreadyOnTheList.add(beerId);
        }

        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromRating(rating, beers.get(beerId)));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }*/


    public LiveData<List<MyBeerFromUser>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings,
                                             LiveData<List<FridgeItem>> myFridgeItems) {

        return map(combineLatest(myWishlist, myRatings, myFridgeItems, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);

        /*return map(combineLatest(myWishlist, myRatings, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);*/
    }


    private static List<MyBeerFromUser> getMyBeers(CombinedBeer combinedBeer) {
        List<Wish> wishlist = combinedBeer.getLastWishlist();
        List<Rating> ratings = combinedBeer.getLastRatings();
        List<FridgeItem> fridgeItems = combinedBeer.getLastFridgeItems();
        HashMap<String, Beer> beers = combinedBeer.getLastBeers();

        ArrayList<MyBeerFromUser> result = new ArrayList<>();
        HashMap<String, MyBeerFromUser> beersAlreadyOnTheList = new HashMap<>();


        for (Wish wish : wishlist) {
            String beerId = wish.getBeerId();
            MyBeerFromUser myBeer = new MyBeerFromUser(beers.get(beerId), wish);
            result.add(myBeer);
            beersAlreadyOnTheList.put(beerId, myBeer);
        }

        for (FridgeItem fridgeItem : fridgeItems) {
            String beerId = fridgeItem.getBeerId();

            MyBeerFromUser existingBeer = beersAlreadyOnTheList.get(beerId);

            if (existingBeer != null) {
                // if the beer is already on the wish list or in a rating, don't add it again
                existingBeer.setFridgeItem(fridgeItem);
            } else {
                MyBeerFromUser myBeer = new MyBeerFromUser(beers.get(beerId), fridgeItem);
                result.add(myBeer);
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.put(beerId, myBeer);
            }
        }

        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();

            MyBeerFromUser existingBeer = beersAlreadyOnTheList.get(beerId);


            if (existingBeer != null) {
                // if the beer is already on the wish list, don't add it again
                existingBeer.setRating(rating);
            } else {
                MyBeerFromUser myBeer = new MyBeerFromUser(beers.get(beerId), rating);
                result.add(myBeer);
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.put(beerId, myBeer);
            }
        }


        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }


}
