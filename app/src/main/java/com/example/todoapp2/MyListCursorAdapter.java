package com.example.todoapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp2.data.CursorRecyclerViewAdapter;
import com.example.todoapp2.data.TaskContract.TaskEntry;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerClickListener listener;
    private int menuGroupId;

    public MyListCursorAdapter(Context context, Cursor cursor, int groupId){
        super(context,cursor);
        mContext = context;
        menuGroupId = groupId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
        public CircleImageView image;
        public TextView time, label, desc, date;
        public RelativeLayout root;
        public MaterialButton compButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            time = itemView.findViewById(R.id.time);
            label = itemView.findViewById(R.id.label);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            root = itemView.findViewById(R.id.root);
            compButton = itemView.findViewById(R.id.completed_button);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(menuGroupId, R.id.editMenu, Menu.NONE, "Edit Task");
            menu.add(menuGroupId, R.id.deleteMenu, Menu.NONE, "Delete Task");
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
        TimeDateFormat obj = new TimeDateFormat(cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NOTIFICATION_TIME)));
        holder.date.setText(obj.getDayOfMonth()+"/"+obj.getMonth()+"/"+obj.getYear());
        holder.label.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_LABEL)));
        holder.desc.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_DESCRIPTION)));
        holder.time.setText(obj.getHour()+":"+obj.getMinute());
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_TYPE));
        if(type==0){
            holder.image.setImageResource(R.drawable.normal);
        }
        else {
            holder.image.setImageResource(R.drawable.priority);
        }

        if(menuGroupId==101){
            holder.compButton.setVisibility(View.VISIBLE);
            holder.compButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.withAppendedPath(TaskEntry.CONTENT_URI, String.valueOf(getItemId(pos)));
                    ContentValues values = new ContentValues();
                    values.put(TaskEntry.COLUMN_TASK_STATUS, TaskEntry.COMPLETED_TASK);
                    mContext.getContentResolver().update(uri, values, null, null);
                }
            });
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
                cursor.moveToPosition(pos);
                return false;
            }
        });
    }

    public interface RecyclerClickListener{
        void openDetailsDialog(Cursor cursor, Uri uri);
    }
}
