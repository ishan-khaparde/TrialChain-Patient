package trialchain.ibm.trialchain_patient.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.libsodium.jni.keys.PublicKey;
import org.w3c.dom.Text;

import java.util.ArrayList;

import trialchain.ibm.trialchain_patient.Dashboard;
import trialchain.ibm.trialchain_patient.Message;
import trialchain.ibm.trialchain_patient.R;

/**
 * Created by ishankhaparde on 12/04/17.
 */

public class MessagesAdapter extends BaseAdapter {
    Context context;
    int resourceId;
    ArrayList<Message> messageArrayList;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public MessagesAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Message> messageArrayList) {

        this.context = context;
        this.messageArrayList = messageArrayList;
        resourceId = resource;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return messageArrayList.size();
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
        return messageArrayList.get(position);
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
       MessageViewHolder messageViewHolder;
        if(convertView==null)
        {
            //Inflate the layout for a single message item that appears on the list.
            LayoutInflater inflater = ((Dashboard)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.message_card_layout,parent,false);
            messageViewHolder = new MessageViewHolder();

            //Get references to the text labels for message and record ID.
            messageViewHolder.messageView = (TextView)convertView.findViewById(R.id.messageOnCardlabel);
            messageViewHolder.recordIDView = (TextView)convertView.findViewById(R.id.recordIdOnCardlabel);

            //Set the text labels on the item with record ID and actual message.
            messageViewHolder.messageView.setText(messageArrayList.get(position).getMessage());
            messageViewHolder.recordIDView.setText(messageArrayList.get(position).getRecordId());
        }
        return convertView;
    }


    private class MessageViewHolder{
        TextView recordIDView;
        TextView messageView;
    }
}

