import java.util.Arrays;

public class Jogador {

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

        int[] peca = escolherPeca(jogador);
        int[][] jogada = escolherJogada(peca, jogador);

        Replays.gravarJogada(jogada);
        Jogo.realizarJogada(jogada, jogador);

        while (ehJogadaObrigatoria) {
            peca = jogada[1];
            int[][][] jogadasDeCapturaMultipla = Analises.obterJogadasDeCapturaMultipla(jogador, peca);
            if (jogadasDeCapturaMultipla.length > 0) {
            jogada = escolherJogada(peca, jogador);
                Replays.gravarJogada(jogada);
                Jogo.realizarJogada(jogada, jogador);
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
        Tabuleiros.criarTabuleiroHipotetico();

        int[][][] jogadasPossiveis = Analises.obterJogadasPossiveis(peca, jogador);

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

    public static void mostrarPecasObrigatorias(int[][] pecasObrigatorias) {
        System.out.println("Você tem jogadas obrigatórias nesse turno! Você deve comer uma peça inimiga!");
        System.out.print("Coordenadas das peças que você pode escolher: ");
        for (int i = 0; i < pecasObrigatorias.length; i++) {
            String coordenadaTexto = Analises.obterCoordenadaEmTexto(pecasObrigatorias[i]);
            System.out.printf("%s ", coordenadaTexto);
        }
        System.out.println();
    }

    public static int[] escolherPecaObrigatoria(int[][] pecasObrigatorias) {
        // Recebemos um int[][] com a listagem de peças que o jogador é obrigado a receber. Aqui, ele precisa escolher uma delas
        System.out.println("Escolha sua peça: ");
        int[] escolha;
        while (true) {
            escolha = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < pecasObrigatorias.length; i++) {
                if (Arrays.equals(pecasObrigatorias[i], escolha)) {
                    return escolha;
                }
            }
            System.out.println("Peça inválida! Você é obrigado a comer uma peça inimiga nesse round!");
        }
    }

    public static int[] escolherJogadaObrigatoria(int[][] jogadasObrigatorias) {
        System.out.print("Você pode jogar nessas coordenadas com essa peça: ");
        for (int i = 0; i < jogadasObrigatorias.length; i++) {
            System.out.printf("%s ", Analises.obterCoordenadaEmTexto(jogadasObrigatorias[i]));
        }
        System.out.println();
        System.out.println("Escolha: ");
        while (true) {
            int[] coordenadasEscolhida = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < jogadasObrigatorias.length; i++) {
                if (!Arrays.equals(coordenadasEscolhida, jogadasObrigatorias[i])) {
                    System.out.println("Coordenada inválida! Você precisa comer uma peça do inimigo!");
                }
            }
            return coordenadasEscolhida;
        }
    }
}