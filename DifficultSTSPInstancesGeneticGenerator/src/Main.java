import java.io.*;
import java.util.*;
import tsp.*;
import tsp.bigbangs.*;
import tsp.generators.*;
import tsp.generators.bedrooms.*;
import tsp.generators.potions.*;
import tsp.judges.*;
import tsp.selectors.*;

public class Main {

	// PARAMETRI
	static String numtest = "21";
	static int mincost = 1;
	static int maxcost = 100;
	static int size = 20;
	static int popsize = 20;
	static int generations = 50000;

	static Set<IndividuoTSP> oldgen;
	static Set<IndividuoTSP> newpop;
	static Random r = new Random();

	// STRUMENTI
	static Comparator<IndividuoTSP> comparator = new FitnessComparator();
	static Judge judge = new HeldKarpSJudge();

	static BigBang bigbang = new TotalRandomSBigBang(r, mincost, maxcost);

	static Bedroom bedroom = new UniformSBedroom(r, 50);
	// static Bedroom bedroom = new SinglePointSBedroom(r);

	static Potion potion = new RandomArcsPercentagesSPotion(r, size / 10, 80, 120);

//	static Generator generator = new EveryoneBothOperationGenerator(potion, bedroom);
//	 static Generator generator = new Everyone1OperationGenerator(r, potion,
//	 bedroom, false);
	static Generator generator = new ProportionalTournamentGenerator(potion, bedroom, r, popsize / 5);

	// static Selector selector = new SteadyStateSelector(comparator, popsize);
	static Selector selector = new ElitarianSelector(comparator, popsize, popsize / 3);

	static int thisgen = 0;

	public static void main(String[] args) throws Exception {

		/////////
		// LOG //
		String logdirectory = "C:\\Users\\Ugo\\Dropbox\\Uni\\M2\\Progetto IA\\Risultati\\Test\\" + numtest + "\\";
		File dir = new File(logdirectory);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}

		genetico(logdirectory, false);

		// validazione(logdirectory, judge, 1906);

