package creational;

import java.io.ObjectStreamException;
import java.io.Serializable;

/** This class represent various implementation of Singleton design pattern in Java
 * Singleton pattern => class has only one instance, and provides a global point of access to it
 *
 * @author mohit
 */

public class Singleton {

    public static class LazySingleton implements Serializable, Cloneable {

        private static LazySingleton uniqueInstance; //just declare

        private LazySingleton() {
        } //private constructor to avoid object creating outside of this class

        public static LazySingleton getInstance() throws InterruptedException {
            if (uniqueInstance == null) {      //Not Thread Safe, can lead to more than one instance if used in multithreaded env
                System.out.println("Thread entering if block " + Thread.currentThread().getName());
                Thread.sleep(1000);      //To mimic thread unsafety
                uniqueInstance = new LazySingleton(); //initialise on demand
            }
            return uniqueInstance;
        }

        @Override
        public LazySingleton clone() { //To mimic clone
            try {
                return (LazySingleton) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public static class EagerSingleton implements Serializable{
        //Thread safe
        private static final EagerSingleton uniqueInstance = new EagerSingleton(); //initialise at time of static initializer

        private EagerSingleton() {
            if (uniqueInstance != null) { //Avoid instantiation using Reflection
                throw new RuntimeException("Use getInstance() method to create instance");
            }
        }

        public static EagerSingleton getInstance() {
            return uniqueInstance; //Return same instance
        }

        protected Object readResolve() throws ObjectStreamException {
            return getInstance();
        }
    }

    public static class ThreadSafeSingleton {

        private static ThreadSafeSingleton uniqueInstance; //just declare

        private ThreadSafeSingleton() {
        } //private constructor so only Singleton can instantiate this class

        //Thread safe as each thread require lock to get instance, overhead once the instance is created
        public static synchronized ThreadSafeSingleton getInstance() {
            if (uniqueInstance == null) {
                uniqueInstance = new ThreadSafeSingleton();
            }
            return uniqueInstance;
        }
    }

    public static class DoubleCheckLockSingleton {

        //volatile ensure the value of instance is read from main memory rather than local CPU cache
        private static volatile DoubleCheckLockSingleton uniqueInstance;

        private DoubleCheckLockSingleton() {
        }

        public static DoubleCheckLockSingleton getInstance() {
            if (uniqueInstance == null) { //check if instance is null
                //we only synchronize when instance is null
                synchronized (DoubleCheckLockSingleton.class) { //class level lock static block
                    if (uniqueInstance == null) {
                        uniqueInstance = new DoubleCheckLockSingleton();
                    }
                }
            }
            return uniqueInstance;
        }
    }

    public static class BillPughSingleton {

        private BillPughSingleton() {
        }

        // The static inner class get loaded when it get called first time, not
        // at the time of parent class loading
        // So thread safe + On demand + avoid synchronized code
        private static final class BillPughSingletonHelper {
            private static final BillPughSingleton uniqueInstance = new BillPughSingleton();
        }

        public BillPughSingleton getInstance() {
            return BillPughSingletonHelper.uniqueInstance;
        }
    }

    //Use of enum to create singleton object - Joshua Bloch Effective Java
    /** Use of enum type for Singleton has the following advantages
     * By default, enum has private constructor so no instantiation outside enum possible
     * Creating enum instance using new is compiled time error
     * Clone, Reflection and Serialization are already handled in Enum type
     * Enum constant can be directly accessed using Enum.<enum-name> ensuring global access
     * refer creational.SingletonTest.EnumSingletonTest
     */
    public enum EnumSingleton {
        UNIQUE_INSTANCE
    }
}
