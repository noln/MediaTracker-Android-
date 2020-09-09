package model.google;

import android.content.Context;
import android.content.Intent;

import com.example.mediatracker20.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.HashCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exceptions.EmptyStringException;
import model.jsonreaders.ItemManagerDocument;
import model.jsonreaders.ListManagerDocument;
import model.jsonreaders.TagManagerDocument;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;
import model.model.Tag;
import model.model.TagManager;
import model.persistence.ReaderLoader;

public class Database {

    public static void initializeDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
    }

    public static void loadAppInfo(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference userDocs = db.collection("users").document(auth.getUid());
        userDocs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    List<MediaList> mediaLists = new ArrayList<>();
                    List<MediaItem> mediaItems = new ArrayList<>();
                    List<Tag> tags = new ArrayList<>();
                    userDocs.collection("Lists").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                mediaLists.add(document.toObject(MediaList.class));
                            }
                            userDocs.collection("Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                        mediaItems.add(document.toObject(MediaItem.class));
                                    }
                                    userDocs.collection("Tags").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                                tags.add(document.toObject(Tag.class));
                                            }
                                            ReaderLoader.loadInfo(context, mediaLists, tags, mediaItems);
                                            Intent intent = new Intent(context, MainActivity.class);
                                            context.startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    db.collection("users").document(auth.getUid()).set(new HashMap<String, Object>());
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    public static void updateInfo(Object object, String type, int hashCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("users").document(auth.getUid()).collection(type).document(Integer.toString(hashCode)).set(object);
    }

    public static void deleteInfo(String type, int hashCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("users").document(auth.getUid()).collection(type).document(Integer.toString(hashCode)).delete();
    }


}
