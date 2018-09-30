package trialchain.ibm.trialchain_patient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trialchain.ibm.trialchain_patient.adapters.RecordListAdapter;
import trialchain.ibm.trialchain_patient.database.DBHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DBHelper helper;
    private List<Record> recordList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String alias;
    private RecordListAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public RecordListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment RecordListFragment.
     */
    public static RecordListFragment newInstance(String param1, String param2) {


        return new RecordListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        helper = new DBHelper(getContext());
        recordList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_list, container, false);
        ListView recordListView = (ListView) fragmentView.findViewById(R.id.recordList);

        //Get the list of all records whose key is stored within the application.
        recordList = helper.getAllKeyPairs();

        adapter = new RecordListAdapter(getContext(),recordList);
        recordListView.setAdapter(adapter);

        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO Bring up the activity with QR Code.
                Intent intent = new Intent(getActivity(),QRCodeDisplayActivity.class);

                intent.putExtra("publickey",((Record)adapter.getItem(position)).getSignkey());
               // intent.putExtra("privatekey",((Record)adapter.getItem(position)).getPrivateKey().toString());

                startActivity(intent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton)fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Intent intent = new Intent(getContext(),QRCodeScannerActivity.class);

                AlertDialog.Builder aliasPromptDialog = new AlertDialog.Builder(getContext());
                //TODO Move this to after scanning the QR code.
                aliasPromptDialog.setTitle("Enter a record name");
                aliasPromptDialog.setCancelable(false);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                final EditText dialogEditText = new EditText(getContext());
                dialogEditText.setLayoutParams(layoutParams);
                aliasPromptDialog.setView(dialogEditText);

                aliasPromptDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                aliasPromptDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alias = dialogEditText.getText().toString();
                        intent.putExtra("alias",alias);

                        intent.putExtra("calling-activity",2);
                        startActivityForResult(intent,20);

                    }
                });

                aliasPromptDialog.show();
            }
        });

        return fragmentView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DBHelper helper = new DBHelper(getActivity().getApplicationContext());

    }

    @Override
    public void onResume()
    {
        super.onResume();
        recordList = helper.getAllRecords();
        Log.d("Record list size",recordList.size()+"");

        ListView listView = getView().findViewById(R.id.recordList);
        RecordListAdapter adapter = (RecordListAdapter)listView.getAdapter();

        adapter.recordList.clear();
        adapter.recordList.addAll(new DBHelper(getContext()).getAllRecords());
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
         void onFragmentInteraction(ListView recordList, RecordListAdapter adapter, List<Record> records);
    }

    @Override
    public void onActivityResult(int requestcode,int resultcode,Intent data){
                super.onActivityResult(requestcode,resultcode,data);

                if(requestcode==20 && resultcode == RESULT_OK){


                }
    }
}
