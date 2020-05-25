package com.example.customizeposition.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customizeposition.R;

import java.util.List;

/**
 * @author chengxiaobo
 * @time 2020-05-23 13:02
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyApplicationBean> list;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    private ItemListener itemListener;

    public MyAdapter(List<MyApplicationBean> list) {
        this.list = list;
    }

    public void changePosition(int position1, int position2) {
        MyApplicationBean myApplicationBean = list.get(position1);
        list.set(position1, list.get(position2));
        list.set(position2, myApplicationBean);
        list.set(position2, myApplicationBean);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recyclerview_item, parent, false));
        viewHolder.imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemListener.onItemClick(list.get(position), viewHolder.flIvContainer);
                }
            }
        });

        viewHolder.imageViewIcon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageViewIcon.setImageResource(list.get(position).drawableId);
        holder.imageViewIcon.setTag(position);
        holder.flIvContainer.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewIcon;
        public FrameLayout flIvContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewIcon = itemView.findViewById(R.id.ivIcon);
            flIvContainer = itemView.findViewById(R.id.fl_iv_container);
        }
    }

    public interface ItemListener {
        void onItemClick(MyApplicationBean myApplicationBean, View view);

        void OnItemFocusChange(MyApplicationBean myApplicationBean, View view, boolean hasFocus);
    }

}