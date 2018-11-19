/**
 * Responsible to handle global variables which we know they are bad.
 * This class was not designed to handle heavy logic only simple one.
 */
public class Singleton {
    public static int counter;
    private static Singleton singleton = null;

    /**
     * Initialize private constructor.
     */
    private Singleton(){

    }

    /**
     * It returns a single instance of the class.
     *
     * @return Singleton
     */
    public static Singleton getInstance(){
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
