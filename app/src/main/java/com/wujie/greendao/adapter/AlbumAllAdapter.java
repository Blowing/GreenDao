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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wujie.greendao.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wujie on 2017/2/25.
 */
public class AlbumAllAdapter extends RecyclerView.Adapter<AlbumAllAdapter.allHolder> {

    private Context mContext;

    public AlbumAllAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    public List<String> getmContentList() {
        return mContentList;
    }

    private List<String> mContentList;

    public SparseBooleanArray getmCheckStates() {
        return mCheckStates;
    }

    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    @Override
    public allHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_album_all, null);

        return new allHolder(convertView);
    }

    @Override
    public void onBindViewHolder(allHolder holder, final int position) {


        String uri = "file:///"+mContentList.get(position);
        Log.e("URI", position+uri);

        int width = 100, height = 100;
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        holder.albumIv.setController(controller);
//        Uri uri = Uri.fromFile(new File(mContentList.get(position)));
//        Log.e("URI", position+uri.toString());
//        holder.albumIv.setImageURI(uri);

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

    public class allHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.album_iv)
        SimpleDraweeView albumIv;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        public allHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
