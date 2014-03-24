package com.evebit.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.evebit.wudawenda.R;

 
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
/**
 * 数据库
 * 
 * @author Bruce Liu
 * @since 20130427
 */
public class DBManager {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "user.db"; //淇�瀛������版��搴����浠跺��
    public static final String PACKAGE_NAME = "com.evebit.wudawenda";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //��ㄦ����洪��瀛���炬�版��搴����浣�缃�
 
    private SQLiteDatabase database;
    private Context context;
 
    public DBManager(Context context) {
        this.context = context;
    }
 
    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
 
    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {//��ゆ����版��搴����浠舵�����瀛����锛���ヤ��瀛���ㄥ����ц��瀵煎�ワ����������存�ユ��寮���版��搴�
                InputStream is = this.context.getResources().openRawResource(R.raw.user); //娆插�煎�ョ����版��搴�
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
    
    public void closeDatabase() {
        this.database.close();
    }
}