package com.example.homework5_1_1;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ItemsDataAdapter adapter;
    private static File dataFile;
    public static final String FILE_NAME = "data.txt";
    private static List<ItemData> items = new ArrayList();
    private EditText editTitle;
    private EditText editSubtitle;
    private Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTitle = findViewById(R.id.title_editText);
        editSubtitle = findViewById(R.id.subtitle_editText);
        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (editTitle.getText().toString().equals("") || editSubtitle.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,
                            R.string.add_error,
                            Toast.LENGTH_SHORT).show();
                } else {
                    String title = editTitle.getText().toString();
                    String subtitle = editSubtitle.getText().toString();
                    items.add(new ItemData(getDrawable(R.drawable.data_icon),
                            title,
                            subtitle));
                    adapter.notifyDataSetChanged();
                    saveData();
                }
            }
        });

        ListView listView = findViewById(R.id.listView);
        dataFile = getDataFile();

        readData();
        adapter = new ItemsDataAdapter(this, items);


        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
                return true;
            }
        });

    }

    private void readData(){
        int i = 0;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(dataFile), StandardCharsets.UTF_8));
            String line;
            String title = "";
            String subtitle = "";
            while (((line = reader.readLine()) != null)){
                if (i == 0){
                    i++;
                    title = line;

                } else {
                    i = 0;
                    subtitle = line;
                    items.add(new ItemData(getDrawable(R.drawable.data_icon),
                            title,
                            subtitle));
                }

            }

        } catch (Exception e){
            fillData();

        }
    }

    private void fillData(){
        items.add(new ItemData(getDrawable(R.drawable.data_icon),
                getString(R.string.homework211_title),
                getString(R.string.homework211_subtitle)));

        items.add(new ItemData(getDrawable(R.drawable.data_icon),
                getString(R.string.homework212_title),
                getString(R.string.homework212_subtitle)));

        items.add(new ItemData(getDrawable(R.drawable.data_icon),
                getString(R.string.homework213_title),
                getString(R.string.homework213_subtitle)));

        items.add(new ItemData(getDrawable(R.drawable.data_icon),
                getString(R.string.homework221_title),
                getString(R.string.homework221_subtitle)));
        saveData();
    }

    private void showItemData(int position) {
        ItemData itemData = adapter.getItem(position);
        Toast.makeText(MainActivity.this,
                "Title: " + itemData.getTitle() + "\n" +
                        "Subtitle: " + itemData.getSubtitle(),
                Toast.LENGTH_SHORT).show();
    }

    private File getDataFolder(){
        return getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }
    private File getDataFile(){
        return new File(getDataFolder(), FILE_NAME);
    }

    protected static void saveData(){
        try {
            Writer writer = new FileWriter(dataFile);
            for (ItemData data: items){
                writer.write(data.getTitle());
                writer.write("\n");
                writer.write(data.getSubtitle());
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {

        }

    }

}
