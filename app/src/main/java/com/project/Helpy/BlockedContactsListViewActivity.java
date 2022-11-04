package com.project.Helpy;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class BlockedContactsListViewActivity extends Fragment {

    DatabaseHelper myDb;
    ListView listView;
    RelativeLayout noContactsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_blocked_contacts_list_view,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Blocked contacts");

        listView=getActivity().findViewById(R.id.listViewMessages);
        noContactsLayout=getActivity().findViewById(R.id.no_contacts_layout);

        myDb=new DatabaseHelper(getContext());
        ArrayList arrayList=myDb.getAllBlockedContacts();

        if(arrayList.size()==0){
            noContactsLayout.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                final String data=(String)adapterView.getItemAtPosition(pos);

                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Remove this number");
                builder.setCancelable(false);
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDb.deleteIntoBlockedContacts(data);
                        Fragment frg=null;
                        frg=new BlockedContactsListViewActivity();
                        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,frg);
                        ft.commit();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }

}
