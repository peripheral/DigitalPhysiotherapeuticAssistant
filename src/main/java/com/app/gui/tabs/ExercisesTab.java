package com.app.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.app.db.DAO;
import com.app.entities.Entity;
import com.app.entities.Exercise;
import com.app.entities.ExerciseFactory;
import com.app.entities.Posture;
import com.app.gui.view.PatientView;

public class ExercisesTab extends JPanel {
	private JList<Entity> list  = new JList<Entity>();
	public ExercisesTab(){
		setLayout(new BorderLayout());	
		setUpExerciseList();
	}
	
	public ExercisesTab(PatientView parent) {
		this();		
	}

	private void setUpExerciseList() {
		DAO dao = new DAO();
		Entity[] listData = dao.getList(Exercise.class.getSimpleName());
		if(listData.length >0){
			list.setListData(listData);
		}else{
			System.out.println("List data is empty");
		}

		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		//list.setBackground(Color.BLUE);
		//list.setFixedCellWidth(500);
		list.setVisibleRowCount(-1);
		list.setFont(new Font("Tahoma", Font.BOLD, 24));
		ExerciseListCellRenderer cellRenderer = new ExerciseListCellRenderer();
		list.setCellRenderer(cellRenderer);
		ExerciseListCellRenderer renderer =  (ExerciseListCellRenderer) list.getCellRenderer();  
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBackground(Color.YELLOW);
		listScroller.getViewport().setBackground(Color.YELLOW);
		add(listScroller,BorderLayout.CENTER);		
	}
	
	class ExerciseListCellRenderer extends DefaultListCellRenderer {
	
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
		//	Container c = (Container) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			c.setText(((Entity)value).getName()+">>");
			return c;
		}
		
	}

	public JList<Entity> getList() {
		return list;
	}
	public Entity getSelectedValue(){
		return list.getSelectedValue();
	}
}
