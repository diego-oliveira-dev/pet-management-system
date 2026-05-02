public class Validador {
    public static boolean isEscolhaValida(String escolha) {
        int escolhaInt = 0;
        try {
            escolhaInt = Integer.parseInt(escolha);
        } catch (Exception e) {
            return false;
        }

        if (escolhaInt < 1 || escolhaInt > 6) {
            return false;
        }

        return true;
    }

}
