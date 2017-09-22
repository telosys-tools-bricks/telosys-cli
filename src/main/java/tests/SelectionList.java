/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tests;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionList {
	
	//private final Font font = new Font("Courier", Font.PLAIN, 18) ;
	private final Font font = new Font("DialogInput", Font.PLAIN, 16) ;

	private Frame frame;
	private List  list;
	private Label headerLabel;
	private Label statusLabel;
	private Panel controlPanel;

	public SelectionList(java.util.List<String> items) {
		prepareGUI(items);
	}

	public void prepareGUI(java.util.List<String> items ) {
		frame = new Frame("Java AWT Examples");
		//frame.setSize(400, 400);
		frame.setBounds(100, 100, 300, 400); // x, y, width, height
		frame.setUndecorated(true); // no title bar
		frame.setBackground(Color.GRAY);
		frame.setFont(font);
		frame.setAlwaysOnTop(true);
		
		//mainFrame.setLayout(new GridLayout(3, 1));
		
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		//frame.setLayout(new GridLayout(0, 1));
		//frame.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.anchor = GridBagConstraints.NORTH;
	    gbc.fill = GridBagConstraints.VERTICAL;
	    gbc.weighty = 1;
		
//		frame.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent windowEvent) {
//				System.exit(0);
//			}
//		});
		
//		headerLabel = new Label();
//		headerLabel.setAlignment(Label.CENTER);
//		headerLabel.setText("Control in action: List");
//
//		statusLabel = new Label();
//		statusLabel.setAlignment(Label.CENTER);
//		statusLabel.setSize(350, 100);

//		controlPanel = new Panel();
//		controlPanel.setLayout(new FlowLayout());

//		mainFrame.add(headerLabel);
		
		//mainFrame.add(controlPanel);
		list = createList(items);
		gbc.gridx = 0 ;
		gbc.gridy = 0 ;
		
		frame.add(list, gbc);
		
		gbc.gridx = 0 ;
		gbc.gridy = 1 ;
		frame.add(createButton(), gbc );
//		mainFrame.add(statusLabel);

		//mainFrame.setVisible(true);
	}
	
	private Button createButton() {
		Button button = new Button(" Ok ");
//		Dimension minimumSize = new Dimension(300,400); //  width, int height
//		button.setPreferredSize(new Dimension(100,10));
		Dimension d = new Dimension(200,20); //  width, height
		button.setMinimumSize(d);
		button.setMaximumSize(d);
		button.setSize(d);
		button.setPreferredSize(d);

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String data = "selected : ";
//				String[] selectedItems = list.getSelectedItems() ;
//				if ( selectedItems != null ) {
//					for (String s : selectedItems ) {
//						data += s + " ";
//					}
//					//statusLabel.setText(data);
//				}
				for (String s : list.getSelectedItems() ) {
					data += s + " ";
					System.out.println(" . " + s );
				}
				hide();
			}
		});
		return button;
	}
	
	private List createList(java.util.List<String> items) {
		//final List list = new List(14, true);
		final List list = new List(12, true);
		Dimension d = new Dimension(300,400); //  width, height
		list.setMinimumSize(d);
		list.setMaximumSize(d);
		list.setSize(d);
		list.setPreferredSize(d);
		for ( String s : items ) {
			list.add(s);
		}
		return list;
	}
	
	public void showListDemo(java.util.List<String> items ) {

		headerLabel.setText("Control in action: List");

		final List list = new List(10, true);
		for ( String s : items ) {
			list.add(s);
		}


		controlPanel.add(list);
		//controlPanel.add(button);

		frame.setVisible(true);
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

}