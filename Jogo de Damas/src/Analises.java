import java.util.ArrayList;
import java.util.Arrays;

public class Analises {
    static boolean debugging = false;

    public static int[][][][] obterTodasAsJogadasPossiveis(char jogador) {
        return obterTodasAsJogadasPossiveis(jogador, Tabuleiros.tabuleiro);
    }

    public static int[][][][] obterTodasAsJogadasPossiveis(char jogador, char[][] tabuleiro) {
        // Esse é o metodo principal do Analises. Deve funcionar para os dois jogadores
        // Deve funcionar tanto para jogadas obrigatórias, quanto jogadas normais
        // Caso encontre uma jogada obrigatória, apaga as informações guardadas, e passa a adicionar apenas jogadas obrigatórias

        char dama = obterDama(jogador);
        char oponente = obterOponente(jogador);
        char damaOponente = obterDamaOponente(jogador);

        boolean ehJogadaObrigatoria = false;

        ArrayList<ArrayList<int[][]>> todasAsJogadas = new ArrayList<>();
        ArrayList<int[][]> jogadasDaPeca = new ArrayList<int[][]>();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                // Aqui vai as regras de peça normal
                if (tabuleiro[linha][coluna] == jogador) {
                    // Aqui as regras para o jogador um
                    if (jogador == '□') {
                        if (linha < 7 && coluna < 7) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha + 1][coluna + 1] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha + 1, coluna + 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}), obterCoordenadaEmTexto(new int[]{linha + 1, coluna + 1}));
                                }
                            }
                        }
                        if (linha < 7 && coluna > 0) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha + 1][coluna - 1] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha + 1, coluna - 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}), obterCoordenadaEmTexto(new int[]{linha + 1, coluna - 1}));
                                }
                            }
                        }
                    }
                    // Aqui as regras para o jogador dois
                    else if (jogador == '○') {
                        if (linha > 0 && coluna < 7) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha - 1][coluna + 1] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha - 1, coluna + 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}), obterCoordenadaEmTexto(new int[]{linha - 1, coluna + 1}));
                                }
                            }
                        }
                        if (linha > 0 && coluna > 0) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha - 1][coluna - 1] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha - 1, coluna - 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}), obterCoordenadaEmTexto(new int[]{linha - 1, coluna - 1}));
                                }
                            }
                        }
                    }

                    // Aqui para os dois jogadores, caso consiga comer uma peça (frente ou trás)
                    if (linha < 6 && coluna < 6) {
                        if (tabuleiro[linha + 1][coluna + 1] == oponente
                                || tabuleiro[linha + 1][coluna + 1] == damaOponente) {
                            if (tabuleiro[linha + 2][coluna + 2] == ' ') {
                                if (!ehJogadaObrigatoria) {
                                    todasAsJogadas.clear();
                                    jogadasDaPeca.clear();
                                    ehJogadaObrigatoria = true;
                                }
                                int[][] estaJogada = {{linha, coluna}, {linha + 2, coluna + 2}, {linha + 1, coluna + 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha + 2, coluna + 2}),
                                            obterCoordenadaEmTexto(new int[]{linha + 1, coluna + 1}));
                                }
                            }
                        }
                    }
                    if (linha < 6 && coluna > 1) {
                        if (tabuleiro[linha + 1][coluna - 1] == oponente
                                || tabuleiro[linha + 1][coluna - 1] == damaOponente) {
                            if (tabuleiro[linha + 2][coluna - 2] == ' ') {
                                if (!ehJogadaObrigatoria) {
                                    todasAsJogadas.clear();
                                    jogadasDaPeca.clear();
                                    ehJogadaObrigatoria = true;
                                }
                                int[][] estaJogada = {{linha, coluna}, {linha + 2, coluna - 2}, {linha + 1, coluna - 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha + 2, coluna - 2}),
                                            obterCoordenadaEmTexto(new int[]{linha + 1, coluna - 1}));
                                }
                            }
                        }
                    }
                    if (linha > 1 && coluna < 6) {
                        if (tabuleiro[linha - 1][coluna + 1] == oponente
                                || tabuleiro[linha - 1][coluna + 1] == damaOponente) {
                            if (tabuleiro[linha - 2][coluna + 2] == ' ') {
                                if (!ehJogadaObrigatoria) {
                                    todasAsJogadas.clear();
                                    jogadasDaPeca.clear();
                                    ehJogadaObrigatoria = true;
                                }
                                int[][] estaJogada = {{linha, coluna}, {linha - 2, coluna + 2}, {linha - 1, coluna + 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha - 2, coluna + 2}),
                                            obterCoordenadaEmTexto(new int[]{linha - 1, coluna + 1}));
                                }
                            }
                        }
                    }
                    if (linha > 1 && coluna > 1) {
                        if (tabuleiro[linha - 1][coluna - 1] == oponente
                                || tabuleiro[linha - 1][coluna - 1] == damaOponente) {
                            if (tabuleiro[linha - 2][coluna - 2] == ' ') {
                                if (!ehJogadaObrigatoria) {
                                    todasAsJogadas.clear();
                                    jogadasDaPeca.clear();
                                    ehJogadaObrigatoria = true;
                                }
                                int[][] estaJogada = {{linha, coluna}, {linha - 2, coluna - 2}, {linha - 1, coluna - 1}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha - 2, coluna - 2}),
                                            obterCoordenadaEmTexto(new int[]{linha - 1, coluna - 1}));
                                }
                            }
                        }
                    }
                }

                // Aqui vai as regras de peças damas
                else if (tabuleiro[linha][coluna] == dama) {
                    for (int distancia = 1; distancia < 8; distancia++) {
                        // Inferior Direita
                        if (linha + distancia < 8 && coluna + distancia < 8) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha + distancia][coluna + distancia] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha + distancia, coluna + distancia}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha + distancia, coluna + distancia}));
                                }
                            }
                            if (tabuleiro[linha + distancia][coluna + distancia] == oponente
                                    || tabuleiro[linha + distancia][coluna + distancia] == damaOponente) {
                                for (int alcance = 1; alcance < 7; alcance++) {
                                    if (linha + distancia + alcance < 8 && coluna + distancia + alcance < 8) {
                                        if (tabuleiro[linha + distancia + alcance][coluna + distancia + alcance] == ' ') {
                                            if (!ehJogadaObrigatoria) {
                                                todasAsJogadas.clear();
                                                jogadasDaPeca.clear();
                                                ehJogadaObrigatoria = true;
                                            }
                                            int[][] estaJogada = {{linha, coluna},
                                                    {linha + distancia + alcance, coluna + distancia + alcance},
                                                    {linha + distancia, coluna + distancia}};
                                            jogadasDaPeca.add(estaJogada);
                                            if (Main.debugging || debugging) {
                                                System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                                        obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                                        obterCoordenadaEmTexto(new int[]{linha + distancia + alcance, coluna + distancia + alcance}),
                                                        obterCoordenadaEmTexto(new int[]{linha + distancia, coluna + distancia}));
                                            }
                                        } else {
                                            // Caso tenha encontrado uma outra peça no caminho, ele para a análise
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            }
                            // Isso roda caso encontramos uma de nossas peças no meio do caminho
                            else if (tabuleiro[linha + distancia][coluna + distancia] != ' ') {
                                break;
                            }
                        }
                    }
                    for (int distancia = 1; distancia < 8; distancia++) {
                        // Inferior Esquerda
                        if (linha + distancia < 8 && coluna - distancia >= 0) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha + distancia][coluna - distancia] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha + distancia, coluna - distancia}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha + distancia, coluna - distancia}));
                                }
                            }
                            if (tabuleiro[linha + distancia][coluna - distancia] == oponente
                                    || tabuleiro[linha + distancia][coluna - distancia] == damaOponente) {
                                for (int alcance = 1; alcance < 7; alcance++) {
                                    if (linha + distancia + alcance < 8 && coluna - distancia - alcance >= 0) {
                                        if (tabuleiro[linha + distancia + alcance][coluna - distancia - alcance] == ' ') {
                                            if (!ehJogadaObrigatoria) {
                                                todasAsJogadas.clear();
                                                jogadasDaPeca.clear();
                                                ehJogadaObrigatoria = true;
                                            }
                                            int[][] estaJogada = {{linha, coluna},
                                                    {linha + distancia + alcance, coluna - distancia - alcance},
                                                    {linha + distancia, coluna - distancia}};
                                            jogadasDaPeca.add(estaJogada);
                                            if (Main.debugging || debugging) {
                                                System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                                        obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                                        obterCoordenadaEmTexto(new int[]{linha + distancia + alcance, coluna - distancia - alcance}),
                                                        obterCoordenadaEmTexto(new int[]{linha + distancia, coluna - distancia}));
                                            }
                                        } else {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            } else if (tabuleiro[linha + distancia][coluna - distancia] != ' ') {
                                break;
                            }
                        }
                    }
                    for (int distancia = 1; distancia < 8; distancia++) {
                        // Superior Direita
                        if (linha - distancia >= 0 && coluna + distancia < 8) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha - distancia][coluna + distancia] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha - distancia, coluna + distancia}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha - distancia, coluna + distancia}));
                                }
                            }
                            if (tabuleiro[linha - distancia][coluna + distancia] == oponente
                                    || tabuleiro[linha - distancia][coluna + distancia] == damaOponente) {
                                for (int alcance = 1; alcance < 7; alcance++) {
                                    if (linha - distancia - alcance >= 0 && coluna + distancia + alcance < 8) {
                                        if (tabuleiro[linha - distancia - alcance][coluna + distancia + alcance] == ' ') {
                                            if (!ehJogadaObrigatoria) {
                                                todasAsJogadas.clear();
                                                jogadasDaPeca.clear();
                                                ehJogadaObrigatoria = true;
                                            }
                                            int[][] estaJogada = {{linha, coluna},
                                                    {linha - distancia - alcance, coluna + distancia + alcance},
                                                    {linha - distancia, coluna + distancia}};
                                            jogadasDaPeca.add(estaJogada);
                                            if (Main.debugging || debugging) {
                                                System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                                        obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                                        obterCoordenadaEmTexto(new int[]{linha - distancia - alcance, coluna + distancia + alcance}),
                                                        obterCoordenadaEmTexto(new int[]{linha - distancia, coluna + distancia}));
                                            }
                                        } else {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            } else if (tabuleiro[linha - distancia][coluna + distancia] != ' ') {
                                break;
                            }
                        }
                    }
                    for (int distancia = 1; distancia < 8; distancia++) {
                        // Superior Esquerda
                        if (linha - distancia >= 0 && coluna - distancia >= 0) {
                            if (!ehJogadaObrigatoria && tabuleiro[linha - distancia][coluna - distancia] == ' ') {
                                int[][] estaJogada = {{linha, coluna}, {linha - distancia, coluna - distancia}};
                                jogadasDaPeca.add(estaJogada);
                                if (Main.debugging || debugging) {
                                    System.out.printf("(Analises) Encontrei uma possivel jogada da peça %s para a coordenada %s!\n",
                                            obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                            obterCoordenadaEmTexto(new int[]{linha - distancia, coluna - distancia}));
                                }
                            }
                            if (tabuleiro[linha - distancia][coluna - distancia] == oponente
                                    || tabuleiro[linha - distancia][coluna - distancia] == damaOponente) {
                                for (int alcance = 1; alcance < 7; alcance++) {
                                    if (linha - distancia - alcance >= 0 && coluna - distancia - alcance >= 0) {
                                        if (tabuleiro[linha - distancia - alcance][coluna - distancia - alcance] == ' ') {
                                            if (!ehJogadaObrigatoria) {
                                                todasAsJogadas.clear();
                                                jogadasDaPeca.clear();
                                                ehJogadaObrigatoria = true;
                                            }
                                            int[][] estaJogada = {{linha, coluna},
                                                    {linha - distancia - alcance, coluna - distancia - alcance},
                                                    {linha - distancia, coluna - distancia}};
                                            jogadasDaPeca.add(estaJogada);
                                            if (Main.debugging || debugging) {
                                                System.out.printf("(Analises) Encontrei uma peça a comer! Minha posição: %s. Posição destino: %s. Peça que comi: %s\n",
                                                        obterCoordenadaEmTexto(new int[]{linha, coluna}),
                                                        obterCoordenadaEmTexto(new int[]{linha - distancia - alcance, coluna - distancia - alcance}),
                                                        obterCoordenadaEmTexto(new int[]{linha - distancia, coluna - distancia}));
                                            }
                                        } else {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            } else if (tabuleiro[linha - distancia][coluna - distancia] != ' ') {
                                break;
                            }
                        }
                    }
                }
                if (!jogadasDaPeca.isEmpty()) {
                    todasAsJogadas.add((ArrayList<int[][]>) jogadasDaPeca.clone());
                    jogadasDaPeca.clear();
                }
            }
        }
        if (ehPartidaReal(tabuleiro)) {
            if (Main.modoDeJogo.equals("multiplayer") || jogador == '□') {
                Jogador.ehJogadaObrigatoria = ehJogadaObrigatoria;
            } else {
                IA.ehJogadaObrigatoria = ehJogadaObrigatoria;
            }
        }
        // Agora que temos o ArrayList<ArrayList<int[][]>> pronto e povoado, vamos transforma-lo em int[][][][] para retorna-lo
        int[][][][] todasAsJogadasFormatado = new int[todasAsJogadas.size()][][][];
        for (int peca = 0; peca < todasAsJogadas.size(); peca++) {
            todasAsJogadasFormatado[peca] = new int[todasAsJogadas.get(peca).size()][][];
            for (int jogada = 0; jogada < todasAsJogadas.get(peca).size(); jogada++) {
                todasAsJogadasFormatado[peca][jogada] = todasAsJogadas.get(peca).get(jogada);
            }
        }
        return todasAsJogadasFormatado;
    }


    public static char obterDama(char simboloJogador) {
        if (simboloJogador == '□') {
            return '⊠';
        } else {
            return '◓';
        }
    }

    public static char obterDamaOponente(char simboloJogador) {
        if (simboloJogador == '□') {
            return '◓';
        } else {
            return '⊠';
        }
    }

    public static char obterOponente(char simboloJogador) {
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

    public static char obterSimboloJogador(char simbolo) {
        if (simbolo == '⊠' || simbolo == '□') {
            return '□';
        }
        else {
            return '○';
        }
    }

    public static char obterEstaPeca(int[] coordenada) {
        return Tabuleiros.tabuleiro[coordenada[0]][coordenada[1]];
    }

    public static boolean ehDama(char peca) {
        return (peca == '⊠' || peca == '◓');
    }

    public static boolean ehPartidaReal(char[][] tabuleiro) {
        return (Tabuleiros.tabuleiro == tabuleiro);
    }

    public static boolean coordenadasEscolhidasEhValida(int[] coordenadas, char jogador) {
        // Verifica se a coordenada recebida é uma coordenada jogável por este jogador
        int [][][][] todasAsJogadas;
        if (jogador == '□' || Main.modoDeJogo.equals("multiplayer")) {
            todasAsJogadas = Jogador.todasAsJogadasPossiveis;
        }
        else {
            todasAsJogadas = IA.todasAsJogadasPossiveis;
        }

        for (int peca = 0; peca < todasAsJogadas.length; peca++) {
            if (Arrays.equals(todasAsJogadas[peca][0][0], coordenadas)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ehFinalDoTabuleiro(int[] coordenada, char jogador) {
        if (jogador == '□') {
            return coordenada[0] == 7;
        } else {
            return coordenada[0] == 0;
        }
    }

    public static int[][][] obterJogadasPossiveis(int[] pecaSelecionada, char jogador) {
        // Retorna as jogadas que são possiveis realizar, a partir da peça selecionada
        // Cada jogada é um int[][] contendo coordenadas partida, coordenadas destino, e se existir, coordenadas de captura
        int[][][][] todasAsJogadas;
        if (jogador == '□' || Main.modoDeJogo.equals("multiplayer")) {
            todasAsJogadas = Jogador.todasAsJogadasPossiveis;
        }
        else {
            todasAsJogadas = IA.todasAsJogadasPossiveis;
        }
        if (Main.debugging || debugging) {
            System.out.printf("Estou procurando jogadas possiveis nas coordenadas %s\n", obterCoordenadaEmTexto(pecaSelecionada));
        }

        for (int peca = 0; peca < todasAsJogadas.length; peca++) {
            if (todasAsJogadas[peca].length > 0 && Arrays.equals(todasAsJogadas[peca][0][0], pecaSelecionada)) {
                return todasAsJogadas[peca];
            }
        }
        return new int[0][][];
    }

    public static String obterCoordenadaEmTexto(int[] coordenadas) {
        char[] coordenadaUmPossiveis = {'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] coordenadaDoisPossiveis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int coordenadaUm = coordenadas[0];
        int coordenadaDois = coordenadas[1];
        return ("" + coordenadaUmPossiveis[coordenadaUm] + coordenadaDoisPossiveis[coordenadaDois]);
    }

    public static int[][][] obterJogadasDeCapturaMultipla(char jogador, int[] peca) {
        return obterJogadasDeCapturaMultipla(jogador, peca, Tabuleiros.tabuleiro);
    }

    public static int[][][] obterJogadasDeCapturaMultipla(char jogador, int[] peca, char[][] tabuleiro) {
        // Esse metodo é parecido com o obterTodasAsJogadasPossiveis. Porém, ele analisa do ponto de vista de uma peça já selecionada
        int[][][][] todasAsJogadas = obterTodasAsJogadasPossiveis(jogador, tabuleiro);

        boolean ehJogadaObrigatoria;
        if (jogador == '□' || Main.modoDeJogo.equals("multiplayer")) {
            ehJogadaObrigatoria = Jogador.ehJogadaObrigatoria;
        } else {
            ehJogadaObrigatoria = IA.ehJogadaObrigatoria;
        }
        if (!ehJogadaObrigatoria) {
            return new int[0][][];
        }


        int[][][] jogadas;
        for (int i = 0; i < todasAsJogadas.length; i++) {
            if (todasAsJogadas[i].length > 0 && Arrays.equals(todasAsJogadas[i][0][0], peca)) {
                jogadas = todasAsJogadas[i];
                if (ehPartidaReal(tabuleiro)) {
                    if (Main.modoDeJogo.equals("multiplayer") || jogador == '□') {
                        Jogador.todasAsJogadasPossiveis = todasAsJogadas;
                    } else {
                        IA.todasAsJogadasPossiveis = todasAsJogadas;
                    }
                }
                return jogadas;
            }
        }
        // Caso não encontre, retorna um array de tamanho 0, e desativa a busca por novas jogadas
        if (ehPartidaReal(tabuleiro)) {
            if (jogador == '□' || Main.modoDeJogo.equals("multiplayer")) {
                Jogador.ehJogadaObrigatoria = false;
            } else {
                IA.ehJogadaObrigatoria = false;
            }
        }
        return new int[0][][];
    }
}
