package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DemoUI implements KeyListener{

	public Main bot;
	public JTextArea chatbox;
	public JTextField input;
	public long start;
	public LinkedList<String> prev = new LinkedList<String>();
	public int cur = -1;
	
	public DemoUI(Main b){
		
		start = System.currentTimeMillis();
		bot = b;
		JFrame frame = new JFrame("KeyWord FAQ-Bot Demo");
		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(800,600));
		content.setBackground(Color.white);
		chatbox = new JTextArea();
		chatbox.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		chatbox.setTabSize(2);
		chatbox.setWrapStyleWord(true);
		chatbox.setLineWrap(true);
		JScrollPane cscroll = new JScrollPane(chatbox);
		cscroll.setPreferredSize(new Dimension(800,550));
		chatbox.setEditable(false);
		content.add(cscroll);
		input = new JTextField();
		input.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		input.setPreferredSize(new Dimension(800, 40));
		content.add(input);
		
		input.addKeyListener(this);
		
		frame.setResizable(false);
		frame.setContentPane(content);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		chatbox.setText("Bot Started");
		input.requestFocus();
		
	}
	
	public void query(){
		
		cur = -1;
		
		String q = input.getText();
		prev.addFirst(q);
		input.setText("");
		chatbox.setText(chatbox.getText()+"\n["+(System.currentTimeMillis()-start)+"] User: \n\t"+q);
		
		String r = bot.query(q);
		chatbox.setText(chatbox.getText()+"\n["+(System.currentTimeMillis()-start)+"] Bot: \n\t"+r+"\n");
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_ENTER && input.getText().length() > 3)
			query();
		else if(e.getKeyCode() == e.VK_UP && cur < prev.size()-1){
			cur++;
			input.setText(prev.get(cur));
		}
		else if(e.getKeyCode() == e.VK_DOWN && cur > -1){
			cur--;
			
			if(cur == -1)
				input.setText("");
			else
				input.setText(prev.get(cur));
				
		}
			
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(input.getText().length() > 255)
			input.setText(input.getText().substring(0, 255));
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}
