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

public class MainActivity extends AppCompatActivity {
  private EditText mEditTitle;
  private EditText mEditDescription;
  private ListView mTaskList;
  private Button mSaveBtn;
  private TaskDatabase mTaskDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTaskDatabase = new TaskDatabase(this);

    mEditTitle = (EditText)findViewById(R.id.etTitle);
    mEditDescription = (EditText)findViewById(R.id.etDescription);

    mSaveBtn = (Button)findViewById(R.id.btnSave);
    mSaveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveRecord();
      }
    });

    mTaskList = (ListView)findViewById(R.id.taskList);
    mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(MainActivity.this, mTaskDatabase.getDescription(id), Toast.LENGTH_SHORT).show();
      }
    });
    mTaskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(MainActivity.this, "Records deleted = " + mTaskDatabase.deleteRecord(id), Toast.LENGTH_SHORT).show();
        updateWordList();
        return true;
      }
    });
    updateWordList();
  }

  private void saveRecord() {
    mTaskDatabase.saveRecord(mEditTitle.getText().toString(), mEditDescription.getText().toString());
    mEditTitle.setText("");
    mEditDescription.setText("");
    updateWordList();
  }

  private void updateWordList() {
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, mTaskDatabase.getTaskList(), new String[]{"title"}, new int[]{android.R.id.text1}, 0);
    mTaskList.setAdapter(adapter);
  }
}
