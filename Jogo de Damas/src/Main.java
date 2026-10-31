import java.util.Random;

public class Main {
    static Random random = new Random();
    static boolean debugging = false;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            int escolha = mostrarMenu();
            switch (escolha) {
                case 1:
                    iniciarPartida("multiplayer");
                    break;

                case 2:
                    iniciarPartida("singleplayer");
                    break;

                case 3:
                    System.out.println("Saindo . . .");
                    System.exit(0);

                case 0:
                    debugging = !debugging;
                    break;

                default:
                    System.out.println("Erro 6");
                    System.exit(6);
            }
        }
    }

    public static void iniciarPartida(String modo) throws InterruptedException {
        char[][] tabuleiro = Tabuleiros.iniciarTabuleiro();
        int jogadorAtual = random.nextInt(1, 3);
        boolean partidaTerminou = false;

        do {
            char simboloJogadorAtual = ' ';
            switch (jogadorAtual) {
                case 1:
                    simboloJogadorAtual = '□';
                    tabuleiro = Jogo.jogadaJogador(tabuleiro, simboloJogadorAtual);
                    jogadorAtual++;
                    break;

                case 2:
                    simboloJogadorAtual = '○';
                    if (modo.contentEquals("multiplayer")) {
                        tabuleiro = Jogo.jogadaJogador(tabuleiro, simboloJogadorAtual);
                    }
                    else {
                        tabuleiro = IA.fazerJogadaIA(tabuleiro, simboloJogadorAtual);
                    }
                    jogadorAtual--;
                    break;

                default:
                    System.out.println("Erro 1");
                    System.exit(1);
            }
            partidaTerminou = Jogo.verificarSePartidaTerminou(tabuleiro, simboloJogadorAtual);
        } while (!partidaTerminou);
    }

    public static String obterCoordenadaEmTexto(int[] coordenadas) {
        char[] coordenadaUmPossiveis = {'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] coordenadaDoisPossiveis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int coordenadaUm = coordenadas[0];
        int coordenadaDois = coordenadas[1];
        return ("" + coordenadaUmPossiveis[coordenadaUm] + coordenadaDoisPossiveis[coordenadaDois]);
    }

    public static boolean continuarJogando() {
        System.out.println("Deseja jogar mais?");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");
        int escolha = ValidadorInput.obterInt(1, 2);
        return escolha == 1;
    }

    public static int mostrarMenu() {
        System.out.println("====== JOGO DE DAMAS ======");
            System.out.println("1 - Jogar contra uma Pessoa");
            System.out.println("2 - Jogar contra uma IA");
            System.out.println("3 - Sair");
            return ValidadorInput.obterInt(0, 3);
    }
}