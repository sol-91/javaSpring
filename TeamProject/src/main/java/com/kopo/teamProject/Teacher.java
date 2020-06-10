package com.kopo.teamProject;

public class Teacher {
	String name;
	String id;
	String pw;
	String nclass;
	String address;
	int phone;
	String created;
	String modified;
	
	Teacher (){
	}
	
	Teacher(String id){
		this.id = id;
	}

	Teacher(String id, String pw){
		this.id = id;
		this.pw = pw;
	}

	Teacher(String name, String id, String pw, String nclass, String address, int phone, String created, String modified){
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.nclass = nclass;
		this.address = address;
		this.phone = phone;
		this.created = created;
		this.modified = modified;
		
		
	}

	Teacher(String name, String id, String pw, String nclass, String address, int phone){
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.nclass = nclass;
		this.address = address;
		this.phone = phone;
		
	}

}
