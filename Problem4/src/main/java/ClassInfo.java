import java.util.Arrays;
import java.util.Scanner;

public class ClassInfo {

    private void getInfo() {
        Class<?> aClass = null;
        boolean classNotFound = true;

        while(classNotFound) {
            System.out.println("Enter class name: ");
            String className = new Scanner(System.in).next();
            classNotFound = false;
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
                classNotFound = true;
            }
        }

        System.out.println("Name: " + aClass.getName());
        System.out.println("Simple name: " + aClass.getSimpleName());
        System.out.println();
        System.out.println("Superclass: " + aClass.getSuperclass());
        System.out.println("Package: " + aClass.getPackage());
        System.out.println("Interfaces: " + Arrays.toString(aClass.getInterfaces()));
        System.out.println();
        System.out.println("Is primitive: " + aClass.isPrimitive());
        System.out.println("Is interface: " + aClass.isInterface());
        System.out.println("Is enum: " + aClass.isEnum());
        if(aClass.isEnum()) {
            System.out.println("Enum's constants: " + Arrays.toString(aClass.getEnumConstants()));
        }
        System.out.println("Is annotation: " + aClass.isAnnotation());
        System.out.println("Is member class: " + aClass.isMemberClass());
        System.out.println("Is local class: " + aClass.isLocalClass());
        System.out.println("Is anonymous: " + aClass.isAnonymousClass());
        if(aClass.isLocalClass() || aClass.isAnonymousClass() || aClass.isMemberClass()) {
            System.out.println("The enclosing class: " + aClass.getEnclosingClass());
            System.out.println("The enclosing constructor: " + aClass.getEnclosingConstructor());
            System.out.println("The enclosing method: " + aClass.getEnclosingMethod());
        }
        System.out.println("Is array : " + aClass.isArray());
        System.out.println("Is synthetic: " + aClass.isSynthetic());
        System.out.println();
        System.out.println("Declared constructors: " + Arrays.toString(aClass.getDeclaredConstructors()));
        System.out.println("Constructors: " + Arrays.toString(aClass.getConstructors()));
        System.out.println();
        System.out.println("Declared fields: " + Arrays.toString(aClass.getDeclaredFields()));
        System.out.println("Fields: " + Arrays.toString(aClass.getFields()));
        System.out.println();
        System.out.println("Declared methods: " + Arrays.toString(aClass.getDeclaredMethods()));
        System.out.println("Methods: " + Arrays.toString(aClass.getMethods()));
        System.out.println();
        System.out.println("Declared classes: " + Arrays.toString(aClass.getDeclaredClasses()));
        System.out.println("Declared annotations: " + Arrays.toString(aClass.getDeclaredAnnotations()));
    }

    public static void main(String[] args) {
        new ClassInfo().getInfo();
    }
}
