package com.example.solotravellersapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
//
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;

//import net.sqlcipher.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;


import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DbHelper {
    @SuppressLint("StaticFieldLeak")
    static Context ctx;

    static Context act;
    public static boolean loadeddb = false;
//    public static SQLiteDatabase db = null;
    public static net.sqlcipher.database.SQLiteDatabase db = null; // Specify SQLCipher's SQLiteDatabase
    private String user_id;

    private String RawText;


    public DbHelper(Context ct) {
        DbHelper.ctx = ct;

        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx.getApplicationContext());

        //try setting up the db and catch exception if otherwise
        if (!loadeddb) {
            try {
                setupdb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPref = ct.getApplicationContext().getSharedPreferences("com.example.kulaapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("version_code", "" + getAppVersion(ct));
        editor.apply();
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "Kula.db";

//        SQLiteDatabase.loadLibs(ctx);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx);
    }

    private int getAppVersion(Context cont) {


        try {
            PackageInfo pinfo = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1; // Return default value in case of error
        }

        /**PackageInfo pinfo = null;
         try {
         pinfo = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0);
         } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
         }

         return pinfo.versionCode;**/
    }

//    public DbHelper(Context ctx, Activity act) {
//        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "   Kula.db";
////        SQLiteDatabase.loadLibs(ctx);
//        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx);
//        act = act;
//        if (db == null) {
//            db = SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);
//        }
//    }

    public DbHelper(Context ctx, Activity act) {
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "Kula.db";
        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx);
        act = act;
        if (db == null) {
//            db = SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);

            db = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);
//            db = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(dbpath, "your_secure_passphrase", null);

        }
    }

    private void setupdb() {

        Log.d("in helper", "db setup: " + "success");
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "Kula.db";
//        SQLiteDatabase.loadLibs(ctx);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx);
//        db = SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);
        db = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);

        createtables();

    }

    public void updateDBAppVersion(String version_code) {
        ContentValues ctx = new ContentValues();
        ctx.put("version_code", version_code);
        ctx.put("version_name", version_code);
        db.insert("app_version", null, ctx);
        Log.d("in helper", "Version update: " + "success");
    }

    public String getVersionCode() {
        try {
            Cursor cursor1 = db.rawQuery("SELECT MAX(id), version_code FROM app_version", null);
            cursor1.moveToFirst();
            if (!cursor1.isAfterLast()) {
                do {
                    return cursor1.getString(1);
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        } catch (Exception e) {

        }
        return "0";
    }

    public void log_event(Context act, String data) {
        try {
            String prefix = GlobalVariables.timeNow() + " : " + GlobalVariables.code;

            String root = Environment.getExternalStorageDirectory().toString() + "/School/.LOGS/";
            File file = new File(root);
            file.mkdirs();

            File gpxfile = new File(file, GlobalVariables.logDate() + "" + GlobalVariables.Log_file_name);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(prefix + data + "\n");
            writer.flush();
            writer.close();
        } catch (Exception ex) {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encrypt(String textRaw) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = textRaw;
        // Change this to UTF-16 if needed
        md.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        String str = new String(digest, StandardCharsets.UTF_8);
        String s = new String(digest);
        return hex;
    }

    private void InsertLog(ContentValues cv) {

        try {
            db.insert("logs", null, cv);
        } catch (Exception e) {

        }
    }


    private void createtables() {

        Log.e("in helper", "create tables: " + "success");
        //TO DO add columns

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY  NOT NULL ,displayname VARCHAR,password VARCHAR,about VARCHAR, pronouns VARCHAR,avatar image,agent_id INTEGER,status_id VARCHAR DEFAULT (null) ,sync_datetime LONG,email_address VARCHAR,phone_no VARCHAR,account_id INTEGER,employee_id INTEGER,account_name VARCHAR,rid INTEGER UNIQUE , branch VARCHAR, branch_id INTEGER, name VARCHAR, is_active VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS logs (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , user_id INTEGER, sync_datetime LONG DEFAULT CURRENT_TIMESTAMP, description VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS app_version  (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, version_code VARCHAR, version_name VARCHAR, datetime VARCHAR, status VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipes  (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, recipeXml VARCHAR)");
    }

//    public users getUserLoggedIn() {
//
//        int p_id = 0;
//        int refID = userLoggedIn();
//        String[] columns = {"_id", "username", "password", "account_name", "branch"};
//        String selection = "rid = ?";
//        String[] args = {"" + refID};
//        users uz = new users();
//        android.database.Cursor c = db.query("users", columns, selection, args, null,
//                null, null);
//        if (c != null) {
//            c.moveToFirst();
//            if (!c.isAfterLast()) {
//                do {
//
//                    uz.setAccount_name(c.getString(3));
//                    uz.setBranch(c.getString(4));
//                    uz.setUsername(c.getString(1));
//
//                } while (c.moveToNext());
//            } else {
//                uz.setAccount_name("");
//                uz.setBranch("");
//                uz.setUsername("");
//            }
//            c.close();
//
//        }
//
//        return uz;
//    }

    private int userLoggedIn() {

        int p_id = 0;
        int refID = (int) getMaxId("logs");
        String[] columns = {"COALESCE(user_id, 0)"}; // columns to select
        String selection = "_id = ?";
        String[] args = {"" + refID}; // value added into where clause -->
        android.database.Cursor c = db.query("logs", columns, selection, args, null, null, null);
        if (c != null) {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                do {
                    p_id = c.getInt(0);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p_id;
    }

    private Object getMaxId(String logs) {
        int id = 0;
        String[] columns = {"MAX(_id)"}; // columns to select
        String selection = "_id = ?";
        android.database.Cursor cursor = db.query("logs", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return id;
    }

    public String EncryptTextMD5(String password1) {

        String EncText = "";
        byte[] keyArray = new byte[24];
        byte[] temporaryKey;

        byte[] toEncryptArray = null;

        try {

            toEncryptArray = RawText.getBytes(StandardCharsets.UTF_8);
            MessageDigest m = MessageDigest.getInstance("MD5");
            temporaryKey = m.digest(getkey().getBytes(StandardCharsets.UTF_8));
            Cipher c = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            KeyGenerator k = KeyGenerator.getInstance("DESede");
            SecretKey key1;
            key1 = new SecretKeySpec(temporaryKey, k.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, key1);
            byte[] encrypted = c.doFinal(toEncryptArray);
            EncText = encodeBase64String(encrypted);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | InvalidKeyException | BadPaddingException NoEx) {
            Log.v("##################", NoEx.getMessage());
        }

        return EncText;
    }

    private String encodeBase64String(byte[] encrypted) {

        return encodeBase64String(encrypted);
    }


    private String getkey() {
        return act.getSharedPreferences("com.example.safety_cap", Context.MODE_PRIVATE).getString("move",
                "");
    }

    public Boolean loginUser(String username, String passcode) {

        boolean login_success = false;
        try {
            android.database.Cursor UsersCursor = db
                    .query("users",
                            new String[]{"_id", "username", "password",
                                    "account_name", "branch", "rid"},
                            "username = ? AND password = ? ",
                            new String[]{username.trim(), passcode.trim()},
                            null, null, "_id");
            UsersCursor.moveToFirst();

            if (!UsersCursor.isAfterLast()) {
                do {
                    String name = UsersCursor.getString(1);
                    login_success = true;
                    ContentValues cv = new ContentValues();
                    cv.put("user_id", UsersCursor.getInt(5));
                    InsertLog(cv);
                    GlobalVariables.userid = UsersCursor.getString(0);
                } while (UsersCursor.moveToNext());
            } else {
                login_success = false;
            }
            UsersCursor.close();

            return login_success;
        } catch (Exception e) {
            e.printStackTrace();
            login_success = false;
        }

        return login_success;
    }

    public void updateUser(ContentValues memberMap, int updatingID, ContentValues logMap) {

        int counter = 0;
        android.database.Cursor user = db.rawQuery("SELECT count(*) FROM users WHERE rid = ?", new String[]{"" + user_id});
        user.moveToFirst();
        if (!user.isAfterLast()) {
            do {
                counter = user.getInt(0);
            } while (user.moveToNext());
        }
        user.close();
        if (counter <= 0) {
            Log.v("LLLLLLLLLL", "KKKKKKKKKKKKKKKKK inserting");
            try {
                db.insert("users", null, memberMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String WhereClause = "rid = ?";
            String[] WhereArgs = {"" + user_id};
            try {
                Log.v("LLLLLLLLLL", "KKKKKKKKKKKKKKKKK updating");
                db.update("users", memberMap, WhereClause, WhereArgs);
            } catch (Exception ep) {
                ep.printStackTrace();
            }
        }
        // UPDATE LOG TBL
        try {
            db.insert("logs", null, logMap);
            // userModel.onCreate(null);
        } catch (Exception e) {
            Log.v("%%%%%%%%%%%% ", "" + e);
        }
    }


}
