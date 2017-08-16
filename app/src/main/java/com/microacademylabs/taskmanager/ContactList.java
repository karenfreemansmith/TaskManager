package com.microacademylabs.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactList extends AppCompatActivity {
  private EditText mEditOrginization;
  private EditText mEditURL;
  private ListView mContactList;
  private Button mSaveBtn;
  private TaskDatabase mTaskDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_list);
    mTaskDatabase = new TaskDatabase(this);

    mEditOrginization = (EditText)findViewById(R.id.etTitle);
    mEditURL = (EditText)findViewById(R.id.etDescription);

    mSaveBtn = (Button)findViewById(R.id.btnSave);
    mSaveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveRecord();
      }
    });

    mContactList = (ListView)findViewById(R.id.taskList);
    mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(ContactList.this, mTaskDatabase.getDescription(id), Toast.LENGTH_SHORT).show();
      }
    });
    mContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(ContactList.this, "Records deleted = " + mTaskDatabase.deleteContact(id), Toast.LENGTH_SHORT).show();
        updateContactList();
        return true;
      }
    });
    updateContactList();
  }

  private void saveRecord() {
    mTaskDatabase.saveContact(mEditOrginization.getText().toString(), mEditURL.getText().toString());
    mEditOrginization.setText("");
    mEditURL.setText("");
    updateContactList();
  }

  private void updateContactList() {
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, mTaskDatabase.getContactList(), new String[]{"organization"}, new int[]{android.R.id.text1}, 0);
    mContactList.setAdapter(adapter);
  }
}

