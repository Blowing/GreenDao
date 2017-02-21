package com.wujie.greendao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wujie on 2017/2/21.
 */
public class AlbumDirAdapter extends RecyclerView.Adapter<AlbumDirAdapter.AlbumHolder>{


    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {

        public AlbumHolder(View itemView) {
            super(itemView);
        }
    }
}
