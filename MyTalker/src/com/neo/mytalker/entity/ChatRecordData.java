package com.neo.mytalker.entity;

import java.io.Serializable;

public class ChatRecordData implements Serializable {
	public String msg;
	public int headId;
	public int usrId;
	public boolean isMe;
	public long time;
}
