package org.fugiczek.gwbot;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainBoard extends JFrame implements ActionListener{

	private static final long serialVersionUID = -7089637730319798637L;
	
	
	private final GW2Tool tool = GW2Tool.INSTANCE;
	private JSlider speed;
	private JLabel curSpeed;
	private JButton setDefaultSpeed;
	
	private Timer timer;
	
	public MainBoard(String title){
		setTitle(title);
		init();
	}
	
	private void init(){
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(0,1,5,5));
		
		JMenuBar menuBar = new JMenuBar();
			JMenu settings = new JMenu("Settings");
				final JCheckBoxMenuItem alwaysOnTop = new JCheckBoxMenuItem("Always On Top");
				alwaysOnTop.setSelected(true);
				alwaysOnTop.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(alwaysOnTop.isSelected()){
							setAlwaysOnTop(true);
						}else{
							setAlwaysOnTop(false);
						}
					}
				});
			settings.add(alwaysOnTop);
		menuBar.add(settings);
		setJMenuBar(menuBar);
				
		speed = new JSlider(JSlider.HORIZONTAL, 0, 50, 9);
			speed.setMajorTickSpacing(10);
			speed.setMinorTickSpacing(1);
			speed.setPaintTicks(true);
			speed.setPaintLabels(true);
		speed.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()){
					float value = source.getValue();
					tool.setSpeedValue(value);
				}
			}
		});
		
		JPanel panelSpeed = new JPanel(new GridLayout(1,0));
		
		curSpeed = new JLabel("Current Speed -> ");
		
		setDefaultSpeed = new JButton("Set Default Speed");
		setDefaultSpeed.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				speed.setValue(9);
				tool.setSpeedValue(9.1875f);				
			}
		});
		
		panelSpeed.add(curSpeed);
		panelSpeed.add(setDefaultSpeed);
		
		add(speed);
		add(panelSpeed);
			
		pack();
		setVisible(true);
		
		timer = new Timer(100, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		curSpeed.setText("Current Speed ->\n" + tool.getSpeedValue());
	}
	
}
