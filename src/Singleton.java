public class Singleton {
    public static int counter;
    private static Singleton singleton = null;

    private Singleton(){

    }

    public static Singleton getInstance(){
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
