package com.github.vitucomment.batalhaNaval;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

	private static int qtd = 26;

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		limpaConsole();
		imprimeMenuInicial();
		qtd = escolhaDeDificuldade(input);

		input = new Scanner(System.in);

		String[][] boardJogador = new String[qtd][qtd];
		String[][] boardNpc = new String[qtd][qtd];
		String[][] boardNpcHide = new String[qtd][qtd];

		boardJogador = preencheBoard(boardJogador, ".");
		boardNpc = preencheBoard(boardNpc, ".");
		boardNpcHide = preencheBoard(boardNpcHide, ".");
		boardNpcHide = preencheBoardNpc(boardNpcHide);
		preencheBoardComJogadas(boardJogador, boardNpc, input);
		limpaEDesenha(boardJogador, boardNpc);
		imprimeRegrasDoJogo();

		while (true) {
			
			// BLOCO QUE PEGA ESCOLHAS DO JOGADOR
			input = new Scanner(System.in);
			String[] escolha = capturaEscolha(input);
			Integer L = retornaLinha(escolha);
			Integer C = retornaColuna(escolha);

			while (true) {
				while (!posicaoJaFoiEscolhida(boardNpcHide, L, C)) {
					System.out.println("ESSA POSICAO JA FOI ESCOLHA");
					escolha = capturaEscolha(input);
					L = retornaLinha(escolha);
					C = retornaColuna(escolha);
				}

				if (!posicaoDoJogadorEhValida(boardNpcHide, L, C)) {
					boardNpc[L - 1][C - 1] = "#";
					boardNpcHide[L - 1][C - 1] = "#";
					limpaEDesenha(boardJogador, boardNpc);

					System.out.println("VOCE AFUNDOU OU ACERTOU PARTE DE UMA EMBARCACAO!");
					new Thread().sleep(3500);
					if (verificaSeHaVencedor(boardNpcHide)) {
						break;
					}
					escolha = capturaEscolha(input);
					L = retornaLinha(escolha);
					C = retornaColuna(escolha);
				} else {
					boardNpc[L - 1][C - 1] = "*";
					boardNpcHide[L - 1][C - 1] = "*";
					System.out.println("TIRO NA AGUA");
					new Thread().sleep(2500);
					limpaEDesenha(boardJogador, boardNpc);

					break;
				}

			}
			if (verificaSeHaVencedor(boardNpcHide)) {
				System.out.println("VOCE AFUNDOU TODAS AS EMBARCACOES INIMIGAS");
				System.out.println("O JOGADOR É O VENCEDOR!!!");
				break;
			}

			//BLOCO QUE GERA AS ESCOLHAS DO COMPUTADOR.
			Integer npcL = aleatorio();
			Integer npcC = aleatorio();
			while (true) {
				while (!posicaoJaFoiEscolhida(boardJogador, npcL, npcC)) {
					npcL = aleatorio();
					npcC = aleatorio();
				}
				
				System.out.println("O computador jogou na posicao: [LINHA: " + npcL + "] [COLUNA: " + npcC + "]");
				new Thread().sleep(2500);
				if (!posicaoDoJogadorEhValida(boardJogador, npcL, npcC)) {
					boardJogador[npcL - 1][npcC - 1] = "#";
					System.out.println("O COMPUTADOR AFUNDOU OU ACERTOU PARTE DE UMA EMBARCACAO!");
					new Thread().sleep(2500);
					limpaEDesenha(boardJogador, boardNpc);
					if (verificaSeHaVencedor(boardJogador)) {
						break;
					}
					npcL = aleatorio();
					npcC = aleatorio();
				} else {
					boardJogador[npcL - 1][npcC - 1] = "*";
					break;
				}
			}
			if (verificaSeHaVencedor(boardJogador)) {
				System.out.println("O COMPUTADOR AFUNDOU TODAS AS SUAS EMBARCACOES");
				System.out.println("O JOGADOR PERDEU!!!");
				break;
			}
			limpaEDesenha(boardJogador, boardNpc);
		}
	}

	private static boolean verificaSeHaVencedor(String[][] board) {
		for (int L = 0; L < qtd; L++) {
			for (int C = 0; C < qtd; C++) {
				if (board[L][C].equalsIgnoreCase("x")) {
					return false;
				}
			}
		}
		return true;
	}

	private static Boolean posicaoJaFoiEscolhida(String[][] board, int L, int C) {
		return (board[L - 1][C - 1].equalsIgnoreCase("*") || board[L - 1][C - 1].equalsIgnoreCase("#")) ? false : true;
	}

	private static Boolean posicaoDoJogadorEhValida(String[][] board, int L, int C) {
		return (board[L - 1][C - 1].equalsIgnoreCase("x")) ? false : true;
	}

	private static Integer retornaLinha(String[] escolha) {
		Integer L = Integer.parseInt(escolha[0]);
		return L;
	}

	private static Integer retornaColuna(String[] escolha) {
		String letra = escolha[1].toUpperCase();
		String letras = ("-ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		Integer escolhaEmNumero = letras.indexOf(letra);
		return escolhaEmNumero;
	}

	private static String[] capturaEscolha(Scanner input) {
		System.out.print("Digite um NUMERO para [LINHA] e uma LETRA para [COLUNA] separados por espacos: ");
		String escolhaInput = input.nextLine();
		String[] escolha = escolhaInput.split(" ");

		try {
			while (!ehNumero(escolha[0]) || ehNumero(escolha[1])) {
				if (!ehNumero(escolha[0])) {
					System.out.println("VOCE NAO DIGITOU UM NUMERO VALIDO.");
					System.out.print("Digite um NUMERO para [LINHA] e uma LETRA para [COLUNA] separados por espacos: ");
					escolhaInput = input.nextLine();
					escolha = escolhaInput.split(" ");
				}
				if (ehNumero(escolha[1])) {
					System.out.println("VOCE NAO DIGITOU UMA LETRA VALIDA.");
					System.out.print("Digite um NUMERO para [LINHA] e uma LETRA para [COLUNA] separados por espacos: ");
					escolhaInput = input.nextLine();
					escolha = escolhaInput.split(" ");
				}
			}
		} catch (Exception ex) {
			capturaEscolha(input);
		}
		return escolha;

	}

	private static boolean ehNumero(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static void limpaEDesenha(String[][] boardJogador, String[][] boardNpc) {
		limpaConsole();
		desenhaBoard(boardJogador, boardNpc);
	}

	private static void preencheBoardComJogadas(String[][] boardJogador, String[][] boardNpc, Scanner input) {
		int qtdEscolhas = qtdDeEscolhasPorBoard();

		for (int i = 1; i <= qtdEscolhas; i++) {
			limpaEDesenha(boardJogador, boardNpc);
			System.out.println("Voce marcara agora " + qtdEscolhas + " barcos para iniciarmos o jogo!");
			String[] escolha = capturaEscolha(input);

			Integer L = retornaLinha(escolha);
			Integer C = retornaColuna(escolha);

			boolean ehValido = posicaoDoJogadorEhValida(boardJogador, L, C);
			while (ehValido == false) {
				limpaEDesenha(boardJogador, boardNpc);
				System.out.println("[ERRO] Esta posicao ja foi jogada anteriormente.");
				System.out.println("Voce marcara agora 10 barcos para iniciarmos o jogo!");
				escolha = capturaEscolha(input);
				L = retornaLinha(escolha);
				C = retornaColuna(escolha);
				L = Integer.parseInt(escolha[0]);
				ehValido = posicaoDoJogadorEhValida(boardJogador, L, C);
			}
			boardJogador[L - 1][C - 1] = "x";
		}
		limpaEDesenha(boardJogador, boardNpc);
	}

	private static int qtdDeEscolhasPorBoard() {
		int qtdEscolhas = 0;
		if (qtd == 9) {
			qtdEscolhas = 10;
		} else if (qtd == 18) {
			qtdEscolhas = 20;
		} else if (qtd == 26) {
			qtdEscolhas = 30;
		}
		return qtdEscolhas;
	}

	private static String[][] preencheBoardNpc(String[][] boardNpcHide) {
		int qtdEscolhas = qtdDeEscolhasPorBoard();
		for (int i = 0; i < qtdEscolhas; i++) {
			int rand1 = aleatorio();
			int rand2 = aleatorio();
			boolean ehValido = posicaoDoJogadorEhValida(boardNpcHide, rand1, rand2);
			while (ehValido == false) {
				rand1 = aleatorio();
				rand2 = aleatorio();
				ehValido = posicaoDoJogadorEhValida(boardNpcHide, rand1, rand2);
			}
			boardNpcHide[rand1 - 1][rand2 - 1] = "x";
		}
		return boardNpcHide;
	}

	private static String[][] preencheBoard(String[][] board, String x) {
		for (int i = 0; i < qtd; i++) {
			for (int j = 0; j < qtd; j++) {
				board[i][j] = x + "";
			}
		}
		return board;
	}

	private static void desenhaBoard(String[][] boardJogador, String[][] boardNpc) {
		if (qtd == 9) {
			System.out.println("   \tTabuleiro do jogador \t\t\tTabuleiro do Computador\n");
		} else if (qtd == 18) {
			System.out.println("   \t\tTabuleiro do jogador \t\t\t\t\t\t Tabuleiro do Computador\n");
		} else if (qtd == 26) {
			System.out.println("   \t\t\tTabuleiro do jogador \t\t\t\t\t\t\t\t Tabuleiro do Computador\n");
		}

		cabecalhoBoardCima();
		divisaoBoardCima();
		for (int L = 0; L < qtd; L++) {
			imprimeBoardJogador(boardJogador, L);
			System.out.print("\t\t    ");
			imprimeBoardNpc(boardNpc, L);
		}
		divisaoBoardBaixo();
	}

	private static void imprimeBoardJogador(String[][] boardJogador, int L) {
		System.out.print(" " + (L + 1) + "\t| ");
		for (int C = 0; C < qtd; C++) {
			System.out.print(boardJogador[L][C] + " ");
		}
		System.out.print("| ");
	}

	private static void imprimeBoardNpc(String[][] boardNpc, int L) {
		System.out.print(L + 1 + "\t| ");
		for (int C = 0; C < qtd; C++) {
			System.out.print(boardNpc[L][C] + " ");
		}
		System.out.println("| ");
	}

	private static void divisaoBoardCima() {
		System.out.print("  \t+");
		for (int l = 0; l < (qtd * 2) + 1; l++) {
			System.out.print("-");
		}
		System.out.print("+");
		if (qtd == 9) {
			System.out.print("\t\t\t");
		} else if (qtd == 18) {
			System.out.print("\t\t\t\t");
		} else if (qtd == 26) {
			System.out.print("\t\t\t\t");

		}

		System.out.print("+");
		for (int l = 0; l < (qtd * 2) + 1; l++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

	private static void divisaoBoardBaixo() {
		System.out.print("  \t+");
		for (int l = 0; l < (qtd * 2) + 1; l++) {
			System.out.print("-");
		}
		System.out.print("+");

		if (qtd == 9) {
			System.out.print("\t\t\t+");
		} else if (qtd == 18) {
			System.out.print("\t\t\t\t+");
		} else if (qtd == 26) {
			System.out.print("\t\t\t\t+");

		}

		for (int l = 0; l < (qtd * 2) + 1; l++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

	private static void cabecalhoBoardCima() {
		String letras = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

		if (qtd == 9) {
			System.out.print("\t  ");
		} else if (qtd == 18) {
			System.out.print("\t  ");
		} else if (qtd == 26) {
			System.out.print("\t  ");
		}

		for (int l = 0; l < qtd; l++) {
			System.out.print(letras.charAt(l) + " ");
		}

		if (qtd == 9) {
			System.out.print("\t\t\t  ");
		} else if (qtd == 18) {
			System.out.print("\t\t\t\t  ");
		} else if (qtd == 26) {
			System.out.print("\t\t\t\t  ");
		}

		for (int l = 0; l < qtd; l++) {
			System.out.print(letras.charAt(l) + " ");
		}
		System.out.println();

	}

	private static void imprimeRegrasDoJogo() {
		bordaRegraDoJogo();
		System.out.println("O X representa o posicionamento dos barcos.");
		System.out.println("O * representa um tiro na agua.");
		System.out.println("O # representa um tiro que acertou o barco do oponente.\n");

	}

	private static void bordaRegraDoJogo() {
		System.out.println("\n");
		for (int i = 0; i <= 30; i++) {
			System.out.print("=");
		}
		System.out.print(" Regras do Jogo ");
		for (int i = 0; i <= 30; i++) {
			System.out.print("=");
		}
		System.out.println("\n");
	}

	private static void imprimeMenuInicial() {
		System.out.println("\n");
		for (int i = 0; i <= 30; i++) {
			System.out.print("=");
		}
		System.out.print("BEM VINDO AO JOGO DA BATALHA NAVAL");
		for (int i = 0; i <= 30; i++) {
			System.out.print("=");
		}

		System.out.println("\nESCOLHA O TAMANHO DO TABULEIRO:");
		System.out.println("1 - 9x9   = (10 EMARCARCOES)");
		System.out.println("2 - 18x18 = (20 EMARCARCOES)");
		System.out.println("3 - 26x26 = (30 EMARCARCOES)");
		System.out.println("\nSUA ESCOLHA: ");

	}

	private static int escolhaDeDificuldade(Scanner input) {
		int escolha = input.nextInt();
		while (escolha != 1 && escolha != 2 && escolha != 3) {
			System.out.println("ESCOLHA ENTRE AS OPCOES 1 - 2 - 3: ");
			escolha = input.nextInt();
		}

		if (escolha == 1) {
			return 9;
		} else if (escolha == 2) {
			return 18;
		} else {
			return 26;
		}
	}

	public static void limpaConsole() {
		try {
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException ex) {
		}
	}

	public static int aleatorio() {
		Random aleatorio = new Random();
		int valor = aleatorio.nextInt(qtd) + 1;
		return valor;
	}
}