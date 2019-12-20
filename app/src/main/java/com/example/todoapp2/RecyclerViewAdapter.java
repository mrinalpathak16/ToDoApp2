package com.example.todoapp2;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerClickListener listener;

    public RecyclerViewAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        listener = (RecyclerClickListener)mContext;
        holder.date.setText("27//11/2019");
        holder.label.setText("Label 1");
        holder.desc.setText("This is the description of Task 1");
        holder.time.setText("22:12");
        holder.image.setImageResource(R.drawable.ic_launcher_background);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openDetailsDialog();
                Toast.makeText(mContext, "Label 1", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
        public CircleImageView image;
        public TextView time, label, desc, date;
        public RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            time = itemView.findViewById(R.id.time);
            label = itemView.findViewById(R.id.label);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            root = itemView.findViewById(R.id.root);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.editMenu, Menu.NONE, "Edit Task");
            menu.add(Menu.NONE, R.id.deleteMenu, Menu.NONE, "Delete Task");
            menu.setHeaderTitle("Select The Action");
        }
    }

    public interface RecyclerClickListener{
        void openDetailsDialog();
    }
}
