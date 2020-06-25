package com.ssomar.executableitems.util;

public class Couple<T,Y> {

	private T elem1;
	
	private Y elem2;
	
	public Couple(T elem1, Y elem2) {
		this.elem1=elem1;
		this.elem2=elem2;
	}

	public T getElem1() {
		return elem1;
	}

	public void setElem1(T elem1) {
		this.elem1 = elem1;
	}

	public Y getElem2() {
		return elem2;
	}

	public void setElem2(Y elem2) {
		this.elem2 = elem2;
	}

	
	
	
	
}