		// HashSet<Integer> set = new HashSet<Integer>();
		// set.add(961);
		// set.add(1158);
		// set.add(1166);
		//
		// validazione(logdirectory, judge, set);

	}

	/**
	 * Load every instance of the specified path and re-judge them.
	 * 
	 * @param cartella
	 *            The path with the instances
	 * @param judge
	 *            The judge for the second judgement
	 * @param highID
	 *            The number of the highest ID file
	 * @throws Exception
	 *             If there are problems with the files (file not presents,
	 *             wrong format ecc)
	 */
	public static void validazione(String cartella, Judge judge, int highID) throws Exception {
		File f = new File(cartella + highID + ".txt");
		while (f.exists()) {
			IndividuoTSP ind = loadIndividuo(f);
			System.out.println("Caricato individuo" + ind.getId());
			HashSet<IndividuoTSP> set = new HashSet<IndividuoTSP>();
			set.add(ind);
			judge.judge(set);

			FileWriter valfw = new FileWriter(cartella + "validazione.txt", true);
			PrintWriter valpw = new PrintWriter(valfw, true);

			String s = ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1)
					+ "\t" + (int) (ind.getScores().get("fitness1") / 1);

			System.out.println(s);
			valpw.println(s);
			valpw.close();

			highID--;
			f = new File(cartella + highID + ".txt");
		}

	}

	/**
	 * Load some instances in the specified path and re-judge them.
	 * 
	 * @param cartella
	 *            The path with the instances
	 * @param judge
	 *            The judge for the second judgement
	 * @param idset
	 *            The instances that must be re-judged
	 * @throws Exception
	 *             If there are problems with the files (file not presents,
	 *             wrong format ecc)
	 */
	public static void validazione(String cartella, Judge judge, Set<Integer> idset) throws Exception {
		for (Integer i : idset) {
			File f = new File(cartella + i + ".txt");
			IndividuoTSP ind = loadIndividuo(f);
			System.out.println("Caricato individuo " + ind.getId());
			HashSet<IndividuoTSP> set = new HashSet<IndividuoTSP>();
			set.add(ind);
			judge.judge(set);

			FileWriter valfw = new FileWriter(cartella + "validazione.txt", true);
			PrintWriter valpw = new PrintWriter(valfw, true);

			String s = ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1)
					+ "\t" + (int) (ind.getScores().get("fitness1") / 1);

			System.out.println(s);
			valpw.println(s);
			valpw.close();
		}

	}

	/**
	 * Perform the genetic algorithm
	 * 
	 * @param logdirectory
	 *            The path in which the logs will be saved
	 * @param load
	 *            False if the algorithm must generate the first population
	 *            using the BigBang, True if the algorithm must load the last
	 *            population written in the files
	 * @throws Exception
	 *             If there are problems with the I/O
	 */
	public static void genetico(String logdirectory, boolean load) throws Exception {
		// file config.txt con la configurazione
		FileWriter conffw = new FileWriter(logdirectory + "config.txt", true);
		PrintWriter confpw = new PrintWriter(conffw, true);
		String instances = logdirectory + "instances.txt";
		// file instances.txt cogni istanza nel formato ID-generazione-punteggio
		FileWriter instfw = new FileWriter(instances, true);
		PrintWriter instpw = new PrintWriter(instfw, true);
		// file popolazioni.txe ogni generazione, da che individui è composta
		String popolazioni = logdirectory + "popolazioni.txt";
		FileWriter popfw = new FileWriter(popolazioni, true);
		PrintWriter poppw = new PrintWriter(popfw, true);
		// per ogni istanza, file ID.txt (dopo averla valutata: ID-generazione,
		// punteggio, matrice)
		FileWriter probfw;
		PrintWriter probpw;
		// LOG //
		/////////

		/////////
		// LOG //
		String config = "";
		config += "test\t" + numtest + "\n";
		config += "mincost\t" + mincost + "\n";
		config += "maxcost\t" + maxcost + "\n";
		config += "size\t" + size + "\n";
		config += "popsize\t" + popsize + "\n";
		config += "Judge\t" + judge + "\n";
		config += "Bigbang\t" + bigbang + "\n";
		// config+="Chooser\t"+chooser+"\n";
		config += "Bedroom\t" + bedroom + "\n";
		config += "Potion\t" + potion + "\n";
		config += "Generator\t" + generator + "\n";
		config += "Selector\t" + selector + "\n";
		System.out.println(config);
		// scrivo il file config.txt
		confpw.println(config);
		confpw.close();
		// LOG //
		/////////

		if (load) {
			oldgen = caricaPop(logdirectory);
			System.out.println("CICLO " + thisgen + ":");
			for (IndividuoTSP ind : oldgen) {
				System.out.println(
						ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1));
			}
			System.out.println("_____________________");

		} else {
			oldgen = bigbang.generatePopulation(popsize, size);

			System.out.println("Individui generati, giudico...");

			judge.judge(oldgen);

			/////////
			// LOG //
			// scrivo nel file popolazioni
			poppw.print(thisgen + "\t");
			System.out.println("CICLO " + thisgen + ":");
			for (IndividuoTSP ind : oldgen) {
				// scrivo nel file instances
				instpw.println(
						ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1));
				// scrivo nel file popolazioni
				poppw.print(ind.getId() + " ");
				// creo e scrivo nel file ID
				probfw = new FileWriter(logdirectory + ind.getId() + ".txt", true);
				probpw = new PrintWriter(probfw, true);
				probpw.println(ind.getId());
				probpw.println(ind.getSize());
				probpw.println(ind.getGeneration());
				probpw.println((int) (ind.getScores().get("fitness") / 1));
				probpw.println(ind.getMatrixAsString());
				probpw.close();

				System.out.println(
						ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1));
			}
			System.out.println("_____________________");
			poppw.close();
			instpw.close();
			// LOG //
			/////////

		}

		thisgen++;

		for (; thisgen < generations; thisgen++) {
			System.out.println("CICLO " + thisgen + ": creazione dei nuovi individui");
			// genero la nuova popolazione e la giudico
			newpop = generator.generate(oldgen, thisgen, mincost, maxcost);

			for (IndividuoTSP ind : newpop) {
				// creo e scrivo nel file ID
				probfw = new FileWriter(logdirectory + "temp" + ind.getId() + ".txt", false);
				probpw = new PrintWriter(probfw, true);
				probpw.println(ind.getId());
				probpw.println(ind.getSize());
				probpw.println(ind.getGeneration());
				probpw.println(ind.getMatrixAsString());
				probpw.close();
			}

			System.out.println("Individui generati, giudico...");
			judge.judge(newpop);

			/////////
			// LOG //
			// scrivo nei file instances e ID
			instfw = new FileWriter(instances, true);
			instpw = new PrintWriter(instfw, true);
			for (IndividuoTSP ind : newpop) {
				// scrivo nel file instances
				instpw.println(
						ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1));
				// creo e scrivo nel file ID
				probfw = new FileWriter(logdirectory + ind.getId() + ".txt", false);
				probpw = new PrintWriter(probfw, true);
				probpw.println(ind.getId());
				probpw.println(ind.getSize());
				probpw.println(ind.getGeneration());
				probpw.println((int) (ind.getScores().get("fitness") / 1));
				probpw.println(ind.getMatrixAsString());
				probpw.close();

				// System.out.println(
				// ind.getId() + "\t" + ind.getGeneration() + "\t" + (int)
				// (ind.getScores().get("fitness") / 1));

				File temp = new File(logdirectory + "temp" + ind.getId() + ".txt");
				temp.delete();
			}
			System.out.println("_____________________");
			instpw.close();
			// LOG //
			/////////

			// selezione naturale
			oldgen = selector.select(oldgen, newpop);

			/////////
			// LOG //
			popfw = new FileWriter(popolazioni, true);
			poppw = new PrintWriter(popfw, true);
			// scrivo nel file popolazioni
			poppw.print("\n" + thisgen + "\t");
			System.out.println("CICLO " + thisgen + ", popolazione:");
			for (IndividuoTSP ind : oldgen) {
				// scrivo nel file popolazioni
				poppw.print(ind.getId() + " ");
				System.out.println(
						ind.getId() + "\t" + ind.getGeneration() + "\t" + (int) (ind.getScores().get("fitness") / 1));
			}
			System.out.println("_____________________");
			poppw.close();
			// LOG //
			/////////

		}

		// DEBUG giudico di nuovo la popolazione sopravvissuta e stampo i
		// risultati
		judge.judge(oldgen);
		System.out.println("CICLO FINALE: generazione " + (generations - 1));
		for (IndividuoTSP ind : oldgen) {
			System.out.println(ind.getId() + "\t" + ind.getGeneration() + "\t"
					+ (int) (ind.getScores().get("fitness") / 1) + "\t" + (int) (ind.getScores().get("fitness1") / 1));
		}
	}

	/**
	 * Load the last population of the test
	 * 
	 * @param logdirectory
	 *            The test directory
	 * @return The loaded population
	 * @throws Exception
	 *             If there are problems with the files (mainly I/O problems)
	 */
	private static Set<IndividuoTSP> caricaPop(String logdirectory) throws Exception {

		// leggo il file popolazioni
		File f = new File(logdirectory + "popolazioni.txt");

		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String popn;
		String popn1;
		// leggo righe fino alla fine del file
		popn = br.readLine();
		while ((popn1 = br.readLine()) != null) {
			popn = popn1;
		}
		// ho trovato l'ultima riga
		br.close();
		Scanner scanner;
		HashSet<IndividuoTSP> popolazione = new HashSet<IndividuoTSP>();

		scanner = new Scanner(popn);

		// leggo il numero della poplazione
		thisgen = scanner.nextInt();

		// scandisco il file instances per trovare l'ultimo id usato
		FileReader fins = new FileReader(logdirectory + "instances.txt");
		BufferedReader brins = new BufferedReader(fins);
		String inst;
		int lid = 0;
		while ((inst = brins.readLine()) != null) {
			Scanner s = new Scanner(inst);
			int tinst = s.nextInt();
			if (tinst > lid) {
				lid = tinst;
			}
			s.close();
		}
		brins.close();
		// imposto il prossimo id da usare
		IndividuoTSP.setNextId(lid + 1);

		// leggo i diversi ID e li salvo
		while (scanner.hasNextInt()) {
			Integer indnumber = scanner.nextInt();

			// carico i problemi corrispondenti agli id
			File fi = new File(logdirectory + indnumber + ".txt");
			IndividuoTSP ind = loadIndividuo(fi);
			// inserisco i problemi nella popolazione

			popolazione.add(ind);
		}
		scanner.close();
		return popolazione;
	}

	/**
	 * Load a single instance
	 * 
	 * @param f
	 *            The file of the instance
	 * @return The instance loaded
	 * @throws Exception
	 *             If there are problems with the file (file not presents, wrong
	 *             format ecc)
	 */
	private static IndividuoTSP loadIndividuo(File f) throws Exception {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);

		int id = Integer.parseInt(br.readLine());
		// int size = 30;
		int size = Integer.parseInt(br.readLine());
		int gen = Integer.parseInt(br.readLine());
		// double score = Double.parseDouble(br.readLine());
		int score = Integer.parseInt(br.readLine());

		int[][] matrix = new int[size][size];

		Scanner scanner = new Scanner(br);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrix[i][j] = scanner.nextInt();
			}
		}
		scanner.close();

		IndividuoTSP ind = new IndividuoTSP(id, size, matrix, gen);
		ind.getScores().put("fitness", (double) score);
		return ind;
	}

}
