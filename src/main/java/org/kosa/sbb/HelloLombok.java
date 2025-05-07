package org.kosa.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@Setter
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class HelloLombok {
	final private String hello;
	final private int lombok;
	
//	public HelloLombok(String hello, int lombok) {
//		this.hello = hello;
//		this.lombok = lombok;
//	}
	
//	public static void main(String [] args) {
////		HelloLombok obj = new HelloLombok();
////		obj.setHello("안녕하세요");
////		obj.setLombok(10);
//		HelloLombok obj = new HelloLombok("안녕하세요", 10);
//		
////		System.out.println("hello : " + obj.getHello());
////		System.out.println("lombok : " + obj.getLombok());
//		System.out.println(obj);
//	}
}
