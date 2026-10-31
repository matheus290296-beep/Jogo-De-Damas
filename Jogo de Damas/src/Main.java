import java.io.IOException;
import java.util.Random;

// TO-DO: Classe Tabuleiros terá um static com o tabuleiro do jogo, e ela que passará a gerencia-lo, ao invés do Main

public class Main {
    static Random random = new Random();
    static boolean debugging = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        Arquivos.garantirIntegridade();
        int tabuleiroTipo = 0;

        while (true) {
            int escolha = mostrarMenu();
            switch (escolha) {
                case 1:
                    Replays.iniciarGravacao();
                    iniciarPartida("multiplayer", tabuleiroTipo);
                    break;

                case 2:
                    Replays.iniciarGravacao();
                    iniciarPartida("singleplayer", tabuleiroTipo);
                    break;

                case 3:
                    tabuleiroTipo = alterarTabuleiroInicial();
                    break;

                case 4:
                    Arquivos.mostrarPlacar();
                    break;

                case 5:
                    Replays.reproduzirReplay();
                    break;

                case 6:
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

    public static void iniciarPartida(String modo, int tabuleiroTipo) throws InterruptedException, IOException {
        char[][] tabuleiro = Tabuleiros.obterTabuleiro(tabuleiroTipo);
        int jogadorAtual = random.nextInt(1, 3);
        Replays.gravarQuemComecou(jogadorAtual, modo);
        String quemGanhou = " ";

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
            quemGanhou = Jogo.verificarSePartidaTerminou(tabuleiro, simboloJogadorAtual);
        } while (quemGanhou.contentEquals(" "));
        if (modo.contentEquals("multiplayer")) {
            Arquivos.atualizarPlacar(quemGanhou);
        }
        Replays.gravarQuemGanhou(quemGanhou, modo);
    }

    public static String obterCoordenadaEmTexto(int[] coordenadas) {
        char[] coordenadaUmPossiveis = {'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] coordenadaDoisPossiveis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int coordenadaUm = coordenadas[0];
        int coordenadaDois = coordenadas[1];
        return ("" + coordenadaUmPossiveis[coordenadaUm] + coordenadaDoisPossiveis[coordenadaDois]);
    }

    public static int mostrarMenu() {
        System.out.println("====== JOGO DE DAMAS ======");
            System.out.println("1 - Jogar contra uma Pessoa");
            System.out.println("2 - Jogar contra uma IA");
            System.out.println("3 - Escolher tabuleiro");
            System.out.println("4 - Mostrar Placar");
            System.out.println("5 - Reproduzir Replays");
            System.out.println("6 - Sair");
            return ValidadorInput.obterInt(0, 6);
    }

    public static int alterarTabuleiroInicial() {
        System.out.println("====== MENU DE ESCOLHA DE TABULEIROS =====");
        System.out.println("1 - Tabuleiro normal");
        System.out.println("2 - Tabuleiro só com damas");
        System.out.println("3 - Tabuleiro gerado aleatoriamente");
        System.out.println("4 - Tabuleiro gerado aleatoriamente (CAOS)");
        if (debugging) {
            System.out.println("\n=========== DEBUGGING ===========");
            System.out.println("5 - Tabuleiro de jogo normal, prestes à virar dama");
            System.out.println("6 - Tabuleiro prestes à comer várias peças");
            System.out.println("7 - Tabuleiro prestes à comer várias peças - versão dama");
            System.out.println("8 - Tabuleiro com alguem prestes a vencer");
            return ValidadorInput.obterInt(1, 8);
        }
        return ValidadorInput.obterInt(1, 4);
    }
}