package com.activequant.aqviz.interfaces;

public interface IVisualTable {

	void setTitle(String title);
	void changeSelection(int row, int col, boolean toogle, boolean extend);
}