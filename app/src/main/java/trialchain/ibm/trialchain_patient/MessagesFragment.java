package trialchain.ibm.trialchain_patient;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.libsodium.jni.keys.KeyPair;

import java.util.ArrayList;

import trialchain.ibm.trialchain_patient.adapters.MessagesAdapter;
import trialchain.ibm.trialchain_patient.messaging.IntentAction;
import trialchain.ibm.trialchain_patient.messaging.MessageBroadcastRecevier;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements MessageLoadAsyncTask.FetchedMessagesEvent {

    private ListView messageList;

    private ArrayList<Message> messageArrayList;

    private MessagesAdapter messagesAdapter;

    private OnFragmentInteractionListener mListener;

    private MessageBroadcastRecevier myRecevier;


    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MessagesFragment.
     */

    public static MessagesFragment newInstance() {

        return new MessagesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View messageFragmentView = inflater.inflate(R.layout.fragment_messages,container,false);
        messageList = (ListView)messageFragmentView.findViewById(R.id.messageList);
        messageArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(getContext(),R.layout.message_card_layout,messageArrayList);

        messageList.setAdapter(messagesAdapter);
        MessageLoadAsyncTask messageLoadAsyncTask = new MessageLoadAsyncTask(messageArrayList,this);
        messageLoadAsyncTask.execute("RecordPK");

        messageArrayList = messageLoadAsyncTask.setMessageList();

        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getActivity(),MessageReplyActivity.class);
                        intent.putExtra("recordID",(messageArrayList.get(position)).getRecordId());
                        intent.putExtra("message",(messageArrayList.get(position)).getMessage());
                        startActivity(intent);
            }
        });

        Log.d("Messages here",messageArrayList.size()+"");

        return messageFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void didMessageLoad(ArrayList<Message> list) {

        Log.d("LIST IN FRAGMENT","LIST SIZE IN FRAGMENT IS "+list.size()+"");

        MessagesAdapter messagesAdapter = new MessagesAdapter(getContext(),R.layout.message_card_layout,list);
        messageList.setAdapter(messagesAdapter);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
