package com.example.rabedi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTodoItem;
    private Button saveButton;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemId = getIntent().getIntExtra("itemPos",-1);

      /*  saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }); */
        editTodoItem = (EditText) findViewById(R.id.editText);
        editTodoItem.setText(getIntent().getStringExtra("item"));
        editTodoItem.setSelection(editTodoItem.getText().length());


    }

    public void onItemSaveEvent(View view) {
        Intent item = new Intent();
        item.putExtra("item",editTodoItem.getText().toString());
        item.putExtra("itemPos", itemId);
        setResult(RESULT_OK,item);
        finish();
    }

}
