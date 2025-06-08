package creational;

import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Nested
    @DisplayName("Enum singleton")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EnumSingletonTest {

        private Singleton.EnumSingleton uniqueInstance;

        @BeforeAll
        void setup() {
            uniqueInstance = Singleton.EnumSingleton.UNIQUE_INSTANCE; //direct global access
        }

        @Test
        @DisplayName("should not create new enum instance on serialization")
        void testEnumSerialization() throws IOException, ClassNotFoundException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(uniqueInstance);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Singleton.EnumSingleton instance = (Singleton.EnumSingleton) ois.readObject();

            Assertions.assertSame(uniqueInstance, instance);
        }

        @Test
        @DisplayName("enum singleton do not support clone")
        void testEnumCloning() {
            // uniqueInstance.clone(); uncomment to check error
            // compile time error as clone() is marked protected
            // Even with reflection, it throws clone not supported exception
        }

        @Test
        @DisplayName("enum singleton do not allow creating new enum constant using reflection")
        void testEnumReflection() throws ClassNotFoundException, NoSuchMethodException {
            Class<?> enumSingleton = Class.forName("creational.Singleton$EnumSingleton");
            Constructor<?> declaredConstructor = enumSingleton.getDeclaredConstructor(String.class, int.class);
            declaredConstructor.setAccessible(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                declaredConstructor.newInstance("UNIQUE_INSTANCE_2", 1);
            });

            assertEquals("Cannot reflectively create enum objects", exception.getMessage());
        }

        @Test
        @DisplayName("enum singleton do not allow creating instance using new operator")
        void testNewInstanceCreation() {
            //Singleton.EnumSingleton enumSingleton = new Singleton.EnumSingleton(); uncomment to check error
        }
    }

    @Test
    @DisplayName("should break singleton in multi-threaded environment")
    void testSingletonMultiThreading() throws InterruptedException {
        AtomicReference<Singleton.LazySingleton> instance1 = new AtomicReference<>();
        AtomicReference<Singleton.LazySingleton> instance2 = new AtomicReference<>();
        Thread t1 = new Thread(() -> {
            try {
                instance1.set(Singleton.LazySingleton.getInstance());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread-1");
        Thread t2 = new Thread(() -> {
            try {
                instance2.set(Singleton.LazySingleton.getInstance());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread-2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertNotSame(instance1.get(), instance2.get(), "Singleton is not thread-safe: different instances created!");
    }

    @Nested
    @DisplayName("Lazy singleton")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class LazySingletonTest {

        private Singleton.LazySingleton uniqueInstance;

        @BeforeAll
        void setup() throws InterruptedException {
            uniqueInstance = Singleton.LazySingleton.getInstance(); //direct global access
        }

        @Test
        @DisplayName("should break singleton on serialization") //No handling of serialization in LazySingleton
        void testSingletonSerialization() throws IOException, ClassNotFoundException, InterruptedException {
            uniqueInstance = Singleton.LazySingleton.getInstance(); //direct global access

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(uniqueInstance);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Singleton.LazySingleton instance = (Singleton.LazySingleton) ois.readObject();

            Assertions.assertNotSame(uniqueInstance, instance); //new instance created
        }

        // Explicitly override clone method in order to support cloning and show Singleton break
        // However in normal use case clone method is not accessible directly in class due to its
        // different nature unlike other protected method, it is very unlikely to add clone support for Singleton
        @Test
        @DisplayName("should break singleton on cloning")
        void testSingletonClone() throws InterruptedException {
            uniqueInstance = Singleton.LazySingleton.getInstance(); //direct global access

            Singleton.LazySingleton clone = uniqueInstance.clone();

            Assertions.assertNotSame(uniqueInstance, clone); //new instance created
        }

        @Test
        @DisplayName("should break singleton via Reflection")
            //No handling of reflection in LazySingleton
        void testSingletonReflection() throws NoSuchMethodException, InvocationTargetException,
                InstantiationException, IllegalAccessException, InterruptedException {
            uniqueInstance = Singleton.LazySingleton.getInstance(); //direct global access

            Constructor<? extends Singleton.LazySingleton> declaredConstructor = uniqueInstance.getClass().getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Singleton.LazySingleton newInstance = declaredConstructor.newInstance();

            Assertions.assertNotSame(uniqueInstance, newInstance); //new instance created
        }
    }

    @Nested
    @DisplayName("Eager singleton")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EagerSingletonTest {

        private Singleton.EagerSingleton uniqueInstance;

        @BeforeAll
        void setup() throws InterruptedException {
            uniqueInstance = Singleton.EagerSingleton.getInstance(); //direct global access
        }

        @Test
        @DisplayName("should not break singleton on serialization") //explicit handling of serialization in EagerSingleton
        void testSingletonSerialization() throws IOException, ClassNotFoundException, InterruptedException {
            uniqueInstance = Singleton.EagerSingleton.getInstance(); //direct global access

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(uniqueInstance);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Singleton.EagerSingleton instance = (Singleton.EagerSingleton) ois.readObject();

            Assertions.assertSame(uniqueInstance, instance); //new instance created
        }

        @Test
        @DisplayName("should not break singleton via Reflection") //explicit handling of reflection in EagerSingleton
        void testSingletonReflection() throws NoSuchMethodException {
            uniqueInstance = Singleton.EagerSingleton.getInstance(); //direct global access

            Constructor<? extends Singleton.EagerSingleton> declaredConstructor =
                    uniqueInstance.getClass().getDeclaredConstructor();
            declaredConstructor.setAccessible(true);

            InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                    declaredConstructor::newInstance); //constructor exception is wrapped inside InvocationTargetException

            assertInstanceOf(RuntimeException.class, exception.getCause());
            assertEquals("Use getInstance() method to create instance",
                    exception.getCause().getMessage());
        }
    }
}