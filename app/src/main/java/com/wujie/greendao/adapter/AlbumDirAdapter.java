package com.wujie.greendao.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wujie.greendao.R;
import com.wujie.greendao.model.AlbumModel;

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

    public void setOnItemClickListener(AlbumDirAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener onItemClickListener;


    public interface  OnItemClickListener  {
        void onClick(View view, int position);
    }


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

//        Uri uri = "file:///mnt/sdcard/MyApp/myfile.jpg";
//        int width = 50, height = 50;
//        ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(uri)
//                .setResizeOptions(new ResizeOptions(width, height))
//                .build();
//        PipelineDraweeController controller = Fresco.newDraweeControllerBuilder() .setOldController(mDraweeView.getController()) .setImageRequest(request)
//                .build();
        String uri = "file:///"+albumModel.getmSrc();
        Log.e("URI", position+uri);

        int width = 100, height = 100;
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        holder.pictureIv.setController(controller);
       // holder.pictureIv.setController(controller);
        //holder.pictureIv.setImageURI(Uri.parse(uri));
//        BitmapFactory.Options op = new BitmapFactory.Options();
//        op.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(albumModel.getmSrc(),op);
//
//        op.inSampleSize = Utils.calculateInSampleSize(op, 100, 100);
//        op.inJustDecodeBounds =false;
//        holder.pictureIv.setImageBitmap(BitmapFactory.decodeFile(albumModel.getmSrc(), op));
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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v, position);
            }
        });


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
        SimpleDraweeView pictureIv;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.picture_name_tv)
        TextView pictureNameTv;
        @BindView(R.id.picture_size_tv)
        TextView pictureSizeTv;

        View mView;
        public AlbumHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
