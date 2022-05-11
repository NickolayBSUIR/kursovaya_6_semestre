package thesis.webcryptoexchange.pattern;

public final class Singleton {
    private static Singleton instance;
    public Boolean value;

    private Singleton(Boolean value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.value = value;
    }

    public static Singleton getInstance(Boolean value) {
        if (instance == null) {
            instance = new Singleton(value);
        }
        return instance;
    }
}
