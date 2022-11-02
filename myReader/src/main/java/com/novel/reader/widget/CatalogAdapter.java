package com.novel.reader.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novel.reader.R;
import com.novel.reader.bean.BookChapterEntity;

import java.util.List;

/**
 * @author : easy on 2022/10/28 18:01
 * @description :
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogHolder> {

    private List<BookChapterEntity> list;
    private ItemClickListener itemClickListener;

    public CatalogAdapter(List<BookChapterEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CatalogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog, parent, false);
        return new CatalogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogHolder holder, int position) {
        holder.textView.setText(list.get(position).getTitle());
        holder.textView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CatalogHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public CatalogHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt);
            textView.setOnClickListener(v -> {
                itemClickListener.onItemClick((Integer) textView.getTag());
            });
        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
