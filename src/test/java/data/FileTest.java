package data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File testFile = new File("testfile1.datserv");
		System.out.println("file is exist - " +testFile.exists());
		if (!testFile.exists()) {
			System.out.println("file is created - " +testFile.createNewFile());
		}
		RandomAccessFile raf = new RandomAccessFile("testfile1.datserv", "rw");
		//System.out.println("Readed line: " + raf.readLine());
		raf.seek(raf.length());
		System.out.println(raf.getFilePointer());
		raf.writeBytes("TEST");
		raf.close();
		//PrintWriter pw = new PrintWriter("testfile1.datserv");
		//BufferedReader br = new BufferedReader (new FileReader("testfile1.datserv"));
		//System.out.println(br.);
		Files.lines(Paths.get("testfile1.datserv"), StandardCharsets.UTF_8).forEach(System.out::println);
		Files.write(Paths.get("testfile1.datserv"), "test2".getBytes(StandardCharsets.UTF_8));
	}

}
