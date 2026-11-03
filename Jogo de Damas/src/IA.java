import java.util.ArrayList;
import java.util.Random;

public class IA {
    static Random random = new Random();
    static boolean debugging = false;

    static int[][][][] todasAsJogadasPossiveis;

    // È atualizado pelo Analises.obterTodasAsJogadasPossiveis
    static boolean ehJogadaObrigatoria = false;

    static int[] coordenadaDaUltimaJogada = new int[2];
    static int dificuldade = 2;

    public static void jogadaIA(char jogador) throws InterruptedException {
        todasAsJogadasPossiveis = Analises.obterTodasAsJogadasPossiveis(jogador);
        if (todasAsJogadasPossiveis.length == 0) {
            Jogo.quemGanhou = "JO";
            System.out.println("Você ganhou! Pressione enter para voltar ao Menu Principal. . .");
            ValidadorInput.scanner.nextLine();
            return;
        }

        System.out.println("Agora é a vez da IA!");
        System.out.println("(IA) Pensando . . .");
        Thread.sleep(1000);

        // Metodo experimental de IA
        int[][] jogada = obterJogadaInteligente(dificuldade);
        int[] peca = jogada[0];
        coordenadaDaUltimaJogada = jogada[1];

        Replays.gravarJogada(jogada);
        Jogo.realizarJogada(jogada, jogador);
        if (jogada.length == 2) {
            System.out.printf("(IA) Eu joguei a peça %s para %s!\n",
                    Analises.obterCoordenadaEmTexto(jogada[0]), Analises.obterCoordenadaEmTexto(jogada[1]));
        }
        else {
            System.out.printf("(IA) Eu joguei a peça %s para %s e capturei a peça %s!\n",
                    Analises.obterCoordenadaEmTexto(jogada[0]),
                    Analises.obterCoordenadaEmTexto(jogada[1]),
                    Analises.obterCoordenadaEmTexto(jogada[2]));
        }

        // Lógica de capturas múltiplas ainda pode ser melhorada
        while (ehJogadaObrigatoria) {
            peca = jogada[1];
            int[][][] jogadasDeCapturaMultipla = Analises.obterJogadasDeCapturaMultipla(jogador, peca);
            if (jogadasDeCapturaMultipla.length > 0) {
                Tabuleiros.mostrarTabuleiro();
                System.out.println("(IA) Eu posso capturar mais peças! Pensando . . .");
                Thread.sleep(1000);
                int jogadaEscolhida = random.nextInt(0, jogadasDeCapturaMultipla.length);
                jogada = jogadasDeCapturaMultipla[jogadaEscolhida];
                System.out.printf("Eu capturei a peça %s!\n", Analises.obterCoordenadaEmTexto(jogada[2]));
                coordenadaDaUltimaJogada = jogada[1];
                Replays.gravarJogada(jogada);
                Jogo.realizarJogada(jogada, jogador);
            }
            else {
                ehJogadaObrigatoria = false;
            }
        }
    }

