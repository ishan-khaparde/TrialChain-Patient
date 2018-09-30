package trialchain.ibm.trialchain_patient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import trialchain.ibm.trialchain_patient.Dashboard;
import trialchain.ibm.trialchain_patient.R;
import trialchain.ibm.trialchain_patient.Record;

/**
 * Created by ishankhaparde on 29/03/17.
 */

public class RecordListAdapter extends BaseAdapter {

    Context context;
    public List<Record> recordList;

    public RecordListAdapter(Context context,List<Record> recordList)
    {
        this.context = context;
        this.recordList = recordList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return recordList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RecordViewHolder viewHolder;
        if(convertView==null)
         {
             LayoutInflater inflater = ((Dashboard)context).getLayoutInflater();

             //Inflate the layout for a record item in the list.
             convertView = inflater.inflate(R.layout.card_item_layout,parent,false);

             viewHolder = new RecordViewHolder();
             viewHolder.aliasTextView = (TextView)convertView.findViewById(R.id.alias_on_card);

             convertView.setTag(viewHolder);

         } else {
             viewHolder = (RecordViewHolder)convertView.getTag();
         }
        //Having the layout in place, populate the item with the name of the record.
        viewHolder.aliasTextView.setText(recordList.get(position).getAlias());
        //Set the alignment of record name to ALIGN_CENTER.
        viewHolder.aliasTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


         return convertView;
    }

    private class RecordViewHolder{
        TextView aliasTextView;
    }
}
