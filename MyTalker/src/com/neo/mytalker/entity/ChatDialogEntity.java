package com.neo.mytalker.entity;

public class ChatDialogEntity {
	private String question;
	private String answer;
	private int id;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ChatDialogEntity(String question, String answer, int id) {
		super();
		this.question = question;
		this.answer = answer;
		this.id = id;
	}
	public ChatDialogEntity() {
		super();
	}
	@Override
	public String toString() {
		return "ChatDialogEntity [question=" + question + ", answer=" + answer + ", id=" + id + "]";
	}
	
	
}
