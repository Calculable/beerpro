package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromUser {

    private Beer beer;

    private FridgeItem fridgeItem = null;
    private Rating rating = null;
    private Wish wish = null;

    public MyBeerFromUser(Beer beer, FridgeItem fridgeItem, Rating rating, Wish wish) {
        this.beer = beer;
        this.fridgeItem = fridgeItem;
        this.rating = rating;
        this.wish = wish;
    }

    public MyBeerFromUser(Beer beer, FridgeItem fridgeItem) {
        this(beer, fridgeItem, null, null);
    }

    public MyBeerFromUser(Beer beer, Rating rating) {
        this(beer, null, rating, null);
    }

    public MyBeerFromUser(Beer beer, Wish wish) {
        this(beer, null, null, wish);
    }

    public String getBeerId() {
        return beer.getId();
    }

    public Date getDate() {
        if (fridgeItem != null) { //if there is a fridge item, this date is returned because it will be presented as fridge-item in the "myBeer" Activity
            return fridgeItem.getAddedAt();
        }

        if (wish != null) {
            return wish.getAddedAt();
        }

        if (rating != null) {
            return rating.getCreationDate();
        }

        return null;

    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Wish getWish() {
        return wish;
    }

    public void setWish(Wish wish) {
        this.wish = wish;
    }

    public FridgeItem getFridgeItem() {
        return this.fridgeItem;
    }

    public void setFridgeItem(FridgeItem fridgeItem) {
        this.fridgeItem = fridgeItem;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }


    public boolean isInFridge() {
        return fridgeItem != null;
    }

    public boolean hasRating() {
        return rating != null;
    }

    public boolean isOnWishlist() {
        return wish != null;
    }


    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromUser)) return false;
        final MyBeerFromUser other = (MyBeerFromUser) o;

        if (!other.canEqual(this)) return false;

        final Object this$fridgeItem = this.getFridgeItem();
        final Object other$fridgeItem = other.getFridgeItem();
        if (this$fridgeItem == null ? other$fridgeItem != null : !this$fridgeItem.equals(other$fridgeItem))
            return false;

        final Object this$rating = this.getRating();
        final Object other$rating = other.getRating();
        if (this$rating == null ? other$rating != null : !this$rating.equals(other$rating))
            return false;

        final Object this$wish = this.getWish();
        final Object other$wish = other.getWish();
        if (this$wish == null ? other$wish != null : !this$wish.equals(other$wish)) return false;

        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromUser;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fridgeItem = this.getFridgeItem();
        result = result * PRIME + ($fridgeItem == null ? 43 : $fridgeItem.hashCode());
        final Object $rating = this.getRating();
        result = result * PRIME + ($rating == null ? 43 : $rating.hashCode());
        final Object $wish = this.getWish();
        result = result * PRIME + ($wish == null ? 43 : $wish.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromUser(fridgeItem=" + this.getFridgeItem() + " rating=" + this.getRating() + " wish=" + this.getWish() + ", beer=" + this.getBeer() + ")";
    }
}
