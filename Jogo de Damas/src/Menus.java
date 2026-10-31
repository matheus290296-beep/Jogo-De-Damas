public class Menus {
    public static int mostrarMenuPrincipal() {
        System.out.println("====== JOGO DE DAMAS ======");
        System.out.println("1 - Jogar contra uma Pessoa");
        System.out.println("2 - Jogar contra uma IA");
        System.out.println("3 - Escolher tabuleiro");
        System.out.println("4 - Mostrar Placar");
        System.out.println("5 - Reproduzir Replays");
        System.out.println("6 - Sair");
        System.out.println("0 - Menu Debugging");
        return ValidadorInput.obterInt(0, 6);
    }

    public static int alterarTabuleiroInicial() {
        System.out.println("====== MENU DE ESCOLHA DE TABULEIROS =====");
        System.out.println("1 - Tabuleiro normal");
        System.out.println("2 - Tabuleiro só com damas");
        System.out.println("3 - Tabuleiro gerado aleatoriamente");
        System.out.println("4 - Tabuleiro gerado aleatoriamente (CAOS)");
        if (Main.debugging) {
            System.out.println("\n=========== DEBUGGING ===========");
            System.out.println("5 - Tabuleiro de jogo normal, prestes à virar dama");
            System.out.println("6 - Tabuleiro prestes à comer várias peças");
            System.out.println("7 - Tabuleiro prestes à comer várias peças - versão dama");
            System.out.println("8 - Tabuleiro com alguem prestes a vencer");
            return ValidadorInput.obterInt(1, 8);
        }
        return ValidadorInput.obterInt(1, 4);
    }

    public static void menuDebugging() {
        String mainDebugging;
        String IADebugging;
        String arquivosDebugging;
        String jogoDebugging;
        String replaysDebugging;
        String tabuleirosDebugging;
        while (true) {
            if (Main.debugging) {
                mainDebugging = "Desativar o debugging do Main";
            } else {
                mainDebugging = "Ativar o debugging do Main";
            }
            if (IA.debugging) {
                IADebugging = "Desativar o debugging de IA";
            } else {
                IADebugging = "Ativar o debugging de IA";
            }
            if (Arquivos.debugging) {
                arquivosDebugging = "Desativar o debugging de Arquivos";
            } else {
                arquivosDebugging = "Ativar o debugging de Arquivos";
            }
            if (Jogo.debugging) {
                jogoDebugging = "Desativar o debugging de Jogo";
            } else {
                jogoDebugging = "Ativar o debugging de Jogo";
            }
            if (Replays.debugging) {
                replaysDebugging = "Desativar o debugging de Replays";
            } else {
                replaysDebugging = "Ativar o debugging de Replays";
            }
            if (Tabuleiros.debugging) {
                tabuleirosDebugging = "Desativar o debugging de Tabuleiros";
            } else {
                tabuleirosDebugging = "Ativar o debugging de Tabuleiros";
            }

            System.out.println("===== MENU DE DEBUGGING =====");
            System.out.printf("1 - %s\n", mainDebugging);
            System.out.printf("2 - %s\n", IADebugging);
            System.out.printf("3 - %s\n", arquivosDebugging);
            System.out.printf("4 - %s\n", jogoDebugging);
            System.out.printf("5 - %s\n", replaysDebugging);
            System.out.printf("6 - %s\n", tabuleirosDebugging);
            System.out.println("0 - Voltar");
            int escolha = ValidadorInput.obterInt(0, 6);

            switch (escolha) {
                case 0:
                    return;

                case 1:
                    Main.debugging = !Main.debugging;
                    break;

                case 2:
                    IA.debugging = !IA.debugging;
                    break;

                case 3:
                    Arquivos.debugging = !Arquivos.debugging;
                    break;

                case 4:
                    Jogo.debugging = !Jogo.debugging;
                    break;

                case 5:
                    Replays.debugging = !Replays.debugging;
                    break;

                case 6:
                    Tabuleiros.debugging = !Tabuleiros.debugging;
                    break;

                default:
                    System.out.println("Erro 17");
                    System.exit(17);
                    return;
            }
        }
    }
}
