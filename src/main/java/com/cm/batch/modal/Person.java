/**
 * 
 */
package com.cm.batch.modal;

import lombok.Data;

/**
 * @author chandresh.mishra
 *
 */
@Data
public class Person {

	private String name;
	private String lastName;
	private int age;
	private double salary;
	private int houseNumber;
	private String line1;
	private String line2;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public int getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", lastName=" + lastName + ", age=" + age + ", salary=" + salary
				+ ", houseNumber=" + houseNumber + ", line1=" + line1 + ", line2=" + line2 + "]";
	}
	
	

}
