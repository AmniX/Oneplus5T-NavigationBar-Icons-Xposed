package com.newera.oneplus5tnavigationbartweaks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newera.oneplus5tnavigationbartweaks.R;
import com.newera.oneplus5tnavigationbartweaks.listener.OnRecyclerViewClickListener;
import com.newera.oneplus5tnavigationbartweaks.provider.StyleProvider;

public class StyleAdapter extends RecyclerView.Adapter<StyleAdapter.StyleViewHolder> {
    private StyleProvider styleProvider;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;
    private int selectedindex, nav_height;

    public StyleAdapter(StyleProvider styleProvider, int index, int nav_height, OnRecyclerViewClickListener onRecyclerViewClickListener) {
        this.styleProvider = styleProvider;
        selectedindex = index;
        this.nav_height = nav_height;
        this.onRecyclerViewClickListener = onRecyclerViewClickListener;


    }

    @Override
    public StyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StyleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style, parent, false));
    }

    @Override
    public void onBindViewHolder(StyleViewHolder holder, int position) {
        holder.check.setVisibility(position == selectedindex ? View.VISIBLE : View.GONE);
        holder.defaultText.setVisibility(position < 2 ? View.VISIBLE : View.GONE);
        holder.defaultText.setText(position == 0 ? "Default" : "None");
        StyleProvider.Icons icons = styleProvider.getIconByIndex(position);
        holder.home.setImageResource(icons.home);
        holder.recent.setImageResource(icons.recent);
        holder.back.setImageResource(icons.back);
    }

    @Override
    public int getItemCount() {
        return styleProvider.getMax();
    }

    class StyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView home, back, recent, check;
        private TextView defaultText;

        StyleViewHolder(View itemView) {
            super(itemView);
            itemView.getLayoutParams().height = nav_height;
            itemView.findViewById(R.id.nav_card).setOnClickListener(this);
            home = itemView.findViewById(R.id.home);
            defaultText = itemView.findViewById(R.id.defaultText);
            back = itemView.findViewById(R.id.back);
            recent = itemView.findViewById(R.id.recent);
            check = itemView.findViewById(R.id.check);
        }

        @Override
        public void onClick(View v) {
            selectedindex = getAdapterPosition();
            if (selectedindex > 0)
                onRecyclerViewClickListener.onRecyclerViewItemClick(selectedindex);
            notifyDataSetChanged();
        }
    }
}
