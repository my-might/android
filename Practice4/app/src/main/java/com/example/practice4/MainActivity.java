package com.example.practice4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    String name[] = {"Китай", "США", "Бразилія", "Україна", "Японія", "Німеччина", "Єгипет", "Італія", "Франція", "Канада"};
    int people[] = { 1400, 311, 195, 142, 128, 82, 80, 60, 66, 35};
    String region[] = {"Азія", "Америка", "Америка", "Європа","Азія", "Європа", "Африка", "Європа", "Європа", "Америка"};

    Button btnAll, btnFunc, btnPeople, btnSort, btnGroup, btnHaving;
    EditText etFunc, etPeople, etRegionPeople;
    RadioGroup rgSort;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAll = (Button) findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);

        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setOnClickListener(this);

        btnPeople = (Button) findViewById(R.id.btnPeople);
        btnPeople.setOnClickListener(this);

        btnSort = (Button) findViewById(R.id.btnSort);
        btnSort.setOnClickListener(this);

        btnGroup = (Button) findViewById(R.id.btnGroup);
        btnGroup.setOnClickListener(this);

        btnHaving = (Button) findViewById(R.id.btnHaving);
        btnHaving.setOnClickListener(this);

        etFunc = (EditText) findViewById(R.id.etFunc);
        etPeople = (EditText) findViewById(R.id.etPeople);
        etRegionPeople = (EditText) findViewById(R.id.etRegionPeople);

        rgSort = (RadioGroup) findViewById(R.id.rgSort);

        dbHelper = new DBHelper(this);
        // Підключаємося до бази
        db = dbHelper.getWritableDatabase();

        // Перевірка існування записів
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.getCount() == 0) {
            ContentValues cv = new ContentValues();
            // заповнимо таблицю
            for (int i = 0; i < 10; i++) {
            cv.put("name", name[i]);
            cv.put("people", people[i]);

            cv.put("region", region[i]);
            Log.d(LOG_TAG, "id="+db.insert("mytable", null, cv));
            }
        }
        c.close();
        dbHelper.close();
        // Емулюємо натискання кнопки btnAll
        onClick(btnAll);
    }

    public void onClick(View v) {
        // Підключаємося до бази
        db = dbHelper.getWritableDatabase();
        // Дані з екрану
        String sFunc = etFunc.getText().toString();
        String sPeople = etPeople.getText().toString();
        String sRegionPeople = etRegionPeople.getText().toString();
        // змінні для query
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // курсор
        Cursor c=null;
        // Визначаємо натиснуту кнопку
        switch (v.getId()) {
                // Усі записи
                case R.id.btnAll:
                Log.d(LOG_TAG, "--- Всі записи ---");
                c = db.query("mytable", null, null, null, null, null, null);
                break;
                // Функція
                case R.id.btnFunc:
                Log.d(LOG_TAG, "--- Функція " + sFunc + " ---");
                columns = new String[] { sFunc };
                c = db.query("mytable", columns, null, null, null, null, null);
                break;
                // Населення більше, ніж
                case R.id.btnPeople:
                Log.d(LOG_TAG, "--- Населення більше " + sPeople + " ---");
                selection = "people > ?";
                selectionArgs = new String[] { sPeople };
                c = db.query("mytable", null, selection, selectionArgs, null, null, null);
                break;
                // Населення по регіону
                case R.id.btnGroup:
                Log.d(LOG_TAG, "--- Населення по регіону ---");
                columns = new String[] {"region", "sum(people) as people"};
                groupBy="region";
                c = db.query("mytable", columns, null, null, groupBy, null, null);
                break;
                // Населення по регіону більше, ніж
                case R.id.btnHaving:
                Log.d(LOG_TAG, "--- Регіони з населенням більше " + sRegionPeople + "---");
                columns = new String[] {"region", "sum(people) as people"};
                groupBy = "region";
                having = "sum(people) > " + sRegionPeople;
                c = db.query("mytable", columns, null, null, groupBy, having, null);
                break;
                // Сортування
                case R.id.btnSort:
                // сортування за
                        switch (rgSort.getCheckedRadioButtonId()) {
                        // найменування
                        case R.id.rName:
                        Log.d(LOG_TAG, "--- Сортування за назвою ---");
                        orderBy = "name";
                        break;
                        // населення
                        case R.id.rPeople:
                        Log.d(LOG_TAG, "--- Сортування за населенням ---");
                        orderBy = "люди";
                        break;
                        // регіон
                        case R.id.rRegion:
                        Log.d(LOG_TAG, "--- Сортування по регіону ---");
                        orderBy = "Регіон";
                        break;
                }
                c = db.query("mytable", null, null, null, null, null, orderBy);
                break;
        }
        if (c != null) {
            if (c.moveToFirst()) {
            String str;
                do {
                     str = "";
                     for (String cn : c.getColumnNames()) {
                     str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                     }

                     Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
            c.close();
        } else
            Log.d(LOG_TAG, "Cursor is null");
            dbHelper.close();
        }

        DBHelper dbHelper;
        SQLiteDatabase db;

        class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
                // конструктор суперкласу
                super(context, "myDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
                Log.d(LOG_TAG, "--- onCreate database ---");
                // Створюємо таблицю з полями
                db.execSQL("create table mytable ("
                        + "id integer primary key autoincrement," + "name text,"
                        + "people integer," + "region text" + "); ");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int
                newVersion) {}
        }
}