    public static int[][] obterJogadaInteligente(int dificuldade) {
        // Objetivo:
        // 1 - Prever n (n = dificuldade) turnos no futuro
        // 2 - Simular todas as jogadas possíveis para estes turnos, tanto para a IA quanto para o jogador
        // 3 - Realizar o cálculo valorJogada = pecasComidas - pecasPerdidas
        // 4 - Obter todas as jogadas que tenham o valor máximo possível desse valorJogada
        // 5 - Escolher aleatoriamente uma jogada dentre estas com valor máximo, e retorna-la
        int[][][][] todasAsJogadas = todasAsJogadasPossiveis;

        // A IA sempre será o ○
        int valorMaximo = Integer.MIN_VALUE;

        ArrayList<int[][]> jogadasDeMaiorValor = new ArrayList<>();
        for (int peca = 0; peca < todasAsJogadas.length; peca++) {
            for (int jogada = 0; jogada < todasAsJogadas[peca].length; jogada++) {
                int valorDestaJogada = obterValorDestaJogada(todasAsJogadas[peca][jogada], dificuldade, '○', Tabuleiros.tabuleiro);
                if (valorDestaJogada > valorMaximo) {
                    valorMaximo = valorDestaJogada;
                    if (Main.debugging || debugging) {
                        System.out.printf("Achei o maior valor %d para a jogada %s a %s\n", valorMaximo,
                                Analises.obterCoordenadaEmTexto(todasAsJogadas[peca][jogada][0]),
                                Analises.obterCoordenadaEmTexto(todasAsJogadas[peca][jogada][1]));
                    }
                    jogadasDeMaiorValor.clear();
                    jogadasDeMaiorValor.add(todasAsJogadas[peca][jogada].clone());
                }
                else if (valorDestaJogada == valorMaximo) {
                    if (Main.debugging || debugging) {
                        System.out.printf("(IA) Dei o valor de %d para a jogada %s a %s\n", valorMaximo,
                                Analises.obterCoordenadaEmTexto(todasAsJogadas[peca][jogada][0]),
                                Analises.obterCoordenadaEmTexto(todasAsJogadas[peca][jogada][1]));
                    }
                    jogadasDeMaiorValor.add(todasAsJogadas[peca][jogada].clone());
                }
            }
        }
        int jogadaEscolhida = random.nextInt(0, jogadasDeMaiorValor.size());
        return jogadasDeMaiorValor.get(jogadaEscolhida);
    }

    public static int obterValorDestaJogada(int[][] jogada, int dificuldade, char jogadorAtual, char[][] simulacaoTabuleiro) {
        char[][] simulacaoDesteTabuleiro = Tabuleiros.copiarTabuleiro(simulacaoTabuleiro);
        Jogo.realizarJogada(jogada, jogadorAtual, simulacaoDesteTabuleiro);

        // Quando terminarmos a análise, retornamos 0 caso a jogada atual não capture, ou 1 caso capture
        if (dificuldade <= 0) {
            if (jogada.length == 2) {
                return 0;
            }
            else {
                return 1;
            }
        }

        int valorJogada = 0;
        char jogadorOposto = Analises.obterOponente(jogadorAtual);

        // Jogadas com tamanho 3 são jogadas que comem uma peça inimiga
        if (jogada.length == 3) {
            int[][][] jogadasDeCapturaMultiplas = Analises.obterJogadasDeCapturaMultipla(jogadorAtual, jogada[1], simulacaoDesteTabuleiro);
            if (jogadasDeCapturaMultiplas.length > 0) {
                int valorMaximo = Integer.MIN_VALUE;
                for (int i = 0; i < jogadasDeCapturaMultiplas.length; i++) {
                    int valorDestaJogada = obterValorDestaJogada(jogadasDeCapturaMultiplas[i], dificuldade, jogadorAtual, simulacaoDesteTabuleiro);
                    if (valorDestaJogada > valorMaximo) {
                        valorMaximo = valorDestaJogada;
                    }
                }
                if (valorMaximo > valorJogada) {
                    valorJogada = valorMaximo;
                }
                return valorJogada + 1;
            }
            valorJogada++;
        }

        // Quando não haverem mais jogadas de captura múltiplas a realizar, trocamos o jogador e analisamos a jogada do outro
        int[][][][] todasAsJogadasPossiveisDoOponente = Analises.obterTodasAsJogadasPossiveis(jogadorOposto, simulacaoDesteTabuleiro);
        // Caso o oponente não tenha jogadas possíveis, damos a esta jogada um valor de 100
        if (todasAsJogadasPossiveisDoOponente.length == 0) {
            return 100;
        }
        int valorJogadaDoOponente = 0;

        for (int peca = 0; peca < todasAsJogadasPossiveisDoOponente.length; peca++) {
            for (int jogadaOponente = 0; jogadaOponente < todasAsJogadasPossiveisDoOponente[peca].length; jogadaOponente++) {
                int valorDestaJogada = obterValorDestaJogada(todasAsJogadasPossiveisDoOponente[peca][jogadaOponente], dificuldade - 1, jogadorOposto, simulacaoDesteTabuleiro);
                if (valorDestaJogada > valorJogadaDoOponente) {
                    valorJogadaDoOponente = valorDestaJogada;
                }
            }
        }
        return valorJogada - valorJogadaDoOponente;
    }
}