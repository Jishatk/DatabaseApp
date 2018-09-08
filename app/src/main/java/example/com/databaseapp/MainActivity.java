package example.com.databaseapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etRoll, etName;
    Button badd, bupdate, bdelete, bfind;
    SQLiteDatabase db;
    SQLiteStatement stmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etRoll = findViewById(R.id.editText);
        etName = findViewById(R.id.editText2);
        badd = findViewById(R.id.button);
        bupdate = findViewById(R.id.button2);
        bdelete = findViewById(R.id.button3);
        bfind = findViewById(R.id.button4);

        badd.setOnClickListener(this);
        bupdate.setOnClickListener(this);
        bdelete.setOnClickListener(this);
        bfind.setOnClickListener(this);
        try {
            db = openOrCreateDatabase("StudDB", Context.MODE_PRIVATE, null);
            db.execSQL("create table if not exists student(roll text ,name text)");
        } catch (Exception e) {
            Log.e("DatabaseApp", "Exception" + e);
        }
    }

    public void onClick(View v) {
        try {
            if (v == badd) {
                Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
            //    db.execSQL("INSERT INTO student VALUES('" + etRoll.getText().toString() + "','" + etName.getText().toString() + "')");
                stmt=db.compileStatement("insert into student values(?,?)");
                stmt.bindString(1,etRoll.getText().toString() );
                stmt.bindString(2,etName.getText().toString());
                long rid=stmt.executeInsert();
                if(rid!=1)
                    Toast.makeText(getApplicationContext(), "Record added successfully", Toast.LENGTH_SHORT).show();
                clearText();
            }

            if (v == bupdate) {
                Cursor c = db.rawQuery("SELECT * FROM student where roll='" + etRoll.getText().toString() + "' ", null);
                c.moveToFirst();
                db.execSQL("UPDATE student SET name='" + etName.getText().toString() + "'  WHERE roll='"+etRoll.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), "update", Toast.LENGTH_SHORT).show();
                clearText();
            }

            if (v == bdelete) {
              /*  Cursor c = db.rawQuery("SELECT * FROM student where roll='" + etRoll.getText().toString() + "' ", null);
                c.moveToFirst();
                db.execSQL("delete from student WHERE roll='"+etRoll.getText().toString()+"'");*/

                stmt = db.compileStatement("DELETE FROM student WHERE roll = ?");
                stmt.bindString(1, etRoll.getText().toString());
                int c=stmt.executeUpdateDelete();
                if(c==1)
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                clearText();
            }

            if (v == bfind) {
                String r = etRoll.getText().toString();
                Cursor c = db.rawQuery("SELECT * FROM student where roll='" + etRoll.getText().toString() + "' ", null);
                c.moveToFirst();
                etName.setText(c.getString(1));
                etRoll.setText(c.getString(0));
                Toast.makeText(getApplicationContext(), "find" + c.getCount(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Log.e("DatabaseApp", "Exception" + e);
        }
    }

    public void clearText()
    {
        etRoll.setText("");
        etName.setText("");
    }
}



