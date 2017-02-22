package com.wujie.greendao.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wujie.greendao.R;
import com.wujie.greendao.model.AlbumModel;
import com.wujie.greendao.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wujie on 2017/2/21.
 */
public class AlbumDirAdapter extends RecyclerView.Adapter<AlbumDirAdapter.AlbumHolder> {



    private Context mContext;

    public List<AlbumModel> getmContentList() {
        return mContentList;
    }

    private List<AlbumModel> mContentList;

    public SparseBooleanArray getmCheckStates() {
        return mCheckStates;
    }

    private SparseBooleanArray mCheckStates=new SparseBooleanArray();


    public AlbumDirAdapter(Context mContext, List<AlbumModel> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("onGreate", "onCreateViewHolder()");

        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_album_dir, null);
        return new AlbumHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, final int position) {
        AlbumModel albumModel = mContentList.get(position);
        Log.i("onBindViewHolder", "mContentList.get(0).getmSrc()");

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(albumModel.getmSrc(),op);

        op.inSampleSize = Utils.calculateInSampleSize(op, 100, 100);
        op.inJustDecodeBounds =false;
        holder.pictureIv.setImageBitmap(BitmapFactory.decodeFile(albumModel.getmSrc(), op));
        holder.pictureNameTv.setText(albumModel.getmAblumName());
        holder.pictureSizeTv.setText(albumModel.getmPictureSize()+"张照片"+albumModel.getmVideoSize()+"个视频");
        holder.checkbox.setTag(position);
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("onCheckedChanged", position + "--" +isChecked);
                int pos = (int) buttonView.getTag();
                if(mCheckStates.get(pos, false) == isChecked) {
                    Log.d("onCheckedChanged", position + "--" +isChecked + "--" + mCheckStates.get(pos, false));
                    return;
                }
                if(isChecked) {
                    mCheckStates.put(pos, true);
                } else {
                    mCheckStates.delete(pos);
                }
            }
        });
        holder.checkbox.setChecked(mCheckStates.get(position, false));


    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }



    public void notifyChanged(List<AlbumModel> mContentList) {
        this.mContentList = mContentList;
        notifyDataSetChanged();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture_iv)
        ImageView pictureIv;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.picture_name_tv)
        TextView pictureNameTv;
        @BindView(R.id.picture_size_tv)
        TextView pictureSizeTv;
        public AlbumHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
