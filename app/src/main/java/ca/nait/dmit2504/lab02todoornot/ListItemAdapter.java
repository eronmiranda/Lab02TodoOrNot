package ca.nait.dmit2504.lab02todoornot;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<ListItem> mListItems;

    public ListItemAdapter(Context context) {
        mContext = context;
        mListItems = new ArrayList<>();
    }
    public void addItem(ListItem newItem){
        mListItems.add(newItem);
        notifyDataSetChanged();
    }
    public void removeItem(ListItem existingItem){
        mListItems.remove(existingItem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public ListItem getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItemView = inflater.inflate(R.layout.listview_item,parent,false);
        TextView itemName = listItemView.findViewById(R.id.listview_item_name);
        TextView date = listItemView.findViewById(R.id.listview_item_date);
        TextView isComplete  = listItemView.findViewById(R.id.listview_item_complete);

        ListItem currentItem = mListItems.get(position);
        itemName.setText(currentItem.getListItemName());
        date.setText(currentItem.getDate());
        if(currentItem.getIsComplete().equals("0"))
        {
            isComplete.setText(R.string.completed_text);
            isComplete.setTextColor(Color.RED);
        }
        else{
            isComplete.setText(R.string.not_completed_text);
            isComplete.setTextColor(Color.GREEN);
        }

        return listItemView;
    }
}
