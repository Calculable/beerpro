package ch.beerpro.domain.utils;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.profile.mybeers.CombinedBeer;

public class LiveDataExtensions {

    public static <A, B> LiveData<Pair<A, B>> zip(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                this.setValue(new Pair<>(lastA, lastB));
            }
        };
    }

    public static <A, B> LiveData<Pair<A, B>> combineLatest(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null) {
                    this.setValue(new Pair<>(lastA, lastB));
                }
            }
        };
    }

    public static <A, B, C> LiveData<Triple<A, B, C>> combineLatest(LiveData<A> as, LiveData<B> bs, LiveData<C> cs) {
        return new MediatorLiveData<Triple<A, B, C>>() {

            A lastA = null;
            B lastB = null;
            C lastC = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                    addSource(cs, (C c) -> {
                        lastC = c;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null && lastC != null) {
                    this.setValue(Triple.of(lastA, lastB, lastC));
                }
            }
        };
    }


    public static LiveData<CombinedBeer> combineLatest(LiveData<List<Wish>> myWishlist, LiveData<List<Rating>> myRatings, LiveData<List<FridgeItem>> myFridgeItems, LiveData<HashMap<String, Beer>> myBeersMap) {
        return new MediatorLiveData<CombinedBeer>() {

            List<Wish> lastWishlist = null;
            List<Rating> lastRatings = null;
            List<FridgeItem> lastFridgeItems = null;
            HashMap<String, Beer> lastBeers = null;

            {
                {
                    addSource(myWishlist, (List<Wish> a) -> {
                        lastWishlist = a;
                        update();
                    });
                    addSource(myRatings, (List<Rating> b) -> {
                        lastRatings = b;
                        update();
                    });
                    addSource(myFridgeItems, (List<FridgeItem> c) -> {
                        lastFridgeItems = c;
                        update();
                    });

                    addSource(myBeersMap, (HashMap<String, Beer> d) -> {
                        lastBeers = d;
                        update();
                    });


                }
            }

            private void update() {
                if (lastWishlist != null && lastRatings != null && lastFridgeItems != null && lastBeers != null) {
                    this.setValue(new CombinedBeer(lastWishlist, lastRatings, lastFridgeItems, lastBeers));
                }
            }
        };
    }

}
