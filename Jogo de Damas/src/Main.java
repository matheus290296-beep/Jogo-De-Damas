import java.io.IOException;

public class Main {
    static boolean debugging = false;
    static String modoDeJogo = " ";

    public static void main(String[] args) throws InterruptedException, IOException {
        Arquivos.garantirIntegridade();

        int tabuleiroTipo = 1;
        while (true) {
            int escolha = Menus.mostrarMenuPrincipal();
            switch (escolha) {
                case 1:
                    Replays.iniciarGravacao();
                    modoDeJogo = "multiplayer";
                    Jogo.iniciarPartida(tabuleiroTipo);
                    break;

                case 2:
                    Replays.iniciarGravacao();
                    modoDeJogo = "singleplayer";
                    Jogo.iniciarPartida(tabuleiroTipo);
                    break;

                case 3:
                    tabuleiroTipo = Menus.alterarTabuleiroInicial();
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
                    Menus.menuDebugging();
                    break;

                default:
                    System.out.println("Erro! Isso não deveria acontecer!");
                    break;
            }
        }
    }
}