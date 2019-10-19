package ch.beerpro.presentation.profile.myfridge;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.modal.ModalInputNumberDialog;


public class FridgeActivity extends AppCompatActivity implements OnFridgeItemInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private FridgeViewModel model;
    private FridgeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_fridge));


        model = ViewModelProviders.of(this).get(FridgeViewModel.class);
        model.getMyFridgeItemsWithBeers().observe(this, this::updateFridge);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FridgeRecyclerViewAdapter(this);

        recyclerView.setAdapter(adapter);

    }

    private void updateFridge(List<Pair<FridgeItem, Beer>> entries) {
        adapter.submitList(entries);
        if (entries.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void onMoreClickedListener(ImageView animationSource, Beer beer) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, beer.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onFridgeClickedListener(Beer beer) {
        removeExistingBeerFromFridge(beer);
    }

    private void removeExistingBeerFromFridge(Beer beer) {
        model.toggleItemInFridge(beer.getId(), 0); //amount is 0 because beers can only be removed from fridge hier
    }

    @Override
    public void onFridgeAmountClickedListener(Beer beer) {

        ModalInputNumberDialog.readUserInputNumber(FridgeActivity.this, getString(R.string.title_input_fridge_amount_change), getString(R.string.message_input_fridge_amount_change), (amount) -> {

            //Callback for Success: Add Beer to Fridge
            if (amount <= 0) {
                //remove beer from fridge if amount = 0 (or below)
                removeExistingBeerFromFridge(beer);
            } else {
                model.changeFridgeItemAmount(beer.getId(), amount);
            }
        }, () -> {
            //No valid amount entered. Do Nothing
        });


    }

}
