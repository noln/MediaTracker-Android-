package com.example.mediatracker20.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;
import com.example.mediatracker20.listselectors.ListItemDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;

//Adapter for MediaItems Recyclerview used only to display and click into. No selection or delete...
public class MediaItemDisplayAdapter extends RecyclerView.Adapter<MediaItemDisplayAdapter.MediaItemViewHolder> {

    private static List<MediaItem> displayItems;
    private boolean pager = false;
    private int actionId;

    public class MediaItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemRating;
        public ImageView itemImage;
        public TextView itemEpisodes;
        public RelativeLayout layout;


        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemRating = itemView.findViewById(R.id.item_rating);
            itemImage = itemView.findViewById(R.id.item_image);
            itemEpisodes = itemView.findViewById(R.id.item_episodes);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }

    @NonNull
    @Override
    public MediaItemDisplayAdapter.MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item_display_card, parent, false);
        if(pager == true) {
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        MediaItemDisplayAdapter.MediaItemViewHolder evh = new MediaItemDisplayAdapter.MediaItemViewHolder(v);
        return evh; //evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemDisplayAdapter.MediaItemViewHolder holder, int position) {
        MediaItem item = displayItems.get(position);
        holder.itemRating.setText("Rating: " + item.getItemInfo("Rating"));
        holder.itemTitle.setText(item.getItemInfo("Title"));
        holder.itemEpisodes.setText("Episodes: " + item.getItemInfo("Episodes"));
        Picasso.get().load(item.getItemInfo("ImageLink")).into(holder.itemImage);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("MEDIA_ITEM", item);
                Navigation.findNavController(holder.itemView).navigate(actionId, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public MediaItemDisplayAdapter(List<MediaItem> itemsList, int navActionID) {
        displayItems = itemsList;
        actionId = navActionID;
    }

    public void addList(List<MediaItem> list) {
            displayItems.addAll(list);
    }

    public void isPager(boolean t) {
        pager = t;
    }

}
