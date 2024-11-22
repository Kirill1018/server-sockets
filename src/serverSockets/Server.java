package serverSockets;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
	static DataOutputStream dataOutputStream = null;//data object of output stream
	static DataInputStream dataInputStream = null;//data object of input stream
	public static void main(String[] args) {
		File file = new File("files");//file object
		file.mkdirs();
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(900)) {//implementation of server sockets
				Socket clientSocket = serverSocket.accept();//socket object
				dataInputStream = new DataInputStream(clientSocket.getInputStream());//data object of input stream
				dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());//data object of output stream
				String firstFile = file.getName() + "/" + "file.txt", fullName = new String();//names of first file of download and next file of download
				receiveFile(firstFile);
				try (FileReader reader = new FileReader(firstFile)) {//being inherited from abstract class of reader and providing functionality for reading text files
					int c;//coded symbol
					while ((c = reader.read()) != -1) fullName += (char)c;//getting file name
				}
				catch (IOException iOException) { }
				receiveFile(file.getName() + "/" + fullName);
				System.out.println("файл " + fullName + " получен. он отправлен в папку " + file.getName());
				closeConn(clientSocket);
			}
			catch(Exception e) { }
		}
	}
	static void receiveFile(String fileName) throws Exception {
		int bytes = 0;//size of empty file
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);//stream object of file output
		long size = dataInputStream.readLong();//length of input stream of data
		byte[] buffer = new byte[4 * 1024];//byte array for download file
		while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes;//decreasing file length
		}
		fileOutputStream.close();
	}
	static void closeConn(Socket custSock) throws IOException {
		dataInputStream.close();
		dataOutputStream.close();
		custSock.close();
	}
}