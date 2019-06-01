package com.example.root.teachersusiev0;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    // GUI declaration
    Button button_addQ, button_traverseSeq, button_traverseRand, button_traverseTopicSeq,
            button_traverseTopicRand, button_traverseHardQ, button_search, button_export, button_import;

    // globalVars
    GlobalVars globalVars;

    // database vars
    DBHelper dbHelper;

    // other vars
    final int SELECT_CSV = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // button_addQ
        button_addQ = (Button) findViewById(R.id.mainActivity_button_addQ);
        button_addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start AddQAActivity
                Intent intent = new Intent(MainActivity.this, AddQAActivity.class);
                startActivity(intent);
            }
        });

        // globalVars
        globalVars = (GlobalVars) getApplication();

        // init dbHelper
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // button_traverseSeq
        button_traverseSeq = (Button) findViewById(R.id.mainActivity_button_traverseSeq);
        button_traverseSeq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting testMode and topic
                globalVars.setTestMode("SEQ");
                globalVars.setTopic("NA");
                globalVars.setOnlyHard(0);
                // start next activity
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        // button_traverseRand
        button_traverseRand = (Button) findViewById(R.id.mainActivity_button_traverseRand);
        button_traverseRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting testMode and topic
                globalVars.setTestMode("RAND");
                globalVars.setTopic("NA");
                globalVars.setOnlyHard(0);
                // start next activity
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        // button_traverseTopicSeq
        button_traverseTopicSeq = (Button) findViewById(R.id.mainActivity_button_traverseTopicSeq);
        button_traverseTopicSeq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting testMode and topic
                globalVars.setTestMode("SEQ");
                globalVars.setOnlyHard(0);
                // start next activity
                Intent intent = new Intent(MainActivity.this, ChooseTopicActivity.class);
                startActivity(intent);
            }
        });

        // button_traverseTopicRand
        button_traverseTopicRand = (Button) findViewById(R.id.mainActivity_button_traverseTopicRand);
        button_traverseTopicRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting testMode and topic
                globalVars.setTestMode("RAND");
                globalVars.setOnlyHard(0);
                // start next activity
                Intent intent = new Intent(MainActivity.this, ChooseTopicActivity.class);
                startActivity(intent);
            }
        });

        // button_traverseHard
        button_traverseHardQ = (Button) findViewById(R.id.mainActivity_button_traverseHardQ);
        button_traverseHardQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting testMode and topic
                globalVars.setTestMode("SEQ");
                globalVars.setOnlyHard(1);
                // start next activity
                Intent intent = new Intent(MainActivity.this, ChooseTopicActivity.class);
                startActivity(intent);
            }
        });

        // button_search
        button_search = (Button) findViewById(R.id.mainActivity_button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start next activity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // button_export
        button_export = (Button) findViewById(R.id.mainActivity_button_export);
        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();
                    if (sd.canWrite()) {
                        String currentDBPath = "//data//"+getPackageName()+"//databases//" +
                                globalVars.getDatabaseName() + "";
                        String backupDBPath = "QA_DB_Backup.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);
                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                        Toast.makeText(getApplicationContext(), "Backup is Successful", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        });

        // button_import
        button_import = (Button) findViewById(R.id.mainActivity_button_import);
        button_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, SELECT_CSV);
            }
        });

    }

    public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(getApplicationContext(), "Process Started", Toast.LENGTH_SHORT).show();
            // for picking XLSX file and inserting data into database
            if (requestCode == SELECT_CSV && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                //String filePath = Environment.getExternalStorageDirectory() + "/" + uri.getPath().split("/")[2];
                Toast.makeText(getApplicationContext(), uri.getPath(), Toast.LENGTH_SHORT).show();
                String filePath = Environment.getExternalStorageDirectory() + "/" + uri.getPath().split("/")[2] + "/" + uri.getPath().split("/")[3];
                //String filePath = uri.getPath();
                Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_SHORT).show();
                File file = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(file);
                OPCPackage opcPackage = OPCPackage.open(fileInputStream);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(opcPackage);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
                Iterator<Row> rowIterator = xssfSheet.rowIterator();
                int firstLine = 0;
                //Toast.makeText(getApplicationContext(), file.getName() + " is found", Toast.LENGTH_SHORT).show();
                while(rowIterator.hasNext()) {
                    XSSFRow xssfRow = (XSSFRow) rowIterator.next();
                    ArrayList<String> arrayList = new ArrayList<String>();
                    if(firstLine > 0) {
                        Iterator<Cell> cellIterator = xssfRow.cellIterator();
                        while(cellIterator.hasNext()) {
                            XSSFCell xssfCell = (XSSFCell) cellIterator.next();
                            arrayList.add(xssfCell.toString());
                        }
                        //if(arrayList.size() == 5)
                        dbHelper.insertData(arrayList.get(1), arrayList.get(2), arrayList.get(3), Integer.parseInt(arrayList.get(4).split("\\.")[1]));
                    }
                    firstLine += 1;
                }
                Toast.makeText(getApplicationContext(), "Data import is accomplished!", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Exception at onActivityResult: " + e, Toast.LENGTH_LONG).show();
        }
    }

    /*private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }*/

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
}
