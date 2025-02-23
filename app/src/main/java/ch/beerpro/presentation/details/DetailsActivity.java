package ch.beerpro.presentation.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.details.createrating.CreateRatingActivity;
import ch.beerpro.presentation.images.ImageHelper;
import ch.beerpro.presentation.images.ImageStorageConstants;
import ch.beerpro.presentation.modal.ModalInputNumberDialog;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class DetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.avgRating)
    TextView avgRating;

    @BindView(R.id.numRatings)
    TextView numRatings;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.manufacturer)
    TextView manufacturer;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.detailsBackgroundImage)
    ImageView detailsBackgroundImage;

    View singleBottomSheetDialogView;
    BottomSheetDialog bottomSheetDialog;
    private ToggleButton addToFridgeToggleButton;

    private RatingsRecyclerViewAdapter adapter;

    private DetailsViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextColor(Color.alpha(0));

        singleBottomSheetDialogView = getLayoutInflater().inflate(R.layout.fragment_single_bottom_sheet_dialog, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(singleBottomSheetDialogView);

        addToFridgeToggleButton = singleBottomSheetDialogView.findViewById(R.id.addToFridge);

        addToFridgeToggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //We won't get an update from firestore when the fridge-entry is removed, so we need to reset the UI state ourselves.
                if (!addToFridgeToggleButton.isChecked()) {
                    removeItemFromFridge(model.getBeer().getValue().getId());
                    toggleAddToFridgeView(null);
                } else {
                    addItemToFridge(model.getBeer().getValue().getId());
                }
            }

            private void addItemToFridge(String beerId) {

                ModalInputNumberDialog.readUserInputNumber(DetailsActivity.this, getString(R.string.title_input_fridge_amount_set), getString(R.string.message_input_fridge_amount_set), (amount) -> {
                    //Callback for Success: Add Beer to Fridge
                    if (amount < 1) {
                        toggleAddToFridgeView(null);
                        return; //do not add to fridge
                    }

                    model.toggleItemInFridge(model.getBeer().getValue().getId(), amount);
                }, () -> {
                    toggleAddToFridgeView(null);
                });

            }

            private void removeItemFromFridge(String beerId) {
                model.toggleItemInFridge(beerId, 0);

            }

        });

        String beerId = getIntent().getExtras().getString(ITEM_ID);

        model = ViewModelProviders.of(this).get(DetailsViewModel.class);
        model.setBeerId(beerId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUser());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        model.getBeer().observe(this, this::updateBeer);
        model.getRatings().observe(this, this::updateRatings);
        model.getWish().observe(this, this::toggleWishlistView);
        model.getFridgeItem().observe(this, this::toggleAddToFridgeView);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(ImageStorageConstants.DIRECTORY).child(ImageStorageConstants.MAIN_BACKGROUND);
        ImageHelper.loadImageFromFirebase(DetailsActivity.this, storageReference, R.drawable.bg_bottles, detailsBackgroundImage);

        recyclerView.setAdapter(adapter);
        addRatingBar.setOnRatingBarChangeListener(this::addNewRating);
    }

    private void addNewRating(RatingBar ratingBar, float v, boolean b) {
        Intent intent = new Intent(this, CreateRatingActivity.class);
        intent.putExtra(CreateRatingActivity.ITEM, model.getBeer().getValue());
        intent.putExtra(CreateRatingActivity.RATING, v);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, addRatingBar, "rating");
        startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        bottomSheetDialog.show();
    }


    private void updateBeer(Beer item) {
        name.setText(item.getName());
        manufacturer.setText(item.getManufacturer());
        category.setText(item.getCategory());
        name.setText(item.getName());
        GlideApp.with(this).load(item.getPhoto()).apply(new RequestOptions().override(120, 160).centerInside())
                .into(photo);
        ratingBar.setNumStars(5);
        ratingBar.setRating(item.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, item.getAvgRating()));
        numRatings.setText(getResources().getString(R.string.fmt_ratings, item.getNumRatings()));
        toolbar.setTitle(item.getName());
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(model.getBeer().getValue().getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void toggleAddToFridgeView(FridgeItem fridgeitem) {

        ToggleButton addToFridgeToggleButton = singleBottomSheetDialogView.findViewById(R.id.addToFridge);

        if (fridgeitem != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(addToFridgeToggleButton, color);
            addToFridgeToggleButton.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(addToFridgeToggleButton, color);
            addToFridgeToggleButton.setChecked(false);
        }
    }
}
