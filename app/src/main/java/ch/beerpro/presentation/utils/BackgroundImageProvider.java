package ch.beerpro.presentation.utils;

import ch.beerpro.R;
import ch.beerpro.presentation.images.ImageStorageConstants;
import ch.beerpro.presentation.images.LazyLoadedImage;

public class BackgroundImageProvider {


    static LazyLoadedImage[] backgrounds = {
            new LazyLoadedImage(R.drawable.bg_beers_card_1, ImageStorageConstants.BEER_CARD_1),
            new LazyLoadedImage(R.drawable.bg_beers_card_2, ImageStorageConstants.BEER_CARD_2),
            new LazyLoadedImage(R.drawable.bg_beers_card_3, ImageStorageConstants.BEER_CARD_3),
            new LazyLoadedImage(R.drawable.bg_beers_card_4, ImageStorageConstants.BEER_CARD_4),
            new LazyLoadedImage(R.drawable.bg_beers_card_5, ImageStorageConstants.BEER_CARD_5),
            new LazyLoadedImage(R.drawable.bg_beers_card_6, ImageStorageConstants.BEER_CARD_6),
            new LazyLoadedImage(R.drawable.bg_beers_card_7, ImageStorageConstants.BEER_CARD_7),
            new LazyLoadedImage(R.drawable.bg_beers_card_8, ImageStorageConstants.BEER_CARD_8),
            new LazyLoadedImage(R.drawable.bg_beers_card_9, ImageStorageConstants.BEER_CARD_9),
            new LazyLoadedImage(R.drawable.bg_beers_card_10, ImageStorageConstants.BEER_CARD_10),
            new LazyLoadedImage(R.drawable.bg_beers_card_11, ImageStorageConstants.BEER_CARD_11),
            new LazyLoadedImage(R.drawable.bg_beers_card_12, ImageStorageConstants.BEER_CARD_12),
            new LazyLoadedImage(R.drawable.bg_beers_card_13, ImageStorageConstants.BEER_CARD_13),
            new LazyLoadedImage(R.drawable.bg_beers_card_14, ImageStorageConstants.BEER_CARD_14)
    };

    public static LazyLoadedImage getBackgroundImage(int position) {
        return backgrounds[position % backgrounds.length];
    }
}
