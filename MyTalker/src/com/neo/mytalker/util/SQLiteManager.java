package com.neo.mytalker.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteManager {
	private String path = null;
	private String dataBaseName = null;
	private SQLiteDatabase db = null;
	private String table = null;
	
	public static final String COL_TYPE_TEXT = "text",
		COL_TYPE_INTEGER = "Integer", 
		COL_NAME_ID = "id",
		DB_NAME_HISTORY = "DBHistory",
		DB_NAME_RULE = "DBRule";

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPath() {
		return path;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public SQLiteManager(String path, String dataBaseName, String table) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.dataBaseName = dataBaseName;
		this.table = table;
		init();
	}

	private void init() {
		StringBuffer dbPath = new StringBuffer(path);
		if(!path.substring(path.length() - File.separator.length(),
				path.length()).equals(File.separator)
			) {
			dbPath.append(File.separator);
		}
		dbPath.append(dataBaseName);
//		Log.i("data base", dbPath.toString());
		db = SQLiteDatabase.openOrCreateDatabase(dbPath.toString(), null);
	}
	
	public void deleteItemById(int id) {
		if(hasTable()) {
			SQLiteManager.this.executeNotQuery("delete from "
				+SQLiteManager.this.getTable()+" where id="+id
			);
		}
	}
	
	//just text column
	public void deleteItem(Map<String, String>limit) {
		if(limit != null && limit.size() > 0) {
			StringBuffer lim = new StringBuffer();
			for(String colName : limit.keySet()) {
				lim.append(" "+colName+"='"+limit.get(colName)+"'");
			}
			SQLiteManager.this.executeNotQuery("delete from "
				+SQLiteManager.this.getTable()+" where "+lim.toString()
			);
		}
	}
	
	public void updateItem(int id, Map<String, String>change) {
		if(hasTable()) {
			StringBuffer values = new StringBuffer();
			for(String key : change.keySet()) {
				if(values.length() > 0) {
					values.append(',');
				}
				values.append(" "+key+"='"+change.get(key)+"'");
			}
			if(values.length() > 0) {
				values.append(" ");
				SQLiteManager.this.executeNotQuery("update "
					+SQLiteManager.this.getTable()
					+" set"+values+"where id = "+id
				);
			}
		}
	}

	//List中每一个元素是表中的一项，Map义同insertItem
	public void insertIterms(List<Map<String, String>>items) {
		if(hasTable()) {
			for(Iterator<Map<String, String>>it = items.iterator();
					it.hasNext();) {
				insertItem(it.next());
			}
		}
	}

	//item为表中的一项，Map中键为列名
	public void insertItem(Map<String,String>item) {
		if(hasTable()) {
			StringBuffer keys = new StringBuffer(),
				values = new StringBuffer();
			for(String key : item.keySet()) {
				if(keys.length() > 0) {
					keys.append(",");
				}
				keys.append(key);
				if(values.length() > 0) {
					values.append(",");
				}
				values.append("'"+item.get(key)+"'");
			}
			SQLiteManager.this.executeNotQuery(
				"insert into "+table+"("+keys
				+") values("+values+")"
			);
		}
	}

	private void executeNotQuery(String sql) {
		db.execSQL(sql);
	}

	//查询表 limit为限制条件，键为列名,若为空则查询所有
	public List<Map<String, String>> queryByLimit(Map<String, String>limit) {
		List<Map<String, String>>bs = new ArrayList<Map<String,String>>();
		if(hasTable()) {
			Cursor cs = db.query(table, null, null, null, null, null, null);
			if(cs.moveToFirst()) {
				Map<String, String> bd = null;
				while(cs.moveToNext()){
					bd = new HashMap<String, String>();
					for(int i = 0; i < cs.getColumnCount(); ++i) {
						String key = cs.getColumnName(i),
							value = cs.getString(i);
						bd.put(key, value);
					}
					boolean need = true;
					if(limit != null && limit.size() > 0) {
						for(String key : limit.keySet()) {
							String keyInBd = bd.get(key);
							if(keyInBd == null) {
								return null;
							}
							if(!(keyInBd).equals(limit.get(key))) {
								need = false;
								break;
							}
						}
					}
					if(need) {
						bs.add(bd);
					}
				}
			}
		}
		return bs;
	}

	public void showAll() {
		if(hasTable()) {
			List<Map<String, String>>res = SQLiteManager.this.queryByLimit(null);
			for(Iterator<Map<String, String>> it = res.iterator(); it.hasNext();) {
				String ans = ">>>>>>>>";
				Map<String, String> tb = it.next();
				for(String key : tb.keySet()) {
					ans += "\n"+key+":"+tb.get(key);
				}
			}
		}
	}

	//从某id开始向上查询若干条(id的那条不返回)
	public List<Map<String, String>> queryConsequent(int id, int number) {
		List<Map<String, String>>bs = new ArrayList<Map<String,String>>();
		if(SQLiteManager.this.hasTable()) {
			Cursor cs = db.query(table, null, null, null, null, null, null);
			if(cs.moveToLast()) {
				Map<String, String> bd = null;
				if(id >= 0) {
					while(cs.getInt(cs.getColumnIndex(
						SQLiteManager.COL_NAME_ID)) != id
						&& cs.moveToPrevious()
						) {}
					
					if(!cs.moveToPrevious()) {
						return bs;
					}
				}
	
				for(int j = 0; j < number; ++j){
					bd = new HashMap<String, String>();
					//将本行信息传出
					for(int i = 0; i < cs.getColumnCount(); ++i) {
						String key = cs.getColumnName(i),
							value = cs.getString(i);
						bd.put(key, value);
					}
					bs.add(0, bd);
					
					//跳向下一个 失败则退出
					if(!cs.moveToPrevious()){
						break;
					}
				}
			}
		}
		return bs;
	}

	public void createTable(Map<String, String>cols) {
		StringBuffer colTypes = new StringBuffer("id integer primary key autoincrement");
		for(String key : cols.keySet()) {
			colTypes.append(","+key);
			colTypes.append(" "+cols.get(key));
		}
		SQLiteManager.this.executeNotQuery("create table "+table+" ("+colTypes+")");
	}

	public boolean hasTable(){
		boolean exits = false;
		String sql = "select * from sqlite_master where name="+"'"+table+"'";
		Cursor cursor = db.rawQuery(sql, null);

		if(cursor.getCount()!=0){
			exits = true;
		}
		return exits;
	}
}
