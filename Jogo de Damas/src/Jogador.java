import java.util.Arrays;

public class Jogador {

    static boolean debugging = false;
    static int[][][][] todasAsJogadasPossiveis;

    // È atualizado pelo Analises.obterTodasAsJogadasPossiveis
    static boolean ehJogadaObrigatoria = false;

    public static void jogadaJogador(char jogador) {
        Tabuleiros.mostrarTabuleiro();

        if (jogador == '□') {
            System.out.println("Agora é a vez do jogador UM! - □");
        } else {
            System.out.println("Agora é a vez do jogador DOIS! - ○");
        }

        todasAsJogadasPossiveis = Analises.obterTodasAsJogadasPossiveis(jogador);

        if (todasAsJogadasPossiveis.length == 0) {
            if (Main.modoDeJogo.equals("singleplayer")) {
                Jogo.quemGanhou = "CO";
            }
            else {
                if (jogador == '□') {
                    Jogo.quemGanhou = "J2";
                }
                else {
                    Jogo.quemGanhou = "J1";
                }
            }
            return;
        }

        if (ehJogadaObrigatoria) {
            System.out.printf("(Jogador %s) - Você possui jogadas obrigatórias!\n", jogador);
            System.out.print("Coordenadas de peças que você pode escolher: ");
            for (int i = 0; i < todasAsJogadasPossiveis.length; i++) {
                System.out.printf("%s ", Analises.obterCoordenadaEmTexto(todasAsJogadasPossiveis[i][0][0]));
            }
            System.out.println();
        }
        int[] peca = escolherPeca(jogador);
        int[][] jogada = escolherJogada(peca, jogador);

        Replays.gravarJogada(jogada);
        Jogo.realizarJogada(jogada, jogador);

        while (ehJogadaObrigatoria) {
            peca = jogada[1];
            int[][][] jogadasDeCapturaMultipla = Analises.obterJogadasDeCapturaMultipla(jogador, peca);
            if (jogadasDeCapturaMultipla.length > 0) {
                System.out.printf("(Jogador %s) - Captura múltipla! Você pode capturar mais peças!\n", jogador);
            jogada = escolherJogada(peca, jogador, jogadasDeCapturaMultipla);
                Replays.gravarJogada(jogada);
                Jogo.realizarJogada(jogada, jogador);
            }
            else {
                ehJogadaObrigatoria = false;
            }
        }
    }

    public static int[] escolherPeca(char jogador) {
        // Aqui o jogador seleciona a peça para fazer sua jogada.
        // Como regra de jogo, no momento que ele pega a peça, não poderá devolve-la. Será obrigado a jogar
        System.out.printf("(Vez do %s) Digite as coordenadas da peça que você vai jogar: ", jogador);
        while (true) {
            int[] coordenadas = ValidadorInput.obterCoordenadas();
            if (Analises.coordenadasEscolhidasEhValida(coordenadas, jogador)) {
                return coordenadas;
            } else {
                System.out.println("Você não tem uma peça com jogadas válidas nessas coordenadas!");
            }
        }
    }

    public static int[][] escolherJogada(int[] peca, char jogador) {
        // Recebemos a coordenada da peça selecionada
        // Objetivo: Mostrar a peça selecionada no tabuleiro de forma clara, e mostrar as jogadas possíveis dessa peça
        // No final, obtém a jogada int[][] a ser feita e a retorna
        int[][][] jogadasPossiveis = Analises.obterJogadasPossiveis(peca, jogador);
        return escolherJogada(peca, jogador, jogadasPossiveis);
    }

    public static int[][] escolherJogada(int[] peca, char jogador, int[][][] jogadasPossiveis) {
        // Mesmo metodo que o de acima, porém recebe já o array de jogadas pré-definido
        Tabuleiros.criarTabuleiroHipotetico();
        Jogo.inserirJogadasPossiveis(jogadasPossiveis, peca, jogador);
        Tabuleiros.mostrarTabuleiroHipotetico();
        mostrarCoordenadasJogaveis(jogadasPossiveis);

        return obterJogada(jogadasPossiveis);
    }

    public static void mostrarCoordenadasJogaveis(int[][][] jogadasPossiveis) {
        System.out.print("Coordenadas que você pode jogar com essa peça: ");
        for (int i = 0; i < jogadasPossiveis.length; i++) {
            System.out.printf("%s ", Analises.obterCoordenadaEmTexto(jogadasPossiveis[i][1]));
        }
        System.out.println();
    }

    public static int[][] obterJogada(int[][][] jogadasPossiveis) {
        // Aqui o jogador seleciona a jogada à realizar
        int[] coordenada;
        while (true) {
            System.out.print("Escolha a sua jogada: ");
            coordenada = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < jogadasPossiveis.length; i++) {
                if (Arrays.equals(jogadasPossiveis[i][1], coordenada)) {
                    return jogadasPossiveis[i];
                }
            }
            System.out.println("Coordenada inválida! Selecione uma coordenada válida para jogar!");
        }
    }
}