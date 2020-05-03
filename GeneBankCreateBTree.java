import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main driver file to read file and parse data
 * @author josh
 *
 */
public class GeneBankCreateBTree {

	private static Integer t; 		//degree to be used -- this is the value for degree
	//	private static BufferedReader br;
	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel = 3;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static int sequenceLength;		//this is the k value
	private static  String nameOfTree; // this will be the name of the binary file
	private BTree tree;

	public static void main(String[] args) throws IOException {

		//use a try-catch to parse all arguments. incorrect param will print usage
		try {
			//use caching or not
			if(Integer.parseInt(args[0]) == 1) {
				useCache = true;
				cacheSize = Integer.parseInt(args[4]);
				if(args[5] != null) {								//if cache is being used a cache, debug is in position 5 of args
					debugLevel = Integer.parseInt(args[5]);
				}
			}else {
				if(args.length == 5) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
					debugLevel = Integer.parseInt(args[4]);
				}
			}

			//parse degree
			t = Integer.parseInt(args[1]);	
			if(t == 0) {
				//implement something to use the optimal degree
			}
			//get sequence length
			sequenceLength = Integer.parseInt(args[3]);
			if(sequenceLength < 0 || sequenceLength > 31) {
				throw new Exception();
			}
			//parse debug level
		}catch(Exception e) {
			printUsage();
		}

		//get gbk file
		String fileName = args[2];
		
		//Get name of Binary file and make a new BTree
		
		nameOfTree = (fileName + ".btree.data." + sequenceLength  );	//This is the name of the binary file
		BTree tree = new BTree(nameOfTree,t,sequenceLength);
		
		try {
			Scanner scan = new Scanner(new FileReader(fileName));
			String line;

			while(scan.hasNextLine()) {
				line = scan.nextLine();
				
				while(line.startsWith("ORIGIN")) {
					String character = "";
					line =  scan.nextLine();
					
					while(!line.startsWith("//")) {
						for(int i = 0; i < line.length(); i++) {
							if(line.charAt(i) == 'a' || line.charAt(i) == 'c' || line.charAt(i) == 'g' || line.charAt(i) == 't' || line.charAt(i) == 'n'|| line.charAt(i) == 'A'|| line.charAt(i) == 'C'|| line.charAt(i) == 'G'|| line.charAt(i) == 'T') {
								character += line.charAt(i);
							}
						}
						line = scan.nextLine();
					}

					for(int i = 0; i < character.length()-sequenceLength+1;i++) {
						String s = "";
						for(int j = 0; j < sequenceLength; j++) {
							if(character.charAt(i+j) == 'a' || character.charAt(i+j) == 'A') {
								s += "00";
							}
							if(character.charAt(i+j) == 't' || character.charAt(i+j) == 'T') {
								s += "11";
							}
							if(character.charAt(i+j) == 'c' || character.charAt(i+j) == 'C') {
								s += "01";
							}
							if(character.charAt(i+j) == 'g' || character.charAt(i+j) == 'G') {
								s += "10";
							}
							if(character.charAt(i+j) == 'n' || character.charAt(i+j) == 'N') {
								s += "n";
							}
							
						}
						//OKAY! We now have s which we will use
						// S is the String of 0 ad 1's to converted to long - 0's in the front are dropped until the first 1, so 00000111 is just 111
						
						if(!s.contains("n")) {
							System.out.println(s);
							Long key = Long.parseLong(s);
							tree.BTreeInsert(key);
						}
						

						
					}

				}

			}
			scan.close();
			
		} catch ( Exception e) {
			e.printStackTrace();
		}

		if(debugLevel == 0) {
			System.err.println("No Status Messages. Please follow this usage example: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		}
		if(debugLevel == 1) {
			tree.dumpTree(sequenceLength*2);
		}
		if(debugLevel == 3) {
//			tree.printTree();
		}
		
	}
	public static void printUsage() {
		System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
}