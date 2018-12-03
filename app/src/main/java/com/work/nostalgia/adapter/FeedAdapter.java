package com.work.nostalgia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.work.nostalgia.R;
import com.work.nostalgia.model.FeedModel;
import com.work.nostalgia.utility.utils;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedsHolder> {

    List<FeedModel> feeds;
    private Context context;

    public FeedAdapter(List<FeedModel> feeds, Context context) {
        this.feeds = feeds;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        FeedsHolder mViewHolder = new FeedsHolder(v);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsHolder viewHolder, int i) {
viewHolder.name.setText(feeds.get(i).getAuthor());
viewHolder.timestamp.setText(feeds.get(i).getTime());
viewHolder.desc.setText(feeds.get(i).getDescription());
        Glide
                .with(context)
                .load(utils.getInstance().getImage(feeds.get(i).feedimage))
                .into(viewHolder.feedImage1);

    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }
    public class FeedsHolder extends RecyclerView.ViewHolder{

        TextView name, timestamp, desc;
        ImageView feedImage1;

        public FeedsHolder(View v) {
            super(v);
            v.setTag(this);
            name = v.findViewById(R.id.name);
            timestamp = v.findViewById(R.id.timestamp);
            desc = v.findViewById(R.id.desc);
            feedImage1 = v.findViewById(R.id.feedImage1);

        }

    }
}
