package com.oguztasgin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Server2 {
	
	public final static int PORT = 5000;
	private final static int BUFFER = 1024;
	
	public static void main(String[] args) {
		ArrayList<InetAddress> client_addresses = new ArrayList<>();
		List<Integer> client_ports = new ArrayList<>();
		HashSet<String> existing_clients = new HashSet<>();
		// Bu ornekte tek bir bilgisayar kullanılmıstir.
		// Eger farkli bilgisayarlardan mesajlasma istenirse port numarası yerine ip adresi yazılarak haberlesme saglanabilir.
		System.out.println("Server baslatiliyor.");
		try {
			@SuppressWarnings("resource")
			DatagramSocket socket = new DatagramSocket(PORT);
			
			while(true) {
				byte[] buffer = new byte[BUFFER];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String receivedText = new String(buffer, 0, packet.getLength());
				System.out.println("Text received is " + receivedText);
				
				// To add timeStamp to sending message.
				String time = (String) LocalTime.now().toString().subSequence(0, 8);
				String returnString1  = receivedText;
				String returnString2  = time + ": " + receivedText;
				
				// To get client addres and port number.
				InetAddress address = packet.getAddress();
				int client_port = packet.getPort();
				
				// To send same message to sender.
				if (client_ports.contains(client_ports)) {
					byte[] bufferSending1 = new byte[BUFFER];
					bufferSending1 = (returnString1).getBytes();
					packet = new DatagramPacket(bufferSending1, bufferSending1.length, address, packet.getPort());
					socket.send(packet);
				}
	
				// Check if address and port number exist int our hastSet
				String id = address.toString() + "|" + client_port;
				if (!existing_clients.contains(id)) {
					existing_clients.add(id);
					client_ports.add(client_port);
					client_addresses.add(address);
				}
				
				byte[] bufferSending = new byte[BUFFER];
				bufferSending = (returnString2).getBytes();
				
				for (int i = 0; i < client_ports.size(); i++) {
					if(client_ports.get(i) != client_port) {
						packet = new DatagramPacket(bufferSending, bufferSending.length, address, client_ports.get(i));
						socket.send(packet);
					}
				}
			}
			
					
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
