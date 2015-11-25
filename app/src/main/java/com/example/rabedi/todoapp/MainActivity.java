package com.example.rabedi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Item> items;
    private ArrayAdapter<Item> itemsAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<Item>();
        readItemsFromDB();
        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        // Setup remove listener method call
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etEditText);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(writeItemsToDB(itemText));
        etNewItem.setText("");
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        final SQLiteHelper sQLhelper=SQLiteHelper.getInstance(this);
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                        Item currentItem=items.get(pos);
                        sQLhelper.deleteItem(currentItem);

                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        //writeItems();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("item", items.get(pos).toString());
                intent.putExtra("itemPos", pos);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    private void readItemsFromDB(){
        SQLiteHelper sQLhelper=SQLiteHelper.getInstance(this);
        List<Item> itemList=sQLhelper.getAllItems();
        if(itemList!=null){
            items.addAll(itemList);
        }
    }

   /* private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }*/

    private Item writeItemsToDB(String itemtext){
        SQLiteHelper dbhelper=SQLiteHelper.getInstance(this);
        int nextInt=items.size();
        Item item=dbhelper.createItem(itemtext);
        return item;


    }

    /*private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent editItem) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            final SQLiteHelper dbhelper=SQLiteHelper.getInstance(this);
            String itemtext = editItem.getExtras().getString("item");
            //int itemPos = Integer.parseInt(editItem.getExtras().getString("itemPos"));
            int pos = editItem.getExtras().getInt("itemPos");
            if(pos > -1) {
                Item item=items.get(pos);
                item.setText(itemtext);
                items.set(pos, item);
                dbhelper.updateItem(item);
                itemsAdapter.notifyDataSetChanged();
               // writeItems();
            }
        }
    }
}