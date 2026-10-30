import java.util.Arrays;
import java.util.ArrayList;

public class Jogo {
    public static char[][] jogadaJogador(char[][] tabuleiro, char simboloJogador) {
        Tabuleiros.mostrarTabuleiro(tabuleiro);
        char simboloAJogar;
        int[] coordenadaVelha = new int[2];
        int[] coordenadaNova = new int[2];

        if (simboloJogador == '□') {
            System.out.println("Agora é a vez do jogador UM! - □");
        } else {
            System.out.println("Agora é a vez do jogador DOIS! - ○");
        }

        int[][][] jogadasObrigatorias = obterJogadasObrigatorias(tabuleiro, simboloJogador);
        if (jogadasObrigatorias.length > 0) {
            boolean comeuPeca = true;
            int[][] jogadaObrigatoria = realizarJogadaObrigatoria(tabuleiro, simboloJogador, jogadasObrigatorias);
            coordenadaVelha = jogadaObrigatoria[0];
            coordenadaNova = jogadaObrigatoria[1];
            simboloAJogar = tabuleiro[coordenadaVelha[0]][coordenadaVelha[1]];
            tabuleiro = apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova, simboloJogador);
            inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);
            // Se uma peça que acabou de comer ainda pode comer mais, ela continua realizando jogadas obrigatórias em loop
            while (comeuPeca) {
                int[][] jogadasObrigatoriasNovas = jogadasObrigatoriasDaPeca(tabuleiro, simboloJogador, coordenadaNova);
                if (jogadasObrigatoriasNovas.length > 0) {
                    char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
                    char simboloPossivel = obterSimboloPossivel(simboloJogador);
                    tabuleiroHipotetico[coordenadaNova[0]][coordenadaNova[1]] = obterSimboloSelecionado(simboloJogador);
                    mostrarCoordenadasJogaveis(jogadasObrigatoriasNovas);
                    inserirJogadasPossiveis(tabuleiroHipotetico, jogadasObrigatoriasNovas, simboloPossivel);
                    Tabuleiros.mostrarTabuleiro(tabuleiroHipotetico);
                    coordenadaVelha = coordenadaNova.clone();
                    coordenadaNova = escolherJogadaObrigatoria(jogadasObrigatoriasNovas);
                    tabuleiro = apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova, simboloJogador);
                    inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);
                }
                else {
                    comeuPeca = false;
                }
            }
            return tabuleiro;
        }


        while (true) {
            coordenadaVelha = escolherPeca(tabuleiro, simboloJogador);
            if (pecaPossuiJogadasValidas(tabuleiro, coordenadaVelha, simboloJogador)) {
                break;
            } else {
                System.out.println("Essa peça que você escolheu não possui jogadas válidas! Escolha outra!");
            }
        }

        if (ehPecaNormal(tabuleiro, coordenadaVelha)) {
            if (Main.debugging) {
                System.out.printf("Peça nas coordenadas %d %d não é dama!\n", coordenadaVelha[0], coordenadaVelha[1]);
            }
            simboloAJogar = simboloJogador;
            coordenadaNova = escolherCoordenadaAJogar(tabuleiro, coordenadaVelha, simboloAJogar);
        } else {
            if (Main.debugging) {
                System.out.printf("Peça nas coordenadas %d %d é dama!\n", coordenadaVelha[0], coordenadaVelha[1]);
            }
            simboloAJogar = obterSimboloDama(simboloJogador);
            coordenadaNova = escolherCoordenadaAJogarDama(tabuleiro, coordenadaVelha, simboloAJogar);
        }
        if (comeuPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova)) {
            tabuleiro = apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova, simboloJogador);
        }

        return inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);
    }

    public static int[] escolherPeca(char[][] tabuleiro, char simboloJogador) {
        // Aqui o jogador seleciona a peça para fazer sua jogada.
        // Como regra de jogo, no momento que ele pega a peça, não poderá devolve-la. Será obrigado a jogar
        System.out.printf("(Vez do %s) Digite as coordenadas da peça que você vai jogar: ", simboloJogador);
        while (true) {
            int[] coordenadas = ValidadorInput.obterCoordenadas();
            if (coordenadaJogadorValida(tabuleiro, coordenadas, simboloJogador)) {
                return coordenadas;
            } else {
                System.out.println("Você não tem uma peça nessas coordenadas!");
            }
        }
    }

    public static boolean coordenadaJogadorValida(char[][] tabuleiro, int[] coordenadas, char simboloJogador) {
        // Verifica se a coordenada recebida pertence ao jogador que a selecionou
        char simboloDama = obterSimboloDama(simboloJogador);
        return (tabuleiro[coordenadas[0]][coordenadas[1]] == simboloJogador
                || tabuleiro[coordenadas[0]][coordenadas[1]] == simboloDama);
    }

    public static int[] escolherCoordenadaAJogar(char[][] tabuleiro, int[] coordenadasDaPecaSelecionada, char simboloJogador) {
        // Recebemos a coordenada da peça selecionada
        // Objetivo: Mostrar a peça selecionada no tabuleiro de forma clara, e mostrar as jogadas possíveis dessa peça
        // No final, obtém a coordenada a ser jogada e a retorna
        int coordenadaUm = coordenadasDaPecaSelecionada[0];
        int coordenadaDois = coordenadasDaPecaSelecionada[1];
        char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
        char simboloSelecionado = ' ';
        char simboloPossivel = ' ';
        if (simboloJogador == '□') {
            simboloSelecionado = '■';
            simboloPossivel = '⬚';
        } else if (simboloJogador == '○') {
            simboloSelecionado = '●';
            simboloPossivel = '◌';
        } else {
            System.out.println("Erro 4");
            System.exit(4);
        }

        int[][] jogadasPossiveis = obterJogadasPossiveis(tabuleiro, coordenadasDaPecaSelecionada, simboloJogador);

        inserirJogadasPossiveis(tabuleiroHipotetico, jogadasPossiveis, simboloPossivel);
        tabuleiroHipotetico[coordenadaUm][coordenadaDois] = simboloSelecionado;
        Tabuleiros.mostrarTabuleiro(tabuleiroHipotetico);
        mostrarCoordenadasJogaveis(jogadasPossiveis);

        return obterJogada(jogadasPossiveis);
    }

    public static char[][] inserirJogada(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova, char simboloAJogar, char simboloJogador) {
        int coordenadaVelhaUm = coordenadaVelha[0];
        int coordenadaVelhaDois = coordenadaVelha[1];
        int coordenadaNovaUm = coordenadaNova[0];
        int coordenavaNovaDois = coordenadaNova[1];

        if (Main.debugging) {
            System.out.printf("Simbolo a jogar: %s\n", simboloAJogar);
            System.out.printf("Coordenadas selecionadas: %d %d\n", coordenadaVelha[0], coordenadaVelha[1]);
            System.out.printf("Jogando coordenadas %d %d\n", coordenadaNovaUm, coordenavaNovaDois);
        }
        tabuleiro[coordenadaVelhaUm][coordenadaVelhaDois] = ' ';
        if (ehFinalDoTabuleiro(coordenadaNova, simboloJogador)) {
            System.out.printf("Peça nas coordenadas %d %d virou dama!\n", coordenadaNova[0], coordenadaNova[1]);
            tabuleiro = virarDama(tabuleiro, coordenadaNova, simboloAJogar);
            return tabuleiro;
        } else {
            tabuleiro[coordenadaNovaUm][coordenavaNovaDois] = simboloAJogar;
        }
        return tabuleiro;
    }

    public static boolean verificarSePartidaTerminou(char[][] tabuleiro) {
        // Verifica se um dos jogadores não tem mais peças no tabuleiro
        int pecasJogadorUm = 0;
        int pecasJogadorDois = 0;
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (tabuleiro[linha][coluna] == '□' || tabuleiro[linha][coluna] == '⊠') {
                    pecasJogadorUm++;
                } else if (tabuleiro[linha][coluna] == '○' || tabuleiro[linha][coluna] == '◓') {
                    pecasJogadorDois++;
                }
            }
        }
        return (pecasJogadorUm == 0 || pecasJogadorDois == 0);
    }

    public static char[][] copiarTabuleiro(char[][] tabuleiro) {
        char[][] tabuleiroHipotetico = new char[8][8];
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                tabuleiroHipotetico[linha][coluna] = tabuleiro[linha][coluna];
            }
        }
        return tabuleiroHipotetico;
    }

    public static int[][] obterJogadasPossiveis(char[][] tabuleiro, int[] coordenadasDaPecaSelecionada, char simboloJogador) {
        // Retorna as coordenadas das jogadas que são possiveis realizar, a partir da peça selecionada
        int coordenadaUm = coordenadasDaPecaSelecionada[0];
        int coordenadaDois = coordenadasDaPecaSelecionada[1];

        if (Main.debugging) {
            System.out.printf("Estou procurando jogadas possiveis nas coordenadas %d %d\n", coordenadaUm, coordenadaDois);
        }

        // Refatorando com Array<List:
        ArrayList<int[]> jogadasPossiveis = new ArrayList<>();
        int[] coordenadas = new int[2];

        // Primeiro, verificamos as casas vazias
        if (simboloJogador == '□') {
            if (coordenadaUm < 7 && coordenadaDois > 0
                    && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == ' ') {
                coordenadas[0] = coordenadaUm + 1;
                coordenadas[1] = coordenadaDois - 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
            if (coordenadaUm < 7 && coordenadaDois < 7
                    && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == ' ') {
                coordenadas[0] = coordenadaUm + 1;
                coordenadas[1] = coordenadaDois + 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
        } else if (simboloJogador == '○') {
            if (coordenadaUm > 0 && coordenadaDois > 0
                    && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == ' ') {
                coordenadas[0] = coordenadaUm - 1;
                coordenadas[1] = coordenadaDois - 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
            if (coordenadaUm > 0 && coordenadaDois < 7
                    && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == ' ') {
                coordenadas[0] = coordenadaUm - 1;
                coordenadas[1] = coordenadaDois + 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
        } else {
            System.out.println("Erro 5");
            System.exit(5);
        }

        // Agora, procura-se jogadas que possibilitam comer a peça do oponente
        if (simboloJogador == '□') {
            if (coordenadaUm < 6 && coordenadaDois > 1
                    && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == '○'
                    || coordenadaUm < 6 && coordenadaDois > 1 && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == '◓') {
                if (tabuleiro[coordenadaUm + 2][coordenadaDois - 2] == ' ') {
                    coordenadas[0] = coordenadaUm + 2;
                    coordenadas[1] = coordenadaDois - 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
            if (coordenadaUm < 6 && coordenadaDois < 6
                    && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == '○'
                    || coordenadaUm < 6 && coordenadaDois < 6 && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == '◓') {
                if (tabuleiro[coordenadaUm + 2][coordenadaDois + 2] == ' ') {
                    coordenadas[0] = coordenadaUm + 2;
                    coordenadas[1] = coordenadaDois + 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
        } else {
            if (coordenadaUm > 1 && coordenadaDois > 1
                    && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == '□'
                    || coordenadaUm > 1 && coordenadaDois > 1 && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == '⊠') {
                if (tabuleiro[coordenadaUm - 2][coordenadaDois - 2] == ' ') {
                    coordenadas[0] = coordenadaUm - 2;
                    coordenadas[1] = coordenadaDois - 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
            if (coordenadaUm > 1 && coordenadaDois < 6
                    && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == '□'
                    || coordenadaUm > 1 && coordenadaDois < 6 && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == '⊠') {
                if (tabuleiro[coordenadaUm - 2][coordenadaDois + 2] == ' ') {
                    coordenadas[0] = coordenadaUm - 2;
                    coordenadas[1] = coordenadaDois + 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
        }

        if (Main.debugging) {
            System.out.printf("Quantidade de jogadas possíveis encontradas da peça nas coordenadas %d %d: %d\n", coordenadaUm, coordenadaDois, jogadasPossiveis.size());
            System.out.printf("Essas são as coordenadas encontradas: %s\n", Arrays.deepToString(jogadasPossiveis.toArray()));
        }

        // Agora, transformamos o ArrayList em int[][] para retorna-lo
        int[][] jogadasPossiveisFormatada = new int[jogadasPossiveis.size()][2];
        ;
        for (int i = 0; i < jogadasPossiveis.size(); i++) {
            jogadasPossiveisFormatada[i] = jogadasPossiveis.get(i);
            if (Main.debugging) {
                System.out.printf("Jogada possível %d: %d %d\n", i, jogadasPossiveisFormatada[i][0], jogadasPossiveisFormatada[i][1]);
            }
        }
        return jogadasPossiveisFormatada;
    }

    public static boolean pecaPossuiJogadasValidas(char[][] tabuleiro, int[] coordenadaDaPecaEscolhida, char simboloJogador) {
        int[][] jogadasValidas;
        if (ehPecaNormal(tabuleiro, coordenadaDaPecaEscolhida)) {
            jogadasValidas = obterJogadasPossiveis(tabuleiro, coordenadaDaPecaEscolhida, simboloJogador);
        } else {
            jogadasValidas = obterJogadasPossiveisDama(tabuleiro, coordenadaDaPecaEscolhida, simboloJogador);
        }
        return jogadasValidas.length > 0;
    }

    public static void inserirJogadasPossiveis(char[][] tabuleiroHipotetico, int[][] jogadasPossiveis, char simboloPossivel) {
        // Insere as jogadas hipotéticas no tabuleiro hipotético
        int quantidadeJogadasPossiveis = jogadasPossiveis.length;

        for (int i = 0; i < quantidadeJogadasPossiveis; i++) {
            tabuleiroHipotetico[jogadasPossiveis[i][0]][jogadasPossiveis[i][1]] = simboloPossivel;
        }
    }

    public static void mostrarCoordenadasJogaveis(int[][] jogadasPossiveis) {
        System.out.print("Coordenadas que você pode jogar com essa peça: ");
        for (int i = 0; i < jogadasPossiveis.length; i++) {
            System.out.printf("%s ", Main.obterCoordenadaEmTexto(jogadasPossiveis[i]));
        }
        System.out.println();
    }

    public static int[] obterJogada(int[][] jogadasPossiveis) {
        // Aqui o jogador seleciona a coordenada que ele vai jogar
        int[] coordenada = new int[2];
        while (true) {
            System.out.print("Escolha a sua jogada: ");
            coordenada = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < jogadasPossiveis.length; i++) {
                if (Arrays.equals(jogadasPossiveis[i], coordenada)) {
                    return coordenada;
                }
            }
            System.out.println("Coordenada inválida! Selecione uma coordenada válida para jogar!");
        }
    }

    public static boolean comeuPecaInimigo(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova) {
        int coordenadaUm;
        int coordenadaDois;
        if (coordenadaVelha[0] > coordenadaNova[0]) {
            coordenadaUm = coordenadaNova[0] + 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            } else {
                coordenadaDois = coordenadaNova[1] - 1;

            }
        } else {
            coordenadaUm = coordenadaNova[0] - 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            } else {
                coordenadaDois = coordenadaNova[1] - 1;
            }
        }
        if (tabuleiro[coordenadaUm][coordenadaDois] != ' ') {
            return true;
        } else {
            return false;
        }
    }

    public static char[][] apagarPecaInimigo(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova, char simboloAtual) {
        // Precisa funcionar tanto para dama quanto para peça normal
        // Precisa funcionar mesmo se a dama andar várias casas após comer o oponente
        int coordenadaVelhaUm = coordenadaVelha[0];
        int coordenadaVelhaDois = coordenadaVelha[1];
        int coordenadaNovaUm = coordenadaNova[0];
        int coordenadaNovaDois = coordenadaNova[1];
        char simboloDamaAtual = obterSimboloDama(simboloAtual);
        char simboloDamaOposto = obterSimboloDamaOposto(simboloAtual);
        char simboloOposto = obterSimboloOposto(simboloAtual);
        int[] coordenadaInimigo = new int[2];

        // Precisamos testar para todas as diagonais, até encontrar onde a peça do inimigo está
        if (coordenadaVelhaUm > coordenadaNovaUm) {
            if (coordenadaVelhaDois > coordenadaNovaDois) {
                for (int i = 1; i < 7; i++) {
                    if (coordenadaVelhaUm - i < 0 || coordenadaVelhaDois - i < 0) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois - i] == simboloAtual
                    || tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois - i] == simboloDamaAtual) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois - i] == simboloOposto
                            || tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois - i] == simboloDamaOposto) {
                        coordenadaInimigo[0] = coordenadaVelhaUm - i;
                        coordenadaInimigo[1] = coordenadaVelhaDois - i;
                        break;
                    }
                }
            }
            // Para coordenadaVelhaUm > coordenadaNovaUm
            else {
                if (coordenadaVelhaDois < coordenadaNovaDois) {
                    for (int i = 1; i < 7; i++) {
                        if (coordenadaVelhaUm - i < 0 || coordenadaVelhaDois + i > 7) {
                            break;
                        }
                        if (tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois + i] == simboloAtual
                                || tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois + i] == simboloDamaAtual) {
                            break;
                        }
                        if (tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois + i] == simboloOposto
                                || tabuleiro[coordenadaVelhaUm - i][coordenadaVelhaDois + i] == simboloDamaOposto) {
                            coordenadaInimigo[0] = coordenadaVelhaUm - i;
                            coordenadaInimigo[1] = coordenadaVelhaDois + i;
                            break;
                        }
                    }
                }
            }
        }
        // Para coordenadaVelhaUm < coordenadaNovaUm
        else {
            if (coordenadaVelhaDois > coordenadaNovaDois) {
                for (int i = 1; i < 7; i++) {
                    if (coordenadaVelhaUm + i > 7 || coordenadaVelhaDois - i < 0) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois - i] == simboloAtual
                            || tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois - i] == simboloDamaAtual) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois - i] == simboloOposto
                            || tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois - i] == simboloDamaOposto) {
                        coordenadaInimigo[0] = coordenadaVelhaUm + i;
                        coordenadaInimigo[1] = coordenadaVelhaDois - i;
                        break;
                    }
                }
            }
            // Para coordenadaVelhaDois < coordenadaNovaDois
            else {
                for (int i = 1; i < 7; i++) {
                    if (coordenadaVelhaUm + i > 7 || coordenadaVelhaDois + i > 7) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois + i] == simboloAtual
                            || tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois + i] == simboloDamaAtual) {
                        break;
                    }
                    if (tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois + i] == simboloOposto
                            || tabuleiro[coordenadaVelhaUm + i][coordenadaVelhaDois + i] == simboloDamaOposto) {
                        coordenadaInimigo[0] = coordenadaVelhaUm + i;
                        coordenadaInimigo[1] = coordenadaVelhaDois + i;
                        break;
                    }
                }
            }
        }

        tabuleiro[coordenadaInimigo[0]][coordenadaInimigo[1]] = ' ';
        return tabuleiro;
    }

    public static boolean ehFinalDoTabuleiro(int[] coordenada, char simboloJogador) {
        if (simboloJogador == '□') {
            return coordenada[0] == 7;
        } else {
            return coordenada[0] == 0;
        }
    }

    public static char[][] virarDama(char[][] tabuleiro, int[] coordenada, char simboloJogador) {
        int coordenadaUm = coordenada[0];
        int coordenadaDois = coordenada[1];
        if (simboloJogador == '□') {
            tabuleiro[coordenadaUm][coordenadaDois] = '⊠';
        } else {
            tabuleiro[coordenadaUm][coordenadaDois] = '◓';
        }
        return tabuleiro;
    }

    public static boolean ehPecaNormal(char[][] tabuleiro, int[] coordenada) {
        // Verifica se a peça é normal (true) ou uma dama (false)
        int coordenadaUm = coordenada[0];
        int coordenadaDois = coordenada[1];

        return tabuleiro[coordenadaUm][coordenadaDois] != '⊠' && tabuleiro[coordenadaUm][coordenadaDois] != '◓';
    }

    public static int[] escolherCoordenadaAJogarDama(char[][] tabuleiro, int[] coordenadaVelha, char simboloJogador) {
        // Aqui o jogador escolhe a coordenada à jogar. Regras de peça de dama aplicadas

        int coordenadaUm = coordenadaVelha[0];
        int coordenadaDois = coordenadaVelha[1];
        char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
        char simboloSelecionado = ' ';
        char simboloPossivel = ' ';
        char simboloNormal = ' ';
        if (simboloJogador == '⊠') {
            simboloSelecionado = '■';
            simboloPossivel = '⬚';
            simboloNormal = '□';
        } else if (simboloJogador == '◓') {
            simboloSelecionado = '●';
            simboloPossivel = '◌';
            simboloNormal = '○';
        } else {
            System.out.println("Erro 5");
            System.exit(4);
        }

        int[][] jogadasPossiveis = obterJogadasPossiveisDama(tabuleiro, coordenadaVelha, simboloNormal);

        inserirJogadasPossiveis(tabuleiroHipotetico, jogadasPossiveis, simboloPossivel);
        tabuleiroHipotetico[coordenadaUm][coordenadaDois] = simboloSelecionado;
        Tabuleiros.mostrarTabuleiro(tabuleiroHipotetico);
        mostrarCoordenadasJogaveis(jogadasPossiveis);

        return obterJogada(jogadasPossiveis);
    }

    public static int[][] obterJogadasPossiveisDama(char[][] tabuleiro, int[] coordenadaVelha, char simboloJogador) {
        // Retorna todas as coordenadas que são possíveis de jogar. Regras de dama

        // Antes de criar o array, precisamos do tamanho dele
        // Vamos atirar para as 4 diagonais. SE, SD, IE, ID (Superior Esquerda, Superior Direita, Inferior Esquerda, Inferior Direita)
        // Caso encontre uma peça inimiga, verificará se a casa seguinte é vazia, e então paramos a verificação. Se a casa seguinte é vazia, ela também conta
        char simboloOposto = obterSimboloOposto(simboloJogador);
        char simboloDamaJogador = obterSimboloDama(simboloJogador);
        char simboloDamaOposto = obterSimboloDamaOposto(simboloJogador);

        if (Main.debugging) {
            System.out.printf("Meu simbolo: %s\nMinha dama: %s\nSeu simbolo: %s\nSua dama: %s\n", simboloJogador, simboloDamaJogador, simboloOposto, simboloDamaOposto);
        }

        ArrayList<int[]> jogadasPossiveis = new ArrayList<>();
        int[] coordenadas = new int[2];

        // Para à superior esquerda
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] - i >= 0) && (coordenadaVelha[1] - i) >= 0) {
                if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] - i;
                    coordenadas[1] = coordenadaVelha[1] - i;
                    jogadasPossiveis.add(coordenadas.clone());
                } else if ((tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloOposto)
                        || (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloDamaOposto)) {
                    if ((coordenadaVelha[0] - i >= 1) && (coordenadaVelha[1] - i >= 1)
                            && (tabuleiro[coordenadaVelha[0] - i - 1][coordenadaVelha[1] - i - 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] - i - 1;
                        coordenadas[1] = coordenadaVelha[1] - i - 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                } else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloJogador
                        || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] - i == 0) || (coordenadaVelha[1] - i == 0)) {
                    break;
                }
            }
        }

        // Para a superior direita
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] - i >= 0) && (coordenadaVelha[1] + i) <= 7) {
                if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] - i;
                    coordenadas[1] = coordenadaVelha[1] + i;
                    jogadasPossiveis.add(coordenadas.clone());
                } else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloOposto
                        || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] - i >= 1) && (coordenadaVelha[1] + i <= 6)
                            && (tabuleiro[coordenadaVelha[0] - i - 1][coordenadaVelha[1] + i + 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] - i - 1;
                        coordenadas[1] = coordenadaVelha[1] + i + 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                } else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloJogador
                        || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] - i == 0) || (coordenadaVelha[1] + i == 7)) {
                    break;
                }
            }
        }

        // Para a inferior esquerda
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] + i <= 7) && (coordenadaVelha[1] - i) >= 0) {
                if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] + i;
                    coordenadas[1] = coordenadaVelha[1] - i;
                    jogadasPossiveis.add(coordenadas.clone());
                } else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloOposto
                        || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] + i <= 6) && (coordenadaVelha[1] - i >= 1)
                            && (tabuleiro[coordenadaVelha[0] + i + 1][coordenadaVelha[1] - i - 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] + i + 1;
                        coordenadas[1] = coordenadaVelha[1] - i - 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                } else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloJogador
                        || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] + i == 7) || (coordenadaVelha[1] - i == 0)) {
                    break;
                }
            }
        }

        // Para a inferior direita
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] + i <= 7) && (coordenadaVelha[1] + i) <= 7) {
                if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] + i;
                    coordenadas[1] = coordenadaVelha[1] + i;
                    jogadasPossiveis.add(coordenadas.clone());
                } else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloOposto
                        || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] + i <= 6) && (coordenadaVelha[1] + i <= 6)
                            && (tabuleiro[coordenadaVelha[0] + i + 1][coordenadaVelha[1] + i + 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] + i + 1;
                        coordenadas[1] = coordenadaVelha[1] + i + 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                } else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloJogador
                        || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] + i == 7) || (coordenadaVelha[1] + i == 7)) {
                    break;
                }
            }
        }

        if (Main.debugging) {
            System.out.printf("Encontrei estas jogadas possiveis: %s\n", Arrays.deepToString(jogadasPossiveis.toArray()));
        }

        // Agora, convertemos o ArrayList em int[][] para retorna-lo
        int[][] jogadasPossiveisFormatada = new int[jogadasPossiveis.size()][2];
        for (int i = 0; i < jogadasPossiveis.size(); i++) {
            jogadasPossiveisFormatada[i][0] = jogadasPossiveis.get(i)[0];
            jogadasPossiveisFormatada[i][1] = jogadasPossiveis.get(i)[1];
            if (Main.debugging) {
                System.out.printf("Encontrei uma jogada possivel na coordenada %d %d\n", jogadasPossiveisFormatada[i][0], jogadasPossiveisFormatada[i][1]);
            }
        }

        return jogadasPossiveisFormatada;
    }

    public static char obterSimboloDama(char simboloJogador) {
        if (simboloJogador == '□') {
            return '⊠';
        } else {
            return '◓';
        }
    }

    public static char obterSimboloDamaOposto(char simboloJogador) {
        if (simboloJogador == '□') {
            return '◓';
        } else {
            return '⊠';
        }
    }

    public static char obterSimboloOposto(char simboloJogador) {
        if (simboloJogador == '□') {
            return '○';
        } else {
            return '□';
        }
    }

    public static char obterSimboloSelecionado(char simboloJogador) {
        if (simboloJogador == '□') {
            return '■';
        } else {
            return '●';
        }
    }

    public static char obterSimboloPossivel(char simboloJogador) {
        if (simboloJogador == '□') {
            return '⬚';
        } else {
            return '◌';
        }
    }

    public static int[][][] obterJogadasObrigatorias(char[][] tabuleiro, char simboloJogador) {
        // Para forçar a regra de jogadas obrigatórias, precisa-se procurar todas as jogadas obrigatórias e armazena-las em um array
        // Como mais de uma peça pode ter jogadas obrigatórias em um turno, é necessario guarda-las em um array 3d

        char simboloDamaJogador = obterSimboloDama(simboloJogador);

        // Como estamos trabalhando com um array 3d, algumas bizarrices vão aparecer
        // Uma coordenada é um int[], uma lista de coordenadas é um int[][], e uma lista de coordenadas de jogadas de várias peças é um int[][][]
        // Formatação do int[][][] é: int[quantidadeDePecas][quantidadeDeJogadasDessaPeca][coordenadasDasJogadasDessaPeca]
        // Um ArrayList<ArrayList<int[]>> é equivalente ao array 3d, armazenando uma lista de listas de coordenadas
        ArrayList<ArrayList<int[]>> jogadasObrigatorias = new ArrayList<>();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (tabuleiro[linha][coluna] == simboloJogador || tabuleiro[linha][coluna] == simboloDamaJogador) {
                    // As 3 linhas acima significam que estamos procurando por todas as peças do jogador atual que estão no tabuleiro
                    int[] coordenadaAtual = {linha, coluna};
                    int[][] jogadasObrigatoriasDaPecaAtualFormatado = jogadasObrigatoriasDaPeca(tabuleiro, simboloJogador, coordenadaAtual);
                    ArrayList<int[]> jogadasObrigatoriasDaPecaAtual = new ArrayList<>();
                    if (jogadasObrigatoriasDaPecaAtualFormatado.length > 0) {
                        // Caso existam jogadas obrigatórias para esta peça, adicionamos todas elas no ArrayList<int[]> criado 2 linhas acima, nesse formato:
                        // Posição 0: Coordenada da peça. Todas as outras posições: Coordenadas de jogadas obrigatórias
                        // Esse formato não será usado aqui, mas será usado no momento que o jogador for escolher qual peça vai jogar, em outro metodo diferente
                        // Por esse motivo, a primeira coisa que adicionamos a coordenada atual ao ArrayList são as coordenadas da peça atual
                        jogadasObrigatoriasDaPecaAtual.add(coordenadaAtual);
                        for (int i = 0; i < jogadasObrigatoriasDaPecaAtualFormatado.length; i++) {
                            //Aqui adicionamos todas as jogadas obrigatórias encontradas no ArrayList<int[]>
                            jogadasObrigatoriasDaPecaAtual.add(jogadasObrigatoriasDaPecaAtualFormatado[i]);
                        }
                    }
                    // Agora que adicionamos todas as jogadas obrigatórias da peça atual no ArrayList<int[]>, colocamos ela no ArrayList<ArrayList<int[]>>
                    if (!jogadasObrigatoriasDaPecaAtual.isEmpty()) {
                        jogadasObrigatorias.add(jogadasObrigatoriasDaPecaAtual);
                    }
                    // Repetimos o loop até que todas as peças do jogador atual passem por essa verificação
                    // No final do loop, teremos um ArrayList<ArrayList<int[]>> contendo todas as jogadas obrigatórias possíveis (se existem)
                }
            }
        }
        // Caso nenhuma jogada obrigatória seja encontrada, retornamos um array de tamanho 0
        if (jogadasObrigatorias.isEmpty()) {
            return new int[0][][];
        }
        // Caso jogadas obrigatórias existam, precisamos converter o ArrayList principal em um int[][][], para depois retorna-lo
        int quantidadeDePecasComJogadasObrigatorias = jogadasObrigatorias.size();
        int[][][] jogadasObrigatoriasFormatada = new int[quantidadeDePecasComJogadasObrigatorias][][];

        // Primeiro, definimos o tamanho do int[][] de cada uma das peças
        for (int i = 0; i < quantidadeDePecasComJogadasObrigatorias; i++) {
            int quantidadeDeJogadasObrigatoriasPecaAtual = jogadasObrigatorias.get(i).size();
            jogadasObrigatoriasFormatada[i] = new int[quantidadeDeJogadasObrigatoriasPecaAtual][2];
            // Agora, podemos povoar o int[][][]
            for (int j = 0; j < quantidadeDeJogadasObrigatoriasPecaAtual; j++) {
                jogadasObrigatoriasFormatada[i][j] = jogadasObrigatorias.get(i).get(j);
            }
        }
        // Esse foi de longe o metodo mais complicado que já fiz até agora
        return jogadasObrigatoriasFormatada;
    }

    public static int[][] jogadasObrigatoriasDaPeca(char[][] tabuleiro, char simboloJogador, int[] coordenadaAtual) {
        // Coloca todas as jogadas obrigatorias de uma peça em um int[][]
        // Retorna null se nenhuma foi encontrada
        char simboloDamaJogador = obterSimboloDama(simboloJogador);
        char simboloJogadorOposto = obterSimboloOposto(simboloJogador);
        char simboloDamaJogadorOposto = obterSimboloDamaOposto(simboloJogador);
        int coordenadaUm = coordenadaAtual[0];
        int coordenadaDois = coordenadaAtual[1];

        ArrayList<int[]> jogadasObrigatorias = new ArrayList<>();
        int[] coordenadas = new int[2];

        // Para peças normais
        if (tabuleiro[coordenadaUm][coordenadaDois] == simboloJogador) {
            if (coordenadaUm < 6) {
                if (coordenadaDois > 1) {
                    if (tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == simboloJogadorOposto
                            || tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == simboloDamaJogadorOposto) {
                        if (tabuleiro[coordenadaUm + 2][coordenadaDois - 2] == ' ') {
                            if (Main.debugging) {
                                System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm + 1), (coordenadaDois - 1));
                                System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                            }
                            coordenadas[0] = coordenadaUm + 2;
                            coordenadas[1] = coordenadaDois - 2;
                            jogadasObrigatorias.add(coordenadas.clone());
                        }
                    }
                }
                if (coordenadaDois < 6) {
                    if (tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == simboloJogadorOposto
                            || tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == simboloDamaJogadorOposto) {
                        if (tabuleiro[coordenadaUm + 2][coordenadaDois + 2] == ' ') {
                            if (Main.debugging) {
                                System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm + 1), (coordenadaDois + 1));
                                System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                            }
                            coordenadas[0] = coordenadaUm + 2;
                            coordenadas[1] = coordenadaDois + 2;
                            jogadasObrigatorias.add(coordenadas.clone());

                        }
                    }
                }
            }
            if (coordenadaUm > 1) {
                if (coordenadaDois > 1) {
                    if (tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == simboloJogadorOposto
                            || tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == simboloDamaJogadorOposto) {
                        if (tabuleiro[coordenadaUm - 2][coordenadaDois - 2] == ' ') {
                            if (Main.debugging) {
                                System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm - 1), (coordenadaDois - 1));
                                System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                            }
                            coordenadas[0] = coordenadaUm - 2;
                            coordenadas[1] = coordenadaDois - 2;
                            jogadasObrigatorias.add(coordenadas.clone());
                        }
                    }
                }
                if (coordenadaDois < 6) {
                    if (tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == simboloJogadorOposto
                            || tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == simboloDamaJogadorOposto) {
                        if (tabuleiro[coordenadaUm - 2][coordenadaDois + 2] == ' ') {
                            if (Main.debugging) {
                                System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm - 1), (coordenadaDois + 1));
                                System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                            }
                            coordenadas[0] = coordenadaUm - 2;
                            coordenadas[1] = coordenadaDois + 2;
                            jogadasObrigatorias.add(coordenadas.clone());
                        }
                    }
                }
            }
        }

        // Para damas
        else {
            int[] coordenadaNova = new int[2];
            int[] coordenadaTemp = new int[2];
            // Para o lado superior esquerdo
            for (int i = 1; i < 7; i++) {
                if (coordenadaUm - i >= 1) {
                    if (coordenadaDois - i >= 1) {
                        if (tabuleiro[coordenadaUm - i][coordenadaDois - i] == simboloJogador
                        || tabuleiro[coordenadaUm - i][coordenadaDois - i] == simboloDamaJogador) {
                            break;
                        }
                        if (tabuleiro[coordenadaUm - i][coordenadaDois - i] == simboloJogadorOposto
                                || tabuleiro[coordenadaUm - i][coordenadaDois - i] == simboloDamaJogadorOposto) {
                            if (tabuleiro[coordenadaUm - i - 1][coordenadaDois - i - 1] == ' ') {
                                if (Main.debugging) {
                                    System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm - i - 1), (coordenadaDois - i - 1));
                                    System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                                }
                                coordenadas[0] = coordenadaUm - i - 1;
                                coordenadas[1] = coordenadaDois - i - 1;
                                jogadasObrigatorias.add(coordenadas.clone());

                                // Aqui, adicionamos as casas vazias após a peça comida como opção de jogada obrigatória
                                coordenadaNova = coordenadas.clone();
                                for (int j = 1; j < 7; j++) {
                                    if (coordenadaNova[0] - j < 0 || coordenadaNova[1] - j < 0) {
                                        break;
                                    }
                                    if (tabuleiro[coordenadaNova[0] - j][coordenadaNova[1] - j] == ' ') {
                                        coordenadaTemp[0] = coordenadaNova[0] - j;
                                        coordenadaTemp[1] = coordenadaNova[1] - j;
                                        jogadasObrigatorias.add(coordenadaTemp.clone());
                                    }
                                    else {
                                        break;
                                    }
                                }
                                // Se comemos uma peça, paramos o loop
                                break;
                            }
                        }
                    }
                }
            }

            // Lado superior direito
            for (int i = 1; i < 7; i++) {
                if (coordenadaUm - i >= 1) {
                    if (coordenadaDois + i <= 6) {
                        if (tabuleiro[coordenadaUm - i][coordenadaDois + i] == simboloJogador
                                || tabuleiro[coordenadaUm - i][coordenadaDois + i] == simboloDamaJogador) {
                            break;
                        }
                        if (tabuleiro[coordenadaUm - i][coordenadaDois + i] == simboloJogadorOposto
                                || tabuleiro[coordenadaUm - i][coordenadaDois + i] == simboloDamaJogadorOposto) {
                            if (tabuleiro[coordenadaUm - i - 1][coordenadaDois + i + 1] == ' ') {
                                if (Main.debugging) {
                                    System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm - i - 1), (coordenadaDois + i + 1));
                                    System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                                }
                                coordenadas[0] = coordenadaUm - i - 1;
                                coordenadas[1] = coordenadaDois + i + 1;
                                jogadasObrigatorias.add(coordenadas.clone());

                                coordenadaNova = coordenadas.clone();
                                for (int j = 1; j < 7; j++) {
                                    if (coordenadaNova[0] - j < 0 || coordenadaNova[1] + j > 7) {
                                        break;
                                    }
                                    if (tabuleiro[coordenadaNova[0] - j][coordenadaNova[1] + j] == ' ') {
                                        coordenadaTemp[0] = coordenadaNova[0] - j;
                                        coordenadaTemp[1] = coordenadaNova[1] + j;
                                        jogadasObrigatorias.add(coordenadaTemp.clone());
                                    }
                                    else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // Lado inferior esquerdo
            for (int i = 1; i < 7; i++) {
                if (coordenadaUm + i <= 6) {
                    if (coordenadaDois - i >= 1) {
                        if (tabuleiro[coordenadaUm + i][coordenadaDois - i] == simboloJogador
                                || tabuleiro[coordenadaUm + i][coordenadaDois - i] == simboloDamaJogador) {
                            break;
                        }
                        if (tabuleiro[coordenadaUm + i][coordenadaDois - i] == simboloJogadorOposto
                                || tabuleiro[coordenadaUm + i][coordenadaDois - i] == simboloDamaJogadorOposto) {
                            if (tabuleiro[coordenadaUm + i + 1][coordenadaDois - i - 1] == ' ') {
                                if (Main.debugging) {
                                    System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm + i + 1), (coordenadaDois - i - 1));
                                    System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                                }
                                coordenadas[0] = coordenadaUm + i + 1;
                                coordenadas[1] = coordenadaDois - i - 1;
                                jogadasObrigatorias.add(coordenadas.clone());

                                coordenadaNova = coordenadas.clone();
                                for (int j = 1; j < 7; j++) {
                                    if (coordenadaNova[0] + j > 7 || coordenadaNova[1] - j < 0) {
                                        break;
                                    }
                                    if (tabuleiro[coordenadaNova[0] + j][coordenadaNova[1] - j] == ' ') {
                                        coordenadaTemp[0] = coordenadaNova[0] + j;
                                        coordenadaTemp[1] = coordenadaNova[1] - j;
                                        jogadasObrigatorias.add(coordenadaTemp.clone());
                                    }
                                    else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // Lado inferior direito
            for (int i = 1; i < 7; i++) {
                if (coordenadaUm + i <= 6) {
                    if (coordenadaDois + i <= 6) {
                        if (tabuleiro[coordenadaUm + i][coordenadaDois + i] == simboloJogador
                                || tabuleiro[coordenadaUm + i][coordenadaDois + i] == simboloDamaJogador) {
                            break;
                        }
                        if (tabuleiro[coordenadaUm + i][coordenadaDois + i] == simboloJogadorOposto
                                || tabuleiro[coordenadaUm + i][coordenadaDois + i] == simboloDamaJogadorOposto) {
                            if (tabuleiro[coordenadaUm + i + 1][coordenadaDois + i + 1] == ' ') {
                                if (Main.debugging) {
                                    System.out.printf("Encontrei um oponente que posso comer nas coordenadas %d %d!\n", (coordenadaUm + i + 1), (coordenadaDois + i + 1));
                                    System.out.printf("Minha coordenada é: %d %d\n", coordenadaAtual[0], coordenadaAtual[1]);
                                }
                                coordenadas[0] = coordenadaUm + i + 1;
                                coordenadas[1] = coordenadaDois + i + 1;
                                jogadasObrigatorias.add(coordenadas.clone());

                                coordenadaNova = coordenadas.clone();
                                for (int j = 1; j < 7; j++) {
                                    if (coordenadaNova[0] + j > 7 || coordenadaNova[1] + j > 7) {
                                        break;
                                    }
                                    if (tabuleiro[coordenadaNova[0] + j][coordenadaNova[1] + j] == ' ') {
                                        coordenadaTemp[0] = coordenadaNova[0] + j;
                                        coordenadaTemp[1] = coordenadaNova[1] + j;
                                        jogadasObrigatorias.add(coordenadaTemp.clone());
                                    }
                                    else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (jogadasObrigatorias.isEmpty()) {
            if (Main.debugging) {
                System.out.printf("Não encontrei peças com jogadas obrigatórias para a peça %d %d!\n", coordenadaAtual[0], coordenadaAtual[1]);
            }
            return new int[0][];
        }

        // Agora que temos a quantidade de jogadas obrigatorias, podemos criar o array, para então retorna-lo.
        int[][] jogadasObrigatoriasFormatada = new int[jogadasObrigatorias.size()][2];
        for (int i = 0; i < jogadasObrigatorias.size(); i++) {
            jogadasObrigatoriasFormatada[i] = jogadasObrigatorias.get(i);
        }
        return jogadasObrigatoriasFormatada;
    }

    public static int[][] realizarJogadaObrigatoria(char[][] tabuleiro, char simboloJogador, int[][][] jogadasObrigatorias) {
        // Recebemos um int[][][] contendo todas as jogadas obrigatórias
        // Retornamos um int[2][2] nesse formato: Coordenada Velha, Coordenada Nova
        char simboloJogadorSelecionado = obterSimboloSelecionado(simboloJogador);
        char simboloPossivel = obterSimboloPossivel(simboloJogador);
        int[][] pecasObrigatorias = new int[jogadasObrigatorias.length][2];
        for (int i = 0; i < jogadasObrigatorias.length; i++) {
            pecasObrigatorias[i] = jogadasObrigatorias[i][0];
        }
        mostrarPecasObrigatorias(pecasObrigatorias);
        int[] coordenadaVelha = escolherPecaObrigatoria(pecasObrigatorias);
        int[][] jogadasPossiveis = obterJogadasPossiveisObrigatorias(coordenadaVelha, jogadasObrigatorias);
        char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
        tabuleiroHipotetico[coordenadaVelha[0]][coordenadaVelha[1]] = simboloJogadorSelecionado;
        inserirJogadasPossiveis(tabuleiroHipotetico, jogadasPossiveis, simboloPossivel);
        Tabuleiros.mostrarTabuleiro(tabuleiroHipotetico);
        int[] coordenadaNova = escolherJogadaObrigatoria(jogadasPossiveis);

        int[][] jogadaObrigatoria = {coordenadaVelha, coordenadaNova};
        return jogadaObrigatoria;
    }

    public static void mostrarPecasObrigatorias(int[][] pecasObrigatorias) {
        System.out.println("Você tem jogadas obrigatórias nesse turno! Você deve comer uma peça inimiga!");
        System.out.printf("Coordenadas das peças que você pode escolher: ");
        for (int i = 0; i < pecasObrigatorias.length; i++) {
            String coordenadaTexto = Main.obterCoordenadaEmTexto(pecasObrigatorias[i]);
            System.out.printf("%s ", coordenadaTexto);
        }
        System.out.println();
    }

    public static int[] escolherPecaObrigatoria(int[][] pecasObrigatorias) {
        // Recebemos um int[][] com a listagem de peças que o jogador é obrigado a receber. Aqui, ele precisa escolher uma delas
        System.out.println("Escolha sua peça: ");
        int[] escolha = new int[2];
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

    public static int[][] obterJogadasPossiveisObrigatorias(int[] coordenadaVelha, int[][][] jogadasObrigatorias) {
        // Recebemos a coordenada selecionada do jogador, e o int[][][] com todas as jogadas possiveis
        // Precisamos retornar todas as jogadas obrigatórias possiveis dessa peça selecionada, em um int[][]
        // Lembrando que o primeiro elemento do int[][][] sempre é a coordenada da peça que vai jogar, e não as coordenadas que ela vai jogar
        int[] coordenada = new int[2];
        ArrayList<int[]> jogadasObrigatoriasDessaPeca = new ArrayList<>();
        for (int i = 0; i < jogadasObrigatorias.length; i++) {
            if (Arrays.equals(jogadasObrigatorias[i][0], coordenadaVelha)) {
                for (int j = 1; j < jogadasObrigatorias[i].length; j++) {
                    coordenada = jogadasObrigatorias[i][j];
                    jogadasObrigatoriasDessaPeca.add(coordenada);
                }
            }
        }

        if (Main.debugging) {
            System.out.printf("Encontrei essas coordenadas para uma jogada obrigatória: %s\n", Arrays.deepToString(jogadasObrigatoriasDessaPeca.toArray()));
        }

        int[][] jogadasObrigatoriasFormatado = new int[jogadasObrigatoriasDessaPeca.size()][2];
        for (int i = 0; i < jogadasObrigatoriasDessaPeca.size(); i++) {
            jogadasObrigatoriasFormatado[i] = jogadasObrigatoriasDessaPeca.get(i);
        }
        return jogadasObrigatoriasFormatado;
    }

    public static int[] escolherJogadaObrigatoria(int[][] jogadasObrigatorias) {
        System.out.print("Você pode jogar nessas coordenadas com essa peça: ");
        for (int i = 0; i < jogadasObrigatorias.length; i++) {
            System.out.printf("%s ", Main.obterCoordenadaEmTexto(jogadasObrigatorias[i]));
        }
        System.out.println();
        System.out.println("Escolha: ");
        while (true) {
            int[] coordenadasEscolhida = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < jogadasObrigatorias.length; i++) {
                if (Arrays.equals(coordenadasEscolhida, jogadasObrigatorias[i])) {
                    return coordenadasEscolhida;
                }
            }
            System.out.println("Coordenada inválida! Você precisa comer uma peça do inimigo!");
        }
    }
}
