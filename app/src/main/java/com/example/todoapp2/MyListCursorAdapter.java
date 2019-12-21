package com.example.todoapp2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
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

import com.example.todoapp2.data.CursorRecyclerViewAdapter;
import com.example.todoapp2.data.TaskContract.TaskEntry;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerClickListener listener;

    public MyListCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final Cursor cursor, final int pos) {
        listener = (RecyclerClickListener)mContext;
        holder.date.setText("27/11/2019");
        holder.label.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_LABEL)));
        holder.desc.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_DESCRIPTION)));
        holder.time.setText("22:12");
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_TYPE));
        if(type==0){
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }
        else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(pos);
                Log.i("ExampleDialog", ""+pos);
                listener.openDetailsDialog(cursor,
                        Uri.withAppendedPath(TaskEntry.CONTENT_URI, String.valueOf(getItemId(pos))));
                Toast.makeText(mContext,
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_LABEL)),
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    public interface RecyclerClickListener{
        void openDetailsDialog(Cursor cursor, Uri uri);
    }
}
