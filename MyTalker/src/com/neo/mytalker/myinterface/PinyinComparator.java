package com.neo.mytalker.myinterface;

import java.util.Comparator;

import com.neo.mytalker.entity.ChatDialogEntity;

public class PinyinComparator implements Comparator<ChatDialogEntity>{

	@Override
	public int compare(ChatDialogEntity lhs, ChatDialogEntity rhs) {
		// TODO Auto-generated method stub
		if (lhs.getSortLetters().equals("@")
				|| rhs.getSortLetters().equals("#")) {
			return -1;
		} else if (lhs.getSortLetters().equals("#")
				|| rhs.getSortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getSortLetters().compareTo(rhs.getSortLetters());
		}
	}

	
	
}
