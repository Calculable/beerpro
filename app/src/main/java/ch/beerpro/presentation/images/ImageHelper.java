package ch.beerpro.presentation.images;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class ImageHelper {

    public static void loadImageFromFirebase(Context context, StorageReference storageReference, int placeHolderResource, ImageView target) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeHolderResource)
                .error(placeHolderResource);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Glide.with(context).load(uri).apply(options).into(target);
            }
        });

    }
}
