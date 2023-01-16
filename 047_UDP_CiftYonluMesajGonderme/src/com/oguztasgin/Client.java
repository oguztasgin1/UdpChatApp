package com.oguztasgin;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class Client {

	private JFrame frmJavaClient;
	private JTextField textMessage;
	private JEditorPane editorPane;
	private JScrollPane scrollPane;
	private JButton btnSend;
	private DatagramSocket datagramSocket;
	private static int count = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frmJavaClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public synchronized void threadlerBurayaSenkronOlarakGirer() {
		count++;
	}

	/**
	 * Create the application.
	 * @throws SocketException 
	 */
	public Client() throws SocketException {
		threadlerBurayaSenkronOlarakGirer();
		initialize();
		datagramSocket = new DatagramSocket();
		Thread threadSender = new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendMessage(datagramSocket);	
			}
		});
		
		Thread threadReceiver = new Thread(new Runnable() {

			@Override
			public void run() {
				receiveMessage(datagramSocket);
			}
		});
		threadSender.start();
		threadReceiver.start();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJavaClient = new JFrame();
		frmJavaClient.setTitle("Java Client - " + count);
		frmJavaClient.setBounds(100, 100, 567, 363);
		frmJavaClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJavaClient.getContentPane().setLayout(null);
		
		textMessage = new JTextField();
		textMessage.setBounds(33, 115, 225, 20);
		frmJavaClient.getContentPane().add(textMessage);
		textMessage.setColumns(10);
		
		btnSend = new JButton("Send Message");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				sendMessage(datagramSocket);
				receiveMessage(datagramSocket);
			}
		});
		btnSend.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnSend.setBounds(304, 114, 107, 23);
		frmJavaClient.getContentPane().add(btnSend);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 176, 380, 137);
		frmJavaClient.getContentPane().add(scrollPane);
		
		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblMessage.setBounds(33, 88, 86, 14);
		frmJavaClient.getContentPane().add(lblMessage);
		
		JLabel lblInMessage = new JLabel("Incoming Message");
		lblInMessage.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblInMessage.setBounds(33, 157, 128, 14);
		frmJavaClient.getContentPane().add(lblInMessage);
	}
	
	public void sendMessage(DatagramSocket datagramSocket){
		try {
			InetAddress address = InetAddress.getLocalHost();
			String sendingMessage;
			
			sendingMessage = textMessage.getText();
			byte [] bufferSend = sendingMessage.getBytes();
			DatagramPacket packet = new DatagramPacket(bufferSend, bufferSend.length, address, 5000);
			datagramSocket.send(packet);
			editorPane.setText(editorPane.getText() + "\n" + sendingMessage);
			
			// To get receive
			
//			byte [] bufferReceive = new byte[1024];
//			packet = new DatagramPacket(bufferReceive, bufferReceive.length);
//			datagramSocket.receive(packet);
//			String receivedText = new String(bufferReceive, 0, packet.getLength());
//			editorPane.setText(editorPane.getText() + "\n" + receivedText);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveMessage(DatagramSocket datagramSocket) {
		
		try {
			byte [] bufferReceive = new byte[1024];
			DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
			datagramSocket.receive(packet);
			String receivedText = new String(bufferReceive, 0, packet.getLength());
			editorPane.setText(editorPane.getText() + "\n" + receivedText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
