package com.example.mediatracker20.adapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mediatracker20.R;
import com.example.mediatracker20.listselectors.ListItemDetail;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.ItemNotFoundException;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;

//Adapter for MediaItems Recyclerview
public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {

    private static List<MediaItem> allItems;
    private static List<MediaItem> displayItems;
    private static ListManager listManager;
    private MediaList selectedList;
    private SelectionTracker selectionTracker;
    private static ItemManager itemManager;

    public class MediaItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemRating;
        public TextView itemStatus;
        public ImageView itemImage;
        public TextView itemEpisodes;
        public CardView layout;


        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemRating = itemView.findViewById(R.id.item_rating);
            itemStatus = itemView.findViewById(R.id.item_status);
            itemImage = itemView.findViewById(R.id.item_image);
            itemEpisodes = itemView.findViewById(R.id.item_episodes);
            layout = itemView.findViewById(R.id.item_layout);
        }

        public ItemDetailsLookup.ItemDetails getItemDetails() {
            return new ListItemDetail(getAdapterPosition(), displayItems.get(getAdapterPosition()));
        }

        public final void bind(MediaList list, boolean isActive) {
            layout.setActivated(isActive);
        }
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void syncLists() {
        allItems.clear();
        allItems.addAll(displayItems);
    }

    @NonNull
    @Override
    public MediaItemAdapter.MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_card, parent, false);
        MediaItemAdapter.MediaItemViewHolder evh = new MediaItemAdapter.MediaItemViewHolder(v);
        return evh; //evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemAdapter.MediaItemViewHolder holder, int position) {
        MediaItem item = displayItems.get(position);
        holder.itemRating.setText( item.getItemInfo("Rating"));
        holder.itemStatus.setText(item.getItemInfo("Status"));
        holder.itemTitle.setText(item.getItemInfo("Title"));
        holder.itemEpisodes.setText(item.getItemInfo("Episodes"));
        //holder.itemBanner. load with picasso later
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public MediaItemAdapter(List<MediaItem> itemsList, MediaList mediaList) {
        displayItems = itemsList;
        allItems = new ArrayList<>();
        allItems.addAll(displayItems);
        listManager = ListManager.getInstance();
        itemManager = ItemManager.getInstance();
        this.selectedList = mediaList;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        displayItems.clear();
        System.out.println(charText.length());
        if (charText.length() == 0) {
            displayItems.addAll(allItems);
        } else {
            for (MediaItem wp : allItems) {
                if (wp.getItemInfo("Title").toLowerCase().contains(charText)) {
                    displayItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
