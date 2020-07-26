package com.example.mediatracker20.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;
import com.example.mediatracker20.listselectors.ListItemDetail;
import com.google.android.gms.common.util.ScopeUtil;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import model.model.ListManager;
import model.model.MediaList;

//adapter for MediaList
public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MediaListViewHolder> {

    private static List<MediaList> displayList; //all active lists
    private static List<MediaList> allLists;
    private static ListManager listManager;
    private SelectionTracker selectionTracker;

    public class MediaListViewHolder extends RecyclerView.ViewHolder {

        public TextView listTitle;
        public TextView numberOfItems;
        public TextView listCreationDate;
        public RelativeLayout layout;


        public MediaListViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.listName);
            numberOfItems = itemView.findViewById(R.id.listItems);
            listCreationDate = itemView.findViewById(R.id.listDateCreated);
            layout = itemView.findViewById(R.id.card_layout);
        }

        public ItemDetailsLookup.ItemDetails getItemDetails() {
            return new ListItemDetail(getAdapterPosition(), displayList.get(getAdapterPosition()));
        }

        public final void bind(MediaList list, boolean isActive) {
            layout.setActivated(isActive);
        }
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        displayList.clear();
        System.out.println(charText.length());
        if (charText.length() == 0) {
            displayList.addAll(allLists);
        } else {
            for (MediaList wp : allLists) {
                if (wp.getName().toLowerCase().contains(charText)) {
                    displayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void syncLists() {
        allLists.clear();
        allLists.addAll(displayList);
    }


    //create cards
    @NonNull
    @Override
    public MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_card, parent, false);
        MediaListViewHolder evh = new MediaListViewHolder(card);
        return evh;
    }

    //set content of the cards
    @Override
    public void onBindViewHolder(@NonNull MediaListViewHolder holder, int position) {
        MediaList list = displayList.get(position);
        holder.listTitle.setText(list.getName());
        holder.listCreationDate.setText("Created: " + list.getDateCreated());
        int num = listManager.getListOfMedia(list).size();
        holder.numberOfItems.setText("Items: " + num);
        holder.bind(list, selectionTracker.isSelected(list));
    }

    //get # of active lists
    @Override
    public int getItemCount() {
        return displayList.size();
    }

    //creates an adapter
    public MediaListAdapter(List<MediaList> exampleList) {
        displayList = exampleList;
        allLists = new ArrayList<>();
        allLists.addAll(displayList);
        listManager = ListManager.getInstance();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
