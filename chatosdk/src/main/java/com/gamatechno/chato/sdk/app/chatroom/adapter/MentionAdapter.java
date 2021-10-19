package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.model.MentionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MentionAdapter extends RecyclerView.Adapter<MentionViewHolder> implements Filterable {

    Context context;
    List<MentionModel> list;
    List<MentionModel> listFilter;
    MentionAdapterView view;

    public MentionAdapter(Context context, List<MentionModel> list, MentionAdapterView view) {
        this.context = context;
        this.view = view;
        this.list = list;
        listFilter = list;
    }

    @NonNull
    @Override
    public MentionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mention, parent, false);
        return new MentionViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MentionViewHolder holder, int position) {
        MentionModel item = listFilter.get(position);

        holder.bindData(item);

        holder.itemView.setOnClickListener(v ->{
            view.onClickItemMention(item);
        });
    }

    public interface MentionAdapterView{
        void onClickItemMention(MentionModel item);
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSearch = constraint.toString();

                if (charSearch.isEmpty()){
                    listFilter = list;
                }else{
                    List<MentionModel> buffer = new ArrayList<>();

                    for (int x=0; x<list.size(); x++){
                        if (list.get(x).getName().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))){
                            buffer.add(list.get(x));
                        }
                    }

                    listFilter = buffer;
                }

                FilterResults results = new FilterResults();
                results.values = listFilter;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFilter = (ArrayList<MentionModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
