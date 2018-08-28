/**
 * 
 */
package org.dpo.mnemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

/**
 * @author dapon
 *
 */
public class WorkingMemory extends JFrame implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4298078282366750949L;

	
	public static final long TEMPO = 1000;
	public static final int DEFAULT_SEQUENCE_SIZE = 9;

	public static String TYPED = "Typed : ";
	public static String SOLUTION = "Solution : ";
	public static final String NO_TEXT = "...";

	JPasswordField passwordFieldAsc;
	JPasswordField passwordFieldDesc;
	JButton buttonPlay;
	JButton buttonCheck;
	private JLabel labelFieldResult;
	private JLabel labelFieldResultValue;

	private List<Integer> listSequence;

	

	public JPasswordField getPasswordFieldAsc() {
		return passwordFieldAsc;
	}

	public JPasswordField getPasswordFieldDesc() {
		return passwordFieldDesc;
	}

	public List<Integer> getListSequence() {
		return listSequence;
	}

	public void setListSequence(List<Integer> listSequence) {
		this.listSequence = listSequence;
	}

	public JButton getButtonPlay() {
		return buttonPlay;
	}

	public JButton getButtonCheck() {
		return buttonCheck;
	}

	public JLabel getLabelFieldResult() {
		return labelFieldResult;
	}

	public JLabel getLabelFieldResultValue() {
		return labelFieldResultValue;
	}

	public String getStrSequenceAsc() {
		StringBuffer strBuff = new StringBuffer();
		for (Integer n : getListSequence()) {
			strBuff.append(n);
		}
		return strBuff.toString();
	}

	public String getStrSequenceDesc() {
		StringBuffer strBuff = new StringBuffer();

		for (Integer n : getListSequence()) {
			strBuff.append(n);
		}
		strBuff = strBuff.reverse();
		return strBuff.toString();
	}

	public void addRandomNumberInSequence() {
		int random = -1;
		if (getListSequence() == null) {
			setListSequence(new ArrayList<Integer>());
		}
		random = new Random().nextInt(10);
		getListSequence().add(random);
	}

	public void createSequence(int n) {
		setListSequence(new ArrayList<Integer>());
		for (int i = 0; i < n; i++) {
			addRandomNumberInSequence();
		}
	}

	public void createSequence() {
		createSequence(DEFAULT_SEQUENCE_SIZE);
	}

	public void playSequence() {
		Timer timer = new Timer();
		PlaySequenceTask playSequenceTask = PlaySequenceTask.buildPlaySequenceTask(this);
		timer.schedule(playSequenceTask, 0, TEMPO);
	}
	
	public void sequenceEnded(){
		getButtonCheck().setEnabled(true);
		getPasswordFieldAsc().setEnabled(true);
		getPasswordFieldDesc().setEnabled(true);
	}


	public void init() {
		this.setTitle("Working Memory");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(0, 2));
		Border border = BorderFactory.createTitledBorder("Input");
		panel.setBorder(border);
		JLabel labelFieldAsc = new JLabel("Chronological order :");
		JLabel labelFieldDesc = new JLabel("Reverse order :");
		passwordFieldAsc = new JPasswordField(10);
		passwordFieldAsc.addKeyListener(this);
		passwordFieldDesc = new JPasswordField(10);
		passwordFieldDesc.addKeyListener(this);
		buttonPlay = new JButton("Play");
		buttonPlay.addActionListener(this);
		buttonCheck = new JButton("Check");
		buttonCheck.setEnabled(false);
		buttonCheck.addActionListener(this);
		labelFieldResult = new JLabel(TYPED);
		labelFieldResultValue = new JLabel(NO_TEXT);
		panel.add(labelFieldAsc);
		panel.add(passwordFieldAsc);
		panel.add(labelFieldDesc);
		panel.add(passwordFieldDesc);
		panel.add(buttonPlay);
		panel.add(buttonCheck);
		panel.add(labelFieldResult);
		panel.add(labelFieldResultValue);
		Container contentPane = this.getContentPane();
		contentPane.add(panel, BorderLayout.CENTER);
		this.setSize(400, 140);
		this.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WorkingMemory workingMemory = new WorkingMemory();
		workingMemory.init();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getButtonPlay())) {
			getButtonPlay().setEnabled(false);
			getButtonCheck().setEnabled(false);
			getPasswordFieldAsc().setEnabled(false);
			getPasswordFieldDesc().setEnabled(false);
			
			createSequence();
			playSequence();

			getLabelFieldResult().setText(TYPED);
			getLabelFieldResultValue().setText(NO_TEXT);

			getPasswordFieldAsc().setText(null);
			getPasswordFieldDesc().setText(null);

			getPasswordFieldAsc().setEditable(true);
			getPasswordFieldDesc().setEditable(true);

			getPasswordFieldAsc().setForeground(Color.black);
			getPasswordFieldDesc().setForeground(Color.black);

			getPasswordFieldAsc().setEchoChar('*');
			getPasswordFieldDesc().setEchoChar('*');

		} else if (e.getSource().equals(getButtonCheck())) {
			getButtonPlay().setEnabled(true);
			getButtonCheck().setEnabled(false);

			getLabelFieldResult().setText(SOLUTION);
			getLabelFieldResultValue().setText(getStrSequenceAsc());

			getPasswordFieldAsc().setEditable(false);
			getPasswordFieldDesc().setEditable(false);

			if (getStrSequenceAsc().equals(new String(getPasswordFieldAsc().getPassword()))) {
				getPasswordFieldAsc().setForeground(Color.green);
			} else {
				getPasswordFieldAsc().setForeground(Color.red);
			}

			if (getStrSequenceDesc().equals(new String(getPasswordFieldDesc().getPassword()))) {
				getPasswordFieldDesc().setForeground(Color.green);
			} else {
				getPasswordFieldDesc().setForeground(Color.red);
			}
			getPasswordFieldAsc().setEchoChar((char) 0);
			getPasswordFieldDesc().setEchoChar((char) 0);
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		getLabelFieldResultValue().setText("" + e.getKeyChar());
	}

	public void keyReleased(KeyEvent e) {

	}


}
